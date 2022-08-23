package com.example.librarianmicroservice.exception;

public class NoCopiesLeftForIssue extends Exception {

    public NoCopiesLeftForIssue(String msg) {

        super(msg);
    }
}
