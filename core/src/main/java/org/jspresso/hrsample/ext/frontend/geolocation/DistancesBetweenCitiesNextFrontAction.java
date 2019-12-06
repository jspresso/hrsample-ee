package org.jspresso.hrsample.ext.frontend.geolocation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.jspresso.contrib.geolocation.model.GeoDistancesOutput;
import org.jspresso.framework.action.IActionHandler;
import org.jspresso.framework.application.frontend.action.FrontendAction;
import org.jspresso.framework.util.gui.Dimension;
import org.jspresso.framework.util.gui.map.MapHelper;
import org.jspresso.framework.util.gui.map.Point;
import org.jspresso.framework.util.gui.map.Route;
import org.jspresso.framework.util.gui.map.Shape;
import org.jspresso.hrsample.ext.model.distances.CityDistance;
import org.jspresso.hrsample.model.City;

public class DistancesBetweenCitiesNextFrontAction<E, F, G> extends FrontendAction<E, F, G> {

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean execute(IActionHandler actionHandler, Map<String, Object> context) {

        CityDistance cd = getModel(context);

        List<Point> points = new ArrayList<>();
        for (City city : cd.getSelectedCities()) {

            Point p = new Point(city.getLongitude(), city.getLatitude());
            p.setHtmlDescription(city.getName());
            p.setImagePath("/org/jspresso/hrsample/images/city.png");
            p.setImageDimension(new Dimension(24, 24));

            points.add(p);
        }

        GeoDistancesOutput output = getActionParameter(context);
        List<Route> routes = output.getRoutes();

        String map = MapHelper.buildMap(points.toArray(new Point[0]), routes.toArray(new Route[0]));
        cd.setMapContent(map);

        return super.execute(actionHandler, context);
    }
}
