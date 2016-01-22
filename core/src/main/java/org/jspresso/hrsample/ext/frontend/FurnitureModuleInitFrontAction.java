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
