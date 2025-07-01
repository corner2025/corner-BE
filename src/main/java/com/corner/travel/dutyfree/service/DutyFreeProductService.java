package com.corner.travel.dutyfree.service;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.corner.travel.dutyFree.domain.DutyFreeProduct;
import com.corner.travel.dutyFree.repository.DutyFreeProductRepository;
import com.opencsv.exceptions.CsvValidationException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class DutyFreeProductService {
    private final DutyFreeProductRepository repo;

    public DutyFreeProductService(DutyFreeProductRepository repo) {
        this.repo = repo;
    }

    public int loadFromCsv(MultipartFile file) {
        List<DutyFreeProduct> batch = new ArrayList<>();
        int total = 0;

        try (Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream()));
             CSVReader csv = new CSVReaderBuilder(reader).withSkipLines(1).build()) {

            String[] line;
            while ((line = csv.readNext()) != null) {
                String ym   = line[0];                 // ex: "2025-06"
                String cat  = line[1];                 // ex: "화장품"
                Integer cnt = Integer.valueOf(line[2]); // ex: "12345"
                batch.add(new DutyFreeProduct(ym, cat, cnt));
                total++;

                if (batch.size() >= 500) {
                    repo.saveAll(batch);
                    batch.clear();
                }
            }
            if (!batch.isEmpty()) {
                repo.saveAll(batch);
            }
        } catch (IOException e) {
            throw new RuntimeException("CSV 파싱 실패", e);
        } catch (CsvValidationException e) {
            throw new RuntimeException(e);
        }
        return total;
    }
}
