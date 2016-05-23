// Implement your views here using the SJS DSL.
external id:['restartModuleWithConfirmationFrontAction',
  'queryPivotModuleAndReloadFilterFrontAction',
  'queryPivotModuleFilterFrontAction']

/*
 * Furnitures
 */
tabs('Furniture.detail.view', actionMap:'beanModuleActionMap') {
  views {
    border {
      north {
        form (model:'Furniture')
      }
      center {
        table (model:'Furniture-previous', borderType:'TITLED') {
          actionMap {
            actionList('ALL') {
              action parent:'chooseEntityFrontAction', custom:[selectionMode:'MULTIPLE_INTERVAL_CUMULATIVE_SELECTION']
              action ref:'unlinkComponentFrontAction'
            }
          }
        }
      }
    }


    table(parent:'ITranslatable-translations.table')
  }
}

/*
 * OVERRIDE JSPRESSO DEFAULT ACTION MAPS
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
    action parent:'createPermalinkAndCopyToClipboardFrontAction', custom:[tinyURL:false]
    action parent:'createPermalinkAndMailToFrontAction', custom:[tinyURL:false]
  }
}

/**
 * OVERRIDE JSPRESSO DEFAULT ACTION MAP
 */
actionMap('beanCollectionModuleActionMap') {
  actionList('SAVE', collapsable:true){
    action ref:'saveModuleObjectFrontAction'
    action ref:'reloadModuleObjectFrontAction'
    action ref:'restartModuleWithConfirmationFrontAction'
  }
  actionList('FILE') {
    action ref:'queryModuleFilterAction'
  }
//  actionList('PIN', renderingOptions:'ICON') {
//    action ref:'pinQueryCriteriasFrontAction'
//  }
  actionList('MY_REQUEST') {
    action ref:'chooseQueryCriteriasFrontAction'
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
    action ref:'importBoxAction'
  }
  actionList('TMAR', collapsable:true) {
    action ref:'exportAllToTmarRecursivelyAction'
    action ref:'exportTableToTmarAction'
  }
  actionList ('PERMALINK', collapsable:true) {
    action parent:'createPermalinkAndCopyToClipboardFrontAction', custom:[tinyURL:false]
    action parent:'createPermalinkAndMailToFrontAction', custom:[tinyURL:false]
  }
}

border ('Employee.test.view', cascadingModels:true) {
  center {
    table {
       actionMap {
        actionList('SAVE', collapsable:true) {
          action ref:'saveModuleObjectFrontAction'
          action ref:'reloadModuleObjectFrontAction'
        }
        actionList('FILE') {
          action parent:'queryModuleFilterAction', permId:'Employee.test.view--actionMap--queryModuleFilterAction'
          action ref:'pinQueryCriteriasFrontAction'
        }
        actionList('SERVICE', collapsable:true) {
          action parent:'navigateToModuleFrontAction', permId:'Employee.test.view--table--actionMap'
        }
        actionList('EXPORT') {
          action parent:'exportFilterModuleResultToHtmlAction',
                    custom:[hideMyExportPopup:false, hideMoreColumnsPopup:false, moreColumnsOneToManyDepth:4]
          action parent:'importEmployeeBoxAction',
                    custom:[mergeFields:['name', 'firstName'], extraColumns:['zip'], additionalFields:['teams']]
        }
        actionList('TMAR', collapsable:true) {
          action ref:'exportAllToTmarRecursivelyAction'
          action ref:'exportTableToTmarAction'
        }
      }
    
      columns {
        propertyView name:'company', action:'navigateToModuleFrontAction', readOnly:true, permId:'Employee.test.view--table--company'
        propertyView name:'company.workforce', readOnly:true
        propertyView name:'name', action:'navigateToModuleFrontAction', readOnly:true, permId:'Employee.test.view--table--name'
        propertyView name:'firstName', readOnly:true
        propertyView name:'gender', readOnly:true
        propertyView name:'birthDate', readOnly:true
        propertyView name:'contact.address', readOnly:true
        propertyView name:'contact.city', readOnly:true
        propertyView name:'contact.phone', readOnly:true
        propertyView name:'contact.phone', readOnly:true
      }
    }
  }
  east {
    border {
      north {
        form {
          actionMap {
            actionList('SERVICE') {
              action parent:'navigateToModuleFrontAction', permId:'Employee.test.view--east--actionMap'
            }
          }
          fields {
            propertyView name:'firstName'
            propertyView name:'birthDate'
            
            propertyView name:'company', action:'navigateToModuleFrontAction', readOnly:true, permId:'Employee.test.view--east--company'
            propertyView name:'company.workforce', readOnly:true, horizontalAlignment:'LEFT'
            propertyView name:'name', action:'navigateToModuleFrontAction', readOnly:true, permId:'Employee.test.view--east--name'
          
          }
        }
      }
      center {
        table (model:'Employee-users') {
          actionMap {
            actionList('SERVICE', collapsable:true) {
              action parent:'navigateToModuleFrontAction', permId:'Employee.test.view--east-table--actionMap'
              action parent:'navigateToModuleFrontAction', permId:'Employee.test.view--east-table--actionMap.notCollectionBased'
            }
          }
          columns {
            propertyView name:'login', action:'navigateToModuleFrontAction', readOnly:true, permId:'Employee.test.view--east-table--login'
          }
        }
      }
    }
  }
}

action ('importEmployeeBoxAction',
  parent:'importBoxAction',
  custom:[okAction_ref:'importEmployeeBoxOkAction'])

action ('importEmployeeBoxOkAction',
  parent:'importBoxOkAction',
  next:'importEmployeeAction')

action ('importEmployeeAction',
  parent:'importEntitiesAction',
  custom:[fileOpenCallback_ref:'importEmployeeFactoryBean'])

bean ('importEmployeeFactoryBean',
  parent:'importEntitiesFactoryBean',
  class:'org.jspresso.hrsample.ext.frontend.ImportEmployeeFactoryBean')


actionMap ('eventsTableActionMap', 
  parents:['masterDetailActionMap']) {
 
  actionList ('EXPORT') {
    action ref:'exportTableToHtmlAction'
  } 
}


/** 
 * navigate to module
 */
action ('navigateToModuleFrontAction',
  parent:'navigateToModuleFrontActionBase',
  custom:[models2Module:['org.jspresso.hrsample.model.Employee':'employees.workspace/employees.module',
                         'org.jspresso.hrsample.model.Employee/name':'employees.workspace/employees.module',
                     
                         'org.jspresso.hrsample.model.Company':'organization.workspace/companies.module',
                         
                         'org.jspresso.hrsample.model.User/login':'administration.workspace/users.admin.module']])

/**
 * target factory
 */
bean ('targetActionsConfigurator',
  parent:'targetActionsConfiguratorBase',
  custom:[targets_ref:['navigateToModuleFrontAction']]);

//action ('openEmployeeFrontAction',
//  parent:'addAsChildModuleFrontAction',
//  custom:[parentWorkspaceName:'employees.workspace', parentModuleName:'employees.module'])

//action ('openEmployeeCompanyFrontAction',
//  parent:'addAsChildModuleFrontAction',
//  custom:[parentWorkspaceName:'organization.workspace', parentModuleName:'companies.module', referencePath:'company'])

//action ('openEmployeeCompanyWorkforceFrontAction',
//  parent:'addAsChildModuleFrontAction',
//  custom:[parentWorkspaceName:'employees.workspace', parentModuleName:'employees.module',
  //      referencePath:'company.employees'])