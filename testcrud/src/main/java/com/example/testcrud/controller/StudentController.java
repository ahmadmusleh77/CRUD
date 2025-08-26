package com.example.testcrud.controller;

import com.example.testcrud.exception.ResourceNotFoundException;
import com.example.testcrud.model.ApiResponse;
import com.example.testcrud.model.Student;
import com.example.testcrud.service.StudentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api/students")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    // Create a new student
    @PostMapping
    public ResponseEntity<ApiResponse> createStudent(@Valid @RequestBody Student student) {
        Student saved = studentService.createStudent(student);
        return new ResponseEntity<>(
                new ApiResponse(201, "Student created successfully", saved),
                HttpStatus.CREATED
        );
    }

    // Get all students
    @GetMapping
    public ResponseEntity<ApiResponse> getAllStudents() {
        List<Student> students = studentService.getAllStudents();
        String message = students.isEmpty() ? "No students found" : "List of all students";
        return new ResponseEntity<>(
                new ApiResponse(200, message, students),
                HttpStatus.OK
        );
    }

    // Get student by ID
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getStudentById(@PathVariable Long id) {
        try {
            Student student = studentService.getStudentById(id);
            return new ResponseEntity<>(
                    new ApiResponse(200, "Student found", student),
                    HttpStatus.OK
            );
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(
                    new ApiResponse(404, "Student not found with id "  + id, Map.of("id", id)),
                    HttpStatus.NOT_FOUND
            );
        }
    }

    // Get student by email
    @GetMapping("/email/{email}")
    public ResponseEntity<ApiResponse> getStudentByEmail(@PathVariable String email) {
        try {
            Student student = studentService.getStudentByEmail(email);
            return new ResponseEntity<>(
                    new ApiResponse(200, "Student found by email", student),
                    HttpStatus.OK
            );
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(
                    new ApiResponse(404, "Student not found with this email", Map.of("email", email)),
                    HttpStatus.NOT_FOUND
            );
        }
    }

    // Update student
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> updateStudent(@PathVariable Long id, @Valid @RequestBody Student student) {
        try {
            Student updated = studentService.updateStudent(id, student);
            return new ResponseEntity<>(
                    new ApiResponse(200, "Student updated successfully", updated),
                    HttpStatus.OK
            );
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(
                    new ApiResponse(404, "Student not found", Map.of("id", id)),
                    HttpStatus.NOT_FOUND
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    new ApiResponse(400, "Invalid request", null),
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    // Delete student
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteStudent(@PathVariable Long id) {
        try {
            studentService.deleteStudent(id);
            return new ResponseEntity<>(
                    new ApiResponse(200, "Student deleted successfully", null),
                    HttpStatus.OK
            );
        }catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(
                    new ApiResponse(404, "Student not found with id " + id, Map.of("id", id)),
                    HttpStatus.NOT_FOUND
            );
        }
    }
}

