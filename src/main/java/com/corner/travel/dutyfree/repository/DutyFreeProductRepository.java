package com.corner.travel.dutyfree.repository;

import com.corner.travel.dutyfree.domain.DutyFreeProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DutyFreeProductRepository extends JpaRepository<DutyFreeProduct, Long> {
    // 집계는 나중에  구현
}
