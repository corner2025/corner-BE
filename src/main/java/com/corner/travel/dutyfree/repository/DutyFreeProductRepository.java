// src/main/java/com/corner/travel/dutyfree/repository/DutyFreeProductRepository.java
package com.corner.travel.dutyfree.repository;

import com.corner.travel.dutyfree.domain.DutyFreeProduct;
import com.corner.travel.dutyfree.dto.RankingDto;              // ★ 추가
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DutyFreeProductRepository extends JpaRepository<DutyFreeProduct, Long> {

    @Query("""
      select new com.corner.travel.dutyfree.dto.RankingDto(p.category, sum(p.salesCount))
      from DutyFreeProduct p
      where p.yearMonth = :ym
      group by p.category
      order by sum(p.salesCount) desc
      """)
    List<RankingDto> findMonthlyCategorySales(
            @Param("ym") String yearMonth,
            Pageable pageable
    );

    @Query("""
      select new com.corner.travel.dutyfree.dto.RankingDto(p.category, sum(p.salesCount))
      from DutyFreeProduct p
      where p.yearMonth like :yearPattern
      group by p.category
      order by sum(p.salesCount) desc
      """)
    List<RankingDto> findAnnualCategorySales(
            @Param("yearPattern") String yearPattern,
            Pageable pageable
    );

}
