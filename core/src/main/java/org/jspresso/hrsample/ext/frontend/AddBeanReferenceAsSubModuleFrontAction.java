package org.jspresso.hrsample.ext.frontend;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.jspresso.framework.application.frontend.action.AddBeanAsSubModuleFrontAction;
import org.jspresso.framework.util.accessor.IAccessor;
import org.jspresso.framework.util.accessor.IAccessorFactory;
import org.jspresso.framework.util.exception.NestedRuntimeException;

/**
 * open a bean reference as sub module
 * @author Maxime HAMM
 *
 * @param <E>
 * @param <F>
 * @param <G>
 */
public class AddBeanReferenceAsSubModuleFrontAction<E,F,G> extends AddBeanAsSubModuleFrontAction<E,F,G> {

  private String openReferenceName = null;
  
  /**
   * setup the name of the reference giving the component to add
   * @param openReferenceName
   */
  public void setOpenReferenceName(String openReferenceName) {
    this.openReferenceName = openReferenceName;
  }
  
  /**
   * get the name of the reference giving the component to add
   * @return
   */
  protected String getOpenReferenceName() {
    return openReferenceName;
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  protected List<?> getComponentsToAdd(Map<String, Object> context) {
    
    List<?> components = super.getComponentsToAdd(context);
    if (getOpenReferenceName() == null || components==null || components.isEmpty()) {
      return components;
    }
    
    try {
      
      IAccessorFactory accessorFactory = getBackendController(context).getAccessorFactory();
      List<Object> references = new ArrayList<Object>();
      for (Object o : components) {
        IAccessor accessor = accessorFactory.createPropertyAccessor(getOpenReferenceName(), o.getClass());
        Object v = accessor.getValue(o);
        if (v instanceof Collection<?>) {
          references.addAll((Collection<?>)v);
        }
        else {
          references.add(v);
        }
      }
      return references;
      
    } catch (IllegalAccessException e) {
      throw new NestedRuntimeException(e);
    } catch (InvocationTargetException e) {
      throw new NestedRuntimeException(e);
    } catch (NoSuchMethodException e) {
      throw new NestedRuntimeException(e);
    }
    
  }
  
}
