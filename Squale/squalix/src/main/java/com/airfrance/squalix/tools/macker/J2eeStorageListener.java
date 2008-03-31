package com.airfrance.squalix.tools.macker;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.airfrance.jraf.commons.exception.JrafDaoException;
import com.airfrance.jraf.spi.persistence.ISession;
import com.airfrance.squalecommon.enterpriselayer.businessobject.component.AbstractComponentBO;
import com.airfrance.squalecommon.enterpriselayer.businessobject.component.ClassBO;
import com.airfrance.squalecommon.enterpriselayer.businessobject.component.JspBO;
import com.airfrance.squalecommon.enterpriselayer.businessobject.component.ProjectBO;
import com.airfrance.squalix.util.file.FileUtility;
import com.airfrance.squalix.util.file.JspFileUtility;
import com.airfrance.squalix.util.parser.J2EEParser;

/**
 *
 */
public class J2eeStorageListener
    extends JavaStorageListener
{

    /** La liste des chemins absolus vers les pages jsps */
    private List mJspPaths;

    /** R�pertoire contenant les jsps compil�es */
    private String mCompiledJsp;

    /** le lien entre le nom des fichiers .java g�n�r�s et le nom initial des jsps */
    private Map mJspMapNames;

    /**
     * Constructeur par d�faut
     * 
     * @param pSession la session
     * @param pProject le projet � auditer
     * @param pConfiguration la configuration Macker
     * @param jspNamesMap le lien entre le nom des fichiers .java g�n�r�s et le nom initial des jsps
     */
    public J2eeStorageListener( ISession pSession, ProjectBO pProject, MackerConfiguration pConfiguration,
                                Map jspNamesMap )
    {
        super( pSession, pProject, pConfiguration );
        this.mParser = new J2EEParser( pProject );
        mJspPaths = pConfiguration.getJsps();
        mCompiledJsp = pConfiguration.getJspRoot();
        mJspMapNames = jspNamesMap;
    }

    /**
     * Cr�er et fait persister la page JSP dont le nom absolu est <code>pFullName</code> si celle-ci appartient au
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
        AbstractComponentBO jspOrClassBO = null;
        String absoluteFileName = null;
        // On r�cup�re le nom absolu du fichier compil� associ� � la classe
        String classFileName = FileUtility.getClassFileName( mFilesToAnalyse, pFullName );
        if ( null != classFileName )
        { // On a trouv� le .class associ�
            File compiledJspDir = new File( mCompiledJsp );
            if ( classFileName.startsWith( compiledJspDir.getCanonicalPath() ) )
            { // Il s'agit d'une jsp compil�e
                // On r�cup�re le nom absolu du fichier parmis les jsps
                // si elle existe et peut �tre persist�e
                absoluteFileName = (String) mJspMapNames.get( pFullName );
                if ( null != absoluteFileName && mIncludedFiles.contains( absoluteFileName ) )
                { // Il faut cr�er une jsp
                    // On r�cup�re la jsp
                    String name =
                        absoluteFileName.substring( absoluteFileName.lastIndexOf( "/" ) + 1,
                                                    absoluteFileName.lastIndexOf( "." ) );
                    int id =
                        JspFileUtility.getJspDirectoryId( mJspPaths, pFullName.substring( 0, pFullName.indexOf( "." ) ) );
                    File rootDir = new File( (String) mJspPaths.get( id ) );
                    String relativeRootDir = FileUtility.getRelativeFileName( rootDir.getAbsolutePath(), mViewPath );
                    String relativeFileName = FileUtility.getRelativeFileName( absoluteFileName, mViewPath );
                    jspOrClassBO = ( (J2EEParser) mParser ).getJsp( name, relativeFileName, relativeRootDir, id );
                    // On fait persister la jsp
                    jspOrClassBO = (JspBO) mRepository.persisteComponent( jspOrClassBO );
                }
            }
            else
            {
                // On v�rifie que le nom absolu du fichier .java correspondant
                // appartient � la liste des fichiers qui peuvent �tre analys�s
                String relativeFileName = isInclude( pFullName );
                if ( null != relativeFileName )
                { // Il faut cr�er une classe
                    // On r�cup�re la classe
                    jspOrClassBO = mParser.getClass( pFullName, relativeFileName );
                    // On fait persister la classe
                    jspOrClassBO = (ClassBO) mRepository.persisteComponent( jspOrClassBO );
                }
            }
        }
        return jspOrClassBO;
    }

    /**
     * True si le nom absolu du fichier jsp correspondant � la classe <code>pFullName</code> appartient � la liste des
     * fichiers qui peuvent �tre analys�s
     * 
     * @param pFullName le nom absolu de la classe JSP
     * @return le nom absolu du fichier dans lequel la jsp est d�finie
     * @throws IOException si erreur de flux
     */
    protected String getAbsoluteFileName( String pFullName )
        throws IOException
    {
        String absoluteFileName = null;
        // On r�cup�re le nom absolu du fichier compil� associ� � la classe
        absoluteFileName = JspFileUtility.getAbsoluteJspFileName( mJspPaths, pFullName );
        if ( null != absoluteFileName )
        {
            // On v�rifie que la jsp peut �tre persist�e
            // On remplace le s�parateur par celui par d�faut
            absoluteFileName = absoluteFileName.replaceAll( "\\\\", "/" );
            String parentName = absoluteFileName.substring( 0, absoluteFileName.lastIndexOf( "/" ) );
            if ( !mIncludedFiles.contains( absoluteFileName ) )
            {
                absoluteFileName = null;
            }
        }
        return absoluteFileName;
    }
}