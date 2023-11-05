package com.campusshuttle.campusshuttleservice.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class GeocodingService {

    @Value("${google.maps.api.key}")
    private String apiKey;

    private static final String GEOCODE_URL = "https://maps.googleapis.com/maps/api/geocode/json?address=%s&key=%s";

    //     Retrieves the latitude and longitude for a given address using the Google Geocoding API.
    public Map<String, Double> getCoordinates(String address) {
        RestTemplate restTemplate = new RestTemplate();

        // Format the geocoding URL with the supplied address and the API key
        String url = String.format(GEOCODE_URL, address, apiKey);

        // Perform the HTTP GET request to the geocoding URL
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        if (response.getStatusCode() == HttpStatus.OK) {
            try {
                // Parse the JSON response using Jackson ObjectMapper
                ObjectMapper mapper = new ObjectMapper();
                JsonNode root = mapper.readTree(response.getBody());

                // Extract the latitude and longitude from the JSON response
                JsonNode location = root.path("results").get(0).path("geometry").path("location");
                double lat = location.path("lat").asDouble();
                double lng = location.path("lng").asDouble();

                // Create a map to hold the coordinates
                Map<String, Double> coordinates = new HashMap<>();
                coordinates.put("lat", lat);
                coordinates.put("lng", lng);

                return coordinates;
            } catch (Exception e) {
                // Handle exception (log it, etc.)
                throw new RuntimeException("Error parsing geocoding response.", e);
            }
        }

        return null; // Or throw an exception if preferred
    }
}
