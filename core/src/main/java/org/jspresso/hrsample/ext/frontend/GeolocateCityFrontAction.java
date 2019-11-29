package org.jspresso.hrsample.ext.frontend;

import org.jspresso.contrib.frontend.geolocation.GeolocateFrontAction;
import org.jspresso.contrib.model.geolocation.GeolocationInput;
import org.jspresso.contrib.model.geolocation.IGeolocationInput;
import org.jspresso.hrsample.model.City;

import java.util.Map;

public class GeolocateCityFrontAction<E, F, G> extends GeolocateFrontAction<E, F, G> {


    @Override
    protected IGeolocationInput getGeolocationInput(Map<String, Object> context) {

        City city = getModel(context);

        GeolocationInput input = new GeolocationInput();
        input.setCountryCode(city.getName());
        input.setZip(city.getZip());

        return input;
    }
}
