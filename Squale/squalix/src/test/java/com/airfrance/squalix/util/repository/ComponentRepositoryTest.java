package com.airfrance.squalix.util.repository;

import junit.framework.TestCase;

import com.airfrance.jraf.commons.exception.JrafDaoException;
import com.airfrance.squalecommon.SqualeTestCase;
import com.airfrance.squalecommon.daolayer.component.ProjectDAOImpl;
import com.airfrance.squalecommon.daolayer.component.ProjectParameterDAOImpl;
import com.airfrance.squalecommon.enterpriselayer.businessobject.component.ApplicationBO;
import com.airfrance.squalecommon.enterpriselayer.businessobject.component.ClassBO;
import com.airfrance.squalecommon.enterpriselayer.businessobject.component.MethodBO;
import com.airfrance.squalecommon.enterpriselayer.businessobject.component.PackageBO;
import com.airfrance.squalecommon.enterpriselayer.businessobject.component.ProjectBO;
import com.airfrance.squalecommon.enterpriselayer.businessobject.component.parameters.MapParameterBO;
import com.airfrance.squalecommon.enterpriselayer.businessobject.rule.QualityGridBO;
import com.airfrance.squalix.util.parser.JavaParser;

/**
 * Teste ComponentRepository.
 */
public class ComponentRepositoryTest
    extends SqualeTestCase
{

    /** l'application */
    private ApplicationBO mAppli = new ApplicationBO();

    /** le projet � auditer */
    private ProjectBO mProject = new ProjectBO();

    /** le projet � auditer */
    private ComponentRepository mRepository;

    /**
     * @see TestCase#setUp()
     */
    protected void setUp()
        throws Exception
    {
        super.setUp();
        getSession().beginTransaction();
        mAppli = getComponentFactory().createApplication( getSession() );
        QualityGridBO grid = getComponentFactory().createGrid( getSession() );
        mProject = getComponentFactory().createProject( getSession(), mAppli, grid );
        // Enregistrement du ProjectBO dans la base
        ProjectDAOImpl projectDAO = ProjectDAOImpl.getInstance();
        MapParameterBO params = new MapParameterBO();
        ProjectParameterDAOImpl.getInstance().create( getSession(), params );
        mProject.setParameters( params );
        ProjectDAOImpl.getInstance().save( getSession(), mProject );
        PackageBO packageBO1 = getComponentFactory().createPackage( getSession(), mProject );
        PackageBO packageBO2 = getComponentFactory().createPackage( getSession(), "package2", packageBO1 );
        ClassBO classBO = getComponentFactory().createClass( getSession(), packageBO2 );
        mRepository = new ComponentRepository( mProject, getSession() );
    }

    /**
     * Teste de la m�thode persisteClass avec une classe qui n'existe pas en base.
     * 
     * @throws JrafDaoException si erreur
     */
    public void testPersisteNotExistingClass()
        throws JrafDaoException
    {
        // On cr�e une classe
        ClassBO classBO = createClassBO();
        ClassBO persistentClass = (ClassBO) mRepository.persisteComponent( classBO );
        getSession().commitTransactionWithoutClose();
        assertEquals( "ClassTest", persistentClass.getName() );
        assertTrue( -1 != persistentClass.getId() );
    }

    /**
     * Teste de la m�thode persisteClass avec une classe qui existe en base.
     * 
     * @throws JrafDaoException si erreur
     */
    public void testPersisteExistingClass()
        throws JrafDaoException
    {
        // On cr�e une classe
        ClassBO classBO = createClassBO();
        ClassBO persistentClass = (ClassBO) mRepository.persisteComponent( classBO );
        getSession().commitTransactionWithoutClose();
        ClassBO classResult = (ClassBO) mRepository.persisteComponent( classBO );
        assertEquals( "ClassTest", classResult.getName() );
        assertTrue( -1 != classResult.getId() );
    }

    /**
     * Teste la persistance d'une classe sans package.
     * 
     * @throws JrafDaoException si erreur
     */
    public void testPersisteExistingClassWithoutPackage()
        throws JrafDaoException
    {
        // On cr�e une classe sans package
        ClassBO classBO = createClassBOWithoutPackage();
        ClassBO persistentClass = (ClassBO) mRepository.persisteComponent( classBO );
        getSession().commitTransactionWithoutClose();
        ClassBO classResult = (ClassBO) mRepository.persisteComponent( classBO );
        assertEquals( "ClassTest", classResult.getName() );
        assertTrue( -1 != classResult.getId() );
    }

    /**
     * Teste de la m�thode persisteMethod avec une m�thode qui n'existe pas en base.
     * 
     * @throws JrafDaoException si erreur
     */
    public void testPersisteNotExistingMethod()
        throws JrafDaoException
    {
        // On cr�e une m�thode
        ClassBO classBO = createClassBO();
        MethodBO methodBO = getComponentFactory().createMethod( getSession(), classBO );
        MethodBO persistentMethod = (MethodBO) mRepository.persisteComponent( methodBO );
        getSession().commitTransactionWithoutClose();
        assertEquals( "method", persistentMethod.getName() );
        assertEquals( "fileName", persistentMethod.getLongFileName() );
        assertTrue( -1 != persistentMethod.getId() );
    }

    /**
     * Teste de la m�thode persisteMethod avec la classe de la m�thode qui existe en base.
     * 
     * @throws JrafDaoException si erreur
     */
    public void testPersisteMethodWithExistingClass()
        throws JrafDaoException
    {
        // On cr�e une classe
        ClassBO classBO1 = createClassBO();
        ClassBO persistentClass = (ClassBO) mRepository.persisteComponent( classBO1 );
        // On cr�e une m�thode avec la classe persistante
        ClassBO classBO2 = createClassBO();
        MethodBO methodBO = getComponentFactory().createMethod( getSession(), classBO2 );
        MethodBO persistentMethod = (MethodBO) mRepository.persisteComponent( methodBO );
        getSession().commitTransactionWithoutClose();
        assertEquals( "method", persistentMethod.getName() );
        assertEquals( "fileName", persistentMethod.getLongFileName() );
        assertTrue( -1 != persistentMethod.getId() );
    }

    /**
     * @return une classe
     */
    private ClassBO createClassBO()
    {
        String absoluteClassName = "com.airfrance.projetTest.ClassTest";
        return createClassBO( absoluteClassName );
    }

    /**
     * @param pAbsoluteClassName le nom absolu de la classe
     * @return une classe
     */
    private ClassBO createClassBO( String pAbsoluteClassName )
    {
        JavaParser javaParser = new JavaParser( mProject );
        return javaParser.getClass( pAbsoluteClassName );
    }

    /**
     * @return une classe sans package
     */
    private ClassBO createClassBOWithoutPackage()
    {
        String absoluteClassName = "ClassTest";
        JavaParser javaParser = new JavaParser( mProject );
        return javaParser.getClass( absoluteClassName );
    }
}
