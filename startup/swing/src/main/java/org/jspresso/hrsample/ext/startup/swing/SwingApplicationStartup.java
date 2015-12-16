package org.jspresso.hrsample.ext.startup.swing;

import org.jspresso.framework.application.startup.swing.SwingStartup;

/**
 * Swing application startup class.
 */
public class SwingApplicationStartup extends SwingStartup {

  /**
   * Returns the "hrsample-ext-swing-context" value.
   * <p>
   * {@inheritDoc}
   */
  @Override
  protected String getApplicationContextKey() {
    return "hrsample-ext-swing-context";
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
