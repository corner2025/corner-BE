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
        List<RankingDto> list = repo.findMonthlyCategorySales(yearMonth, page);

        System.out.println("=== getMonthlyRanking 호출 ===");
        System.out.println("파라미터 → yearMonth=" + yearMonth + ", limit=" + limit);
        System.out.println("리스트 사이즈 → " + list.size());
        for (RankingDto dto : list) {
            System.out.println(dto.getCategory() + " = " + dto.getTotalSales());
        }
        System.out.println("==============================");

        return list;
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
