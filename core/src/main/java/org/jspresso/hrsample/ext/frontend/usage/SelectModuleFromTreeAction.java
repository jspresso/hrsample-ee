package org.jspresso.hrsample.ext.frontend.usage;

import java.util.Map;

import org.jspresso.framework.action.IActionHandler;
import org.jspresso.framework.application.frontend.action.FrontendAction;
import org.jspresso.framework.binding.ICollectionConnectorProvider;
import org.jspresso.framework.binding.IValueConnector;
import org.jspresso.hrsample.ext.model.usage.MUModule;
import org.jspresso.hrsample.ext.model.usage.MUStat;
import org.jspresso.hrsample.ext.model.usage.MUWorkspace;

public class SelectModuleFromTreeAction<E, F, G> extends FrontendAction<E, F, G> {

  /**
   * select module from tree node
   */
  @Override
  public boolean execute(IActionHandler actionHandler, Map<String, Object> context) {
    
    
    Object actionParam = getActionParameter(context)!=null ? ((IValueConnector)getActionParameter(context)).getConnectorValue() : null;
    
    if (actionParam instanceof MUModule) {
      
      // selecting a module tree node
      MUStat stat = (MUStat) getModel(context);
      MUModule module = (MUModule) getSelectedModel(context);
      MUWorkspace workspace = stat.getWorkspaceForModule(module.getModuleId());
      if (workspace!=null) {
        stat.setWorkspace(workspace);
      }
      
      stat.setHistoryModule(module);
    }
    else if (actionParam instanceof MUWorkspace) {
      
     // selecting a workspace tree node
      MUStat stat = (MUStat) getModel(context);
      MUWorkspace workspace = (MUWorkspace) getSelectedModel(context);
      
      stat.setWorkspace(workspace);
    }
    else if (actionParam instanceof MUStat) {
      
      // selecting the tree root node
//      MUStat stat = (MUStat) getModel(context); 
//      stat.setWorkspace(null);
    }
    
    return super.execute(actionHandler, context);
  }

  private boolean isRootNodeSelected(Map<String, Object> context) {
    ICollectionConnectorProvider tree = (ICollectionConnectorProvider) getViewConnector(context);
    int counter = getSelectedNodeCount(tree, context);
    return counter>1;
  }
  private int getSelectedNodeCount(ICollectionConnectorProvider parentNode, Map<String, Object> context) {
    int counter = 0;
    for (String nodeKey : parentNode.getChildConnectorKeys()) {
      IValueConnector node = parentNode.getChildConnector(nodeKey); 
      if (node instanceof ICollectionConnectorProvider) {
        int selectedIndices[] = ((ICollectionConnectorProvider) node).getSelectedIndices();
        if (selectedIndices!=null) {
          counter += selectedIndices.length;
        }
        counter += getSelectedNodeCount((ICollectionConnectorProvider) node, context);
      }
    } 
    
    return counter;
  }

  
  
}
