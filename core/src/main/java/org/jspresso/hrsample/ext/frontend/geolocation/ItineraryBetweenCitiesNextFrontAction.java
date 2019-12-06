package org.jspresso.hrsample.ext.frontend.geolocation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.jspresso.contrib.geolocation.model.ItineraryOutput;
import org.jspresso.contrib.geolocation.util.Itinerary;
import org.jspresso.framework.action.IActionHandler;
import org.jspresso.framework.application.frontend.action.FrontendAction;
import org.jspresso.framework.util.gui.Dimension;
import org.jspresso.framework.util.gui.map.MapHelper;
import org.jspresso.framework.util.gui.map.Point;
import org.jspresso.framework.util.gui.map.Route;
import org.jspresso.hrsample.ext.model.distances.CityDistance;
import org.jspresso.hrsample.model.City;

import static org.jspresso.framework.util.gui.map.MapHelper.GREEN_MARK;
import static org.jspresso.framework.util.gui.map.MapHelper.RED_MARK;

public class ItineraryBetweenCitiesNextFrontAction<E, F, G> extends FrontendAction<E, F, G> {

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

            if (points.isEmpty())
                p.setImagePath(GREEN_MARK);
            else if (points.size() == cd.getSelectedCities().size()-1)
                p.setImagePath(RED_MARK);
            else
                p.setImagePath("/org/jspresso/hrsample/images/city.png");

            p.setImageDimension(new Dimension(24, 24));

            points.add(p);
        }

        ItineraryOutput output = getActionParameter(context);

        List<Itinerary> itineraries = output.getItineraries();
        for (Itinerary it : itineraries) {

            Point mid = it.getPoints()[it.getPoints().length / 2];
            mid.setHtmlDescription(it.getDistanceToSring() + " / " + it.getDurationToString());

            points.add(mid);
        }

        Itinerary overview = output.getOverview();
        overview.setColor("#225586");
        overview.setWidth(2);
        overview.addOption("lineDash", new int[]{5, 5});

        String map = MapHelper.buildMap(points.toArray(new Point[0]), new Route[]{overview});
        cd.setMapContent(map);

        return super.execute(actionHandler, context);
    }
}
