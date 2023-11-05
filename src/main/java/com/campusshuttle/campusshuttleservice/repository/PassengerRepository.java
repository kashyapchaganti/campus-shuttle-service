package com.campusshuttle.campusshuttleservice.repository;

import com.campusshuttle.campusshuttleservice.entity.Passenger;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PassengerRepository extends JpaRepository<Passenger,Long> {
}
