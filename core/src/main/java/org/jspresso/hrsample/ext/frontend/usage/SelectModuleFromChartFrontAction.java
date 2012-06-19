package org.jspresso.hrsample.ext.frontend.usage;

import java.util.Iterator;
import java.util.Map;
import java.util.Stack;

import org.apache.log4j.Logger;
import org.jspresso.framework.action.IActionHandler;
import org.jspresso.framework.application.frontend.action.FrontendAction;
import org.jspresso.framework.binding.ICollectionConnectorProvider;
import org.jspresso.framework.binding.IValueConnector;
import org.jspresso.hrsample.ext.model.usage.MUItem;
import org.jspresso.hrsample.ext.model.usage.MUModule;
import org.jspresso.hrsample.ext.model.usage.MUStat;
import org.jspresso.hrsample.ext.model.usage.MUWorkspace;

public class SelectModuleFromChartFrontAction<E, F, G> extends FrontendAction<E, F, G> {

  /**
   * select module from chart slice
   */
  @Override
  public boolean execute(IActionHandler actionHandler, Map<String, Object> context) {
    
    if (getSelectedModel(context) instanceof MUItem && getParentModel(context) instanceof MUStat) {
      
      // selecting a chart slice...
      MUStat stat = (MUStat) getParentModel(context);
      MUItem item = (MUItem) getSelectedModel(context);
      MUModule module = stat.getModule(item.getItemId());
      
      stat.setHistoryModule(module);
      
      //selectTreeModule(stat, module, context);
    }
    
    return super.execute(actionHandler, context);
  }
  
  
  /**
   * select workspace and module in the left tree 
   * 
   * @param workspace
   * @param module
   * @param context
   */
  private void selectTreeModule(MUStat stat, MUModule module, Map<String, Object> context) {
    
    // module's parent workspace
    MUWorkspace workspace = stat.getWorkspaceForModule(module.getModuleId());
    if (workspace == null) {
      return;
    }

    // get tree
    ICollectionConnectorProvider tree;
    try {
      tree = (ICollectionConnectorProvider) getViewConnector(new int[]{-4, 0}, context);
    } catch (Exception ex) {
      Logger.getLogger(getClass()).error(ex);
      return;
    }
    
    // find module node and select it
    selectNodeConnector(tree, module);
   
  }
  
  /**
   * find node for module id
   * @param node
   * @return stop
   */
  private void selectNodeConnector(ICollectionConnectorProvider tree, MUModule module) {
    Stack<IValueConnector> path = getPathToModule(tree, module);
    Iterator<IValueConnector> iter = path.iterator();
    if (! iter.hasNext()) {
      return;
    }
    
    ICollectionConnectorProvider parentNode = (ICollectionConnectorProvider) iter.next();
    while (iter.hasNext()) {
      IValueConnector node = iter.next();
      selectNode(parentNode, node);
      if (iter.hasNext()) {
        parentNode = (ICollectionConnectorProvider) node;
      }
    }
  }
  private void selectNode(ICollectionConnectorProvider parentNode, IValueConnector node) {
    int index = 0;
    for (String workspaceKey : parentNode.getChildConnectorKeys()) {
      IValueConnector subNode = parentNode.getChildConnector(workspaceKey); 
      if (subNode == node) {
        parentNode.setSelectedIndices(new int[]{index});
        return;
      }
      index++;
    }
  }

  /**
   * get path to module
   */
  private Stack<IValueConnector> getPathToModule(ICollectionConnectorProvider tree, MUModule module) {
    Stack<IValueConnector> path = new Stack<IValueConnector>();
    fillPathToModule(tree, module, path);
    return path;
  }
  private boolean fillPathToModule(ICollectionConnectorProvider node, MUModule module, Stack<IValueConnector> path) {
    path.push(node);
    
    Object o = node.getConnectorValue();
    if (module == o) {
      return true;
    }
    for (String workspaceKey : node.getChildConnectorKeys()) {
      IValueConnector subNode = node.getChildConnector(workspaceKey); 
      if (subNode instanceof ICollectionConnectorProvider) {
        boolean stop = fillPathToModule((ICollectionConnectorProvider) subNode, module, path);
        if (stop) {
          return true;
        }
      }
    }
    
    path.pop();
    return false;
  }
  
}
