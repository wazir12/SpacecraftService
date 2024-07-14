package org.solenix.location_service.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Event {
    private String id;
    private String occurrence_time;
    private String event_name;
    private String severity;
}
