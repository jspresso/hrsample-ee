package org.jspresso.hrsample.ext.backend;

import java.security.acl.Group

import javax.security.auth.Subject

import org.hibernate.criterion.DetachedCriteria
import org.jboss.security.SimpleGroup
import org.jboss.security.SimplePrincipal
import org.jspresso.contrib.test.application.startup.AbstractTmar4JUnitBackendStartup
import org.jspresso.framework.application.backend.persistence.hibernate.HibernateBackendController
import org.jspresso.framework.model.entity.IEntity
import org.jspresso.framework.model.persistence.hibernate.criterion.EnhancedDetachedCriteria
import org.jspresso.framework.security.SecurityHelper
import org.jspresso.framework.security.UserPrincipal
import org.jspresso.framework.util.exception.NestedRuntimeException
import org.jspresso.hrsample.model.City
import org.jspresso.hrsample.model.Company
import org.junit.AfterClass
import org.springframework.transaction.TransactionStatus
import org.springframework.transaction.support.TransactionCallbackWithoutResult
import groovy.transform.TypeChecked

/**
 * Base class for integration tests.
 * 
 * @version $LastChangedRevision$ 
 * @author Maxime Hamm
 */
@TypeChecked
public class TmarBackendStartup extends AbstractTmar4JUnitBackendStartup {

  
  /**
   * Returns the "hrsample-ext-backend-context" value.
   * <p>
   * {@inheritDoc}
   */
  @Override
  protected String getApplicationContextKey() {
    return "hrsample-ext-backend-context";
  }

  /**
   * Returns "org/jspresso/hrsample/ext/beanRefFactory.xml".
   * <p>
   * {@inheritDoc}
   */
  @Override
  protected String getBeanFactorySelector() {
    return "org/jspresso/hrsample/ext/beanRefFactory.xml";
  }
  
  /**
   * Destroys all data from DB.
   * @throws java.lang.Exception
   */
  @AfterClass
  public static void cleanupSpec() throws Exception {
    
    TmarBackendStartup startup = new TmarBackendStartup();
    startup.start();
    final HibernateBackendController bc = (HibernateBackendController) startup.getBackendController();
    bc.getTransactionTemplate().execute(new TransactionCallbackWithoutResult() {
      @Override
      protected void doInTransactionWithoutResult(TransactionStatus status) {
        deleteAllEntities(Company.class);
        deleteAllEntities(City.class);
      }
    });
  }
  
  /**
   * create test subject
   * @return
   */
  @Override
  protected Subject createTestSubject() {
    Subject testSubject = new Subject();
    UserPrincipal p = new UserPrincipal("demo");
    testSubject.getPrincipals().add(p);
    p.putCustomProperty(UserPrincipal.LANGUAGE_PROPERTY, "en");
    Group rolesGroup = new SimpleGroup(SecurityHelper.ROLES_GROUP_NAME);
    rolesGroup.addMember(new SimplePrincipal("administrator"));
    testSubject.getPrincipals().add(rolesGroup);
    return testSubject;
  }
  


}
