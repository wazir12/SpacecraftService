package org.solenix.location_service.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventWithPosition {
    private Event event;
    private GeoPosition position;
}
