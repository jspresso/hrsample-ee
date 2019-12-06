package org.jspresso.hrsample.ext.frontend.geolocation;

import org.jspresso.contrib.geolocation.model.Geolocation;
import org.jspresso.framework.action.IActionHandler;
import org.jspresso.framework.application.frontend.action.FrontendAction;
import org.jspresso.hrsample.model.City;

import java.util.Map;

public class GeolocateReverseCityFinishedFrontAction<E, F, G> extends FrontendAction<E, F, G> {

    public static final String CITY_REF = "CITY";
    
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean execute(IActionHandler actionHandler, Map<String, Object> context) {

        Geolocation geolocation = getActionParameter(context);
        
        if (geolocation!=null) {

             City city = (City) context.get(CITY_REF);
             city.setName(geolocation.getCity());
             city.setZip(geolocation.getZip());

             if (geolocation.getZip()!=null)
                 city.setZip(geolocation.getZip());
         }
        
        return super.execute(actionHandler, context);
    }
}
