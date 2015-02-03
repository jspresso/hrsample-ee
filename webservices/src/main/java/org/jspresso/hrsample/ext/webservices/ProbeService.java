/*
 * Copyright (c) 2005-2013 Vincent Vandenschrick. All rights reserved.
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

import java.sql.Connection;
import java.sql.SQLException;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.hibernate.jdbc.Work;
import org.jspresso.framework.application.backend.BackendControllerHolder;
import org.jspresso.framework.application.backend.persistence.hibernate.HibernateBackendController;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;

/**
 * An application health probe.
 * 
 * @author Vincent Vandenschrick
 */
@Path("/probe")
public class ProbeService extends AbstractService {

  /**
   * Tests that the application backend controller can be instanciated and that
   * a connection can be taken from the datasource backing up the Hibernate
   * session.
   * @return &quot;SUCCESS&quot; if the test is successful.
   */
  @GET
  @Path("/alive")
  @Produces({
    MediaType.TEXT_PLAIN
  })
  public String probeHealth() {
    final HibernateBackendController hbc = (HibernateBackendController) getBackendController();
    try {
      hbc.getTransactionTemplate().execute(
          new TransactionCallbackWithoutResult() {

            @Override
            protected void doInTransactionWithoutResult(TransactionStatus status) {
              hbc.getHibernateSession().doWork(new Work() {

                @Override
                public void execute(Connection connection) throws SQLException {
                  // Do nothing but taking a connection.
                }
              });
            }
          });
    } finally {
      hbc.cleanupRequestResources();
      BackendControllerHolder.setThreadBackendController(null);
    }
    return "SUCCESS";
  }
}
