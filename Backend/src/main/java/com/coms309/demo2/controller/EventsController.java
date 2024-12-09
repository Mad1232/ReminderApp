package com.coms309.demo2.controller;

import com.coms309.demo2.entity.Events;
import com.coms309.demo2.repository.EventsRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Fury Poudel and Madeleine Carydis
 * Creates events
 */
@RestController
@Tag(name = "Events Controller", description = "Manages creation of reminders related to eye drop for pets")

@RequestMapping("/events")
public class EventsController {

    @Autowired
    private EventsRepository eventsRepository;
    /**
     * Creates event
     * @param event
     * @return the event that was created
     */
    @Operation(summary = "Creates a new reminder for a user")
    @PostMapping
    public ResponseEntity<Events> addNewEvent(@RequestBody Events event) {
        event = eventsRepository.save(event);
        return ResponseEntity.ok(event);
    }
}

