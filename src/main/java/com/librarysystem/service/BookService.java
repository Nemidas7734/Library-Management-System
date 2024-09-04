package com.librarysystem.service;

import com.librarysystem.dao.BookDAO;
import com.librarysystem.model.Book;
import com.librarysystem.exception.LibrarySystemException;

import java.util.List;

public class BookService {
    private BookDAO bookDAO;

    public BookService() {
        this.bookDAO = new BookDAO();
    }

    public void addBook(Book book) throws LibrarySystemException {
        try {
            bookDAO.add(book);
        } catch (Exception e) {
            throw new LibrarySystemException("Error adding book: " + e.getMessage(), e);
        }
    }

    public Book getBookById(int bookId) throws LibrarySystemException {
        try {
            return bookDAO.getById(bookId);
        } catch (Exception e) {
            throw new LibrarySystemException("Error getting book: " + e.getMessage(), e);
        }
    }

    public void updateBook(Book book) throws LibrarySystemException {
        try {
            bookDAO.update(book);
        } catch (Exception e) {
            throw new LibrarySystemException("Error updating book: " + e.getMessage(), e);
        }
    }

    public void deleteBook(int bookId) throws LibrarySystemException {
        try {
            bookDAO.delete(bookId);
        } catch (Exception e) {
            throw new LibrarySystemException("Error deleting book: " + e.getMessage(), e);
        }
    }

    public List<Book> getAllBooks() throws LibrarySystemException {
        try {
            return bookDAO.getAll();
        } catch (Exception e) {
            throw new LibrarySystemException("Error getting all books: " + e.getMessage(), e);
        }
    }
}