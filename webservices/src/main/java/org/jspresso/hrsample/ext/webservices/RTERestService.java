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
package org.jspresso.hrsample.ext.webservices;

import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.NotFoundException;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import org.jspresso.framework.application.backend.persistence.hibernate.HibernateBackendController;
import org.jspresso.framework.application.backend.session.EMergeMode;
import org.jspresso.framework.model.persistence.hibernate.criterion.EnhancedDetachedCriteria;

import org.jspresso.hrsample.model.Employee;

/**
 * A sample web service for rich text editor backend callbacks.
 *
 * @author Vincent Vandenschrick
 */
public class RTERestService extends AbstractService implements
    IRTERestService {

  @Override
  public TemplateDto[] getTemplates() {
    HibernateBackendController backendController = (HibernateBackendController) getBackendController();
    TemplateDto template = new TemplateDto();
    template.title = "Sample template";
    template.description = "Sample template description";
    template.content = "<b>Test Template</b> for " + backendController.getApplicationSession().getUsername();
    return new TemplateDto[] {template};
  }
}
