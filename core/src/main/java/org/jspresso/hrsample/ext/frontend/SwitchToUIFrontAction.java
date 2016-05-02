package org.jspresso.hrsample.ext.frontend;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.jspresso.framework.action.IActionHandler;
import org.jspresso.framework.application.frontend.action.FrontendAction;
import org.jspresso.framework.util.http.HttpRequestHolder;

/**
 * Switch to flex or html5 UI.
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
public class SwitchToUIFrontAction<E, F, G> extends FrontendAction<E, F, G> {
  
  /**
   * {@inheritDoc}
   */
  @Override
  public boolean execute(IActionHandler actionHandler, Map<String, Object> context) {
    
    HttpServletRequest request = HttpRequestHolder.getServletRequest();

    String ui = request.getRequestURI().contains(".qxrpc") ? "flex" : "html5";
    
    StringBuilder sb = new StringBuilder();
    sb.append(request.getScheme()).append("://").append(request.getServerName()).append(':').append(request.getServerPort());
    sb.append(request.getContextPath()).append('/').append(ui).append("/index.html");
    
    setActionParameter(sb.toString(), context);
    
    return super.execute(actionHandler, context);
  }
  
}
