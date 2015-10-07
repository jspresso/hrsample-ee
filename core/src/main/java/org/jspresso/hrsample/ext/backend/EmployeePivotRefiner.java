package org.jspresso.hrsample.ext.backend;

import java.math.BigDecimal;
import java.util.Map;

import org.jspresso.framework.ext.pivot.backend.DefaultPivotRefiner;
import org.jspresso.framework.ext.pivot.backend.IPivotRefiner;
import org.jspresso.framework.ext.pivot.backend.IPivotRefinerField;
import org.jspresso.framework.ext.pivot.backend.IPivotStyles;
import org.jspresso.framework.ext.pivot.backend.SimplePivotRefinerField;
import org.jspresso.hrsample.model.Employee;

/**
 * @author Maxime HAMM
 */
public class EmployeePivotRefiner extends DefaultPivotRefiner<Employee> {

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
    public Map<String, Object> getStyle(String measure, Map<String, Object> parentStyle) {
      Map<String, Object> style = super.getStyle(measure, parentStyle);
      style.put(IPivotStyles.STYLE_UNIT, "K€");
      return style;
    }
    @Override
    protected void setValue(BigDecimal value) {
      if (value != null)
        value = value.divide(BigDecimal.valueOf(1000));
      super.setValue(value);
    }
  }
  
}
