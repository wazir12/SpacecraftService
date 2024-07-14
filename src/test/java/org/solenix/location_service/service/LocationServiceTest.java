package org.solenix.location_service.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.solenix.location_service.model.Event;
import org.solenix.location_service.model.GeoPosition;
import org.solenix.location_service.model.Latitude;
import org.solenix.location_service.model.Longitude;
import org.springframework.util.ReflectionUtils;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class LocationServiceTest {

    private LocationService locationService;
    @BeforeEach
    void setUp() {
        locationService = new LocationService();

        // Injecting mocked data directly to private fields
        List<Latitude> mockLatitudes = Arrays.asList(
                new Latitude("2023-10-01T10:00:00", 34.0522),
                new Latitude("2023-10-01T10:10:00", 35.1234)
        );

        List<Longitude> mockLongitudes = Arrays.asList(
                new Longitude("2023-10-01T10:00:00", -118.2437),
                new Longitude("2023-10-01T10:10:00", -117.2437)
        );

        List<Event> mockEvents = Arrays.asList(
                new Event("E001", "2023-10-01T10:05:00", "System Startup", "Info")
        );

        injectMockData("latitudes", mockLatitudes);
        injectMockData("longitudes", mockLongitudes);
        injectMockData("events", mockEvents);
    }

    /**
     * Injects mock data into the specified field of the locationService instance using reflection.
     *
     * @param fieldName The name of the field to inject data into.
     * @param mockData The mock data to inject.
     */
    private void injectMockData(String fieldName, Object mockData) {
        // Find the field by name
        Field field = ReflectionUtils.findField(LocationService.class, fieldName);
        // Make the field accessible
        ReflectionUtils.makeAccessible(field);
        // Set the field's value to the mock data
        ReflectionUtils.setField(field, locationService, mockData);
    }

    @Test
    public void testInit() throws IOException {
        // Initialize the service with real JSON files
        locationService = new LocationService();

        // Run init method to load the data
        locationService.init();

        // Verify the data was loaded correctly using reflection
        List<Latitude> latitudes = (List<Latitude>) ReflectionUtils.getField(
                ReflectionUtils.findField(LocationService.class, "latitudes"), locationService);
        List<Longitude> longitudes = (List<Longitude>) ReflectionUtils.getField(
                ReflectionUtils.findField(LocationService.class, "longitudes"), locationService);
        List<Event> events = (List<Event>) ReflectionUtils.getField(
                ReflectionUtils.findField(LocationService.class, "events"), locationService);

        assertNotNull(latitudes);
        assertNotNull(longitudes);
        assertNotNull(events);

        assertFalse(latitudes.isEmpty());
        assertFalse(longitudes.isEmpty());
        assertFalse(events.isEmpty());
    }
    @Test
    public void testGetGeoPositionForEvent() {
        // Test getting the GeoPosition for a specific event ID
        Optional<GeoPosition> position = locationService.getGeoPositionForEvent("E001");
        // Verify that the position is present
        assertTrue(position.isPresent());
        // Verify the latitude value with a tolerance of 0.0001
        assertEquals(34.0522, position.get().getLatitude(), 0.0001);
        // Verify the longitude value with a tolerance of 0.0001
        assertEquals(-118.2437, position.get().getLongitude(), 0.0001);
    }

    @Test
    public void testGetGeoPositionForEvent_NotFound() {
        // Test getting the GeoPosition for a non-existent event ID
        Optional<GeoPosition> position = locationService.getGeoPositionForEvent("E999");
        // Verify that the position is not present
        assertFalse(position.isPresent());
    }

    @Test
    public void testGetAllEventsWithPositions() {
        // Test getting all events with their associated positions
        var eventsWithPositions = locationService.getAllEventsWithPositions();
        // Verify that the list is not empty
        assertFalse(eventsWithPositions.isEmpty());
        // Verify that the list contains one item
        assertEquals(1, eventsWithPositions.size());
        //Verify the event ID
        assertEquals("E001", eventsWithPositions.get(0).getEvent().getId());
        // Verify that the position is not null
        assertNotNull(eventsWithPositions.get(0).getPosition());
    }
}