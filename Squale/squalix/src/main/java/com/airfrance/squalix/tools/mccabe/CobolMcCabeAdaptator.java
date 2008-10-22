package com.airfrance.squalix.tools.mccabe;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.airfrance.jraf.commons.exception.JrafDaoException;
import com.airfrance.squalecommon.enterpriselayer.businessobject.component.ClassBO;
import com.airfrance.squalecommon.enterpriselayer.businessobject.component.MethodBO;
import com.airfrance.squalecommon.enterpriselayer.businessobject.result.mccabe.McCabeQAMethodMetricsBO;
import com.airfrance.squalix.util.parser.CobolParser;
import com.airfrance.squalix.util.repository.ComponentRepository;

/**
 * Adaptateur des composants du Cobol.
 */
public class CobolMcCabeAdaptator
{
    /**
     * Logger.
     */
    private static final Log LOGGER = LogFactory.getLog( CobolMcCabeAdaptator.class );

    /**
     * Repository des composants du langage.
     */
    private ComponentRepository mRepository;

    /**
     * Parser du Cobol.
     */
    private CobolParser mParser;

    /**
     * Constructeur.
     * 
     * @param pComponentRepository le repository des composants du langage
     * @param pCobolParser le parser du Cobol
     */
    public CobolMcCabeAdaptator( final ComponentRepository pComponentRepository, final CobolParser pCobolParser )
    {
        mRepository = pComponentRepository;
        mParser = pCobolParser;
    }

    /**
     * Cr�� les composants <code>programme</code> et <code>module</code> correspondant � une ligne du rapport
     * McCabe, associe les m�triques de cette ligne � ces composants, et r�alise la persistence en base des composants.
     * 
     * @param pModuleResult ensemble des m�triques du module devant �tre enregistr�s en base
     * @throws JrafDaoException si exception Jraf
     */
    public String adaptModuleResult( final McCabeQAMethodMetricsBO pModuleResult )
        throws JrafDaoException
    {
        // analyse des noms de programme et de module
        String lFullModuleName = pModuleResult.getComponentName();
        String lFileName = pModuleResult.getFilename();
        List<String> lPrgAndModuleNames = new ArrayList<String>();
        mParser.getPrgAndModuleNamesForModule( lFullModuleName, lPrgAndModuleNames );

        // enregistrement du programme si non pr�sent en base
        ClassBO lPrgBO = mParser.getProgram( lPrgAndModuleNames.get( 0 ), lFileName );
        // enregistrement en base
        mRepository.persisteComponent( lPrgBO );

        // enregistrement du module si non pr�sent en base
        MethodBO lModuleBO = mParser.getModule( lPrgAndModuleNames.get( 1 ), lFileName, lPrgBO );
        lModuleBO.setStartLine( Integer.valueOf( pModuleResult.getStartLine() ).intValue() );
        // enregistrement en base
        mRepository.persisteComponent( lModuleBO );

        // association des r�sultats au module avant enregistrement
        pModuleResult.setComponent( lModuleBO );

        return lPrgAndModuleNames.get( 0 );
    }

}
