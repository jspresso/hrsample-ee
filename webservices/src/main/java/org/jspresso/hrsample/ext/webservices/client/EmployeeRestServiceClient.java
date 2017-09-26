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
package org.jspresso.hrsample.ext.webservices.client;

import org.jboss.resteasy.client.ProxyFactory;
import org.jboss.resteasy.plugins.providers.RegisterBuiltin;
import org.jboss.resteasy.spi.ResteasyProviderFactory;
import org.jspresso.hrsample.ext.webservices.IEmployeeRestService;
import org.jspresso.hrsample.ext.webservices.IEmployeeRestService.EmployeeDto;

/**
 * A sample java client for the Employee REST web service.
 *
 * @author Vincent Vandenschrick
 */
public class EmployeeRestServiceClient {

  /**
   * Calls the employee web service using the args parameter.
   *
   * @param args
   *          the names of the Employees to lookup.
   */
  public static void main(String[] args) {
//    if (args == null || args.length == 0) {
//      throw new IllegalArgumentException(
//          "You have to provide at least one Employee name.");
//    }

    // this initialization only needs to be done once per VM
    RegisterBuiltin.register(ResteasyProviderFactory.getInstance());

    IEmployeeRestService client = ProxyFactory.create(
        IEmployeeRestService.class, "http://localhost:8080/rest");
    EmployeeDto employee = client.getEmployee("Berlutti", "demo", "demo");

    System.out.println(employee);
  }
}
