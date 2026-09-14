package com.zuitt.postApp.models;

import jakarta.persistence.*;

// This entity represents a table in the database.
@Entity

// Specifies the name of the database table that this entity represent.
@Table(name = "posts")
public class Post {

    //Uniquely identifies the primary key column in the table
    @Id
    // Tells JPA that the value of the primary key should be generated automatically by the database.
    // This will also use the database's auto-increment feature to generate unique ID for each new record.
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // This property maps to a table column in the database.
        // property name should correspond on the actual column name.
    @Column
    private String title;

    @Column
    private String content;


    @ManyToOne
//    @JoinColumn specifies the foreign key column used to connect Post table to the User table.
//    nullable=false ensures that every Post must have a User.
    @JoinColumn(name = "user_id", nullable = false)

//    Stores the User associated with this Post
    private User user;

    public User getUser(){
        return user;
    }

    public void setUser(User user){
        this.user = user;
    }

    //Constructor
    // JPA requires an empty constructor to create an entity object when retrieving records from the database.
    public Post(){}

    // This allows us to easily create a Post object by providing its title and content.
    public Post(String title, String content){
        this.title = title;
        this.content = content;
    }

    // Getter and Setters method
    public String getTitle(){
        return title;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public String getContent(){
        return content;
    }

    public void setContent(String content){
        this.content = content;
    }
}
