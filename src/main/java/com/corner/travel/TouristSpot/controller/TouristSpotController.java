package com.corner.travel.TouristSpot.controller;


import com.corner.travel.TouristSpot.domain.TouristSpot;
import com.corner.travel.TouristSpot.service.TouristSpotService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tourist-spots")
public class TouristSpotController {

    private final TouristSpotService touristSpotService;

    public TouristSpotController(TouristSpotService touristSpotService) {
        this.touristSpotService = touristSpotService;
    }

    @GetMapping
    public List<TouristSpot> getAllSpots(){
        return touristSpotService.getAllTouristSpots();
    }

    @GetMapping("/{id}")
    public TouristSpot getSpot(@PathVariable Long id){
        return touristSpotService.getTouristSpotById(id);
    }

    @PostMapping
    public TouristSpot createSpot(@RequestBody TouristSpot spot){
        return touristSpotService.createTouristSpot(spot);
    }

    @PutMapping("/{id}")
    public TouristSpot updateSpot(@PathVariable Long id, @RequestBody TouristSpot spot) {
        return touristSpotService.updateTouristSpot(id, spot);
    }


    @DeleteMapping("/{id}")
    public void deleteSpot(@PathVariable Long id){
        touristSpotService.deleteTouristSpot(id);
    }

}
