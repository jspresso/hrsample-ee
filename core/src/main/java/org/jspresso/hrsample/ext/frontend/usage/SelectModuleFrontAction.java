package org.jspresso.hrsample.ext.frontend.usage;

import java.util.Map;

import org.jspresso.framework.action.IActionHandler;
import org.jspresso.framework.application.frontend.action.FrontendAction;
import org.jspresso.hrsample.ext.model.usage.MUItem;
import org.jspresso.hrsample.ext.model.usage.MUModule;
import org.jspresso.hrsample.ext.model.usage.MUStat;

public class SelectModuleFrontAction<E, F, G> extends FrontendAction<E, F, G> {

  @Override
  public boolean execute(IActionHandler actionHandler, Map<String, Object> context) {
    
    if (getSelectedModel(context) instanceof MUItem && getParentModel(context) instanceof MUStat) {
      MUStat stat = (MUStat) getParentModel(context);
      MUItem item = (MUItem) getSelectedModel(context);
      MUModule module = stat.getModule(item.getItemId());
      
      stat.setHistoryModule(module);
    }
    
    return super.execute(actionHandler, context);
  }
}
