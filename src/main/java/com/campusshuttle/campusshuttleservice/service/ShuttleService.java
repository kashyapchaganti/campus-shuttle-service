package com.campusshuttle.campusshuttleservice.service;

import com.campusshuttle.campusshuttleservice.entity.Passenger;

import java.util.List;

public interface ShuttleService {

    // Algorithm to pick the next passenger
    String pickedPassenger();

    // Algorithm to drop the next passenger
    String dropPassenger();

    // // Algorithm for Shuttle's current Location
    String shuttleLocation();


}
