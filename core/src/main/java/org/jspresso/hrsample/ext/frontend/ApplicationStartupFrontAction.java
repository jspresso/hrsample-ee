package org.jspresso.hrsample.ext.frontend;

import java.util.List;
import java.util.Map;

import org.jspresso.framework.action.IActionHandler;
import org.jspresso.framework.application.frontend.IFrontendController;
import org.jspresso.framework.application.frontend.action.FrontendAction;

/**
 * Application startup action.
 * 
 * Select first workspace if current user does only have access to a single workspace.
 * 
 * @author Maxime HAMM
 *
 * @param <E>
 *          the actual gui component type used.
 * @param <F>
 *          the actual icon type used.
 * @param <G>
 *          the actual action type used.
 */
public class ApplicationStartupFrontAction<E, F, G> extends FrontendAction<E, F, G> {

  @Override
  public boolean execute(IActionHandler actionHandler, Map<String, Object> context) {
   
    IFrontendController<Object, Object, Object> controller = getFrontendController(context);
    List<String> workspaceNames = controller.getWorkspaceNames();
    if (workspaceNames.size() == 1) {
      controller.displayWorkspace(workspaceNames.get(0));
    }
    
    return super.execute(actionHandler, context);
  }
}
