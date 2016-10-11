package org.jspresso.hrsample.ext.model.service;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.jspresso.contrib.model.tracking.IModificationTracker;
import org.jspresso.contrib.model.tracking.service.IModificationTrackerServiceDelegate;
import org.jspresso.framework.application.backend.BackendControllerHolder;
import org.jspresso.framework.application.backend.persistence.hibernate.HibernateBackendController;
import org.jspresso.framework.application.backend.session.EMergeMode;
import org.jspresso.hrsample.ext.model.Furniture;
import org.jspresso.hrsample.model.Traceable;
import org.jspresso.hrsample.model.User;

/**
 * ILeaseModificationTracker services delegate.
 */
public class HRTrackerServiceDelegate extends IModificationTrackerServiceDelegate {

  private static final List<String> EXCLUDED;
  
  static {
    EXCLUDED = Arrays.asList(
        Traceable.CREATE_TIMESTAMP, Traceable.LAST_UPDATE_TIMESTAMP,
        Furniture.PARENT);
  }
  
  @Override
  public Locale getTrackingLocale() {
    return Locale.ENGLISH;
  }

  @Override
  public List<String> getTrackingExcludedProperties() {
    return EXCLUDED;
  }
  
  @Override
  public IModificationTracker getParentTracker() {
    IModificationTracker component = getComponent();
    if (component instanceof Furniture) {
      Furniture f = (Furniture)component;
      
      Furniture parent = f.getParent();
      if (parent != null && parent.getParent()==null) {
        return parent;
      }
    }
    return super.getParentTracker();
  }
  
  
  @Override
  public String translateLogin(String login) {
    HibernateBackendController controller = (HibernateBackendController) BackendControllerHolder.getCurrentBackendController();
    
    DetachedCriteria dc = DetachedCriteria.forClass(User.class);
    dc.add(Restrictions.eq(User.LOGIN, login));
    User user = controller.findFirstByCriteria(dc, EMergeMode.MERGE_KEEP, User.class);
    if (user !=null && user.getEmployee()!=null)
      return user.getEmployee().getFullName();
    
    return super.translateLogin(login);
  }
        
}