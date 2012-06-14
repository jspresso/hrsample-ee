package org.jspresso.hrsample.ext.frontend.usage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.jspresso.framework.action.IActionHandler;
import org.jspresso.framework.application.frontend.IFrontendController;
import org.jspresso.framework.application.frontend.action.FrontendAction;
import org.jspresso.framework.application.model.BeanModule;
import org.jspresso.framework.application.model.Module;
import org.jspresso.framework.application.model.Workspace;
import org.jspresso.framework.model.entity.IEntityFactory;
import org.jspresso.hrsample.ext.model.usage.MUItem;
import org.jspresso.hrsample.ext.model.usage.MUModule;
import org.jspresso.hrsample.ext.model.usage.MUModuleInterface;
import org.jspresso.hrsample.ext.model.usage.MUStat;
import org.jspresso.hrsample.ext.model.usage.MUWorkspace;

public class UsageModuleEntryAction<E, F, G> extends FrontendAction<E, F, G> {

  @Override
  public boolean execute(IActionHandler actionHandler, Map<String, Object> context) {

    // prepare workspaces 
    IFrontendController<E, F, G> controller = getFrontendController(context);
    ArrayList<MUWorkspace> workspaces = new ArrayList<MUWorkspace>();
    for (String workspaceName : controller.getWorkspaceNames()) {
      Workspace w = controller.getWorkspace(workspaceName);
      MUWorkspace mw = createMUWorkspace(w, getBackendController(context).getEntityFactory());
      workspaces.add(mw);
    }

    // create stat
    MUStat stat = getBackendController(context).getEntityFactory().createComponentInstance(MUStat.class);
    stat.setAllWorkspaces(workspaces); 
    if (stat.getAllModules().isEmpty()) {
      stat.setHistoryModule(stat.getAllModules().get(0));
    }
    
    // mock other datas
    //TODO Remove this !
    //mockDatas(stat, getBackendController(context).getEntityFactory());

    // init module
    BeanModule statisticsModule = (BeanModule)getModule(context);
    statisticsModule.setModuleObject(stat);

    return super.execute(actionHandler, context);
  }

  /**
   * @param workspace
   * @param context
   * @return
   */
  private MUWorkspace createMUWorkspace(Workspace workspace, IEntityFactory entityFactory) {
    MUWorkspace w = entityFactory.createComponentInstance(MUWorkspace.class);
    w.setWorkspaceId(workspace.getName());
    w.setLabel(workspace.getI18nName());
    
    List<Module> modules = workspace.getModules();
    if (modules!=null) {
      for (Module subModule : modules) {
        MUModule m = createMUModule(w, subModule, entityFactory);
        w.addToModules(m);
      }
    }
    
    return w;
  }

  
  private MUModule createMUModule(MUModuleInterface masterMUModule, Module masterModule, IEntityFactory entityFactory) {
    MUModule m = entityFactory.createComponentInstance(MUModule.class);
    m.setModuleId(masterModule.getName());
    m.setLabel(masterModule.getI18nName());
    
    List<Module> subModules = masterModule.getSubModules();
    if (subModules!=null) {
      for (Module subModule : subModules) {
        MUModule mm = createMUModule(m, subModule, entityFactory);
        m.addToModules(mm);
      }
    }
    
    return m;
  }

  /**
   * mock data 
   * TODO REMOVE THIS !
   * @param stat
   * @param entityFactory
   */
  private boolean mockLoaded = false;
  public void mockDatas(MUStat stat, IEntityFactory entityFactory) {
    if (mockLoaded) {
      return;
    }

    // users count
//    for (MUModule m : stat.getAllModules()) {
//      int rand = new Random().nextInt(150);
//      stat.addToUsersPerModule(getItem(m.getLabel(), rand, entityFactory));
//    }
//    stat.setUsersCount(new Random().nextInt(40));

    // users count
//    int accessCount = 0;
//    for (MUModule m : stat.getAllModules()) {
//      int rand = new Random().nextInt(40);
//      stat.addToAccessPerModule(getItem(m.getLabel(), rand, entityFactory));
//      accessCount += rand;
//    }
//    stat.setAccessCount(accessCount);

    // history
//    stat.addToHistoryDetails(getItem("Lun", 13, entityFactory));
//    stat.addToHistoryDetails(getItem("Mar", 19, entityFactory));
//    stat.addToHistoryDetails(getItem("Mer", 8, entityFactory));
//    stat.addToHistoryDetails(getItem("Jeu", 23, entityFactory));
//    stat.addToHistoryDetails(getItem("Ven", 35, entityFactory));
//    stat.addToHistoryDetails(getItem("Sam", 2, entityFactory));
//    stat.addToHistoryDetails(getItem("Dim", 1, entityFactory));

    mockLoaded = true;
  }

  /**
   * mock to get a new MUItem 
   * TODO REMOVE THIS !
   * @param label
   * @param count
   * @param entityFactory
   */
  public MUItem getItem(String label, int count, IEntityFactory entityFactory) {
    MUItem item = entityFactory.createComponentInstance(MUItem.class);
    item.setCount(count);
    item.setLabel(label);
    return item;
  }


}
