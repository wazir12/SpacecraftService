package org.solenix.location_service.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.solenix.location_service.model.Event;
import org.solenix.location_service.model.EventWithPosition;
import org.solenix.location_service.model.GeoPosition;
import org.solenix.location_service.service.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(LocationServiceController.class)
class LocationServiceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LocationService locationService;


    @BeforeEach
    public void setUp() {
        // Mocking the LocationService methods
        Mockito.when(locationService.getGeoPositionForEvent(anyString())).thenAnswer(invocation -> {
            String id = invocation.getArgument(0);
            if ("E001".equals(id)) {
                return Optional.of(new GeoPosition(34.0522, -118.2437));
            } else {
                return Optional.empty();
            }
        });

        Mockito.when(locationService.getAllEventsWithPositions()).thenReturn(Arrays.asList(
                new EventWithPosition(
                        new Event("E001", "2023-10-01T10:05:00", "System Startup", "Info"),
                        new GeoPosition(34.0522, -118.2437)
                )
        ));
    }

    @Test
    public void testGetPositionForEvent_Found() throws Exception {
        mockMvc.perform(get("/api/location-service/event/E001/position")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.latitude").value(34.0522))
                .andExpect(jsonPath("$.longitude").value(-118.2437));
    }

    @Test
    public void testGetPositionForEvent_NotFound() throws Exception {
        mockMvc.perform(get("/api/location-service/event/E999/position")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testGetAllEventsWithPositions_NotEmpty() throws Exception {
        mockMvc.perform(get("/api/location-service/events/positions")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].event.id").value("E001"))
                .andExpect(jsonPath("$[0].position.latitude").value(34.0522))
                .andExpect(jsonPath("$[0].position.longitude").value(-118.2437));
    }

    @Test
    public void testGetAllEventsWithPositions_Empty() throws Exception {
        Mockito.when(locationService.getAllEventsWithPositions()).thenReturn(List.of());
        mockMvc.perform(get("/api/location-service/events/positions")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

}