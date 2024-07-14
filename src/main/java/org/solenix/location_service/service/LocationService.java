package org.solenix.location_service.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.solenix.location_service.model.*;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class LocationService {

    private List<Latitude> latitudes;
    private List<Longitude> longitudes;
    private List<Event> events;

    @PostConstruct
    public void init() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        latitudes = Arrays.asList(objectMapper.readValue(new ClassPathResource("data/latitudes.json").getInputStream(), Latitude[].class));
        longitudes = Arrays.asList(objectMapper.readValue(new ClassPathResource("data/longitudes.json").getInputStream(), Longitude[].class));
        events = Arrays.asList(objectMapper.readValue(new ClassPathResource("data/events.json").getInputStream(), Event[].class));
    }

    public Optional<GeoPosition> getGeoPositionForEvent(String eventId) {
        Event event = events.stream().filter(e -> e.getId().equals(eventId)).findFirst().orElse(null);
        if (event == null) {
            return Optional.empty();
        }

        Latitude closestLatitude = findClosestLatitude(event.getOccurrence_time());
        Longitude closestLongitude = findClosestLongitude(event.getOccurrence_time());

        if (closestLatitude == null || closestLongitude == null) {
            return Optional.empty();
        }

        return Optional.of(new GeoPosition(closestLatitude.getPosition(), closestLongitude.getPosition()));
    }

    public List<EventWithPosition> getAllEventsWithPositions() {
        return events.stream().map(event -> {
            GeoPosition position = getGeoPositionForEvent(event.getId()).orElse(null);
            return new EventWithPosition(event, position);
        }).collect(Collectors.toList());
    }

    private Latitude findClosestLatitude(String occurrenceTime) {
        return latitudes.stream()
                .min(Comparator.comparingDouble(lat -> Math.abs(parseTimestamp(lat.getTimestamp()).getTime() - parseTimestamp(occurrenceTime).getTime())))
                .orElse(null);
    }

    private Longitude findClosestLongitude(String occurrenceTime) {
        return longitudes.stream()
                .min(Comparator.comparingDouble(lon -> Math.abs(parseTimestamp(lon.getTimestamp()).getTime() - parseTimestamp(occurrenceTime).getTime())))
                .orElse(null);
    }

    private Date parseTimestamp(String timestamp) {
        try {
            return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(timestamp);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
}
