package com.smartcampus;

import org.glassfish.jersey.server.ResourceConfig;
import javax.ws.rs.ApplicationPath;

@ApplicationPath("/api/v1")
public class RestConfig extends ResourceConfig {
    public RestConfig() {
        // 1. Tell Jersey to scan the package for resources
        packages("com.smartcampus"); 
        
        // 2. Add the Discovery Resource registration here:
        register(DiscoveryResource.class); 
        
        // (Optional but good for safety) Hardwire the others:
        register(RoomResource.class);
        register(SensorResource.class);
        register(SensorReadingResource.class);
        
        // Exception Mappers & Filters
        register(RoomNotEmptyMapper.class);
        register(LinkedResourceNotFoundMapper.class);
        register(SensorUnavailableMapper.class);
        register(GlobalExceptionMapper.class);
        register(LoggingFilter.class);
    }
}