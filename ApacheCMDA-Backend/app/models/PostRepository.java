package models;

/**
 * Created by dachengwen on 11/3/15.
 */

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import javax.inject.Named;
import javax.inject.Singleton;

/**
 * Provides CRUD functionality for accessing people. Spring Data auto-magically takes care of many standard
 * operations here.
 */
@Named
@Singleton
public interface PostRepository extends CrudRepository<Post, Long> {
    List<Post> findByUserOrderByTimeDesc(User user);
    List<Post> findBySharedUsersOrderByTimeDesc(User user);
}

