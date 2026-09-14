package com.zuitt.postApp.models;

import java.io.Serializable;

public class JwtResponse implements Serializable {

    private static final long serialVersionUID = -3793156500380407593L;

    private final String jwttoken;

    public JwtResponse(String jwttoken){
        this.jwttoken = jwttoken;
    }

    public String getJwttoken(){
        return  jwttoken;
    }

}
