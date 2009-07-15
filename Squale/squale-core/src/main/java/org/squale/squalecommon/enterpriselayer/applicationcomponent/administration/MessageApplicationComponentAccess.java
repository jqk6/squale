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
package org.squale.squalecommon.enterpriselayer.applicationcomponent.administration;

import java.io.InputStream;
import java.util.Collection;

import org.squale.jraf.commons.exception.JrafEnterpriseException;
import org.squale.jraf.provider.accessdelegate.DefaultExecuteComponent;
import org.squale.squalecommon.datatransfertobject.message.MessagesDTO;
import org.squale.squalecommon.datatransfertobject.message.NewsDTO;
import org.squale.squalecommon.datatransfertobject.message.NewsListDTO;
import org.squale.squalecommon.enterpriselayer.facade.message.MessageFacade;

/**
 * Manipulation des messages
 */
public class MessageApplicationComponentAccess
    extends DefaultExecuteComponent
{

    /**
     * Obtention des messages
     * 
     * @return messages
     * @throws JrafEnterpriseException si erreur
     */
    public MessagesDTO getMessages()
        throws JrafEnterpriseException
    {
        return MessageFacade.getMessages();
    }

    /**
     * Importation de messages Les messages sont import�s � partir d'un flux et sont stock�s dans la base de donn�es
     * 
     * @param pStream flux
     * @param pErrors erreurs
     * @return messages
     * @throws JrafEnterpriseException si erreur
     */
    public MessagesDTO importMessages( InputStream pStream, StringBuffer pErrors )
        throws JrafEnterpriseException
    {
        return MessageFacade.importMessages( pStream, pErrors );
    }

    /**
     * @param pKind le param�tre permettant de savoir � partir de quand on doit les r�cup�rer
     * @param pLang la langue �ventuelle des news a r�cup�rer (peut etre null si sans importance)
     * @return la collection des news ou des messages
     * @throws JrafEnterpriseException en cas d'�chec
     */
    public NewsListDTO getNews( String pKind, String pLang )
        throws JrafEnterpriseException
    {
        return MessageFacade.getNews( pKind, pLang );
    }

    /**
     * Supprime une nouveaut�
     * 
     * @param pDto la nouveaut� � supprimer
     * @throws JrafEnterpriseException en cas d'�chec
     */
    public void purgeNews( NewsDTO pDto )
        throws JrafEnterpriseException
    {
        MessageFacade.purgeNews( pDto );
    }

    /**
     * ajoute une nouveaut�
     * 
     * @param pNewsDto la nouveaut� � ajouter
     * @throws JrafEnterpriseException en cas d'�chec
     */
    public void addNews( NewsDTO pNewsDto )
        throws JrafEnterpriseException
    {
        MessageFacade.addNews( pNewsDto );
    }

    /**
     * modifie une nouveaut�
     * 
     * @param pNewsDto la nouveaut� � modifiers
     * @throws JrafEnterpriseException en cas d'�chec
     */
    public void modifyNews( NewsDTO pNewsDto )
        throws JrafEnterpriseException
    {
        MessageFacade.modifyNews( pNewsDto );
    }

    /**
     * @return la liste des langues disponibles
     * @throws JrafEnterpriseException en cas d'�chec
     */
    public Collection findLangs()
        throws JrafEnterpriseException
    {
        return MessageFacade.findLangs();
    }

    /**
     * @param pNewsDto le dto dont on veut tester la cl�
     * @return un bool�en indiquant si la cl� existe d�ja
     * @throws JrafEnterpriseException en cas d'�chec
     */
    public Boolean newsAlreadyExists( NewsDTO pNewsDto )
        throws JrafEnterpriseException
    {
        return MessageFacade.newsAlreadyExists( pNewsDto.getKey() );
    }
}
