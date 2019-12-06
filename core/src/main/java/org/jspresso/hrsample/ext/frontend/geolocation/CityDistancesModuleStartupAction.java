package org.jspresso.hrsample.ext.frontend.geolocation;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.jspresso.contrib.geolocation.model.GeoDistanceParameters;
import org.jspresso.contrib.model.ExtraManagerModule;
import org.jspresso.contrib.model.HibernateUtil;
import org.jspresso.contrib.model.extradata.ExtraManager;
import org.jspresso.framework.action.IActionHandler;
import org.jspresso.framework.application.backend.persistence.hibernate.HibernateBackendController;
import org.jspresso.framework.application.backend.session.EMergeMode;
import org.jspresso.framework.application.frontend.action.FrontendAction;
import org.jspresso.framework.application.model.BeanModule;
import org.jspresso.hrsample.ext.model.distances.CityDistance;
import org.jspresso.hrsample.model.City;

public class CityDistancesModuleStartupAction<E, F, G> extends FrontendAction<E, F, G> {

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean execute(IActionHandler actionHandler, Map<String, Object> context) {

        HibernateBackendController controller = (HibernateBackendController) getBackendController(context);
        DetachedCriteria entityCriteria = DetachedCriteria.forClass(City.class);
        List<City> cities = controller.findByCriteria(entityCriteria, EMergeMode.MERGE_KEEP, City.class);

        CityDistance cd = controller.getEntityFactory().createComponentInstance(CityDistance.class);
        cd.setCities(new LinkedHashSet<>(cities));
        cd.setTravelMode(GeoDistanceParameters.TRAVEL_MODE_DRIVING);

        BeanModule module = (BeanModule) getModule(context);
        module.setModuleObject(cd);

        return super.execute(actionHandler, context);
    }
}
