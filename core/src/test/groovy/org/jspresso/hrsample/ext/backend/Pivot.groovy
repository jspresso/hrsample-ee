package org.jspresso.hrsample.ext.backend

import java.io.Serializable;

import org.jspresso.framework.application.backend.BackendControllerHolder
import org.jspresso.framework.application.backend.IBackendController
import org.jspresso.framework.ext.pivot.backend.DefaultPivotRefiner
import org.jspresso.framework.ext.pivot.backend.PivotHelper
import org.jspresso.framework.ext.pivot.model.PivotCriteria;
import org.jspresso.framework.ext.pivot.model.PivotField
import org.jspresso.framework.ext.pivot.model.PivotFilterableBeanCollectionModule
import org.jspresso.framework.ext.pivot.model.PivotMeasure
import org.jspresso.framework.model.component.query.QueryComponent;
import org.jspresso.framework.model.component.query.QueryComponentSerializationUtil;
import org.jspresso.framework.model.descriptor.basic.BasicQueryComponentDescriptorFactory;
import org.jspresso.framework.model.entity.IEntityFactory
import org.jspresso.hrsample.model.Employee
import org.junit.Test
import org.slf4j.LoggerFactory


class Pivot extends TmarBackendStartup {

  @Test
  void test() {
    
    eachIteration('test') { tmar ->
      
      try {
        IBackendController controller = BackendControllerHolder.getCurrentBackendController()
        controller.getApplicationSession().setLocale(Locale.ENGLISH);
        
        IEntityFactory factory = controller.getEntityFactory()
        
        if (tmar.measure ==null) {

          // manage field
          PivotField pf = factory.createComponentInstance(PivotField.class)
          pf.setComponentClass(Employee.class);
          String fieldName = tmar.field != null ? tmar.field : 'any'
          pf.setCode(fieldName);
          
          // serialize and deserialize
          pf.setSelected(true);
          PivotFilterableBeanCollectionModule module = createPivotModule(factory, Employee.class)
          module.getPivot().setLinesRef([pf])
          String ser = module.serializeCriteria();
          
          Serializable[] criteria = QueryComponentSerializationUtil.deserializeFilter(ser);
          String back = criteria[1]
          
          tmar.serialization = (back == fieldName) ? "ok" : "KO: "+back;
          
          // translation
          tmar.translation = pf.getTranslation(controller)
        }
        else {
          
          // manage measure
          PivotField pf = factory.createComponentInstance(PivotField.class)
          pf.setComponentClass(Employee.class);
          String fieldName = PivotHelper.getFieldFromRestrictions(tmar.measure);
          pf.setCode(fieldName)
          
          PivotMeasure pm = factory.createComponentInstance(PivotMeasure.class)
          pf.addToMeasures(pm)
          
          pm.setupMeasure(tmar.measure)
          
          tmar.fieldName = fieldName;
          tmar.type = pm.getType()
          tmar.sum = pm.isSum()
          tmar.notNull = pm.isNotNull()
          tmar.distinct = pm.isDistinct()
          tmar.percentil = pm.getPercentileRank()
          tmar.percent = pm.getPercent()
          tmar.referencedAttribut = pm.getReferenceCode()
          tmar.referencedMeasure = pm.getReferenceTotal()
          
          if (pm.getReferenceCode()!=null) {
            
            PivotField pfr = factory.createComponentInstance(PivotField.class)
            pfr.setComponentClass(Employee.class)
            pfr.setCode(pm.getReferenceCode())

            pm.setReference(pfr)
          }
          
          // serialize and deserialize
          pf.setSelected(true)
          pm.setSelected(true)
          PivotFilterableBeanCollectionModule module = createPivotModule(factory, Employee.class)
          module.getPivot().setMeasuresRef([pf])
          String ser = module.serializeCriteria()
          
          module.deserializeCriteria(ser)
          String back = module.getPivot().getMeasuresRef()[0].getMeasures()[0].getMeasure(false)
          
          tmar.serialization = (back == tmar.measure) ? "ok" : "KO: " + back;
          
          // translation
          tmar.translation = pm.getToString()
        }
      }
      catch (Exception ex) {
        LoggerFactory.getLogger(getClass()).error("Test error", ex)
        throw ex;
      }
    }
  }

  private PivotFilterableBeanCollectionModule createPivotModule(IEntityFactory factory, Class componentClass) {
    PivotFilterableBeanCollectionModule module = new PivotFilterableBeanCollectionModule();
    module.setElementComponentDescriptor(factory.getComponentDescriptor(componentClass))
    module.setFilter(new QueryComponent(factory.getComponentDescriptor(componentClass), factory))
    module.setPivotRefiner(new DefaultPivotRefiner())
    module.setPivot(factory.createComponentInstance(PivotCriteria.class))
    return module
  }

}
