package com.airfrance.squalix.tools.macker;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import com.airfrance.jraf.commons.exception.JrafDaoException;
import com.airfrance.jraf.spi.persistence.ISession;
import com.airfrance.squalecommon.enterpriselayer.businessobject.component.AbstractComponentBO;
import com.airfrance.squalecommon.enterpriselayer.businessobject.component.ClassBO;
import com.airfrance.squalecommon.enterpriselayer.businessobject.component.ProjectBO;
import com.airfrance.squalecommon.enterpriselayer.businessobject.result.rulechecking.RuleCheckingTransgressionBO;
import com.airfrance.squalecommon.enterpriselayer.businessobject.result.rulechecking.RuleCheckingTransgressionItemBO;
import com.airfrance.squalecommon.enterpriselayer.facade.macker.RuleFactory;
import com.airfrance.squalix.util.file.FileUtility;
import com.airfrance.squalix.util.parser.JavaParser;
import com.airfrance.squalix.util.repository.ComponentRepository;

import net.innig.macker.event.AccessRuleViolation;
import net.innig.macker.event.ListenerException;
import net.innig.macker.event.MackerEvent;
import net.innig.macker.event.MackerEventListener;
import net.innig.macker.event.MackerIsMadException;
import net.innig.macker.rule.RuleSet;

/**
 * R�cup�re toutes les transgressions des r�gles Macker
 */
public class JavaStorageListener
    implements MackerEventListener
{

    /** S�parateur pour la classe concern� par la transgression */
    public static final String FROM_SEPARATOR = " : ";

    /** S�parateur pour la classe en relation avec la transgression */
    public static final String TO_SEPARATOR = " -> ";

    /** La liste des chemins absolus vers les sources */
    protected List mPaths;

    /** La liste des fichiers inclus */
    protected List mIncludedFiles;

    /** Le chemin de la vue */
    protected String mViewPath;

    /** Contient le d�tails des transgressions */
    protected HashMap mDetails;

    /** Contient le nombre de transgressions par r�gle */
    protected HashMap mNbOcc;

    /** Les .class a analyser */
    protected HashSet mFilesToAnalyse;

    /** Parser java */
    protected JavaParser mParser;

    /** Aide � la cr�ation de composants */
    protected ComponentRepository mRepository;

    /**
     * Constructeur par d�faut
     * 
     * @param pSession la session
     * @param pProject le projet � auditer
     * @param pConfiguration la configuration Macker
     */
    public JavaStorageListener( ISession pSession, ProjectBO pProject, MackerConfiguration pConfiguration )
    {
        mIncludedFiles = pConfiguration.getIncludedFiles();
        mViewPath = pConfiguration.getRoot();
        mFilesToAnalyse = pConfiguration.getFilesToAnalyze();
        mParser = new JavaParser( pProject );
        mRepository = new ComponentRepository( pProject, pSession );
        mNbOcc = new HashMap();
        mDetails = new HashMap();
        mPaths = pConfiguration.getSources();

    }

    /**
     * @return le nombre de transgressions par r�gles
     */
    public HashMap getNbOcc()
    {
        return mNbOcc;
    }

    /**
     * @return les d�tails des transgressions
     */
    public HashMap getDetails()
    {
        return mDetails;
    }

    /**
     * {@inheritDoc}
     * 
     * @see net.innig.macker.event.MackerEventListener#mackerStarted(net.innig.macker.rule.RuleSet)
     */
    public void mackerStarted( RuleSet arg0 )
        throws ListenerException
    {
    }

    /**
     * {@inheritDoc}
     * 
     * @see net.innig.macker.event.MackerEventListener#mackerFinished(net.innig.macker.rule.RuleSet)
     */
    public void mackerFinished( RuleSet arg0 )
        throws MackerIsMadException, ListenerException
    {
    }

    /**
     * {@inheritDoc}
     * 
     * @see net.innig.macker.event.MackerEventListener#mackerAborted(net.innig.macker.rule.RuleSet)
     */
    public void mackerAborted( RuleSet arg0 )
    {
    }

    /**
     * {@inheritDoc}
     * 
     * @see net.innig.macker.event.MackerEventListener#handleMackerEvent(net.innig.macker.rule.RuleSet,
     *      net.innig.macker.event.MackerEvent)
     */
    public void handleMackerEvent( RuleSet pRuleSet, MackerEvent pEvent )
        throws MackerIsMadException, ListenerException
    {
        // L'erreur est un acc�s ill�gal
        if ( pEvent instanceof AccessRuleViolation )
        {
            // On r�cup�re l'�v�nement
            AccessRuleViolation violation = (AccessRuleViolation) pEvent;
            // On r�cup�re les classes en relation avec la transgression
            // si elles existent
            AbstractComponentBO from = null;
            AbstractComponentBO to = null;
            try
            {
                from = getComponent( violation.getFrom().getFullName() );
                to = getComponent( violation.getTo().getFullName() );
            }
            catch ( JrafDaoException e )
            {
                throw new ListenerException( this, e.toString() );
            }
            catch ( IOException e )
            {
                throw new ListenerException( this, e.toString() );
            }
            // Si la classe from n'est pas nulle (donc existe dans les sources
            // non exclues du projet), on ajoute la transgression:
            if ( null != from )
            {
                // On r�cup�re le code de la r�gle si il existe sinon on met le code
                // par d�faut
                String code = RuleFactory.CODE;
                Iterator it = violation.getMessages().iterator();
                if ( it.hasNext() )
                {
                	// Delete all spaces in message for future comparison
                    code = ((String) it.next()).trim();
                }
                // On r�cup�re le nombre de transgression pour cette r�gle
                Integer nbOcc = (Integer) mNbOcc.get( code );
                if ( null == nbOcc )
                {
                    nbOcc = new Integer( 0 );
                }
                // On ajoute une occurence
                nbOcc = new Integer( nbOcc.intValue() + 1 );
                // On enregistre la modification ou la cr�ation
                mNbOcc.put( code, nbOcc );
                /* On ajoute le d�tail de cette transgression � la liste des d�tails */
                RuleCheckingTransgressionItemBO item = new RuleCheckingTransgressionItemBO();
                item.setComponent( from );
                // On modifie le composant en relation avec la transgression
                item.setComponentInvolved( to );
                String message = code;
                // On concat�ne le nom des classes concern�es par la transgression
                // au message avec des s�parateurs universels pour pouvoir les r�cup�rer
                // par la suite pour l'affichage web.
                message += FROM_SEPARATOR + violation.getFrom().getFullName();
                message += TO_SEPARATOR + violation.getTo().getFullName();
                item.setMessage( message );
                // On r�cup�re les items li�s � la r�gle
                ArrayList items = (ArrayList) mDetails.get( code );
                if ( null == items )
                {
                    items = new ArrayList();
                }
                // On ajoute le nouvel item � la liste seulement si le nombres des items
                // pour la r�gle est < 100
                if ( items.size() < RuleCheckingTransgressionBO.MAX_DETAILS )
                {
                    items.add( item );
                }
                // On ajoute la liste des items dans la map
                mDetails.put( code, items );
            }
        }
    }

    /**
     * Cr�er et fait persister la classe dont le nom absolu est <code>pFullName</code> si celle-ci appartient au
     * projet � auditer et n'appartient pas � un des r�pertoires exclus.
     * 
     * @param pFullName le nom absolu de la classe
     * @throws IOException si erreur de flux
     * @throws JrafDaoException si erreur de persistance
     * @return le composant persist�
     */
    protected AbstractComponentBO getComponent( String pFullName )
        throws IOException, JrafDaoException
    {
        ClassBO classBO = null;
        // On r�cup�re le nom relatif du fichier source si il est pr�sent
        // dans les sources.
        String relativeFileName = isInclude( pFullName );
        if ( null != relativeFileName )
        { // La classe appartient bien au projet � auditer
            // On r�cup�re la classe
            classBO = mParser.getClass( pFullName, relativeFileName );
            // On fait persister la classe et on construit la mesure ckjm associ�e
            classBO = (ClassBO) mRepository.persisteComponent( classBO );
        }
        return classBO;
    }

    /**
     * Le nom relatif du fichier dans lequel la classe est d�finis si il fait parti des fichiers inclus, null sinon
     * 
     * @param pFullName le nom absolu de la classe
     * @return le nom relatif du fichier dans lequel la classe est d�finie
     * @throws IOException si erreur de flux
     */
    protected String isInclude( String pFullName )
        throws IOException
    {
        String relativeFileName = null;
        // On r�cup�re le fichier compil� associ� � la classe
        String classFileName = FileUtility.getClassFileName( mFilesToAnalyse, pFullName );
        if ( null != classFileName )
        { // On a trouv� le .class associ�
            // Le nom du fichier source dans lequel se trouve la d�finition de la classe
            String fileName = FileUtility.getFileName( classFileName );
            // On v�rifie que la classe n'appartient pas � un r�pertoire exclu
            String packageName = mParser.getAbsolutePackage( pFullName );
            // On construit le chemin relatif du fichier source de la classe
            String endOfFileName = FileUtility.buildEndOfFileName( packageName, fileName );
            // On r�cup�re le nom absolu du fichier source si il est pr�sent
            // dans les param�tres.
            String absoluteFileName = FileUtility.getAbsoluteFileName( mPaths, endOfFileName );
            if ( mIncludedFiles.contains( absoluteFileName ) )
            {
                relativeFileName = FileUtility.getRelativeFileName( absoluteFileName, mViewPath );
            }
        }
        return relativeFileName;
    }
}
