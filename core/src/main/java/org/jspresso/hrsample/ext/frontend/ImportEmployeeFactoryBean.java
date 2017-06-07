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
package org.jspresso.hrsample.ext.frontend;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

import org.jspresso.framework.action.ActionBusinessException;
import org.jspresso.framework.model.entity.IEntity;

import org.jspresso.contrib.frontend.ImportEntitiesFactoryBean;
import org.jspresso.contrib.frontend.ImportReference;
import org.jspresso.hrsample.model.City;
import org.jspresso.hrsample.model.Company;
import org.jspresso.hrsample.model.ContactInfo;
import org.jspresso.hrsample.model.Employee;

/**
 * Import Employees
 * Automatically create Company and City if does not exists
 *
 * @author Maxime HAMM
 */
public class ImportEmployeeFactoryBean extends ImportEntitiesFactoryBean {

  /**
   * ImportEmployeeFactoryBean
   */
  public ImportEmployeeFactoryBean() {
  }

  /**
   * ZIP
   */
  public static final String ZIP = "zip";

  /**
   * CONTACT_CITY
   */
  public static final String CONTACT_CITY = Employee.CONTACT + "." + ContactInfo.CITY;

  /**
   * create Company and City if does not exists
   */
  @Override
  public IEntity findReference(ImportReference refData, Map<String, String> cells, Map<String, Object> context)
      throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {

    // load from database
    IEntity entity = super.findReference(refData, cells, context);

    String property = refData.getName();
    if (Employee.COMPANY.equals(property)) {
      if (entity == null) {
        entity = getBackendController().getEntityFactory().createEntityInstance(Company.class);
        ((Company) entity).setName(cells.get(Employee.COMPANY));

        // manage cache !
        getCache(context).put(refData.getKeyCache(), entity);
      }
    } else if (CONTACT_CITY.equals(property)) {
      if (entity == null) {
        entity = getBackendController().getEntityFactory().createEntityInstance(City.class);
        ((City) entity).setName(cells.get(CONTACT_CITY));

        // manage cache !
        getCache(context).put(refData.getKeyCache(), entity);
      }

      // update zip
      ((City) entity).setZip(cells.get(ZIP));
      getBackendController().registerForUpdate(entity);
    }


    return entity;
  }

  /**
   * manage also an extrat column 'test'
   */
  @Override
  protected void checkLineFormat(List<String> columns, List<String> cells, String line, int idx) {
    if (columns.size() + 1 != cells.size()) {
      throw new ActionBusinessException("Wrong file format for import", "import.file.formatError", idx - 1,
          cells.size(), columns.size(), line);
    }
  }
}
