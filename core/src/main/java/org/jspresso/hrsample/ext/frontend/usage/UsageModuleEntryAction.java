package org.jspresso.hrsample.ext.frontend.usage;

import java.util.Map;

import org.jspresso.framework.action.IActionHandler;
import org.jspresso.framework.application.frontend.action.FrontendAction;
import org.jspresso.framework.application.model.BeanModule;
import org.jspresso.hrsample.ext.model.usage.MUStat;

public class UsageModuleEntryAction<E, F, G> extends FrontendAction<E, F, G> {

  @Override
  public boolean execute(IActionHandler actionHandler, Map<String, Object> context) {
    
    MUStat stat = getBackendController(context).getEntityFactory().createComponentInstance(MUStat.class);
    
    BeanModule statisticsModule = (BeanModule)getModule(context);
    statisticsModule.setModuleObject(stat);
    
    return super.execute(actionHandler, context);
  }
  
}
