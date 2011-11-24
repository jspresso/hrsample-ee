// Implement your views here using the SJS DSL.
table ('Furniture.view', parent:'decoratedView') {
  actionMap (parents:['filterableBeanCollectionModuleActionMap']) {
    actionList {
      action ref:'exportFilterModuleResultToHtmlAction'
    }
  }
}


