bean ('remoteFrontController', class:'org.jspresso.hrsample.ext.frontend.remote.mobile.CustomRemoteController',
    parent:'abstractMobileFrontController')

bean ('remoteViewFactory', class:'org.jspresso.framework.ext.view.remote.mobile.MobileEnhancedRemoteViewFactory',
    parent:'mobileViewFactoryBase')

/*
 * Controller
 */
controller ('hrsample-ext.name',
  description: 'hrsample.description',
  icon:'icon.png',
  context:'hrsample-ext',
  language:'en',
  startup:'applicationStartupFrontAction',
  onModuleEnter:'manageAnyModuleEnterFrontAction',
  actionMap:'controllerActionMap',
  workspaces:[
    'organization.workspace',
    'employees.workspace',
    'masterdata.workspace',
    'parameters.mobile.workspace'  
  ])

/*
 * Login
 */
bean ('loginViewDescriptor', parent:'loginViewDescriptorBase',
  custom:[modelDescriptor_ref:'CaptchaUsernamePasswordHandler',
          permId:'loginViewDescriptor'])

external id:['basicLoginViewDescriptorBase']

action ('applicationStartupFrontAction',  
  class:'org.jspresso.hrsample.ext.frontend.ApplicationStartupFrontAction')

/*
mobileCompositePage('basicLoginViewDescriptor',
  name:'login.name', description:'credentialMessage') {
  
  sections {
    
    mobileForm (parent:'basicLoginViewDescriptorBase',
      model:'CaptchaUsernamePasswordHandler') {
    
      fields {
        propertyView parent:'username'
        propertyView parent:'password'
        propertyView parent:'rememberMe'
      }
    }
      
    mobileForm (model:'CaptchaUsernamePasswordHandler', labelsPosition:'NONE') {
      fields {
        propertyView name:'captchaImageUrl'
        propertyView name:'captchaChallenge'
      }
    }
  }
}
*/

mobileForm('basicLoginViewDescriptor', 
  parent:'basicLoginViewDescriptorBase',
  model:'CaptchaUsernamePasswordHandler',
  name:'login.name', description:'credentialMessage') {
  
  fields {
    propertyView parent:'username'
    propertyView parent:'password'
    
    propertyView name:'captchaImage', action:'generateNewCaptchaAction'
    propertyView name:'captchaChallenge'

    propertyView parent:'rememberMe'
  }
}
  
actionMap('secondaryLoginActionMap', 
  parents:['secondaryLoginActionMapBase']) {
  
  actionList {
    action ref:'registerFrontAction' 
  }
}
  
mobileForm('Registration.form', fields:['name', 'firstName']) {
  actionMap {
    actionList {
      action parent:'englishFrontAction', iconHeight:32
      action parent:'frenchFrontAction', iconHeight:32
    }
  }
}
