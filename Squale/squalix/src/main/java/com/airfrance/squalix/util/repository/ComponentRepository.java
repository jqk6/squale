package com.airfrance.squalix.util.repository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.airfrance.jraf.commons.exception.JrafDaoException;
import com.airfrance.jraf.spi.persistence.ISession;
import com.airfrance.squalecommon.daolayer.component.AbstractComponentDAOImpl;
import com.airfrance.squalecommon.daolayer.component.MethodDAOImpl;
import com.airfrance.squalecommon.enterpriselayer.businessobject.component.AbstractComplexComponentBO;
import com.airfrance.squalecommon.enterpriselayer.businessobject.component.AbstractComponentBO;
import com.airfrance.squalecommon.enterpriselayer.businessobject.component.ClassBO;
import com.airfrance.squalecommon.enterpriselayer.businessobject.component.JspBO;
import com.airfrance.squalecommon.enterpriselayer.businessobject.component.MethodBO;
import com.airfrance.squalecommon.enterpriselayer.businessobject.component.PackageBO;
import com.airfrance.squalecommon.enterpriselayer.businessobject.component.ProjectBO;
import com.airfrance.squalecommon.enterpriselayer.businessobject.component.UmlClassBO;
import com.airfrance.squalecommon.enterpriselayer.businessobject.component.UmlInterfaceBO;
import com.airfrance.squalecommon.enterpriselayer.businessobject.component.UmlModelBO;
import com.airfrance.squalecommon.enterpriselayer.businessobject.component.UmlPackageBO;

/**
 * Charger de faire persister les composants.
 */
public class ComponentRepository
{

    /**
     * S�parateur des champs de cl�s. Ne doit pas �tre un s�parateur utilis� par McCabe pour qualifier enti�rement un
     * nom.
     */
    private static final String KEY_SEPARATOR = "<=>";

    /**
     * Session de persistance.
     */
    private ISession mSession = null;

    /**
     * Contient la liste des classes du projet
     */
    private Map mClasses;

    /**
     * Contient la liste des m�thodes du projet
     */
    private Map mMethods;

    /**
     * Liste des packages/namespaces du projet
     */
    private Map mPackages;

    /**
     * Liste des Mod�les UML
     */
    private Map mUmlModels;

    /**
     * Liste des packages UML
     */
    private Map mUmlPackages;

    /**
     * Liste des Interfaces UML
     */

    private Map mUmlInterface;

    /**
     * Contient la liste des classes UML du mod�le
     */
    private Map mUmlClasses;

    /**
     * Contient la liste des pages JSP
     */
    private Map mJSPs;

    /**
     * Projet sur lequel est r�alis�e l'analyse.
     */
    private ProjectBO mProject;

    /**
     * Constructeur
     * 
     * @param pProject le projet sur lequel est r�alis�e l'analyse.
     * @param pSession la session de persistance
     */
    public ComponentRepository( ProjectBO pProject, ISession pSession )
    {
        mProject = pProject;
        mSession = pSession;
        mClasses = new HashMap();
        mMethods = new HashMap();
        mPackages = new HashMap();
        mUmlInterface = new HashMap();
        mUmlModels = new HashMap();
        mUmlClasses = new HashMap();
        mUmlPackages = new HashMap();
        mJSPs = new HashMap();
        Collection children = mProject.getChildren();
        if ( null != children )
        {
            Iterator it = children.iterator();
            while ( it.hasNext() )
            {
                addToCollection( (AbstractComponentBO) it.next(), null );
            }
        }
    }

    /**
     * Cr�e la cl� du composant pour �tre utilis�e avec l'adaptateur et ajoute le composant et ses enfants dans les
     * collections locales associ�es.
     * 
     * @param pComponent le composant � ajouter.
     * @param pParentName le nom du parent si disponible.
     */
    private void addToCollection( AbstractComponentBO pComponent, String pParentName )
    {
        // Cr�ation de la cl�
        String newParentName = pParentName;

        if ( null == newParentName )
        {
            newParentName = pComponent.getName();
        }
        else
        {
            newParentName += KEY_SEPARATOR + pComponent.getName();
        }
        Map map = getMapForComponent( pComponent );
        if ( pComponent instanceof AbstractComplexComponentBO )
        {
            map.put( newParentName, pComponent );
            Collection children = ( (AbstractComplexComponentBO) pComponent ).getChildren();
            if ( null != children )
            {
                // Si le composant est complexe, alors on va r�appeler la m�thode sur
                // chacun de ses enfants, de mani�re r�cursive pour parcourir tout
                // l'arbre des composants du projet
                Iterator it = children.iterator();
                while ( it.hasNext() )
                {
                    addToCollection( (AbstractComponentBO) it.next(), newParentName );
                }
            }
        }
        else
        {
            // Sinon, il s'agit d'une m�thode, on l'ajoute simplement � sa map
            map.put( newParentName, pComponent );
        }
    }

    /**
     * Fait persister un composant ou le r�cup�re si il existe d�j�.
     * 
     * @param pComponent le composant � rechercher ou � faire persister.
     * @return le composant persistant.
     * @throws JrafDaoException si erreur
     */
    public AbstractComponentBO persisteComponent( AbstractComponentBO pComponent )
        throws JrafDaoException
    {
        // On cr�e la liste de ses parents + le composant lui-m�me
        List parents = getParents( pComponent );
        AbstractComponentBO persistentParent = mProject;
        int parentsSize = parents.size();
        for ( int i = parents.size() - 1; i >= 0; i-- )
        {
            persistentParent = persisteComponent( persistentParent, (AbstractComponentBO) parents.get( i ) );
        }
        return persistentParent;
    }

    /**
     * Construit l'arbre complet du composant.
     * 
     * @param pComponent le composant
     * @return la liste contenant les parents de pComponent et lui-m�me.
     */
    private List getParents( AbstractComponentBO pComponent )
    {
        List parents = new ArrayList();
        parents.add( pComponent );
        AbstractComplexComponentBO parent = pComponent.getParent();
        boolean hasCorrectParents = ( parent != null );
        for ( ; parent != null && hasCorrectParents; parent = parent.getParent() )
        {
            if ( parent instanceof ProjectBO )
            {
                hasCorrectParents = false;
            }
            else
            {
                parents.add( parent );
            }
        }
        return parents;
    }

    /**
     * Fait persister un composant (ou le r�cup�re si il existe d�j�) en lui associant son parent d�j� persistant.
     * 
     * @param pPersistentParent le parent du composant d�j� persistant.
     * @param pComponent le composant � faire persister ou � rechercher
     * @return le composant persistant
     * @throws JrafDaoException si erreur
     */
    private AbstractComponentBO persisteComponent( AbstractComponentBO pPersistentParent, AbstractComponentBO pComponent )
        throws JrafDaoException
    {
        // On r�cup�re la map du composant
        Map map = getMapForComponent( pComponent );
        // On cr�e la cl� repr�sentant l'objet dans la map
        String key = buildKey( pComponent );
        AbstractComponentBO mapComponent = (AbstractComponentBO) map.get( key );
        if ( null == mapComponent )
        {
            // il n'existe pas, on le cr�e et on sauvegarde l'objet en base
            AbstractComponentDAOImpl dao = AbstractComponentDAOImpl.getInstance();
            pComponent.setParent( (AbstractComplexComponentBO) pPersistentParent );
            pComponent.setProject( mProject );
            dao.save( mSession, pComponent );
            // On ajoute l' �l�ment persistant � la map correspondante
            map.put( key, pComponent );
            mapComponent = pComponent;
        }
        else if ( pComponent instanceof ClassBO )
        {
            // cas particulier d'un classe qui peut �tre persist�e sans son fichier
            ClassBO classPersistent = (ClassBO) mapComponent;
            ClassBO classArg = (ClassBO) pComponent;
            if ( null == classPersistent.getFileName() && null != classArg.getFileName() )
            {
                // On update la classe
                AbstractComponentDAOImpl dao = AbstractComponentDAOImpl.getInstance();
                classPersistent.setFileName( classArg.getFileName() );
                dao.save( mSession, classPersistent );
            }
        }

        return mapComponent;
    }

    /**
     * Retrouve la map en fonction du type du composant.
     * 
     * @param pComponent le composant
     * @return la map dans laquelle pComponent doit �tre
     */
    private Map getMapForComponent( AbstractComponentBO pComponent )
    {
        Map result = null;
        // Chaque type de composant � sa map associ�e,
        // on r�cup�re donc le v�ritable type de l'objet
        // pass� en param�tre et on retourne sa map associ�e
        if ( pComponent instanceof MethodBO )
        {
            // mMethods contient des MethodBO
            result = mMethods;
        }
        else if ( pComponent instanceof ClassBO )
        {
            // mClasses contient des ClassBO
            result = mClasses;
        }
        else if ( pComponent instanceof PackageBO )
        {
            // mPackages contient des PackageBO
            result = mPackages;
        }
        else if ( pComponent instanceof UmlInterfaceBO )
        {
            // mUmlInterface contient des UmlInterfaceBO
            result = mUmlInterface;
        }
        else if ( pComponent instanceof UmlModelBO )
        {
            // mUmlModels contient des UmlModelBO
            result = mUmlModels;
        }
        else if ( pComponent instanceof UmlClassBO )
        {
            // mUmlClass contient des UmlClasslBO
            result = mUmlClasses;
        }
        else if ( pComponent instanceof UmlPackageBO )
        {
            // mUmlPackage contient des UmlPackagelBO
            result = mUmlPackages;
        }
        else if ( pComponent instanceof JspBO )
        {
            // mJSPs contient des JspBO
            result = mJSPs;
        }
        return result;
    }

    /**
     * Construit la cl� du composant qui correspond � la concat�nation de ses parents s�par�e par KEY_SEPARATOR
     * 
     * @param pComponent le composant
     * @return la cl� repr�sentant le composant dans les maps
     */
    public String buildKey( AbstractComponentBO pComponent )
    {
        String key = pComponent.getName();
        AbstractComponentBO parent = pComponent.getParent();
        boolean hasCorrectParents = ( parent != null );
        while ( hasCorrectParents )
        {
            if ( parent instanceof ProjectBO )
            {
                hasCorrectParents = false;
            }
            else
            {
                key = parent.getName() + KEY_SEPARATOR + key;
                parent = parent.getParent();
                hasCorrectParents = ( parent != null );
            }
        }
        return key;
    }

    /**
     * @return la session
     */
    public ISession getSession()
    {
        return mSession;
    }

    /**
     * @param pMethodName le nom de la m�thode � r�cup�rer
     * @param pFileName le nom du fichier dans lequel se trouve la m�thode
     * @param pAuditId l'id de l'audit
     * @return la bonne m�thodBO
     * @throws JrafDaoException en cas d'�chec
     */
    public Collection getSimilarMethods( String pMethodName, String pFileName, long pAuditId )
        throws JrafDaoException
    {
        AbstractComponentDAOImpl compDao = AbstractComponentDAOImpl.getInstance();
        Collection result = new ArrayList( 0 );
        MethodDAOImpl dao = MethodDAOImpl.getInstance();
        result = dao.findMethodeByName( mSession, pMethodName, pFileName, pAuditId );
        return result;
    }

    /**
     * This method search the component give in arguments in the list of all componentBO already existent
     * 
     * @param component Component to search
     * @return The componentBO associate if it is found or null if it is not found
     */
    public AbstractComponentBO getComponent( AbstractComponentBO component )
    {
        Map map = getMapForComponent( component );
        String key = buildKey( component );
        AbstractComponentBO persistentComponent = (AbstractComponentBO) map.get( key );
        return persistentComponent;
    }

    /**
     * This method give access to the attribute mClasses
     * 
     * @return the list of classBO already persist
     */
    public Map getClasses()
    {
        return mClasses;
    }

    /**
     * This method give access to the attribute mMethods
     * 
     * @return the list of methodBo already persist
     */
    public Map getMethods()
    {
        return mMethods;
    }

    /**
     * Compare two component by there key. This method use the buildKey() method of the ComponentRepository class to
     * build the key
     * 
     * @param pComponent First component to compare
     * @param pComponentToCompare Second component to compare
     * @return true if there key are the same
     */
    public boolean compare( AbstractComponentBO pComponent, AbstractComponentBO pComponentToCompare )
    {
        boolean bool = false;
        if ( buildKey( pComponent ).compareTo( buildKey( pComponentToCompare ) ) == 0 )
        {
            bool = true;
        }
        return bool;
    }

}
