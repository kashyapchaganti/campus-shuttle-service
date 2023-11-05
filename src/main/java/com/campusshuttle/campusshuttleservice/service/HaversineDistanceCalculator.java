package com.campusshuttle.campusshuttleservice.service;

import com.campusshuttle.campusshuttleservice.entity.Coordinates;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class HaversineDistanceCalculator {



        // Radius of the Earth in kilometers
        private static final double EARTH_RADIUS = 6371.0;

        // Calculate the distance between two coordinates on Earth using the Haversine formula.

    public static double calculateDistance(Coordinates first, Coordinates second) {
            double lat1= first.getLatitude();
            double lat2= second.getLatitude();
            double lon1= first.getLongitude();
            double lon2= second.getLongitude();
            // Convert degrees to radians
            lat1 = Math.toRadians(lat1);
            lon1 = Math.toRadians(lon1);
            lat2 = Math.toRadians(lat2);
            lon2 = Math.toRadians(lon2);

            // Compute the differences between the two latitudes and longitudes
            double deltaLat = lat2 - lat1;
            double deltaLon = lon2 - lon1;

            // Haversine formula
            double a = Math.pow(Math.sin(deltaLat / 2), 2) +
                    Math.cos(lat1) * Math.cos(lat2) *
                            Math.pow(Math.sin(deltaLon / 2), 2);

            double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

            return EARTH_RADIUS * c;
        }



}
