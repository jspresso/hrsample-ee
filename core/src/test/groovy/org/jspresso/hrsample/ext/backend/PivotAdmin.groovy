

package org.jspresso.hrsample.ext.backend

import org.hibernate.criterion.DetachedCriteria
import org.hibernate.criterion.Restrictions
import org.jspresso.contrib.model.pivot.PivotSetup
import org.jspresso.contrib.model.pivot.PivotSetupField
import org.jspresso.contrib.model.pivot.PivotStyleSet
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
import org.springframework.transaction.support.TransactionCallback
import org.springframework.transaction.support.TransactionCallbackWithoutResult

class PivotAdmin extends TmarBackendStartup {

  /**
   * Test
   */
  @Test
  void test() {
    eachIteration('test') { tmar ->

      int l = tmar.getCurrentIterationNumber()
      
      HibernateBackendController controller = BackendControllerHolder.getCurrentBackendController()
      controller.getApplicationSession().setLocale(Locale.ENGLISH);
      
      Map styleSets = new HashMap<String, IEntity>();
      if (l == 1) {
        // load styles sets
        for (int i=0; i<tmar.getTableSize('pivotStyleSets'); i++) {
          Map line = tmar.getTableLine('pivotStyleSets', i)
          PivotStyleSet style = createEntityInstance(PivotStyleSet.class)
          style.setName(line.get('name'))
          style.setCustomStyle(line.get('customStyle'))
          
          saveEntity(controller, style);
          styleSets.put(style.getName(), style)          
        }
        
        // setup styles ancestors
        for (int i=0; i<tmar.getTableSize('pivotStyleSets'); i++) {
          Map line = tmar.getTableLine('pivotStyleSets', i)
          PivotStyleSet style = styleSets.get(line.get('name'))
          def parents = line.get('parents')
          if (parents!=null)
            style.setAscendantStylesString(parents)
            
          saveEntity(controller, style);
        }
        
      }
      
      // create and load pivot setup
      createEntities(PivotSetup: tmar.table.pivotSetup)
      
      String moduleId = tmar.getTableValue("pivotSetup", 0, "pivotId")
      PivotFilterableBeanCollectionModule module = findModule("statistics.workspace", "employee.statistics.module")
      module.setPermId(moduleId)
      EmployeePivotRefiner refiner = module.getPivotRefiner()
      
      DetachedCriteria criteria = DetachedCriteria.forClass(PivotSetup.class)
      criteria.add(Restrictions.eq(PivotSetup.PIVOT_ID, moduleId))
      PivotSetup pivotSetup = controller.findFirstByCriteria(criteria, EMergeMode.MERGE_CLEAN_EAGER, PivotSetup.class)
      pivotSetup.setAscendantStylesString(tmar.globalParent)
      pivotSetup.setCustomStyle(tmar.globalCustom)
      
      // create pivot setup's field
      PivotSetupField field = null
      if (tmar.measure !=null) {
        field = createEntityInstance(PivotSetupField.class);
        field.setPivotSetup(pivotSetup)
        field.setFieldId(tmar.measure)
        field.setAscendantStylesString(tmar.parents)
        field.setCustomStyle(tmar.custom)
        
        saveEntity(controller, field)
      }
      
      // save admin entities
      saveEntity(controller, pivotSetup)

      // init cross items
      module.resetPivotBuilder()     
      refiner.resetCache()
      
      int i = refiner.getRefinerFields().size();
        
      // prepare assertions
      List<IStyle> defaultStyles = refiner.getDefaultStyles();
      tmar.globalFullStyle = PivotHelper.stylesMapToString(pivotSetup.getFullStyleAsMap())
      if (tmar.measure !=null) {
        
        String measure = tmar.measure
        String fieldId = PivotHelper.getFieldFromMeasure(tmar.measure)
        List<IStyle> fieldStyles = refiner.getFieldStyles(fieldId, tmar.measure);
       
        Map<String, String> styles = refiner.getCrossItems().getMeasureStyleAsMapAttributes(measure)
        tmar.crossItemsStyle = PivotHelper.stylesMapToString(styles)
      }
    }
  }

  private saveEntity(HibernateBackendController controller, IEntity entity) {
    controller.registerForUpdate(entity)
    controller.getTransactionTemplate().execute(
      new TransactionCallbackWithoutResult() {

        @Override
        protected void doInTransactionWithoutResult(TransactionStatus status) {
          try {
            controller.performPendingOperations();
          } catch (RuntimeException ex) {
            controller.clearPendingOperations();
            throw ex;
          }
        }
      })
  }

  /**
   * findParentStyle
   */
  private PivotStyleSet findParentStyle(String styleId) {
    HibernateBackendController controller = BackendControllerHolder.getCurrentBackendController()
    DetachedCriteria criteria = DetachedCriteria.forClass(PivotStyleSet.class)
    criteria.add(Restrictions.eq(PivotStyleSet.NAME, styleId))
    PivotStyleSet set = controller.findFirstByCriteria(criteria, EMergeMode.MERGE_CLEAN_EAGER, PivotStyleSet.class)
    return set;
  }

  /**
   * Find bean
   * @param workspaceId
   * @param moduleId
   * @return
   */
  private PivotFilterableBeanCollectionModule findModule(String workspaceId, String moduleId) {
    PivotFilterableBeanCollectionModule module = null;
    Workspace workspace = (Workspace) getApplicationContext().getBean(workspaceId);
    List<Module> modules = workspace.getModules(true);
    if (modules!=null) {
      for (Module m : modules) {
        if (moduleId.equals(m.getName())) {
          module = (PivotFilterableBeanCollectionModule) m;
          break;
        }
      }
    }
    return module;
  }
  
  @Override
  protected String getApplicationContextKey() {
    return "hrsample-ext-frontend-test-context";
  }
}
