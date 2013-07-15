// Implement your domain here using the SJS DSL.

namespace('security') {
  Component('CaptchaUsernamePasswordHandler') {
    string_64 'username'
    password 'password', maxLength:64
    imageUrl 'captchaImageUrl'
    string_64 'captchaChallenge'
    enumeration 'language', enumName:'LANGUAGE',
    valuesAndIcons:[
        'fr':'/org/jspresso/framework/application/images/i18n/fr.png',
        'en':'/org/jspresso/framework/application/images/i18n/en.png',
        'de':'/org/jspresso/framework/application/images/i18n/de.png',
        'es':'/org/jspresso/framework/application/images/i18n/es.png',
        'pt':'/org/jspresso/framework/application/images/i18n/pt.png',
        'it':'/org/jspresso/framework/application/images/i18n/it.png',
        'nl':'/org/jspresso/framework/application/images/i18n/nl.png',
        'pl':'/org/jspresso/framework/application/images/i18n/pl.png',
        'tr':'/org/jspresso/framework/application/images/i18n/tr.png',
        'bg':'/org/jspresso/framework/application/images/i18n/bg.png',
        'ru':'/org/jspresso/framework/application/images/i18n/ru.png',
        'cs':'/org/jspresso/framework/application/images/i18n/cs.png',
        'sk':'/org/jspresso/framework/application/images/i18n/sk.png'
    ]
    bool 'rememberMe'
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
