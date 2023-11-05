package com.campusshuttle.campusshuttleservice.service;

import com.campusshuttle.campusshuttleservice.entity.Student;

import javax.naming.ServiceUnavailableException;

public interface StudentService {

    // method to save student to the DB
    Student saveToDb(Student student);

    // method to process student's request
    String requestPickup(long suid, String pickupAddress, String dropAddress) throws ServiceUnavailableException;

}
