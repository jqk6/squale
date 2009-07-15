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
package org.squale.squalecommon.enterpriselayer.facade.checkstyle;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.squale.jraf.commons.exception.JrafDaoException;
import org.squale.jraf.commons.exception.JrafEnterpriseException;
import org.squale.jraf.helper.PersistenceHelper;
import org.squale.jraf.provider.persistence.hibernate.facade.FacadeHelper;
import org.squale.jraf.spi.enterpriselayer.IFacade;
import org.squale.jraf.spi.persistence.IPersistenceProvider;
import org.squale.jraf.spi.persistence.ISession;
import org.squale.squalecommon.daolayer.result.rulechecking.RuleCheckingTransgressionDAOImpl;
import org.squale.squalecommon.daolayer.rulechecking.CheckstyleRuleSetDAOImpl;
import org.squale.squalecommon.datatransfertobject.rulechecking.CheckstyleDTO;
import org.squale.squalecommon.datatransfertobject.transform.rulechecking.CheckstyleTransform;
import org.squale.squalecommon.enterpriselayer.businessobject.rulechecking.checkstyle.CheckstyleRuleSetBO;
import org.squale.squalecommon.enterpriselayer.facade.checkstyle.xml.CheckstyleImport;
import org.squale.squalecommon.util.file.FileUtility;

/**
 * @author henix
 */
public class CheckstyleFacade
    implements IFacade
{

    /**
     * provider de persistence
     */
    private static final IPersistenceProvider PERSISTENTPROVIDER = PersistenceHelper.getPersistenceProvider();

    /**
     * Contructeur vide : evite l'instanciation de cet objet
     */
    private CheckstyleFacade()
    {
    }

    /**
     * Donne les diff�rentes versions du fichier de configuartion checkstyle
     * 
     * @return Collection
     * @throws JrafEnterpriseException exception JRAF
     */

    public static ArrayList getAllVersions()
        throws JrafEnterpriseException
    {

        // Initialisation
        Collection versionBOs = null; // retour de la DAO
        ArrayList versionDTOs = new ArrayList(); // retour de la facade

        ISession session = null;
        try
        {
            // cr�ation d'une session Hibernate
            session = PERSISTENTPROVIDER.getSession();

            CheckstyleRuleSetDAOImpl versionDAO = CheckstyleRuleSetDAOImpl.getInstance();
            versionBOs = versionDAO.findAllSorted( session );

            Iterator iterator = versionBOs.iterator();
            CheckstyleRuleSetBO versionBO = null;
            while ( iterator.hasNext() )
            {
                versionDTOs.add( CheckstyleTransform.bo2Dto( (CheckstyleRuleSetBO) iterator.next() ) );
            }
        }
        catch ( JrafDaoException e )
        {
            FacadeHelper.convertException( e, CheckstyleFacade.class.getName() + ".getAllversions" );
        }
        finally
        {
            FacadeHelper.closeSession( session, CheckstyleFacade.class.getName() + ".getAllversions" );
        }
        return versionDTOs;
    }

    /**
     * Insert une nouvelle version du fichier de configuration checkstyle. Et assure l'ascendance de la nouvelle version
     * vers les anciennes
     * 
     * @param pStream flux � parser
     * @param pErrors les erreurs g�n�r�s pendant le parsing.
     * @return VersionDTO version
     * @throws JrafEnterpriseException exception JRAF
     */

    public static CheckstyleDTO importCheckstyleConfFile( InputStream pStream, StringBuffer pErrors )
        throws JrafEnterpriseException
    {
        CheckstyleDTO version = null;
        ISession session = null;
        try
        {
            // cr�ation d'une session Hibernate
            session = PERSISTENTPROVIDER.getSession();
            session.beginTransaction();
            // Importation du fichier
            CheckstyleImport checkstyleImport = new CheckstyleImport();
            byte[] bytes = FileUtility.toByteArrayImpl( pStream );

            CheckstyleRuleSetBO versionBo =
                checkstyleImport.importCheckstyle( new ByteArrayInputStream( bytes ), pErrors );

            if ( pErrors.length() == 0 )
            {
                // Il faut tester que le fichier pars� �tait param�tr� pour squale
                // Dans le cas inverse, le parsing se passe bien mais versionBo est null
                if ( versionBo != null )
                {
                    // verification des �ventuelles ambiguit�s au niveau des modules
                    Collection colModuleName = versionBo.isAmbiguityModules();

                    if ( colModuleName.size() == 0 )
                    {

                        // persistance des r�sultats
                        versionBo.setValue( bytes );
                        versionBo = CheckstyleRuleSetDAOImpl.getInstance().createCheckstyleRuleSet( session, versionBo );
                        version = CheckstyleTransform.bo2Dto( versionBo );
                        // on persiste les r�sultats dans la base
                        session.commitTransactionWithoutClose();
                    }
                    else
                    {

                        pErrors.append( CheckstyleParsingMessages.getString( "checkstyle.file.unupload" ) + "\n" );
                        // on enr�gistre le nom des modules concern�s
                        pErrors.append( CheckstyleParsingMessages.getString( "checkstyle.ambiguty.detected" ) + "\n" );
                        String moduleName = null;
                        Iterator it = colModuleName.iterator();
                        while ( it.hasNext() )
                        {
                            moduleName = (String) it.next();
                            pErrors.append( moduleName + "\n" );
                        }
                        session.rollbackTransactionWithoutClose();
                    }
                }
                else
                {
                    session.rollbackTransactionWithoutClose();
                    pErrors.append( CheckstyleParsingMessages.getString( "checkstyle.squale.format.violation" ) );
                }
            }

        }
        catch ( JrafDaoException e )
        {
            session.rollbackTransactionWithoutClose();
            FacadeHelper.convertException( e, CheckstyleFacade.class.getName() + ".parseFile" );

        }
        catch ( IOException e )
        {
            session.rollbackTransactionWithoutClose();
            FacadeHelper.convertException( e, CheckstyleFacade.class.getName() + ".parseFile" );
        }
        finally
        {
            FacadeHelper.closeSession( session, CheckstyleFacade.class.getName() + ".parseFile" );
        }
        return version;
    }

    /**
     * Destruction de rulesets obsol�tes
     * 
     * @param pRuleSets ruleSets devant �tre d�truits
     * @return rulesets obsol�tes ne pouvant pas �tre supprim�s
     * @throws JrafEnterpriseException si erreur
     */
    public static Collection deleteRuleSets( Collection pRuleSets )
        throws JrafEnterpriseException
    {
        ISession session = null;
        Collection result = new ArrayList();
        try
        {
            // r�cup�ration d'une session
            session = PERSISTENTPROVIDER.getSession();
            CheckstyleRuleSetDAOImpl checkstyleRuleSetDAO = CheckstyleRuleSetDAOImpl.getInstance();
            Iterator ruleSetsIt = pRuleSets.iterator();
            // Parcours des rulesets � d�truire
            ArrayList rulesetsId = new ArrayList();
            RuleCheckingTransgressionDAOImpl ruleCheckingTransgressionDAO =
                RuleCheckingTransgressionDAOImpl.getInstance();
            while ( ruleSetsIt.hasNext() )
            {
                CheckstyleDTO checkstyleDTO = (CheckstyleDTO) ruleSetsIt.next();
                Long ruleSetId = new Long( checkstyleDTO.getId() );
                // On v�rifie que le jeu de r�gles n'est pas utilis�
                // au niveau des mesures r�alis�es, pour les projets param�tr�s mais non encore audit�s, on ne le g�re
                // pas
                if ( ruleCheckingTransgressionDAO.isRuleSetUsed( session, ruleSetId ) )
                {
                    result.add( checkstyleDTO );
                }
                else
                {
                    // Ajout dans les rulesets � d�truire
                    rulesetsId.add( ruleSetId );
                }
            }
            // Destruction des rulesets qui ne sont plus r�f�renc�s
            if ( rulesetsId.size() > 0 )
            {
                checkstyleRuleSetDAO.removeCheckstyleRuleSets( session, rulesetsId );
            }
        }
        catch ( JrafDaoException e )
        {
            FacadeHelper.convertException( e, CheckstyleFacade.class.getName() + ".get" );
        }
        finally
        {
            FacadeHelper.closeSession( session, CheckstyleFacade.class.getName() + ".get" );
        }
        return result;
    }
}
