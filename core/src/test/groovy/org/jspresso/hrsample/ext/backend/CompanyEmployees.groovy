package org.jspresso.hrsample.ext.backend

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.jspresso.contrib.model.query.UserQuery;
import org.jspresso.contrib.tmar.core.Tmar
import org.jspresso.framework.application.backend.session.EMergeMode;
import org.jspresso.framework.model.descriptor.IComponentDescriptor;
import org.jspresso.hrsample.model.City;
import org.jspresso.hrsample.model.Company;
import org.jspresso.hrsample.model.Department;
import org.jspresso.hrsample.model.Employee;
import org.jspresso.hrsample.model.Event;
import org.jspresso.hrsample.model.Team;

class CompanyEmployees extends TmarBackendStartup {

  def test() {

    when:
      while (tmar.line) {
        
        if (tmar.currentIndexList == 0) {
          
          createEntities(City, tmar.table.city)
          createEntities(Company, tmar.table.company)
    
          createEntities(
              [Employee: tmar.table.employee,
               Team: tmar.table.team,
               Department: tmar.table.department])
          
          createEntities(Event, tmar.table.event)
        }
        
        if ('COUNT' == tmar.operation) {
          tmar.count = countTable(tmar.tableName)
        }
        else if ('EXISTS' == tmar.operation) {
          tmar.exists = true
        }
      }

    //-------
    then:
    tmar.asserts()

    //-------
    cleanup:
    // Cleanup your data
    // TODO If necessary !

    //-------
    where:
    tmar << getData('test')
  }
  
  
  
  private int countTable(String clazz) {
    IComponentDescriptor<?> d = getApplicationContext().getBean(clazz);
    Class c = d.getComponentContract(); 
    
    DetachedCriteria criteria = DetachedCriteria.forClass(c);
    List<UserQuery> queries = getBackendController().findByCriteria(criteria, EMergeMode.MERGE_KEEP, c);
    return queries.size();
  }
}
