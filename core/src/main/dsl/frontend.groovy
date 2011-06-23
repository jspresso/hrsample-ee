// Implement your application frontend here using the SJS DSL.

/*
 * The workspaces and modules
 */

// Describe your workspaces and modules here.

bean 'viewFactoryBase', parent:'abstractViewFactory',
    custom: [
      defaultActionMapRenderingOptions:'LABEL_ICON'
    ]

controller 'hrsample-ext.name',
    icon:'icon.png',
    context:'hrsample-ext',
    language:'en',
    startup:'startupHrsampleAction',
    workspaces:[
      'organization.workspace',
      'employees.workspace',
      'masterdata.workspace'
    ]

spec('remote') {
  bean ('remotePeerRegistryBase', class:'org.jspresso.framework.util.remote.registry.BasicRemotePeerRegistry', custom:[automationEnabled:true])
}

spec('remote-recording') {
  bean('remoteFrontController',
      class:'org.jspresso.framework.application.testing.RecordingRemoteController',
      parent:'abstractFrontController', custom:[
        captureDirectory:'/tmp/commands'
      ])
}
