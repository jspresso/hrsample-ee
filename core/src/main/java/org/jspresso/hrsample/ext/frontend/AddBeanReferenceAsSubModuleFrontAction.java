/*
 * Copyright (c) 2005-2016 Vincent Vandenschrick. All rights reserved.
 *
 *  This file is part of the Jspresso framework.
 *
 *  Jspresso is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Jspresso is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with Jspresso.  If not, see <http://www.gnu.org/licenses/>.
 */
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
