package com.minor.photo_app.service;

import com.minor.photo_app.dto.recommendation.PlaceCardWithScoreDto;
import com.minor.photo_app.entity.Place;
import com.minor.photo_app.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class RecommendationAlgorithmService {
    private static final double WEIGHT_FAVORITE_PLACES = 3.0;
    private static final double WEIGHT_ROUTE = 1.0;
    private static final double MAX_RADIUS_METERS = 10000.0;
    private static final int RADIUS_EARTH = 6371000;
    private static final String CATEGORY_PREFIX = "CAT_";
    private static final String TAG_PREFIX = "TAG_";

    private final UserRepository userRepository;

    public List<Place> getRecommendedPlaces(Long userId, Point userLocation, List<Place> unVisitedPlaces) {
        Map<String, Double> userWeights = getWeights(userId);
        boolean isNewUser = userWeights.isEmpty();

        List<PlaceCardWithScoreDto> recommendedPlaces = new ArrayList<>();
        for (Place place : unVisitedPlaces) {
            double matchScore = isNewUser ? 0.5 : calculateCosineSimilarity(userWeights, place);

            double distance = calculateDistance(userLocation, place.getLocation());
            double locationScore = Math.max(0, 1.0 - (distance / MAX_RADIUS_METERS));

            double finalScore = (matchScore * 0.7) + (locationScore * 0.3);

            if (finalScore > 0.1) {
                recommendedPlaces.add(new PlaceCardWithScoreDto()
                        .setPlace(place)
                        .setScore(matchScore)
                        .setDistanceMeters(distance)
                        .setFinalScore(finalScore)
                );
            }
        }

        recommendedPlaces.sort((place1, place2) ->
                Double.compare(place2.getFinalScore(), place1.getFinalScore()));

        return recommendedPlaces.stream().
                map(PlaceCardWithScoreDto::getPlace)
                .toList();
    }

    private Map<String, Double> getWeights(Long userId) {
        Map<String, Double> weights = new HashMap<>();

        userRepository.countFavoriteCategories(userId).forEach(category ->
                weights.put(CATEGORY_PREFIX + category.getFeatureId(), WEIGHT_FAVORITE_PLACES * category.getFeatureCount()));
        userRepository.countFavoriteTags(userId).forEach(tag ->
                weights.put(TAG_PREFIX + tag.getFeatureId(), WEIGHT_FAVORITE_PLACES * tag.getFeatureCount()));

        userRepository.countRouteCategories(userId).forEach(category ->
                weights.merge(CATEGORY_PREFIX+ category.getFeatureId(), WEIGHT_ROUTE * category.getFeatureCount(), Double::sum));
        userRepository.countRouteTags(userId).forEach(tag ->
                weights.merge(TAG_PREFIX + tag.getFeatureId(), WEIGHT_ROUTE * tag.getFeatureCount(), Double::sum));

        return weights;
    }

    private double calculateCosineSimilarity(Map<String, Double> userWeights, Place place) {
        List<String> placeFeatures = extractFeatures(place);
        if (CollectionUtils.isEmpty(placeFeatures)) return 0.0;

        double similarity = 0.0;
        double placeVectorLength = Math.sqrt(placeFeatures.size());

        for (String feature : placeFeatures) {
            if (userWeights.containsKey(feature)) {
                similarity += userWeights.get(feature);
            }
        }

        double userVectorLengthSq = userWeights.values().stream()
                .mapToDouble(weight -> Math.pow(weight, 2))
                .sum();
        double userVectorLength = Math.sqrt(userVectorLengthSq);

        if (userVectorLength == 0 || placeVectorLength == 0) return 0.0;

        return similarity / (userVectorLength * placeVectorLength);
    }

    private List<String> extractFeatures(Place place) {
        List<String> features = new ArrayList<>();

        if (CollectionUtils.isNotEmpty(place.getCategories())) {
            place.getCategories().forEach(category -> features.add(CATEGORY_PREFIX + category.getId()));
        }
        if (CollectionUtils.isNotEmpty(place.getTags())) {
            place.getTags().forEach(tag -> features.add(TAG_PREFIX + tag.getId()));
        }
        return features;
    }

    private double calculateDistance(Point p1, Point p2) {
        if (Objects.isNull(p1) || Objects.isNull(p2)) return MAX_RADIUS_METERS;

        double lat1 = Math.toRadians(p1.getY());
        double lon1 = Math.toRadians(p1.getX());
        double lat2 = Math.toRadians(p2.getY());
        double lon2 = Math.toRadians(p2.getX());

        double distLat = lat2 - lat1;
        double distLon = lon2 - lon1;


        double a = Math.sin(distLat / 2) * Math.sin(distLat / 2) +
                        Math.cos(lat1) * Math.cos(lat2) *
                        Math.sin(distLon / 2) * Math.sin(distLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return RADIUS_EARTH * c;
    }
}
