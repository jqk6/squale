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
package com.airfrance.squalecommon.enterpriselayer.facade.pmd;

import java.io.InputStream;

import org.apache.commons.digester.Digester;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.airfrance.squalecommon.enterpriselayer.businessobject.rulechecking.RuleBO;
import com.airfrance.squalecommon.enterpriselayer.businessobject.rulechecking.pmd.PmdRuleSetBO;
import com.airfrance.squalecommon.util.xml.XmlImport;

/**
 * Parser de la configuration Pmd
 */
public class PmdConfigParser
    extends XmlImport
{

    /** Log */
    private static Log LOG = LogFactory.getLog( PmdConfigParser.class );

    /**
     * Constructeur par d�faut
     */
    public PmdConfigParser()
    {
        super( LOG );
    }

    /**
     * Parsing du fichier de configuration Macker
     * 
     * @param pStream flux
     * @param pErrors erreurs rencontr�es
     * @return donn�es lues
     */
    public PmdRuleSetBO parseFile( InputStream pStream, StringBuffer pErrors )
    {
        // R�sultat
        PmdRuleSetBO result = new PmdRuleSetBO();
        // On d'a pas de DTD � fournir
        Digester configDigester = preSetupDigester( null, null, pErrors );
        configDigester.push( new Wrapper( result ) );
        // On r�cup�re le nom du ruleSet
        configDigester.addSetProperties( "ruleset" );
        // On cr�e une r�gle
        configDigester.addSetProperties( "ruleset/rule" );
        // On r�cup�re les informations suppl�mentaires
        configDigester.addCallMethod( "ruleset/rule/properties/property", "setRuleProperty", 2, new Class[] {
            String.class, String.class } );
        configDigester.addCallParam( "ruleset/rule/properties/property", 0, "name" );
        configDigester.addCallParam( "ruleset/rule/properties/property", 1, "value" );
        // On ajoute la r�gle au ruleSet
        configDigester.addCallMethod( "ruleset/rule", "addRule" );
        parse( configDigester, pStream, pErrors );
        return result;
    }

    /**
     * 
     *
     */
    public class Wrapper
    {
        /** R�gle courante */
        private RuleBO mCurrentRule;

        /** Jeu de r�gles */
        private PmdRuleSetBO mRuleSet;

        /**
         * Constructeur
         * 
         * @param pRuleSet ruleset
         */
        public Wrapper( PmdRuleSetBO pRuleSet )
        {
            mRuleSet = pRuleSet;
        }

        /**
         * Traitement de la balise ref
         * 
         * @param pRuleRef r�f�rence
         */
        public void setRef( String pRuleRef )
        {
            lazyCreateRule();
            int index = pRuleRef.lastIndexOf( '/' );
            if ( index != -1 )
            {
                mCurrentRule.setCode( pRuleRef.substring( index + 1 ) );
            }
            else
            {
                mCurrentRule.setCode( pRuleRef );
            }
        }

        /**
         * Traitement des propri�t�s
         * 
         * @param pName nom
         * @param pValue valeur
         */
        public void setRuleProperty( String pName, String pValue )
        {
            if ( pName.equals( "squaleSeverity" ) )
            {
                lazyCreateRule();
                mCurrentRule.setSeverity( pValue );
            }
            else if ( pName.equals( "squaleCategory" ) )
            {
                lazyCreateRule();
                mCurrentRule.setCategory( pValue );
            }
        }

        /**
         * Ajout d'une r�gle
         */
        public void addRule()
        {
            mCurrentRule.setRuleSet( mRuleSet );
            mRuleSet.addRule( mCurrentRule );
            mCurrentRule = null;
        }

        /**
         * Cr�ation de la r�gle courante
         */
        private void lazyCreateRule()
        {
            if ( mCurrentRule == null )
            {
                mCurrentRule = new RuleBO();
            }
        }

        /**
         * @param pLanguage langage
         */
        public void setLanguage( String pLanguage )
        {
            mRuleSet.setLanguage( pLanguage );
        }

        /**
         * @param pName nom
         */
        public void setName( String pName )
        {
            mRuleSet.setName( pName );
        }

    }
}
