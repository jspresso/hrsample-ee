package org.jspresso.hrsample.ext.startup.swing.development;

import org.jspresso.hrsample.ext.development.HibernateTestDataPersister;
import org.jspresso.hrsample.ext.startup.swing.SwingApplicationStartup;

/**
 * Swing development application startup class.
 */
public class SwingDevApplicationStartup extends SwingApplicationStartup {

  /**
   * Sets up some test data before actually starting.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public void start() {
    new HibernateTestDataPersister(getApplicationContext()).persistTestData();
    super.start();
  }
}
