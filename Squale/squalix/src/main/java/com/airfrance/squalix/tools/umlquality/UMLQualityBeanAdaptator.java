package com.airfrance.squalix.tools.umlquality;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.airfrance.squalecommon.enterpriselayer.businessobject.component.AbstractComplexComponentBO;
import com.airfrance.squalecommon.enterpriselayer.businessobject.component.AbstractComponentBO;
import com.airfrance.squalecommon.enterpriselayer.businessobject.component.ProjectBO;
import com.airfrance.squalecommon.enterpriselayer.businessobject.result.umlquality.UMLQualityMetricsBO;
import com.airfrance.squalecommon.enterpriselayer.businessobject.result.umlquality.UMLQualityModelMetricsBO;
import com.airfrance.squalecommon.enterpriselayer.businessobject.result.umlquality.UMLQualityPackageMetricsBO;
import com.airfrance.squalix.util.repository.ComponentRepository;

/**
 * Prend en charge de remplacer tous les noms des composants par les objets
 * 
 * @author sportorico
 */
public class UMLQualityBeanAdaptator
{
    /**
     * Contient la liste des composant complexe
     */
    private Map mComplexeComponents = null;

    /**
     * Projet sur lequel est r�alis�e l'analyse.
     */
    private ProjectBO mProject = null;

    /**
     * Repository
     */
    private ComponentRepository mRepository;

    /**
     * Logger
     */
    private static final Log LOGGER = LogFactory.getLog( UMLQualityBeanAdaptator.class );

    /**
     * Constructeur
     * 
     * @param pProject le projet sur lequel est r�alis�e l'analyse.
     * @param pRepository le repository
     */
    public UMLQualityBeanAdaptator( final ProjectBO pProject, ComponentRepository pRepository )
    {
        mProject = pProject;
        mRepository = pRepository;
        mComplexeComponents = new HashMap();

    }

    /**
     * Adapte le bean des r�sultats des composants UML et le fait persister.<br>
     * En utilisant nom du composant r�cup�r� depuis les rapports(resultats)UMLQuality, on cr�e<br>
     * la relation avec le composant ad�quat.<br>
     * 
     * @param pComponentResult Ensemble de r�sultats devant �tre modifi�s et persist�s.
     * @return true si le resultat a �t� adapt� si non false
     * @throws Exception si le composant UML n'a pas �t� instancier
     */
    public boolean adaptComponentResult( final UMLQualityMetricsBO pComponentResult )
        throws Exception
    {
        LOGGER.debug( UMLQualityMessages.getString( "logs.debug.adapt_composant" )
            + pComponentResult.getComponentName() );

        boolean result = false; // resultat de la methode
        String name = null; // nom du composant
        String parentName = null; // nom du parent direct
        AbstractComponentBO component = null, adaptedComponent; // un composant UML

        name = UMLQualityUtility.getName( pComponentResult.getComponentName() );// recupere le nom du composant
        // sur lequel les resultats(m�triques)
        // a �t� g�n�r�s
        if ( name != null )
        {// nom non vide
            parentName = UMLQualityUtility.getParentName( pComponentResult.getComponentName() );// le nom du parent
                                                                                                // direct

            if ( null != parentName )
            {// le parent direct n'existe pas
                AbstractComplexComponentBO conplexComponentBO =
                    (AbstractComplexComponentBO) mComplexeComponents.get( parentName );// recherche du parent direct
                if ( null != conplexComponentBO )
                {// le parent direct existe
                    /* on instancie un composant */
                    component =
                        UMLQualityUtility.newInstance( UMLQualityMessages.getString( "uml.component.package.name" )
                            + UMLQualityMessages.getString( "uml.component.prefix.name" )
                            + UMLQualityUtility.getUmlComponentName( pComponentResult.getClass().getName() )
                            + UMLQualityMessages.getString( "uml.component.sufix.name" ), name );

                    if ( component instanceof AbstractComplexComponentBO )
                    {
                        mComplexeComponents.put( pComponentResult.getComponentName(), component );

                    }

                    component.setParent( conplexComponentBO );// �tablie le lien de parent� entre deux composants
                    adaptedComponent = mRepository.persisteComponent( component );

                    pComponentResult.setComponent( adaptedComponent );// �tablie le lien entre resutat(metrique) et
                                                                        // composant

                    if ( pComponentResult instanceof UMLQualityPackageMetricsBO )
                    {
                        // on ne persiste pas les mesures UMLQualityPackage
                        result = false;
                    }
                    else
                    {
                        result = true;
                    }

                }
            }
            else
            {
                // si c'est le resultat(m�trique)d'un composant UML racine (Model)
                if ( pComponentResult instanceof UMLQualityModelMetricsBO )
                {

                    component =
                        UMLQualityUtility.newInstance( UMLQualityMessages.getString( "uml.component.package.name" )
                            + UMLQualityMessages.getString( "uml.component.prefix.name" )
                            + UMLQualityUtility.getUmlComponentName( pComponentResult.getClass().getName() )
                            + UMLQualityMessages.getString( "uml.component.sufix.name" ), name );

                    mComplexeComponents.put( pComponentResult.getComponentName(), component );

                    component.setParent( mProject );// �tablie le lien de parent� entre le composant
                    // et le sous-projet
                    adaptedComponent = mRepository.persisteComponent( component );

                    pComponentResult.setComponent( adaptedComponent );
                    result = true;
                }
            }
        }
        return result;
    }

}
