package org.jspresso.hrsample.ext.backend

import org.hibernate.criterion.DetachedCriteria
import org.hibernate.criterion.Restrictions
import org.jspresso.contrib.backend.tmar.TmarParameters
import org.jspresso.contrib.test.backend.tmar.TmarPlus
import org.jspresso.framework.application.backend.BackendControllerHolder
import org.jspresso.framework.application.backend.session.EMergeMode
import org.jspresso.framework.model.descriptor.IComponentDescriptor
import org.jspresso.framework.model.entity.IEntity
import org.jspresso.framework.util.accessor.IAccessor
import org.jspresso.framework.util.accessor.IAccessorFactory
import org.jspresso.framework.util.uid.ByteArray
import org.jspresso.hrsample.model.City;
import org.jspresso.hrsample.model.Company;
import org.jspresso.hrsample.model.Department;
import org.jspresso.hrsample.model.Employee;
import org.jspresso.hrsample.model.Team;

class CompanyEmployees extends TmarBackendStartup {

  /**
   * test1
   */
  def test1() {
    when:
    while (tmar.hasNext()) {
      if (tmar.currentIndexList == 0) {
        createEntities(
            [Employee: tmar.table.employee,
             Team: tmar.table.team,
             Department: tmar.table.department,
             City: tmar.table.city,
             Company: tmar.table.company])
      }
      prepareThen(tmar)
    }

    //-------
    then:
    tmar.asserts()

    //-------
    cleanup:
    cleanupData(tmar);

    //-------
    where:
    tmar << getData('test1')
    
  }

  
  /**
   * test2
   */
  def test2() {
    when:
    while (tmar.hasNext()) {
      if (tmar.currentIndexList == 0) {
        createEntities([City: tmar.table.city])

        createEntities([Company: tmar.table.company])

        createEntities(
           [Employee: tmar.table.employee,
            Team: tmar.table.team,
            Department: tmar.table.department])
      }
      prepareThen(tmar)
    }

    //-------
    then:
    tmar.asserts()

    //-------
    cleanup:
    cleanupData(tmar);
    
    //-------
    where:
    tmar << getData('test2')
    
  }
  
  
  
  /**
   * test3
   */
  def test3() {
    when:
    
    // remove all tmar ids !
    getTmarParameters().setTmarIds(null)
    
    while (tmar.hasNext()) {
      if (tmar.currentIndexList == 0) {
        createEntities([City: tmar.table.city])

        createEntities([Company: tmar.table.company])

        createEntities(
           [Employee: tmar.table.employee,
            Team: tmar.table.team,
            Department: tmar.table.department])
      }
      prepareThen(tmar)
    }

    //-------
    then:
    tmar.asserts()

    //-------
    cleanup:
    cleanupData(tmar);
    
    //-------
    where:
    tmar << getData('test3')
    
  }
  
  /**
   * cleanup data
   * @param tmar
   * @return
   */
  private cleanupData(tmar) {
    deleteAllEntities(Company.class)
    deleteAllEntities(City.class)
  }
  
  /**
   * prepate then
   * @param tmar
   * @return
   */
  private prepareThen(tmar) {
    if ('COUNT' == tmar.operation) {
      tmar.size = countTable(tmar.tableName)
    }
    else if ('EXISTS' == tmar.operation) {
      tmar.exists = findEntity(tmar.tableName, tmar.keyValue as String) !=null
    }
    else if ('CHECK' == tmar.operation) {
      tmar.value = findEntityField(tmar.tableName, tmar.keyValue as String, tmar.field as String)
    }
    return tmar
  }


  
  
  
//  /**
//   * do test
//   */
//  def doTest(Closure createEntitiesClosure) { 
//
//    when:
//    while (tmar.hasNext()) {
//
//      if (tmar.currentIndexList == 0) {
//        createEntitiesClosure.call()
//      }
//
//      if ('COUNT' == tmar.operation) {
//        tmar.size = countTable(tmar.tableName)
//      }
//      else if ('EXISTS' == tmar.operation) {
//        tmar.exists = findEntity(tmar.tableName, tmar.keyValue as String) !=null
//      }
//      else if ('CHECK' == tmar.operation) {
//        tmar.value = findEntityField(tmar.tableName, tmar.keyValue as String, tmar.field as String)
//      }
//    }
//
//    //-------
//    then:
//    tmar.asserts()
//
//    //-------
//    where:
//    tmar << getData('test')
//  }

  
  
  /**
   * countTable
   * @param clazz
   * @return
   */
  private int countTable(String clazz) {
    IComponentDescriptor<?> d = getApplicationContext().getBean(clazz);
    Class c = d.getComponentContract();

    DetachedCriteria criteria = DetachedCriteria.forClass(c);
    List<?> queries = getBackendController().findByCriteria(criteria, EMergeMode.MERGE_KEEP, c);

    return queries.size();
  }

  /**
   * find entity
   * @param clazz
   * @param keyValue
   * @return
   */
  private IEntity findEntity(String clazz, String keyValue) {
    IComponentDescriptor<?> d = getApplicationContext().getBean(clazz);
    Class c = d.getComponentContract();
    if (keyValue.startsWith("\"")) {
      keyValue = keyValue.substring(1, keyValue.length()-2)
    }

    String key = null;
    TmarParameters param = getTmarParameters();
    if (param.getTmarIds()!=null) {
      key = param.getTmarIds().get(clazz);
    }
    
    if (key == null) {
      key = TmarPlus.findUnicityScope(d);
    }
    
    if (key==null) {
      Serializable keyId = isStringGUIDGenerator() ? keyValue : new ByteArray(keyValue.getBytes())
      
      IEntity entity = getBackendController().findById(keyId, EMergeMode.MERGE_KEEP, c)
      return entity;
    }

    DetachedCriteria criteria = DetachedCriteria.forClass(c);
    criteria.add(Restrictions.eq(key, keyValue));
    IEntity entity = getBackendController().findFirstByCriteria(criteria, EMergeMode.MERGE_KEEP, c);
    return entity;
  }

  /**
   * find entitie's field value
   * @param clazz
   * @param keyValue
   * @param field
   * @return
   */
  private Object findEntityField(String clazz, String keyValue, String field) {
    IEntity entity = findEntity(clazz, keyValue);
    if (entity == null) {
      return null;
    }
    
    String javaField = org.jspresso.contrib.backend.tmar.TmarUtil.convertSpacesToCamelCase(field);
    IAccessorFactory accessorFactory = BackendControllerHolder.getCurrentBackendController().getAccessorFactory();
    IAccessor accessor = accessorFactory.createPropertyAccessor(javaField, entity.getComponentContract());
    Object o = accessor.getValue(entity);
    
    return o;
  }



  
}
