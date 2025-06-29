package com.corner.travel.TouristSpot.service;


import com.corner.travel.TouristSpot.domain.TouristSpot;
import com.corner.travel.TouristSpot.repository.TouristSpotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TouristSpotService {

    private final TouristSpotRepository touristSpotRepository;

    public List<TouristSpot> getAllTouristSpots(){
        return  touristSpotRepository.findAll();
    }

    public TouristSpot getTouristSpotById(Long id){
        return touristSpotRepository.findById(id).orElse(null);
    }

    //Create
    public TouristSpot createTouristSpot(TouristSpot spot){
        return touristSpotRepository.save(spot);
    }

    //Update
    public TouristSpot updateTouristSpot(Long id, TouristSpot updatedSpot) {
        return touristSpotRepository.findById(id).map(existing -> {
            existing.setName(updatedSpot.getName());
            existing.setRegion(updatedSpot.getRegion());
            existing.setCategory(updatedSpot.getCategory());
            existing.setDescription(updatedSpot.getDescription());
            return touristSpotRepository.save(existing);
        }).orElse(null);
    }
    //Delete
    public void deleteTouristSpot(Long id) {
        touristSpotRepository.deleteById(id);
    }




}
