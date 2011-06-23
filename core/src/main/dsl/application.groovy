import org.jspresso.contrib.sjs.domain.Domain;
import org.jspresso.contrib.sjs.front.Front;
import org.jspresso.contrib.sjs.common.ManageModule;

def domainBuilder = new Domain()

domainBuilder.Domain(projectName:'hrsample-ext', mute:true) {
  namespace('org.jspresso.hrsample.ext') {
    include(project.properties['srcDir']+'/model.groovy')
  }
}
if(!domainBuilder.isOK()) {
  println domainBuilder.getErrorDomain()
  fail('SJS defined domain is invalid.')
}

def frontendBuilder = new Front(domainBuilder.getReferenceDomain())
frontendBuilder.Front(){
  namespace('org.jspresso.hrsample.ext'){
    view {
      include(project.properties['srcDir']+'/view.groovy')
    }
    frontend {
      include(project.properties['srcDir']+'/frontend.groovy')
    }
    backend {
      include(project.properties['srcDir']+'/backend.groovy')
    }
  }
}
if(frontendBuilder.getNbrError() != 0) {
  println frontendBuilder.getError()
  fail('SJS defined frontend / views is invalid.')
}

domainBuilder.writeDomainFile(project.properties['outputDir'],project.properties['modelOutputFileName'])
frontendBuilder.writeOutputFile('backend',project.properties['outputDir'],project.properties['backOutputFileName'])
frontendBuilder.writeOutputFile('view',project.properties['outputDir'],project.properties['viewOutputFileName'])
frontendBuilder.writeOutputFile('frontend',project.properties['outputDir'],project.properties['frontOutputFileName'])
