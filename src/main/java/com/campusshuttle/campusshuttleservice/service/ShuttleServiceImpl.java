package com.campusshuttle.campusshuttleservice.service;

import com.campusshuttle.campusshuttleservice.entity.*;
import com.campusshuttle.campusshuttleservice.repository.PassengerRepository;
import com.campusshuttle.campusshuttleservice.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ShuttleServiceImpl implements ShuttleService{
    private final Shuttle shuttle= Shuttle.getInstance();

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private StudentServiceImpl studentServiceImpl;

    @Autowired
    private HaversineDistanceCalculator distanceCalculator;

    @Autowired
    private PassengerRepository passengerRepository;

    @Autowired
    private UtilFunctions utilFunctions;


    //        Our system for deciding which passenger to pick up uses two main criteria: proximity and wait time.
    //        Here's how we make the choice:
    //
    //     1  Priority is given to passengers who have been waiting since before more than 6 other passengers
    //        have been picked up and dropped off, regardless of how far away they are.
    //
    //     2  If there is more than one such passenger, we will pick the one who made the request the earliest.
    //
    //     3  If no passengers have been waiting through 6 pickups and drop-offs, then we will pick up
    //        the person who is closest to the shuttle's current location first.


    public String pickedPassenger(){


        // Check shuttle capacity and waitlist before picking up a passenger

        Coordinates shuttleCoordinates = Shuttle.getCoordinates();
        List<Passenger> listOfPassengersTravelling = shuttle.getListOfPassengers();
        List<Passenger> waitList = shuttle.getWaitList();

        // Handle case where there are no passengers waiting or traveling
        if(waitList.isEmpty()){
            if(listOfPassengersTravelling.isEmpty()){
                shuttleCoordinates.setLatitude(Shuttle.getCollegePlaceLat());
                shuttleCoordinates.setLongitude(Shuttle.getCollegePlaceLng());
                Shuttle.setCoordinates(shuttleCoordinates);
                return "Returning To Campus Stop, No Passengers to be dropped or picked";
            }else{
                return "No Passengers to pickup";
            }
        }

        // we will drop off a passenger to pick anyone else because of Max Capacity
        if(listOfPassengersTravelling.size()>=5){

            dropPassenger();
        }

        // get passenger list sorted based on their current distances and ETAs wrt to Shuttle's current locn
        List<Passenger> nextPassengers = UtilFunctions.sortedListWithETA(waitList,false);;


        // find the next passenger to be picked based on sorted list and request time logic stated above
        Passenger nextPassengerToBePicked= UtilFunctions.findNext(nextPassengers);

        String passengerPicked="";

        // If there is a passenger to pick up, process their pickup
        if(nextPassengerToBePicked!=null){

            // Update the student status in the database to reflect they have been picked up
            Student student = studentRepository.findBySUID(nextPassengerToBePicked.getSuid());
            student.setUserStatus(UserStatus.PICKEDUP);
            studentServiceImpl.saveToDb(student);

            // Update shuttle coordinates to the passenger's pickup location
            shuttleCoordinates.setLatitude(nextPassengerToBePicked.getPickup_lat());
            shuttleCoordinates.setLongitude(nextPassengerToBePicked.getPickup_lng());
            Shuttle.setCoordinates(shuttleCoordinates);


            // Remove the picked passenger from the waiting list and update passenger and shuttle lists
            UtilFunctions.removePassenger(nextPassengerToBePicked,waitList);

            // Increment the waiting time counter for both traveling and waiting passengers
            waitList= utilFunctions.incrementCounter(waitList);
            shuttle.setWaitList(waitList);
            listOfPassengersTravelling= utilFunctions.incrementCounter(listOfPassengersTravelling);
            nextPassengerToBePicked.setCounterSinceRequested(0L);
            listOfPassengersTravelling.add(nextPassengerToBePicked);
            shuttle.setListOfPassengers(listOfPassengersTravelling);

            // Update the passenger status in the database to reflect they have been picked up
            nextPassengerToBePicked.setUserStatus(UserStatus.PICKEDUP);
            nextPassengerToBePicked.setCounterSinceRequested(0L);
            passengerRepository.save(nextPassengerToBePicked);

            passengerPicked= "Picked Passenger " + student;

        }else{ // if no one is there in the wait list, send the shuttle to campus
            shuttleCoordinates.setLatitude(Shuttle.getCollegePlaceLat());
            shuttleCoordinates.setLongitude(Shuttle.getCollegePlaceLng());
            Shuttle.setCoordinates(shuttleCoordinates);

            passengerPicked="No Passengers Currently, returning to Campus Stop";
        }

        // Return the status of the pickup operation
        return passengerPicked;
    }


    //        Our method for deciding which passenger to drop off next depends on two main factors:
    //        who is closest and who has been waiting the longest. Here's how we decide:
    //
    //        1. If a passenger has been on the shuttle through more than 6 pickups and drop-offs,
    //        they get priority to be dropped off next, no matter how far away they are.
    //        2. If there's more than one passenger who has been on the shuttle for that long,
    //        then the one who has been waiting the longest since they first requested the ride will be chosen.
    //        3. If there aren't any passengers who've been on board for more than 6 stops,
    //        then we'll drop off the passenger who is closest to the shuttle's current location.


    public String dropPassenger() {

        // Get the shuttle's current coordinates and the list of passengers traveling and waiting
        Coordinates shuttleCoordinates = Shuttle.getCoordinates();
        List<Passenger> listOfPassengersTravelling = shuttle.getListOfPassengers();
        List<Passenger> waitList = shuttle.getWaitList();
        List<Passenger> dropList =new ArrayList<>();

        Passenger nextPassengerToBeDropped = null;

        // Sort the list of traveling passengers by their ETA for dropping off (true indicates descending order, presumably)
        dropList= UtilFunctions.sortedListWithETA(listOfPassengersTravelling,true);

        // Find the next passenger to be dropped off based on the sorted list
        nextPassengerToBeDropped= UtilFunctions.findNext(dropList);

        String passengerDropped="";

        // If there is a passenger to drop off, proceed with the drop-off process
        if(nextPassengerToBeDropped!=null){

            // Remove the passenger from the traveling list
            UtilFunctions.removePassenger(nextPassengerToBeDropped,listOfPassengersTravelling);


            // Increment the waiting time counter for both traveling and waiting passengers
            listOfPassengersTravelling=utilFunctions.incrementCounter(listOfPassengersTravelling);
            waitList=utilFunctions.incrementCounter(waitList);

            // Update the shuttle's list of passengers and the waiting list
            shuttle.setListOfPassengers(listOfPassengersTravelling);
            shuttle.setWaitList(waitList);

            // Update the student status to IDLE in the database after they are dropped off
            Student student= studentRepository.findBySUID(nextPassengerToBeDropped.getSuid());
            student.setUserStatus(UserStatus.IDLE);
            studentServiceImpl.saveToDb(student);

            // Update the shuttle's coordinates to the passenger's drop-off location
            shuttleCoordinates.setLatitude(nextPassengerToBeDropped.getDrop_lat());
            shuttleCoordinates.setLongitude(nextPassengerToBeDropped.getDrop_lng());
            Shuttle.setCoordinates(shuttleCoordinates);

            // Update the passenger status in the database to reflect they have been dropped off
            nextPassengerToBeDropped.setUserStatus(UserStatus.IDLE);
            nextPassengerToBeDropped.setCounterSinceRequested(0L);
            passengerRepository.save(nextPassengerToBeDropped);

            // Create a message indicating which passenger was dropped off
            passengerDropped= "Dropped Passenger " + student;
        }else{
            // If there are no passengers to drop off, check if any passengers need to be picked up
            passengerDropped= pickedPassenger();
            shuttleCoordinates.setLatitude(Shuttle.getCollegePlaceLat());
            shuttleCoordinates.setLongitude(Shuttle.getCollegePlaceLng());
            Shuttle.setCoordinates(shuttleCoordinates);
        }
        // Return the status of the drop-off or pick-up operation
        return passengerDropped;
    }

    // Method to get shuttle current coordinates
    public String shuttleLocation() {
        return Shuttle.getCoordinates().getLatitude() +" "+ Shuttle.getCoordinates().getLongitude();
    }


}



