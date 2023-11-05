package com.campusshuttle.campusshuttleservice.service;

import com.campusshuttle.campusshuttleservice.entity.Student;
import com.campusshuttle.campusshuttleservice.entity.UserStatus;
import com.campusshuttle.campusshuttleservice.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import javax.naming.ServiceUnavailableException;

@Service
public class RegisterServiceImpl implements RegisterService {
    @Autowired
    private StudentRepository studentRepository;

    //  Saves a student to the database after validation.
    public Student saveToDb(Student student) throws ServiceUnavailableException{

        // Validate the SUID to ensure it is a 9-digit number.
        if (!isValidSUID9Digit(student.getSUID())) {
            throw new ServiceUnavailableException("Student SUID must be a 9-digit long number");
        }

        // Check if a student with the same SUID already exists in the database.
        if (studentRepository.existsById(student.getSUID())) {
            throw new ServiceUnavailableException("Student SUID already exists in the database");
        }

        // Set the user status of the student to IDLE before saving.
        student.setUserStatus(UserStatus.IDLE);

        // Attempt to save the student to the database.
        try {
            return studentRepository.save(student);
        } catch (DataIntegrityViolationException e) {
            // Catch any database constraints violations and rethrow as a ServiceUnavailableException.
            throw new ServiceUnavailableException("Error saving student to the database");
        }
    }


    //  Checks if the provided SUID is a valid 9-digit number.
    private boolean isValidSUID9Digit(Long suid) {

        // Check if the SUID is not null and matches a regular expression for a 9-digit number.
        return suid != null && String.valueOf(suid).matches("\\d{9}");

    }
}
