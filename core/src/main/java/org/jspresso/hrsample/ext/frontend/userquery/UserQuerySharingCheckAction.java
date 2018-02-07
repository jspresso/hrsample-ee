package org.jspresso.hrsample.ext.frontend.userquery;

import org.jspresso.contrib.model.query.UserQuery;
import org.jspresso.framework.action.IActionHandler;
import org.jspresso.framework.application.frontend.IFrontendController;
import org.jspresso.framework.application.frontend.action.FrontendAction;
import org.jspresso.hrsample.ext.model.userquery.UserSharingList;
import org.jspresso.hrsample.ext.model.userquery.service.UserSharingListService;

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

        final UserQuery query = getSelectedModel(context);
        final UserSharingList sharingList = getBackendController(context).getEntityFactory().createComponentInstance(UserSharingList.class);
        sharingList.setQuery(query);
        IFrontendController<Object, Object, Object> controller = getFrontendController(context);

        //
        // Gets previous sharing string
        final String previousSharedString;
        Map<String, Object> dirtyProperties = getBackendController(context).getDirtyProperties(query, false);
        if (dirtyProperties != null) {
            previousSharedString = (String) dirtyProperties.get(UserQuery.SHARED_STRING);
        }
        else {
            previousSharedString = null;
        }

        //
        // If not yet shared, simply share with all
        boolean clickToShare = getActionParameter(context);
        if (! clickToShare) {

            sharingList.shareWithAll();

            controller.popupInfo(
                    getSourceComponent(context),
                    controller.getTranslation("editUserSharing.message.sharedAll.title", controller.getLocale()),
                    null,//getIconImageURL(),
                    controller.getTranslation("editUserSharing.message.sharedAll.message", controller.getLocale()));

            return false;
        }

        //
        // If already shared with all, simply stop share
        if (UserSharingListService.ALL.equals(previousSharedString)) {

            sharingList.stopSharing();
            return false;
        }

        //
        // Query is shared with a list of people... ask confirmation to stop sharing
        if (previousSharedString != null) {
             controller.popupYesNo(
                    getSourceComponent(context),
                    controller.getTranslation("editUserSharing.confirm.unshare.title", controller.getLocale()),
                    null,//getIconImageURL(),
                    controller.getTranslation("editUserSharing.confirm.unshare.question", controller.getLocale()),
                    new FrontendAction<E, F, G>() {
                        @Override
                        public boolean execute(IActionHandler actionHandler, Map<String, Object> context) {
                            // Clear sharing list
                            sharingList.stopSharing();
                            return super.execute(actionHandler, context);
                        }
                    },
                    new FrontendAction<E, F, G>() {
                        @Override
                        public boolean execute(IActionHandler actionHandler, Map<String, Object> context) {
                            // Restore sharing list
                            query.setSharedString(previousSharedString);
                            return super.execute(actionHandler, context);
                        }
                    },
                    context);
        }

        return false;
    }
}
