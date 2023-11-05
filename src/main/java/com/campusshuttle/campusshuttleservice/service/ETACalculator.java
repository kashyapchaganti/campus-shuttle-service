package com.campusshuttle.campusshuttleservice.service;

import com.campusshuttle.campusshuttleservice.entity.*;
import com.campusshuttle.campusshuttleservice.repository.PassengerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class ETACalculator {
    @Autowired
    private HaversineDistanceCalculator distanceCalculator;

    private Shuttle shuttle;
    private Passenger passenger;


    @Autowired
    private PassengerRepository passengerRepository;

    //     Calculates the ETA for a shuttle to reach a passenger and adds them to the waitlist
    public ETACalculator(){
        shuttle = Shuttle.getInstance();
    }
    public String etaCalculation(long suid, Coordinates pickup_coordinates,
                                 Coordinates drop_coordinates){

        List<Passenger> waitList = shuttle.getWaitList();

        // Calculate the distance from the shuttle's current location to the pickup location
        double distance =  distanceCalculator.calculateDistance(pickup_coordinates, shuttle.getCoordinates());
        double speed = shuttle.getSpeed();
        double ETA= (distance/speed)*60;

        // Not gonna accept requests with longer distaces
        if(ETA>40){
            return "";
        }

        // add passenger to the waitlist
        passenger=new Passenger(suid, pickup_coordinates,drop_coordinates,ETA,0);
        waitList.add(passenger);
        shuttle.setWaitList(waitList);

        // add the passenger to the Passenger DB
        passenger.setUserStatus(UserStatus.WAITING);;
        passengerRepository.save(passenger);

        return Double.toString(ETA);

    }

}
