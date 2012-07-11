// Implement your views here using the SJS DSL.

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

table ('Furniture.module.view', parent:'filterableBeanCollectionModuleView') {
  actionMap (parents:['filterableBeanCollectionModuleActionMap']) {
    actionList { 
      action ref:'exportFilterModuleResultToHtmlAction' 
    }
    actionList {
      action ref:'createPermaLinkFrontAction'
    }
  }
}

tabs('Furniture.detail.view') {
  actionMap (parents:['beanModuleActionMap']) {
    actionList {
      action ref:'createPermaLinkFrontAction'
    }
  }
  views {
    form (model:'Furniture')
    table(parent:'ITranslatable-translations.table')
  }
}




