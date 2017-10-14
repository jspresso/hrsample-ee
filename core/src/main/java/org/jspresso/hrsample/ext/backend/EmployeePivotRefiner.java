/*
 * Copyright (c) 2005-2016 Vincent Vandenschrick. All rights reserved.
 *
 *  This file is part of the Jspresso framework.
 *
 *  Jspresso is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Jspresso is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with Jspresso.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.jspresso.hrsample.ext.backend;

import org.hibernate.criterion.Projection;
import org.jspresso.contrib.backend.pivot.ExtendedPivotRefiner;
import org.jspresso.framework.ext.pivot.backend.IPivotRefiner;
import org.jspresso.framework.ext.pivot.backend.IPivotRefinerField;
import org.jspresso.framework.ext.pivot.backend.IPivotStyles;
import org.jspresso.framework.ext.pivot.backend.SimplePivotRefinerField;
import org.jspresso.framework.ext.pivot.model.IStyle;
import org.jspresso.framework.ext.pivot.model.Style;
import org.jspresso.framework.model.persistence.hibernate.criterion.EnhancedDetachedCriteria;
import org.jspresso.hrsample.model.Employee;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Maxime HAMM
 */
public class EmployeePivotRefiner extends ExtendedPivotRefiner<Employee> {

  @Override
  protected IPivotRefinerField<Employee, ?> createRefinerField(String field) {

    if ("salary".equals(field))
      return new SalaryField(this);

    if (field.endsWith("encryptedValue"))
      return new EncryptedField(this, field);

    return super.createRefinerField(field);
  }

  /*********************
   * EncryptedField
   */
  private class EncryptedField extends SimplePivotRefinerField<Employee, Object> {

    public EncryptedField(IPivotRefiner<Employee> refiner, String name) {
      super(refiner, name);
    }

    @Override
    public void evaluate(Object... line) {

      int index = getIndexForField(getName());
      if (index >= 0) {
        byte[] encryptedValue = (byte[]) line[index];
        if (encryptedValue !=null) {
          Double decryptedValue = new Double(new String(encryptedValue));
          line[index] = decryptedValue;
        }
      }
      else {
        super.evaluate(line);
      }
    }

    @Override
    public Projection getProjection(EnhancedDetachedCriteria criteria) {
      return super.getProjection(criteria);
    }
  }

  /*********************
   * SalaryField
   */
  private class SalaryField extends SimplePivotRefinerField<Employee, BigDecimal> {
    
    public SalaryField(IPivotRefiner<Employee> refiner) {
      super(refiner, Employee.SALARY);
    }

    @Override
    public List<IStyle> getStyles(String measure) {
      List<IStyle> styles = super.getStyles(measure);
      for (IStyle style : styles) { 
        if (style.getStyleAttributs().containsKey(IPivotStyles.STYLE_UNIT))
          return styles;
      }
      
      Map<String, Object> attributs = new HashMap<>();
      attributs.put(IPivotStyles.STYLE_UNIT, "K€");
      IStyle newStyle = new Style(measure + '-' +SalaryField.class.getSimpleName(), attributs);
      
      styles.add(newStyle);
      
      return styles;
    }
    
    @Override
    protected void setValue(BigDecimal value) {
      if (value != null)
        value = value.divide(BigDecimal.valueOf(1000));
      super.setValue(value);
    }
  }

}
