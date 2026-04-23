package com.smartcampus.resources;

import com.smartcampus.db.CampusMemoryDB;
import com.smartcampus.exceptions.LinkedResourceNotFoundException;
import com.smartcampus.models.Sensor;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Path("/sensors")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SensorResource {

    @GET
    public Response getSensors(@QueryParam("type") String type) {
        List<Sensor> result = CampusMemoryDB.getSensors().values().stream()
                .filter(s -> type == null || type.equalsIgnoreCase(s.getType()))
                .collect(Collectors.toList());
        return Response.ok(result).build();
    }

    @POST
    public Response createSensor(Sensor sensor) {
        if (!CampusMemoryDB.getRooms().containsKey(sensor.getRoomId())) {
            throw new LinkedResourceNotFoundException("Unprocessable Entity: Room ID '" + sensor.getRoomId() + "' does not exist.");
        }

        if (sensor.getId() == null || sensor.getId().trim().isEmpty()) {
            sensor.setId("SENS-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        }
        
        CampusMemoryDB.getSensors().put(sensor.getId(), sensor);
        CampusMemoryDB.getRooms().get(sensor.getRoomId()).getSensorIds().add(sensor.getId());

        return Response.status(Response.Status.CREATED).entity(sensor).build();
    }

    // Sub-Resource Locator
    @Path("/{sensorId}/readings")
    public SensorReadingResource getSensorReadingResource(@PathParam("sensorId") String sensorId) {
        return new SensorReadingResource(sensorId);
    }
}