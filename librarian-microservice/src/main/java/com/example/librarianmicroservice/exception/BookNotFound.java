package com.example.librarianmicroservice.exception;

public class BookNotFound extends Exception{

    public BookNotFound(String msg) {

        super(msg);
    }
}
