package org.solenix.location_service.controller;

import org.solenix.location_service.model.EventWithPosition;
import org.solenix.location_service.model.GeoPosition;
import org.solenix.location_service.service.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/location-service")
public class LocationServiceController {

    private final LocationService locationService;

    @Autowired
    public LocationServiceController(LocationService locationService) {
        this.locationService = locationService;
    }

    @GetMapping("/event/{id}/position")
    public ResponseEntity<GeoPosition> getPositionForEvent(@PathVariable String id) {
        return locationService.getGeoPositionForEvent(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/events/positions")
    public ResponseEntity<List<EventWithPosition>> getAllEventsWithPositions() {
        List<EventWithPosition> eventsWithPositions = locationService.getAllEventsWithPositions();
        if (eventsWithPositions.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(eventsWithPositions, HttpStatus.OK);
    }
}
