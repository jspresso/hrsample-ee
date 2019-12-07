package org.jspresso.hrsample.ext.model.distances.extension;

import org.jspresso.contrib.geolocation.model.GeoDistanceParameters;
import org.jspresso.framework.model.component.AbstractComponentExtension;
import org.jspresso.framework.model.component.service.DependsOn;
import org.jspresso.framework.model.component.service.DependsOnGroup;
import org.jspresso.hrsample.ext.model.distances.CityDistance;
import org.jspresso.hrsample.ext.model.distances.ICityDistanceExtension;
import org.jspresso.hrsample.model.City;

import java.util.Set;

public class CityDistanceExtension extends AbstractComponentExtension<CityDistance> implements ICityDistanceExtension {

    /**
     * Constructs a new {@code CityDistanceExtension} instance.
     *
     * @param extendedCityDistance The extended CityDistance instance.
     */
    public CityDistanceExtension(CityDistance extendedCityDistance) {
        super(extendedCityDistance);
    }

    /**
     * Gets the itineraryCalculationAllowed.
     *
     * @return the itineraryCalculationAllowed.
     */
    @DependsOn({CityDistance.SELECTED_CITIES,
            GeoDistanceParameters.TRAVEL_MODE})
    @Override
    public boolean isItineraryCalculationAllowed() {

        CityDistance cityDistance = getComponent();
        Set<City> selectedCities = cityDistance.getSelectedCities();
        if (selectedCities == null || selectedCities.size()<2)
            return false;

        if (cityDistance.getTravelMode()==null)
            return false;

        return true;
    }


}
