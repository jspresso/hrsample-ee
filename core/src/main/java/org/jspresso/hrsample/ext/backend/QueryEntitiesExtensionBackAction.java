package org.jspresso.hrsample.ext.backend;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Junction;
import org.hibernate.criterion.Restrictions;
import org.jspresso.framework.action.IActionHandler;
import org.jspresso.framework.application.backend.action.persistence.hibernate.QueryEntitiesAction;
import org.jspresso.framework.application.model.BeanModule;
import org.jspresso.framework.model.component.IComponent;
import org.jspresso.framework.model.component.IQueryComponent;
import org.jspresso.framework.model.component.query.EnumValueQueryStructure;
import org.jspresso.framework.model.descriptor.IComponentDescriptor;
import org.jspresso.framework.model.descriptor.IEnumerationPropertyDescriptor;
import org.jspresso.framework.model.descriptor.IPropertyDescriptor;
import org.jspresso.framework.model.entity.IEntity;
import org.jspresso.framework.view.descriptor.basic.PropertyViewDescriptorHelper;
import org.jspresso.hrsample.ext.model.usage.MUStat;
import org.jspresso.hrsample.ext.model.usage.MUWorkspace;

public class QueryEntitiesExtensionBackAction extends QueryEntitiesAction {

  @Override
  public boolean execute(IActionHandler actionHandler, Map<String, Object> context) {
    return super.execute(actionHandler, context);
  }
  
  @Override
  protected List<?> performQuery(IQueryComponent queryComponent, Map<String, Object> context) {
    IComponentDescriptor<?> queryDescriptor = queryComponent.getQueryDescriptor();
    if (MUWorkspace.class.isAssignableFrom(queryDescriptor.getComponentContract())) {
      
      //TODO FInd a better way.. the following line is is not generic at all ! May using getParentModule() (which return null for now)
      MUStat stat = (MUStat) ((BeanModule)getModule(context)).getModuleObject(); 
      
      // filter result
      String labelFilter = (String) queryComponent.get("label");
      ArrayList<IComponent> list = new ArrayList<IComponent>();
      for (MUWorkspace w : stat.getAllWorkspaces()) {
        if (labelFilter!=null && !matchQueryString(w.getLabel(), labelFilter)) {
          continue;
        }
        list.add(w);
      }
      return list;
    }
    else {
      return super.performQuery(queryComponent, context);
    }
  }
  
  /**
   * check if an input string matches a standard jspresso query expression including :
   * - "!" to invert selection
   * - "%" anywhere to allow any sequence
   * - implicit "%" at end of field  
   * @param input
   * @param expression
   * @return
   */
  public static boolean matchQueryString(String input, String exp) {
    boolean inverseSelection = false;
    Pattern regex = null;
    
    StringBuffer sb = new StringBuffer();   
    if (exp.startsWith("!") && exp.length()>1) {
      inverseSelection = true;
      exp = exp.substring(1);
    }

    StringTokenizer st = new StringTokenizer(exp, "%", true);
    while (st.hasMoreTokens()) {
      String token = st.nextToken();
      if (token.equals("%")) {
        sb.append(".*");
      }
      else {
        sb.append(Pattern.quote(token));
      } 
    }
    exp = sb.toString();
    if (!exp.endsWith(".*")) {
      exp = exp + ".*";
    }
    regex = Pattern.compile(exp, Pattern.CASE_INSENSITIVE);

    return regex.matcher(input).matches() != inverseSelection;
  }
  
}
