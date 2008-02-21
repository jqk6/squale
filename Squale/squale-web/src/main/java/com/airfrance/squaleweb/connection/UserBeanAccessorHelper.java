package com.airfrance.squaleweb.connection;

import com.airfrance.jraf.bootstrap.locator.SpringLocator;

/**
 * Acc�s au provider de s�curit�
 */
public class UserBeanAccessorHelper {


    /**
     * Retourne le bean permettant d'acc�der � l'utilisateur authentifi�
     * @return l'accesseur de bean
     */
    public final static IUserBeanAccessor getUserBeanAccessor() {
        IUserBeanAccessor accessor = null;
        SpringLocator springLocator = SpringLocator.getInstance();
        accessor = (IUserBeanAccessor) springLocator.getBean("userBeanAccessor");
        return accessor;
    }

}
