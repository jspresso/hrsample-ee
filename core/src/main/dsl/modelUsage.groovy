// Implement your domain here using the SJS DSL.
namespace('usage') {

  Entity ('ModuleUsage', icon:'view_tree.png') {
    string_64 'moduleId' 
    date_time 'accessDate'
    string_64 'accessBy' 
  } 

  Component('MUStat',
    extension:'MUStatExtension',
    processor:'MUStatProcessors',
    serviceBeans:['MUStatService':'MUStatServiceBean'], 
    icon:'view_tree.png') {
    
    enumeration 'period', enumName:'PERIOD', values:['DAY', 'WEEK', 'MONTH', 'YEAR'], processors:['PeriodProcessor'] 
    reference 'workspace', ref:'MUWorkspace', processors:['WorkspaceProcessor']
    
    integer 'usersCount'
    list 'usersPerModule', ref:'MUItem'
    
    integer 'accessCount'
    list 'accessPerModule', ref:'MUItem'
    
    reference 'historyModule', ref:'MUModule', processors:['HistoryModuleProcessor']
    list 'historyDetails', ref:'MUItem'
    
    list 'allWorkspaces', ref:'MUWorkspace'
    list 'allModules', ref:'MUModule', computed:true
  }
   
  Interface('MUModuleInterface', 
      extension:'MUModuleInterfaceExtension',
      services:['org.jspresso.framework.model.component.IComponent' : null]) { 
    list 'modules', ref:'MUModule'
    list 'allModules', ref:'MUModule', computed:true
  }
  
  Component('MUWorkspace', 
      extend:['MUModuleInterface'],
      toString:'label', autoComplete:'label', rendered:['label'], queryable:['label'], 
      icon:'workspace.png') {
      
    string 'workspaceId'
    string 'label'
  }
  
  Component('MUModule', 
      extend:['MUModuleInterface'],
      toString:'label', autoComplete:'label', rendered:['label'], queryable:['label'], 
      icon:'module.png') {
      
    string 'moduleId'
    string 'label'
  }
  
  Component('MUItem') {
    string 'label'
    integer 'count'
  }
}


