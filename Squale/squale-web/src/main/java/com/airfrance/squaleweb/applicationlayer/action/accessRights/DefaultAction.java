package com.airfrance.squaleweb.applicationlayer.action.accessRights;

import com.airfrance.squaleweb.applicationlayer.formbean.LogonBean;

/**
 */
public class DefaultAction
    extends BaseDispatchAction
{

    /**
     * v�rifie les droits de l'utilisateur � effectuer cette action
     * 
     * @param pApplicationId l'id de l'application
     * @param pUser l'utilisateur courant
     * @return true car il n'y a pas de droits n�c�ssaires pour ces pages
     */
    protected boolean checkRights( LogonBean pUser, Long pApplicationId )
    {
        return true;
    }

}
