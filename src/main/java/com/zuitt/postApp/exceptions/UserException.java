package com.zuitt.postApp.exceptions;

public class UserException extends Exception {

    public UserException(String message){
//        Passing the message to super() to store it as the exception's error message.
        super(message);
    }
}
