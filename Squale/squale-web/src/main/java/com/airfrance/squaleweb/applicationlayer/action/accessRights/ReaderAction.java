package com.airfrance.squaleweb.applicationlayer.action.accessRights;

import com.airfrance.squalecommon.enterpriselayer.businessobject.profile.ProfileBO;
import com.airfrance.squaleweb.applicationlayer.formbean.LogonBean;

/**
 */
public class ReaderAction extends BaseDispatchAction{

    /** 
     * v�rifie les droits de l'utilisateur � effectuer cette action
     * @param pApplicationId la liste des utilisateurs de l'application
     * @param pUser l'utilisateur courant
     * @return un bool�en indiquant si l'utilisateur poss�de les droits suffisants
     */
    protected boolean checkRights(LogonBean pUser, Long pApplicationId){
        return pUser.isAdmin() || (pApplicationId != null && pUser.getApplicationRight(pApplicationId) != null // illegal access
         && (pUser.getApplicationRight(pApplicationId).equals(ProfileBO.MANAGER_PROFILE_NAME)
         || pUser.getApplicationRight(pApplicationId).equals(ProfileBO.READER_PROFILE_NAME)));
    }

}
