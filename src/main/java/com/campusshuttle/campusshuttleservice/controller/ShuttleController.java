package com.campusshuttle.campusshuttleservice.controller;

import com.campusshuttle.campusshuttleservice.service.ShuttleServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ShuttleController {

    @Autowired
    private ShuttleServiceImpl shuttleServiceImpl;

    // WHEN DRIVER REQUESTS SERVER TO UPDATE THAT HE PICKED UP A PASSENGER

    @GetMapping("/pickedPassenger")
    public String pickedPassenger(){
        return shuttleServiceImpl.pickedPassenger();
    }

    // WHEN DRIVER REQUESTS SERVER TO UPDATE THAT HE DROPPED OFF A PASSENGER
    @GetMapping("/dropPassenger")
    public String dropPassenger(){
        return shuttleServiceImpl.dropPassenger();
    }
}
