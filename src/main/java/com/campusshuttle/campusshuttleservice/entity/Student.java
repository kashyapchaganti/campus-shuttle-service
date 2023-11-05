package com.campusshuttle.campusshuttleservice.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;


@Entity
public class Student {
    @Id
    private Long SUID;
    private String Name;
    private String homeAddress;

    // Tracks a Student status if he/she is picked up, or waiting for the ride, or idle
    private UserStatus userStatus;

    @Override
    public String toString() {
        return "Student{" +

                " SUID=" + SUID +
                ", Name='" + Name + '\'' +
                ", Address='" + homeAddress + '\'' +
                '}';
    }



    public void setUserStatus(UserStatus userStatus) {
        this.userStatus = userStatus;
    }

    public Long getSUID() {
        return SUID;
    }

    public void setSUID(Long SUID) {
        this.SUID = SUID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getHomeAddress() {
        return homeAddress;
    }

    public void setHomeAddress(String homeAddress) {
        this.homeAddress = homeAddress;
    }

    public UserStatus getUserStatus() {
        return userStatus;
    }
}
