package org.jspresso.hrsample.ext.frontend.geolocation;

import org.jspresso.contrib.geolocation.frontend.action.GeoDirectionsFrontAction;
import org.jspresso.contrib.geolocation.model.GeoDistancesInput;
import org.jspresso.contrib.geolocation.model.IGeoDistancesInput;
import org.jspresso.contrib.geolocation.util.GeolocationException;
import org.jspresso.framework.util.gui.Dimension;
import org.jspresso.framework.util.gui.map.ILatLng;
import org.jspresso.framework.util.gui.map.Point;
import org.jspresso.hrsample.ext.model.distances.CityDistance;
import org.jspresso.hrsample.model.City;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DistancesBetweenCitiesFrontAction<E, F, G> extends GeoDirectionsFrontAction<E, F, G> {

    @Override
    protected IGeoDistancesInput<? extends ILatLng> getGeoDistanceInput(Map<String, Object> context) {

        CityDistance cd = getModel(context);
        Set<City> selectedCities = cd.getSelectedCities();
        if (selectedCities == null || selectedCities.size() < 2)
            throw new GeolocationException("Geolocation error", "geolocation.error", "select two or more cities first");

        List<Point> points = new ArrayList<>();
        for (City city : selectedCities) {
            points.add(new Point(city.getLongitude(), city.getLatitude()));
        }

        GeoDistancesInput<Point> input = new GeoDistancesInput<>();
        input.setOptimize(true);
        input.setWaypoints(points);

        return input;
    }
}
