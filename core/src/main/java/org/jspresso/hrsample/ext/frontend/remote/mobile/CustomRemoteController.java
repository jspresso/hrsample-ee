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

import org.jspresso.framework.ext.application.frontend.controller.remote.mobile.MobileEnhancedRemoteController;
import org.jspresso.framework.security.UsernamePasswordHandler;
import org.jspresso.framework.util.gui.Dimension;
import org.jspresso.framework.util.resources.server.ResourceProviderServlet;

import org.jspresso.hrsample.ext.model.security.CaptchaUsernamePasswordHandler;

/**
 * A custom remote controller used to override the login process in order to
 * introduce a CAPTCHA challenge.
 *
 * @author Vincent Vandenschrick
 */
public class CustomRemoteController extends MobileEnhancedRemoteController {

  /**
   * {@inheritDoc}
   */
  @Override
  protected UsernamePasswordHandler createUsernamePasswordHandler() {
    CaptchaUsernamePasswordHandler cuph = new CaptchaUsernamePasswordHandler();
    cuph.setCaptchaImageUrl(ResourceProviderServlet
        .computeImageResourceDownloadUrl("classpath:org/jspresso/hrsample/ext/images/jspresso.png",
            new Dimension(150, 25)));
    return cuph;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected boolean performLogin() {
    if (getLoginContextName() != null) {
      String password = getLoginCallbackHandler().getPassword();
      if (password == null || password.length() > 0 && !"jspresso".equalsIgnoreCase(
          ((CaptchaUsernamePasswordHandler) getLoginCallbackHandler()).getCaptchaChallenge())) {
        // Captcha challenge failed and it's not an remember me token shortcut.
        return false;
      }
    }
    return super.performLogin();
  }
}
