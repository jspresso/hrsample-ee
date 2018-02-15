// Implement your domain here using the SJS DSL.
external id:['loginModelDescriptor']

namespace('security') {

  Component('CaptchaUsernamePasswordHandler', 
    extend:['loginModelDescriptor']) {
    
    image 'captchaImage', readOnly: true, i18nNameKey:'captchaImageUrl'
    
    string_64 'captchaChallenge'
    string_64 'register', readOnly:true
    string_64 'help', readOnly:true 
    string_64 'switchUI', readOnly:true
  }
}

Component('Registration') {
  string_32 'firstName'
  string_32 'name'
}

Entity('Furniture',
  extend:['Traceable', 'ITranslatable', 'IHRModificationTracker'], 
  icon:'furniture.png',
  toString:'name',
  queryable:['name', 'nlsOrRawLabel', 'lastUpdateTimestamp'],
  extension:'FurnitureExtension',
  rendered:['name', 'price', 'discount', 'rawLabel', 'nlsLabel', 'nlsOrRawLabel']) {
  string_128 'name'
  
  decimal 'price'
  percent 'discount'   
  
  set 'previous', ref:'Furniture', i18nNameKey:'details'
  reference 'parent', ref:'Furniture', reverse:'Furniture-previous'

  string_256 'nlsLabel', computed:true, delegateWritable:true, sqlName:"(SELECT T.LABEL FROM TRANSLATION T WHERE T.TRANSLATED_ID = ID AND T.LANGUAGE = :JspressoSessionGlobals.language AND T.TRANSLATED_NAME = '{entityName}')"
  string_256 'nlsOrRawLabel', computed:true, delegateWritable:true, sqlName:"CASE WHEN (SELECT T.LABEL FROM TRANSLATION T WHERE T.TRANSLATED_ID = ID AND T.LANGUAGE = :JspressoSessionGlobals.language  AND T.TRANSLATED_NAME = '{entityName}') IS NULL THEN RAW_LABEL ELSE (SELECT T.LABEL FROM TRANSLATION T WHERE T.TRANSLATED_ID = ID AND T.LANGUAGE = :JspressoSessionGlobals.language AND T.TRANSLATED_NAME = '{entityName}') END"
}
  
Interface ('IHRModificationTracker',
  extend:['IModificationTracker'],
  extension:'IHRModificationTrackerExtension',
  services:['org.jspresso.contrib.model.tracking.service.IModificationTrackerService':'HRTrackerServiceDelegate']) {
  
  reference 'trackingFilterUser', ref:'User', computed:true, delegateWritable:true, i18nNameKey:'tracking.updatedBy'

  color 'priceBackground', computed: true, cacheable: true
  html 'priceTooltip', computed: true, cacheable: true
  
}

namespace('userquery') {
    
  Component('UserSharingList',
      processor: 'UserSharingListProcessor', 
      services: ['UserSharingListService': 'UserSharingListServiceDelegate']) {

    list 'users', ref: 'User'
    reference 'query', ref: 'UserQuery', processors: ['QueryProcessor']
  }

}

Component('ModuleUsageExt',
        extend: ['IModuleUsage']) {

  reference 'user', ref:'User'
  reference 'workspace', ref:'MUWorkspace'
  reference 'module', ref:'MUModule'

}