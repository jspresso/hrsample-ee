package org.jspresso.hrsample.ext.frontend.userquery;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.jspresso.framework.application.backend.action.CreateQueryComponentAction;
import org.jspresso.framework.application.backend.persistence.hibernate.HibernateBackendController;
import org.jspresso.framework.application.backend.session.EMergeMode;
import org.jspresso.framework.model.component.IQueryComponent;
import org.jspresso.framework.model.descriptor.IReferencePropertyDescriptor;
import org.jspresso.framework.security.UserPrincipal;
import org.jspresso.hrsample.model.Company;
import org.jspresso.hrsample.model.Employee;
import org.jspresso.hrsample.model.User;

import java.util.Map;

/**
 * SelectUsersForQuerySharingCreateQueryAction
 * User: Maxime HAMM
 * Date: 30/11/2017
 */
public class SelectUsersForQuerySharingCreateQueryAction extends CreateQueryComponentAction {

    /**
     * Get user.
     *
     * @param controller The controller
     * @return the User.
     */
    private static User getUser(HibernateBackendController controller) {
        UserPrincipal principal = controller.getApplicationSession().getPrincipal();

        DetachedCriteria dc = DetachedCriteria.forClass(User.class);
        dc.add(Restrictions.eq(User.LOGIN, principal.getName()));
        return controller.findFirstByCriteria(dc, EMergeMode.MERGE_KEEP, User.class);

    }

    @Override
    protected void completeQueryComponent(IQueryComponent queryComponent, IReferencePropertyDescriptor<?> erqDescriptor, Map<String, Object> context) {

        HibernateBackendController controller = (HibernateBackendController) getBackendController(context);
        User user = getUser(controller);
        if (user == null)
            return;

        Employee employee = user.getEmployee();
        if (employee == null)
            return;

        Company company = employee.getCompany();
        if (company == null)
            return;

        queryComponent.put(Employee.COMPANY, company);
        super.completeQueryComponent(queryComponent, erqDescriptor, context);
    }

}
