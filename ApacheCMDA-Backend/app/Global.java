/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.hibernate3.HibernateExceptionTranslator;
import org.springframework.orm.jpa.JpaTransactionManager;

import play.Application;
import play.GlobalSettings;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import static play.core.j.JavaResults.BadRequest;
import static play.core.j.JavaResults.InternalServerError;
import static play.core.j.JavaResults.NotFound;

import java.util.ArrayList;
import java.util.List;

import play.api.mvc.Results.Status;
import play.libs.F.Promise;
import play.libs.Scala;
import play.mvc.Action;
import play.mvc.Http;
import play.mvc.Result;
import scala.Tuple2;
import scala.collection.Seq;

/**
 * Application wide behaviour. We establish a Spring application context for the dependency injection system and
 * configure Spring Data.
 */
public class Global extends GlobalSettings {

    /**
     * The name of the persistence unit we will be using.
     */
    static final String DEFAULT_PERSISTENCE_UNIT = "default";
    
    /**
     * Declare the application context to be used - a Java annotation based application context requiring no XML.
     */
    final private AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();


    /**
     * Sync the context lifecycle with Play's.
     */
    @Override
    public void onStart(final Application app) {
        super.onStart(app);

        // AnnotationConfigApplicationContext can only be refreshed once, but we do it here even though this method
        // can be called multiple times. The reason for doing during startup is so that the Play configuration is
        // entirely available to this application context.
        ctx.register(SpringDataJpaConfiguration.class);
        ctx.scan("controllers", "models");
        ctx.refresh();

        // This will construct the beans and call any construction lifecycle methods e.g. @PostConstruct
        ctx.start();
    }
    /**
     * Sync the context lifecycle with Play's.
     */
    @Override
    public void onStop(final Application app) {
        // This will call any destruction lifecycle methods and then release the beans e.g. @PreDestroy
        ctx.close();

        super.onStop(app);
    }

    /**
     * Controllers must be resolved through the application context. There is a special method of GlobalSettings
     * that we can override to resolve a given controller. This resolution is required by the Play router.
     */
    @Override
    public <A> A getControllerInstance(Class<A> aClass) {
        return ctx.getBean(aClass);
    }

    /**
     * This configuration establishes Spring Data concerns including those of JPA.
     */
    @Configuration
    @EnableJpaRepositories("models")
    public static class SpringDataJpaConfiguration {

        @Bean
        public EntityManagerFactory entityManagerFactory() {
            return Persistence.createEntityManagerFactory(DEFAULT_PERSISTENCE_UNIT);
        }

        @Bean
        public HibernateExceptionTranslator hibernateExceptionTranslator() {
            return new HibernateExceptionTranslator();
        }

        @Bean
        public JpaTransactionManager transactionManager() {
            return new JpaTransactionManager();
        }
    }

    private class ActionWrapper extends Action.Simple {
        public ActionWrapper(Action<?> action) {
            this.delegate = action;
        }

        @Override
        public Promise<Result> call(Http.Context ctx) throws java.lang.Throwable {
            Promise<Result> result = this.delegate.call(ctx);
            Http.Response response = ctx.response();
            response.setHeader("Access-Control-Allow-Origin", "*");
            return result;
        }
    }
    /**
     * Adds the required CORS header "Access-Control-Allow-Origin" to successfull requests
     */
    @Override
    public Action<?> onRequest(Http.Request request, java.lang.reflect.Method actionMethod) {
        return new ActionWrapper(super.onRequest(request, actionMethod));
    }

    private static class CORSResult implements Result {
        final private play.api.mvc.Result wrappedResult;

        public CORSResult(Status status) {
            List<Tuple2<String, String>> list = new ArrayList<Tuple2<String, String>>();
            Tuple2<String, String> t = new Tuple2<String, String>("Access-Control-Allow-Origin","*");
            list.add(t);
            Seq<Tuple2<String, String>> seq = Scala.toSeq(list);
            wrappedResult = status.withHeaders(seq);
        }

        public play.api.mvc.Result toScala() {
            return this.wrappedResult;
        }
    }

    /**
     * Adds the required CORS header "Access-Control-Allow-Origin" to bad requests
     */
    @Override
    public Promise<Result> onBadRequest(Http.RequestHeader request, String error) {
        return Promise.<Result>pure(new CORSResult(BadRequest()));
    }

    /**
     * Adds the required CORS header "Access-Control-Allow-Origin" to requests that causes an exception
     */
    @Override
    public Promise<Result> onError(Http.RequestHeader request, Throwable t) {
        return Promise.<Result>pure(new CORSResult(InternalServerError()));
    }

    /**
     * Adds the required CORS header "Access-Control-Allow-Origin" when a route was not found
     */
    @Override
    public Promise<Result> onHandlerNotFound(Http.RequestHeader request) {
        return Promise.<Result>pure(new CORSResult(NotFound()));
    }
}
