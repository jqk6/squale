package com.airfrance.squalix.tools.mccabe;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.airfrance.squalix.util.parser.LanguageParser;

/**
 * T�che McCabe pour les projet de langage par objet (Java, C++, etc.).
 */
public abstract class OOMcCabeTask extends AbstractMcCabeTask
{
    /**
     * Logger.
     */
    private static final Log LOGGER = LogFactory.getLog( AbstractMcCabeTask.class );

    /** Le parser */
    protected LanguageParser mParser;

    /**
     * Instance du persisteur McCabe.
     */
    private OOMcCabePersistor mPersistor = null;

    /** Le template de la classe � utiliser. */
    protected String mClassTemplate = "";

    /**
     * Modifie le template niveau classe.
     */
    public abstract void setClassTemplate();

    protected void initialize() throws Exception {
        setClassTemplate();
        super.initialize();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setPersistor() throws Exception {
        mPersistor =
            new OOMcCabePersistor( mParser, mConfiguration, mAudit, getSession(), getData(), mName, mClassTemplate );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void parseReport( final String pReport )
    throws Exception
    {
    String reportFileName = computeReportFileName( pReport );
    // Parser le rapport g�n�r�
    // Il y a deux m�thodes de parsing des rapports, une pour les rapports de m�thodes
    // et l'autre pour les rapports de classe. Ainsi, il suffit que le nom du rapport
    // d�bute par METHOD ou CLASS pour que la bonne m�thode soit s�lectionn�, le reste
    // est laiss� � l'appr�ciation de l'utilisateur. Ceci permet de versionner, dater
    // les noms de rapports.

    if ( pReport.startsWith( McCabeMessages.getString( "reports.profile.class" ) ) )
    {
        mPersistor.parseClassReport( reportFileName );
    }
    else if ( pReport.startsWith( McCabeMessages.getString( "reports.profile.module" ) ) )
    {
        mPersistor.parseMethodReport( reportFileName, mData );
    }
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void persistProjectResult() {
        mPersistor.persistProjectResult();
    }
    
}
