package com.airfrance.squalecommon.enterpriselayer.applicationcomponent.administration;

import java.io.InputStream;
import java.util.Collection;

import com.airfrance.jraf.commons.exception.JrafEnterpriseException;
import com.airfrance.jraf.provider.accessdelegate.DefaultExecuteComponent;
import com.airfrance.squalecommon.datatransfertobject.message.MessagesDTO;
import com.airfrance.squalecommon.datatransfertobject.message.NewsDTO;
import com.airfrance.squalecommon.datatransfertobject.message.NewsListDTO;
import com.airfrance.squalecommon.enterpriselayer.facade.message.MessageFacade;

/**
 * Manipulation des messages
 */
public class MessageApplicationComponentAccess extends DefaultExecuteComponent {

    /**
     * Obtention des messages
     * @return messages
     * @throws JrafEnterpriseException si erreur
     */
    public MessagesDTO getMessages() throws JrafEnterpriseException {
        return MessageFacade.getMessages();
    }
    /**
     * Importation de messages
     * Les messages sont import�s � partir d'un flux et sont stock�s
     * dans la base de donn�es
     * @param pStream flux
     * @param pErrors erreurs
     * @return messages
     * @throws JrafEnterpriseException si erreur
     */
    public MessagesDTO importMessages(InputStream pStream, StringBuffer pErrors) throws JrafEnterpriseException {
        return MessageFacade.importMessages(pStream, pErrors);
    }

    /**
     * @param pKind le param�tre permettant de savoir � partir de quand on doit les r�cup�rer
     * @param pLang la langue �ventuelle des news a r�cup�rer (peut etre null si sans importance)
     * @return la collection des news ou des messages
     * @throws JrafEnterpriseException en cas d'�chec
     */
    public NewsListDTO getNews(String pKind,String pLang) throws JrafEnterpriseException {
        return MessageFacade.getNews(pKind,pLang);
    }
    /**
     * Supprime une nouveaut�
     * @param pDto la nouveaut� � supprimer
     * @throws JrafEnterpriseException en cas d'�chec
     */
    public void purgeNews(NewsDTO pDto) throws JrafEnterpriseException {
        MessageFacade.purgeNews(pDto);
    }

    /**
     * ajoute une nouveaut�
     * @param pNewsDto la nouveaut� � ajouter
     * @throws JrafEnterpriseException en cas d'�chec
     */
    public void addNews(NewsDTO pNewsDto) throws JrafEnterpriseException {
        MessageFacade.addNews(pNewsDto);
    }

    /**
     * modifie une nouveaut�
     * @param pNewsDto la nouveaut� � modifiers
     * @throws JrafEnterpriseException en cas d'�chec
     */
    public void modifyNews(NewsDTO pNewsDto) throws JrafEnterpriseException {
        MessageFacade.modifyNews(pNewsDto);
    }

    /**
     * @return la liste des langues disponibles
     * @throws JrafEnterpriseException en cas d'�chec
     */
    public Collection findLangs() throws JrafEnterpriseException {
        return MessageFacade.findLangs();
    }

    /**
     * 
     * @param pNewsDto le dto dont on veut tester la cl�
     * @return un bool�en indiquant si la cl� existe d�ja
     * @throws JrafEnterpriseException en cas d'�chec
     */
    public Boolean newsAlreadyExists(NewsDTO pNewsDto) throws JrafEnterpriseException {
        return MessageFacade.newsAlreadyExists(pNewsDto.getKey());
    }
}
