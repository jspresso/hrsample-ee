package org.jspresso.hrsample.ext.frontend.usage;

import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.jspresso.framework.action.IActionHandler;
import org.jspresso.framework.application.backend.persistence.hibernate.HibernateBackendController;
import org.jspresso.framework.application.frontend.action.FrontendAction;
import org.jspresso.framework.application.model.Module;
import org.jspresso.hrsample.ext.model.usage.ModuleUsage;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;

/**
 * This action should be parametered as "onModuleEnter" in 
 * the controller bean to log module entry
 * 
 * @author Maxime HAMM
 *
 * @param <E>
 * @param <F>
 * @param <G>
 */
public class AnyModuleEnterFrontAction<E, F, G> extends FrontendAction<E, F, G> {
  
  private static final String LOG_CACHE = "USAGE_LOG_CACHE";
  
  private boolean logOncePerSession = false;
  
  /** 
   * set if logging each time entering a module or only once per module and per session
   * @param logOncePerSession
   */
  public void setLogOncePerSession(boolean logOncePerSession) {
    this.logOncePerSession = logOncePerSession;
  }
  
  @Override
  public boolean execute(final IActionHandler actionHandler, final Map<String, Object> context) {
    
    Module currentModule = getCurrentModule(context);
    if (currentModule.getName() != null &&                         // it is not a dynamic node module
        currentModule.getProjectedViewDescriptor() !=null &&       // any module except "nodeModule"
        !noMoreLogForSession(currentModule.getName(), context)) {  // check if log already done for this session (if logOncePerSession==true)
    
      
      final HibernateBackendController bc = (HibernateBackendController) getBackendController(context);
      bc.getTransactionTemplate().execute(new TransactionCallbackWithoutResult() {

        @Override
        protected void doInTransactionWithoutResult(TransactionStatus status) {
          ModuleUsage mu = newModuleUsage(actionHandler, context);
          bc.registerForUpdate(mu);
        }

      });
    }
    
    return super.execute(actionHandler, context);
  }
  
  /**
   * check if log already done for this session (if logOncePerSession==true)
   * 
   * @param name
   * @param context
   * @return
   */
  private boolean noMoreLogForSession(String moduleId, Map<String, Object> context) {
    if (! logOncePerSession) {
      return false;
    }
    
    @SuppressWarnings("unchecked")
    Set<String> alreadyLoggedSession = (Set<String>) getBackendController(context).getApplicationSession().getCustomValue(LOG_CACHE);
    if (alreadyLoggedSession == null) {
      alreadyLoggedSession = new HashSet<String>();
      getBackendController(context).getApplicationSession().putCustomValue(LOG_CACHE, alreadyLoggedSession);
    }
    
    if (alreadyLoggedSession.contains(moduleId)) {
      return true;
    }
    else {
      alreadyLoggedSession.add(moduleId);
    }
    
    return false;
  }

  /**
   * generate a new module usage entity to log the module entry
   * 
   * @param actionHandler
   * @param context
   * @return
   */
  public ModuleUsage newModuleUsage(IActionHandler actionHandler, Map<String, Object> context) {
    ModuleUsage mu = getBackendController(context).getEntityFactory().createEntityInstance(ModuleUsage.class);
    mu.setAccessDate(new Date());
    mu.setAccessBy(getBackendController(context).getApplicationSession().getPrincipal().getName());
    mu.setModuleId(getCurrentModule(context).getName());
    
    //Logger logger = Logger.getLogger(getClass().getName());
    //logger.log(Level.DEBUG, "Module usage logged : '" + mu.getModuleId() + "'", null);
    
    return mu;
  }
}
