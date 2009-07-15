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
package org.squale.squalix.tools.jdepend;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.regex.PatternSyntaxException;

import jdepend.framework.JDepend;
import jdepend.framework.JavaPackage;
import jdepend.framework.PackageFilter;

import org.squale.squalecommon.daolayer.result.MeasureDAOImpl;
import org.squale.squalecommon.enterpriselayer.businessobject.component.PackageBO;
import org.squale.squalecommon.enterpriselayer.businessobject.component.parameters.ListParameterBO;
import org.squale.squalecommon.enterpriselayer.businessobject.component.parameters.ParametersConstants;
import org.squale.squalecommon.enterpriselayer.businessobject.result.jdepend.JDependPackageMetricsBO;
import org.squale.squalix.core.AbstractTask;
import org.squale.squalix.core.TaskData;
import org.squale.squalix.core.TaskException;
import org.squale.squalix.util.buildpath.BuildProjectPath;
import org.squale.squalix.util.file.FileUtility;
import org.squale.squalix.util.parser.JavaParser;
import org.squale.squalix.util.repository.ComponentRepository;

/**
 * T�che JDepend pour l'analyse des packages java
 */
public class JDependTask
    extends AbstractTask
{

    /** Aide � la cr�ation de composants */
    private ComponentRepository mRepository;

    /** l'outil jdepend */
    private JDepend mJDepend;

    /** le filtre pour construire les packages valides */
    private JDependFilter mFilter;

    /** l'ensemble des packages que l'on consid�re comme valides */
    private ArrayList mValidPackages;

    /**
     * liste des sources � analyser (en tenant compte des exclusions et inclusions, (r�cup�r�es car r�utilis�es)
     */
    private List mIncludedFileNames = new ArrayList( 0 );

    // Cette tache n'intervient pas dans le calcul de la taille du file system

    /**
     * Constructeur par defaut
     */
    public JDependTask()
    {
        mName = "JDependTask";
        mValidPackages = new ArrayList();
        mFilter = new JDependFilter();

    }

    /**
     * {@inheritDoc}
     * 
     * @see org.squale.squalix.core.Task#execute()
     */
    public void execute()
        throws TaskException
    {
        try
        {
            // Le chemin racine du projet (vue clearcase, ...)
            String viewPath = (String) mData.getData( TaskData.VIEW_PATH );
            // la liste des sources
            List srcs =
                (List) ( (ListParameterBO) mProject.getParameters().getParameters().get( ParametersConstants.SOURCES ) ).getParameters();

            // Les nom des fichiers qui peuvent �tre persist�s
            // (on ne veut pour cette t�che que les .java)
            mIncludedFileNames =
                FileUtility.getIncludedFiles(
                                              viewPath,
                                              BuildProjectPath.buildProjectPath( viewPath, srcs ),
                                              (ListParameterBO) mProject.getParameter( ParametersConstants.INCLUDED_PATTERNS ),
                                              (ListParameterBO) mProject.getParameter( ParametersConstants.EXCLUDED_PATTERNS ),
                                              (ListParameterBO) mProject.getParameter( ParametersConstants.EXCLUDED_DIRS ),
                                              new String[] { ".java" } );
            // On va filtrer les packages de l'api java pour les exclure de l'analyse
            PackageFilter filter = new PackageFilter();
            filter.addPackage( "java.*" );
            filter.addPackage( "javax.*" );
            filter.addPackage( "org.ietf.jgss" );
            filter.addPackage( "org.omg.*" );
            filter.addPackage( "org.w3c.*" );
            filter.addPackage( "org.xml.*" );
            mJDepend = new JDepend( filter );
            mRepository = new ComponentRepository( mProject, getSession() );
            // on r�cup�re les r�pertoires o� se trouvent les .class
            List classesDirs = (List) mData.getData( TaskData.CLASSES_DIRS );
            // on affecte ces r�pertoires comme �tant les r�pertoires
            // racine pour l'analyse
            for ( int i = 0; i < classesDirs.size(); i++ )
            {
                mJDepend.addDirectory( (String) classesDirs.get( i ) );
            }
            // r�cup�re l'ensemble des packages d�finis pour l'arborescence dont
            // on a d�fini la racine ci dessus
            Collection packages = mJDepend.analyze();
            Iterator iter = packages.iterator();
            JDependPackageMetricsBO measure;
            Collection measuresColl = new ArrayList( 0 );
            JavaParser parser = new JavaParser( mProject );
            // on cr�� d'abord la liste des packages � prendre en compte
            // en mettant la chaine vide comme parent root
            // on lance la construction des r�pertoires valides
            for ( int i = 0; i < classesDirs.size(); i++ )
            {
                buildValidPackagesSet( new File( (String) classesDirs.get( i ) ), "" );
            }
            while ( iter.hasNext() )
            {
                JavaPackage p = (JavaPackage) iter.next();
                if ( mValidPackages.contains( p.getName() ) )
                {
                    measure = new JDependPackageMetricsBO();
                    // on affecte toutes les mesures
                    measure.setCa( p.afferentCoupling() );
                    measure.setCe( p.efferentCoupling() );
                    measure.setNumberOfClassesAndInterfaces( p.getClassCount() );
                    // pour les metrics flottantes,
                    // on prend des valeurs enti�res en pourcentage (x100)
                    final int TOINT = 100;
                    measure.setAbstractness( p.abstractness() * TOINT );
                    measure.setDistance( p.distance() * TOINT );
                    measure.setInstability( p.instability() * TOINT );
                    // gestion du cycle �ventuel
                    measure.setHaveCycles( p.containsCycle() );
                    List list = new ArrayList( 0 );
                    if ( p.containsCycle() )
                    {
                        // collecte les cycles et les mets dans la liste
                        p.collectCycle( list );
                        // pour faciliter l'affichage on a une m�thode interm�diaire
                        // qui met en forme
                        measure.setCycle( getCycle( list ) );
                    }
                    // cr�e le composant et le fait persister
                    PackageBO packBO = parser.getPackage( p.getName() );
                    measure.setComponent( mRepository.persisteComponent( packBO ) );
                    measure.setAudit( mAudit );
                    measure.setTaskName( mName );
                    measuresColl.add( measure );
                }
            }
            // persiste l'ensemble des mesures
            MeasureDAOImpl.getInstance().saveAll( getSession(), measuresColl );
        }
        catch ( Exception e )
        {
            throw new TaskException( e );
        }
    }

    /**
     * M�thode qui cr�e la liste des packages consid�r�s comme valide, c'est � dire qu'ils font partie de l'arborescence
     * directe du r�pertoire racine � analyser. Par cons�quent on ne prend pas en compte les packages "java.*" ...
     * 
     * @param pRootFile le fichier racine de l'arborescence � parcourir
     * @param pParent le nom du package parent
     * @throws IOException en cas de probl�mes de lecture du fichier
     */
    private void buildValidPackagesSet( File pRootFile, String pParent )
        throws IOException
    {
        // pRootFile est forc�ment un r�pertoire
        // grace au filtre pour les appels r�cursifs et par appel originel aussi
        if ( pRootFile.canRead() )
        {
            // On r�cup�re tous les dossiers et les fichiers correspondant au pattern
            // des fichiers � analyser
            File[] list = pRootFile.listFiles( mFilter );
            // il faut v�rifier l'appartenance aux sources meme si on sait que le parent pr�sum�
            // appartenait aux sources car il y a des packages communs � plusieurs projets
            // ("com" par exemple) et il faut etre sur que le parent du package courant
            // qui appartient aux sources
            // ou si il n'y a pas de r�pertoire en dessous
            // ou si il y a pas que des repertoires en dessous
            // et que le repertoire courant fait partie des sources
            if ( ( ( list.length == 0 || ( list.length != 0 && pRootFile.listFiles().length != 0 ) ) && isMemberOfParameterList(
                                                                                                                                 mIncludedFileNames,
                                                                                                                                 pParent.replace(
                                                                                                                                                  '.',
                                                                                                                                                  '/' ) ) ) )
            {
                // on ajoute l'ensemble des r�pertoires de l'arborescence
                mValidPackages.add( pParent );
            }
            String filename = null;
            for ( int i = 0; i < list.length; i++ )
            {
                // pour m�moriser le parent courant afin de le conserver
                // pour faire l'appel r�cursif sur chaque enfant
                String newParent = pParent;
                filename = list[i].getName();
                File f = new File( filename );
                // il ne faut pas ajouter un "." au d�but
                if ( !newParent.equals( "" ) )
                {
                    newParent += "." + filename;
                }
                else
                {
                    newParent = filename;
                }
                // et on recommence sur chaque fils
                buildValidPackagesSet( list[i], newParent );
                // r�attribue la bonne valeur
                newParent = pParent;


            }
        }
    }

    /**
     * indique si le fichier ou le r�pertoire dont le nom est pSuffix appartient � l'ensemble des sources ou des
     * r�pertoires exclus (suivant la liste pass�e en argument) en effectuant une recherche r�cursive.
     * 
     * @param pSuffix le nom du package transform� dont les "." ont �t� remplac�s par des "/"
     * @param pList la liste des noms absolus des fichiers le cas d'utilisation
     * @return un bool�en correspondant
     */
    private boolean isMemberOfParameterList( List pList, String pSuffix )
    {
        boolean result = false;
        for ( int i = 0; !result && !result && i < pList.size(); i++ )
        {
            // src ne peut �tre que le nom d'un fichier
            String src = (String) pList.get( i );
            // on teste l'appartenance � la fois des r�pertoires ou des fichiers
            try
            {
                if ( !pSuffix.equals( "" ) && src.matches( ".*/" + pSuffix + "/.*\\..*" ) )
                {
                    // on a trouv� le fichier ou le r�pertoire dans l'arborescence
                    result = true;
                }
            }
            catch ( PatternSyntaxException pse )
            {
                // Il ne peut donc pas s'agir d'un package
                result = false;
            }
        }
        return result;
    }

    /**
     * @return l'outil jDepend utilis�
     */
    public JDepend getJDepend()
    {
        return mJDepend;
    }

    /**
     * @param pJDepend le nouveau JDepend
     */
    public void setJDepend( JDepend pJDepend )
    {
        mJDepend = pJDepend;
    }

    /**
     * @param pList la liste des packages
     * @return le cycle sous une forme affichable
     */
    private String getCycle( List pList )
    {
        final int MAX_LEN = 2000;
        String result = "";
        // le package qui est la cause du cycle
        String cycleName = ( (JavaPackage) pList.get( pList.size() - 1 ) ).getName();
        for ( int i = 0; i < pList.size() && result.length() < MAX_LEN; i++ )
        {
            JavaPackage pkg = (JavaPackage) pList.get( i );
            String name = pkg.getName();
            // juste de la mise en forme d'affichage
            // suivant la fa�on dont JDepend r�cup�re les informations
            // cf source JDepend pour la mise en forme
            if ( cycleName.equals( name ) )
            {
                result += "|->" + name + "\n";
            }
            else
            {
                result += "|  " + name + "\n";
            }
        }
        // retourne le cycle sous une forme affichable sur le web
        if ( result.length() >= MAX_LEN )
        {
            result += "\n...";
        }
        return result;
    }

}