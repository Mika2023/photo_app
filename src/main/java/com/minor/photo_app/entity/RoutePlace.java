package com.minor.photo_app.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "route_places")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RoutePlace {

    @EmbeddedId
    private RoutePlaceId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("routeId")
    @JoinColumn(name = "route_id")
    private Route route;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("placeId")
    @JoinColumn(name = "place_id")
    private Place place;

    @Column(name = "position", nullable = false)
    private Integer position;
}
