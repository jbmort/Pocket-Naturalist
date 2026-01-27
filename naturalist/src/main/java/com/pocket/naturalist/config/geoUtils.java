package com.pocket.naturalist.config;

// import org.springframework.data.geo.Point;
// import org.springframework.data.geo.Polygon;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LinearRing;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;

import java.util.List;
import java.util.stream.Collectors;

public class geoUtils {

    private static final GeometryFactory geometryFactory = new GeometryFactory();

    // public static boolean isPointInPolygon(Point point, Polygon parkPolygon) {
    //     // 1. Convert Spring points to JTS Coordinates
    //     List<Coordinate> coordinates = parkPolygon.getPoints().stream()
    //             .map(p -> new Coordinate(p.getX(), p.getY()))
    //             .collect(Collectors.toList());

    //     // GeoJson polygons and JTS LinearRings require the first and last point to be the same (closed loop)
    //     if (!coordinates.get(0).equals(coordinates.get(coordinates.size() - 1))) {
    //         coordinates.add(coordinates.get(0));
    //     }

    //     Coordinate[] coordsArray = coordinates.toArray(new Coordinate[0]);

    //     // 2. Create JTS LinearRing (boundary) and Polygon
    //     LinearRing ring = geometryFactory.createLinearRing(coordsArray);
    //     org.locationtech.jts.geom.Polygon jtsPolygon = geometryFactory.createPolygon(ring, null);

    //     // 3. Create JTS Point
    //     org.locationtech.jts.geom.Point jtsPoint = geometryFactory.createPoint(new Coordinate(point.getX(), point.getY()));

    //     // 4. Perform the check
    //     // contains() checks if the point is strictly inside (not on the boundary)
    //     // within() checks if the point is within or on the boundary
    //     return jtsPolygon.contains(jtsPoint);
    // }

    // public static Polygon createPolygonFromJTSPolygon(org.locationtech.jts.geom.Polygon jtsPolygon) {
    //     List<Point> springPoints =  java.util.Arrays.stream(jtsPolygon.getCoordinates())
    //             .map(coord -> new Point(coord.x, coord.y))
    //             .collect(Collectors.toList());

    //     return new Polygon(springPoints);
    // }

    // public static org.locationtech.jts.geom.Polygon createJTSPolygonFromPolygon(Polygon springPolygon) {
    //     List<Coordinate> coordinates = springPolygon.getPoints().stream()
    //             .map(p -> new Coordinate(p.getX(), p.getY()))
    //             .collect(Collectors.toList());

    //     // Ensure the polygon is closed
    //     if (!coordinates.get(0).equals(coordinates.get(coordinates.size() - 1))) {
    //         coordinates.add(coordinates.get(0));
    //     }

    //     Coordinate[] coordsArray = coordinates.toArray(new Coordinate[0]);
    //     LinearRing ring = geometryFactory.createLinearRing(coordsArray);
    //     return geometryFactory.createPolygon(ring, null);
    // }
}