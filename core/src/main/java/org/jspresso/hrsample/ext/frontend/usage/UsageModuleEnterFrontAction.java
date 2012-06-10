package org.jspresso.hrsample.ext.frontend.usage;

import java.util.Date;
import java.util.Map;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.jspresso.framework.action.IActionHandler;
import org.jspresso.framework.application.backend.persistence.hibernate.HibernateBackendController;
import org.jspresso.framework.application.frontend.action.FrontendAction;
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
public class UsageModuleEnterFrontAction<E, F, G> extends FrontendAction<E, F, G> {
  
  @Override
  public boolean execute(final IActionHandler actionHandler, final Map<String, Object> context) {
    
    final HibernateBackendController bc = (HibernateBackendController) getBackendController(context);
    bc.getTransactionTemplate().execute(new TransactionCallbackWithoutResult() {

      @Override
      protected void doInTransactionWithoutResult(TransactionStatus status) {
        ModuleUsage mu = newModuleUsage(actionHandler, context);
        bc.getHibernateSession().saveOrUpdate(mu);
      }
      
    });
    
    return super.execute(actionHandler, context);
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
    mu.setModuleId(getCurrentModule(context).getName());
    
    Logger logger = Logger.getLogger(getClass().getName());
    logger.log(Level.DEBUG, "Module usage logged : '" + mu.getModuleId() + "'", null);
    
    return mu;
  }
}
