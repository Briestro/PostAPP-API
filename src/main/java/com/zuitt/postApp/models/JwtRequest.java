package com.zuitt.postApp.models;

import java.io.Serializable;

public class JwtRequest implements Serializable {

    private static final long serialVersionUID = 1312603338994935267L;

//   Store username and password from the user.
    private String username;
    private String password;

    public JwtRequest(){}

    public JwtRequest(String username, String password){
        this.username = username;
        this.password = password;
    }

    public String getUsername(){
        return  username;
    }

    public void setUsername(){
        this.username = username;
    }

    public String getPassword(){
        return  password;
    }

    public void setPassword(){
        this.password = password;
    }
}
