package com.example.testcrud.Util;

import com.example.testcrud.dto.StudentDTO;
import com.example.testcrud.model.Student;

public class StudentMapper {

    public static StudentDTO toDTO(Student student) {
        return new StudentDTO(student.getId(), student.getName(), student.getEmail());
    }

    public static Student toEntity(StudentDTO dto) {
        Student student = new Student();
        student.setId(dto.getId());
        student.setName(dto.getName());
        student.setEmail(dto.getEmail());
        return student;
    }
}
