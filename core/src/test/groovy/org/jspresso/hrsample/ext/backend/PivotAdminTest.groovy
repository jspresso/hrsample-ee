package org.jspresso.hrsample.ext.backend

import org.hibernate.criterion.DetachedCriteria
import org.hibernate.criterion.Restrictions
import org.jspresso.contrib.backend.pivot.ExtendedPivotRefiner;
import org.jspresso.contrib.model.pivot.PivotSetup
import org.jspresso.contrib.model.pivot.PivotSetupField
import org.jspresso.contrib.model.pivot.PivotStyleSet
import org.jspresso.contrib.test.backend.tmar.TmarValidation
import org.jspresso.contrib.tmar.core.TmarDataSequence
import org.jspresso.framework.application.backend.BackendControllerHolder
import org.jspresso.framework.application.backend.persistence.hibernate.HibernateBackendController
import org.jspresso.framework.application.backend.session.EMergeMode
import org.jspresso.framework.application.model.Module
import org.jspresso.framework.application.model.Workspace
import org.jspresso.framework.ext.pivot.backend.PivotHelper
import org.jspresso.framework.ext.pivot.model.IStyle
import org.jspresso.framework.ext.pivot.model.PivotFilterableBeanCollectionModule
import org.jspresso.framework.model.entity.IEntity
import org.junit.After;
import org.junit.Test
import org.springframework.transaction.TransactionStatus
import org.springframework.transaction.support.TransactionCallbackWithoutResult

class PivotAdminTest extends TmarBackendStartup {

  /**
   * Test
   */
  @Test
  void test() {
    eachIteration('test') { tmar ->

      int l = tmar.getCurrentIterationNumber()
      
      // load styles
      HibernateBackendController controller = BackendControllerHolder.getCurrentBackendController()
      controller.getApplicationSession().setLocale(Locale.ENGLISH);
  
      if (l == 1)
        loadStyles(tmar)

      // create and load pivot setup
      PivotSetup pivotSetup = createPivotSetup(tmar)

      // init pivot module
      PivotFilterableBeanCollectionModule module = initPivotModule(pivotSetup)
      
      // prepare assertions
      tmar.globalFullStyle = PivotHelper.stylesMapToString(pivotSetup.getFullStyleAsMap())
      if (tmar.globalFullStyle == null) {
        tmar.globalFullStyle = PivotHelper.stylesMapToString(module.getPivotRefiner().getDefaultStyles().toArray(new IStyle[0]));
      }

      if (tmar.measure !=null) {

        def measure2 = PivotHelper.formatField4CrossItems(tmar.measure)
        Map<String, String> styles = module.getPivotRefiner().getCrossItems().getMeasureStyleAsMapAttributes(measure2)
        if (styles!=null)
          tmar.crossItemsStyle = PivotHelper.stylesMapToString(styles)
      }
    }
  }

  
  /**
   * Init pivot module
   * @param pivotSetup
   * @return
   */
  protected PivotFilterableBeanCollectionModule initPivotModule(PivotSetup pivotSetup) {
    PivotFilterableBeanCollectionModule module = findModule("statistics.workspace", "employee.statistics.module")
    
    ((ExtendedPivotRefiner)module.getPivotRefiner()).getPivotService().reloadApplicationStyles()
    
    module.setPermId(pivotSetup.getPivotId())
    module.resetPivotBuilder()
    module.getPivotRefiner().resetCache()
    ((ExtendedPivotRefiner)module.getPivotRefiner()).getPivotService().reloadApplicationStyles()
    return module
  }

  
  /**
   * Create pivot setup
   * @param tmar
   * @return
   */
  protected PivotSetup createPivotSetup(tmar) {
    return createPivotSetup(tmar, true, true, true)
  }
  @TmarValidation('test')
  protected PivotSetup createPivotSetup(tmar, boolean useGlobalParent, boolean useGlobalCustom, boolean useCustom) {
    
    // create entity
    createEntities(PivotSetup: tmar.table.pivotSetup)

    String moduleId = tmar.getTableValue("pivotSetup", 0, "pivotId")
    DetachedCriteria criteria = DetachedCriteria.forClass(PivotSetup.class)
    criteria.add(Restrictions.eq(PivotSetup.PIVOT_ID, moduleId))
    
    HibernateBackendController controller = BackendControllerHolder.getCurrentBackendController()
    PivotSetup pivotSetup = controller.findFirstByCriteria(criteria, EMergeMode.MERGE_CLEAN_EAGER, PivotSetup.class)
    
    // setup pivot setup
    if (useGlobalParent)
      pivotSetup.setAscendantStylesString(tmar.globalParent)
    if (useGlobalCustom)
      pivotSetup.setCustomStyle(tmar.globalCustom)
    
    // create pivot setup's field
    PivotSetupField field = null
    if (tmar.measure !=null) {
      field = createEntityInstance(PivotSetupField.class);
      field.setPivotSetup(pivotSetup)
      field.setFieldId(tmar.measure)
      field.setAscendantStylesString(tmar.parents)
      
      if (useCustom)
        field.setCustomStyle(tmar.custom)
    }
    
    // save admin entities
    saveEntity(controller, pivotSetup)
    
    return pivotSetup
  }
  
  /**
   * clean pivot admin
   */
  protected void cleanPivotAdmin() {
    HibernateBackendController controller = BackendControllerHolder.getCurrentBackendController()
    
    // Delete pivot setup fields
    DetachedCriteria criteria = DetachedCriteria.forClass(PivotSetupField.class)
    deleteEntity( controller.findByCriteria(criteria, EMergeMode.MERGE_CLEAN_EAGER, PivotSetupField.class).toArray(new IEntity[0]))
    
    // Delete pivot setups
    criteria = DetachedCriteria.forClass(PivotSetup.class)
    deleteEntity( controller.findByCriteria(criteria, EMergeMode.MERGE_CLEAN_EAGER, PivotSetup.class).toArray(new IEntity[0]))
    
    // Delete all styles (from previous tests)
    criteria = DetachedCriteria.forClass(PivotStyleSet.class)
    List<PivotStyleSet> all = controller.findByCriteria(criteria, EMergeMode.MERGE_CLEAN_EAGER, PivotStyleSet.class)
    List<PivotStyleSet> ordered = PivotHelper.getSortedStyles(all)
    deleteEntity(ordered.toArray(new IEntity[0]))

  }

  /**
   * Load styles
   * @param tmar
   */
  protected Map loadStyles(TmarDataSequence  tmar) {
    Map styleSets = new HashMap<String, IEntity>();
  
    HibernateBackendController controller = BackendControllerHolder.getCurrentBackendController()
       
    // load styles sets
    for (int i=0; i<tmar.getTableSize('pivotStyleSets'); i++) {
      Map line = tmar.getTableLine('pivotStyleSets', i)
      PivotStyleSet style = createEntityInstance(PivotStyleSet.class)
      style.setName(line.get('name'))
      style.setCustomStyle(line.get('customStyle'))

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
    
    return styleSets
  }
  
  
  /**
   * Load styles
   * @param tmar
   */
  protected void loadDynamicStyles(TmarDataSequence  tmar, Map styleSets) {
    int l = tmar.getCurrentIterationNumber()
    if (l == 1) {

      HibernateBackendController controller = BackendControllerHolder.getCurrentBackendController()
      controller.getApplicationSession().setLocale(Locale.ENGLISH);
      
      // load styles sets
      for (int i=0; i<tmar.getTableSize('pivotDynamicStyleSets'); i++) {
        Map line = tmar.getTableLine('pivotDynamicStyleSets', i)
        PivotStyleSet style = createEntityInstance(PivotStyleSet.class)
        style.setDynamic(true)
        style.setName(line.get('name'))
        style.setCustomStyle(line.get('customStyle'))

        // setup styles ancestors
        def parents = line.get('parents')
        if (parents!=null)
          style.setAscendantStylesString(parents)
          
        saveEntity(controller, style);
      }

    }
  }
  

  
  /**
   * saveEntity
   */
  protected saveEntity(HibernateBackendController controller, IEntity entity) {
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
   * saveEntity
   */
  protected deleteEntity(HibernateBackendController controller, IEntity entity) {
    controller.registerForDeletion(entity)
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
  protected PivotStyleSet findParentStyle(String styleId) {
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
  protected PivotFilterableBeanCollectionModule findModule(String workspaceId, String moduleId) {
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
  
  /**
   * getApplicationContextKey
   */
  @Override
  protected String getApplicationContextKey() {
    return "hrsample-ext-frontend-test-context";
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
