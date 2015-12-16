package org.jspresso.hrsample.ext.webapp;

import org.jspresso.framework.application.startup.development.AbstractTestDataContextListener;
import org.springframework.beans.factory.BeanFactory;

import org.jspresso.hrsample.ext.development.HibernateTestDataPersister;

/**
 * A simple listener to hook in webapp startup and persist sample data.
 */
public class TestDataContextListener extends AbstractTestDataContextListener {

  /**
   * {@inheritDoc}
   */
  @Override
  public void persistTestData(BeanFactory beanFactory) {
    new HibernateTestDataPersister(beanFactory).persistTestData();
  }

}
