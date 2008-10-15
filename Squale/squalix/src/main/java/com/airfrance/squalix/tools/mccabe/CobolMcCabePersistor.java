package com.airfrance.squalix.tools.mccabe;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.airfrance.jraf.commons.exception.JrafDaoException;
import com.airfrance.jraf.spi.persistence.ISession;
import com.airfrance.squalecommon.daolayer.result.MeasureDAOImpl;
import com.airfrance.squalecommon.enterpriselayer.businessobject.component.AuditBO;
import com.airfrance.squalecommon.enterpriselayer.businessobject.result.mccabe.McCabeQAMethodMetricsBO;
import com.airfrance.squalecommon.enterpriselayer.businessobject.result.mccabe.McCabeQAProjectMetricsBO;
import com.airfrance.squalix.core.TaskData;
import com.airfrance.squalix.util.csv.CSVParser;
import com.airfrance.squalix.util.parser.CobolParser;
import com.airfrance.squalix.util.repository.ComponentRepository;

/**
 * Objet charg� de faire persister les m�triques McCabe pour les projets Cobol.
 */
public class CobolMcCabePersistor
    extends McCabePersistor
{

    /**
     * Logger.
     */
    private static final Log LOGGER = LogFactory.getLog( CobolMcCabePersistor.class );

    /**
     * Adaptateur.
     */
    private CobolMcCabeAdaptator mAdaptator;

    /**
     * Le parser.
     */
    private CobolParser mParser;

    /**
     * Nombre de module du projet.
     */
    private int mNumberOfModules;

    /**
     * Nombre de programmes.
     */
    private int mNumberOfPrograms;

    /**
     * Constructeur.
     * 
     * @param pSession la session de persistence utilis�e par la t�che.
     * @param pDatas la liste des param�tres temporaires du projet
     * @param pTaskName le nom de la tache
     * @param pAudit l'audit en cours
     * @param pConfiguration la configuration
     * @param pCobolParser le parser Cobol
     */
    public CobolMcCabePersistor( final ISession pSession, final TaskData pDatas, final String pTaskName,
                                 final AuditBO pAudit, final McCabeConfiguration pConfiguration,
                                 final CobolParser pCobolParser )
    {
        super( pSession, pDatas, pTaskName, pAudit, pConfiguration );
        mParser = pCobolParser;
        ComponentRepository repository = new ComponentRepository( mProject, mSession );
        mAdaptator = new CobolMcCabeAdaptator( repository, mParser );
    }

    /**
     * Analyse le rapport des m�triques McCabe et enregistre les composants et m�triques associ�es.
     * 
     * @param pFilename le nom du fichier des m�triques rapport�es par McCabe.
     * @throws Exception si exception
     */
    public void parseCobolReport( final String pFilename )
        throws Exception
    {
        LOGGER.info( McCabeMessages.getString( "logs.debug.report_parsing_module" ) + pFilename );
        // chargement du mod�le csv de rapport McCabe pour le Cobol
        CSVParser csvparser = new CSVParser( McCabeMessages.getString( "csv.config.file" ) );
        // analyse du rapport des m�triques et cr�ation des objets m�tier des m�triques
        Collection<McCabeQAMethodMetricsBO> moduleResults = new ArrayList<McCabeQAMethodMetricsBO>();
        moduleResults = csvparser.parse( McCabeMessages.getString( "csv.template.cobol" ), pFilename );
        // association des m�triques avec le composant Cobol avant enregistrement en base
        McCabeQAMethodMetricsBO bo = null;
        Iterator<McCabeQAMethodMetricsBO> it = moduleResults.iterator();
        while ( it.hasNext() )
        {
            bo = it.next();
            bo.setAudit( mAudit );
            bo.setTaskName( mTaskName );
            // association des m�triques avec les composants Cobol
            mAdaptator.adaptModuleResult( bo );
            // incr�ment du nombre de modules
            mNumberOfModules++;
        }

        // enregistrement en base des m�triques McCabe
        LOGGER.info( McCabeMessages.getString( "logs.debug.report_parsing_database" ) );
        MeasureDAOImpl.getInstance().saveAll( mSession, moduleResults );
        mSession.commitTransactionWithoutClose();
        mSession.beginTransaction();
        LOGGER.info( McCabeMessages.getString( "logs.debug.report_parsing_end" ) );
    }

    /**
     * Cr�e les r�sultats de niveau projet et les enregistre en base.
     */
    public void persistProjectResult()
    {
        LOGGER.info( McCabeMessages.getString( "logs.debug.project_database" ) );
        McCabeQAProjectMetricsBO metrics = new McCabeQAProjectMetricsBO();
        // Cr�ation des m�triques de niveau projet
        metrics.setComponent( mProject );
        metrics.setAudit( mAudit );
        metrics.setTaskName( mTaskName );
        metrics.setNumberOfClasses( new Integer( mNumberOfPrograms ) );
        metrics.setNumberOfMethods( new Integer( mNumberOfModules ) );
        try
        {
            MeasureDAOImpl.getInstance().create( mSession, metrics );
        }
        catch ( JrafDaoException e )
        {
            LOGGER.error( e, e );
        }

    }
}
