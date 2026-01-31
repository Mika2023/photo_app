package com.minor.photo_app.customConverter;

import com.minor.photo_app.dto.response.mapsResponse.Geometry;
import com.minor.photo_app.dto.response.mapsResponse.Maneuver;
import com.minor.photo_app.dto.response.mapsResponse.OutcomingPath;
import com.minor.photo_app.exception.PhotoAppException;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.PrecisionModel;
import org.locationtech.jts.io.WKTReader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class DoubleGisConverter {
    private static final GeometryFactory GEOMETRY_FACTORY = new GeometryFactory(new PrecisionModel(), 4326);
    private static final WKTReader WKT_READER = new WKTReader(GEOMETRY_FACTORY);

    public static LineString convertManeuversToLineString(List<Maneuver> maneuvers) {
        if (maneuvers == null || maneuvers.isEmpty()) {
            throw new PhotoAppException("Список маневров из ответа от карт пуст");
        }

        List<Coordinate> coordinates = new ArrayList<>();
        maneuvers.stream()
                .filter(Objects::nonNull)
                .map(Maneuver::getOutcomingPath)
                .filter(Objects::nonNull)
                .map(OutcomingPath::getGeometry)
                .filter(geometries -> geometries != null && !geometries.isEmpty())
                .forEach(geometries -> {
                    for (Geometry geometry : geometries) {

                        if (geometry.getSelection() == null) {
                            throw new PhotoAppException(
                                    "Не получилось конвертировать маневры в LineString - LineString отсутствует в Geometry"
                            );
                        }

                        try {
                            LineString lineString = (LineString) WKT_READER.read(geometry.getSelection());
                            coordinates.addAll(Arrays.asList(lineString.getCoordinates()));
                        } catch (Exception e) {
                            throw new PhotoAppException(
                                    "Не получилось конвертировать маневры в LineString " + e.getMessage()
                            );
                        }
                    }
                });

        if (coordinates.isEmpty()) {
            throw new PhotoAppException(
                    "Ошибка при конвертации маневров в LineString - список координат после конвертации пуст"
            );
        }

        return GEOMETRY_FACTORY.createLineString(coordinates.toArray(new Coordinate[0]));
    }
}
