package com.campusshuttle.campusshuttleservice;

import com.campusshuttle.campusshuttleservice.entity.Shuttle;
import com.campusshuttle.campusshuttleservice.service.GeocodingService;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class MyStartupRunner implements ApplicationRunner {
    @Override

    public void run(ApplicationArguments args) throws Exception {

        // My startup logic here
        System.out.println("Hi There!!! --- Welcome to Campus Shuttle! ");

        // creating the first instance of shuttle for the entire application (SINGLETON)
        Shuttle shuttle = Shuttle.getInstance();
    }
}
