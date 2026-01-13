package com.minor.photo_app.repository.specification;

import com.minor.photo_app.entity.Category;
import com.minor.photo_app.entity.Place;
import com.minor.photo_app.entity.User;
import com.minor.photo_app.enums.TransportType;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@NoArgsConstructor
public final class PlaceSpecification {

    public static Specification<Place> hasCategory(Set<Long> categoryIds) {
        return (root, query, criteriaBuilder) -> {
            if (categoryIds == null || categoryIds.isEmpty()) {
                return criteriaBuilder.conjunction();
            }

            Subquery<Long> subquery = query.subquery(Long.class);
            Root<Place> subroot = subquery.from(Place.class);
            Join<Place, Category> placeCategoryJoin = subroot.join("categories");

            subquery.select(subroot.get("id"))
                    .where(
                            criteriaBuilder.equal(subroot.get("id"), root.get("id")),
                            placeCategoryJoin.get("id").in(categoryIds)
                    );

            return criteriaBuilder.exists(subquery);
        };
    }

    public static Specification<Place> isFavoriteByUserId(Long userId) {
        return (root, query, criteriaBuilder) -> {

            Subquery<Long> subquery = query.subquery(Long.class);
            Root<User> subroot = subquery.from(User.class);
            Join<User, Place> favoritePlaceJoin = subroot.join("favoritePlaces");

            subquery.select(favoritePlaceJoin.get("id"))
                    .where(criteriaBuilder.equal(subroot.get("id"), userId));

            return criteriaBuilder.exists(subquery);
        };
    }

    public static Specification<Place> isOpenInDaysAndTime(Set<DayOfWeek> days, LocalTime from, LocalTime to) {
        return (root, query, criteriaBuilder) -> {

            if (days==null || days.isEmpty()) {
                String jsonPathFilter = String.format("$.*[*] ? (@.from < \"%s\" && @.to > \"%s\")",
                        to == null ? LocalTime.MAX : to,
                        from == null ? LocalTime.MIN : from);
                return criteriaBuilder.isTrue(
                        criteriaBuilder.function(
                                "jsonb_path_exists",
                                Boolean.class,
                                root.get("workingHours"),
                                criteriaBuilder.literal(jsonPathFilter)
                        )
                );
            }

            List<Predicate> dayPredicates = new ArrayList<>();
            for (DayOfWeek day: days) {
                String dayNameShort = day.name().substring(0, 3).toLowerCase();
                String jsonPathFilter = String.format("$.%s[*] ? (@.from < \"%s\" && @.to > \"%s\")",
                        dayNameShort,
                        to == null ? LocalTime.MAX : to,
                        from == null ? LocalTime.MIN : from);


                dayPredicates.add(criteriaBuilder.isTrue(
                        criteriaBuilder.function(
                                "jsonb_path_exists",
                                Boolean.class,
                                root.get("workingHours"),
                                criteriaBuilder.literal(jsonPathFilter)
                        ))
                );
            }

            return criteriaBuilder.or(dayPredicates.toArray(new Predicate[0]));
        };
    }

    public static Specification<Place> filterLocationDescription(Map<TransportType, Set<String>> selectedStops) {
        return (root, query, criteriaBuilder) -> {

            if (selectedStops.isEmpty()) {
                return criteriaBuilder.conjunction();
            }

            List<Predicate> predicates = new ArrayList<>();
           selectedStops.forEach((transportType, stops) -> {
               if (stops != null) {
                   for (String stop: stops) {
                       String jsonPathFilter = String.format("$.%s[*] ? (@ == \"%s\")",
                               transportType.getType(),
                               stop
                       );

                       predicates.add(criteriaBuilder.isTrue(
                               criteriaBuilder.function(
                                       "jsonb_path_exists",
                                       Boolean.class,
                                       root.get("locationDescription"),
                                       criteriaBuilder.literal(jsonPathFilter)
                               ))
                       );
                   }
               }
           });

            return criteriaBuilder.or(predicates.toArray(new Predicate[0]));
        };
    }

    public static Specification<Place> filterVisitCost(BigDecimal minPrice, BigDecimal maxPrice) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (minPrice != null) {
                predicates.add(criteriaBuilder.ge(root.get("visitCost"), minPrice));
            }

            if (maxPrice != null) {
                predicates.add(criteriaBuilder.le(root.get("visitCost"), maxPrice));
            }

            return criteriaBuilder.and(predicates.toArray(Predicate[]::new));
        };
    }
}
