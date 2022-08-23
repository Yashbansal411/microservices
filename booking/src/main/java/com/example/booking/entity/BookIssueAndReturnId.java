package com.example.booking.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookIssueAndReturnId implements Serializable {

    private int registrationNumber;
    private int bookId;
}
