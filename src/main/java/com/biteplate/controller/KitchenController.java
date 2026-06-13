package com.biteplate.controller;

import com.biteplate.dto.ApiMessage;
import com.biteplate.dto.ReprioritiseRequest;
import com.biteplate.service.KitchenService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/kitchen")
public class KitchenController {
    private final KitchenService kitchenService;
    public KitchenController(KitchenService kitchenService) { this.kitchenService = kitchenService; }

    @GetMapping("/queue")
    public List<String> queue() { return kitchenService.queue(); }

    @PostMapping("/process-next")
    public ApiMessage processNext() { return new ApiMessage(kitchenService.processNext()); }

    @PostMapping("/undo")
    public ApiMessage undo() { return new ApiMessage(kitchenService.undo()); }

    @PostMapping("/reprioritise")
    public ApiMessage reprioritise(@Valid @RequestBody ReprioritiseRequest request) {
        return new ApiMessage(kitchenService.reprioritise(request.orderId()));
    }

    @PostMapping("/cancel/{orderId}")
    public ApiMessage cancel(@PathVariable String orderId) { return new ApiMessage(kitchenService.cancel(orderId)); }
}
