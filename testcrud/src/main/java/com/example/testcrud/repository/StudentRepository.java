package com.example.testcrud.repository;

import com.example.testcrud.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    // Find a student by email
    Student findByEmail(String email);



    // Find students whose name contains the given string (case-insensitive)
    @Query("SELECT s FROM Student s WHERE LOWER(s.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Student> findByNameContaining(@Param("name") String name);


    // Count students with the given name
    @Query("SELECT COUNT(s) FROM Student s WHERE s.name = :name")
    Long countByName(@Param("name") String name);



    // Find students whose name starts with the given prefix
    @Query("SELECT s FROM Student s WHERE s.name LIKE CONCAT(:prefix, '%')")
    List<Student> findByNameStartingWith(@Param("prefix") String prefix);



    // Find students whose email ends with the given domain (e.g., gmail.com)
    @Query("SELECT s FROM Student s WHERE s.email LIKE CONCAT('%', :domain)")
    List<Student> findByEmailEndingWith(@Param("domain") String domain);




    // Get all students ordered by name ascending
    @Query("SELECT s FROM Student s ORDER BY s.name ASC")
    List<Student> findAllOrderByNameAsc();}