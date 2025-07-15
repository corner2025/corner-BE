package com.corner.travel.festival.repository;

import com.corner.travel.festival.domain.Festival;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class FestivalRepositoryImpl implements FestivalRepositoryCustom {

    @PersistenceContext
    private EntityManager em;

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("yyyyMMdd");

    @Override
    public Page<Festival> findByFilters(String startDate, String endDate, String location, String title, Pageable pageable) {
        CriteriaBuilder cb = em.getCriteriaBuilder();

        // 메인 쿼리
        CriteriaQuery<Festival> cq = cb.createQuery(Festival.class);
        Root<Festival> root = cq.from(Festival.class);

        List<Predicate> predicates = new ArrayList<>();

        if (startDate != null && !startDate.isEmpty()) {
            LocalDate sDate = LocalDate.parse(startDate, FMT);
            predicates.add(cb.greaterThanOrEqualTo(root.get("eventStartDate"), sDate));
        }
        if (endDate != null && !endDate.isEmpty()) {
            LocalDate eDate = LocalDate.parse(endDate, FMT);
            predicates.add(cb.lessThanOrEqualTo(root.get("eventEndDate"), eDate));
        }
        if (location != null && !location.isEmpty()) {
            predicates.add(cb.or(
                    cb.like(root.get("addr1"), "%" + location + "%"),
                    cb.like(root.get("addr2"), "%" + location + "%")
            ));
        }
        if (title != null && !title.isEmpty()) {
            predicates.add(cb.like(root.get("title"), "%" + title + "%"));
        }

        cq.where(predicates.toArray(new Predicate[0]));
        cq.orderBy(cb.asc(root.get("eventStartDate")));

        List<Festival> resultList = em.createQuery(cq)
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize())
                .getResultList();

        // ✅ count 쿼리에서는 별도의 Root와 Predicate 재작성
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<Festival> countRoot = countQuery.from(Festival.class);
        List<Predicate> countPredicates = new ArrayList<>();

        if (startDate != null && !startDate.isEmpty()) {
            LocalDate sDate = LocalDate.parse(startDate, FMT);
            countPredicates.add(cb.greaterThanOrEqualTo(countRoot.get("eventStartDate"), sDate));
        }
        if (endDate != null && !endDate.isEmpty()) {
            LocalDate eDate = LocalDate.parse(endDate, FMT);
            countPredicates.add(cb.lessThanOrEqualTo(countRoot.get("eventEndDate"), eDate));
        }
        if (location != null && !location.isEmpty()) {
            countPredicates.add(cb.or(
                    cb.like(countRoot.get("addr1"), "%" + location + "%"),
                    cb.like(countRoot.get("addr2"), "%" + location + "%")
            ));
        }
        if (title != null && !title.isEmpty()) {
            countPredicates.add(cb.like(countRoot.get("title"), "%" + title + "%"));
        }

        countQuery.select(cb.count(countRoot))
                .where(countPredicates.toArray(new Predicate[0]));

        Long total = em.createQuery(countQuery).getSingleResult();

        return new PageImpl<>(resultList, pageable, total);
    }
}
