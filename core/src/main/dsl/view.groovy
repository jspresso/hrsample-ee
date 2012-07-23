// Implement your views here using the SJS DSL.
external id:['restartModuleWithConfirmationFrontAction']

form('loginViewDescriptor', parent:'loginViewDescriptorBase', model:'CaptchaUsernamePasswordHandler', columnCount:2) {
  fields {
    propertyView name:'username', width:2
    propertyView name:'password', width:2
    propertyView name:'captchaImageUrl', width:2
    propertyView name:'captchaChallenge', width:2
    propertyView name:'rememberMe', width:2
    propertyView name:'register', readOnly:true, action:'registerFrontAction'
    propertyView name:'help', readOnly:true, action:'helpFrontAction'
  }
}

form('Registration.form', fields:['name', 'firstName']) {
  actionMap {
    actionList {
      action ref:'englishFrontAction'
      action ref:'frenchFrontAction'
    }
  }
}

action('helpFrontAction', parent:'staticInfoFrontAction', custom:[messageCode:'help'])

action('registerFrontAction', class:'org.jspresso.hrsample.ext.frontend.RegisterFrontAction', parent:'editComponentAction',
    custom:[okAction_ref:'performRegistrationFrontAction',
      cancelAction_ref:'cancelDialogFrontAction',
      viewDescriptor_ref:'Registration.form'])

action('performRegistrationFrontAction', parent:'staticInfoFrontAction', name:'doRegister',
    custom:[messageCode:'register'])

action('changeRegistrationLanguageFrontAction',
    class:'org.jspresso.hrsample.ext.frontend.ChangeRegistrationLanguageFrontAction', next:'registerFrontAction')

action('englishFrontAction', parent:'changeRegistrationLanguageFrontAction',
    custom:['targetLanguage':'en'], icon:'classpath:org/jspresso/contrib/images/i18n/en.png')
action('frenchFrontAction', parent:'changeRegistrationLanguageFrontAction',
    custom:['targetLanguage':'fr'], icon:'classpath:org/jspresso/contrib/images/i18n/fr.png')

tabs('Furniture.detail.view', actionMap:'beanModuleActionMap') {
  views {
    form (model:'Furniture')
    table(parent:'ITranslatable-translations.table')
  }
}
  
/** 
 * OVERRIDE JSPRESSO DEFAULT ACTION MAP
 */
actionMap ('beanModuleActionMap') {  
  actionList('SAVE', collapsable:true){
    action ref:'saveModuleObjectFrontAction'
    action ref:'reloadModuleObjectFrontAction'
  }
  actionList('FILE') {
    action ref:'removeModuleObjectFrontAction'
    action ref:'parentModuleConnectorSelectionFrontAction'
  } 
  actionList ('PERMALINK', collapsable:true) {
    action parent:'createPermalinkAndCopyToClipboardFrontAction', custom:[tinyURL:true]
    action parent:'createPermalinkAndMailToFrontAction', custom:[tinyURL:true]
  }
}

/**
 * OVERRIDE JSPRESSO DEFAULT ACTION MAP
 */
actionMap('beanCollectionModuleActionMap') {
  actionList('RELOAD') {
    action ref:'restartModuleWithConfirmationFrontAction'
  }
  actionList('SAVE', collapsable:true){
    action ref:'saveModuleObjectFrontAction'
    action ref:'reloadModuleObjectFrontAction'
  }
  actionList('FILE') {
    action ref:'queryModuleFilterAction'
  }
  actionList('PIN', renderingOptions:'ICON') {
    action ref:'pinQueryCriteriasFrontAction'
  }
  actionList('SERVICE') {
    action ref:'addAsChildModuleFrontAction'
  }
  actionList('ADD', collapsable:true) {
    action ref:'addToMasterFrontAction'
    action ref:'cloneEntityCollectionFrontAction'
  }
  actionList('REMOVE') {
    action ref:'removeFromModuleObjectsFrontAction'
  }
  actionList('EXPORT') {
    action ref:'exportFilterModuleResultToHtmlAction'
  }
  actionList ('PERMALINK', collapsable:true) {
    action parent:'createPermalinkAndCopyToClipboardFrontAction', custom:[tinyURL:true]
    action parent:'createPermalinkAndMailToFrontAction', custom:[tinyURL:true]
  }
}




