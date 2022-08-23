package com.example.usermicroservice.exception;

public class UserAlreadyPresent extends Exception{

    public UserAlreadyPresent(String msg) {

        super(msg);
    }
}
