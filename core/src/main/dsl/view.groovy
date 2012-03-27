// Implement your views here using the SJS DSL.

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




