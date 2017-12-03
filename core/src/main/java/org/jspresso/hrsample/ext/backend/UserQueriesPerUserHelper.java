/*
 * Copyright (c) 2005-2016 Vincent Vandenschrick. All rights reserved.
 *
 *  This file is part of the Jspresso framework.
 *
 *  Jspresso is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Jspresso is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with Jspresso.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.jspresso.hrsample.ext.backend;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.jspresso.contrib.backend.query.UserQueriesHelper;
import org.jspresso.contrib.model.query.UserQuery;
import org.jspresso.framework.action.ActionContextConstants;
import org.jspresso.framework.application.backend.persistence.hibernate.HibernateBackendController;
import org.jspresso.framework.security.UserPrincipal;

import javax.security.auth.Subject;
import java.security.Principal;
import java.util.List;
import java.util.Map;

import static org.jspresso.hrsample.ext.model.userquery.service.UserSharingListService.SEP;

/**
 * User query helper implementation.
 * 
 *
 * @author Maxime HAMM
 */
public class UserQueriesPerUserHelper extends UserQueriesHelper {

  @Override
  public String getMySharedGroup(Subject subject) {

    for (Principal principal : subject.getPrincipals()) {
      if (principal instanceof UserPrincipal) {
        return principal.getName();
      }
    }

    return null;
  }

  @Override
  public List<String> getAllGroups(Map<String, Object> context) {
    throw new RuntimeException("Method use not allowed");
  }

  @Override
  protected void completeSharedQueriesCriteria(DetachedCriteria criteria, Map<String, Object> context) {

    HibernateBackendController controller = (HibernateBackendController) context.get(ActionContextConstants.BACK_CONTROLLER);
    Subject subject = controller.getSubject();
    String mySharedGroup = getMySharedGroup(subject);

    criteria.add(Restrictions.like(UserQuery.SHARED_STRING, "%" + SEP + mySharedGroup + SEP + "%"));
  }

}
