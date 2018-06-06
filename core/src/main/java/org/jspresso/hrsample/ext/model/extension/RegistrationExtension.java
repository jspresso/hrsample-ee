package org.jspresso.hrsample.ext.model.extension;

import org.jspresso.framework.model.component.AbstractComponentExtension;
import org.jspresso.framework.model.component.service.DependsOn;
import org.jspresso.framework.model.component.service.DependsOnGroup;

import org.jspresso.hrsample.ext.model.Registration;
import org.jspresso.hrsample.ext.model.IRegistrationExtension;

/**
 * TODO Comment needed.
 *
 * @author Vincent Vandenschrick
 */
public class RegistrationExtension extends AbstractComponentExtension<Registration> implements IRegistrationExtension {

  /**
   * Constructs a new {@code RegistrationExtension} instance.
   *
   * @param extendedRegistration
   *     The extended Registration instance.
   */
  public RegistrationExtension(Registration extendedRegistration) {
    super(extendedRegistration);
  }

  /**
   * Gets the complete.
   *
   * @return the complete.
   */
  @DependsOn({Registration.FIRST_NAME, Registration.NAME})
  @Override
  public boolean isComplete() {
    Registration registration = getComponent();
    return registration.getName() != null;
  }


}
