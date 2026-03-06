package com.pocket.naturalist.config;

import org.locationtech.jts.algorithm.Centroid;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class geoUtils {
    private geoUtils() {
    }

    public static Point calculateCenter(List<Polygon> boundaries){
        GeometryFactory geoFactory = new GeometryFactory();
        if(boundaries == null || boundaries.isEmpty()){
            return null;
        }

        if(boundaries.size() == 1){
            Centroid center = new Centroid(boundaries.get(0));
            return geoFactory.createPoint(center.getCentroid());
        }

        List<Point> centers = boundaries.stream().map(boundary -> {
            Centroid center = new Centroid(boundary);
            return geoFactory.createPoint(center.getCentroid());
        }).toList();
        
        double avgX = centers.stream()
                            .mapToDouble(Point::getX)
                            .average().orElse(0);

        double avgY = centers.stream()
                            .mapToDouble(Point::getY)
                            .average().orElse(0);

        return geoFactory.createPoint(new Coordinate(avgX, avgY));
    }
}