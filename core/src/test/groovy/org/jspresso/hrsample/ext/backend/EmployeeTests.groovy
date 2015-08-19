package org.jspresso.hrsample.ext.backend

import org.jspresso.framework.model.entity.IEntityFactory
import org.jspresso.hrsample.model.Employee
import org.junit.Test

class EmployeeTests extends TmarBackendStartup {

  @Test
  void test() {   
    eachIteration('test') { tmar ->
      IEntityFactory entityFactory = getBackendController().getEntityFactory()
      Employee employee = entityFactory.createEntityInstance(Employee.class)
      employee.birthDate = Date.parse("yyyy/MM/dd", tmar.birthDate)
      
      // make age relative to 2015 !
      def age = employee.age
      age += Calendar.instance.get(Calendar.YEAR) - 2015
            
      // assert age is correctly calculated
      tmar.age = age
    }
  }
}
