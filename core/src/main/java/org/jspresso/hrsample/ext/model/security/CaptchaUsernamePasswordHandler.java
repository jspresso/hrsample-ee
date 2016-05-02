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
package org.jspresso.hrsample.ext.model.security;

import javax.servlet.http.HttpServletRequest;

import org.jspresso.framework.application.backend.BackendControllerHolder;
import org.jspresso.framework.application.backend.IBackendController;
import org.jspresso.framework.security.UsernamePasswordHandler;
import org.jspresso.framework.util.http.HttpRequestHolder;

/**
 * A custom username password handler to demonstrate how login view can be
 * augmented.
 *
 * @author Vincent Vandenschrick
 */
public class CaptchaUsernamePasswordHandler extends UsernamePasswordHandler {

  private String captchaImageUrl;
  private String captchaChallenge;

  /**
   * Gets the captchaImageUrl.
   *
   * @return the captchaImageUrl.
   */
  public String getCaptchaImageUrl() {
    return captchaImageUrl;
  }

  /**
   * Sets the captchaImageUrl.
   *
   * @param captchaImageUrl
   *          the captchaImageUrl to set.
   */
  public void setCaptchaImageUrl(String captchaImageUrl) {
    this.captchaImageUrl = captchaImageUrl;
  }

  /**
   * Gets the captchaChallenge.
   *
   * @return the captchaChallenge.
   */
  public String getCaptchaChallenge() {
    return captchaChallenge;
  }

  /**
   * Sets the captchaChallenge.
   *
   * @param captchaChallenge
   *          the captchaChallenge to set.
   */
  public void setCaptchaChallenge(String captchaChallenge) {
    this.captchaChallenge = captchaChallenge;
  }

  /**
   * Gets the registration link text.
   *
   * @return the registration link text.
   */
  public String getRegister() {
    IBackendController bc = BackendControllerHolder.getCurrentBackendController();
    if (bc == null) {
      return "...";
    }
    return bc.getTranslation("registerLink", bc.getLocale());
  }

  /**
   * Gets the help link text.
   *
   * @return the help link text.
   */
  public String getHelp() {
    IBackendController bc = BackendControllerHolder.getCurrentBackendController();
    if (bc == null) {
      return "...";
    }
    return bc.getTranslation("helpLink", bc.getLocale());
  }

  /**
   * Gets the swith UI link text.
   *
   * @return the help link text.
   */
  public String getSwithUI() {
    IBackendController bc = BackendControllerHolder.getCurrentBackendController();
    if (bc == null) {
      return "...";
    }
    
    String key;
    HttpServletRequest request = HttpRequestHolder.getServletRequest();
    if (request.getRequestURI().contains(".qxrpc")) 
      key = "swith.flex.action.name";
    else 
      key = "swith.html5.action.name";
    
    return bc.getTranslation(key, bc.getLocale());
  }

}
