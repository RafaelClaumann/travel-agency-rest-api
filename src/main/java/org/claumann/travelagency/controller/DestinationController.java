package org.claumann.travelagency.controller;

import org.claumann.travelagency.controller.dto.in.DestinationRequest;
import org.claumann.travelagency.controller.dto.in.RatingRequest;
import org.claumann.travelagency.model.Destination;
import org.claumann.travelagency.service.DestinationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/destinations")
public class DestinationController {

    private final DestinationService destinationService;

    public DestinationController(DestinationService destinationService) {
        this.destinationService = destinationService;
    }

    @PostMapping
    public ResponseEntity<Destination> create(@RequestBody DestinationRequest request) {
        var created = destinationService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping
    public ResponseEntity<List<Destination>> findAll() {
        return ResponseEntity.ok(destinationService.findAll());
    }

    @GetMapping("/search")
    public ResponseEntity<List<Destination>> search(
            @RequestParam(required = false, defaultValue = "") String name,
            @RequestParam(required = false, defaultValue = "") String location) {
        return ResponseEntity.ok(destinationService.search(name, location));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Destination> findById(@PathVariable Long id) {
        return ResponseEntity.ok(destinationService.findById(id));
    }

    @PatchMapping("/{id}/rating")
    public ResponseEntity<Destination> rate(@PathVariable Long id, @RequestBody RatingRequest request) {
        return ResponseEntity.ok(destinationService.rate(id, request.getRating()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        destinationService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
