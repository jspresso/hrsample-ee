package org.jspresso.hrsample.ext.frontend

import org.jboss.security.SimpleGroup
import org.jboss.security.SimplePrincipal
import org.jspresso.contrib.test.application.startup.AbstractTmar4JUnitBackendStartup
import org.jspresso.framework.application.backend.BackendControllerHolder
import org.jspresso.framework.application.backend.IBackendController
import org.jspresso.framework.application.backend.persistence.hibernate.HibernateBackendController
import org.jspresso.framework.application.frontend.IFrontendController
import org.jspresso.framework.application.startup.AbstractStartup
import org.jspresso.framework.security.SecurityHelper
import org.jspresso.framework.security.UserPrincipal
import org.jspresso.framework.util.http.HttpRequestHolder
import org.jspresso.hrsample.model.City
import org.jspresso.hrsample.model.Company
import org.junit.After
import org.junit.AfterClass
import org.junit.Before
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.transaction.TransactionStatus
import org.springframework.transaction.support.TransactionCallbackWithoutResult

import javax.security.auth.Subject
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpSession
import java.security.acl.Group

/**
 * Base class for integration tests.
 * 
 * @version $LastChangedRevision$ 
 * @author Maxime Hamm
 */
//@TypeChecked
public class TmarFrontendStartup<E, F, G> extends AbstractTmar4JUnitBackendStartup {

  private static final Logger LOG = LoggerFactory.getLogger(TmarFrontendStartup.class);

  private IFrontendController<E, F, G> frontendController;

  /**
   * Returns the "hrsample-ext-backend-context" value.
   * <p>
   * {@inheritDoc}
   */
  @Override
  protected String getApplicationContextKey() {
    return "hrsample-ext-frontend-test-context"
  }

  /**
   * Returns "org/jspresso/hrsample/ext/beanRefFactoryTest.xml".
   * <p>
   * {@inheritDoc}
   */
  @Override
  protected String getBeanFactorySelector() {
    return "org/jspresso/hrsample/ext/beanRefFactoryTest.xml"
  }
  
  /**
   * @throws java.lang.Exception
   */
  @AfterClass
  public static void cleanupSpec() throws Exception {
    
    TmarFrontendStartup startup = new TmarFrontendStartup()
    startup.start();
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

  @Before
  public void setUp() throws Exception {
    HttpSession mockHttpSession = new MockHttpSession();
    HttpServletRequest mockServletRequest = new MockHttpServletRequest(mockHttpSession, "/hrsample-ext-webapp");
    HttpRequestHolder.setServletRequest(mockServletRequest);

    start();

    getFrontendController().loggedIn(createTestSubject());
  }

  /**
   * Stops the controller.
   *
   * @throws java.lang.Exception
   */
  @After
  public void tearDown() throws Exception {
    getFrontendController().getBackendController().cleanupRequestResources();
    getFrontendController().stop();
  }

  /**
   * Gets the application frontend controller.
   *
   * @return the application frontend controller.
   */
  protected IFrontendController<E, F, G> getFrontendController() {
    return frontendController;
  }

  /**
   * Programmatically stops the application and performs all necessary cleanups.
   */
  public void stop() {
    // Breaks SSO. Useless to perform before garbage collecting.
    // if (frontendController != null) {
    // frontendController.stop();
    // }
    frontendController = null;
    BackendControllerHolder.setSessionBackendController(null);
  }

  /**
   * Both front and back controllers are retrieved from the spring context,
   * associated and started.
   * <p>
   * {@inheritDoc}
   */
  @SuppressWarnings("unchecked")
  @Override
  public void start() {
    // start on brand new instances.
    stop();
    IBackendController backendController;
    try {
      backendController = (IBackendController) getApplicationContext().getBean("applicationBackController");
    } catch (RuntimeException ex) {
      LOG.error("applicationBackController could not be instantiated.", ex);
      throw ex;
    }
    try {
      frontendController = (IFrontendController<E, F, G>) getApplicationContext()
              .getBean("applicationFrontController");
    } catch (RuntimeException ex) {
      LOG.error("applicationFrontController could not be instantiated.", ex);
      throw ex;
    }
    frontendController.start(backendController, getStartupLocale(),
            getClientTimeZone());
    BackendControllerHolder.setSessionBackendController(backendController);
  }

}
