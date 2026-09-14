package com.zuitt.postApp.repositories;

import com.zuitt.postApp.models.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, Object> {
//    "findBy" indicates that we want to retrieve a record.
//    "Username" refers to the property in the User entity.
//    Spring data generates the appropriate database query without requiring to write the SQL manually.
    User findByUsername(String username);
}


/*
* Controller
*
* Service
*
* Repository
*
* Database
* */