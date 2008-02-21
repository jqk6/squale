package com.airfrance.squaleweb.connection;

import javax.servlet.http.HttpServletRequest;

import com.airfrance.squaleweb.connection.exception.ConnectionException;

/**
 * Interface pour acc�der au bean utilisateur lors de la connexion
 */
public interface IUserBeanAccessor {
    
    /**
     * @param pRequest la requ�te permettant de r�cup�rer le bean
     * @return l'utilisateur connect�
     * @throws ConnectionException si erreur de connexion
     */
    public IUserBean getUserBean(HttpServletRequest pRequest) throws ConnectionException;

}
