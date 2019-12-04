package org.jspresso.hrsample.ext.frontend;

import org.apache.commons.lang3.tuple.Pair;
import org.jspresso.contrib.frontend.geolocation.GeolocateFrontAction;
import org.jspresso.contrib.model.geolocation.Geolocation;
import org.jspresso.contrib.model.geolocation.GeolocationInput;
import org.jspresso.contrib.model.geolocation.IGeolocationInput;
import org.jspresso.hrsample.model.City;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.jspresso.hrsample.ext.frontend.GeolocateCityFinishedFrontAction.CITY_REF;

public class GeolocateCityFrontAction<E, F, G> extends GeolocateFrontAction<E, F, G> {
    
    @Override
    protected IGeolocationInput getGeolocationInput(Map<String, Object> context) {

        City city = getModel(context);
        context.put(CITY_REF, city);

        GeolocationInput input = new GeolocationInput();
        input.setCity(city.getName());
        input.setZip(city.getZip());

        return input;
    }

    @Override
    protected Pair<Double, Double> getReverseGeolocationInput(Map<String, Object> context) {

        City city = getModel(context);
        context.put(CITY_REF, city);

        return Pair.of(city.getLatitude(), city.getLongitude());
    }

    @Override
    protected List<Geolocation> filterGeolocation(List<Geolocation> geolocalized, Map<String, Object> context) {

        if (isReverse()) {
            
            LinkedHashMap<String, Geolocation> map = new LinkedHashMap<>();
            for (Geolocation loc : geolocalized) {

                if (loc.getCity() == null || loc.getZip() == null)
                    continue;

                map.put(loc.getCity() + "/" + loc.getZip(), loc);
            }
            return new ArrayList<>(map.values());
        }

        return super.filterGeolocation(geolocalized, context);
    }
}
