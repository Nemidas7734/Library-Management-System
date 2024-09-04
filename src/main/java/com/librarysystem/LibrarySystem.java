package com.librarysystem;

import com.librarysystem.model.Book;
import com.librarysystem.model.Member;
import com.librarysystem.model.Loan;
import com.librarysystem.service.BookService;
import com.librarysystem.service.MemberService;
import com.librarysystem.service.LoanService;
import com.librarysystem.exception.LibrarySystemException;

import java.util.Scanner;
import java.util.List;
import java.time.LocalDate;

public class LibrarySystem {
    private static final Scanner scanner = new Scanner(System.in);
    private static final BookService bookService = new BookService();
    private static final MemberService memberService = new MemberService();
    private static final LoanService loanService = new LoanService();

    public static void main(String[] args) {
        while (true) {
            displayMainMenu();
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            try {
                switch (choice) {
                    case 1:
                        handleBookManagement();
                        break;
                    case 2:
                        handleMemberManagement();
                        break;
                    case 3:
                        handleLoanManagement();
                        break;
                    case 4:
                        System.out.println("Exiting the system. Goodbye!");
                        System.exit(0);
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            } catch (LibrarySystemException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private static void displayMainMenu() {
        System.out.println("\n--- Library Management System ---");
        System.out.println("1. Book Management");
        System.out.println("2. Member Management");
        System.out.println("3. Loan Management");
        System.out.println("4. Exit");
        System.out.print("Enter your choice: ");
    }

    private static void handleBookManagement() throws LibrarySystemException {
        while (true) {
            System.out.println("\n--- Book Management ---");
            System.out.println("1. Add a new book");
            System.out.println("2. View book details");
            System.out.println("3. Update book information");
            System.out.println("4. Delete a book");
            System.out.println("5. Return to main menu");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    addBook();
                    break;
                case 2:
                    viewBook();
                    break;
                case 3:
                    updateBook();
                    break;
                case 4:
                    deleteBook();
                    break;
                case 5:
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void addBook() throws LibrarySystemException {
        System.out.println("Enter book details:");
        System.out.print("Title: ");
        String title = scanner.nextLine();
        System.out.print("Author: ");
        String author = scanner.nextLine();
        System.out.print("Genre: ");
        String genre = scanner.nextLine();
        System.out.print("Publication Year: ");
        int publicationYear = scanner.nextInt();
        System.out.print("Quantity: ");
        int quantity = scanner.nextInt();

        Book book = new Book(0, title, author, genre, publicationYear, quantity);
        bookService.addBook(book);
        System.out.println("Book added successfully.");
    }

    private static void viewBook() throws LibrarySystemException {
        System.out.print("Enter book ID: ");
        int bookId = scanner.nextInt();
        Book book = bookService.getBookById(bookId);
        if (book != null) {
            System.out.println(book);
        } else {
            System.out.println("Book not found.");
        }
    }

    private static void updateBook() throws LibrarySystemException {
        System.out.print("Enter book ID to update: ");
        int bookId = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        Book book = bookService.getBookById(bookId);
        if (book != null) {
            System.out.println("Enter new details (press enter to keep current value):");
            System.out.print("Title (" + book.getTitle() + "): ");
            String title = scanner.nextLine();
            if (!title.isEmpty()) book.setTitle(title);

            System.out.print("Author (" + book.getAuthor() + "): ");
            String author = scanner.nextLine();
            if (!author.isEmpty()) book.setAuthor(author);

            System.out.print("Genre (" + book.getGenre() + "): ");
            String genre = scanner.nextLine();
            if (!genre.isEmpty()) book.setGenre(genre);

            System.out.print("Publication Year (" + book.getPublicationYear() + "): ");
            String yearStr = scanner.nextLine();
            if (!yearStr.isEmpty()) book.setPublicationYear(Integer.parseInt(yearStr));

            System.out.print("Quantity (" + book.getQuantity() + "): ");
            String quantityStr = scanner.nextLine();
            if (!quantityStr.isEmpty()) book.setQuantity(Integer.parseInt(quantityStr));

            bookService.updateBook(book);
            System.out.println("Book updated successfully.");
        } else {
            System.out.println("Book not found.");
        }
    }

    private static void deleteBook() throws LibrarySystemException {
        System.out.print("Enter book ID to delete: ");
        int bookId = scanner.nextInt();
        bookService.deleteBook(bookId);
        System.out.println("Book deleted successfully.");
    }

    private static void handleMemberManagement() throws LibrarySystemException {
        // Similar implementation to handleBookManagement, but for Member operations
    }

    private static void handleLoanManagement() throws LibrarySystemException {
        while (true) {
            System.out.println("\n--- Loan Management ---");
            System.out.println("1. Issue a book");
            System.out.println("2. Return a book");
            System.out.println("3. View loan details");
            System.out.println("4. List all loans for a member");
            System.out.println("5. Return to main menu");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    issueBook();
                    break;
                case 2:
                    returnBook();
                    break;
                case 3:
                    viewLoan();
                    break;
                case 4:
                    listMemberLoans();
                    break;
                case 5:
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void issueBook() throws LibrarySystemException {
        System.out.print("Enter book ID: ");
        int bookId = scanner.nextInt();
        System.out.print("Enter member ID: ");
        int memberId = scanner.nextInt();

        loanService.issueBook(bookId, memberId);
        System.out.println("Book issued successfully.");
    }

    private static void returnBook() throws LibrarySystemException {
        System.out.print("Enter loan ID: ");
        int loanId = scanner.nextInt();

        loanService.returnBook(loanId);
        System.out.println("Book returned successfully.");
    }

    private static void viewLoan() throws LibrarySystemException {
        System.out.print("Enter loan ID: ");
        int loanId = scanner.nextInt();
        Loan loan = loanService.getLoanById(loanId);
        if (loan != null) {
            System.out.println(loan);
        } else {
            System.out.println("Loan not found.");
        }
    }

    private static void listMemberLoans() throws LibrarySystemException {
        System.out.print("Enter member ID: ");
        int memberId = scanner.nextInt();
        List<Loan> loans = loanService.getLoansByMemberId(memberId);
        if (!loans.isEmpty()) {
            for (Loan loan : loans) {
                System.out.println(loan);
            }
        } else {
            System.out.println("No loans found for this member.");
        }
    }
}