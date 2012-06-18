package org.jspresso.hrsample.ext.frontend.usage;

import java.util.Map;

import org.jspresso.framework.action.IActionHandler;
import org.jspresso.framework.application.frontend.action.FrontendAction;
import org.jspresso.hrsample.ext.model.usage.MUModule;
import org.jspresso.hrsample.ext.model.usage.MUStat;
import org.jspresso.hrsample.ext.model.usage.MUWorkspace;

public class SelectModuleFromTreeAction<E, F, G> extends FrontendAction<E, F, G> {

  /**
   * select module from tree node
   */
  @Override
  public boolean execute(IActionHandler actionHandler, Map<String, Object> context) {
    
    if (getSelectedModel(context) instanceof MUModule && getModel(context) instanceof MUStat) {
      
      // selecting a module tree node
      MUStat stat = (MUStat) getModel(context);
      MUModule module = (MUModule) getSelectedModel(context);
      
      stat.setHistoryModule(module);
    }
    else if (getSelectedModel(context) instanceof MUWorkspace && getModel(context) instanceof MUStat) {
      
     // selecting a workspace tree node
      MUStat stat = (MUStat) getModel(context);
      MUWorkspace workspace = (MUWorkspace) getSelectedModel(context);
      
      stat.setWorkspace(workspace);
    }
    else if (getSelectedModel(context)==null && getModel(context) instanceof MUStat) {

      // selecting the tree root node
      // TODO Why is the selected model null when selecting a workspace node ??? it shoud be MUStat...
      //MUStat stat = (MUStat) getModel(context);
      //stat.setWorkspace(null);
    }
    
    return super.execute(actionHandler, context);
  }

  
  
}
