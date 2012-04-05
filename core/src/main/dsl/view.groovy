// Implement your views here using the SJS DSL.

form('loginViewDescriptor', parent:'loginViewDescriptorBase', verticallyScrollable:true, model:'loginModelDescriptor') {
  fields {
    propertyView name:'username'
    propertyView name:'password'
    propertyView name:'captchaImageUrl'
    propertyView name:'captchaChallenge'
    propertyView name:'rememberMe'
    propertyView name:'register', readOnly:true, action:'infoFrontAction'
  }
}

table ('Furniture.module.view', parent:'decoratedView') {
  actionMap (parents:['filterableBeanCollectionModuleActionMap']) {
    actionList {
      action ref:'exportFilterModuleResultToHtmlAction'
    }
  }
}

tabs('Furniture.detail.view', actionMap:'beanModuleActionMap') {
  views {
    form (model:'Furniture')
    table(parent:'ITranslatable-translations.table')
  }
}




