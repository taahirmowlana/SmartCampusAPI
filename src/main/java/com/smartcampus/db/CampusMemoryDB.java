package com.smartcampus.db;

import com.smartcampus.models.SensorReading;
import com.smartcampus.models.Sensor;
import com.smartcampus.models.Room;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;
import java.util.List;

public class CampusMemoryDB {
    private static final Map<String, Room> rooms = new ConcurrentHashMap<>();
    private static final Map<String, Sensor> sensors = new ConcurrentHashMap<>();
    private static final Map<String, List<SensorReading>> sensorReadings = new ConcurrentHashMap<>();

    public static Map<String, Room> getRooms() { return rooms; }
    public static Map<String, Sensor> getSensors() { return sensors; }
    public static Map<String, List<SensorReading>> getSensorReadings() { return sensorReadings; }
}