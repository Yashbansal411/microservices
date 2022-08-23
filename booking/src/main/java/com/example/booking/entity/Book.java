package com.example.booking.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
public class Book {

    @Id
    private int bookId;
    @NotBlank
    private String bookName;
    @NotEmpty(message = "Author name can't be empty")
    private String authorName;
    @Min(value=1, message = "number of copies can't be less than 1")
    private int numberOfCopies;
}
