package com.example.librarianmicroservice.service;

import com.example.librarianmicroservice.entity.Book;
import com.example.librarianmicroservice.exception.BookAlreadyPresent;
import com.example.librarianmicroservice.exception.BookNotFound;

import java.util.List;

public interface BookService {
    public Book addBook(Book bookAdded) throws BookAlreadyPresent;

    public Book updateBook(Book newData) throws BookNotFound;

    public Book getBook(int id) throws BookNotFound;

    public String deleteBook(int id) throws BookNotFound;

    public List<Book> getAllBooksWithAuthorNameOrName(String name);
}
