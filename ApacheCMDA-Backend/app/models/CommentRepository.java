package models;

import org.springframework.data.repository.CrudRepository;

import javax.inject.Named;
import javax.inject.Singleton;
import java.util.List;

/**
 * Created by tonyfu on 11/15/15.
 */
@Named
@Singleton
public interface CommentRepository extends CrudRepository<Comment, Long> {
    List<Comment> findByPostOrderByTimeDesc(Post post);
    Comment findOne(Long Id);
}
