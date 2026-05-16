package com.minor.photo_app.dto.recommendation;

/**
 * Проекция для подсчета количества повторений фичи (категории или тега).
 */
public interface FeatureCountProjection {
    /**
     * Id фичи (категории или тега)
     */
    Long getFeatureId();

    /**
     * Количество повторений фичи
     */
    Long getFeatureCount();
}
