package com.campusshuttle.campusshuttleservice.controller;

import com.campusshuttle.campusshuttleservice.entity.Coordinates;
import com.campusshuttle.campusshuttleservice.service.HaversineDistanceCalculator;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class DistanceCalculator {

    // This api calculates distance between two coordinates using Haversine Algorithm

    @GetMapping("/distanceCalculator")
    public String etaCal(@RequestParam double x, double y, double a, double b){

        Coordinates a1= new Coordinates(x,y);

        Coordinates a2= new Coordinates(a,b);

        return String.valueOf(HaversineDistanceCalculator.calculateDistance(a1,a2));

    }
}
