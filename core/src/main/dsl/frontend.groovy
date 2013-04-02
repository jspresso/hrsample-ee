// Implement your application frontend here using the SJS DSL.

/*
 * The workspaces and modules
 */

// Describe your workspaces and modules here.

bean 'viewFactoryBase', parent:'abstractViewFactory',
    custom: [
      defaultActionMapRenderingOptions:'LABEL_ICON'
    ]
    
workspace('tools.workspace', icon:'tools.png') { 
  filterModule ('furniture.module', 
    detailView:'Furniture.detail.view',
    component:'Furniture') {
  }
}
 
controller 'hrsample-ext.name',
    icon:'icon.png',
    context:'hrsample-ext',
    language:'en',
    startup:'startupHrsampleExtAction',
    onModuleEnter:'manageAnyModuleEnterFrontAction',
    actionMap:'controllerActionMap',
    workspaces:[
      'organization.workspace',
      'employees.workspace',
      'masterdata.workspace', 
      'tools.workspace',
      'usage.workspace'
    ]
    
action ('startupHrsampleExtAction', 
    parent:'permalinkSelectionAction',
    custom:[defaultAction_ref:'startupHrsampleAction']
)    

action ('manageAnyModuleEnterFrontAction',
  class:'org.jspresso.framework.application.frontend.action.FrontendAction',
  wrapped:'anyModuleEnterFrontAction', 
  next:'loadPinnedQueryCriteriasFrontAction')   

spec('remote') {
  bean ('remotePeerRegistryBase', class:'org.jspresso.framework.util.remote.registry.BasicRemotePeerRegistry', custom:[automationEnabled:true])
  bean ('remoteFrontController', class:'org.jspresso.hrsample.ext.frontend.remote.CustomRemoteController', parent:'abstractFrontController')
  bean ('remoteViewFactory', class:'org.jspresso.framework.view.remote.EnhancedRemoteViewFactory', parent:'viewFactoryBase')
}

spec('swing') {
  bean ('swingFrontController', class:'org.jspresso.hrsample.ext.frontend.swing.CustomSwingController', parent:'abstractFrontController')
}


spec('remote-recording') {
  bean('remoteFrontController',
      class:'org.jspresso.framework.application.testing.RecordingRemoteController',
      parent:'abstractFrontController', custom:[
        captureDirectory:'/tmp/commands'
      ])
}
