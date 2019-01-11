package org.jspresso.hrsample.ext.backend;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.jspresso.contrib.backend.pivot.subscription.ModificationTrackerSubcriptionHelperBase;
import org.jspresso.contrib.model.subscription.ITrackerSubscriber;
import org.jspresso.framework.application.backend.BackendControllerHolder;
import org.jspresso.framework.application.backend.persistence.hibernate.HibernateBackendController;
import org.jspresso.framework.application.backend.session.EMergeMode;
import org.jspresso.framework.security.UserPrincipal;
import org.jspresso.hrsample.ext.model.UserSubscriber;

public class ModificationTrackerSubcriptionHelper extends ModificationTrackerSubcriptionHelperBase {

    @Override
    public ITrackerSubscriber getTrackerSubscriber() {

        HibernateBackendController controller = (HibernateBackendController) BackendControllerHolder.getCurrentBackendController();
        UserPrincipal principal = controller.getApplicationSession().getPrincipal();
        String login = principal.getName();

        DetachedCriteria dc = DetachedCriteria.forClass(UserSubscriber.class);
        dc.add(Restrictions.eq(UserSubscriber.LOGIN, login));
        UserSubscriber subscriber = controller.findFirstByCriteria(dc, EMergeMode.MERGE_KEEP, UserSubscriber.class);

        if (subscriber == null) {
            subscriber = controller.getEntityFactory().createEntityInstance(UserSubscriber.class);
            subscriber.setLogin(login);
        }

        return subscriber;
    }
}
