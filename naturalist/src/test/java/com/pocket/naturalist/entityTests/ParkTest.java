package com.pocket.naturalist.entityTests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;

import com.pocket.naturalist.entity.Animal;
import com.pocket.naturalist.entity.Park;
import com.pocket.naturalist.entity.Sighting;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;





class ParkTest {
    GeometryFactory geometryFactory;

    @BeforeEach
    public void setUp() {
        geometryFactory = new GeometryFactory();
    }
    @Test
    void constructor_setsNameAndSlug() {
        Park park = new Park("Yellowstone National Park");
        assertEquals("Yellowstone National Park", park.getName());
        assertEquals("yellowstone-national-park", park.getUrlSlug());
    }

    @Test
    void constructor_emptyName_setsDefaultSlug() {
        Park park = new Park("");
        assertEquals("General-Park", park.getUrlSlug());
    }

    @Test
    void setAndGetId() {
        Park park = new Park("Test Park");
        park.setId(42L);
        assertEquals(42L, park.getId());
    }

    @Test
    void setAndGetName() {
        Park park = new Park("Test Park");
        park.setName("New Name");
        assertEquals("New Name", park.getName());
    }

    @Test
    void setAndGetURLSlug() {
        Park park = new Park("Test Park");
        park.setUrlSlug("custom-slug");
        assertEquals("custom-slug", park.getUrlSlug());
    }

    @Test
    void setAndGetBoundary() {
        Park park = new Park("Test Park");
        Polygon polygon = geometryFactory.createPolygon(geometryFactory.createLinearRing(new Coordinate[] {
                new Coordinate(0, 0),
                new Coordinate(1, 0),
                new Coordinate(1, 1),
                new Coordinate(0, 1),
                new Coordinate(0, 0)
        }), null);  
        park.setBoundaryList(List.of(polygon));
        assertEquals(polygon, park.getBoundaryList().get(0));
    }

    @Test
    void setAndGetMapCenter() {
        Park park = new Park("Test Park");
        Point center = geometryFactory.createPoint(new Coordinate(5, 10));
        park.setMapCenter(center);
        assertEquals(center, park.getMapCenter());
    }

    @Test
    void setAndGetSightings() {
        Park park = new Park("Test Park");
        Sighting s1 = new Sighting();
        Sighting s2 = new Sighting();
        List<Sighting> sightings = Arrays.asList(s1, s2);
        park.setSightings(sightings);
        assertEquals(sightings, park.getSightings());
    }

    @Test
    void setAndGetAnimals() {
        Park park = new Park("Test Park");
        Animal a1 = new Animal();
        Animal a2 = new Animal();
        Set<Animal> animals = new HashSet<>(Arrays.asList(a1, a2));
        park.setAnimals(animals);
        assertEquals(animals, park.getAnimals());
    }

    @Test
    void slugRemovesSpecialCharactersAndSpaces() {
        Park park = new Park("Test! Park's @ National #1");
        assertEquals("test-parks-national-1", park.getUrlSlug());
    }
}