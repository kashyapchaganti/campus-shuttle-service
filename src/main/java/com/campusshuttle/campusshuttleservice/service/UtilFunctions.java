package com.campusshuttle.campusshuttleservice.service;

import com.campusshuttle.campusshuttleservice.entity.Coordinates;
import com.campusshuttle.campusshuttleservice.entity.Passenger;
import com.campusshuttle.campusshuttleservice.entity.Shuttle;
import com.campusshuttle.campusshuttleservice.repository.PassengerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;


@Service
public class UtilFunctions {

   private static final Shuttle shuttle=Shuttle.getInstance();;

   @Autowired
   private PassengerRepository passengerRepository;

    public static void removePassenger(Passenger passengerToBeRemoved, List<Passenger> listToBeModified) {
        // Create an iterator for the list
        Iterator<Passenger> iterator = listToBeModified.iterator();

        // Loop through the passengers with the iterator
        while (iterator.hasNext()) {
            Passenger passenger = iterator.next();
            // Check if the current passenger's unique identifier matches the one to be removed
            if (passenger.getSuid().equals(passengerToBeRemoved.getSuid())) {
                // If a match is found, remove the passenger using the iterator to avoid ConcurrentModificationException
                iterator.remove();
                // Break is not needed here since we're safely removing via iterator
                break;
            }
        }
    }

    // gives sorted list based on ETA
    public static  List<Passenger> sortedListWithETA(List<Passenger> listOfPersons, boolean isTravelling ){
        // Retrieve the shuttle's current coordinates
        Coordinates shuttleCoordinates = Shuttle.getCoordinates();

        // Initialize a new list to hold the passengers with updated ETAs
        List<Passenger>sortedList=new ArrayList<>();

        for(Passenger currentPassenger: listOfPersons){

            // Create coordinates for passenger pickup and drop off locations
            Coordinates nextPassenger_pickup_coordinates=new Coordinates(currentPassenger.getPickup_lat(),
                    currentPassenger.getPickup_lng() );
            Coordinates nextPassenger_drop_coordinates= new Coordinates(currentPassenger.getDrop_lat(),
                    currentPassenger.getDrop_lng() );

            // Decide which coordinates to use for distance calculation based on whether the passenger is travelling or not
            Coordinates targetCoordinates = isTravelling?nextPassenger_drop_coordinates:nextPassenger_pickup_coordinates;

            // Calculate the distance between the shuttle and the target coordinates. Also calculate ETA
            double distance = HaversineDistanceCalculator.calculateDistance(targetCoordinates, shuttleCoordinates);
            double speed = shuttle.getSpeed();
            double ETA= (distance/speed)*60;

            // Add a new Passenger object with updated ETA to the sorted list
            sortedList.add(new Passenger(currentPassenger.getSuid(), nextPassenger_pickup_coordinates, nextPassenger_drop_coordinates,
                    ETA,currentPassenger.getCounterSinceRequested()));

        }
        // return sorted list with ETAs of passengers
        return sortedList;

    }
    public List<Passenger> incrementCounter(List<Passenger>listToBeModified) {

        // Create a new list to store the passengers that haven't been picked up
        List<Passenger>nonPickedPassengers=new ArrayList<>();

        for(Passenger passenger: listToBeModified){

            // Increment the counter of time units since the pickup was requested or since onboarded
            long counter= passenger.getCounterSinceRequested()+1;

            // Set the new counter value for the passenger
            passenger.setCounterSinceRequested(counter);
            // Add the updated passenger to the non-picked-up list or non-dropped list
            nonPickedPassengers.add(passenger);
            passengerRepository.save(passenger);
        }
        listToBeModified=new ArrayList<>(nonPickedPassengers);
        return listToBeModified;
    }


    public static Passenger findNext(List<Passenger> nextPassengers) {
        // Initialize the variable to hold the next passenger to null
        Passenger nextPassenger = null;
        // Initialize the maximum counter found to 0
        double max = 0;

        // Loop through each passenger in the list of next passengers
        for (Passenger passenger : nextPassengers) {
            // Check if the passenger's counter is at least 6
            if (passenger.getCounterSinceRequested() >= 6) {
                // If the current passenger's counter is greater than the max found so far
                if (max < passenger.getCounterSinceRequested()) {
                    // Update the max counter
                    max = passenger.getCounterSinceRequested();
                    // Set this passenger as the next one to be processed
                    nextPassenger = passenger;
                }
            }
        }

        // If no passenger has a counter greater than or equal to 6 and the list is not empty
        if (nextPassenger == null && !nextPassengers.isEmpty()) {
            // Sort the list of next passengers by their ETA in ascending order
            nextPassengers.sort(Comparator.comparingDouble(Passenger::getETA));
            // Select the passenger with the lowest ETA as the next one
            nextPassenger = nextPassengers.get(0);
        }

        // Return the passenger who is next in line to be processed, or null if none are found
        return nextPassenger;
    }

}
