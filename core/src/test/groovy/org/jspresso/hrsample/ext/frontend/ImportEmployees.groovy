package org.jspresso.hrsample.ext.frontend

import org.hibernate.criterion.DetachedCriteria
import org.hibernate.criterion.Restrictions
import org.jspresso.contrib.tmar.core.Tmar4JUnit
import org.jspresso.framework.application.backend.session.EMergeMode
import org.jspresso.framework.model.entity.IEntityFactory
import org.jspresso.framework.model.persistence.hibernate.criterion.EnhancedDetachedCriteria
import org.jspresso.hrsample.ext.backend.EmployeeTests
import org.jspresso.hrsample.ext.backend.TmarBackendStartup
import org.jspresso.hrsample.model.Employee
import org.junit.AfterClass
import org.junit.BeforeClass
import org.junit.Test

class ImportEmployees extends TmarFrontendStartup {

    @Test
    void test() {

        eachIteration('test') { tmar ->

            // Check if employee exists
            Employee employee = findEmployee(tmar.employeeName, tmar.employeeFirstName);
            tmar.employeeExists = employee!=null

        }
    }



    private Employee findEmployee(String name, String firstName) {

        EnhancedDetachedCriteria criteria = EnhancedDetachedCriteria.forClass(Employee.class);
        criteria.add(Restrictions.eq(Employee.FIRST_NAME, firstName))
        criteria.add(Restrictions.eq(Employee.NAME, name))

        Employee employee = getBackendController().findFirstByCriteria(criteria, EMergeMode.MERGE_KEEP, Employee.class);

        return employee;
    }
}