package com.minor.photo_app.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.minor.photo_app.dto.UserPrincipal;
import com.minor.photo_app.dto.filters.PlaceFilter;
import com.minor.photo_app.dto.request.PlaceCreationRequest;
import com.minor.photo_app.dto.request.PlaceSearchRequest;
import com.minor.photo_app.dto.request.PlaceUpdateRequest;
import com.minor.photo_app.dto.response.PlaceCardResponse;
import com.minor.photo_app.dto.response.PlaceResponse;
import com.minor.photo_app.entity.Category;
import com.minor.photo_app.entity.FavoritePlace;
import com.minor.photo_app.entity.Place;
import com.minor.photo_app.enums.PlaceSort;
import com.minor.photo_app.exception.NotFoundException;
import com.minor.photo_app.mapper.PlaceMapper;
import com.minor.photo_app.repository.PlaceRepository;
import com.minor.photo_app.repository.specification.PlaceSpecification;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class PlaceService {
    private final PlaceMapper placeMapper;
    private final PlaceRepository placeRepository;
    private final UserLocationService userLocationService;
    private final FavoritePlaceService favoritePlaceService;
    private final CategoryService categoryService;
    private static final double RADIUS_METERS = 1000.0;
    private static final int LIMIT_PLACES = 10;

    private Slice<PlaceCardResponse> getFilteredPlaces(Specification<Place> spec,
                                                      Integer page,
                                                      Integer size,
                                                      PlaceSort sort,
                                                      UserPrincipal userPrincipal) {
        page = page == null ? 0 : page;
        size = size == null ? 10 : size;
        sort = sort == null ? PlaceSort.DEFAULT : sort;
        Pageable pageable = PageRequest.of(page, size+1);

        List<Place> places = switch (sort) {
            case NEWEST -> {
                pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
                yield placeRepository.findAll(spec,pageable).getContent();
            }
            case PRICE_ASC -> {
                pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "visitCost"));
                yield placeRepository.findAll(spec,pageable).getContent();
            }
            case PRICE_DESC -> {
                pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "visitCost"));
                yield placeRepository.findAll(spec,pageable).getContent();
            }
            case CLOSEST -> {
                Point userLocation = userLocationService.getUserLocationPoint(userPrincipal);
                Double lon = userLocation.getX();
                Double lat = userLocation.getY();
                yield placeRepository.findAll((root, query, cb) -> {
                    Expression<Double> distance = cb.function(
                            "ST_Distance",
                            Double.class,

                            cb.function(
                                    "ST_Transform",
                                    Object.class,
                                    root.get("location"),
                                    cb.literal(3857)
                            ),

                            cb.function(
                                    "ST_Transform",
                                    Object.class,
                                    cb.function(
                                            "ST_SetSRID",
                                            Object.class,
                                            cb.function(
                                                    "ST_MakePoint",
                                                    Object.class,
                                                    cb.literal(lon),
                                                    cb.literal(lat)
                                            ),
                                            cb.literal(4326)
                                    ),
                                    cb.literal(3857)
                            )
                    );
                    query.orderBy(cb.asc(distance));
                    return spec.toPredicate(root, query, cb);
                }, pageable).getContent();
            }
            case POPULARITY ->
                placeRepository.findAll((root, query, cb) -> {
                    query.groupBy(root.get("id"));
                    Join<Place, FavoritePlace> join = root.join("favoritePlaceUsers", JoinType.LEFT);
                    query.orderBy(cb.desc(cb.count(join.get("user"))));
                    return spec.toPredicate(root, query, cb);
                }, pageable).getContent();
            default -> placeRepository.findAll(spec, pageable).getContent();
        };

        boolean hasNext = places.size() > size;
        if (hasNext) {
            places = places.subList(0, size);
        }

        Set<Long> placeFavoriteIds = favoritePlaceService.getFavoritePlacesByUser(userPrincipal);
        List<PlaceCardResponse> response = placeMapper.toCardResponseList(places, placeFavoriteIds);
        return new SliceImpl<>(response, PageRequest.of(page, size), hasNext);
    }

    @Transactional(readOnly = true)
    public Slice<PlaceCardResponse> findPlacesByFilter(UserPrincipal userPrincipal, PlaceFilter placeFilter) {
        Specification<Place> specification = PlaceSpecification.hasCategory(placeFilter.getCategoryIds());

        if (placeFilter.getMaxPrice() != null || placeFilter.getMinPrice() != null) {
            specification = specification
                    .and(PlaceSpecification.filterVisitCost(placeFilter.getMinPrice(),placeFilter.getMaxPrice()));
        }

        if (placeFilter.getIsFavoriteByUser() != null && userPrincipal.getId() != null) {
            if (placeFilter.getIsFavoriteByUser())
                specification = specification.and(PlaceSpecification.isFavoriteByUserId(userPrincipal.getId()));
        }

        if (placeFilter.getSelectedStops() != null) {
            specification = specification
                    .and(PlaceSpecification.filterLocationDescription(placeFilter.getSelectedStops()));
        }

        if (placeFilter.getWorkingHours() != null) {
            specification = specification
                    .and(PlaceSpecification.isOpenInDaysAndTime(placeFilter.getWorkingHours().getOpenDays(),
                            placeFilter.getWorkingHours().getFrom(),
                            placeFilter.getWorkingHours().getTo()
                    ));
        }

        return getFilteredPlaces(specification,
                placeFilter.getPage(),
                placeFilter.getSize(),
                placeFilter.getSort(),
                userPrincipal);
    }

    @Transactional(readOnly = true)
    public List<PlaceCardResponse> getPlacesBySearch(UserPrincipal userPrincipal, PlaceSearchRequest request) {
        if (request.getName().length()<3) {
            return List.of();
        }

        List<Place> places = placeRepository.findAllByNameContainingIgnoreCase(request.getName());
        return getPlaceCardResponses(userPrincipal, places);
    }

    @Transactional(readOnly = true)
    public List<PlaceCardResponse> findPlacesNearby(Long placeId, UserPrincipal userPrincipal) {
        if (!placeRepository.existsById(placeId)) {
            throw new NotFoundException("Место не найдено по id: " + placeId);
        }

        Point location = placeRepository.findLocationById(placeId);
        List<Place> places = placeRepository.findPlacesNearby(placeId,
                location.getY(),
                location.getX(),
                RADIUS_METERS,
                LIMIT_PLACES);

        return getPlaceCardResponses(userPrincipal, places);
    }

    @Transactional(readOnly = true)
    public PlaceResponse getPlaceById(Long placeId, UserPrincipal userPrincipal) {
        Place place = placeRepository.findById(placeId)
                .orElseThrow(() -> new NotFoundException("Место не найдено по id: " + placeId));

        Set<Long> placeFavoriteIds = favoritePlaceService.getFavoritePlacesByUser(userPrincipal);
        return placeMapper.toResponse(place, placeFavoriteIds);
    }

    @Transactional
    public PlaceResponse createPlace(PlaceCreationRequest request, UserPrincipal userPrincipal) {
        Place place = placeMapper.toEntity(request);

        Point location = new GeometryFactory(new PrecisionModel(), 4326)
                .createPoint(new Coordinate(request.getLongitude(), request.getLatitude()));
        location.setSRID(4326);
        place.setLocation(location);

        Set<Category> categories = categoryService.getExistCategoriesByIds(request.getCategoryIds());
        place.getCategories().addAll(categories);

        Place saved = placeRepository.save(place);
        Set<Long> favoritePlacesIds = favoritePlaceService.getFavoritePlacesByUser(userPrincipal);
        return placeMapper.toResponse(saved, favoritePlacesIds);
    }

    @Transactional
    public PlaceResponse updatePlace(Long placeId, UserPrincipal userPrincipal, PlaceUpdateRequest request) {
        Place place = placeRepository.findById(placeId)
                .orElseThrow(() -> new NotFoundException("Место не найдено по id: " + placeId));

        placeMapper.updatePlace(request, place);

        Set<Long> favoritePlacesIds = favoritePlaceService.getFavoritePlacesByUser(userPrincipal);
        return placeMapper.toResponse(place, favoritePlacesIds);
    }

    @Transactional
    public void deletePlace(Long placeId) {
        Place place = placeRepository.findById(placeId)
                .orElseThrow(() -> new NotFoundException("Место не найдено по id: " + placeId));

        placeRepository.delete(place);
    }

    @Transactional
    public void removeCategoryFromPlace(Long placeId, Long categoryId) {
        Place place = placeRepository.findById(placeId)
                .orElseThrow(() -> new NotFoundException("Место не найдено по id: " + placeId));

        Category category = categoryService.getCategoryFromId(categoryId);
        if (!place.getCategories().contains(category)) {
            throw new IllegalArgumentException(String.format("Категории с id = %s нет у места с id = %s",
                    categoryId, placeId));
        }

        place.getCategories().remove(category);
        removeUnneededParent(place, category);
    }

    private void removeUnneededParent(Place place, Category removingCategory) {
        Category parent = removingCategory.getParent();

        while (parent != null) {
            Category finalParent = parent;
            boolean stillNeeded = place.getCategories().stream()
                    .anyMatch(c -> isDescendantOf(c, finalParent));

            if(stillNeeded) return;

            place.getCategories().remove(parent);
            parent = parent.getParent();
        }
    }

    public boolean isDescendantOf(Category category, Category ancestor) {
        Category current = category;

        while (current != null) {
            if (current.equals(ancestor)) return true;
            current = current.getParent();
        }
        return false;
    }

    private List<PlaceCardResponse> getPlaceCardResponses(UserPrincipal userPrincipal, List<Place> places) {
        Set<Long> placeFavoriteIds = favoritePlaceService.getFavoritePlacesByUser(userPrincipal);
        return placeMapper.toCardResponseList(places, placeFavoriteIds);
    }
}
