package org.jspresso.hrsample.ext.development;

import java.util.Date;

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
  public void persistTestData() {
    super.persistTestData();

    try {
      createFurniture("Blue desk - 2012", 99.95, 0.1);
      createFurniture("Main blue desk - 2012", 69.95, 0.15);
      createFurniture("Various great table - 2012", 139.95, 0.1);
    } catch (Throwable ex) {
      // In no way the test data persister should make the application
      // startup fail.
    }
    
    try {
      // furniture.module
      createModuleUsages("furniture.module", "Tom", 365, 1800);
      createModuleUsages("furniture.module", "Bob", 365, 120);
      createModuleUsages("furniture.module", "Alice", 240, 120);
      
      
    } catch (Throwable ex) {
      // In no way the test data persister should make the application
      // startup fail.
    }
  }

  /**
   * create a set of Module usage for module <i>moduleId</i> 
   * from <i>daysAgo</i> to now, using a random method 
   * to set a total of <i>accessCount</i> access
   * 
   * @param moduleId 
   * @param daysAgo
   * @param accessCount
   */
  private void createModuleUsages(String moduleId, String accessBy, int daysAgo, double accessCount) {
    
    
    
  }

  /**
   * create a new Furniture
   * @param name
   * @param price
   * @param discount
   * @return
   */
  private Furniture createFurniture(String name, double price, double discount) {
    Furniture furniture = createEntityInstance(Furniture.class);
    furniture.setName(name);
    furniture.setCreateTimestamp(new Date());
    furniture.setPrice(new Double(price));
    furniture.setDiscount(new Double(discount));
    saveOrUpdate(furniture);
    return furniture;
  }

}
