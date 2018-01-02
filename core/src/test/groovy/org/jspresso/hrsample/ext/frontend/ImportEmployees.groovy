package org.jspresso.hrsample.ext.frontend

import org.hibernate.criterion.Restrictions
import org.jspresso.contrib.frontend.ImportBoxOkFrontAction
import org.jspresso.contrib.frontend.ImportEntitiesFactoryBean
import org.jspresso.contrib.model.ModuleUtils
import org.jspresso.contrib.test.frontend.FrontendTestHelper
import org.jspresso.framework.action.IAction
import org.jspresso.framework.action.IActionHandler
import org.jspresso.framework.application.backend.session.EMergeMode
import org.jspresso.framework.application.frontend.IFrontendController
import org.jspresso.framework.application.frontend.action.FrontendAction
import org.jspresso.framework.application.model.FilterableBeanCollectionModule
import org.jspresso.framework.application.model.Module
import org.jspresso.framework.gui.remote.RAction
import org.jspresso.framework.gui.remote.RActionEvent
import org.jspresso.framework.gui.remote.RComponent
import org.jspresso.framework.gui.remote.RIcon
import org.jspresso.framework.model.persistence.hibernate.criterion.EnhancedDetachedCriteria
import org.jspresso.framework.util.remote.registry.IRemotePeerRegistry
import org.jspresso.framework.util.spring.ThisApplicationContextFactoryBean
import org.jspresso.framework.view.IView
import org.jspresso.hrsample.model.ContactInfo
import org.jspresso.hrsample.model.Employee
import org.jspresso.hrsample.model.Team
import org.junit.Test
import org.springframework.beans.factory.FactoryBean
import org.springframework.context.ApplicationContext

import java.text.SimpleDateFormat

class ImportEmployees extends TmarFrontendStartup {

    @Test
    void test() {

        eachIterationWithIndex('test') { tmar, iterationNumber ->

            // Create master data
            if (iterationNumber == 1) {
                createEntities(
                        [Company: tmar.table.company,
                         Department: tmar.table.department,
                         Team: tmar.table.team])

            }

            // import csv
            if (tmar.importFile != null) {
                importFile(tmar.importFile)
            }

            // Check if employee exists
            Employee employee = findEmployee(tmar.employeeName, tmar.employeeFirstName);
            tmar.employeeExists = employee!=null

            // Check employee properties
            if (employee!=null) {

                tmar.employeeGender = "M".equals(employee.getGender()) ? "Male" : "Female"
                tmar.employeeBirthdate = formatDate(employee.getBirthDate())
                tmar.employeeAddress = formatContact(employee.getContact())
                tmar.employeeTeams = formatTeams(employee.getTeams())
            }
        }
    }



    private void importFile(String fileContent) {

        // clean up blank at begining
        StringBuilder sb = new StringBuilder();
        for (String line : fileContent.split("\\n")) {
            if (sb.size()>0)
                sb.append('\n');
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
        focused = helper.findComponent(currentModuleView, "importEmployeesBoxAction");
        helper.executeAction(focused.peer, focused, frontendController);

        // trick the 'ok' button
        ImportBoxOkFrontAction importOkAction = getApplicationContext().getBean("importEmployeeBoxOkAction");
        ImportEntitiesFactoryBean openCallback = importOkAction.getNextAction(null).fileOpenCallbackAction.fileOpenCallback
        IAction tricky = new FrontendAction() {
            @Override
            boolean execute(IActionHandler actionHandler, Map<String, Object> ctxt) {

                InputStream inputStream = new ByteArrayInputStream(fileContent.getBytes("UTF8"))
                openCallback.fileChosen("Nop", inputStream, actionHandler, ctxt)
            }
        }
        importOkAction.setNextAction(tricky);

        // click to 'ok' button
        IRemotePeerRegistry remotePeerRegistry = (IRemotePeerRegistry)frontendController;
        RAction raction = (RAction) remotePeerRegistry.getRegisteredForPermId(importOkAction.getPermId());

        raction.actionPerformed(new RActionEvent(), null);

        // click to 'save' button
        focused = helper.findComponent(currentModuleView, "Employee.test.view--saveModuleObjectFrontAction");
        helper.executeAction(focused.peer, focused, frontendController);

    }

    private Employee findEmployee(String name, String firstName) {

        EnhancedDetachedCriteria criteria = EnhancedDetachedCriteria.forClass(Employee.class);
        criteria.add(Restrictions.eq(Employee.FIRST_NAME, firstName))
        criteria.add(Restrictions.eq(Employee.NAME, name))

        Employee employee = getBackendController().findFirstByCriteria(criteria, EMergeMode.MERGE_KEEP, Employee.class);

        return employee;
    }

    private static String formatDate(Date date) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/DD");
        return df.format(date)
    }

    private static String formatContact(ContactInfo contact) {
        StringBuilder sb = new StringBuilder()
        sb.append(contact.city).append(" - ")
          .append(contact.address).append(" - ")
          .append(contact.city.zip).append(" - ")
          .append(contact.phone)
        return sb.toString()
    }

    private static String formatTeams(Set<Team> teams) {
        StringBuilder sb = new StringBuilder()
        for (Team team : teams) {
            if (sb.size()>0)
                sb.append(', ')
            sb.append(team.name)
        }
        return sb.toString()
    }

}