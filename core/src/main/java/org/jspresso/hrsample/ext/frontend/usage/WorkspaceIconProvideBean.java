package org.jspresso.hrsample.ext.frontend.usage;

import org.jspresso.framework.util.gui.Icon;
import org.jspresso.framework.util.gui.IconProvider;
import org.jspresso.hrsample.ext.model.usage.MUModuleInterface;

public class WorkspaceIconProvideBean implements IconProvider {

  @Override
  public Icon getIconForObject(Object userObject) {
    if (userObject instanceof MUModuleInterface) {
      MUModuleInterface m = (MUModuleInterface)userObject;
      if (m.getIconImageUrl() != null) {
        Icon icon = new Icon(m.getIconImageUrl(), null);
        return icon;
      }
    }
    return null;
  }

}