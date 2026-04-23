package com.smartcampus;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.Map;

@Path("/") // This maps to /api/v1/
public class DiscoveryResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getDiscovery() {
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("version", "1.0-STARK");
        metadata.put("description", "Smart Campus Sensor & Room Management API");
        metadata.put("admin_contact", "taahir@university.edu");

        // HATEOAS: Hypermedia links for discovery [cite: 110]
        Map<String, String> links = new HashMap<>();
        links.put("rooms", "/api/v1/rooms");
        links.put("sensors", "/api/v1/sensors");
        metadata.put("links", links);

        return Response.ok(metadata).build();
    }
}