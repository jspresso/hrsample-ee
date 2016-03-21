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

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jspresso.contrib.backend.pivot.ExtendedPivotRefiner;
import org.jspresso.framework.ext.pivot.backend.IPivotRefiner;
import org.jspresso.framework.ext.pivot.backend.IPivotRefinerField;
import org.jspresso.framework.ext.pivot.backend.IPivotStyles;
import org.jspresso.framework.ext.pivot.backend.SimplePivotRefinerField;
import org.jspresso.framework.ext.pivot.model.IStyle;
import org.jspresso.framework.ext.pivot.model.Style;
import org.jspresso.hrsample.model.Employee;

/**
 * @author Maxime HAMM
 */
public class EmployeePivotRefiner extends ExtendedPivotRefiner<Employee> {

  @Override
  protected IPivotRefinerField<Employee, ?> createRefinerField(String field) {
    if ("salary".equals(field))
      return new SalaryField(this);
    return super.createRefinerField(field);
  }

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
