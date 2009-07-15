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
package com.airfrance.squalix.tools.mccabe;

import java.util.HashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.airfrance.jraf.commons.exception.JrafDaoException;
import com.airfrance.squalecommon.enterpriselayer.businessobject.component.ClassBO;
import com.airfrance.squalecommon.enterpriselayer.businessobject.component.JspBO;
import com.airfrance.squalecommon.enterpriselayer.businessobject.component.MethodBO;
import com.airfrance.squalecommon.enterpriselayer.businessobject.result.mccabe.McCabeQAClassMetricsBO;
import com.airfrance.squalecommon.enterpriselayer.businessobject.result.mccabe.McCabeQAJspMetricsBO;
import com.airfrance.squalecommon.enterpriselayer.businessobject.result.mccabe.McCabeQAMethodMetricsBO;
import com.airfrance.squalix.util.parser.J2EEParser;
import com.airfrance.squalix.util.parser.LanguageParser;
import com.airfrance.squalix.util.repository.ComponentRepository;

/**
 * Est en charge de remplacer tous les noms des m�thodes par les objets correspondant.<br>
 * Ceci permet d'utiliser les relations propos�es par la base de donn�es.<br>
 */
public class OOMcCabeAdaptator
{

    /**
     * HashMap des sommes des iv(g).
     */
    private HashMap mSumIvg;

    /**
     * Parser
     */
    private LanguageParser mParser;

    /**
     * Repository
     */
    private ComponentRepository mRepository;

    /**
     * Logger
     */
    private static final Log LOGGER = LogFactory.getLog( OOMcCabeAdaptator.class );

    /**
     * Constructeur
     * 
     * @param pParser le parser
     * @param pRepository le repository
     */
    public OOMcCabeAdaptator( LanguageParser pParser, ComponentRepository pRepository )
    {
        mParser = pParser;
        mRepository = pRepository;
        mSumIvg = new HashMap();
    }

    /**
     * Adapte le bean des r�sultats des m�thodes et le fait persister.<br>
     * En utilisant nom du composant r�cup�r� depuis le rapport McCabe, on cr�e la relation avec le composant ad�quat.
     * 
     * @param pMethodResult Ensemble de r�sultats de la m�thode devant �tre modifi�s et persist�s.
     * @throws JrafDaoException si erreur
     */
    public void adaptMethodResult( final McCabeQAMethodMetricsBO pMethodResult )
        throws JrafDaoException
    {
        LOGGER.debug( McCabeMessages.getString( "logs.debug.adapt_method" ) + pMethodResult.getComponentName() );
        // Recherche de la m�thode associ�e
        MethodBO method = mParser.getMethod( pMethodResult.getComponentName(), pMethodResult.getFilename() );
        if ( null != method )
        {
            // On renseigne son num�ro de ligne dans le fichier pour le plugin IDE
            try
            {
                method.setStartLine( Integer.parseInt( pMethodResult.getStartLine() ) );
            }
            catch ( NumberFormatException nfe )
            {
                // Par d�faut on met la ligne � 0
                method.setStartLine( 0 );
            }
            MethodBO persistentMethod = (MethodBO) mRepository.persisteComponent( method );
            String parentKeyName = mRepository.buildKey( (ClassBO) persistentMethod.getParent() );
            Integer sumIvG = (Integer) mSumIvg.get( parentKeyName );
            if ( null == sumIvG )
            {
                sumIvG = new Integer( pMethodResult.getIvg().intValue() );
            }
            else
            {
                int value = sumIvG.intValue() + pMethodResult.getIvg().intValue();
                sumIvG = new Integer( value );
            }
            mSumIvg.put( parentKeyName, sumIvG );
            pMethodResult.setComponent( persistentMethod );
        }
    }

    /**
     * Adapte le bean des r�sultats des classes et le fait persister.
     * 
     * @param pClassResult Ensemble des r�sultats de la classe devant �tre modifi�s et persist�s.
     * @throws JrafDaoException si erreur
     */
    public void adaptClassResult( final McCabeQAClassMetricsBO pClassResult )
        throws JrafDaoException
    {
        LOGGER.debug( McCabeMessages.getString( "logs.debug.adapt_class" ) + pClassResult.getComponentName() );
        ClassBO classBO = mParser.getClass( pClassResult.getComponentName() );
        if ( null != classBO )
        {
            ClassBO persistentClassBO = (ClassBO) mRepository.persisteComponent( classBO );
            pClassResult.setComponent( persistentClassBO );
            String keyName = mRepository.buildKey( persistentClassBO );
            Integer sumIvG = (Integer) mSumIvg.get( keyName );
            if ( null == sumIvG )
            {
                sumIvG = new Integer( 0 );
            }
            pClassResult.setSumivg( sumIvG );
        }
    }

    /**
     * Adapte le bean des r�sultats des m�thodes au composant JSP et le fait persister.<br>
     * En utilisant le nom du composant r�cup�r� depuis le rapport McCabe, on cr�e la relation avec le composant
     * ad�quat.
     * 
     * @param pJspMethodResult Ensemble de r�sultats de la m�thode de la JSP
     * @param pDirectoryName le nom du r�pertoire racine contenant les pages JSP
     * @param pId l'index du r�pertoires des JSPs dans les param�tres du projet
     * @throws JrafDaoException si erreur
     */
    public void adaptJspResult( McCabeQAJspMetricsBO pJspMethodResult, String pDirectoryName, int pId )
        throws JrafDaoException
    {
        LOGGER.debug( McCabeMessages.getString( "logs.debug.adapt_jsp_with_method_result" )
            + pJspMethodResult.getComponentName() );
        JspBO jsp =
            ( (J2EEParser) mParser ).getJsp( pJspMethodResult.getComponentName(), pJspMethodResult.getFileName(),
                                             pDirectoryName, pId );
        if ( null != jsp )
        {
            JspBO persistentJsp = (JspBO) mRepository.persisteComponent( jsp );
            pJspMethodResult.setComponent( persistentJsp );
        }
    }
}
