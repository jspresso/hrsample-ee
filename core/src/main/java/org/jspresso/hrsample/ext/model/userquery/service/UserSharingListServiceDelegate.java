package org.jspresso.hrsample.ext.model.userquery.service;

import org.jspresso.framework.model.component.service.AbstractComponentServiceDelegate;
import org.jspresso.hrsample.ext.model.userquery.UserSharingList;
import org.jspresso.hrsample.model.User;

import java.util.List;

/**
 * UserSharingListServiceDelegate
 * User: Maxime HAMM
 * Date: 30/11/2017
 */
public class UserSharingListServiceDelegate extends AbstractComponentServiceDelegate<UserSharingList> implements UserSharingListService {

    /**
     * {@inheritDoc}
     */
    @Override
    public void shareWithUserList() {

        UserSharingList sharingList = getComponent();

        List<User> users = sharingList.getUsers();
        if (users == null || users.isEmpty()) {

            sharingList.getQuery().setSharedString(null);
            return;
        }

        StringBuilder sb = new StringBuilder();
        for (User user : users) {
            sb.append(SEP);
            sb.append(user.getLogin());
        }
        sb.append(SEP);

        sharingList.getQuery().setSharedString(sb.toString());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isSharedWithUsers() {

        // If no user at all it is not shared
        return UserSharingListService.ALL.equalsIgnoreCase(getComponent().getQuery().getSharedString());
    }

    @Override
    public void stopSharing() {

        UserSharingList sharingList = getComponent();
        sharingList.setUsers(null);
    }

    @Override
    public void shareWithAll() {

        UserSharingList sharingList = getComponent();
        sharingList.setUsers(null);
    }
}
