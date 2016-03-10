package org.jspresso.hrsample.ext.backend

import java.text.SimpleDateFormat

import org.jspresso.framework.model.entity.IEntityFactory
import org.jspresso.hrsample.model.Employee
import org.junit.Before
import org.junit.Test

class EmployeeTests extends TmarBackendStartup {

//  @Before
//  def setupTest() {
//    def myShareContext = loadSharedContext()
//    setTransformValue('sequence','birthdate') { value ->
//      return Date.parse("yyyy/MM/dd", value)
//    }
//  }

  @Test
  void test() {
    eachIteration('test') { tmar ->
      IEntityFactory entityFactory = getBackendController().getEntityFactory()
      Employee employee = entityFactory.createEntityInstance(Employee.class)
      employee.birthDate = Date.parse("yyyy/MM/dd", tmar.birthDate)

      // make age relative to 2015 !
      def age = employee.age
      age += 2015 -  Calendar.instance.get(Calendar.YEAR) 

      // assert age is correctly calculated
      tmar.age = age
    }
  }
}
