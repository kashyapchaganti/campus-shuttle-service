package com.campusshuttle.campusshuttleservice.controller;

import com.campusshuttle.campusshuttleservice.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.naming.ServiceUnavailableException;

@RestController
@RequestMapping("/api")
public class StudentController {


    @Autowired
    private StudentServiceImpl studentServiceImpl;

    //ENDPOINT TO PROCESS STUDENT'S PICKUP REQUEST
    @GetMapping("/requestPickup")
    public String requestPickup(@RequestParam long suid, String pickup_address, String drop_address
                                            ) throws ServiceUnavailableException{

        return studentServiceImpl.requestPickup(suid,pickup_address,drop_address);
    }


}
