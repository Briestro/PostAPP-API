package com.zuitt.postApp.repositories;
import com.zuitt.postApp.models.Post;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

// That this component is responsible for interacting with the database and performing data access operations
@Repository

// By extending CrudRepository, this interface automatically inherits the pre-defined CRUD (Create, Read, Update, and Delete) methods that a repository can use.
public interface PostRepository extends CrudRepository<Post, Object> {
}
