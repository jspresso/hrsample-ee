package org.jspresso.hrsample.ext.backend;

import java.security.acl.Group

import javax.security.auth.Subject

import org.hibernate.criterion.DetachedCriteria
import org.jboss.security.SimpleGroup
import org.jboss.security.SimplePrincipal
import org.jspresso.contrib.test.application.startup.AbstractTmarBackendStartup
import org.jspresso.framework.application.backend.persistence.hibernate.HibernateBackendController
import org.jspresso.framework.model.entity.IEntity
import org.jspresso.framework.model.persistence.hibernate.criterion.EnhancedDetachedCriteria
import org.jspresso.framework.security.SecurityHelper
import org.jspresso.framework.security.UserPrincipal
import org.jspresso.framework.util.exception.NestedRuntimeException
import org.jspresso.hrsample.model.City
import org.jspresso.hrsample.model.Company
import org.springframework.transaction.TransactionStatus
import org.springframework.transaction.support.TransactionCallbackWithoutResult

/**
 * Base class for integration tests.
 * 
 * @version $LastChangedRevision$ 
 * @author Maxime Hamm
 */
public class TmarBackendStartup extends AbstractTmarBackendStartup {

  /**
   * Performs DB initialization and test data creation. 
   * 
   * @throws java.lang.Exception
   */
  //@BeforeClass
  //static setUpBeforeClass
  public void setupSpec() throws Exception {
    //TmarBackendStartup startup = new TmarBackendStartup();
    //new TestDataPersister(startup.getApplicationContext()).persistTestData();
  }

  /**
   * Destroys all data from DB.
   * 
   * @throws java.lang.Exception
   */
  //@AfterClass
  //static tearDownAfterClass
  public void cleanupSpec() throws Exception {
    
    TmarBackendStartup startup = new TmarBackendStartup();
    startup.start();
    final HibernateBackendController bc = (HibernateBackendController) startup.getBackendController();
    bc.getTransactionTemplate().execute(new TransactionCallbackWithoutResult() {
      @Override
      protected void doInTransactionWithoutResult(TransactionStatus status) {
        deleteAllInstances(bc, Company.class);
        deleteAllInstances(bc, City.class);
      }
    });
  }

  /**
   * Starts a new controller and creates the session using the "test" user with
   * english locale.
   * 
   * @throws java.lang.Exception
   */
  //@Before
  //setUp
  public void setup() throws Exception {
    start();
    configureApplicationSession(createTestSubject(), getStartupLocale());
  }
  
  /**
   * Stops the controller.
   *
   * @throws java.lang.Exception
   */
  //@After
  //tearDown
  public void cleanup() throws Exception {
    getBackendController().cleanupRequestResources();
    getBackendController().stop();
  }
  
  
  
  /**
   * creat test subject
   * @return
   */
  private Subject createTestSubject() {
    Subject testSubject = new Subject();
    UserPrincipal p = new UserPrincipal("demo");
    testSubject.getPrincipals().add(p);
    p.putCustomProperty(UserPrincipal.LANGUAGE_PROPERTY, "en");
    Group rolesGroup = new SimpleGroup(SecurityHelper.ROLES_GROUP_NAME);
    rolesGroup.addMember(new SimplePrincipal("administrator"));
    testSubject.getPrincipals().add(rolesGroup);
    return testSubject;
  }
  
  

  /**
   * delete all instances
   * @param bc
   * @param clazz
   */
  private static <E extends IEntity> void deleteAllInstances(HibernateBackendController bc, Class<E> clazz) {
    DetachedCriteria crit;
    crit = EnhancedDetachedCriteria.forClass(clazz);
    for (E entity : bc.findByCriteria(crit, null, clazz)) {
      try {
        bc.cleanRelationshipsOnDeletion(entity, false);
      } catch (Exception ex) {
        throw new NestedRuntimeException(ex);
      }
    }
    bc.getHibernateSession().flush();
  }



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
}
