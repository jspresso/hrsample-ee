package org.jspresso.hrsample.ext.model.userquery.service;

/**
 * UserSharingListService
 * User: Maxime HAMM
 * Date: 30/11/2017
 */
public interface UserSharingListService {

    /**
     * SEPARATOR
     */
    String SEP = " - ";

    /**
     * SHARE WITH ALL MARK
     */
    String ALL = "#";

    /**
     * Update query sharing string using users list
     */
    void shareWithUserList();

    /**
     * Share query with all users
     */
    void shareWithAll();

    /**
     * Is shared with all
     * @return
     */
    boolean isSharedWithUsers();

    /**
     * Stop sharing
     */
    void stopSharing();

}
