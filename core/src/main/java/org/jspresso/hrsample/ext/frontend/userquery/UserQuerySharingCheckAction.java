package org.jspresso.hrsample.ext.frontend.userquery;

import org.jspresso.contrib.model.query.UserQuery;
import org.jspresso.framework.action.IActionHandler;
import org.jspresso.framework.application.frontend.action.FrontendAction;

import java.util.Map;

/**
 * UserQuerySharingCheckAction
 * User: Maxime HAMM
 * Date: 30/11/2017
 */
public class UserQuerySharingCheckAction<E, F, G> extends FrontendAction<E, F, G> {

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean execute(IActionHandler actionHandler, Map<String, Object> context) {

        UserQuery query = getSelectedModel(context);
        return !query.isShared() || super.execute(actionHandler, context);

    }
}
