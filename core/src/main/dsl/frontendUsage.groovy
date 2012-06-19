// Implement your application frontend here using the SJS DSL.
workspace('usage.workspace',
  icon:'classpath:org/jspresso/hrsample/ext/images/usage/view_tree.png') {
  
  beanModule ('usage.module',
    icon:'classpath:org/jspresso/hrsample/ext/images/usage/view_tree.png',
    moduleView:'MUStat.module.view',
    entry:'usageModuleEntryFrontAction', 
    component:'MUStat')
  
}
  
//action('usageWorkspaceEntryFrontAction',
//  class:'org.jspresso.framework.application.frontend.action.module.ModuleSelectionAction',
//  custom:[workspaceName:'usage.workspace', moduleName:'usage.module'])
  
action('usageModuleEntryFrontAction',
  class:'org.jspresso.hrsample.ext.frontend.usage.UsageModuleEntryAction')  

action('anyModuleEnterFrontAction',
  class:'org.jspresso.hrsample.ext.frontend.usage.AnyModuleEnterFrontAction',
  custom:[logOncePerSession:true])
