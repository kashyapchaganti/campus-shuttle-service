package com.campusshuttle.campusshuttleservice.controller;

import com.campusshuttle.campusshuttleservice.service.GeocodingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class GeoCodeController {

    @Autowired
    private GeocodingService geocodingService;


    // This API is used to give coordinates of any address

    @GetMapping("/api/geoCoord")
    public String geoCoord(@RequestParam String address){
        Map<String, Double> coordinates = geocodingService.getCoordinates(address);
        return coordinates.toString();
    }
}
