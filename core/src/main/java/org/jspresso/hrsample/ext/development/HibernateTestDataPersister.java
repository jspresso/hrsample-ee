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

import org.jspresso.contrib.backend.pivot.ExtendedPivotRefiner;
import org.jspresso.contrib.frontend.autodoc.LoadAutoDocIndexFrontAction;
import org.jspresso.contrib.model.TestDataHelper;
import org.jspresso.contrib.model.autodoc.*;
import org.jspresso.contrib.model.pivot.PivotSetup;
import org.jspresso.contrib.model.pivot.PivotSetupField;
import org.jspresso.contrib.model.pivot.PivotStyleSet;
import org.jspresso.contrib.usage.model.ModuleUsage;
import org.jspresso.framework.application.backend.session.IApplicationSession;
import org.jspresso.framework.application.frontend.IFrontendController;
import org.jspresso.framework.ext.pivot.model.PivotFilterableBeanCollectionModule;
import org.jspresso.framework.model.entity.IEntity;
import org.jspresso.framework.model.entity.IEntityFactory;
import org.jspresso.hrsample.ext.model.Furniture;
import org.jspresso.hrsample.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactory;

import java.io.IOException;
import java.util.*;

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
      LOGGER.error("Data loading error", ex);
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
      LOGGER.error("Data loading error", ex);
    }

    // 
    // Pivot setup
    createPivotSetup();
    
    //
    // Filter module saved criteria
    try {
      createFilter(false, "demo", "employees.workspace", "employees.module", "More than 40 years old", Employee.BIRTH_DATE, "> year-40");
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

    //
    // Pivots module saved criteria
    try {

      createPivotFilter(false, "demo", "statistics.workspace", "employee.statistics.module", "Salary & Bonus per age",
              new String[]{"age(30, 40)"},
              new String[]{},
              new String[]{"salary@sum", "bonus.encryptedValue@sum", "company.budget.encryptedValue@average"});

      createPivotFilter(false, "demo", "statistics.workspace", "employee.statistics.module", "Salary per age group",
          new String[]{"gender"},
          new String[]{"age(30, 40)"},
          new String[]{"salary@sum"});

      createPivotFilter(false, "demo", "statistics.workspace", "employee.statistics.module", "Salary & Employees per age group",
          new String[]{"gender"},
          new String[]{"age(30, 40)"},
          new String[]{"salary@sum", "ssn@count"});

      createPivotFilter(true, "demo", "statistics.workspace", "employee.statistics.module", "Salary per gender & department",
          new String[]{"gender", "managedOu.ouId"},
          new String[]{"salary(40, 80, 100)"},
          new String[]{"salary@sum", "ssn@count"});
      
      createPivotFilter(true, "demo", "statistics.workspace", "employee.statistics.module", "Number of people per age & gender",
          new String[]{"contact.city.name", "managedOu.ouId"},
          new String[]{"salary(40, 80, 100)", "gender"},
          new String[]{"ssn@count"});

    } catch (Throwable ex) {
      LOGGER.warn("Unable to create filter criterias !", ex);
      // In no way the test data persister should make the application
      // startup fail.
    }

    //
    // Create application doc
    createAutoDocGraph("Company's organization", null,
            Company.class.getSimpleName(),
            OrganizationalUnit.class.getSimpleName(),
            Department.class.getSimpleName(),
            Employee.class.getSimpleName(),
            Team.class.getSimpleName());

    createAutoDocGraph("Admin", null,
            User.class.getSimpleName(),
            Role.class.getSimpleName(),
            Employee.class.getSimpleName());

    createAutoDocGraph("All entities", AutoDocFilter.TYPE_ENTITY);
  }
  
  @SuppressWarnings("unused")
  private void createPivotSetup() {
    
    // load style sets
    PivotStyleSet styleMain = createPivotStyleSet("main", "decimal-separator='.',\nthousand-separator='\\,',\ntext-align='center'", null);
    PivotStyleSet styleMain2 = createPivotStyleSet("main2", "color='#ABABAB'", null);
    PivotStyleSet styleDecimal = createPivotStyleSet("decimal", "decimal=2,\ntext-align='right'", styleMain);
    PivotStyleSet styleCurrency = createPivotStyleSet("currency", "unit='$'", styleDecimal);
    PivotStyleSet styleSalary = createPivotStyleSet("salary", "decimal=0,\nunit='K$'", styleCurrency);
     
    PivotStyleSet grey = createPivotStyleSet("grey", "background-color='#E6E6E6'", null);
    PivotStyleSet red = createPivotStyleSet("red", "color='#FF0000'", null);
    PivotStyleSet orange = createPivotStyleSet("orange", "color='#FF8000'", null);
    PivotStyleSet green = createPivotStyleSet("green", "color='#008000'", null);
    
    PivotStyleSet styleSalaryWithColor = createPivotStyleSet("salary-colored", "default:'red',\ncomparator:'>',\n100:'orange',\n200:'green',\nempty:'grey'", styleSalary);
    PivotStyleSet styleEuro = createPivotStyleSet("euro", "unit='K€'", styleCurrency);
    
    // find module
    PivotFilterableBeanCollectionModule module = TestDataHelper.findModule("statistics.workspace", "employee.statistics.module", beanFactory);
    
    // pivot
    PivotSetup pivotSetup = createEntityInstance(PivotSetup.class);
    pivotSetup.init(module.getPivotRefiner());
    
    // override it
    pivotSetup.setPivotId(module.getPermId());
    pivotSetup.setPivotName("Statistics - Employees");
    pivotSetup.setAvailableDimensions(
        "salary(40, 80, 100)\n" + 
        "age(30, 40)\n" + 
        "gender\n" + 
        "managedOu.ouId\n" + 
        "company.departments\n" + 
        "contact.city.name\n" +
        "managedOu.contact.city.name\n" +
        "contact.city.neighbours");
    pivotSetup.setAvailableMeasures(
        "ssn@count\n" + 
        "salary@sum\n" +
        "bonus.encryptedValue@sum\n" +
        "company.budget.encryptedValue@average\n" +
        "salary@percentile90\n" +
        "salary@average/managedOu.ouId\n" + 
        "managedOu.teamCount@sum\n" + 
        "%salary@sum/managedOu.teamCount@sum\n" +
        "company.departments@sum");
    pivotSetup.addToAscendantStyles(styleMain);
   
    saveOrUpdate(pivotSetup);
        
    // dimension
    List<PivotSetupField> fields = new ArrayList<>();
    fields.add(createPivotSetupField(pivotSetup, "managedOu.teamCount", "Nb managed people", "Nb collaborateurs", null));
    fields.add(createPivotSetupField(pivotSetup, "contact.city.name", "City", "Ville", null));
    fields.add(createPivotSetupField(pivotSetup, "managedOu.contact.city.name", "Workplace", "Lieu de travail", null));
    fields.add(createPivotSetupField(pivotSetup, "contact.city.neighbours", "City size", "Importante de la ville", null));
    
    // measures
    fields.add(createPivotSetupField(pivotSetup, "ssn@count", "Nb persons", "Nb personnes", "unit='P'", styleMain));
    fields.add(createPivotSetupField(pivotSetup, "salary@sum", null, null, null, styleEuro, styleSalaryWithColor));
    fields.add(createPivotSetupField(pivotSetup, "salary@percentile90", null, null, "decimal=0", styleSalary));
    fields.add(createPivotSetupField(pivotSetup, "salary@average/managedOu.ouId", null, null, null, styleSalary));
    
    // update module
    ((ExtendedPivotRefiner<?>)module.getPivotRefiner()).resetCache();
    module.getPivot();
    module.reloadPivotCriteria();
  }

  private PivotSetupField createPivotSetupField(PivotSetup pivotSetup, String fieldId, String fieldLabel, String frenchLabel, String customStyle, PivotStyleSet... parentStyles) {

    PivotSetupField f = TestDataHelper.createPivotSetupField(pivotSetup, fieldId, fieldLabel, frenchLabel, customStyle, getEntityFactory(), parentStyles);
    saveOrUpdate(f);
    return f; 
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
      Object... criterias) throws IOException {

    List<IEntity> entities =
            TestDataHelper.createPivotFilter(
                    defaultFilter,
                    "[administrator]",
                    login,
                    workspaceId,
                    moduleId,
                    queryName,
                    pivotLines,
                    pivotColumns,
                    pivotMeasures,
                    beanFactory,
                    getBackendController(),
                    criterias);

    for (IEntity entity : entities)
      saveOrUpdate(entity);
  }

  private void createFilter(

          boolean defaultFilter,
          String login,
          String workspaceId,
          String moduleId,
          String queryName,
          Object... criterias) throws IOException {

    List<IEntity> entities = TestDataHelper.createQueryFilter(defaultFilter, "[administrator]", login, workspaceId, moduleId, queryName, beanFactory, getBackendController(), criterias);
    for (IEntity entity : entities)
      saveOrUpdate(entity);

  }

  /**
   *
   * @param name
   * @param style
   * @param parent
   * @return
   */
  private PivotStyleSet createPivotStyleSet(String name, String style, PivotStyleSet parent) {

    PivotStyleSet s = TestDataHelper.createPivotStyleSet(name, style, parent, getEntityFactory());
    saveOrUpdate(s);
    return s;
  }

  /**
   * create furniture
   * @param name
   * @param price
   * @param discount
   * @return
   */
  private Furniture createFurniture(String name, double price, double discount) {

    Furniture furniture = createEntityInstance(Furniture.class);
    furniture.setName(name);
    furniture.setCreateTimestamp(new Date());
    furniture.setPrice(price);
    furniture.setDiscount(discount);
    
    furniture.setCreateTimestamp(new Date());
    furniture.setLastUpdateTimestamp(new Date());
    
    saveOrUpdate(furniture);
    return furniture;
  }

  private AutoDocGraph createAutoDocGraph(String label, String filterType, String... filterNames) {

    AutoDocGraph graph = createEntityInstance(AutoDocGraph.class);
    graph.setLabel(label);
    graph.setMergeReverse(true);
    graph.setDisplayInheritence(true);

    AutoDocIndex instance = AutoDocIndex.getInstance();
    if (instance == null) {

      IFrontendController frontendController = (IFrontendController) beanFactory.getBean("applicationFrontController");
      instance = LoadAutoDocIndexFrontAction.loadMeta(frontendController);

      IApplicationSession session = frontendController.getApplicationSession();
      session.putCustomValue(AutoDocIndex.AUTODOC_INDEX, instance);
    }

    List<MetaComponent> components = new ArrayList<>();
    AutoDocFilter filter = createComponentInstance(AutoDocFilter.class);
    filter.setType(filterType);

    for (String filterName : filterNames) {

      filter.setName(filterName);
      components.addAll(instance.findComponents(filter));
    }

    graph.setComponents(components);

    saveOrUpdate(graph);
    return graph;
  }


  /**
   * create a set of Module usage for module <i>moduleId</i> from <i>formDaysAgo</i>
   * to <i>toDaysAgo</i>, using a random method to set a total of <i>accessCount</i> access.
   */
  private void createModuleUsages(String moduleId, String accessBy,
      int fromDaysAgo, int toDaysAgo, int accessCount) {

    List<IEntity> entities =
            createModuleUsages(moduleId, accessBy, fromDaysAgo, toDaysAgo, accessCount, getEntityFactory());

    for (IEntity entity : entities)
      saveOrUpdate(entity);
  }
  private static List<IEntity> createModuleUsages(String moduleId, String accessBy, int fromDaysAgo, int toDaysAgo, int accessCount, IEntityFactory entityFactory) {

    List<IEntity> entities = new ArrayList<>();
    for (int i = 0; i < accessCount; i++) {
      Calendar cal = Calendar.getInstance();
      cal.add(Calendar.DAY_OF_YEAR,
          -new Random().nextInt(fromDaysAgo - toDaysAgo) - toDaysAgo);

      ModuleUsage mu = entityFactory.createEntityInstance(ModuleUsage.class);
      mu.setModuleId(moduleId);
      mu.setAccessBy(accessBy);
      mu.setAccessDate(cal.getTime());

      entities.add(mu);

    }

    return entities;
  }

}
