package com.corner.travel.dutyfree.controller;

import com.corner.travel.dutyfree.dto.DutyFreeProdectDto;
import com.corner.travel.dutyfree.service.DutyFreeProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
        import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/admin/dutyfree/products")
public class DutyFreeProductController {
    private final DutyFreeProductService service;

    public DutyFreeProductController(DutyFreeProductService service) {
        this.service = service;
    }

    @PostMapping("/upload")
    public ResponseEntity<DutyFreeProdectDto> uploadCsv(@RequestParam("file") MultipartFile file) {
        int count = service.loadFromCsv(file);
        return ResponseEntity.ok(new DutyFreeProdectDto(count + "건 업로드 완료"));
    }
}
