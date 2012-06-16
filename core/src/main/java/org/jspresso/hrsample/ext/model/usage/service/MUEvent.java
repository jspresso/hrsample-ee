package org.jspresso.hrsample.ext.model.usage.service;

public class MUEvent {

  public static enum EMUEvent {
    INIT, PERIOD, WORKSPACE, HISTORY_MODULE
  }
  
  public EMUEvent type;

  public MUEvent(EMUEvent type) {
    this.type = type;
  }


}
