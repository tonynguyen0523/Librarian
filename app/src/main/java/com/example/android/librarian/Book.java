package com.example.android.librarian;

/**
 * Created by tonynguyen on 11/30/16.
 */

public class Book {

    // Picture of book
    private String mBookImageUrl;

    // Title of book
    private String mBookTitle;

    // Author of book
    private String mBookAuthor;

    // Constructing book object
    public Book(String bookImageUrl, String title, String author) {
        mBookImageUrl = bookImageUrl;
        mBookTitle = title;
        mBookAuthor = author;
    }

    // Getter method to retrieve book image url
    public String getBookImageUrl() {
        return mBookImageUrl;
    }

    // Getter method to retrieve book title
    public String getBookTitle() {
        return mBookTitle;
    }

    // Getter method to retrieve book author/s
    public String getBookAuthor() {
        return mBookAuthor;
    }


}
