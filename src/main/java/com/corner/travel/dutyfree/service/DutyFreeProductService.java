package com.corner.travel.dutyfree.service;

import com.corner.travel.dutyfree.dto.RankingDto;
import com.corner.travel.dutyfree.repository.DutyFreeProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DutyFreeProductService {

    private final DutyFreeProductRepository repo;

    /**
     * 월간 판매량 랭킹 조회
     * @param yearMonth yyyy-MM 포맷, ex: "2023-07"
     * @param limit     상위 N개
     */
    public List<RankingDto> getMonthlyRanking(String yearMonth, int limit) {
        var page = PageRequest.of(0, limit);
        return repo.findMonthlyCategorySales(yearMonth, page);
    }

    /**
     * 연간 판매량 랭킹 조회
     * @param year  yyyy 포맷, ex: "2023"
     * @param limit 상위 N개
     */
    public List<RankingDto> getAnnualRanking(String year, int limit) {
        var page = PageRequest.of(0, limit);
        return repo.findAnnualCategorySales(year + "%", page);
    }
}
