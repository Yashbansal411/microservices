package com.example.booking.service;

import com.example.booking.exception.BookAlreadyIssued;
import com.example.booking.exception.FineGreaterThanZero;
import com.example.booking.exception.NoCopiesLeftForIssue;

public interface BookIssueAndReturnService {
    public String issueBook(int userId, int bookId) throws BookAlreadyIssued, FineGreaterThanZero, NoCopiesLeftForIssue;

    public String returnBook(int userId, int bookId);
}
