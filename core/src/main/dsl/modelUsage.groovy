// Implement your domain here using the SJS DSL.
namespace('usage') {

  Entity ('ModuleUsage', icon:'view_tree.png') {
    string_64 'moduleId' 
    date_time 'accessDate'
    string_64 'accessBy' 
  }

  Component('MUStat', 
    extension:'MUStatExtension',
    interceptors:['MUStatInterceptor'], 
    icon:'view_tree.png') {
    
    enumeration 'period', enumName:'PERIOD', values:['DAY', 'WEEK', 'MONTH'] 
    reference 'workspace', ref:'MUWorkspace'
    
    integer 'usersCount'
    set 'usersPerModule', ref:'MUItem'
    
    integer 'accessCount'
    set 'accessPerModule', ref:'MUItem'
    
    reference 'historyModule', ref:'MUModule'
    set 'historyDetails', ref:'MUItem'
    
    set 'allWorkspaces', ref:'MUWorkspace'
    set 'allModules', ref:'MUModule', computed:true
  }
  
  Component('MUWorkspace', 
      toString:'label', autoComplete:'label', rendered:['label'], queryable:['label'], 
      icon:'workspace.png') {
      
    integer 'workspaceId'
    string 'label'
    
    set 'modules', ref:'MUModule'
  }
  
  Component('MUModule', 
      toString:'label', autoComplete:'label', rendered:['label'], queryable:['label'], 
      icon:'module.png') {
    integer 'workspaceId'
    string 'label'
  }
  
  Component('MUItem') {
    string 'label'
    integer 'count'
  }
}
