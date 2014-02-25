package org.jspresso.hrsample.ext.backend

import org.apache.commons.collections.KeyValue
import org.hibernate.criterion.DetachedCriteria
import org.hibernate.criterion.Restrictions
import org.jspresso.contrib.test.backend.tmar.TmarUtil
import org.jspresso.framework.application.backend.BackendControllerHolder
import org.jspresso.framework.application.backend.session.EMergeMode
import org.jspresso.framework.model.descriptor.IComponentDescriptor
import org.jspresso.framework.model.entity.IEntity
import org.jspresso.framework.util.accessor.IAccessor
import org.jspresso.framework.util.accessor.IAccessorFactory
import org.jspresso.framework.util.uid.ByteArray

class CompanyEmployees extends TmarBackendStartup {

  def test() {

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

//        createEntities([City: tmar.table.city])
//        createEntities([Company: tmar.table.company])
        
//        createEntities(
//            [Employee: tmar.table.employee,
//              Team: tmar.table.team,
//              Department: tmar.table.department,
//              City: tmar.table.city,
//              Company: tmar.table.company])

      if ('COUNT' == tmar.operation) {
        tmar.size = countTable(tmar.tableName)
      }
      else if ('EXISTS' == tmar.operation) {
        tmar.exists = findEntity(tmar.tableName, tmar.keyValue as String) !=null
      }
      else if ('CHECK' == tmar.operation) {
        tmar.value = findEntityField(tmar.tableName, tmar.keyValue as String, tmar.field as String)
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

    String key = TmarUtil.findUnicityScope(d);
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
