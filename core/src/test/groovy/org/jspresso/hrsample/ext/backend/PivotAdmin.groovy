

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
import org.jspresso.framework.ext.pivot.backend.IPivotRefiner
import org.jspresso.framework.ext.pivot.backend.PivotHelper
import org.jspresso.framework.ext.pivot.model.IStyle
import org.jspresso.framework.ext.pivot.model.PivotFilterableBeanCollectionModule
import org.jspresso.framework.model.entity.IEntityFactory
import org.junit.Test
import org.springframework.transaction.TransactionStatus
import org.springframework.transaction.support.TransactionCallback

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
      IEntityFactory factory = controller.getEntityFactory()

      // load styles sets
      createEntities(PivotStyleSet: tmar.table.pivotStyleSets)

      // create and load pivot setup
      createEntities(PivotSetup: tmar.table.pivotSetup)
      
      String moduleId = tmar.getTableValue("pivotSetup", 0, "pivotId")
      PivotFilterableBeanCollectionModule module = findModule("statistics.workspace", "employee.statistics.module")
      module.setPermId(moduleId)
      EmployeePivotRefiner refiner = module.getPivotRefiner()
      
      DetachedCriteria criteria = DetachedCriteria.forClass(PivotSetup.class)
      criteria.add(Restrictions.eq(PivotSetup.PIVOT_ID, moduleId))
      PivotSetup pivotSetup = controller.findFirstByCriteria(criteria, EMergeMode.MERGE_CLEAN_EAGER, PivotSetup.class)
     
      pivotSetup.setParentStyle(findParentStyle(tmar.globalParent))
      pivotSetup.setCustomStyle(tmar.globalCustom)
      
      // create pivot setup's field
      PivotSetupField field = null
      if (tmar.measure !=null) {
        field = factory.createEntityInstance(PivotSetupField.class);
        field.setPivotSetup(pivotSetup)
        field.setFieldId(tmar.measure)
        
        if (tmar.parent!=null) {
          DetachedCriteria c = DetachedCriteria.forClass(PivotStyleSet.class)
          c.add(Restrictions.eq(PivotStyleSet.NAME, tmar.parent))
          PivotStyleSet style = controller.findFirstByCriteria(c, EMergeMode.MERGE_CLEAN_EAGER, PivotStyleSet.class)
          
          field.setParentStyle(style)
        }
        field.setCustomStyle(tmar.custom)
      }
      // save admin entities
      controller.getTransactionTemplate().execute(new TransactionCallback() {
        @Override
        public Object doInTransaction(TransactionStatus status) {
          PivotSetup ps = controller.cloneInUnitOfWork(pivotSetup)
          controller.registerForUpdate(ps)
          
          if (field!=null) {
            PivotSetupField f = controller.cloneInUnitOfWork(field)
            controller.registerForUpdate(f)
          }
          
          controller.performPendingOperations();
          return null;
        }
      });

      // init cross items
      refiner.resetCache()
     module.resetPivotBuilder()
        
      // prepare assertions
      IStyle defaultStyle = refiner.getDefaultStyle();
      tmar.globalFullStyle = PivotHelper.stylesMapToString(pivotSetup.getFullStyleAsMap())
      if (tmar.measure !=null) {
        
        String fieldId = PivotHelper.getFieldFromMeasure(tmar.measure)
        IStyle style = refiner.getFieldStyle(fieldId, tmar.measure);
        if (style == null) 
          style = defaultStyle;
        String styleName = style.getStyleName();
        
        Map styles = refiner.getCrossItems().getStyleAsMapAttributes(styleName)
        tmar.crossItemsStyle = PivotHelper.stylesMapToString(styles)
        
        //tmar.customStyle = PivotHelper.stylesMapToString(field.getStyleAsMap())
        //tmar.fullStyle = module.getPivotRefiner().getFieldStyle(null, tmar.measure)
        // tmar.fullStyle = PivotHelper.stylesMapToString(field.getFullStyleAsMap())
      }
    }
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
