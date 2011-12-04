// Implement your domain here using the SJS DSL.

Entity('Furniture', 
  extend:['Traceable'],
  icon:'furniture.png') {
  
  string_128 'name'
  
  decimal 'price'
  percent 'discount' 

}