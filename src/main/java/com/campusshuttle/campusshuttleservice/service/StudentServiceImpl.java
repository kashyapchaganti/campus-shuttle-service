package com.campusshuttle.campusshuttleservice.service;

import com.campusshuttle.campusshuttleservice.entity.Coordinates;
import com.campusshuttle.campusshuttleservice.entity.Student;
import com.campusshuttle.campusshuttleservice.entity.UserStatus;
import com.campusshuttle.campusshuttleservice.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.naming.ServiceUnavailableException;
import java.util.Map;

@Service
public class StudentServiceImpl implements StudentService{
    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private SuidService suidService;
    @Autowired
    private ETACalculator etaCalculator;

    @Autowired
    private GeocodingService geocodingService;

    Coordinates pickup_coordinates,drop_coordinates;
    public Student saveToDb(Student student){

        return studentRepository.save(student);
    }

    public String requestPickup(long suid, String pickupAddress, String dropAddress) throws ServiceUnavailableException{

        // Check if the student with the provided SUID exists in the database
        if(!suidService.findBySUID(suid)) {
            throw new ServiceUnavailableException("SUID INCORRECT");
        }

        // Retrieve the Student object from the database
        Student student = suidService.getStudent((suid));

        // Check the student's current status, if they are already waiting, throw an exception
        if(student.getUserStatus()==(UserStatus.WAITING)){

            throw new ServiceUnavailableException("You're already on Waitlist");
        }
        // If the student has already been picked up, they can't make a new request
        if(student.getUserStatus()==(UserStatus.PICKEDUP)){

            throw new ServiceUnavailableException("You can't request while on ride");
        }

        // Use the geocoding service to convert the pickup and drop off address into geographical coordinates
        Map<String, Double> pickupCoordinatesMap =  geocodingService.getCoordinates(pickupAddress);

        Map<String, Double> dropCoordinatesMap =  geocodingService.getCoordinates(dropAddress);


        pickup_coordinates=new Coordinates(pickupCoordinatesMap.get("lat"),
                pickupCoordinatesMap.get("lng") );
        drop_coordinates=new Coordinates(dropCoordinatesMap.get("lat"),
                dropCoordinatesMap.get("lng"));

        // Calculate the estimated time for the pickup request and return it
        return EstimatedTimeCalculatorPerRequest(suid,student);


    }

    // A helper method to calculate the estimated time for a student's pickup request
    private String EstimatedTimeCalculatorPerRequest(long suid, Student student){

        // Calculate ETA using an etaCalculator object (presumably injected or instantiated elsewhere in the class)
        String ETA= etaCalculator.etaCalculation(suid,pickup_coordinates,drop_coordinates );

        // If the ETA calculation returns an empty string, we assume the service is unavailable
        if(ETA.equals("")){
            return "Shuttle Service is unavailable from your location";
        }

        // Set the student's status to WAITING, indicating they are now on the waitlist for a pickup
        student.setUserStatus(UserStatus.WAITING);
        saveToDb(student);

        return "Estimated Wait Time is " + ETA +" Minutes";
    }

}
