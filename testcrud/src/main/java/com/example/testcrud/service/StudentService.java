package com.example.testcrud.service;

import com.example.testcrud.dto.StudentDTO;
import com.example.testcrud.exception.ResourceNotFoundException;
import com.example.testcrud.model.Student;
import com.example.testcrud.repository.StudentRepository;
import com.example.testcrud.Util.StudentMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class StudentService {

    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    // Create student
    public StudentDTO createStudent(StudentDTO studentDTO) {
        Student student = StudentMapper.toEntity(studentDTO);
        Student saved = studentRepository.save(student);
        return StudentMapper.toDTO(saved);
    }

    // Get all students with Optional
    public Optional<List<StudentDTO>> getAllStudents() {
        List<StudentDTO> students = studentRepository.findAll()
                .stream()
                .map(StudentMapper::toDTO)
                .collect(Collectors.toList());
        return students.isEmpty() ? Optional.empty() : Optional.of(students);
    }

    // Get student by ID
    public StudentDTO getStudentById(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id " + id));
        return StudentMapper.toDTO(student);
    }

    // Update student
    public StudentDTO updateStudent(Long id, StudentDTO studentDTO) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id " + id));

        student.setName(studentDTO.getName());
        student.setEmail(studentDTO.getEmail());

        Student updated = studentRepository.save(student);
        return StudentMapper.toDTO(updated);
    }

    // Delete student
    public void deleteStudent(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id " + id));
        studentRepository.delete(student);
    }

    // Get student by email
    public StudentDTO getStudentByEmail(String email) {
        Student student = studentRepository.findByEmail(email);
        return (student != null) ? StudentMapper.toDTO(student) : null;
    }

    // Search by email or create new student if not found
    public StudentDTO findOrCreateByEmail(String email, String name) {
        Student student = studentRepository.findByEmail(email);
        if (student != null) {
            return StudentMapper.toDTO(student);
        }
        if (name == null || name.isEmpty()) {
            name = "New Student";
        }
        Student newStudent = new Student(name, email);
        Student saved = studentRepository.save(newStudent);
        return StudentMapper.toDTO(saved);
    }

    // Find students by name containing a keyword (case-insensitive)
    public List<StudentDTO> searchByName(String keyword) {
        return studentRepository.findByNameContaining(keyword)
                .stream()
                .map(StudentMapper::toDTO)
                .collect(Collectors.toList());
    }

    // Count students with a given name
    public Long countByName(String name) {
        return studentRepository.countByName(name);
    }

    // Find students whose name starts with a prefix
    public List<StudentDTO> findByPrefix(String prefix) {
        return studentRepository.findByNameStartingWith(prefix)
                .stream()
                .map(StudentMapper::toDTO)
                .collect(Collectors.toList());
    }

    // Find students by email domain
    public List<StudentDTO> findByDomain(String domain) {
        return studentRepository.findByEmailEndingWith(domain)
                .stream()
                .map(StudentMapper::toDTO)
                .collect(Collectors.toList());
    }

    // Get all students sorted by name ascending
    public List<StudentDTO> getAllSortedByName() {
        return studentRepository.findAllOrderByNameAsc()
                .stream()
                .map(StudentMapper::toDTO)
                .collect(Collectors.toList());
    }
}
