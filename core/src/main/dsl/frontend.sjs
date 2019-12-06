bean ('viewFactoryBase', parent:'abstractViewFactory',
  custom: [defaultActionMapRenderingOptions:'LABEL_ICON',
  defaultTabRenderingOptions:'LABEL'])

spec('remote') {
  bean ('remotePeerRegistryBase', class:'org.jspresso.framework.util.remote.registry.BasicRemotePeerRegistry', custom:[automationEnabled:true])
  bean ('remoteFrontController', class:'org.jspresso.hrsample.ext.frontend.remote.CustomRemoteController', parent:'abstractFrontController')
  bean ('remoteViewFactory', class:'org.jspresso.framework.ext.view.remote.EnhancedRemoteViewFactory', parent:'viewFactoryBase')
  bean('iconFactoryBase', parent:'abstractIconFactory') {
      bean('largeIconSize', class:'org.jspresso.framework.util.gui.Dimension', custom: [width: 32, height: 32])
      bean('mediumIconSize', class: 'org.jspresso.framework.util.gui.Dimension', custom: [width: 28, height: 28])
      bean('smallIconSize', class: 'org.jspresso.framework.util.gui.Dimension', custom: [width: 22, height: 22])
      bean('tinyIconSize', class: 'org.jspresso.framework.util.gui.Dimension', custom: [width: 16, height: 16])
  }
}

spec('swing') {
  bean ('swingFrontController', class:'org.jspresso.hrsample.ext.frontend.swing.CustomSwingController', parent:'abstractFrontController')
}


spec('remote-recording') {
  bean('remoteFrontController',
      class:'org.jspresso.framework.ext.application.testing.RecordingRemoteController',
      parent:'abstractFrontController',
      custom:[captureDirectory:'/tmp/commands'])
}

/*
 * controler
 */
controller ('hrsample-ext.name',
    icon:'icon.png',
    context:'hrsample-ext',
    language:'en',
    startup:'startupHrsampleExtAction',
    onModuleEnter:'manageAnyModuleEnterFrontAction',
    actionMap:'controllerActionMap',
    workspaces:[
      'statistics.workspace',
      'organization.workspace',
      'employees.workspace',
      'geography.workspace',
      'tools.workspace',
      'usage.workspace',
      'administration.workspace'])

action ('startupHrsampleExtAction',
    parent:'permalinkSelectionAction',
    custom:[defaultAction_ref:'startupHrsampleAction'])

action ('manageAnyModuleEnterFrontAction',
    class:'org.jspresso.framework.application.frontend.action.FrontendAction',
    wrapped:'usageAnyModuleEnterFrontAction',
    next:'loadPinnedQueryCriteriasFrontAction')


/*
 * Workspaces
 */
workspace('statistics.workspace', icon: 'tools.png') {

  pivotModule('employee.statistics.module',
          dimensions: ['salary(40, 80, 100)',
                       'age(30, 40)',
                       'gender',
                       'managedOu.ouId',
                       //'managedOu.manager.name',
                       //'company.name',
                       'company.departments'],
          measures: ['ssn@count',
                     'salary@sum',
                     'salary@percentile90',
                     'bonus.encryptedValue@sum',
                     'company.budget.encryptedValue@sum',
                     'salary@average/managedOu.ouId'],
          refiner: 'employeePivotRefiner',
          moduleView: 'employee.statistics.module.view',
          component: 'Employee')

}

workspace('administration.workspace', icon:'classpath:org/jspresso/framework/application/images/execute-48x48.png') {

  filterModule ('users.admin.module',
    component:'User')

  filterModule('roles.admin.module',
    component:'Role')

  beanModule ('userqueries.admin.module',
    ref:'userqueries.admin.module')

  beanModule ('pivot.admin.module',
    ref:'pivot.admin.module')

  nodeModule ('autodoc.admin.module',
    ref:'autodoc.admin.module')
}

workspace('tools.workspace', icon:'tools.png') {

  filterModule ('furniture.module',
    startup:'furnitureModuleInitFrontAction',
    moduleView:'Furniture.module.view',
    detailView:'Furniture.detail.view',
    component:'Furniture',
    includeDetail: true) {
  }

  filterModule ('Employee.test.module',
    component:'Employee',
    moduleView:'Employees.module.test.view')
}


/*
 * login
 */
external id:['loginActionMap', 'secondaryLoginActionMap']
border ('loginViewDescriptor', model:'CaptchaUsernamePasswordHandler',
    name:'login.name', description:'credentialMessage',
    actionMap:'loginActionMap',
    secondaryActionMap:'secondaryLoginActionMap'
    ) {

  center {

    border {
      west {
        form (model:'CaptchaUsernamePasswordHandler', columnCount:2, preferredWidth:600) {
          fields {
            propertyView parent:'username', width:2, preferredWidth:380
            propertyView parent:'password', width:2, preferredWidth:380
            propertyView name:'captchaChallenge', width:2, preferredWidth:380

            propertyView parent:'rememberMe', width:2

            propertyView parent:'language'
            enumerationPropertyView parent:'timeZoneId'
          }
        }
      }
      east {
        border (preferredWidth:160){
          north {
            form (borderType:'SIMPLE', labelsPosition:'ABOVE') {
              fields {
                propertyView name:'captchaImage', labelFont:';BOLD;', horizontalAlignment:'CENTER', action:'generateNewCaptchaAction', description:'captchaImage.description'
              }
            }
          }
          south {
            form (columnCount:1, borderType:'NONE', labelsPosition:'NONE') {
              fields {
                propertyView name:'register', readOnly:true, action:'registerFrontAction', horizontalAlignment:'RIGHT'
                propertyView name:'switchUI', readOnly:true, action:'switchToUIFrontAction',  horizontalAlignment:'RIGHT'
              }
            }
          }
        }
      }
    }
  }
}

propertyView ('language', model:'languagePropertyDescriptor')
propertyView ('rememberMe', model:'rememberMePropertyDescriptor')
propertyView ('username', model:'usernamePropertyDescriptor')
propertyView ('password', model:'passwordPropertyDescriptor')
enumerationPropertyView ('timeZoneId', model:'timeZoneIdPropertyDescriptor', forbiddenValues:['Antarctica/DumontDUrville', 'America/Bahia_Banderas', 'America/Port-au-Prince', 'Atlantic/South_Georgia'])

form('Registration.form', fields:['name', 'firstName']) {
  actionMap {
    actionList {
      action ref:'englishFrontAction'
      action ref:'frenchFrontAction'
    }
  }
}

action('registerFrontAction', class:'org.jspresso.hrsample.ext.frontend.RegisterFrontAction', parent:'editComponentAction',
  name:'register.action.name', description:'', icon:'tools.png',
  custom:[okAction_ref:'performRegistrationFrontAction',
    cancelAction_ref:'cancelDialogFrontAction',
    viewDescriptor_ref:'Registration.form'])

action('switchToUIFrontAction',
  description:'', icon:'tools.png',
  class:'org.jspresso.hrsample.ext.frontend.SwitchToUIFrontAction') {
  next parent:'displayUrlFrontAction', custom:[target:'_self']
}

action('performRegistrationFrontAction', parent:'staticInfoFrontAction', name:'doRegister',
  custom:[messageCode:'register'], booleanActionabilityGates: ['complete'], hiddenWhenDisabled: true)

action('changeRegistrationLanguageFrontAction',
    class:'org.jspresso.hrsample.ext.frontend.ChangeRegistrationLanguageFrontAction', next:'registerFrontAction')

action('englishFrontAction', parent:'changeRegistrationLanguageFrontAction',
    custom:['targetLanguage':'en'], icon:'uk.png')
action('frenchFrontAction', parent:'changeRegistrationLanguageFrontAction',
    custom:['targetLanguage':'fr'], icon:'fr.png')

action('generateNewCaptchaAction',
  class:'org.jspresso.hrsample.ext.frontend.GenerateNewCaptchaFrontAction')

/*
 * Furnitures
 */
action ('furnitureModuleInitFrontAction',
  class:'org.jspresso.framework.application.frontend.action.FrontendAction') {

  wrapped ref:'initModuleFilterAction'
  next class:'org.jspresso.hrsample.ext.frontend.FurnitureModuleInitFrontAction'
}

/*
 * Documentation
 */
action('displayDocumentationRootAction',
        parent: 'abstractDisplayDocumentationRootAction',
        custom: [autoDocWorkspaceName: 'administration.workspace'])
