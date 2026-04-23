package com.smartcampus;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Collection;
import java.util.UUID;

@Path("/rooms")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RoomResource {

    // 1. GET ALL ROOMS
    @GET
    public Response getAllRooms() {
        Collection<Room> rooms = CampusMemoryDB.getRooms().values();
        return Response.ok(rooms).build();
    }

    // 2. GET SPECIFIC ROOM
    @GET
    @Path("/{roomId}")
    public Response getRoom(@PathParam("roomId") String roomId) {
        Room room = CampusMemoryDB.getRooms().get(roomId);
        if (room == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(room).build();
    }

    // 3. CREATE NEW ROOM
    @POST
    public Response createRoom(Room room) {
        // Auto-generate an ID if the client forgets to provide one
        if (room.getId() == null || room.getId().trim().isEmpty()) {
            room.setId("ROOM-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        }
        CampusMemoryDB.getRooms().put(room.getId(), room);
        return Response.status(Response.Status.CREATED).entity(room).build();
    }

    // 4. SECURE DELETE ROOM
    @DELETE
    @Path("/{roomId}")
    public Response deleteRoom(@PathParam("roomId") String roomId) {
        Room room = CampusMemoryDB.getRooms().get(roomId);
        
        if (room == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        // The Safety Protocol: Scan the database for orphaned sensors
        boolean isOccupied = CampusMemoryDB.getSensors().values().stream()
                .anyMatch(sensor -> roomId.equals(sensor.getRoomId()));

        if (isOccupied) {
            // Triggers the custom exception we just built
            throw new RoomNotEmptyException("Conflict: Room " + roomId + " cannot be deleted. Active sensors are currently assigned to this location.");
        }

        CampusMemoryDB.getRooms().remove(roomId);
        return Response.noContent().build(); // 204 No Content for successful deletion
    }
}