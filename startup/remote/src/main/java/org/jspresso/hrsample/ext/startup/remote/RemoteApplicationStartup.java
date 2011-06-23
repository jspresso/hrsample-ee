package org.jspresso.hrsample.ext.startup.remote;

import org.jspresso.framework.application.startup.remote.RemoteStartup;

/**
 * Remote application startup class.
 */
public class RemoteApplicationStartup extends RemoteStartup {

  /**
   * Returns the "hrsample-ext-remote-context" value.
   * <p>
   * {@inheritDoc}
   */
  @Override
  protected String getApplicationContextKey() {
    return "hrsample-ext-remote-context";
    // return "hrsample-ext-remote-recording-context";
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
