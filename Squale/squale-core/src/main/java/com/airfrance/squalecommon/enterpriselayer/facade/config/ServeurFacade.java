/**
 * Copyright (C) 2008-2010, Squale Project - http://www.squale.org
 *
 * This file is part of Squale.
 *
 * Squale is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or any later version.
 *
 * Squale is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Squale.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.airfrance.squalecommon.enterpriselayer.facade.config;


import com.airfrance.jraf.commons.exception.JrafDaoException;
import com.airfrance.jraf.helper.PersistenceHelper;
import com.airfrance.jraf.spi.enterpriselayer.IFacade;
import com.airfrance.jraf.spi.persistence.IPersistenceProvider;
import com.airfrance.jraf.spi.persistence.ISession;
import com.airfrance.squalecommon.daolayer.config.ServeurDAOImpl;
import com.airfrance.squalecommon.datatransfertobject.config.ServeurDTO;
import com.airfrance.squalecommon.datatransfertobject.transform.config.ServeurTransform;
import com.airfrance.squalecommon.enterpriselayer.businessobject.config.ServeurBO;

/**
 * Facade pour un serveur Squalix
 */
public class ServeurFacade
    implements IFacade
{
	/**
     * provider de persistence
     */
    private static final IPersistenceProvider PERSISTENTPROVIDER = PersistenceHelper.getPersistenceProvider();
    
    /**
     * Renvoie le ServeurDTO correspondant au SiteId fourni
     * @param pSiteId le serveur du site recherché
     * @return ServeurBO du site recherché
     * @throws JrafDaoException
     */
    public static ServeurDTO getServeur (long pSiteId)
    	throws JrafDaoException
    {
    	ServeurDTO result = new ServeurDTO();
    	ISession session = null;

    	{
    		//Récupération d'une session Hibernate
    		session = PERSISTENTPROVIDER.getSession();
    		//Instanciation d'un serveur
    		ServeurDAOImpl serveurDAO = ServeurDAOImpl.getInstance();
    		ServeurBO serveurBO = serveurDAO.findWhereId( session , pSiteId );
    		result = ServeurTransform.bo2dto( serveurBO );
    		return result;
    	}
    	
    	
    }
    
}