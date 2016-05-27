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
package org.jspresso.hrsample.ext.frontend;

import org.jspresso.contrib.frontend.NavigateToModuleFrontAction;
import org.jspresso.contrib.model.ModuleUtils;
import org.jspresso.contrib.test.frontend.FrontendTestHelper;
import org.jspresso.contrib.test.frontend.FrontendTestHelper.FocusedComponent;
import org.jspresso.framework.action.IAction;
import org.jspresso.framework.application.frontend.IFrontendController;
import org.jspresso.framework.application.model.FilterableBeanCollectionModule;
import org.jspresso.framework.application.model.Module;
import org.jspresso.framework.gui.remote.RAction;
import org.jspresso.framework.gui.remote.RActionField;
import org.jspresso.framework.gui.remote.RActionList;
import org.jspresso.framework.gui.remote.RComponent;
import org.jspresso.framework.gui.remote.RIcon;
import org.jspresso.framework.gui.remote.RLink;
import org.jspresso.framework.gui.remote.RTextField;
import org.jspresso.framework.util.spring.ThisApplicationContextFactoryBean;
import org.jspresso.framework.view.IView;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.context.ApplicationContext;

/** 
 * Test navigation from property view to module using
 * action {@link NavigateToModuleFrontAction}.
 * 
 * NB : To display the UI used by this tests, add "test" role :
 * 
 *   hrsample-ext {
 *     org.jspresso.framework.security.auth.spi.DevelopmentLoginModule required
 *        user_1=demo
 *        password_1=demo
 *        roles_1="administrator,test"
 *        custom.language_1=en
 *
 * @author Maxime HAMM
 */
public class JspressoNavigationToModuleTest extends FrontTestStartup {

  private static final String NAVIGATE_TO_MODULE_ACTION = "navigateToModuleFrontAction";
  private static final String QUERY_ACTION = "queryModuleFilterAction";
  
  /**
   * Test 
   */
  @Test 
  public void testNavigateFromFormUsingScalarProperty() {

    FrontendTestHelper context = new FrontendTestHelper();
    context.originWorkspaceId = "tools.workspace";
    context.originModuleId = "Employee.test.module";
    context.originPermId = "Employee.test.view--east--name";
    
    doNavigationModuleAction(context, "employees.module", "Berlutti Graziella");

  }
  
  /**
   * Test 
   */
  @Test 
  public void testNavigateFromFormUsingDotsToReferenceProperty() {

    FrontendTestHelper context = new FrontendTestHelper();
    context.originWorkspaceId = "tools.workspace";
    context.originModuleId = "Employee.test.module";
    context.originPermId = "Employee.test.view--east--company.contact.city";
    
    doNavigationModuleAction(context, "masterdata.cities.module", "Paris I");

  }
  
  /**
   * Test 
   */
  @Test 
  public void testNavigateFromFormUsingDotsToScalarProperty() {

    FrontendTestHelper context = new FrontendTestHelper();
    context.originWorkspaceId = "tools.workspace";
    context.originModuleId = "Employee.test.module";
    context.originPermId = "Employee.test.view--east--company.contact.city.name";
    
    doNavigationModuleAction(context, "masterdata.cities.module", "Paris I");

  }
  
  /**
   * Test 
   */
  @Test 
  public void testNavigateFromFormUsingScalarPropertyActionMap() {

    FrontendTestHelper context = new FrontendTestHelper();
    context.originWorkspaceId = "tools.workspace";
    context.originModuleId = "Employee.test.module";
    context.originPermId = "Employee.test.view--east--name.actionMap";
    
    doNavigationModuleAction(context, "employees.module", "Berlutti Graziella");

  }
  
  /**
   * Test 
   */
  @Test 
  public void testNavigateFromFormUsingReferenceProperty() {

    FrontendTestHelper helper = new FrontendTestHelper();
    helper.originWorkspaceId = "tools.workspace";
    helper.originModuleId = "Employee.test.module";
    helper.originPermId = "Employee.test.view--east--company";
    
    doNavigationModuleAction(helper, "companies.module", "Design2See");

  }
  
  /**
   * Test 
   */
  @Test 
  public void testNavigateFromFormUsingReferencePropertyActionMap() {

    FrontendTestHelper helper = new FrontendTestHelper();
    helper.originWorkspaceId = "tools.workspace";
    helper.originModuleId = "Employee.test.module";
    helper.originPermId = "Employee.test.view--east--company.actionMap";
    
    doNavigationModuleAction(helper, "companies.module", "Design2See");

  }
  
  /**
   * Test 
   */
  @Test 
  public void testNavigateFromFormUsingActionMap() {

    FrontendTestHelper helper = new FrontendTestHelper();
    helper.originWorkspaceId = "tools.workspace";
    helper.originModuleId = "Employee.test.module";
    helper.originPermId = "Employee.test.view--east--actionMap";
    
    doNavigationModuleAction(helper, "employees.module", "Berlutti Graziella");
  }

  /**
   * Test 
   */
  @Test 
  public void testNavigateFromTableUsingScalarProperty() {

    FrontendTestHelper helper = new FrontendTestHelper();
    helper.originWorkspaceId = "tools.workspace";
    helper.originModuleId = "Employee.test.module";
    helper.originPermId = "Employee.test.view--table--name";
    helper.originComponentToString = "Den Mike"; 
    
    doNavigationModuleAction(helper, "employees.module", "Den Mike");

  }
  
  /**
   * Test 
   */
  @Test 
  public void testNavigateFromTableUsingScalarPropertyActionMap() {

    FrontendTestHelper helper = new FrontendTestHelper();
    helper.originWorkspaceId = "tools.workspace";
    helper.originModuleId = "Employee.test.module";
    helper.originPermId = "Employee.test.view--table--name.actionMap";
    helper.originComponentToString = "Den Mike"; 
    
    doNavigationModuleAction(helper, "employees.module", "Den Mike");

  }
  
  /**
   * Test 
   */
  @Test 
  public void testNavigateFromTableUsingScalarProperty2() {

    FrontendTestHelper helper = new FrontendTestHelper();
    helper.originWorkspaceId = "tools.workspace";
    helper.originModuleId = "Employee.test.module";
    
    doLoadModule(helper);
    
    helper.originPermId = "Employee.test.view--table--name";
    helper.originComponentToString = "Demo Demo"; 
    doSetFocus(helper);
    
    helper.originPermId = "Employee.test.view--east-table--login";
    helper.originComponentToString = "demo"; 
    doSetFocus(helper);
    
    doNavigateAndCheck(helper, "users.admin.module", "demo");

  }
  
  /**
   * Test 
   */
  @Test 
  public void testNavigateFromTableUsingReferenceProperty() {

    FrontendTestHelper helper = new FrontendTestHelper();
    helper.originWorkspaceId = "tools.workspace";
    helper.originModuleId = "Employee.test.module";
    helper.originPermId = "Employee.test.view--table--company";
    helper.originComponentToString = "Den Mike"; 
    
    doNavigationModuleAction(helper, "companies.module", "Design2See");

  }
 
  /**
   * Test 
   */
  @Test 
  public void testNavigateFromTableUsingReferencePropertyActionMap() {

    FrontendTestHelper helper = new FrontendTestHelper();
    helper.originWorkspaceId = "tools.workspace";
    helper.originModuleId = "Employee.test.module";
    helper.originPermId = "Employee.test.view--table--company.actionMap";
    helper.originComponentToString = "Den Mike"; 
    
    doNavigationModuleAction(helper, "companies.module", "Design2See");

  }
  
  /**
   * Test 
   */
  @Test 
  public void testNavigateFromTableUsingActionMap() {

    FrontendTestHelper helper = new FrontendTestHelper();
    helper.originWorkspaceId = "tools.workspace";
    helper.originModuleId = "Employee.test.module";
    helper.originPermId = "Employee.test.view--table--actionMap";
    
    helper.originComponentToString = "Den Mike"; 
    
    doNavigationModuleAction(helper, "employees.module", "Den Mike");
  }

  /**
   * Test 
   */
  @Test 
  public void testNavigateFromTableUsingActionMap2() {

    FrontendTestHelper helper = new FrontendTestHelper();
    helper.originWorkspaceId = "tools.workspace";
    helper.originModuleId = "Employee.test.module";
    
    doLoadModule(helper);
    
    helper.originPermId = "Employee.test.view--table--name";
    helper.originComponentToString = "Demo Demo"; 
    doSetFocus(helper);
    
    helper.originPermId = "Employee.test.view--east-table--login";
    helper.originComponentToString = "demo"; 
    doSetFocus(helper);
    
    helper.originPermId = "Employee.test.view--east-table--actionMap";
    doSetFocus(helper);
    
    doNavigateAndCheck(helper, "users.admin.module", "demo");

  }
  
  /**
   * Test 
   */
  @Test 
  public void testNavigateFromTableUsingDotsAndActionMap() {

    FrontendTestHelper helper = new FrontendTestHelper();
    helper.originWorkspaceId = "tools.workspace";
    helper.originModuleId = "Employee.test.module";
    
    doLoadModule(helper);
    
    helper.originPermId = "Employee.test.view--table--name";
    helper.originComponentToString = "Den Mike"; 
    doSetFocus(helper);
    
    helper.originPermId = "Employee.test.view--east-table--mainRole.roleId";
    helper.originComponentToString = "denpass"; 
    doSetFocus(helper);
    
    doNavigateAndCheck(helper, "roles.admin.module", "employee");

  }
  
  //
  
  /**
   * Test 
   */
  @Test 
  public void testNavigateFromTableUsingActionMapNotCollectionBased() {

    FrontendTestHelper helper = new FrontendTestHelper();
    helper.originWorkspaceId = "tools.workspace";
    helper.originModuleId = "Employee.test.module";
    
    doLoadModule(helper);
    
    helper.originPermId = "Employee.test.view--table--name";
    helper.originComponentToString = "Demo Demo"; 
    doSetFocus(helper);
    
    helper.originPermId = "Employee.test.view--east-table--login";
    helper.originComponentToString = "demo"; 
    doSetFocus(helper);
    
    helper.originPermId = "Employee.test.view--east-table--actionMap.notCollectionBased";
    doSetFocus(helper);
    
    doNavigateAndCheck(helper, "employees.module", "Demo Demo");

  }
  
  /**
   * Test navigation to module action 
   * @param helper The test context
   */
  protected void doLoadModule(
      FrontendTestHelper helper) {
    
    // load app'
    IFrontendController<RComponent, RIcon, RAction> frontendController = getFrontendController();
    
    // find test module 
    FactoryBean<ApplicationContext> refContext = getApplicationContext().getBean(ThisApplicationContextFactoryBean.class);
    ApplicationContext context;
    try {
      context = refContext.getObject();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
        
    Module module = ModuleUtils.findModule(helper.originWorkspaceId, helper.originModuleId, FilterableBeanCollectionModule.class, context);
      //navAction.getApplicationContext());
    
    // navigate to test filter module
    frontendController.displayWorkspace(helper.originWorkspaceId);
    frontendController.displayModule(module);
    
    // load the filter module
    IAction queryAction = (IAction) getApplicationContext().getBean(QUERY_ACTION);
    getFrontendController().execute(queryAction, getFrontendController().getInitialActionContext());
  
  }
  
  
  
  
  /**
   * Test navigation to module action 
   * @param helper The test context
   * @param expectedTargetParentModule The expected target parent module name
   * @param expectedTargetModuleI18nName The expected module name
   */
  protected void doNavigationModuleAction(
      FrontendTestHelper helper, 
      String expectedTargetParentModule, 
      String expectedTargetModuleI18nName) {
    
    // load app'
    doLoadModule(helper);
    
    // set focus to tested field
    doSetFocus(helper);
    
    // simulate navigation
    doNavigateAndCheck(helper, expectedTargetParentModule, expectedTargetModuleI18nName);
  }

  /**
   * doNavigateAndCheck
   * @param helper
   * @param expectedTargetParentModule
   * @param expectedTargetModuleI18nName
   */
  protected void doNavigateAndCheck(
      FrontendTestHelper helper, 
      String expectedTargetParentModule, 
      String expectedTargetModuleI18nName) {
        IFrontendController<RComponent, RIcon, RAction> frontendController = getFrontendController();
    
    FocusedComponent focused = helper.focused;
    if (focused.peer instanceof RAction) {
      helper.executeAction((RAction) focused.peer, focused, frontendController);
    }
    else {  
      RAction raction = null;
      if (focused.peer instanceof RAction) {
        raction = (RAction)focused.peer;
      }
      else if (focused.peer instanceof RLink) {
        raction = ((RLink) focused.peer).getAction();
      }
      else if (focused.peer instanceof RTextField) {
        RActionList[] actionLists = ((RTextField) focused.peer).getActionLists();
        raction = findNavigationAction(actionLists);
      }
      else if (focused.peer instanceof RActionField) {
        RActionList[] actionLists = ((RActionField) focused.peer).getActionLists();
        raction = findNavigationAction(actionLists);
      }
      else {
        throw new RuntimeException("Navigate action not found !");
      }
      
      helper.executeAction(raction, focused, frontendController);
    }
    
    // Assert module is the expected one !
    Module targetModule = frontendController.getSelectedModule();
    
    Assert.assertNotNull("Target module is null !", targetModule);
    Assert.assertNotNull("Target module's parent is null !", targetModule.getParent());
    
    Assert.assertEquals(expectedTargetParentModule, targetModule.getParent().getName());
    Assert.assertEquals(expectedTargetModuleI18nName, targetModule.getI18nName());
  }

  private RAction findNavigationAction(RActionList[] actionLists) {
    for (RActionList l : actionLists) {
      RAction[] actions = l.getActions();
      if (actions != null) {
        for (RAction a : l.getActions()) {
          if (a.getPermId().startsWith(NAVIGATE_TO_MODULE_ACTION))
            return a;
        }
      }
    }
    return null;
  }

  /**
   * doSetFocus
   * @param helper
   * @return focused object
   */
  protected FocusedComponent doSetFocus(FrontendTestHelper helper) {
    
    IFrontendController<RComponent, RIcon, RAction> frontendController = getFrontendController();
    
    IView<RComponent> currentModuleView = frontendController.getCurrentModuleView();
    FocusedComponent focused = helper.findComponent(currentModuleView, helper.originPermId);
    
    Assert.assertNotNull("focused is null !", focused);
    if (focused.peer instanceof RComponent) {
      frontendController.focus((RComponent) focused.peer);
    }
    
    // select table line 
    if (helper.originComponentToString !=null) {
      helper.selectTableLine(focused);
    }
    
    helper.focused = focused;
    
    return focused;
  }
  
}
