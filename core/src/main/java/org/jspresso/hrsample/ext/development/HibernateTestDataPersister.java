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
package org.jspresso.hrsample.ext.development;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.jspresso.contrib.backend.query.IUserQueriesHelper;
import org.jspresso.contrib.model.query.UserDefaultQuery;
import org.jspresso.contrib.model.query.UserQuery;
import org.jspresso.contrib.usage.model.ModuleUsage;
import org.jspresso.framework.action.ActionContextConstants;
import org.jspresso.framework.application.model.FilterableBeanCollectionModule;
import org.jspresso.framework.application.model.Module;
import org.jspresso.framework.application.model.Workspace;
import org.jspresso.framework.ext.pivot.backend.PivotHelper;
import org.jspresso.framework.ext.pivot.model.PivotCriteria;
import org.jspresso.framework.ext.pivot.model.PivotField;
import org.jspresso.framework.ext.pivot.model.PivotFilterableBeanCollectionModule;
import org.jspresso.framework.ext.pivot.model.PivotMeasure;
import org.jspresso.framework.model.component.IQueryComponent;
import org.jspresso.framework.model.component.query.EnumQueryStructure;
import org.jspresso.framework.model.component.query.EnumValueQueryStructure;
import org.jspresso.framework.model.component.query.QueryComponent;
import org.jspresso.framework.model.descriptor.IEnumerationPropertyDescriptor;
import org.jspresso.framework.model.descriptor.IPropertyDescriptor;
import org.jspresso.hrsample.ext.model.Furniture;
import org.jspresso.hrsample.model.ContactInfo;
import org.jspresso.hrsample.model.Employee;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactory;

/**
 * Persists some test data for the application.
 */
public class HibernateTestDataPersister extends org.jspresso.hrsample.development.HibernateTestDataPersister {

  private static final Logger LOGGER = LoggerFactory.getLogger(HibernateTestDataPersister.class);

  private BeanFactory beanFactory = null;

  /**
   * Constructs a new <code>TestDataPersister</code> instance.
   *
   * @param beanFactory
   *          the spring bean factory to use.
   */
  public HibernateTestDataPersister(BeanFactory beanFactory) {
    super(beanFactory);
    this.beanFactory = beanFactory;
  }

  /**
   * Creates some test data using the passed in Spring application context.
   */
  @Override
  public void createAndPersistTestData() {
    super.createAndPersistTestData();

    try {
      createFurniture("Blue desk - 2012", 99.95, 0.1);
      createFurniture("Main blue desk - 2012", 69.95, 0.15);
      createFurniture("Various great table - 2012", 139.95, 0.1);
    } catch (Throwable ex) {
      // In no way the test data persister should make the application
      // startup fail.
    }

    try {
      // furniture workspace
      createModuleUsages("furniture.module", "Tom", 365, 45, 30);
      createModuleUsages("furniture.module", "Bob", 230, 20, 10);
      createModuleUsages("furniture.module", "Alice", 30, 0, 20);

      // organization workspace
      createModuleUsages("companies.module", "Tom", 365, 3, 10);
      createModuleUsages("companies.module", "Bob", 365, 0, 15);
      createModuleUsages("companies.module", "Alice", 2, 0, 20);

      // employees workspace
      createModuleUsages("employees.module", "Tom", 365, 0, 15);
      createModuleUsages("employees.module", "Bob", 2, 0, 20);
      createModuleUsages("employees.module", "Alice", 120, 60, 15);

      // masterdata workspace
      createModuleUsages("masterdata.cities.module", "Tom", 30, 3, 16);
      createModuleUsages("masterdata.cities.module", "Bob", 40, 8, 10);
      createModuleUsages("masterdata.cities.module", "Alice", 2, 0, 10);

    } catch (Throwable ex) {
      // In no way the test data persister should make the application
      // startup fail.
    }

    try {
      createFilter(false, "demo", "employees.workspace", "employees.module", "More than 40 yers old", Employee.AGE, "40");
      createFilter(false, "demo", "employees.workspace", "employees.module", "Men", Employee.GENDER, Employee.GENDER_M);
      createFilter(false, "demo", "employees.workspace", "employees.module", "Living at Every", Employee.CONTACT+'.'+ContactInfo.CITY, "Evry");
      createFilter(false, "demo", "employees.workspace", "employees.module", "Women", Employee.GENDER, Employee.GENDER_F);

      createFilter(false, "demo", "employees.workspace", "employees.module", "Start with D", Employee.NAME, "D%");
      createFilter(false, "demo", "employees.workspace", "employees.module", "Start with B", Employee.NAME, "B%");
      createFilter(false, "demo", "employees.workspace", "employees.module", "Start with M", Employee.NAME, "M%");
      createFilter(false, "demo", "employees.workspace", "employees.module", "Start with U", Employee.NAME, "U%");
      createFilter(false, "demo", "employees.workspace", "employees.module", "Start with V", Employee.NAME, "V%");
      createFilter(false, "demo", "employees.workspace", "employees.module", "Start with W", Employee.NAME, "W%");

      createFilter(false, "maxime", "employees.workspace", "employees.module", "Start with X", Employee.NAME, "D%");
      createFilter(false, "maxime", "employees.workspace", "employees.module", "Start with Y", Employee.NAME, "B%");
      createFilter(false, "maxime", "employees.workspace", "employees.module", "Start with Z", Employee.NAME, "E%");

    } catch (Throwable ex) {
      LOGGER.warn("Unable to create filter criterias !", ex);
      // In no way the test data persister should make the application
      // startup fail.
    }

    try {
      createPivotFilter(false, "demo", "statistics.workspace", "employee.statistics.module", "Salary per age group",
          new String[]{"gender"},
          new String[]{"age(30, 40)"},
          new String[]{"salary@sum"});

      createPivotFilter(false, "demo", "statistics.workspace", "employee.statistics.module", "Salary & Employees per age group",
          new String[]{"gender"},
          new String[]{"age(30, 40)"},
          new String[]{"salary@sum", "ssn@count"});

      createPivotFilter(true, "demo", "statistics.workspace", "employee.statistics.module", "Salary per department",
          new String[]{"managedOu.ouId"},
          new String[]{"salary(40, 80, 100)"},
          new String[]{"salary@sum", "ssn@count"});

    } catch (Throwable ex) {
      LOGGER.warn("Unable to create filter criterias !", ex);
      // In no way the test data persister should make the application
      // startup fail.
    }
  }

  private void createPivotFilter(
      boolean defaultFilter,
      String login,
      String workspaceId,
      String moduleId,
      String queryName,
      String[] pivotLines,
      String[] pivotColumns,
      String[] pivotMeasures,
      String... criterias) throws IOException {

    PivotFilterableBeanCollectionModule module = null;
    Workspace workspace = (Workspace) beanFactory.getBean(workspaceId);
    List<Module> modules = workspace.getModules(true);
    if (modules!=null) {
      for (Module m : modules) {
        if (moduleId.equals(m.getName())) {
          module = (PivotFilterableBeanCollectionModule) m;
          break;
        }
      }
    }

    PivotCriteria pivot = module.getPivot();

    pivot.setLinesRef(createPivotFields(pivotLines, module));
    pivot.setColumnsRef(createPivotFields(pivotColumns, module));
    pivot.setMeasuresRef(createPivotFields(pivotMeasures, module));

    createFilter(
        defaultFilter, login, queryName, module, criterias);
  }

  private List<PivotField> createPivotFields(String[] pivotFields, PivotFilterableBeanCollectionModule module) {
    List<PivotField> fields = new ArrayList<>();
    for (String s : pivotFields) {

      String fieldName;
      if (s.contains("@")) {
        fieldName = PivotHelper.getFieldFromRestrictions(s);
      }
      else {
        fieldName = s;
      }


      PivotField pf = getEntityFactory().createComponentInstance(PivotField.class);
      pf.setComponentClass(module.getElementComponentDescriptor().getComponentContract());
      pf.setCode(fieldName);
      pf.setSelected(true);

      if (s.contains("@")) {
        PivotMeasure pm = getEntityFactory().createComponentInstance(PivotMeasure.class);
        pm.setupMeasure(s);
        pm.setSelected(true);

        pf.addToMeasures(pm) ;
      }

      fields.add(pf);
    }
    return fields;
  }

  //private int filterSeq = 0;

  private void createFilter(
      boolean defaultFilter,
      String login,
      String workspaceId,
      String moduleId,
      String queryName,
      String... criterias) throws IOException {

    FilterableBeanCollectionModule module = null;
    Workspace workspace = (Workspace) beanFactory.getBean(workspaceId);
    List<Module> modules = workspace.getModules(true);
    if (modules!=null) {
      for (Module m : modules) {
        if (moduleId.equals(m.getName())) {
          module = (FilterableBeanCollectionModule) m;
          break;
        }
      }
    }

    createFilter(defaultFilter, login, queryName, module, criterias);
  }

  private void createFilter(
      boolean defaultFilter,
      String login,
      String queryName,
      FilterableBeanCollectionModule module,
      String... criterias) throws IOException {

    module.setFilter(new QueryComponent(module.getElementComponentDescriptor(), getEntityFactory()));

    IQueryComponent filter = module.getFilter();
    for (int i = 0; i < criterias.length; i+=2) {
      String key = criterias[i];
      Object value = criterias[i+1];

      IPropertyDescriptor propertyDescriptor = module.getElementComponentDescriptor().getPropertyDescriptor(key);
      if (propertyDescriptor instanceof IEnumerationPropertyDescriptor) {
        IEnumerationPropertyDescriptor epd = (IEnumerationPropertyDescriptor) propertyDescriptor;
        EnumQueryStructure eqs = new EnumQueryStructure(epd);

        Set<EnumValueQueryStructure> selected = new HashSet<>();
        for (EnumValueQueryStructure evqs : eqs.getEnumerationValues()) {
          if (value.equals(evqs.getValue())) {
            selected.add(evqs);
            break;
          }
        }
        eqs.setSelectedEnumerationValues(selected);

        value = eqs;
      }

      filter.put(key, value);
    }

    String criteria = module.serializeCriteria();

    Map<String, Object> context = new HashMap<>();
    context.put(ActionContextConstants.MODULE, module);

    IUserQueriesHelper helper = (IUserQueriesHelper) beanFactory.getBean("userQueriesHelper");
    String key = helper.getKey(context);

    UserQuery q = createEntityInstance(UserQuery.class);

    q.setLogin(login);
    q.setKey(key);
    q.setName(queryName);
    q.setCriterias(criteria);
//    q.setSeq(filterSeq++);
    q.setSharedString("[administrator]");

    saveOrUpdate(q);

    if (defaultFilter) {
      UserDefaultQuery qd = createEntityInstance(UserDefaultQuery.class);

      qd.setLogin(login);
      qd.setKey(key);
//      qd.setQuery(q);

      saveOrUpdate(qd);
    }
  }

  private Furniture createFurniture(String name, double price, double discount) {
    Furniture furniture = createEntityInstance(Furniture.class);
    furniture.setName(name);
    furniture.setCreateTimestamp(new Date());
    furniture.setPrice(price);
    furniture.setDiscount(discount);
    saveOrUpdate(furniture);
    return furniture;
  }

  /**
   * create a set of Module usage for module <i>moduleId</i> from <i>formDaysAgo</i>
   * to <i>toDaysAgo</i>, using a random method to set a total of <i>accessCount</i> access.
   */
  private void createModuleUsages(String moduleId, String accessBy,
      int fromDaysAgo, int toDaysAgo, int accessCount) {

    for (int i = 0; i < accessCount; i++) {
      Calendar cal = Calendar.getInstance();
      cal.add(Calendar.DAY_OF_YEAR,
          -new Random().nextInt(fromDaysAgo - toDaysAgo) - toDaysAgo);

      ModuleUsage mu = createEntityInstance(ModuleUsage.class);
      mu.setModuleId(moduleId);
      mu.setAccessBy(accessBy);
      mu.setAccessDate(cal.getTime());
      saveOrUpdate(mu);

    }

  }

}
