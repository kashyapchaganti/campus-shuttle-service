package com.campusshuttle.campusshuttleservice.controller;

import com.campusshuttle.campusshuttleservice.service.ShuttleServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ShuttleLocation {

    @Autowired
    private ShuttleServiceImpl shuttleServiceImpl;

    // THIS API PROVIDES CURRENT LOCATION OF SHUTTLE
    @GetMapping("/shuttleLocation")
    public String shuttleLocation(){
        return shuttleServiceImpl.shuttleLocation();
    }
}
