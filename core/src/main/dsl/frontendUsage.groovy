// Implement your application frontend here using the SJS DSL.
workspace('usage.workspace',
  icon:'classpath:org/jspresso/hrsample/ext/images/usage/view_tree.png') {
  
  beanModule ('usage.module',
    icon:'classpath:org/jspresso/hrsample/ext/images/usage/view_tree.png',
    moduleView:'MUStat.module.view',
    entry:'usageModuleEntryFrontAction',
    component:'MUStat')
  
}
  
action('usageModuleEntryFrontAction',
  class:'org.jspresso.hrsample.ext.frontend.usage.UsageModuleEntryAction')  

action('usageModuleEnterFrontAction',
  class:'org.jspresso.hrsample.ext.frontend.usage.UsageModuleEnterFrontAction')
