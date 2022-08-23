package com.example.booking.service;

import com.example.booking.entity.Book;
import com.example.booking.entity.BookIssueAndReturn;
import com.example.booking.entity.User;
import com.example.booking.exception.BookAlreadyIssued;
import com.example.booking.exception.FineGreaterThanZero;
import com.example.booking.exception.NoCopiesLeftForIssue;
import com.example.booking.repository.BookIssueAndReturnRepo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.Period;

@Service
public class BookIssueAndReturnServiceImpl implements BookIssueAndReturnService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private BookIssueAndReturnRepo bookIssueAndReturnRepo;

    static Logger logger = LoggerFactory.getLogger(BookIssueAndReturnServiceImpl.class);
    /*
    This method intended to issue book to the user

     */

    /**
     * This method intended to issue book to the user
     * @param userId unique id of
     * @param bookId
     * @return
     * @throws BookAlreadyIssued
     * @throws FineGreaterThanZero
     * @throws NoCopiesLeftForIssue
     */
    public String issueBook(int userId, int bookId) throws BookAlreadyIssued, FineGreaterThanZero, NoCopiesLeftForIssue {
        logger.info("issue book function called successfully for book id "+ bookId +" and userId "+userId);

        // called rest template to get user and book object
        //Book bookObj = bookRepo.findById(bookId).orElse(null);
        Book bookObj = restTemplate.getForObject("http://LIBRARIAN-MICROSERVICE/getBook/"+bookId, Book.class);
        User userObj = restTemplate.getForObject("http://USER-MICROSERVICE/getUser/"+userId, User.class);
        // check pending fine, if fine of the user is greater than 0 then don't issue the book
        if (userObj.getFine()>0) {
            logger.info("for user userId "+userId+ " book not issued because previous fine is pending");
            throw new FineGreaterThanZero("Please pay fine first");
        }
        // code for checking if book already issued to the user
        BookIssueAndReturn act = bookIssueAndReturnRepo.findActivity(userId, bookId);
        if (act!=null) {

            logger.info("for user with id " + userId + " book already issued of book id " + bookId);
            throw new BookAlreadyIssued("can't re-issue same book without returning");
        }
        act = new BookIssueAndReturn();
        // code for checking number of copies available.
        if (bookObj.getNumberOfCopies()>0) {
            // code for setting necessary details of book issue and user in database
            act.setBookId(bookId);
            act.setRegistrationNumber(userId);
            // localDate.now to get current data(issue date)
            act.setStartDate(LocalDate.now());
            // localDate.now().plusDays(7) to get +7 days to set return date in database
            act.setEndDate(LocalDate.now().plusDays(7));
            bookObj.setNumberOfCopies(bookObj.getNumberOfCopies()-1);
            // calling update controller for book
            restTemplate.put("http://LIBRARIAN-MICROSERVICE/update", Book.class);
            //bookRepo.save(bookObj);
            bookIssueAndReturnRepo.save(act);
            logger.info("book issued successfully");
            return "book issued successfully";
        }
        logger.info("No copies of book with id"+ bookId + "left");
        throw new NoCopiesLeftForIssue("No copies left for issue");
    }

    /*
        This method  intend to return already issued book and set fine to the user is book return late
        @Param userId is unique id of the user, bookId is unique id of book
     */
    public String returnBook(int userId, int bookId) {

        logger.info("return function called successfully for user "+ userId+ " book id "+ bookId);
        // code for getting data of book issued from database
        BookIssueAndReturn act = bookIssueAndReturnRepo.findActivity(userId, bookId);
        // code for setting fine if actual return date is greater than expected return date
        if (LocalDate.now().isAfter(act.getEndDate())) {
          // code for getting difference in days
          Period days = Period.between(act.getEndDate(),LocalDate.now());
          int diff = Math.abs(days.getDays());
          User user = restTemplate.getForObject("http://USER-MICROSERVICE/getUser/"+userId, User.class);
          logger.info("fine of rupees " +diff*20);
          user.setFine(diff*20);
        }
        // code for deleting details of issued book from database
        bookIssueAndReturnRepo.delete(act);
        // code for getting book object from database
        Book book = restTemplate.getForObject("http://LIBRARIAN-MICROSERVICE/getBook/"+bookId, Book.class);
        // code for increase book count in book database
        book.setNumberOfCopies(book.getNumberOfCopies()+1);
        logger.info("book returned successfully");
        return "returned successfully";
    }
}
