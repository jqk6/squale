package com.airfrance.squalix.tools.compiling.java.parser.rsa;

import com.airfrance.squalix.tools.compiling.CompilingMessages;
import com.airfrance.squalix.tools.compiling.java.parser.wsad.JWSADParserConfiguration;

/**
 * Cette classe permet de r�cup�rer la configuration relative au parser de fichiers de classpath pour les projets RSA7
 */
public class JRSAParserConfiguration
    extends JWSADParserConfiguration
{
    /**
     * Valeur "con" pour l'attribut kind.
     */
    private String mCon = "con";

    /**
     * Nom du projet EAR (peut �tre vide)
     */
    private String mEARProject = "";

    /**
     * Chemin vers le fichier Manifest.mf du projet
     */
    private String mManifestPath = "";

    /**
     * Chemin vers le fichier contenant les propri�t�s d'un projet J2EE (sert � r�cup�rer le r�pertoire d'application
     * qui est par d�faut WebContent)
     */
    private String mWebSettings = "";

    /**
     * Constructeur par d�faut
     * 
     * @throws Exception exception
     */
    public JRSAParserConfiguration()
        throws Exception
    {
        super();
    }

    /**
     * {@inheritDoc}
     * 
     * @see com.airfrance.squalix.tools.compiling.java.parser.wsad.JWSADParserConfiguration#createReflectionMap()
     */
    protected void createReflectionMap()
        throws Exception
    {
        super.createReflectionMap();
        /*
         * valeur "con" possible pour l'attribut "kind" pour la balise <classpathentry>
         */
        mMap.put( CompilingMessages.getString( "configuration.java.parsing.wsad.values.value.name.con" ),
                  this.getClass().getDeclaredMethod( "setCon", new Class[] { String.class } ) );
    }

    /**
     * @return la valeur "con" pour l'attribut kind.
     */
    public String getCon()
    {
        return mCon;
    }

    /**
     * @param pValue valeur "con" pour l'attribut kind.
     */
    public void setCon( String pValue )
    {
        mCon = pValue;
    }

    /**
     * @return le chemin vers le fichier de configuration web � parser
     */
    public String getWebSettings()
    {
        return mWebSettings;
    }

    /**
     * @param pWebSettings le chemin vers le fichier de configuration web � parser
     */
    public void setWebSettings( String pWebSettings )
    {
        mWebSettings = pWebSettings;
    }

    /**
     * @return le nom du projet EAR
     */
    public String getEARProject()
    {
        return mEARProject;
    }

    /**
     * @return le chemin vers le manifest
     */
    public String getManifestPath()
    {
        return mManifestPath;
    }

    /**
     * @param pEARProject le nom du projet EAR
     */
    public void setEARProject( String pEARProject )
    {
        mEARProject = pEARProject;
    }

    /**
     * @param pManifestPath le chemin vers le manifest
     */
    public void setManifestPath( String pManifestPath )
    {
        mManifestPath = pManifestPath;
    }

}
