// Implement your domain here using the SJS DSL.

Component('loginModelDescriptor') {
  string_64 'username'
  password 'password', maxLength:64
  imageUrl 'captchaImageUrl'
  string_64 'captchaChallenge'
  bool 'rememberMe'
  string_64 'register' 
  string_64 'help' 
}

Component('Registration') {
  string_32 'firstName'
  string_32 'name'
}

Entity('Furniture', 
  extend:['Traceable', 'ITranslatable'],
  icon:'furniture.png',
  toString:'name',
  queryable:['name']) {
  
  string_128 'name'
  
  decimal 'price'
  percent 'discount' 

}