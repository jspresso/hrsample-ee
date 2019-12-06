package org.jspresso.hrsample.ext.frontend.geolocation;

import com.google.maps.model.GeocodingResult;
import org.jspresso.contrib.geolocation.backend.googlemaps.GeolocationGoogleMapsEngine;
import org.jspresso.contrib.geolocation.model.Geolocation;
import org.jspresso.framework.model.entity.IEntityFactory;

import static org.jboss.security.identity.Attribute.TYPE.POSTAL_CODE;

public class GeolocationGoogleEngine extends GeolocationGoogleMapsEngine {

    @Override
    protected Geolocation loadAddress(GeocodingResult result, boolean reverse, IEntityFactory entityFactory) {

        if (reverse && POSTAL_CODE.equals(result.types[0]))
            return null;

        return super.loadAddress(result, reverse, entityFactory);
    }
}
