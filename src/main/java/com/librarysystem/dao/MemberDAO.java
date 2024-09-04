package com.librarysystem.dao;

import com.librarysystem.exception.LibrarySystemException;
import com.librarysystem.model.Member;
import com.librarysystem.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MemberDAO {
    public void add(Member member) throws SQLException, LibrarySystemException {
        String sql = "INSERT INTO members (name, email, phone_number, membership_date) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, member.getName());
            pstmt.setString(2, member.getEmail());
            pstmt.setString(3, member.getPhoneNumber());
            pstmt.setDate(4, Date.valueOf(member.getMembershipDate()));
            pstmt.executeUpdate();

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    member.setMemberId(generatedKeys.getInt(1));
                }
            }
        }
    }

    public Member getById(int memberId) throws SQLException {
        String sql = "SELECT * FROM members WHERE member_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, memberId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Member(
                            rs.getInt("member_id"),
                            rs.getString("name"),
                            rs.getString("email"),
                            rs.getString("phone_number"),
                            rs.getDate("membership_date").toLocalDate()
                    );
                }
            }
        } catch (LibrarySystemException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public void update(Member member) throws SQLException, LibrarySystemException {
        String sql = "UPDATE members SET name = ?, email = ?, phone_number = ?, membership_date = ? WHERE member_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, member.getName());
            pstmt.setString(2, member.getEmail());
            pstmt.setString(3, member.getPhoneNumber());
            pstmt.setDate(4, Date.valueOf(member.getMembershipDate()));
            pstmt.setInt(5, member.getMemberId());
            pstmt.executeUpdate();
        }
    }

    public void delete(int memberId) throws SQLException {
        String sql = "DELETE FROM members WHERE member_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, memberId);
            pstmt.executeUpdate();
        } catch (LibrarySystemException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Member> getAll() throws SQLException, LibrarySystemException {
        List<Member> members = new ArrayList<>();
        String sql = "SELECT * FROM members";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                members.add(new Member(
                        rs.getInt("member_id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("phone_number"),
                        rs.getDate("membership_date").toLocalDate()
                ));
            }
        }
        return members;
    }
}