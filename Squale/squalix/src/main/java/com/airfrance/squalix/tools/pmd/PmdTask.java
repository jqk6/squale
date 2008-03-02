package com.airfrance.squalix.tools.pmd;

import java.util.Iterator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.sourceforge.pmd.Report;

import com.airfrance.jraf.commons.exception.JrafDaoException;
import com.airfrance.squalecommon.daolayer.rulechecking.PmdRuleSetDAOImpl;
import com.airfrance.squalecommon.enterpriselayer.businessobject.component.parameters.MapParameterBO;
import com.airfrance.squalecommon.enterpriselayer.businessobject.component.parameters.ParametersConstants;
import com.airfrance.squalecommon.enterpriselayer.businessobject.component.parameters.StringParameterBO;
import com.airfrance.squalecommon.enterpriselayer.businessobject.config.TaskParameterBO;
import com.airfrance.squalecommon.enterpriselayer.businessobject.rulechecking.pmd.PmdRuleSetBO;
import com.airfrance.squalecommon.util.file.FileUtility;
import com.airfrance.squalix.core.AbstractTask;
import com.airfrance.squalix.core.TaskException;
import com.airfrance.squalix.core.exception.ConfigurationException;

/**
 * T�che de d�tection de copier/coller La t�che est configur�e par un ou plusieurs param�tres nomm�s 'language', chaque
 * param�tre donne lieu au lancement du de la d�tection du copy/paste correspondant
 */
public class PmdTask
    extends AbstractTask
{
    /**
     * Logger
     */
    private static final Log LOGGER = LogFactory.getLog( PmdTask.class );

    /**
     * Constructeur
     */
    public PmdTask()
    {
        mName = "PmdTask";
    }

    /**
     * {@inheritDoc}
     * 
     * @see com.airfrance.squalix.core.Task#execute()
     */
    public void execute()
        throws TaskException
    {
        try
        {
            // Factory
            PmdProcessingFactory factory = new PmdProcessingFactory();
            // Parcours sur les langages
            boolean oneLangAtLeast = false;
            for ( Iterator it = getTaskParameters().iterator(); it.hasNext(); )
            {
                TaskParameterBO param = (TaskParameterBO) it.next();
                if ( param.getName().equals( "language" ) )
                {
                    LOGGER.info( PmdMessages.getString( "pmd.processing.language", param.getValue() ) );
                    oneLangAtLeast = true;
                    AbstractPmdProcessing processing = factory.createPmdProcessing( param.getValue() ); // R�cup�ration
                                                                                                        // du fichier de
                                                                                                        // ruleset
                    PmdRuleSetBO ruleset = getRuleSet( param.getValue() );
                    // La t�che peut avoir �t� annul�e
                    if ( mStatus != CANCELLED )
                    {
                        Report report =
                            processing.process( getData(), getProject().getParameters(),
                                                FileUtility.byteToFile( ruleset.getValue() ) );
                        // Cr�ation du persistor
                        PmdPersistor persistor = new PmdPersistor( getProject(), getAudit(), ruleset, param.getValue() );
                        persistor.storeResults( getSession(), report );
                    }
                }
                else
                {
                    throw new ConfigurationException( PmdMessages.getString( "exception.parameter.invalid",
                                                                             new Object[] { param.getName(),
                                                                                 param.getValue() } ) );
                }
            }
            // V�rification qu'il existe au moins un language d�fini
            if ( !oneLangAtLeast )
            {
                throw new ConfigurationException( PmdMessages.getString( "exception.parameter.missing" ) );
            }
        }
        catch ( Exception e )
        {
            throw new TaskException( e );
        }
    }

    /**
     * @param pLanguage langage
     * @return fichier de ruleset
     * @throws JrafDaoException si erreur
     * @throws ConfigurationException si erreur
     */
    protected PmdRuleSetBO getRuleSet( String pLanguage )
        throws JrafDaoException, ConfigurationException
    {
        PmdRuleSetBO ruleset = null;
        // On r�cup�re les param�tres du projet
        MapParameterBO params = (MapParameterBO) getProject().getParameter( ParametersConstants.PMD );
        if ( params == null )
        {
            String message = PmdMessages.getString( "warning.task.not_set" );
            // On affiche un warning sans lancer d'exception, la t�che ne sera pas ex�cut�e.
            initError( message );
            LOGGER.warn( message );
            // Les param�tres ne sont pas configur�s, on annule la t�che
            mStatus = CANCELLED;
        }
        else
        {
            StringParameterBO param = null;
            if ( pLanguage.equals( "java" ) )
            {
                param = (StringParameterBO) params.getParameters().get( ParametersConstants.PMD_JAVA_RULESET_NAME );
            }
            else if ( pLanguage.equals( "jsp" ) )
            {
                param = (StringParameterBO) params.getParameters().get( ParametersConstants.PMD_JSP_RULESET_NAME );
            }
            else
            {
                throw new ConfigurationException( PmdMessages.getString( "exception.unknown.language", pLanguage ) );
            }
            if ( param == null )
            {
                String message = PmdMessages.getString( "exception.ruleset.missing" );
                LOGGER.error( message );
                // Renvoi d'une exception de configuration
                throw new ConfigurationException( message );
            }
            ruleset = PmdRuleSetDAOImpl.getInstance().findRuleSet( getSession(), param.getValue(), pLanguage );

        }
        return ruleset;
    }
}
