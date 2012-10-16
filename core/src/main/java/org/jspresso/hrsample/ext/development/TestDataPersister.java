package org.jspresso.hrsample.ext.development;

import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import org.jspresso.contrib.usage.model.ModuleUsage;
import org.jspresso.hrsample.ext.model.Furniture;
import org.springframework.beans.factory.BeanFactory;

/**
 * Persists some test data for the application.
 */
public class TestDataPersister extends
    org.jspresso.hrsample.development.TestDataPersister {

  /**
   * Constructs a new <code>TestDataPersister</code> instance.
   * 
   * @param beanFactory
   *          the spring bean factory to use.
   */
  public TestDataPersister(BeanFactory beanFactory) {
    super(beanFactory);
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
  }

  private Furniture createFurniture(String name, double price, double discount) {
    Furniture furniture = createEntityInstance(Furniture.class);
    furniture.setName(name);
    furniture.setCreateTimestamp(new Date());
    furniture.setPrice(Double.valueOf(price));
    furniture.setDiscount(Double.valueOf(discount));
    saveOrUpdate(furniture);
    return furniture;
  }

  /**
   * create a set of Module usage for module <i>moduleId</i> from <i>daysAgo</i>
   * to now, using a random method to set a total of <i>accessCount</i> access
   * 
   * @param moduleId
   * @param daysAgo
   * @param accessCount
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
