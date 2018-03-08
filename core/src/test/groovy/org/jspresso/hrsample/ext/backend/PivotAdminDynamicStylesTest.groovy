package org.jspresso.hrsample.ext.backend

import org.jspresso.contrib.model.pivot.PivotSetup
import org.jspresso.framework.application.backend.BackendControllerHolder
import org.jspresso.framework.application.backend.persistence.hibernate.HibernateBackendController
import org.jspresso.framework.ext.pivot.backend.PivotHelper
import org.jspresso.framework.ext.pivot.model.PivotFilterableBeanCollectionModule
import org.junit.After
import org.junit.Test

class PivotAdminDynamicStylesTest extends PivotAdminTest {

  
  
  /**
   * Test
   */
  @Test
  void test() {
    
    Map styleSets;
    
    eachIteration('test') { tmar ->

      HibernateBackendController controller = BackendControllerHolder.getCurrentBackendController()
      controller.getApplicationSession().setLocale(Locale.ENGLISH);
      int l = tmar.getCurrentIterationNumber()

      // load styles
      if (l == 1) {
        
        // non dynamic styles
        styleSets = loadStyles(tmar)

        // load dynamic styles
        loadDynamicStyles(tmar, styleSets)
      }

      // create and load pivot setup
      PivotSetup pivotSetup = createPivotSetup(tmar)

      // init pivot module
      PivotFilterableBeanCollectionModule module = initPivotModule(pivotSetup)

      // prepare assertions
      def crossItems = module.getPivotRefiner().getCrossItems()
      Map<String, String> styles = crossItems.getStyleAsMapAttributesFromMeasureValue(tmar.measure, tmar.value)
      if (styles!=null)
        tmar.crossItemsStyle = PivotHelper.stylesMapToString(styles)
    }
  }

  /**
   * createPivotSetup
   */
  protected PivotSetup createPivotSetup(tmar) {
    return createPivotSetup(tmar, false, false, false)
  }

  /**
   * cleanup data
   * @param tmar
   * @return
   */
  @After
  public void cleanupData() {
    cleanPivotAdmin()
  }
}
