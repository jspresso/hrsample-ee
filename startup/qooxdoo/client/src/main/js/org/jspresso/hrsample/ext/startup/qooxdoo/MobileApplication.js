/* ************************************************************************

   Copyright:

   License:

   Authors:

************************************************************************ */

/**
 * This is the main application class
 */
qx.Class.define("org.jspresso.hrsample.ext.startup.qooxdoo.MobileApplication",
{
  extend : org.jspresso.framework.ext.application.frontend.MobileEnhancedApplication,



  /*
  *****************************************************************************
     MEMBERS
  *****************************************************************************
  */

  members :
  {
    start : function() {
      var remoteController;
      if (qx.core.Environment.get("qx.debug")) {
        remoteController = new qx.io.remote.Rpc(
            "http://localhost:8080/hrsample-ext-webapp/.qxrpc",
            "org.jspresso.hrsample.ext.startup.remote.RemoteMobileApplicationStartup"
        );
        remoteController.setCrossDomain(true);
      } else {
        remoteController = new qx.io.remote.Rpc(
            qx.io.remote.Rpc.makeServerURL(),
            "org.jspresso.hrsample.ext.startup.remote.RemoteMobileApplicationStartup"
        );
      }
      remoteController.setTimeout(600000);
      
      this.startController(remoteController);
    }
  }
});
