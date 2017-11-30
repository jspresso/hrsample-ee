package org.jspresso.hrsample.ext.model.userquery.processor;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.jspresso.contrib.model.query.UserQuery;
import org.jspresso.framework.application.backend.BackendControllerHolder;
import org.jspresso.framework.application.backend.persistence.hibernate.HibernateBackendController;
import org.jspresso.framework.application.backend.session.EMergeMode;
import org.jspresso.framework.util.bean.integrity.EmptyPropertyProcessor;
import org.jspresso.hrsample.ext.model.userquery.UserSharingList;
import org.jspresso.hrsample.model.User;

import java.util.List;

import static org.jspresso.hrsample.ext.model.userquery.service.UserSharingListService.SEP;

/**
 * UserSharingListProcessor
 * User: Maxime HAMM
 * Date: 30/11/2017
 */
public class UserSharingListProcessor {

    /**
     * Query field's property processor.
     */
    public static class QueryProcessor extends EmptyPropertyProcessor<UserSharingList, UserQuery> {

        @Override
        public void postprocessSetter(UserSharingList sharingList, UserQuery oldUserQuery, UserQuery newUserQuery) {

            if (newUserQuery == null)
                return;

            String sharedString = newUserQuery.getSharedString();
            if (sharedString == null)
                return;

            String[] logins = sharedString.split(SEP);
            HibernateBackendController controller = (HibernateBackendController) BackendControllerHolder.getCurrentBackendController();

            DetachedCriteria criteria = DetachedCriteria.forClass(User.class);
            criteria.add(Restrictions.in(User.LOGIN, logins));
            List<User> users = controller.findByCriteria(criteria, EMergeMode.MERGE_LAZY, User.class);

            sharingList.setUsers(users);
        }
    }

}
