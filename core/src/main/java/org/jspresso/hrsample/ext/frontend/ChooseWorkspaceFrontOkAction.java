package org.jspresso.hrsample.ext.frontend;

import java.util.Map;

import org.jspresso.framework.action.IActionHandler;
import org.jspresso.framework.application.frontend.action.FrontendAction;
import org.jspresso.hrsample.ext.model.usage.MUStat;
import org.jspresso.hrsample.ext.model.usage.MUWorkspace;

public class ChooseWorkspaceFrontOkAction<E, F, G> extends FrontendAction<E, F, G> {

  @Override
  public boolean execute(IActionHandler actionHandler, Map<String, Object> context) {
    
    MUStat stat = (MUStat) getParentModel(context);
    MUWorkspace w = (MUWorkspace) getSelectedModel(context);
    stat.setWorkspace(w); 
    
    return super.execute(actionHandler, context);
  }
}
