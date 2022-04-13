package com.example.demo.student;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StudentService {

  private final StudentRepository studentRepository;

  @Autowired
  public StudentService(StudentRepository studentRepository) {
    this.studentRepository = studentRepository;
  }

  public List<Student> getStudents() {
    return studentRepository.findAll();
  }

  public void addNewStudent(Student student) {
    Optional<Student> studentOptional = studentRepository.findStudentByEmail(student.getEmail());
    System.out.println(student);

    if (studentOptional.isPresent()) {
      throw new IllegalStateException("email taken");
    }
    studentRepository.save(student);
  }

  public void deleteStudent(Long studentId) {
    boolean exists = studentRepository.existsById(studentId);

    if (!exists) {
      throw new IllegalStateException("student with id " + studentId + " does not exist");
    }

    studentRepository.deleteById(studentId);
  }

  @Transactional
  public Student updateStudent(Long id, Student studentFields) {
    return studentRepository.findById(id)
            .map(student -> {
              student.setName(studentFields.getName());
              student.setEmail(studentFields.getEmail());
              return student;
            }).orElseThrow(() -> new IllegalStateException("student does not exist"));
  }
}
