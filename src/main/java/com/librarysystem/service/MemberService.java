package com.librarysystem.service;

import com.librarysystem.dao.MemberDAO;
import com.librarysystem.model.Member;
import com.librarysystem.exception.LibrarySystemException;

import java.util.List;

public class MemberService {
    private MemberDAO memberDAO;

    public MemberService() {
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

    public void addMember(Member member) throws LibrarySystemException {
        try {
            memberDAO.add(member);
        } catch (Exception e) {
            throw new LibrarySystemException("Error adding member: " + e.getMessage(), e);
        }
    }

    public Member getMemberById(int memberId) throws LibrarySystemException {
        try {
            return memberDAO.getById(memberId);
        } catch (Exception e) {
            throw new LibrarySystemException("Error getting member: " + e.getMessage(), e);
        }
    }

    public void updateMember(Member member) throws LibrarySystemException {
        try {
            memberDAO.update(member);
        } catch (Exception e) {
            throw new LibrarySystemException("Error updating member: " + e.getMessage(), e);
        }
    }

    public void deleteMember(int memberId) throws LibrarySystemException {
        try {
            memberDAO.delete(memberId);
        } catch (Exception e) {
            throw new LibrarySystemException("Error deleting member: " + e.getMessage(), e);
        }
    }

    public List<Member> getAllMembers() throws LibrarySystemException {
        try {
            return memberDAO.getAll();
        } catch (Exception e) {
            throw new LibrarySystemException("Error getting all members: " + e.getMessage(), e);
        }
    }
}