// Implement your domain here using the SJS DSL.

Component('loginModelDescriptor') {
  string_64 'username'
  string_64 'password'
  imageUrl 'captchaImageUrl'
  string_64 'captchaChallenge'
  bool 'rememberMe'
  string_64 'register' 
}

Entity('Furniture', 
  extend:['Traceable', 'ITranslatable'],
  icon:'furniture.png',
  toString:'name') {
  
  string_128 'name'
  
  decimal 'price'
  percent 'discount' 

}