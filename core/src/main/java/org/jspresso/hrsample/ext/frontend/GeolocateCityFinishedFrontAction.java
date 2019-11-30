package org.jspresso.hrsample.ext.frontend;

import java.util.Map;

import org.jspresso.contrib.model.geolocation.Geolocation;
import org.jspresso.framework.action.IActionHandler;
import org.jspresso.framework.application.frontend.action.FrontendAction;
import org.jspresso.hrsample.model.City;

public class GeolocateCityFinishedFrontAction<E, F, G> extends FrontendAction<E, F, G> {

    public static final String CITY_REF = "CITY";
    
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean execute(IActionHandler actionHandler, Map<String, Object> context) {

        Geolocation geolocation = getActionParameter(context);
        
        if (geolocation!=null) {

             City city = (City) context.get(CITY_REF);
             city.setLatitude(geolocation.getLatitude());
             city.setLongitude(geolocation.getLongitude());

             if (geolocation.getZip()!=null)
                 city.setZip(geolocation.getZip());
         }
        
        return super.execute(actionHandler, context);
    }
}
