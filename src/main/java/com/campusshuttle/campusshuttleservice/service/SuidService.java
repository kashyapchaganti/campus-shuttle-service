package com.campusshuttle.campusshuttleservice.service;

import com.campusshuttle.campusshuttleservice.entity.Student;
import com.campusshuttle.campusshuttleservice.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SuidService {
    @Autowired
    private StudentRepository studentRep;
    public SuidService(){

    }
    // Method to check if a student exists by SUID
    public boolean findBySUID(long suid) {
        return studentRep.existsBySUID(suid);
    }

    // Method to retrieve a student object by SUID
    public Student getStudent(long suid){

        return studentRep.findBySUID(suid);
    }
}
