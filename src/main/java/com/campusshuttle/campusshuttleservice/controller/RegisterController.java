package com.campusshuttle.campusshuttleservice.controller;

import com.campusshuttle.campusshuttleservice.entity.Student;
import com.campusshuttle.campusshuttleservice.service.RegisterServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.naming.ServiceUnavailableException;

@RestController
@RequestMapping("/api")
public class RegisterController {

    // THIS API IS TO REGISTER A STUDENT TO THE DB
    @Autowired
    private RegisterServiceImpl registerService;

    @PostMapping("/register")
    public Student registerStudent(@RequestBody Student student) throws ServiceUnavailableException {

        return registerService.saveToDb(student);

    }


}
