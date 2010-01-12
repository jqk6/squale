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
package org.squale.squalix.tools.rsm;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.squale.jraf.commons.exception.JrafDaoException;
import org.squale.jraf.spi.persistence.ISession;
import org.squale.squalecommon.daolayer.result.MeasureDAOImpl;
import org.squale.squalecommon.enterpriselayer.businessobject.component.AuditBO;
import org.squale.squalecommon.enterpriselayer.businessobject.component.ProjectBO;
import org.squale.squalecommon.enterpriselayer.businessobject.result.rsm.RSMClassMetricsBO;
import org.squale.squalecommon.enterpriselayer.businessobject.result.rsm.RSMMethodMetricsBO;
import org.squale.squalecommon.enterpriselayer.businessobject.result.rsm.RSMProjectMetricsBO;
import org.squale.squalix.core.TaskData;
import org.squale.squalix.util.csv.CSVParser;
import org.squale.squalix.util.parser.LanguageParser;
import org.squale.squalix.util.repository.ComponentRepository;

/**
 * Objet charg� de faire persister les r�sultats RSM
 */
public class RSMPersistor
{

    /** Le template pour le rapport de classe */
    private String mClassTemplate = "csv.template.class";

    /** Le template pour le rapport de classe */
    private String mMethodTemplate = "csv.template.method";

    /**
     * Nombre de m�thodes
     */
    private int mNumberOfMethods = 0;

    /**
     * Nombre de classes
     */
    private int mNumberOfClasses = 0;

    /**
     * Nombre de lignes de commentaires sur le projet
     */
    private int mComments = 0;

    /**
     * Nombre de lignes de commentaires sur le projet
     */
    private int mSLOC = 0;

    /** le nom de la tache r�el, pour diff�rencier java et cpp */
    private String mTaskName;

    /**
     * Configuration
     */
    private RSMConfiguration mConfiguration;

    /**
     * Chemin du fichier � parser
     */
    private String mReportFileName = null;

    /**
     * Logger
     */
    private static final Log LOGGER = LogFactory.getLog( RSMPersistor.class );

    /**
     * Audit durant lequel l'analyse est effectu�e
     */
    private AuditBO mAudit = null;

    /**
     * Projet sur lequel est r�alis�e l'analyse.
     */
    private ProjectBO mProject = null;

    /**
     * Session Persistance
     */
    private ISession mSession = null;

    /** les param�tres temporaires */
    private TaskData mDatas;

    /** le parser CSV */
    private CSVParser mParser;

    /** l'adaptateur permettant de relier les r�sultats RSM au composants associ�s */
    private RSMAdaptator mAdaptator;

    /** pour faciliter le stockage */
    private ComponentRepository mRepository;

    /** Le marqueur pour pr�processer le rapport g�n�r� et en extraire les m�triques de classes */
    private final static String CLASS_LOCATOR = "Class,";

    /** Le marqueur pour pr�processer le rapport g�n�r� et en extraire les m�triques de m�thodes */
    private final static String METHOD_LOCATOR = "Function,";

    /** Marqueur pour signaler la fin de la zone doublon */
    private final static String FILE_LOCATOR = "File,";

    /** Marqueur pour signaler le d�but de la zone doublon */
    private final static String STOP_WRITE = "ProjectFunctionMetrics";

    /** Marqueur pour signaler la fin de la zone doublon */
    private final static String RESTART_WRITE = "ProjectClassMetrics";
    
    /**
     * Cha�ne contenant l'expression r�guli�re permettant de effacer les param�tres 3 � 5 de la ligne m�thode
     */
    private String mREGEXPREMOVEPARAMS = ",[^,]*\\([^,]*\\),[^,]*,[^,]*";
    
    /**
     * Cha�ne contenant l'expression r�guli�re permettant de effacer les param�tres function point attach� aux champs LOC, ELOC, LLOC
     */    
    private String mREGEXPREMOVEFUNCTIONPOINT = ",([^,]*)/[^,]*";

    /**
     * Constructeur.
     * 
     * @param pConfiguration configuration du framework.
     * @param pAudit audit encadrant l'ex�cution.
     * @param pSession la session de persistance utilis�e par la t�che.
     * @param pDatas la liste des param�tres temporaires du projet
     * @param pTaskName le nom de la tache (pour diff�rencier java et cpp)
     * @param pLanguageParser le parser de language pour persister les donn�es
     * @throws JrafDaoException si une session de peristance ne peut �tre cr��e.
     */
    public RSMPersistor( final RSMConfiguration pConfiguration, final AuditBO pAudit, final ISession pSession,
                         TaskData pDatas, String pTaskName, LanguageParser pLanguageParser )
        throws JrafDaoException
    {
        mSession = pSession;
        mConfiguration = pConfiguration;
        mAudit = pAudit;
        mDatas = pDatas;
        mProject = pConfiguration.getProject();
        mRepository = new ComponentRepository( mProject, mSession );
        mTaskName = pTaskName;
        mAdaptator = new RSMAdaptator( pLanguageParser, mRepository );
    }

    /**
     * Parse le rapport pour obtenir tous les m�triques
     * 
     * @param pFileName chemin du fichier rapport.
     * @param pDatas les donn�es
     * @throws Exception si un probl�me de parsing apparait.
     * @return le nombre de m�triques
     */
    public int parseReport( final String pFileName, TaskData pDatas )
        throws Exception
    {
        mReportFileName = pFileName;
        LOGGER.info( RSMMessages.getString( "logs.debug.report_parsing_class" ) + mReportFileName );
        mParser = new CSVParser( RSMMessages.getString( "csv.config.file" ) );

        // effecue les diff�rents pr�processing
        managePreProcess( pFileName );

        int nbClassResults = parseReportForClassMetrics( mConfiguration.getClassReportPath() );
        int nbMethodsResults = parseMethodReport( mConfiguration.getMethodsReportPath(), pDatas );
        return nbClassResults + nbMethodsResults;
    }

    /**
     * Effectur les diff�rents pr�process n�cessaires
     * 
     * @param pFileName le nom du fichier g�n�r� par RSM
     * @throws IOException en cas de problemes lors du pr�-processing
     */
    private void managePreProcess( String pFileName )
        throws IOException
    {
        // On effectue un pr�processing du fichier r�sultat car le format CSV renvoy� n'est pas vraiment du CSV,
        // Il y a des lignes suppl�mentaires et des informations g�nantes pour le parsing
        eraseDouble( pFileName, mConfiguration.getAuxFile() );
        preProcess( mConfiguration.getClassReportPath(), mConfiguration.getAuxFile(), CLASS_LOCATOR );
        preProcess( mConfiguration.getMethodsReportPath(), mConfiguration.getAuxFile(), METHOD_LOCATOR );
    }

    /**
     * @param pCSVOutputFile nom du fichier pars� pour le mettre au format CSV correct
     * @param pCSVOutputFileAux nom du fichier auxilliaire utilis� pour supprimer les doublons
     * @param pMarker la chaine rep�re
     * @throws IOException en cas de problemes avec le pr�processing du fichier
     */
    private void preProcess( String pCSVOutputFile, String pCSVOutputFileAux, String pMarker )
        throws IOException
    {
        // Ecrit a partir du deuxi�me fichier
        File f = new File( pCSVOutputFile );
        BufferedWriter bw = new BufferedWriter( new FileWriter( pCSVOutputFile ) );
        BufferedReader br = new BufferedReader( new FileReader( pCSVOutputFileAux ) );
        String line = "";
        List results = new ArrayList( 0 );
        while ( line != null )
        {
            line = br.readLine();
            // Null signifie fin de fichier
            if ( line != null )
            {
                // Si on a trouv� une ligne commencant par le rep�re pass� en param�tre, on la r�cup�re
                // Pour plus de suret� on v�rifie �galement que la ligne contient une virgule
                // Traitement diff�rent suivant les classes ou les fichiers, pour les classes on ne s'occupe
                // pas du nom du fichier, pour les m�thodes oui
                if ( pMarker.equals( CLASS_LOCATOR ) )
                {
                    if ( isValidCSVLine( line, pMarker ) )
                    {
                        bw.write( line + "\n" );
                    }
                }
                else
                {
                    if ( isValidCSVLine( line, pMarker ) )
                    {
                        results.add( line );
                    }
                    else if ( isFileLine( line ) )
                    {
                        // On est sur la ligne indiquant le fichier dans lequel se trouve tous les �l�ments
                        // obtenus aux lignes pr�c�dentes
                        // on �crit les r�sultats pr�c�dents avec le nom du fichier en plus
                        StringTokenizer st = new StringTokenizer( line, "," );
                        String fileName = "";
                        int counter = 0;
                        while ( st.hasMoreElements() && counter < 2 )
                        {
                            fileName = st.nextToken().trim();
                            counter++;
                        }
                        // on �crit la ligne
                        for ( int i = 0; i < results.size(); i++ )
                        {
                        	// efface le retour � la ligne
                            String result = ( (String) ( results.get( i ) ) ).replaceAll( "\n", "" );

                            // efface param�tres 3 � 5 et function points
                            result = result.replaceFirst(mREGEXPREMOVEPARAMS, "").replaceAll(mREGEXPREMOVEFUNCTIONPOINT, ",$1");
                            
                            // Dans ce cas il faut rajouter un " " sinon le parser ne tient pas compte de la colonne
                            if ( result.charAt( result.length() - 1 ) != ' ' )
                            {                        
                                bw.write( result + " ," + fileName + "\n" );
                            }
                            else
                            {
                                bw.write( result + "," + fileName + "\n" );
                            }
                        }
                        // reset la liste des r�sultats
                        results = new ArrayList( 0 );
                    }
                }
            }
        }
        // ferme les buffers
        bw.close();
        br.close();

    }

    /**
     * @param pLine la ligne courante
     * @return true si la ligne commence par le marqueur de ligne fichier
     */
    private boolean isFileLine( String pLine )
    {
        return pLine.startsWith( FILE_LOCATOR );
    }

    /**
     * @param pFilename le nom du fichier g�n�r� par rsm
     * @param pOutputFileAux nom du fichier auxilliaire utilis� pour supprimer les doublons
     * @throws IOException en cas d'�chec de lecture
     */
    private void eraseDouble( String pFilename, String pOutputFileAux )
        throws IOException
    {
        BufferedWriter bw = new BufferedWriter( new FileWriter( pOutputFileAux ) );
        BufferedReader br = new BufferedReader( new FileReader( pFilename ) );
        String line = "";
        // Supprime les lignes redondantes du � l'utilisation (n�cessaire)
        // de deux options donnant en partie le meme r�sultat
        boolean write = true;
        while ( line != null )
        {
            line = br.readLine();
            // Null signifie fin de fichier
            if ( line != null )
            {
                // on enl�ve tous les espaces (il n'est pas cens� y avoir d'espaces dans un fichier CSV)
                // et dans le fichier renvoy� par RSM il y en a un peu partout
                // Toutefois on enl�ve pas l'espace qui est entre deux virgules sinon le parser ne compte pas la colonne
                String properLine = "";
                for ( int i = 0; i < line.length(); i++ )
                {
                    if ( line.charAt( i ) != ' '
                        || ( line.charAt( i ) == ' ' && i > 0 && i < ( line.length() - 1 )
                            && line.charAt( i - 1 ) == ',' && line.charAt( i + 1 ) == ',' ) )
                    {
                        properLine += line.charAt( i );
                    }
                }
                // Si on a trouv� une ligne commencant par le rep�re pass� en param�tre, on la r�cup�re
                // Pour plus de suret� on v�rifie �galement que la ligne contient une virgule
                if ( properLine.startsWith( STOP_WRITE ) )
                {
                    write = false;
                }
                else
                {
                    if ( properLine.startsWith( RESTART_WRITE ) )
                    {
                        write = true;
                    }
                }
                if ( write && isValidCSVLine( properLine ) )
                {
                    bw.write( properLine + "\n" );
                }
            }
        }
        // ferme les buffers
        bw.close();
        br.close();
    }

    /**
     * Effectue un test plus g�n�ral sans marqueur Sert pour l'�tape 1 du pr�-processing
     * 
     * @param pLine la ligne � tester
     * @return true si la ligne est valide pour pr�processing CSV
     */
    private boolean isValidCSVLine( String pLine )
    {
        // On v�rifie que la ligne comporte les �l�ments n�cessaire � l'analyse CSV
        // Il faut une virgule
        return commonValidCVSLine( pLine )
            && ( pLine.startsWith( CLASS_LOCATOR ) || pLine.startsWith( FILE_LOCATOR ) || pLine.startsWith( METHOD_LOCATOR ) );
    }

    /**
     * Effectue les tests de pr�processing pour r�cup�rer les donn�es qui interressent en fonction du marqueur Sert pour
     * l'�tape 2 du pr�-processing
     * 
     * @param pLine la ligne � tester
     * @param pMarker le rep�re
     * @return true si la ligne est valide pour analyse CSV
     */
    private boolean isValidCSVLine( String pLine, String pMarker )
    {
        // On v�rifie que la ligne comporte les �l�ments n�cessaire � l'analyse CSV
        // Il faut une virgule
        return commonValidCVSLine( pLine )
        // Les contraintes sur la taille
            && pLine.length() > pMarker.length() && pMarker.equals( pLine.substring( 0, pMarker.length() ) );
    }

    /**
     * Permet de traiter les conditions communes qu'une ligne doit avoir pour �tre analys�e par CSV
     * 
     * @param pLine la ligne � v�rifier
     * @return true si la ligne est partiellement valide pour pr�processing CSV
     */
    private boolean commonValidCVSLine( String pLine )
    {
        // On v�rifie que la ligne comporte les �l�ments n�cessaire � l'analyse CSV
        // Il faut une virgule
        return pLine.indexOf( "," ) != -1
            // Les mots cl�s interdits
            && pLine.indexOf( "Average" ) == -1 && pLine.indexOf( "Total" ) == -1 && pLine.indexOf( "Maximum" ) == -1
            && pLine.indexOf( "Minimum" ) == -1;
    }

    /**
     * Parse le rapport pour obtenir les m�triques de classe.
     * 
     * @param pFilename chemin du fichier rapport.
     * @throws Exception si un probl�me de parsing apparait.
     * @return le nombre de r�sultats de niveau classe
     * @roseuid 42B976100269
     */
    private int parseReportForClassMetrics( final String pFilename )
        throws Exception
    {
        // R�cup�rer les beans issus du rapport de classes RSM
        Collection classResults = mParser.parse( RSMMessages.getString( mClassTemplate ), pFilename );
        // On ne compte que les classes qui ont une logique
        Collection classToPersist = new ArrayList( 0 );
        // Ajout de la volum�trie dans les beans
        Iterator it = classResults.iterator();
        RSMClassMetricsBO bo = null;
        while ( it.hasNext() )
        {
            // On adapte chaque bean issu du rapport
            bo = (RSMClassMetricsBO) it.next();
            // On ne compte que les classes qui ont une logique, c'est � dire les classes ayant des m�thodes
            if ( ( bo.getPublicData().intValue() + bo.getProtectedData().intValue() + bo.getPrivateData().intValue() ) > 0 )
            {
                // rattache le bo rsm � la classe associ�e
                mAdaptator.adaptClassResult( bo );
                // on ajoute pour les donn�es projets les donn�es concernant la classe courante
                // au niveau des commentaires et du nombre de lignes de code
                mSLOC += bo.getSloc().intValue();
                mComments += bo.getComments().intValue();
                bo.setAudit( mAudit );
                mNumberOfClasses++;
                bo.setTaskName( mTaskName );
                classToPersist.add( bo );
            }
        }
        LOGGER.info( RSMMessages.getString( "logs.debug.report_parsing_database" ) );
        // On sauvegarde le mesures sur les classes
        MeasureDAOImpl.getInstance().saveAll( mSession, classToPersist );
        mSession.commitTransactionWithoutClose();
        mSession.beginTransaction();
        LOGGER.info( RSMMessages.getString( "logs.debug.report_parsing_end" ) );
        return classResults.size();
    }

    /**
     * Parse le rapport pour obtenir les m�triques de m�thodes
     * 
     * @param pFilename chemin du fichier rapport.
     * @param pDatas les donn�es temporaires
     * @throws Exception si un probl�me de parsing appara�t.
     * @return le nombre de r�sultats de niveau m�thode r�cup�r�s.
     * @roseuid 42B9761F015F
     */
    private int parseMethodReport( final String pFilename, TaskData pDatas )
        throws Exception
    {
        // R�cup�rer les beans issus du rapport de m�thodes RSM
        Collection methodResults = mParser.parse( RSMMessages.getString( mMethodTemplate ), pFilename );
        RSMMethodMetricsBO bo = null;
        String name = null;
        // On cr�e une nouvelle collection contenant les beans � faire effectivement persister
        List resultsToPersist = new ArrayList();
        // Adaptation des beans
        Iterator it = methodResults.iterator();
        while ( it.hasNext() )
        {
            bo = (RSMMethodMetricsBO) it.next();
            // rattache le bo rsm � la classe associ�e
            bo.setAudit( mAudit );
            // Le nom du fichier est mis en relatif par rapport � la racine du projet
            String completFileName = bo.getFileName();
            if (completFileName.indexOf( "vobs" ) != -1) {
            	String fileName = completFileName.substring( completFileName.indexOf( "vobs" ), completFileName.length() );
            	bo.setFileName( fileName );
        	} else {
        		bo.setFileName( completFileName );
        	}
            bo.setTaskName( mTaskName );
            mNumberOfMethods++;
            // Probl�me RSM avec le polymorphisme, si plusieurs m�thodes de meme nom pr�sentes
            // dans le fichier on ne les sauvegarde pas
            boolean canAdd = mAdaptator.adaptMethodResult( bo );
            if ( canAdd )
            {
                resultsToPersist.add( bo );
            }
        } // La collection des r�sultats de m�thodes est persist�e
        LOGGER.info( RSMMessages.getString( "logs.debug.report_parsing_database" ) );
        MeasureDAOImpl.getInstance().saveAll( mSession, resultsToPersist );
        mSession.commitTransactionWithoutClose();
        mSession.beginTransaction();
        LOGGER.info( RSMMessages.getString( "logs.debug.report_parsing_end" ) );
        return methodResults.size();
    }

    /**
     * Cr�e et fait persister les r�sultats de niveau projet.
     * 
     * @roseuid 42E09E5201AB
     */
    public void persistProjectResult()
    {
        LOGGER.info( RSMMessages.getString( "logs.debug.project_database" ) );
        RSMProjectMetricsBO metrics = new RSMProjectMetricsBO();
        // Cr�ation des m�triques de niveau projet
        metrics.setComponent( mProject );
        metrics.setAudit( mAudit );
        metrics.setTaskName( mTaskName );
        metrics.setNumberOfClasses( new Integer( mNumberOfClasses ) );
        metrics.setNumberOfMethods( new Integer( mNumberOfMethods ) );
        metrics.setSloc( new Integer( mSLOC ) );
        metrics.setComments( new Integer( mComments ) );
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