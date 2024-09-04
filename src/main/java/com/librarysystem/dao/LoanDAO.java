package com.librarysystem.dao;

import com.librarysystem.exception.LibrarySystemException;
import com.librarysystem.model.Loan;
import com.librarysystem.model.Book;
import com.librarysystem.model.Member;
import com.librarysystem.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LoanDAO {
    public void add(Loan loan) throws SQLException, LibrarySystemException {
        String sql = "INSERT INTO loans (book_id, member_id, issue_date, return_date, status) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setInt(1, loan.getBook().getBookId());
            pstmt.setInt(2, loan.getMember().getMemberId());
            pstmt.setDate(3, Date.valueOf(loan.getIssueDate()));
            pstmt.setDate(4, loan.getReturnDate() != null ? Date.valueOf(loan.getReturnDate()) : null);
            pstmt.setString(5, loan.getStatus());
            pstmt.executeUpdate();

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    loan.setLoanId(generatedKeys.getInt(1));
                }
            }
        }
    }

    public Loan getById(int loanId) throws SQLException, LibrarySystemException {
        String sql = "SELECT l.*, b.*, m.* FROM loans l " +
                "JOIN books b ON l.book_id = b.book_id " +
                "JOIN members m ON l.member_id = m.member_id " +
                "WHERE l.loan_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, loanId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Book book = new Book(
                            rs.getInt("b.book_id"),
                            rs.getString("b.title"),
                            rs.getString("b.author"),
                            rs.getString("b.genre"),
                            rs.getInt("b.publication_year"),
                            rs.getInt("b.quantity")
                    );
                    Member member = new Member(
                            rs.getInt("m.member_id"),
                            rs.getString("m.name"),
                            rs.getString("m.email"),
                            rs.getString("m.phone_number"),
                            rs.getDate("m.membership_date").toLocalDate()
                    );
                    return new Loan(
                            rs.getInt("l.loan_id"),
                            book,
                            member,
                            rs.getDate("l.issue_date").toLocalDate(),
                            rs.getDate("l.return_date") != null ? rs.getDate("l.return_date").toLocalDate() : null,
                            rs.getString("l.status")
                    );
                }
            }
        }
        return null;
    }

    public void update(Loan loan) throws SQLException {
        String sql = "UPDATE loans SET book_id = ?, member_id = ?, issue_date = ?, return_date = ?, status = ? WHERE loan_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, loan.getBook().getBookId());
            pstmt.setInt(2, loan.getMember().getMemberId());
            pstmt.setDate(3, Date.valueOf(loan.getIssueDate()));
            pstmt.setDate(4, loan.getReturnDate() != null ? Date.valueOf(loan.getReturnDate()) : null);
            pstmt.setString(5, loan.getStatus());
            pstmt.setInt(6, loan.getLoanId());
            pstmt.executeUpdate();
        } catch (LibrarySystemException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Loan> getByMemberId(int memberId) throws SQLException, LibrarySystemException {
        List<Loan> loans = new ArrayList<>();
        String sql = "SELECT l.*, b.*, m.* FROM loans l " +
                "JOIN books b ON l.book_id = b.book_id " +
                "JOIN members m ON l.member_id = m.member_id " +
                "WHERE l.member_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, memberId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Book book = new Book(
                            rs.getInt("b.book_id"),
                            rs.getString("b.title"),
                            rs.getString("b.author"),
                            rs.getString("b.genre"),
                            rs.getInt("b.publication_year"),
                            rs.getInt("b.quantity")
                    );
                    Member member = new Member(
                            rs.getInt("m.member_id"),
                            rs.getString("m.name"),
                            rs.getString("m.email"),
                            rs.getString("m.phone_number"),
                            rs.getDate("m.membership_date").toLocalDate()
                    );
                    loans.add(new Loan(
                            rs.getInt("l.loan_id"),
                            book,
                            member,
                            rs.getDate("l.issue_date").toLocalDate(),
                            rs.getDate("l.return_date") != null ? rs.getDate("l.return_date").toLocalDate() : null,
                            rs.getString("l.status")
                    ));
                }
            }
        }
        return loans;
    }

    public List<Loan> getAll() throws SQLException {
        List<Loan> loans = new ArrayList<>();
        String sql = "SELECT l.*, b.*, m.* FROM loans l " +
                "JOIN books b ON l.book_id = b.book_id " +
                "JOIN members m ON l.member_id = m.member_id";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Book book = new Book(
                        rs.getInt("b.book_id"),
                        rs.getString("b.title"),
                        rs.getString("b.author"),
                        rs.getString("b.genre"),
                        rs.getInt("b.publication_year"),
                        rs.getInt("b.quantity")
                );
                Member member = new Member(
                        rs.getInt("m.member_id"),
                        rs.getString("m.name"),
                        rs.getString("m.email"),
                        rs.getString("m.phone_number"),
                        rs.getDate("m.membership_date").toLocalDate()
                );
                loans.add(new Loan(
                        rs.getInt("l.loan_id"),
                        book,
                        member,
                        rs.getDate("l.issue_date").toLocalDate(),
                        rs.getDate("l.return_date") != null ? rs.getDate("l.return_date").toLocalDate() : null,
                        rs.getString("l.status")
                ));
            }
        } catch (LibrarySystemException e) {
            throw new RuntimeException(e);
        }
        return loans;
    }
}