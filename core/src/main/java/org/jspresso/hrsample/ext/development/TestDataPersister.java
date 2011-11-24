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
      createFurniture("Blue desk - 2012");
      createFurniture("Main blue desk - 2012");
      createFurniture("Various great table - 2012");
    } catch (Throwable ex) {
      // In no way the test data persister should make the application
      // startup fail.
    }
  }
  
  
  private Furniture createFurniture(String name) {
    Furniture furniture = createEntityInstance(Furniture.class);
    furniture.setName(name);
    furniture.setCreateTimestamp(new Date());
    saveOrUpdate(furniture);
    return furniture;
  }

}
