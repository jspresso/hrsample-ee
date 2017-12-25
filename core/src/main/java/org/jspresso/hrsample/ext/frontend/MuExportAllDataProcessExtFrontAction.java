package org.jspresso.hrsample.ext.frontend;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.jspresso.contrib.usage.model.MUStat;
import org.jspresso.contrib.usage.model.ModuleUsage;
import org.jspresso.contrib.usage.frontend.MuExportAllDataProcessFrontAction;
import org.jspresso.framework.application.backend.persistence.hibernate.HibernateBackendController;
import org.jspresso.framework.application.backend.session.EMergeMode;
import org.jspresso.framework.application.model.BeanModule;
import org.jspresso.framework.application.model.Module;
import org.jspresso.framework.model.entity.IEntityFactory;
import org.jspresso.hrsample.ext.model.ModuleUsageExt;
import org.jspresso.hrsample.model.User;

public class MuExportAllDataProcessExtFrontAction<E, F, G> extends MuExportAllDataProcessFrontAction<E, F, G> {

    @Override
    public Iterator<?> getIterator(Map<String, Object> context) {

        final Iterator<ModuleUsage> iterator = (Iterator<ModuleUsage>) super.getIterator(context);
        final HibernateBackendController controller = (HibernateBackendController) getBackendController(context);
        final MUStat stat = (MUStat) ((BeanModule)getModule(context)).getModuleObject();

        return new ModuleUsageExtIterator(iterator, stat, controller);
    }

    /*****************************************************************
     * ModuleUsageExtIterator
     */
    private class ModuleUsageExtIterator implements Iterator<ModuleUsageExt> {

        private final Iterator<ModuleUsage> iterator;
        private MUStat stat;
        private final HibernateBackendController controller;
        private final Map<String, User> cache;

        public ModuleUsageExtIterator(Iterator<ModuleUsage> iterator, MUStat stat, HibernateBackendController controller) {
            this.iterator = iterator;
            this.stat = stat;
            this.controller = controller;
            this.cache = new HashMap<>();
        }

        @Override
        public boolean hasNext() {
            return iterator.hasNext();
        }

        @Override
        public ModuleUsageExt next() {

            ModuleUsage next = iterator.next();

            ModuleUsageExt ext = controller.getEntityFactory().createComponentInstance(ModuleUsageExt.class);
            ext.setModuleId(next.getModuleId());
            ext.setAccessDate(next.getAccessDate());
            ext.setAccessBy(next.getAccessBy());

            ext.setUser(getUser(next.getAccessBy(), controller));
            ext.setModule(stat.getModule(next.getModuleId()));
            ext.setWorkspace(stat.getWorkspaceForModule(next.getModuleId()));

            return ext;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("remove");
        }

        private User getUser(String login, HibernateBackendController controller) {

            if (login == null) {
                return null;
            }

            if (cache.containsKey(login)) {
                return cache.get(login);
            }

            DetachedCriteria dc = DetachedCriteria.forClass(User.class);
            dc.add(Restrictions.eq(User.LOGIN, login));

            User user = controller.findFirstByCriteria(dc, EMergeMode.MERGE_KEEP, User.class);
            cache.put(login, user);

            return user;
        }
    }
}
