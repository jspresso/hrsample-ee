package org.jspresso.hrsample.ext.frontend;

import static org.jspresso.framework.model.descriptor.query.ComparableQueryStructureDescriptor.*;

import java.util.Calendar;
import java.util.Map;

import org.jspresso.framework.action.IActionHandler;
import org.jspresso.framework.application.frontend.action.FrontendAction;
import org.jspresso.framework.application.model.FilterableBeanCollectionModule;
import org.jspresso.framework.model.component.IQueryComponent;
import org.jspresso.framework.model.component.query.QueryComponent;
import org.jspresso.hrsample.ext.model.Furniture;

/** 
 * init furniture filter module 
 * @author Maxime HAMM
 */
public class FurnitureModuleInitFrontAction<E, F, G> extends FrontendAction<E, F, G> {

  @Override
  public boolean execute(IActionHandler actionHandler, Map<String, Object> context) {
   
    FilterableBeanCollectionModule module = (FilterableBeanCollectionModule)getModule(context);
    IQueryComponent query = module.getFilter();
    
    Calendar c = Calendar.getInstance();
    c.set(Calendar.DAY_OF_YEAR, 1);
    c.set(Calendar.MILLISECOND, 0);
    c.set(Calendar.SECOND, 0);
    c.set(Calendar.MINUTE, 0);
    c.set(Calendar.HOUR_OF_DAY, 0);
    
    QueryComponent d = (QueryComponent) query.get(Furniture.LAST_UPDATE_TIMESTAMP);
    d.put(INF_VALUE, c.getTime());
    d.put(COMPARATOR, GT);
    
    return super.execute(actionHandler, context);
  }
  
}
