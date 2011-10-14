import org.jspresso.contrib.sjs.domain.Domain;
import org.jspresso.contrib.sjs.front.Front;
import org.jspresso.contrib.sjs.common.ManageModule;

ManageModule libraries = new ManageModule()
libraries.importModuleAsResource('hrsample')

def domainBuilder = new Domain(libraries)

domainBuilder.Domain(projectName:'hrsample-ext', mute:true, includeDirectory:project.properties['srcDir']) {
  namespace('org.jspresso.hrsample.ext') {
    include('model.groovy')
  }
}
if(!domainBuilder.isOK()) {
  println domainBuilder.getErrorDomain()
  fail('SJS defined domain is invalid.\n' + domainBuilder.getErrorDomain())
}

def frontendBuilder = new Front(domainBuilder.getReferenceDomain())
frontendBuilder.Front(){
  namespace('org.jspresso.hrsample.ext'){
    view {
      include('view.groovy')
    }
    frontend {
      include('frontend.groovy')
    }
    backend {
      include('backend.groovy')
    }
  }
}
if(frontendBuilder.getNbrError() != 0) {
  println frontendBuilder.getError()
  fail('SJS defined frontend / views is invalid:\n' + frontendBuilder.getError())
}

domainBuilder.writeDomainFile(project.properties['outputDir'],project.properties['modelOutputFileName'])
frontendBuilder.writeOutputFile('backend',project.properties['outputDir'],project.properties['backOutputFileName'])
frontendBuilder.writeOutputFile('view',project.properties['outputDir'],project.properties['viewOutputFileName'])
frontendBuilder.writeOutputFile('frontend',project.properties['outputDir'],project.properties['frontOutputFileName'])

frontendBuilder.writeOutputFile('remote',project.properties['outputDir'],'remote-'+project.properties['frontOutputFileName'])
frontendBuilder.writeOutputFile('remote-recording',project.properties['outputDir'],'remote-recording-'+project.properties['frontOutputFileName'])

//Export as module
ManageModule manageModule = new ManageModule()
manageModule.exportModule('hrsample-ext',domainBuilder,frontendBuilder,project.properties['outputDir'])
