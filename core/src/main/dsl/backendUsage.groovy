// Implement your application backend here using the SJS DSL.
external id:['queryEntitiesBackActionBase']

bean ('MUStatServiceBean', 
  class:'org.jspresso.hrsample.ext.model.usage.service.MUStatServiceDelegate',
  custom:[dataSource_ref:'dataSource', 
          entityFactory_ref:'basicEntityFactory'])

action ('queryEntitiesBackAction', parent:'queryEntitiesBackActionBase',
  class:'org.jspresso.hrsample.ext.backend.QueryEntitiesExtensionBackAction')