package org.jspresso.hrsample.ext.frontend.usage;

import java.util.Map;

import org.jspresso.framework.action.IActionHandler;
import org.jspresso.framework.application.frontend.action.FrontendAction;
import org.jspresso.framework.binding.IValueConnector;
import org.jspresso.hrsample.ext.model.usage.MUStat;

public class DoubleSelectModuleFromTreeAction<E, F, G> extends FrontendAction<E, F, G> {

  /**
   * select module from tree node
   */
  @Override
  public boolean execute(IActionHandler actionHandler, Map<String, Object> context) {
    
    Object actionParam = getModel(context);
   
    if (actionParam instanceof MUStat) {
      // selecting the tree root node
      MUStat stat = (MUStat) getModel(context); 
      stat.setWorkspace(null);
    }
    
    return super.execute(actionHandler, context);
  }

  
}
