/* ************************************************************************

   Copyright:

   License:

   Authors:

************************************************************************ */

/**
 * This is the main application class
 */
/**
 * @asset(tinymce/*)
 */
qx.Class.define("org.jspresso.hrsample.ext.startup.qooxdoo.Application",
{
  extend : org.jspresso.framework.ext.application.frontend.EnhancedApplication,



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
        remoteController = new org.jspresso.framework.io.Rpc(
            "http://localhost:9080/hrsample-ext-webapp/.qxrpc",
            "org.jspresso.hrsample.ext.startup.remote.RemoteApplicationStartup"
        );
        //remoteController.setCrossDomain(true);
      } else {
        remoteController = new org.jspresso.framework.io.Rpc(
            org.jspresso.framework.io.Rpc.makeServerURL(),
            "org.jspresso.hrsample.ext.startup.remote.RemoteApplicationStartup"
        );
      }
      remoteController.setTimeout(600000);

      this.startController(remoteController);
    }
  }
});
