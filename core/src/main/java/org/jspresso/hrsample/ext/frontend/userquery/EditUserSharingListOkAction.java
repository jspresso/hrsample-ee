package org.jspresso.hrsample.ext.frontend.userquery;

import org.jspresso.framework.action.IActionHandler;
import org.jspresso.framework.application.frontend.action.FrontendAction;
import org.jspresso.hrsample.ext.model.userquery.UserSharingList;

import java.util.Map;

/**
 * EditUserSharingListOkAction
 * User: Maxime HAMM
 * Date: 30/11/2017
 */
public class EditUserSharingListOkAction<E, F, G> extends FrontendAction<E, F, G> {

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean execute(IActionHandler actionHandler, Map<String, Object> context) {

        Object model = getModel(context);
        if (model instanceof UserSharingList) {

            UserSharingList sharingList = (UserSharingList) model;

            if (sharingList.getQuery().isMine())
                sharingList.shareWithUserList();
        }

        return super.execute(actionHandler, context);
    }
}
