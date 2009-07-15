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
package com.airfrance.squalix.tools.cpd;

import java.util.Iterator;

import net.sourceforge.pmd.cpd.Match;
import net.sourceforge.pmd.cpd.TokenEntry;

import com.airfrance.jraf.commons.exception.JrafDaoException;
import com.airfrance.jraf.spi.persistence.ISession;
import com.airfrance.squalecommon.daolayer.result.MeasureDAOImpl;
import com.airfrance.squalecommon.daolayer.rulechecking.ProjectRuleSetDAOImpl;
import com.airfrance.squalecommon.enterpriselayer.businessobject.component.AuditBO;
import com.airfrance.squalecommon.enterpriselayer.businessobject.component.ProjectBO;
import com.airfrance.squalecommon.enterpriselayer.businessobject.result.IntegerMetricBO;
import com.airfrance.squalecommon.enterpriselayer.businessobject.result.rulechecking.CpdTransgressionBO;
import com.airfrance.squalecommon.enterpriselayer.businessobject.result.rulechecking.RuleCheckingTransgressionBO;
import com.airfrance.squalecommon.enterpriselayer.businessobject.result.rulechecking.RuleCheckingTransgressionItemBO;
import com.airfrance.squalecommon.enterpriselayer.businessobject.rulechecking.ProjectRuleSetBO;
import com.airfrance.squalecommon.enterpriselayer.businessobject.rulechecking.RuleBO;

/**
 * Persistance des donn�es de Cpd Les donn�es sont stock�es dans une ruleset dynamique, ce ruleset contient une r�gle
 * par langage
 */
public class CpdPersistor
{
    /** Transgression */
    private CpdTransgressionBO mTransgression;

    /** RuleSet */
    private ProjectRuleSetBO mRuleSet;

    /** Pr�fixe de chemin */
    private String mPrefixPath;

    /**
     * Constructeur
     * 
     * @param pProjectBO projet
     * @param pAuditBO audit
     * @param pPrefixPath pr�fixe de path
     */
    public CpdPersistor( ProjectBO pProjectBO, AuditBO pAuditBO, String pPrefixPath )
    {
        mPrefixPath = pPrefixPath;
        mRuleSet = createRuleSet( pProjectBO );
        mTransgression = createTransgression( pProjectBO, pAuditBO );
    }

    /**
     * Cr�ation de la transgression
     * 
     * @param pProjectBO projet
     * @param pAuditBO audit
     * @return transgression
     */
    protected CpdTransgressionBO createTransgression( ProjectBO pProjectBO, AuditBO pAuditBO )
    {
        // Cr�ation de la transgression
        CpdTransgressionBO transgression = new CpdTransgressionBO();
        transgression.setAudit( pAuditBO );
        transgression.setComponent( pProjectBO );
        transgression.setRuleSet( mRuleSet );
        transgression.setTaskName( "CpdTask" );
        return transgression;
    }

    /**
     * Cr�ation du RuleSet
     * 
     * @param pProject projet
     * @return r�gle de copy/paste
     */
    protected ProjectRuleSetBO createRuleSet( ProjectBO pProject )
    {
        ProjectRuleSetBO ruleset = new ProjectRuleSetBO();
        ruleset.setProject( pProject );
        return ruleset;
    }

    /**
     * Cr�ation d'une r�gle
     * 
     * @param pLanguage langage
     * @return r�gle
     */
    protected RuleBO createRule( String pLanguage )
    {
        RuleBO rule = new RuleBO();
        rule.setCategory( "copypaste" );
        rule.setCode( "cpd." + pLanguage );
        rule.setSeverity( "error" );
        rule.setRuleSet( mRuleSet );
        mRuleSet.addRule( rule );
        return rule;
    }

    /**
     * Sauvegarde des r�sultats
     * 
     * @param pSession session
     * @throws JrafDaoException si erreur
     */
    public void storeResults( ISession pSession )
        throws JrafDaoException
    {
        // Cr�ation du RuleSet
        ProjectRuleSetDAOImpl.getInstance().create( pSession, mRuleSet );
        // Sauvegarde des donn�es dans la base
        MeasureDAOImpl.getInstance().create( pSession, mTransgression );
    }

    /**
     * Ajout d'un r�sultat
     * 
     * @param pLanguage langage
     * @param pMatches d�tections
     */
    public void addResult( String pLanguage, Iterator pMatches )
    {
        RuleBO rule = createRule( pLanguage );
        int nbOcc = 0;
        int dupLinesNb = 0;
        // On traite chaque d�tection
        while ( pMatches.hasNext() )
        {
            Match m = (Match) pMatches.next();
            StringBuffer detail = new StringBuffer();
            detail.append( CpdMessages.getString( "copypaste.summary", new Object[] { new Integer( m.getLineCount() ),
                new Integer( m.getTokenCount() ) } ) );
            detail.append( '\n' );
            nbOcc++;
            Iterator occ = m.iterator();
            // Parcours de chaque occurrence de copy/paste
            String itemFileName = "";
            int itemLine = 0;
            while ( occ.hasNext() )
            {
                TokenEntry entry = (TokenEntry) occ.next();
                String fileName = entry.getTokenSrcID();
                // On �limine dans le nom du fichier le nom de la vue
                if ( fileName.startsWith( mPrefixPath ) )
                {
                    fileName = fileName.substring( mPrefixPath.length() );
                }
                detail.append( CpdMessages.getString( "copypaste.occurrenceentry", new Object[] {
                    new Integer( entry.getBeginLine() ), fileName } ) );
                detail.append( '\n' );
                // On affecte le premier fichier et la premi�re ligne trouv�e � l'item
                if ( itemFileName.length() == 0 )
                {
                    itemFileName = fileName;
                    itemLine = entry.getBeginLine();
                }
            }
            dupLinesNb += ( m.getMarkCount() - 1 ) * m.getLineCount();
            // On ne fait persister que les 100 premiers items par r�gle
            // ICI il n'y a qu'une r�gle d�finie par langage
            if ( nbOcc <= RuleCheckingTransgressionBO.MAX_DETAILS )
            {
                RuleCheckingTransgressionItemBO item = new RuleCheckingTransgressionItemBO();
                // Taille maximale pour les d�tails
                final int MAX_LENGTH = 3000;
                // Troncature du message si besoin
                if ( detail.length() > MAX_LENGTH )
                {
                    detail.setLength( MAX_LENGTH - 1 );
                }
                item.setMessage( detail.toString() );
                item.setRule( rule );
                item.setComponentFile( itemFileName );
                item.setLine( itemLine );
                mTransgression.getDetails().add( item );
            }
        }
        // On ajoute une m�trique de type Integer pour chaque r�gle transgress�e
        IntegerMetricBO metric = new IntegerMetricBO();
        metric.setName( rule.getCode() );
        metric.setValue( nbOcc );
        metric.setMeasure( mTransgression );
        mTransgression.putMetric( metric );
        // Cr�ation de la m�trique donnant le nombre total de lignes pour le langage
        mTransgression.setDuplicateLinesForLanguage( pLanguage, new Integer( dupLinesNb ) );
        // Incr�ment du nombre total de lignes dupliqu�es
        mTransgression.incrementDuplicateLinesNumber( dupLinesNb );
    }
}
