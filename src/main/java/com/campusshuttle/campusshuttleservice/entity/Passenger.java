package com.campusshuttle.campusshuttleservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;


@Entity
@Data

public class Passenger {

    @Id
    private Long suid;
    private double pickup_lat,drop_lat, pickup_lng,drop_lng;

    // Tracks how long a person has been in the waitlist or in the shuttle since he requested or picked up respectively
    private Long counterSinceRequested;

    private double ETA;
    private UserStatus userStatus;



    public Passenger(){}


    public Passenger(Long suid, Coordinates pickup, Coordinates drop, double ETA, long counterSinceRequested){
        this.pickup_lat=pickup.getLatitude();
        this.pickup_lng=pickup.getLongitude();
        this.drop_lat= drop.getLatitude();
        this.drop_lng= drop.getLongitude();
        this.ETA=ETA;
        this.suid=suid;
        this.counterSinceRequested=counterSinceRequested;
    }

    public Long getSuid() {
        return suid;
    }

    public void setUserStatus(UserStatus userStatus) {
        this.userStatus = userStatus;
    }

    public double getPickup_lat() {
        return pickup_lat;
    }


    public double getDrop_lat() {
        return drop_lat;
    }


    public double getPickup_lng() {
        return pickup_lng;
    }


    public double getDrop_lng() {
        return drop_lng;
    }


    public Long getCounterSinceRequested() {
        return counterSinceRequested;
    }

    public void setCounterSinceRequested(Long counterSinceRequested) {
        this.counterSinceRequested = counterSinceRequested;
    }

    public double getETA() {
        return ETA;
    }

    @Override
    public String toString() {
        return "Passenger{" +
                "suid=" + suid +
                ", counterSinceRequested=" + counterSinceRequested +
                ", userStatus=" + userStatus +
                '}';
    }


}
