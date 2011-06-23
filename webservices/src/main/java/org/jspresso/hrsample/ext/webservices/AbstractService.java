package org.jspresso.hrsample.ext.webservices;

import java.util.Locale;

import org.jspresso.framework.application.startup.AbstractBackendStartup;

/**
 * Abstract base service implementation.
 */
public abstract class AbstractService extends AbstractBackendStartup {
  
  /**
   * Constructs a new <code>AbstractService</code> instance.
   * 
   */
  protected AbstractService() {
    start();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected String getApplicationContextKey() {
    return "hrsample-ext-backend-context";
  }

  /**
   * Overrides default bean ref locator.
   * <p>
   * {@inheritDoc}
   */
  @Override
  protected String getBeanFactorySelector() {
    return "org/jspresso/hrsample/ext/beanRefFactory.xml";
  }
}
