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

    private static final DateTimeFormatter FRONT_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Override
    public Page<Festival> findByFilters(String startDate, String endDate, String location, String title, Pageable pageable) {
        CriteriaBuilder cb = em.getCriteriaBuilder();

        // 메인 쿼리
        CriteriaQuery<Festival> cq = cb.createQuery(Festival.class);
        Root<Festival> root = cq.from(Festival.class);

        List<Predicate> predicates = new ArrayList<>();

        if (startDate != null && endDate != null && !startDate.isEmpty() && !endDate.isEmpty()) {
            LocalDate sDate = LocalDate.parse(startDate, FRONT_FMT);
            LocalDate eDate = LocalDate.parse(endDate, FRONT_FMT);

            predicates.add(cb.or(
                    cb.between(root.get("eventStartDate"), sDate, eDate),
                    cb.between(root.get("eventEndDate"), sDate, eDate),
                    cb.and(
                            cb.lessThanOrEqualTo(root.get("eventStartDate"), sDate),
                            cb.greaterThanOrEqualTo(root.get("eventEndDate"), eDate)
                    )
            ));
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

        // 카운트 쿼리
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<Festival> countRoot = countQuery.from(Festival.class);
        List<Predicate> countPredicates = new ArrayList<>();

        if (startDate != null && endDate != null && !startDate.isEmpty() && !endDate.isEmpty()) {
            LocalDate sDate = LocalDate.parse(startDate, FRONT_FMT);
            LocalDate eDate = LocalDate.parse(endDate, FRONT_FMT);

            countPredicates.add(cb.or(
                    cb.between(countRoot.get("eventStartDate"), sDate, eDate),
                    cb.between(countRoot.get("eventEndDate"), sDate, eDate),
                    cb.and(
                            cb.lessThanOrEqualTo(countRoot.get("eventStartDate"), sDate),
                            cb.greaterThanOrEqualTo(countRoot.get("eventEndDate"), eDate)
                    )
            ));
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
