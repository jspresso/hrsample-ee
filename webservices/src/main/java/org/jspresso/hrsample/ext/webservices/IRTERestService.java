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

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

/**
 * Employee REST service specification. Can be used on client side by RestEasy
 * client framework.
 *
 * @author Vincent Vandenschrick
 */
@Path("/rte")
public interface IRTERestService {

  /**
   * Retrieves an employee by its name.
   *
   * @param name
   *     the name of the employee to retrieve.
   * @return the employee simplified DTO.
   */
  @GET
  @Path("/templates")
  @Produces({MediaType.APPLICATION_JSON})
  TemplateDto[] getTemplates();

  /**
   * Employee DTO.
   */
  @XmlAccessorType(XmlAccessType.FIELD)
  class TemplateDto {

    /**
     * The Title.
     */
    public String title;

    /**
     * The Description.
     */
    public String description;

    /**
     * The Content.
     */
    public String content;
  }
}
