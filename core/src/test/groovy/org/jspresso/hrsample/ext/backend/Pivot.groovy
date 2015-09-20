package org.jspresso.hrsample.ext.backend

import org.jspresso.framework.application.backend.BackendControllerHolder
import org.jspresso.framework.application.backend.IBackendController
import org.jspresso.framework.ext.pivot.model.PivotField
import org.jspresso.framework.ext.pivot.model.PivotMeasure
import org.jspresso.framework.model.entity.IEntityFactory
import org.jspresso.hrsample.model.Employee
import org.junit.Ignore;
import org.junit.Test
import org.slf4j.LoggerFactory;

class Pivot extends TmarBackendStartup {

  @Test
  @Ignore
  void test() {
    
    eachIteration('test') { tmar ->
      
      try {
        IBackendController controller = BackendControllerHolder.getCurrentBackendController()
        IEntityFactory factory = controller.getEntityFactory()
        
        // manage field
        PivotField pf = factory.createComponentInstance(PivotField.class)
        pf.setComponentContract(Employee.class);
        String fieldName = tmar.field != null ? tmar.field : 'any'
        pf.setName(fieldName);
        
        tmar.translation = pf.getTranslation(Employee.class, controller)
        
        // manage measure
        if (tmar.measure !=null) {
          
          PivotMeasure pm = factory.createComponentInstance(PivotMeasure.class)
          pm.setupMeasure(tmar.measure)
          
          tmar.type = pm.getType()
          tmar.sum = pm.isSum()
          tmar.notNull = pm.isNotNull()
          tmar.distinct = pm.isDistinct()
          tmar.percentil = pm.getPercentileRank()
          tmar.reference = pm.getReferenceCode()
          tmar.percent = pm.getPercent()
          tmar.percentRef = pm.getPercentRef()
          
          tmar.translation = pm.getTranslation()
        }
      }
      catch (Exception ex) {
        LoggerFactory.getLogger(getClass()).error("Test error", ex)
      }
    }
  }

}
