package org.jspresso.hrsample.ext.frontend;

import org.jspresso.contrib.frontend.geolocation.GeolocateFrontAction;
import org.jspresso.contrib.model.geolocation.GeolocationInput;
import org.jspresso.contrib.model.geolocation.IGeolocationInput;
import org.jspresso.hrsample.model.City;

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
}
