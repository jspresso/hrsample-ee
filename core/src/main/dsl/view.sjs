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
        form (model:'Furniture') {
          fields {
            propertyView name: 'name', actionMap: 'displayPropertyDocumentationActionMap'
            propertyView name: 'price', background: 'priceBackground', description: 'priceTooltip', actionMap: 'displayPropertyDocumentationActionMap'
            propertyView name: 'discount', actionMap: 'displayPropertyDocumentationActionMap'
            propertyView name: 'rawLabel'
            propertyView name: 'nlsLabel'
            propertyView name: 'nlsOrRawLabel'
          }
        }
      }
      center {

        table(model: 'Furniture-previous', permId: 'previousFurnitures.table') {
          actionMap {
            actionList('ALL', renderingOptions:'ICON') {
              action parent:'chooseEntityFrontAction', custom:[selectionMode:'MULTIPLE_INTERVAL_CUMULATIVE_SELECTION']
              action ref:'unlinkComponentFrontAction'
            }
          }
        }

      }
    }

    // translation
    table(parent:'ITranslatable-translations.table')

    // tracking
    border (parent:'Tracking.view', model:'Furniture')

  }
}


table('Furniture.module.view',
        parent: 'filterableBeanCollectionModuleView') {
  actionMap(parents: ['filterableBeanCollectionModuleActionMap']) {
    actionList(renderingOptions: 'LABEL_ICON') {
      action ref: 'addModificationTrackerSubscriptionAction'
    }
  }
}

// OVERRIDE HRSample property views
propertyView('Employee-fullname.property', actionMap:'navigateToModuleActionMap')
propertyView('OrganizationalUnit-manager.property', actionMap:'navigateToModuleActionMap')

// OVERRIDE default tracking filter vies
form ('Tracking.filter.view', borderType:'NONE', columnCount:3, model:'IHRModificationTracker') {
  fields {
    referencePropertyView name:'trackingFilterUser', action:'refreshTrackingEventsFrontAction'
    propertyView name:'trackingFilterFrom', action:'refreshTrackingEventsFrontAction'
    propertyView name:'trackingFilterTo', action:'refreshTrackingEventsFrontAction'
  }
}

// OVERRIDE JSPRESSO DEFAULT ACTION MAPS
actionMap ('beanModuleActionMap') {
  actionList('SAVE', renderingOptions:'ICON'){
    action ref:'saveModuleObjectFrontAction'
    action ref:'reloadModuleObjectFrontAction'
    action ref:'removeModuleObjectFrontAction'
  }
  actionList('FILE') {
    action ref:'parentModuleConnectorSelectionFrontAction'
  }
  actionList ('PERMALINK', collapsable:true) {
    action parent:'createPermalinkAndCopyToClipboardFrontAction', custom:[tinyURL:false]
    action parent:'createPermalinkAndMailToFrontAction', custom:[tinyURL:false]
  }
  actionList ('DOC') {
    action ref: 'displayModuleDocumentationAction'
    action ref: 'displayModuleDocumentationNoneAction'
  }
}

/**
 * OVERRIDE JSPRESSO DEFAULT ACTION MAP AND ACTIONS
 */
actionMap('beanCollectionModuleActionMap') {
  actionList('FILE', renderingOptions:'ICON'){
    action ref:'queryModuleFilterAction'
    action ref:'addAsChildModuleFrontAction'
    action ref:'saveModuleObjectFrontAction'
    action ref:'reloadModuleObjectFrontAction'
  }
//  actionList('PIN', renderingOptions:'ICON') {
//    action ref:'pinQueryCriteriasFrontAction'
//  }
  actionList('ADD_REMOVE', renderingOptions:'ICON') {
    action ref:'addToMasterFrontAction'
    action ref:'cloneEntityCollectionFrontAction'
    action ref:'removeFromModuleObjectsFrontAction'
  }
  actionList('CRITERIA') {
    action ref:'chooseQueryCriteriasFrontAction'
    action ref:'restartModuleWithConfirmationFrontAction'
  }
  actionList('EXPORT', collapsable:true) {
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
  actionList ('DOC') {
    action ref: 'displayModuleDocumentationAction'
    action ref: 'displayModuleDocumentationNoneAction'
  }
}

actionMap('masterDetailActionMap') {
  actionList('ACTION', renderingOptions:'ICON') {
    action(ref:'addToMasterFrontAction')
    action(ref:'cloneEntityCollectionFrontAction')
    action(ref:'removeEntityCollectionFromMasterFrontAction')
  }
  actionList('EDIT', collapsable:true) {
    action(ref:'copyCollectionFrontAction')
    action(ref:'cutCollectionFrontAction')
    action(ref:'pasteCollectionFrontAction')
  }
}

border ('Employees.module.test.view', cascadingModels:true, borderType:'TITLED', name:'employees.module') {
  center {
    table(permId: 'employee.test.table', parent: 'filterableBeanCollectionModuleView', validationModel: 'Employee', borderType: 'NONE') {
       actionMap (permId: 'Employees.module.test.view--actionMap'){
        actionList('FILE', renderingOptions:'ICON'){
          action ref:'queryModuleFilterAction'
          //action ref:'addAsChildModuleFrontAction'
          action parent:'navigateToModuleFrontAction', permId:'Employees.module.test.view--table--actionMap',
                 icon:'classpath:/org/jspresso/framework/application/images/view-48x48.png'
          action parent:'saveModuleObjectFrontAction', permId: 'Employees.module.test.view--saveModuleObjectFrontAction'
          action ref:'reloadModuleObjectFrontAction'
        }
        actionList('CRITERIA') {
          action ref:'pinQueryCriteriasFrontAction'
        }
        actionList('EXPORT') {
          action parent:'exportFilterModuleResultToHtmlAction',
                    custom:[hideMyExportPopup:false, hideMoreColumnsPopup:false, moreColumnsOneToManyDepth:4]
          action ref: 'importEmployeesBoxAction'
        }
        actionList('TMAR', collapsable:true) {
          action ref:'exportAllToTmarRecursivelyAction'
          action ref:'exportTableToTmarAction'
        }
      }

      columns {
        propertyView name:'company', action:'navigateToModuleFrontAction', readOnly:true, permId:'Employees.module.test.view--table--company'
        propertyView name:'company.workforce', readOnly:true
        propertyView name:'name', action:'navigateToModuleFrontAction', readOnly:true, permId:'Employees.module.test.view--table--name'
        propertyView name:'firstName', readOnly:true
        propertyView name:'gender', readOnly:true
        propertyView name:'birthDate', readOnly:true
        propertyView name:'contact.address', readOnly:true
        propertyView name:'contact.city', readOnly:true
        propertyView name:'contact.phone', readOnly:true

        // FOR UNIT TEST ONLY : @See JspressoNavigationToModuleTest
        propertyView name:'name', actionMap:'navigateToModuleActionMap', permId:'Employees.module.test.view--table--name.actionMap', grantedRoles:['test']
        propertyView name:'company', actionMap:'navigateToModuleActionMap', permId:'Employees.module.test.view--table--company.actionMap', grantedRoles:['test']

      }
    }
  }
  east {

    // FOR UNIT TEST ONLY : @See JspressoNavigationToModuleTest
    border (grantedRoles:['test'], validationModel: 'Employee') {
      north {
        form (columnCount:2) {
          actionMap {
            actionList('SERVICE') {
              action parent:'navigateToModuleFrontAction', permId:'Employees.module.test.view--east--actionMap'
            }
          }
          fields {
            propertyView name:'firstName', width:2
            propertyView name:'birthDate', width:2

            propertyView name:'company', action:'navigateToModuleFrontAction', readOnly:true, permId:'Employees.module.test.view--east--company'
            propertyView name:'company', actionMap:'navigateToModuleActionMap', permId:'Employees.module.test.view--east--company.actionMap', preferredWidth:120

            propertyView name:'company.workforce', readOnly:true, horizontalAlignment:'LEFT', width:2

            propertyView name:'name', action:'navigateToModuleFrontAction', readOnly:true, permId:'Employees.module.test.view--east--name'
            propertyView name:'name', actionMap:'navigateToModuleActionMap', permId:'Employees.module.test.view--east--name.actionMap', preferredWidth:120

            propertyView name:'company.contact.city', action:'navigateToModuleFrontAction', readOnly:true, permId:'Employees.module.test.view--east--company.contact.city'
            propertyView name:'company.contact.city.name', action:'navigateToModuleFrontAction', readOnly:true, i18nNameKey:'org.jspresso.hrsample.model.City', permId:'Employees.module.test.view--east--company.contact.city.name'

          }
        }
      }
      center {
        table(model: 'Employee-users', permId: 'users.table') {
          actionMap {
            actionList('SERVICE') {
              action parent:'navigateToModuleFrontAction', permId:'Employees.module.test.view--east-table--actionMap', collectionBased:true
              action parent:'navigateToModuleFrontAction', permId:'Employees.module.test.view--east-table--actionMap.notCollectionBased', collectionBased:false
            }
          }
          columns {
            propertyView name:'login', action:'navigateToModuleFrontAction', readOnly:true, permId:'Employees.module.test.view--east-table--login'

            propertyView name:'mainRole.roleId', action:'navigateToModuleFrontAction', readOnly:true, permId:'Employees.module.test.view--east-table--mainRole.roleId'
            propertyView name:'mainRole.roleId', actionMap:'navigateToModuleActionMap'

            propertyView name:'mainRole', action:'navigateToModuleFrontAction', readOnly:true
          }
        }
      }
    }
  }
}

action('importEmployeesBoxAction', parent: 'importBoxAction',
        custom: [mergeFields: ['name', 'firstName'],
                 extraColumns: ['zip'],
                 additionalFields: ['teams'],
                 okAction_ref:'importEmployeeBoxOkAction'])

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
  custom:[models2Module:[
    'org.jspresso.hrsample.model.Employee':'employees.workspace/employees.module',
    'org.jspresso.hrsample.model.Employee/name':'employees.workspace/employees.module',

    'org.jspresso.hrsample.model.Company':'organization.workspace/companies.module',
    'org.jspresso.hrsample.model.Company.name':'organization.workspace/companies.module',

    'org.jspresso.hrsample.model.City':'masterdata.workspace/masterdata.cities.module',
    'org.jspresso.hrsample.model.City/name':'masterdata.workspace/masterdata.cities.module',

    'org.jspresso.hrsample.model.Role':'administration.workspace/roles.admin.module',
    'org.jspresso.hrsample.model.Role/roleId':'administration.workspace/roles.admin.module',

    'org.jspresso.hrsample.model.User/login':'administration.workspace/users.admin.module',
    'org.jspresso.hrsample.model.User':'administration.workspace/users.admin.module']])

/**
 * target factory
 */
bean ('targetActionsConfigurator',
  parent:'targetActionsConfiguratorBase',
  custom:[targets_ref:['navigateToModuleFrontAction']]);

bean ('applicationContextFactoryBean',
  class:'org.jspresso.framework.util.spring.ThisApplicationContextFactoryBean')

// Export to CSV
action ('exportTableToCsvAction', parent:'abstractExportTableAction', icon: '',
        custom:[exportActiveResource_ref:'exportCsvActiveResourceProviderBean'])

action ('exportFilterModuleResultToCsvAction', parent:'abstractExportFilterModuleResultAction',
        custom:[exportActiveResource_ref:'exportCsvActiveResourceProviderBean'],)

action ('exportPivotModuleResultToCsvAction',
        parent:'abstractExportPivotModuleResultAction',
        booleanActionabilityGates:['moduleObjects'],
        name: 'export.csv',
        custom:[exportActiveResource_ref:'exportCsvActiveResourceProviderBean'])

bean ('exportCsvActiveResourceProviderBean',
        class:'org.jspresso.hrsample.ext.frontend.export.ExportCsvActiveResourceProviderBean',
        custom:[mimeType:'text/csv', fileExtension:'csv', separator:','])


//
// Override pivot module table's action map
actionMap('pivotModuleTableActionMap') {
  actionList ('ADD') {
    action parent:'checkAllLinesAction', booleanActionabilityGates:['moduleObjects']
    action parent:'uncheckAllLinesAction', booleanActionabilityGates:['moduleObjects']
    action ref:'addAsChildModuleFrontAction'
  }
  actionList ('EXPORT', collapsable: true) {
    action ref:'exportPivotModuleResultToHtmlAction'
    action ref:'exportPivotModuleResultToCsvAction'
  }
}

tabs('MyFilter.view', borderType: 'TITLED', name: 'filter',
    parent: 'MyFilter.view.base')


tabs('employee.statistics.module.view') {
  actionMap(permId: 'pivotModuleActionMap') {
    actionList('PIVOT_QUERY', collapsable: true,
            renderingOptions: 'ICON') {
      action ref: 'queryPivotModuleFilterFrontAction'
      action ref: 'queryPivotModuleAndReloadFilterFrontAction'
      action ref: 'restartPivotModuleFrontAction'
      action ref: 'queryPivotModuleFilterFrontAction'
      action ref: 'queryPivotModuleAndReloadFilterFrontAction'
    }
    actionList('EXTENSIONS') {
      action ref: 'restartPivotModuleFrontAction'
      action ref: 'chooseQueryCriteriasFrontAction'
      action ref: 'createPermalinkAndCopyToClipboardFrontAction'
    }
    actionList('EXPORT') {
      action parent: 'exportPivotToHtmlAction', custom: [fileName: 'Employee Pivot.xls']
    }
    actionList('DOC') {
      action ref: 'displayModuleDocumentationAction'
      action ref: 'displayModuleDocumentationNoneAction'
    }
  }

  views {

    pivotTable(cellSelectionAction: 'pivotCellSelectionToModuleListFrontAction')

    table(parent: 'pivotBeanCollectionModuleView',
            selectionMode: 'MULTIPLE_INTERVAL_CUMULATIVE_SELECTION', borderType: 'NONE',
            rowAction: 'addEmployeesAsChildModuleFrontAction') {
      actionMap {
        actionList('ADD') {
          action parent: 'checkAllLinesAction', booleanActionabilityGates: ['moduleObjects']
          action parent: 'uncheckAllLinesAction', booleanActionabilityGates: ['moduleObjects']
          action ref: 'addEmployeesAsChildModuleFrontAction'
        }
        actionList('EXPORT', collapsable: true) {
          action ref: 'exportPivotModuleResultToHtmlAction'
          action ref: 'exportPivotModuleResultToCsvAction'
        }
      }
    }
  }
}

action('addEmployeesAsChildModuleFrontAction',
        parent: 'addAsChildModuleFrontAction',
      custom: [parentWorkspaceName: 'employees.workspace',
               parentModuleName:'employees.module'])

/***********************************************
 * Manage User queries
 */
actionMap('UserQueries.secondaryActionMap',
        parents: ['UserQueries.secondaryActionMap.base']) {

  actionList {
    action ref:'editUserSharingListAction'
  }
}

action('editUserSharingListAction',
        parent: 'editComponentAction',
        class: 'org.jspresso.hrsample.ext.frontend.userquery.EditUserSharingListAction',
        collectionBased: true,
        name:'editUserSharingListAction.name', description:'', icon: 'classpath:org/jspresso/hrsample/images/employees.png',
        custom:[okAction_ref:'editUserSharingListOkAction',
                viewDescriptor_ref:'UserSharingList.view'])

border('UserSharingList.view') {
  center {
    table(model: 'UserSharingList-users', readOnly: true, permId: 'sharingUsers.table') {
      actionMap {
        actionList {
          action parent:'chooseEntityFrontAction',
                  booleanActionabilityGates: ['query.mine'],
                  custom:[selectionMode: 'MULTIPLE_INTERVAL_CUMULATIVE_SELECTION',
                          createQueryComponentAction_ref: 'selectUsersForQuerySharingCreateQueryAction',
                          findAction_ref:'sharingUserQueryFrontAction']
          action parent: 'removeEntityCollectionFromMasterFrontAction',
                  booleanActionabilityGates: ['query.mine']
        }
      }
    }
  }
}

border('CityDistance.view') {
  west {
    table(model: 'CityDistance-cities',  permId: 'CityDistance-cities.table',
            selectionModel: 'CityDistance-selectedCities',
            preferredWidth: 200, selectionMode: 'MULTIPLE_INTERVAL_CUMULATIVE_SELECTION', horizontallyScrollable: false) {
      columns {
        propertyView name: 'name', i18nNameKey: 'city'
      }
    }
  }
  center {
    border (i18nNameKey: 'CityDistance.title', borderType: 'TITLED') {
      north {
        border {
          center {
            actionView(renderingOptions: 'LABEL_ICON', font: ';BOLD;16') {
              actionList {
                action parent:'cityItineraryFrontAction', iconHeight:64, iconWidth:64, booleanActionabilityGates: ['itineraryCalculationAllowed'], name: 'itineray.compute.name'
              }
            }
          }
          east {
              enumerationPropertyView name: 'travelMode', radio: true, orientation: 'HORIZONTAL',
                      allowedValues: ['DRIVING', 'WALKING', 'BICYCLING'],
                      action: 'cityItineraryFrontAction'
          }
        }
      }
      center {
        mapView(mapContent: 'mapContent')
      }
    }
  }
}

action('sharingUserQueryFrontAction',
        parent:'lovFindFrontAction') {
  wrapped parent: 'lovFindBackAction', custom: ['queryAction_ref': 'sharingUserQueryBackAction']
}

action('sharingUserQueryBackAction',
        parent: 'queryEntitiesBackAction',
        custom: [criteriaRefiner_ref: 'sharingUserQueryRefiner'])

bean('sharingUserQueryRefiner',
        class: 'org.jspresso.hrsample.ext.frontend.userquery.SharingUserQueryRefiner')

action('editUserSharingListOkAction',
        class: 'org.jspresso.hrsample.ext.frontend.userquery.EditUserSharingListOkAction',
        name: 'ok', icon: 'classpath:org/jspresso/framework/application/images/ok-48x48.svg',
        wrapped: 'closeDialogAction')

action('selectUsersForQuerySharingCreateQueryAction',
        class: 'org.jspresso.hrsample.ext.backend.userquery.SelectUsersForQuerySharingCreateQueryAction')

action('userQuerySharingCheckAction',
        class: 'org.jspresso.hrsample.ext.frontend.userquery.UserQuerySharingCheckAction')

action('muExportAllDataProcessFrontAction',
        parent: 'muExportAllDataProcessBaseFrontAction',
        class: 'org.jspresso.hrsample.ext.frontend.MuExportAllDataProcessExtFrontAction',
        custom: [descriptor_ref: 'ModuleUsageExt',
                 fields        : ['moduleId', 'accessDate', 'accessBy',
                                  'user.employee.name', 'user.employee.firstName',
                                  'workspace.label', 'module.label']])

// Geolocation
actionMap('cityDetailActionMap') {
  actionList ('PERMALINK', collapsable:true) {
    action parent:'createPermalinkAndCopyToClipboardFrontAction', custom:[tinyURL:false]
    action parent:'createPermalinkAndMailToFrontAction', custom:[tinyURL:false]
  }
  actionList ('DOC') {
    action ref: 'displayModuleDocumentationAction'
    action ref: 'displayModuleDocumentationNoneAction'
  }
  actionList {
    action ref: 'cityGeolocateFrontAction'
    action ref: 'cityReverseGeolocateFrontAction'
  }
}

action('cityGeolocateFrontAction',
        parent: 'geolocateFrontAction',
        class: 'org.jspresso.hrsample.ext.frontend.geolocation.GeolocateCityFrontAction',
        custom: ['geolocationEngine_ref': 'geolocationEngine']) {
  custom {
    action('geolocationFinishedAction',
            class: 'org.jspresso.hrsample.ext.frontend.geolocation.GeolocateCityFinishedFrontAction')
  }
}

action('cityReverseGeolocateFrontAction',
        parent: 'reverseGeolocateFrontAction',
        class: 'org.jspresso.hrsample.ext.frontend.geolocation.GeolocateCityFrontAction',
        custom: ['geolocationEngine_ref': 'geolocationEngine']) {
  custom {
    action('geolocationFinishedAction',
            class: 'org.jspresso.hrsample.ext.frontend.geolocation.GeolocateReverseCityFinishedFrontAction')
  }
}

action('cityItineraryFrontAction',
        parent: 'itineraryFrontAction',
        class: 'org.jspresso.hrsample.ext.frontend.geolocation.ItineraryBetweenCitiesFrontAction',
        custom: ['geolocationEngine_ref': 'geolocationEngine']) {
  next class: 'org.jspresso.hrsample.ext.frontend.geolocation.ItineraryBetweenCitiesNextFrontAction'
}

action('cityDistancesModuleStartupAction',
        class: 'org.jspresso.hrsample.ext.frontend.geolocation.CityDistancesModuleStartupAction')

bean('geolocationEngine',
        parent: 'geolocationGoogleMapsEngine',
        class: 'org.jspresso.hrsample.ext.frontend.geolocation.GeolocationGoogleEngine',
        custom: ['key': 'AIzaSyB1WmlmkjNPCrGU7QuxInQ5iFXBpzuap08'])

//bean('geolocationEngine',
//        parent: 'geolocationOpenStreetMapEngine')