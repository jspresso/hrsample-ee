package org.jspresso.hrsample.ext.model.extension;

import org.jspresso.framework.model.component.AbstractComponentExtension;
import org.jspresso.hrsample.ext.model.IHRModificationTracker;
import org.jspresso.hrsample.model.User;

/**
 * IHRModificationTracker extension.
 */
public class IHRModificationTrackerExtension extends AbstractComponentExtension<IHRModificationTracker> {

  private User trackingFilterUser;
  
  /**
   * IHRModificationTrackerExtension constructor.
   * @param component 
   */
	public IHRModificationTrackerExtension(IHRModificationTracker component) {
		super(component);	}
  
  /**
   * Gets user
   * @return the user.
   */
  public User getTrackingFilterUser() {
    return trackingFilterUser;
  }
  
  /**
   * Sets user
   * @param trackingFilterUser 
   */
  public void setTrackingFilterUser(User trackingFilterUser) {
    User previousValue = trackingFilterUser;
    
    this.trackingFilterUser = trackingFilterUser;
    getComponent().firePropertyChange(IHRModificationTracker.TRACKING_FILTER_USER, previousValue, trackingFilterUser);

    if (trackingFilterUser != null) {
      getComponent().setTrackingFilterBy(trackingFilterUser.getLogin());
    }
    else {
      getComponent().setTrackingFilterBy(null);
    }
    
   }

}
