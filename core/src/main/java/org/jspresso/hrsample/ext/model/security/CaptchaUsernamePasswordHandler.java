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

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;

import jj.play.ns.nl.captcha.Captcha;
import jj.play.ns.nl.captcha.gimpy.FishEyeGimpyRenderer;
import jj.play.ns.nl.captcha.noise.StraightLineNoiseProducer;

import org.jspresso.framework.application.backend.BackendControllerHolder;
import org.jspresso.framework.application.backend.IBackendController;
import org.jspresso.framework.security.UsernamePasswordHandler;
import org.jspresso.framework.util.exception.NestedRuntimeException;
import org.jspresso.framework.util.http.HttpRequestHolder;

/**
 * A custom username password handler to demonstrate how login view can be
 * augmented.
 *
 * @author Vincent Vandenschrick
 * @author Maxime Hamm
 */
public class CaptchaUsernamePasswordHandler extends UsernamePasswordHandler {

  private String captchaChallenge;
  private byte[] captchaImage;
  private String captchaAnswer;

  /**
   * CaptchaUsernamePasswordHandler constructor
   */
  public CaptchaUsernamePasswordHandler() {
    generateCaptcha();
  }

  /**
   * Generate new captcha.
   */
  public void generateCaptcha() {
    Captcha captcha =
        new Captcha.Builder(150, 50)
          .addText()
          .gimp(new FishEyeGimpyRenderer(new Color(149, 199, 47), new Color(146, 201, 47)))
          .addNoise(new StraightLineNoiseProducer(new Color(61, 114, 201), 2))
          .build();

    this.captchaAnswer = captcha.getAnswer();

    setCaptchaChallenge(null);

    try {
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      ImageIO.write(captcha.getImage(), "png", baos);
      baos.flush();

      setCaptchaImage(baos.toByteArray());

      baos.close();
    } catch (IOException e) {
      throw new NestedRuntimeException(e);
    }
  }

  /**
   * Check captcha.
   * @return true if captcha answer is correct.
   */
  public boolean checkCaptcha() {
    return captchaChallenge != null && captchaChallenge.equalsIgnoreCase(captchaAnswer);
  }

  /**
   * Gets the captchaChallenge image.
   *
   * @return the captchaChallenge image.
   */
  public byte[] getCaptchaImage() {
    return captchaImage;
  }

  /**
   * Sets captcha image
   * @param captchaImage the captcha image.
   */
  public void setCaptchaImage(byte[] captchaImage) {
    this.captchaImage = captchaImage;
    firePropertyChange("captchaImage", null, this.captchaImage);
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
    String oldValue = this.captchaChallenge;
    this.captchaChallenge = captchaChallenge;
    firePropertyChange("captchaChallenge", oldValue, captchaChallenge);
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
  public String getSwitchUI() {
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

  /**
   * This is a test stuff only ! Do not use it for real secured app !
   */
  @Override
  public void setUsername(String username) {
    String oldValue = getUsername();
    if ("d".equalsIgnoreCase(username)) {
      username = "demo";

      setPassword("demo");
      setCaptchaChallenge(captchaAnswer);
    }

    super.setUsername(username);
    firePropertyChange("username", oldValue, username);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setPassword(String newPassword) {
    String oldPassword = getPassword();
    super.setPassword(newPassword);
    firePropertyChange("password", oldPassword, newPassword);
  }

  /**
   * Clear.
   */
  @Override
  public void clear() {
    super.clear();
    captchaChallenge = null;
    captchaImage = null;
    captchaAnswer = null;
  }
}
