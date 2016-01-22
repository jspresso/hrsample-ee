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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.security.auth.Subject;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.jspresso.contrib.backend.query.AbstractUserQueriesMatchingGroupsHelper;
import org.jspresso.framework.application.backend.BackendControllerHolder;
import org.jspresso.framework.application.backend.persistence.hibernate.HibernateBackendController;
import org.jspresso.framework.application.backend.session.EMergeMode;
import org.jspresso.hrsample.model.Company;
import org.jspresso.hrsample.model.Department;
import org.jspresso.hrsample.model.Employee;
import org.jspresso.hrsample.model.Team;

/**
 * User query helper implementation.
 * Uses companies and organisation units.
 *
 * @author Maxime HAMM
 */
public class UserQueriesHelper extends AbstractUserQueriesMatchingGroupsHelper {

  /**
   * {@inheritDoc}
   *
   * Gets Employee's company, department and team names.
   */
  @Override
  public List<String> getMySharedGroups(Subject subject) {

    // get user instance
    HibernateBackendController controller = (HibernateBackendController) BackendControllerHolder.getCurrentBackendController();
    DetachedCriteria criteria = DetachedCriteria.forClass(Employee.class);
    criteria.add(Restrictions.eq(Employee.SSN, getLogin(subject)));
    Employee employee = controller.findFirstByCriteria(criteria, EMergeMode.MERGE_KEEP, Employee.class);
    if (employee == null || employee.getCompany() == null)
      return Collections.emptyList();

    // build shared groups, with company and organisation units
    List<String> groups = new ArrayList<>();
    String company = employee.getCompany().getNameRaw();
    groups.add(company);

    for (Team t : employee.getTeams()) {
      groups.add(company + '/' + t.getDepartment().getOuId());
    }

    return groups;
  }

  /**
   * {@inheritDoc}
   *
   * Get all company names and organisation unit names.
   */
  @Override
  public List<String> getAllGroups(Map<String, Object> context) {
    List<String> groups = new ArrayList<>();
    HibernateBackendController controller = (HibernateBackendController) BackendControllerHolder.getCurrentBackendController();

    DetachedCriteria criteria = DetachedCriteria.forClass(Company.class);
    criteria.addOrder(Order.asc(Company.NAME_RAW));
    List<Company> companies = controller.findByCriteria(criteria, EMergeMode.MERGE_KEEP, Company.class);
    for (Company c : companies) {
      String company = c.getNameRaw();
      groups.add(company);

      for (Department d : c.getDepartments()) {
        groups.add(company + '/' + d.getOuId());
      }
    }

    return groups;
  }
}
