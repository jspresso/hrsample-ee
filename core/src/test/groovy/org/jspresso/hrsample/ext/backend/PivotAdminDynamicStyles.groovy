package org.jspresso.hrsample.ext.backend

import org.hibernate.criterion.DetachedCriteria
import org.hibernate.criterion.Restrictions
import org.jspresso.contrib.model.pivot.PivotSetup
import org.jspresso.contrib.model.pivot.PivotSetupField
import org.jspresso.contrib.model.pivot.PivotStyleSet
import org.jspresso.contrib.model.pivot.service.PivotSetupServiceDelegate
import org.jspresso.framework.application.backend.BackendControllerHolder
import org.jspresso.framework.application.backend.persistence.hibernate.HibernateBackendController
import org.jspresso.framework.application.backend.session.EMergeMode
import org.jspresso.framework.application.model.Module
import org.jspresso.framework.application.model.Workspace
import org.jspresso.framework.ext.pivot.backend.PivotHelper
import org.jspresso.framework.ext.pivot.model.IStyle
import org.jspresso.framework.ext.pivot.model.PivotFilterableBeanCollectionModule
import org.jspresso.framework.model.entity.IEntity
import org.junit.Test
import org.springframework.transaction.TransactionStatus
import org.springframework.transaction.support.TransactionCallbackWithoutResult

class PivotAdminDynamicStyles extends PivotAdmin {

  /**
   * Test
   */
  @Test
  void test() {
    eachIteration('test') { tmar ->

      HibernateBackendController controller = BackendControllerHolder.getCurrentBackendController()
      controller.getApplicationSession().setLocale(Locale.ENGLISH);
      int l = tmar.getCurrentIterationNumber()
      
      // load styles
      Map styleSets = loadStyles(tmar)
      
      // load dynamic styles
      loadDynamicStyles(tmar, styleSets)
      
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
}
