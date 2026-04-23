package com.smartcampus;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SensorReadingResource {

    private final String sensorId;

    public SensorReadingResource(String sensorId) {
        this.sensorId = sensorId;
    }

    @GET
    public Response getReadings() {
        List<SensorReading> readings = CampusMemoryDB.getSensorReadings().getOrDefault(sensorId, new ArrayList<>());
        return Response.ok(readings).build();
    }

    @POST
    public Response addReading(SensorReading reading) {
        Sensor parentSensor = CampusMemoryDB.getSensors().get(sensorId);
        
        if (parentSensor == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Sensor not found.").build();
        }

        if ("MAINTENANCE".equalsIgnoreCase(parentSensor.getStatus()) || "OFFLINE".equalsIgnoreCase(parentSensor.getStatus())) {
            throw new SensorUnavailableException("Forbidden: Sensor " + sensorId + " is in " + parentSensor.getStatus() + " mode.");
        }

        CampusMemoryDB.getSensorReadings().putIfAbsent(sensorId, new ArrayList<>());
        CampusMemoryDB.getSensorReadings().get(sensorId).add(reading);
        parentSensor.setCurrentValue(reading.getValue());

        return Response.status(Response.Status.CREATED).entity(reading).build();
    }
}