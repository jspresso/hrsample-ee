package org.jspresso.hrsample.ext.model.extension;

import org.jspresso.contrib.model.tracking.Delta;
import org.jspresso.contrib.model.tracking.IModificationTracker;
import org.jspresso.contrib.model.tracking.service.ModificationEventService;
import org.jspresso.framework.model.component.AbstractComponentExtension;
import org.jspresso.framework.model.component.service.DependsOn;
import org.jspresso.hrsample.ext.model.Furniture;
import org.jspresso.hrsample.ext.model.IHRModificationTracker;
import org.jspresso.hrsample.ext.model.IIHRModificationTrackerExtension;
import org.jspresso.hrsample.model.User;

import java.util.List;

/**
 * IHRModificationTracker extension.
 */
public class IHRModificationTrackerExtension extends AbstractComponentExtension<IHRModificationTracker> implements IIHRModificationTrackerExtension {

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
   *
   * @param trackingFilterUser
   */
  public void setTrackingFilterUser(User trackingFilterUser) {
    User previousValue = trackingFilterUser;

    this.trackingFilterUser = trackingFilterUser;
    getComponent().firePropertyChange(IHRModificationTracker.TRACKING_FILTER_USER, previousValue, trackingFilterUser);

    if (trackingFilterUser != null) {
      getComponent().setTrackingFilterBy(trackingFilterUser.getLogin());
    } else {
      getComponent().setTrackingFilterBy(null);
    }
  }

  /**
   * Gets the priceBackground.
   *
   * @return the priceBackground.
   */
  @DependsOn({IModificationTracker.TRACKING_EVENTS,
          Furniture.PARENT,
          Furniture.PRICE})
  @Override
  public String getPriceBackground() {
    IHRModificationTracker tracker = getComponent();

    List<Delta> deltas = tracker.loadTrackingDelta(Furniture.PRICE, 1);
    if (!deltas.isEmpty())
      return ModificationEventService.HIGHLIGHT_COLOR;

    return null;

  }


  /**
   * Gets the priceTooltip.
   *
   * @return the priceTooltip.
   */
  @DependsOn(IHRModificationTracker.PRICE_BACKGROUND)
  @Override
  public String getPriceTooltip() {
    IHRModificationTracker tracker = getComponent();

    List<Delta> deltas = tracker.loadTrackingDelta(Furniture.PRICE, 25);
    if (!deltas.isEmpty())
      return tracker.buildTooltip(deltas);

    return null;
  }


}
