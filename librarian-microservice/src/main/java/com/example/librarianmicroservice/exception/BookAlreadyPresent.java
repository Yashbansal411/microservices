package com.example.librarianmicroservice.exception;

public class BookAlreadyPresent extends Exception{

    public BookAlreadyPresent(String msg) {

        super(msg);
    }
}
