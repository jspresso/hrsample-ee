package org.jspresso.hrsample.ext.frontend

import org.hibernate.criterion.DetachedCriteria
import org.hibernate.criterion.Restrictions
import org.jspresso.contrib.frontend.ImportBoxFrontAction
import org.jspresso.contrib.model.ModuleUtils
import org.jspresso.contrib.test.frontend.FrontendTestHelper
import org.jspresso.contrib.tmar.core.Tmar4JUnit
import org.jspresso.framework.action.IAction
import org.jspresso.framework.application.backend.session.EMergeMode
import org.jspresso.framework.application.frontend.IFrontendController
import org.jspresso.framework.application.model.FilterableBeanCollectionModule
import org.jspresso.framework.application.model.Module
import org.jspresso.framework.gui.remote.RAction
import org.jspresso.framework.gui.remote.RActionEvent
import org.jspresso.framework.gui.remote.RComboBox
import org.jspresso.framework.gui.remote.RComponent
import org.jspresso.framework.gui.remote.RIcon
import org.jspresso.framework.model.entity.IEntityFactory
import org.jspresso.framework.model.persistence.hibernate.criterion.EnhancedDetachedCriteria
import org.jspresso.framework.util.remote.registry.IRemotePeerRegistry
import org.jspresso.framework.util.spring.ThisApplicationContextFactoryBean
import org.jspresso.framework.view.IView
import org.jspresso.framework.view.descriptor.IPropertyViewDescriptor
import org.jspresso.framework.view.remote.AbstractRemoteViewFactory
import org.jspresso.hrsample.ext.backend.EmployeeTests
import org.jspresso.hrsample.ext.backend.TmarBackendStartup
import org.jspresso.hrsample.model.Employee
import org.junit.AfterClass
import org.junit.BeforeClass
import org.junit.Test
import org.springframework.beans.factory.FactoryBean
import org.springframework.context.ApplicationContext

class ImportEmployees extends TmarFrontendStartup {

    @Test
    void test() {

        eachIteration('test') { tmar ->

            // import csv
            if (tmar.importFile != null) {
                importFile(tmar.importFile)
            }

            // Check if employee exists
            Employee employee = findEmployee(tmar.employeeName, tmar.employeeFirstName);
            tmar.employeeExists = employee!=null


        }
    }

    private void importFile(String fileContent) {

        // clean up blank at begining
        StringBuilder sb = new StringBuilder();
        for (String line : fileContent.split("\\n")) {
            sb.append(line.trim());
        }
        fileContent = sb.toString();

        // find module
        FrontendTestHelper helper = new FrontendTestHelper();
        helper.originWorkspaceId = "tools.workspace";
        helper.originModuleId = "Employee.test.module";
        helper.originPermId = "Employee.test.view--table--name";

        IFrontendController<RComponent, RIcon, RAction> frontendController = getFrontendController();
        FactoryBean<ApplicationContext> refContext = getApplicationContext().getBean(ThisApplicationContextFactoryBean.class);
        ApplicationContext context;
        try {
          context = refContext.getObject();
        } catch (Exception e) {
          throw new RuntimeException(e);
        }
        Module module = ModuleUtils.findModule("tools.workspace", "Employee.test.module", FilterableBeanCollectionModule.class, context);

        // navigate to test filter module
        frontendController.displayWorkspace(helper.originWorkspaceId);
        frontendController.displayModule(module);

        // put focus to table action
        IView<RComponent> currentModuleView = frontendController.getCurrentModuleView();
        FrontendTestHelper.FocusedComponent focused = helper.findComponent(currentModuleView, helper.originPermId);
        frontendController.focus((RComponent) focused.peer);

        // open import popup
        ImportBoxFrontAction importAction = getApplicationContext().getBean("importEmployeesBoxAction");
        focused = helper.findComponent(currentModuleView, "importEmployeesBoxAction");
        helper.executeAction(focused.peer, focused, frontendController);

        // adapt import to use string content instead of file content
        IAction importOkAction = getApplicationContext().getBean("importEmployeeBoxOkAction");
        currentModuleView = frontendController.getInitialActionContext().get("DIALOG_VIEW")
//        focused = helper.findComponent(currentModuleView, "importEmployeesBoxAction");
//        helper.executeAction(importOkAction, focused, frontendController);

        IRemotePeerRegistry remotePeerRegistry = (IRemotePeerRegistry)frontendController;
        RAction raction = (RAction) remotePeerRegistry.getRegisteredForPermId(importOkAction.getPermId());

        raction.actionPerformed(new RActionEvent(), null);

        // do import
        // raise action
//        focused = helper.findComponent(currentModuleView, "importEmployeesBoxAction");
//        helper.executeAction((RAction)focused.peer, focused, frontendController);

//        getFrontendController().execute(importAction, getFrontendController().getInitialActionContext());

    }
    private Employee findEmployee(String name, String firstName) {

        EnhancedDetachedCriteria criteria = EnhancedDetachedCriteria.forClass(Employee.class);
        criteria.add(Restrictions.eq(Employee.FIRST_NAME, firstName))
        criteria.add(Restrictions.eq(Employee.NAME, name))

        Employee employee = getBackendController().findFirstByCriteria(criteria, EMergeMode.MERGE_KEEP, Employee.class);

        return employee;
    }
}