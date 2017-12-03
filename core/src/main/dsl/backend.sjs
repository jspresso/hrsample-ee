// Implement your application backend here using the SJS DSL
/**
 * Tmar export
 */
bean('tmarParametersBean',
  parent:'tmarParametersBaseBean',
  custom:[collectionLimit:1,
          excludedEntities:['Event'],
          excludedFields:['id', 'version', 'photo', 'createTimestamp', 'lastUpdateTimestamp', 'propertyTranslations'],
          tmarIds:['City':'zip',
                   'OrganizationalUnit':'ouId', 'Team':'ouId', 'Department':'ouId']])
 
external id:['pivotRefiner'] 

bean('employeePivotRefiner', 
  parent:'pivotRefiner',
  class:'org.jspresso.hrsample.ext.backend.EmployeePivotRefiner') 
 
bean('userQueriesHelper', 
  parent:'userQueriesHelperBase', 
  class:'org.jspresso.hrsample.ext.backend.UserQueriesPerUserHelper')

bean('userQueriesPerOrganisationUnitHelper',
        parent:'userQueriesHelperBase',
        class:'org.jspresso.hrsample.ext.backend.UserQueriesPerOrganisationUnitHelper')

bean('rememberMePropertyDescriptor',    
  class:'org.jspresso.framework.model.descriptor.basic.BasicBooleanPropertyDescriptor',
  name:'rememberMe') 

bean('usernamePropertyDescriptor',
  class:'org.jspresso.framework.model.descriptor.basic.BasicStringPropertyDescriptor',
  name:'username')

bean('passwordPropertyDescriptor',
  class:'org.jspresso.framework.model.descriptor.basic.BasicPasswordPropertyDescriptor',
  name:'password')

bean('timeZoneIdPropertyDescriptor', 
  class:'org.jspresso.framework.model.descriptor.basic.TimeZoneEnumerationPropertyDescriptor',
  name:'timeZoneId')
