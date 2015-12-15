package org.jspresso.hrsample.ext.development;

import java.io.IOException;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.jspresso.contrib.backend.query.IUserQueriesHelper;
import org.jspresso.contrib.model.query.UserQuery;
import org.jspresso.contrib.usage.model.ModuleUsage;
import org.jspresso.framework.action.ActionContextConstants;
import org.jspresso.framework.application.model.FilterableBeanCollectionModule;
import org.jspresso.framework.application.model.Module;
import org.jspresso.framework.application.model.Workspace;
import org.jspresso.framework.model.component.IQueryComponent;
import org.jspresso.framework.model.component.query.EnumQueryStructure;
import org.jspresso.framework.model.component.query.EnumValueQueryStructure;
import org.jspresso.framework.model.component.query.QueryComponent;
import org.jspresso.framework.model.component.query.QueryComponentSerializationUtil;
import org.jspresso.framework.model.descriptor.IEnumerationPropertyDescriptor;
import org.jspresso.hrsample.ext.model.Furniture;
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
      createFilter("demo", "employees.workspace", "employees.module", "Start with D", Employee.NAME, "D%");
      createFilter("demo", "employees.workspace", "employees.module", "Start with B", Employee.NAME, "B%");
      
      createFilter("maxime", "employees.workspace", "employees.module", "Men", Employee.GENDER, Employee.GENDER_M);
      createFilter("maxime", "employees.workspace", "employees.module", "Women", Employee.GENDER, Employee.GENDER_F);

    } catch (Throwable ex) {
      LOGGER.warn("Unable to create filter criterias !", ex);
      // In no way the test data persister should make the application
      // startup fail.
    }
  }

  private int filterSeq = 0;
  @SuppressWarnings("null")
  private void createFilter(
      String login, 
      String workspaceId,
      String moduleId, 
      String queryName, 
      String... criterias) throws IOException {
    
    FilterableBeanCollectionModule module = null;
    Workspace workspace = (Workspace) beanFactory.getBean(workspaceId);
    for (Module m : workspace.getModules()) {
      if (moduleId.equals(m.getName())) {
        module = (FilterableBeanCollectionModule) m;
        break;
      }
    }
    
    module.setFilter(new QueryComponent(module.getElementComponentDescriptor(), getEntityFactory()));
    IQueryComponent filter = module.getFilter();
    for (int i = 0; i < criterias.length; i+=2) {
      String key = criterias[i];
      Object value = criterias[i+1];
      
      if (key.equals(Employee.GENDER)) {
        IEnumerationPropertyDescriptor epd = (IEnumerationPropertyDescriptor) module.getElementComponentDescriptor().getPropertyDescriptor(key);
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
    
    String criteria = QueryComponentSerializationUtil.serializeFilter(
            filter, new LinkedHashMap<String, Serializable>());
    
    Map<String, Object> context = new HashMap<>();
    context.put(ActionContextConstants.MODULE, module);
    
    IUserQueriesHelper helper = (IUserQueriesHelper) beanFactory.getBean("userQueriesHelper");
    String key = helper.getKey(context);
    
    UserQuery q = createEntityInstance(UserQuery.class);

    q.setLogin(login);
    q.setKey(key);
    q.setName(queryName);
    q.setCriterias(criteria);
    q.setSeq(filterSeq++);
    q.setSharedString("[administrator]");

    saveOrUpdate(q);
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

//  @SuppressWarnings({ "unchecked" })
//  private FilterableBeanCollectionModule createFilterModule(String moduleName, Class<?> componentClass) {
//    IEntityFactory factory = getEntityFactory();
//    
//    FilterableBeanCollectionModule module = new FilterableBeanCollectionModule();
//    module.setElementComponentDescriptor((IComponentDescriptor<Object>) factory.getComponentDescriptor(componentClass));
//    module.setFilter(new QueryComponent(factory.getComponentDescriptor(componentClass), factory));
//    module.setPermId(moduleName);
//    return module;
//  }

}
