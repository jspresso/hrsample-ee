package org.jspresso.hrsample.ext.frontend.geolocation;

import org.jspresso.contrib.geolocation.frontend.action.ItineraryFrontAction;
import org.jspresso.contrib.geolocation.model.GeoDistanceParameters.ETravelMode;
import org.jspresso.contrib.geolocation.model.ItineraryInput;
import org.jspresso.contrib.geolocation.model.IItineraryInput;
import org.jspresso.contrib.geolocation.util.GeolocationException;
import org.jspresso.framework.util.gui.map.ILatLng;
import org.jspresso.framework.util.gui.map.Point;
import org.jspresso.hrsample.ext.model.distances.CityDistance;
import org.jspresso.hrsample.model.City;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ItineraryBetweenCitiesFrontAction<E, F, G> extends ItineraryFrontAction<E, F, G> {

    @Override
    protected IItineraryInput<Point> getItineraryInput(Map<String, Object> context) {

        CityDistance cd = getModel(context);
        String travelMode = cd.getTravelMode();
        if (travelMode == null)
            throw new GeolocationException("Geolocation error", "geolocation.error", "select a travel model");

        Set<City> selectedCities = cd.getSelectedCities();
        if (selectedCities == null || selectedCities.size() < 2)
            throw new GeolocationException("Geolocation error", "geolocation.error", "select two or more cities first");

        List<Point> points = new ArrayList<>();
        for (City city : selectedCities) {
            points.add(new Point(city.getLongitude(), city.getLatitude()));
        }

        ItineraryInput<Point> input = new ItineraryInput<>();
        input.setOptimize(true);
        input.setWaypoints(points);
        input.setTravelMode(ETravelMode.valueOf(travelMode));

        return input;
    }
}
