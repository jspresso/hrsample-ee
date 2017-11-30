package org.jspresso.hrsample.ext.frontend.userquery;

import org.jspresso.contrib.model.query.UserQuery;
import org.jspresso.framework.action.IActionHandler;
import org.jspresso.framework.application.frontend.action.std.EditComponentAction;
import org.jspresso.hrsample.ext.model.userquery.UserSharingList;

import java.util.Map;

/**
 * EditUserSharingListAction
 * User: Maxime HAMM
 * Date: 30/11/2017
 */
public class EditUserSharingListAction<E, F, G> extends EditComponentAction<E, F, G> {

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean execute(IActionHandler actionHandler, Map<String, Object> context) {

        UserQuery query = getSelectedModel(context);

        UserSharingList sharingList = getBackendController(context).getEntityFactory().createComponentInstance(UserSharingList.class);
        sharingList.setQuery(query);
        setActionParameter(sharingList, context);

        return super.execute(actionHandler, context);
    }
}
