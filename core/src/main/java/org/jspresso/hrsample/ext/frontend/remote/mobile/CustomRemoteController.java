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
package org.jspresso.hrsample.ext.frontend.remote.mobile;

import java.io.Serializable;
import java.security.Principal;
import java.util.Locale;

import javax.security.auth.Subject;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.jspresso.framework.application.backend.BackendControllerHolder;
import org.jspresso.framework.application.backend.persistence.hibernate.HibernateBackendController;
import org.jspresso.framework.application.backend.session.EMergeMode;
import org.jspresso.framework.ext.application.frontend.controller.remote.mobile.MobileEnhancedRemoteController;
import org.jspresso.framework.security.UserPrincipal;
import org.jspresso.framework.security.UsernamePasswordHandler;
import org.jspresso.framework.util.bean.integrity.IntegrityException;
import org.jspresso.framework.util.i18n.ITranslationProvider;
import org.jspresso.hrsample.ext.frontend.ICaptchaController;
import org.jspresso.hrsample.ext.model.security.CaptchaUsernamePasswordHandler;
import org.jspresso.hrsample.model.User;

/**
 * A custom remote controller used to override the login process in order to
 * introduce a CAPTCHA challenge.
 *
 * @author Vincent Vandenschrick
 */
public class CustomRemoteController extends MobileEnhancedRemoteController implements ICaptchaController {

  /**
   * {@inheritDoc}
   */
  @Override
  protected UsernamePasswordHandler createUsernamePasswordHandler() {
    return new CaptchaUsernamePasswordHandler();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void loggedIn(Subject subject) {
    CaptchaUsernamePasswordHandler login = (CaptchaUsernamePasswordHandler) getLoginCallbackHandler();
    if (login.getPassword()!=null && login.getPassword().length()>0 && !login.checkCaptcha()) {
      generateNewCaptcha();
      throw new IntegrityException("Captcha challenge failed", "captcha.failed.msg");
    }
    super.loggedIn(subject);
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public void generateNewCaptcha() {
    ((CaptchaUsernamePasswordHandler) getLoginCallbackHandler()).generateCaptcha();
  }
  
  @Override
  public String getI18nDescription(ITranslationProvider translationProvider, Locale locale) {
    return translationProvider.getTranslation(
        getDescription(), 
        new Object[]{getUser().getEmployee().getFullName()}, 
        locale);
  }

  /**
   * Get user.
   * @param controller The controller
   * @return the User.
   */
  private static User getUser() {
    HibernateBackendController controller = (HibernateBackendController) BackendControllerHolder.getCurrentBackendController();
    UserPrincipal principal = controller.getApplicationSession().getPrincipal();
    
    DetachedCriteria dc = DetachedCriteria.forClass(User.class);
    dc.add(Restrictions.eq(User.LOGIN, principal.getName()));
    
    return controller.findFirstByCriteria(dc, EMergeMode.MERGE_KEEP, User.class);
  }
}
