package com.minor.photo_app.entity;

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

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "favorite_places")
public class FavoritePlace {
        @EmbeddedId
        private FavoritePlaceId id;

        @ManyToOne(fetch = FetchType.LAZY)
        @MapsId("userId")
        @JoinColumn(name = "user_id")
        private User user;

        @ManyToOne(fetch = FetchType.LAZY)
        @MapsId("placeId")
        @JoinColumn(name = "place_id")
        private Place place;
}
