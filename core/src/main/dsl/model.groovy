// Implement your domain here using the SJS DSL.
external id:['loginModelDescriptor']

namespace('security') {
  Component('CaptchaUsernamePasswordHandler', extend:['loginModelDescriptor']) {
    imageUrl 'captchaImageUrl'
    string_64 'captchaChallenge'
    string_64 'register', readOnly:true
    string_64 'help', readOnly:true
  }
}

Component('Registration') {
  string_32 'firstName'
  string_32 'name'
}

Entity('Furniture', 
  extend:['Traceable', 'ITranslatable'], 
  icon:'furniture.png',
  toString:'name',
  queryable:['name', 'nlsOrRawLabel', 'lastUpdateTimestamp'],
  extension:'FurnitureExtension',
  rendered:['name', 'price', 'discount', 'createTimestamp', 'lastUpdateTimestamp', 'rawLabel', 'nlsLabel', 'nlsOrRawLabel']) {
  string_128 'name'
  
  decimal 'price'
  percent 'discount'   
  
  set 'previous', ref:'Furniture'

  string_256 'nlsLabel', computed:true, delegateWritable:true, sqlName:"(SELECT T.LABEL FROM TRANSLATION T WHERE T.TRANSLATED_ID = ID AND T.LANGUAGE = :JspressoSessionGlobals.language) AND T.TRANSLATED_NAME = '{entityName}'"
  string_256 'nlsOrRawLabel', computed:true, delegateWritable:true, sqlName:"CASE WHEN (SELECT T.LABEL FROM TRANSLATION T WHERE T.TRANSLATED_ID = ID AND T.LANGUAGE = :JspressoSessionGlobals.language  AND T.TRANSLATED_NAME = '{entityName}') IS NULL THEN RAW_LABEL ELSE (SELECT T.LABEL FROM TRANSLATION T WHERE T.TRANSLATED_ID = ID AND T.LANGUAGE = :JspressoSessionGlobals.language AND T.TRANSLATED_NAME = '{entityName}') END"
}
