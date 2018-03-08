package org.jspresso.hrsample.ext.backend

import org.jspresso.contrib.model.pivot.PivotSetup
import org.jspresso.contrib.model.pivot.PivotSetupField
import org.jspresso.contrib.model.pivot.PivotStyleReference;
import org.jspresso.contrib.model.pivot.PivotStyleSet
import org.jspresso.framework.application.backend.BackendControllerHolder
import org.jspresso.framework.application.backend.persistence.hibernate.HibernateBackendController
import org.jspresso.framework.ext.pivot.model.PivotFilterableBeanCollectionModule
import org.junit.After;
import org.junit.Test

class PivotAdminFindStyleReferencesTest extends PivotAdminTest {

  /**
   * Test
   */
  @Test
  void test() {
    
    Map styleSets
    eachIteration('test') { tmar ->

      HibernateBackendController controller = BackendControllerHolder.getCurrentBackendController()
      controller.getApplicationSession().setLocale(Locale.ENGLISH);
      int l = tmar.getCurrentIterationNumber()
      
      // load styles
      if (l ==1)
        styleSets = loadStyles(tmar)
      
      // load dynamic styles
      loadDynamicStyles(tmar, styleSets)
      
      // create and load pivot setup
      PivotSetup pivotSetup = createPivotSetup(tmar)
      
      // init pivot module
      PivotFilterableBeanCollectionModule module = initPivotModule(pivotSetup)
      
      // prepare assertions
      pivotSetup.setAscendantStylesString(tmar.globalParents)
      
      PivotSetupField field = createEntityInstance(PivotSetupField.class);
      field.setPivotSetup(pivotSetup)
      field.setFieldId(tmar.measure)
      field.setAscendantStylesString(tmar.parents)
      saveEntity(controller, pivotSetup)

      PivotStyleSet style = styleSets[tmar.style]
      
      def references = '\n';
      for (PivotStyleReference ref : style.getAllStyleReferences()) {
        String cleaned = ref.label.replaceAll("<[^>]*>", "")
        if (cleaned.startsWith("&nbsp;"))
          continue;
        references <<= cleaned <<= '\n'
      }
       
      def r = references.toString() // references.substring(0, references.size()-1) + '\n'
      tmar.references = r
      
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
