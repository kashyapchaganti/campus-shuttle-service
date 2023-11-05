package com.campusshuttle.campusshuttleservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;


@Scope("singleton")

public class Shuttle {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private static List<Passenger> listOfPassengersTravelling;

    private static List<Passenger> waitList;

    private static double collegePlaceLat= 43.03782492295655; // campus place lat
    private static double collegePlaceLng= -76.13154013374061; // campus place lng

    private static Coordinates coordinates;
    private Double Speed = 20.0;

    private static Shuttle shuttle;



    protected Shuttle() {
        // Private constructor for singleton pattern
    }

    public static synchronized Shuttle getInstance() {
        if (shuttle == null) {
            shuttle = new Shuttle();
            shuttle.coordinates = new Coordinates(collegePlaceLat,
                    collegePlaceLng ); // Initialize the coordinates object with Campus Stop Coordinates

            listOfPassengersTravelling=new ArrayList<>();
            waitList=new ArrayList<>();

        }
        return shuttle;
    }

    public static void setCoordinates(Coordinates coordinates) {
        Shuttle.coordinates = coordinates;
    }

    public List<Passenger> getListOfPassengers() {
        return listOfPassengersTravelling;
    }

    public void setListOfPassengers(List<Passenger> listOfPassengers) {
        this.listOfPassengersTravelling = listOfPassengers;
    }

    public List<Passenger> getWaitList() {
        return waitList;
    }

    public void setWaitList(List<Passenger> waitList) {
        this.waitList = waitList;
    }

    public static Coordinates getCoordinates() {
        return coordinates;
    }

    public Double getSpeed() {
        return Speed;
    }

    public static double getCollegePlaceLat() {
        return collegePlaceLat;
    }

    public static double getCollegePlaceLng() {
        return collegePlaceLng;
    }

    //for future if we want to change speed accordingly
    public void setSpeed(Double speed) {
        Speed = speed;
    }
}
