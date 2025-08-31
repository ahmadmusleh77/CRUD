package com.example.testcrud.controller;

import com.example.testcrud.dto.StudentDTO;
import com.example.testcrud.exception.ResourceNotFoundException;
import com.example.testcrud.Warpper.ResponseWrapper;
import com.example.testcrud.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    @Autowired
    private StudentService studentService;

    // Generic helper method to build ResponseEntity with ResponseWrapper
    private <T> ResponseEntity<ResponseWrapper<T>> buildResponse(HttpStatus status, String message, T data) {
        ResponseWrapper<T> response = new ResponseWrapper<>(status.value(), message, data);
        return new ResponseEntity<>(response, status);
    }

    // Create a new student
    @PostMapping
    public ResponseEntity<ResponseWrapper<StudentDTO>> createStudent(@Valid @RequestBody StudentDTO studentDTO) {
        StudentDTO saved = studentService.createStudent(studentDTO);
        return buildResponse(HttpStatus.CREATED, "Student created successfully", saved);
    }

    // Get all students using Optional
    @GetMapping
    public ResponseEntity<ResponseWrapper<List<StudentDTO>>> getAllStudents() {
        Optional<List<StudentDTO>> optionalStudents = studentService.getAllStudents();

        if (optionalStudents.isEmpty()) {
            return buildResponse(HttpStatus.OK, "No students found", null);
        } else {
            return buildResponse(HttpStatus.OK, "List of all students", optionalStudents.get());
        }
    }

    // Get student by ID
    @GetMapping("/{id}")
    public ResponseEntity<ResponseWrapper<StudentDTO>> getStudentById(@PathVariable Long id) {
        try {
            StudentDTO student = studentService.getStudentById(id);
            return buildResponse(HttpStatus.OK, "Student found", student);
        } catch (ResourceNotFoundException e) {
            return buildResponse(HttpStatus.NOT_FOUND, "Student not found with id " + id, null);
        }
    }

    // Update student
    @PutMapping("/{id}")
    public ResponseEntity<ResponseWrapper<StudentDTO>> updateStudent(@PathVariable Long id, @Valid @RequestBody StudentDTO studentDTO) {
        try {
            StudentDTO updated = studentService.updateStudent(id, studentDTO);
            return buildResponse(HttpStatus.OK, "Student updated successfully", updated);
        } catch (ResourceNotFoundException e) {
            return buildResponse(HttpStatus.NOT_FOUND, "Student not found with id " + id, null);
        } catch (Exception e) {
            return buildResponse(HttpStatus.BAD_REQUEST, "Invalid request", null);
        }
    }

    // Delete student
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseWrapper<Void>> deleteStudent(@PathVariable Long id) {
        try {
            studentService.deleteStudent(id);
            return buildResponse(HttpStatus.OK, "Student deleted successfully", null);
        } catch (ResourceNotFoundException e) {
            return buildResponse(HttpStatus.NOT_FOUND, "Student not found with id " + id, null);
        }
    }

    // Search student by email or create a new one if not found
    @PostMapping("/check-email")
    public ResponseEntity<ResponseWrapper<StudentDTO>> checkEmail(@RequestParam String email,
                                                                  @RequestParam(required = false) String name) {
        StudentDTO student = studentService.getStudentByEmail(email);

        if (student != null) {
            return buildResponse(HttpStatus.OK, "Student found by email", student);
        }

        if (name == null || name.isEmpty()) {
            name = "New Student";
        }

        StudentDTO newStudent = new StudentDTO(null, name, email);
        StudentDTO saved = studentService.createStudent(newStudent);
        return buildResponse(HttpStatus.CREATED, "New student created with email", saved);
    }

    // Search students by name (contains keyword)
    @GetMapping("/search")
    public ResponseEntity<ResponseWrapper<List<StudentDTO>>> searchByName(@RequestParam String name) {
        List<StudentDTO> students = studentService.searchByName(name);
        return buildResponse(HttpStatus.OK, "Students found containing name: " + name, students);
    }

    // Count students by name
    @GetMapping("/count")
    public ResponseEntity<ResponseWrapper<Long>> countByName(@RequestParam String name) {
        Long count = studentService.countByName(name);
        return buildResponse(HttpStatus.OK, "Count of students with name: " + name, count);
    }

    // Find students by name prefix
    @GetMapping("/prefix")
    public ResponseEntity<ResponseWrapper<List<StudentDTO>>> findByPrefix(@RequestParam String prefix) {
        List<StudentDTO> students = studentService.findByPrefix(prefix);
        return buildResponse(HttpStatus.OK, "Students found with prefix: " + prefix, students);
    }

    // Find students by email domain
    @GetMapping("/domain")
    public ResponseEntity<ResponseWrapper<List<StudentDTO>>> findByDomain(@RequestParam String domain) {
        List<StudentDTO> students = studentService.findByDomain(domain);
        return buildResponse(HttpStatus.OK, "Students with email domain: " + domain, students);
    }

    // Get all students sorted by name ASC
    @GetMapping("/sorted")
    public ResponseEntity<ResponseWrapper<List<StudentDTO>>> getAllSorted() {
        List<StudentDTO> students = studentService.getAllSortedByName();
        return buildResponse(HttpStatus.OK, "All students sorted by name ASC", students);
    }
}
