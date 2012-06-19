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
    reference 'workspace', ref:'MUWorkspace', processors:['WorkspaceProcessor'], mandatory:false
    
    integer 'usersCount'
    list 'usersPerModule', ref:'MUItem'
    
    integer 'accessCount'
    list 'accessPerModule', ref:'MUItem'
    
    reference 'historyModule', ref:'MUModule', processors:['HistoryModuleProcessor'], mandatory:false
    list 'historyDetails', ref:'MUItem'
    
    list 'allWorkspaces', ref:'MUWorkspace'
    list 'allModules', ref:'MUModule', computed:true
    
    string 'treeTitle'
  }
   
  Interface('MUModuleInterface', 
      extension:'MUModuleInterfaceExtension',
      services:['org.jspresso.framework.model.component.IComponent' : null]) { 
    
    string 'label'
    string 'iconImageUrl'
      
    list 'modules', ref:'MUModule'
    list 'allModules', ref:'MUModule', computed:true
  }
  
  Component('MUWorkspace', 
      extend:['MUModuleInterface'],
      toString:'label', autoComplete:'label', rendered:['label'], queryable:['label'], 
      icon:'workspace.png') {
      
    string 'workspaceId'
  }
  
  Component('MUModule', 
      extend:['MUModuleInterface'],
      toString:'label', autoComplete:'label', rendered:['label'], queryable:['label'], 
      icon:'module.png') {
      
    string 'moduleId'
    enumeration 'type', values:['BEAN_MODULE', 'NODE_MODULE', 'FILTER_MODULE', 'COLLECTION_MODULE']
  }
  
  Component('MUItem') {
    string 'itemId'  
    string 'label'
    integer 'count'
  }
}


