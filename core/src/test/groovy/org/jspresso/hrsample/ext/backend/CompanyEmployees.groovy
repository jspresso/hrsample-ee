package org.jspresso.hrsample.ext.backend

import org.jspresso.hrsample.model.City;
import org.jspresso.hrsample.model.Company
import org.jspresso.hrsample.model.Department;
import org.jspresso.hrsample.model.OrganizationalUnit;
import org.jspresso.hrsample.model.Team;


class CompanyEmployees extends TmarBackendStartup {

  def test() {

    when:
    // Manage your test case 
    createEntities(City, tmar.table.city)
    createEntities(Company, tmar.table.company)
    
    createEntities(
      [Employee: tmar.table.employee,
       Team: tmar.table.team,
       Department: tmar.table.department])
    
    // Prepare assertions
    tmar.employeeCount = 5

    then:
    tmar.asserts()

    cleanup:
    // Cleanup your data
    // TODO If necessary !

    where:
    tmar << getData('test')
  }

  

}
