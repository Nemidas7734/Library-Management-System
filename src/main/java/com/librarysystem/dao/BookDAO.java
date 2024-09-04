package com.librarysystem.dao;

import com.librarysystem.exception.LibrarySystemException;
import com.librarysystem.model.Book;
import com.librarysystem.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookDAO {
    public void add(Book book) throws SQLException {
        String sql = "INSERT INTO books (title, author, genre, publication_year, quantity) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, book.getTitle());
            pstmt.setString(2, book.getAuthor());
            pstmt.setString(3, book.getGenre());
            pstmt.setInt(4, book.getPublicationYear());
            pstmt.setInt(5, book.getQuantity());
            pstmt.executeUpdate();

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    book.setBookId(generatedKeys.getInt(1));
                }
            }
        } catch (LibrarySystemException e) {
            throw new RuntimeException(e);
        }
    }

    public Book getById(int bookId) throws SQLException, LibrarySystemException {
        String sql = "SELECT * FROM books WHERE book_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, bookId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Book(
                            rs.getInt("book_id"),
                            rs.getString("title"),
                            rs.getString("author"),
                            rs.getString("genre"),
                            rs.getInt("publication_year"),
                            rs.getInt("quantity")
                    );
                }
            }
        }
        return null;
    }

    public void update(Book book) throws SQLException {
        String sql = "UPDATE books SET title = ?, author = ?, genre = ?, publication_year = ?, quantity = ? WHERE book_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, book.getTitle());
            pstmt.setString(2, book.getAuthor());
            pstmt.setString(3, book.getGenre());
            pstmt.setInt(4, book.getPublicationYear());
            pstmt.setInt(5, book.getQuantity());
            pstmt.setInt(6, book.getBookId());
            pstmt.executeUpdate();
        } catch (LibrarySystemException e) {
            throw new RuntimeException(e);
        }
    }

    public void delete(int bookId) throws SQLException, LibrarySystemException {
        String sql = "DELETE FROM books WHERE book_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, bookId);
            pstmt.executeUpdate();
        }
    }

    public List<Book> getAll() throws SQLException {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT * FROM books";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                books.add(new Book(
                        rs.getInt("book_id"),
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getString("genre"),
                        rs.getInt("publication_year"),
                        rs.getInt("quantity")
                ));
            }
        } catch (LibrarySystemException e) {
            throw new RuntimeException(e);
        }
        return books;
    }
}