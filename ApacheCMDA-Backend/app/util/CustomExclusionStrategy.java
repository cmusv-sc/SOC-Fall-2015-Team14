package util;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.annotations.Expose;

/**
 * Created by dachengwen on 11/17/15.
 * refernece
 * http://www.studytrails.com/java/json/java-google-json-exclusion-strategy.jsp
 * https://google-gson.googlecode.com/svn/trunk/gson/docs/javadocs/com/google/gson/ExclusionStrategy.html
 */
public class CustomExclusionStrategy implements ExclusionStrategy {

    private Class classToExclude;

    public CustomExclusionStrategy(Class classToExclude) {
        this.classToExclude = classToExclude;
    }

    @Override
    public boolean shouldSkipField(FieldAttributes f) {
        //Excludes any field (or class) that is NOT tagged with an "@FooAnnotation"
        return f.getAnnotation(Expose.class) == null;
    }


    @Override
    public boolean shouldSkipClass(Class<?> clazz) {
       return false;
    }

}
