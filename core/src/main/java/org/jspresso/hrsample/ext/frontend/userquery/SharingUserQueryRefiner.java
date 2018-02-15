package org.jspresso.hrsample.ext.frontend.userquery;

import org.hibernate.criterion.Restrictions;
import org.jspresso.framework.action.ActionContextConstants;
import org.jspresso.framework.application.backend.persistence.hibernate.HibernateBackendController;
import org.jspresso.framework.model.component.IQueryComponent;
import org.jspresso.framework.model.persistence.hibernate.criterion.EnhancedDetachedCriteria;
import org.jspresso.framework.security.UserPrincipal;
import org.jspresso.hrsample.backend.GlobalCriteriaRefiner;
import org.jspresso.hrsample.ext.model.userquery.UserSharingList;
import org.jspresso.hrsample.model.User;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * SharingUserQueryRefiner
 * User: Maxime HAMM
 * Date: 03/12/2017
 */
public class SharingUserQueryRefiner extends GlobalCriteriaRefiner {

    @Override
    public void refineCriteria(EnhancedDetachedCriteria criteria, IQueryComponent queryComponent, Map<String, Object> context) {

        super.refineCriteria(criteria, queryComponent, context);

        HibernateBackendController controller = (HibernateBackendController) context.get(ActionContextConstants.BACK_CONTROLLER);
        UserPrincipal principal = controller.getApplicationSession().getPrincipal();

        Set<String> logins = new HashSet<>();
        logins.add(principal.getName());
        
        UserSharingList userSharingList = (UserSharingList) context.get(ActionContextConstants.MASTER_COMPONENT);
        List<User> users = userSharingList.getUsers();
        if (users !=null) {
            for (User u : users) {
                if (u.getLogin() != null)
                    logins.add(u.getLogin());
            }
        }
        
        criteria.add(
                Restrictions.not(
                        Restrictions.in(User.LOGIN, logins.toArray(new String[0]))));
    }

}
