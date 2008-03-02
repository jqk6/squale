package com.airfrance.squalecommon.datatransfertobject.component;

import java.io.Serializable;

import com.airfrance.squalecommon.datatransfertobject.component.parameters.MapParameterDTO;
import com.airfrance.squalecommon.datatransfertobject.config.ProjectProfileDTO;
import com.airfrance.squalecommon.datatransfertobject.config.SourceManagementDTO;
import com.airfrance.squalecommon.datatransfertobject.rule.QualityGridDTO;

/**
 */
public class ProjectConfDTO
    implements Serializable
{

    /**
     * Id du projet
     */
    private long mId = -1;

    /**
     * @dev-squale mettre � jour les champs qui suivent : Peut contenir : - Partie JAVA : - BUILDXML chemin relatif du
     *             fichier ANT build.xml OU - WORKSPACE chemin relatif du workspace - EXCLUDEFROMCOMPIL liste des
     *             fichiers et r�pertoires � exclure de la compilation. - EXCLUDEFROMANALYSE liste des fichiers et
     *             r�pertoires � exclure de l'analyse. - CLASSPATHNAME nom du fichier classpath (".classpath" par
     *             defaut) - PARTIE C++ : - ----------------- A d�finir (Fichier makefile OU commande compl�te
     *             --------------- - - Liste des TREs � exclure / inclure de l'analyse : EXCLUDE_RESULTS - Liste des
     *             fichiers � exclure / inclure de la compilation : EXCLUDE_COMPIL
     */
    private MapParameterDTO mParameters;

    /**
     * Nom du projet
     */
    private String mName;

    /**
     * Nom de la VOB relative au projet
     */
    private String mLocation;

    /**
     * Type de l'application (Java, CPP, ...)
     */
    private ProjectProfileDTO mProfile;

    /**
     * Le statut du projet
     */
    private int mStatus;

    /**
     * R�cup�rateur de source
     */
    private SourceManagementDTO mSourceManager;

    /** Grille qualit� */
    private QualityGridDTO mQualityGrid;

    /**
     * Constructeur par defaut
     * 
     * @roseuid 42CB92BE01EA
     */
    public ProjectConfDTO()
    {

    }

    /**
     * Access method for the mPreRequisites property.
     * 
     * @return the current value of the mPreRequisites property
     * @roseuid 42CB92BE0213
     */
    public MapParameterDTO getParameters()
    {
        return mParameters;
    }

    /**
     * Sets the value of the mPreRequisites property.
     * 
     * @param pParameters the new value of the mPreRequisites property
     * @roseuid 42CB92BE0227
     */
    public void setParameters( MapParameterDTO pParameters )
    {
        mParameters = pParameters;
    }

    /**
     * Access method for the mProjectName property.
     * 
     * @return the current value of the mProjectName property
     * @roseuid 42CB92BE0277
     */
    public String getName()
    {
        return mName;
    }

    /**
     * Sets the value of the mProjectName property.
     * 
     * @param pName the new value of the mProjectName property
     * @roseuid 42CB92BE028B
     */
    public void setName( String pName )
    {
        mName = pName;
    }

    /**
     * Access method for the mVOBName property.
     * 
     * @return the current value of the mVOBName property
     * @roseuid 42CB92BE02EF
     */
    public String getLocation()
    {
        return mLocation;
    }

    /**
     * Sets the value of the mLocation property.
     * 
     * @param pLocation the new value of the mLocation property
     * @roseuid 42CB92BE0303
     */
    public void setLocation( String pLocation )
    {
        mLocation = pLocation;
    }

    /**
     * Access method for the mId property.
     * 
     * @return the current value of the mId property
     * @roseuid 42CB92BE037B
     */
    public long getId()
    {
        return mId;
    }

    /**
     * Sets the value of the mId property.
     * 
     * @param pId the new value of the mId property
     * @roseuid 42CB92BE03A3
     */
    public void setId( long pId )
    {
        mId = pId;
    }

    /**
     * Access method for the mProfile property.
     * 
     * @return the current value of the mProfile property
     */
    public ProjectProfileDTO getProfile()
    {
        return mProfile;
    }

    /**
     * Sets the value of the mProfile property.
     * 
     * @param pProfile the new value of the mProfile property
     */
    public void setProfile( ProjectProfileDTO pProfile )
    {
        mProfile = pProfile;
    }

    /**
     * @return grille qualit�
     */
    public QualityGridDTO getQualityGrid()
    {
        return mQualityGrid;
    }

    /**
     * @param pGridDTO grille qualit�
     */
    public void setQualityGrid( QualityGridDTO pGridDTO )
    {
        mQualityGrid = pGridDTO;
    }

    /**
     * @return le nom du r�cup�rateur de ressources
     */
    public SourceManagementDTO getSourceManager()
    {
        return mSourceManager;
    }

    /**
     * @param pSourceManager le nouveau r�cup�rateur de ressource
     */
    public void setSourceManager( SourceManagementDTO pSourceManager )
    {
        mSourceManager = pSourceManager;
    }

    /**
     * @return le statut du projet
     */
    public int getStatus()
    {
        return mStatus;
    }

    /**
     * @param pStatus le statut du projet
     */
    public void setStatus( int pStatus )
    {
        mStatus = pStatus;
    }

}
