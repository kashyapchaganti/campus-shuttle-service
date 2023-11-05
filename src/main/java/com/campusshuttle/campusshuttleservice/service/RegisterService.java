package com.campusshuttle.campusshuttleservice.service;

import com.campusshuttle.campusshuttleservice.entity.Student;

import javax.naming.ServiceUnavailableException;

public interface RegisterService {

    // saving student to DB
    public Student saveToDb(Student student) throws ServiceUnavailableException;
}
