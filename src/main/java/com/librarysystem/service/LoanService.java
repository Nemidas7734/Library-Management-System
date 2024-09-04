package com.librarysystem.service;

import com.librarysystem.dao.LoanDAO;
import com.librarysystem.dao.BookDAO;
import com.librarysystem.dao.MemberDAO;
import com.librarysystem.model.Loan;
import com.librarysystem.model.Book;
import com.librarysystem.model.Member;
import com.librarysystem.exception.LibrarySystemException;

import java.time.LocalDate;
import java.util.List;

public class LoanService {
    private LoanDAO loanDAO;
    private BookDAO bookDAO;
    private MemberDAO memberDAO;

    public LoanService() {
        this.loanDAO = new LoanDAO() {
            @Override
            public void add(Loan loan) {

            }

            @Override
            public Loan getById(int loanId) {
                return null;
            }

            @Override
            public void update(Loan loan) {

            }

            @Override
            public List<Loan> getByMemberId(int memberId) {
                return List.of();
            }

            @Override
            public List<Loan> getAll() {
                return List.of();
            }
        };
        this.bookDAO = new BookDAO();
        this.memberDAO = new MemberDAO() {
            @Override
            public void add(Member member) {

            }

            @Override
            public Member getById(int memberId) {
                return null;
            }

            @Override
            public void update(Member member) {

            }

            @Override
            public void delete(int memberId) {

            }

            @Override
            public List<Member> getAll() {
                return List.of();
            }
        };
    }

    public void issueBook(int bookId, int memberId) throws LibrarySystemException {
        try {
            Book book = bookDAO.getById(bookId);
            Member member = memberDAO.getById(memberId);

            if (book == null) {
                throw new LibrarySystemException("Book not found");
            }
            if (member == null) {
                throw new LibrarySystemException("Member not found");
            }
            if (book.getQuantity() <= 0) {
                throw new LibrarySystemException("Book is out of stock");
            }

            Loan loan = new Loan(0, book, member, LocalDate.now(), null, "issued");
            loanDAO.add(loan);

            book.setQuantity(book.getQuantity() - 1);
            bookDAO.update(book);
        } catch (Exception e) {
            throw new LibrarySystemException("Error issuing book: " + e.getMessage(), e);
        }
    }

    public void returnBook(int loanId) throws LibrarySystemException {
        try {
            Loan loan = loanDAO.getById(loanId);
            if (loan == null) {
                throw new LibrarySystemException("Loan not found");
            }
            if ("returned".equals(loan.getStatus())) {
                throw new LibrarySystemException("Book already returned");
            }

            loan.setReturnDate(LocalDate.now());
            loan.setStatus("returned");
            loanDAO.update(loan);

            Book book = loan.getBook();
            book.setQuantity(book.getQuantity() + 1);
            bookDAO.update(book);
        } catch (Exception e) {
            throw new LibrarySystemException("Error returning book: " + e.getMessage(), e);
        }
    }

    public Loan getLoanById(int loanId) throws LibrarySystemException {
        try {
            return loanDAO.getById(loanId);
        } catch (Exception e) {
            throw new LibrarySystemException("Error getting loan: " + e.getMessage(), e);
        }
    }

    public List<Loan> getLoansByMemberId(int memberId) throws LibrarySystemException {
        try {
            return loanDAO.getByMemberId(memberId);
        } catch (Exception e) {
            throw new LibrarySystemException("Error getting loans for member: " + e.getMessage(), e);
        }
    }

    public List<Loan> getAllLoans() throws LibrarySystemException {
        try {
            return loanDAO.getAll();
        } catch (Exception e) {
            throw new LibrarySystemException("Error getting all loans: " + e.getMessage(), e);
        }
    }
}