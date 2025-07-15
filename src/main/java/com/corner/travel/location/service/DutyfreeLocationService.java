package com.corner.travel.location.service;

import com.corner.travel.location.dto.DutyfreeLocationDto;
import com.corner.travel.location.entity.DutyfreeLocation;
import com.corner.travel.location.repository.DutyfreeLocationRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DutyfreeLocationService {

    private final DutyfreeLocationRepository repository;

    public DutyfreeLocationService(DutyfreeLocationRepository repository) {
        this.repository = repository;
    }

    public List<DutyfreeLocationDto> getAllLocations() {
        List<DutyfreeLocation> entities = repository.findAll();
        return entities.stream()
                .map(e -> new DutyfreeLocationDto(e.getName(), e.getLatitude(), e.getLongitude()))
                .collect(Collectors.toList());
    }
}
