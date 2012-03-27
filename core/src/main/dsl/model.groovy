// Implement your domain here using the SJS DSL.

Entity('Furniture', 
  extend:['Traceable', 'ITranslatable'],
  icon:'furniture.png',
  toString:'name') {
  
  string_128 'name'
  
  decimal 'price'
  percent 'discount' 

}