package com.airfrance.squalix.tools.cpd;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import junit.framework.TestCase;

import com.airfrance.squalecommon.SqualeTestCase;
import com.airfrance.squalecommon.daolayer.component.ProjectDAOImpl;
import com.airfrance.squalecommon.daolayer.component.ProjectParameterDAOImpl;
import com.airfrance.squalecommon.daolayer.result.MeasureDAOImpl;
import com.airfrance.squalecommon.enterpriselayer.businessobject.component.ApplicationBO;
import com.airfrance.squalecommon.enterpriselayer.businessobject.component.AuditBO;
import com.airfrance.squalecommon.enterpriselayer.businessobject.component.ProjectBO;
import com.airfrance.squalecommon.enterpriselayer.businessobject.component.parameters.ListParameterBO;
import com.airfrance.squalecommon.enterpriselayer.businessobject.component.parameters.MapParameterBO;
import com.airfrance.squalecommon.enterpriselayer.businessobject.component.parameters.ParametersConstants;
import com.airfrance.squalecommon.enterpriselayer.businessobject.component.parameters.StringParameterBO;
import com.airfrance.squalecommon.enterpriselayer.businessobject.config.TaskParameterBO;
import com.airfrance.squalecommon.enterpriselayer.businessobject.result.rulechecking.CpdTransgressionBO;
import com.airfrance.squalecommon.enterpriselayer.businessobject.rule.QualityGridBO;
import com.airfrance.squalix.core.AbstractTask;
import com.airfrance.squalix.core.TaskData;

/**
 * Test de la t�che Cpd UNIT_KO : assert trop fonctionnel (voir si on peut g�n�r� les duplications avec cpd) puis
 * comparer les nombres avec ceux trouver dans le test
 */
public class CpdTaskTest
    extends SqualeTestCase
{

    /** Nombre de lignes dupliqu�es en C++ */
    private static final int CPP_DUPLICATE_LINES = 42;

    /** Nombre de lignes dupliqu�es en JSP */
    private static final int JSP_DUPLICATE_LINES = 150;

    /** Nombre de lignes dupliqu�es en JAVA */
    private static final int JAVA_DUPLICATE_LINES = 464;

    /** r�pertoire des .java */
    public static final String SOURCES_DIR = "data/Project4CpdTest";

    /**
     * Pseudo-chemin vers une vue.
     */
    private static final String VIEW_PATH = ".";

    /** l'application */
    private ApplicationBO mAppli = new ApplicationBO();

    /** le projet � auditer */
    private ProjectBO mProject = new ProjectBO();

    /** l'audit */
    private AuditBO mAudit = new AuditBO();

    /** les t�ches temporaires */
    private TaskData mData = new TaskData();

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
        // Les param�tres doivent contenir le chemin du fichier de configuration
        MapParameterBO params = new MapParameterBO();
        ListParameterBO listParam = new ListParameterBO();
        // Traitement des sources C++ et Java
        List list = new ArrayList();
        StringParameterBO stringParam = new StringParameterBO();
        stringParam.setValue( SOURCES_DIR );
        list.add( stringParam );
        listParam.setParameters( list );
        params.getParameters().put( ParametersConstants.SOURCES, listParam );
        // Traitement des sources JSP
        listParam = new ListParameterBO();
        list = new ArrayList();
        stringParam = new StringParameterBO();
        stringParam.setValue( SOURCES_DIR );
        list.add( stringParam );
        listParam.setParameters( list );
        params.getParameters().put( ParametersConstants.JSP, listParam );
        // Pas de r�pertoire exclu
        ProjectParameterDAOImpl.getInstance().create( getSession(), params );
        mProject.setParameters( params );
        ProjectDAOImpl.getInstance().save( getSession(), mProject );
        mAudit = getComponentFactory().createAudit( this.getSession(), mProject );
        // On fait le commit pour permettre l'acc�s aux donn�es dans une autre session
        getSession().commitTransactionWithoutClose();
        File viewFile = new File( VIEW_PATH );
        mData.putData( TaskData.VIEW_PATH, viewFile.getCanonicalPath() );
    }

    /**
     * Cr�ation de la t�che
     * 
     * @param pLanguages langages utilis�s
     * @return t�che correspondante
     */
    private CpdTask createTask( String[] pLanguages )
    {
        CpdTask task = new CpdTask();
        task.setData( mData );
        ArrayList taskParameters = new ArrayList();
        for ( int i = 0; i < pLanguages.length; i++ )
        {
            TaskParameterBO param = new TaskParameterBO();
            param.setName( "language" );
            param.setValue( pLanguages[i] );
            taskParameters.add( param );
        }
        task.setTaskParameters( taskParameters );
        task.setAuditId( new Long( mAudit.getId() ) );
        task.setProjectId( new Long( mProject.getId() ) );
        task.setApplicationId( new Long( mAppli.getId() ) );
        task.setStatus( AbstractTask.NOT_ATTEMPTED );
        return task;
    }

    /**
     * Test d'ex�cution en Java
     */
    public void testJavaExecute()
    {
        try
        {
            CpdTask task = createTask( new String[] { "java" } );
            task.run();
            assertEquals( AbstractTask.TERMINATED, task.getStatus() );

            // On v�rifie les r�sultats
            MeasureDAOImpl dao = MeasureDAOImpl.getInstance();
            assertEquals( 1, dao.count( getSession() ).intValue() );

            // On r�cup�re la mesure de trasngression
            CpdTransgressionBO measure =
                (CpdTransgressionBO) dao.load( getSession(), new Long( mProject.getId() ), new Long( mAudit.getId() ),
                                               CpdTransgressionBO.class );
            // 1 metric pour les details, 1 pour le langage et 1 pour le total
            final int metricsCount = 3;
            assertEquals( metricsCount, measure.getMetrics().size() );
            assertEquals( measure.getDuplicateLinesForLanguage( "java" ), measure.getDuplicateLinesNumber() );
            Collection details = measure.getDetails();
            // Le fichier java est cl�n� et modifi� de fa�on � contenir 7 morceaux de code identiques
            final int cpCount = 7;
            assertEquals( "Check values with external CPD tool", cpCount, details.size() );

            // On r�cup�re la mesure du nombre de lignes dupliqu�es
            final int dupLinesNb = JAVA_DUPLICATE_LINES; // Nombre de lignes dupliqu�es au total
            assertEquals( "Check values with external CPD tool", dupLinesNb,
                          measure.getDuplicateLinesNumber().intValue() );
        }
        catch ( Exception e )
        {
            e.printStackTrace();
            fail( "unexpected exception" );
        }
    }

    /**
     * Test d'ex�cution en Java
     */
    public void testJavaWithexclusionExecute()
    {
        try
        {
            // On cr�e les exclusion
            getSession().beginTransaction();
            MapParameterBO params = mProject.getParameters();
            // Les patterns � exclure
            ListParameterBO excludedList = new ListParameterBO();
            ArrayList eList = new ArrayList();
            StringParameterBO stringExclude = new StringParameterBO();
            stringExclude.setValue( "**/*Clone*/**" );
            eList.add( stringExclude );
            excludedList.setParameters( eList );
            params.getParameters().put( ParametersConstants.EXCLUDED_PATTERNS, excludedList );

            ProjectParameterDAOImpl.getInstance().create( getSession(), params );
            mProject.setParameters( params );
            ProjectDAOImpl.getInstance().save( getSession(), mProject );
            getSession().commitTransactionWithoutClose();
            CpdTask task = createTask( new String[] { "java" } );
            task.run();
            assertEquals( AbstractTask.TERMINATED, task.getStatus() );

            // On v�rifie les r�sultats
            MeasureDAOImpl dao = MeasureDAOImpl.getInstance();
            assertEquals( 1, dao.count( getSession() ).intValue() );

            // On r�cup�re la mesure de transgression
            CpdTransgressionBO measure =
                (CpdTransgressionBO) dao.load( getSession(), new Long( mProject.getId() ), new Long( mAudit.getId() ),
                                               CpdTransgressionBO.class );
            // 1 metric pour les details, 1 pour le langage et 1 pour le total
            final int metricsCount = 3;
            assertEquals( metricsCount, measure.getMetrics().size() );
            assertEquals( measure.getDuplicateLinesForLanguage( "java" ), measure.getDuplicateLinesNumber() );
            Collection details = measure.getDetails();
            // Un seul fichier r�cup�rer donc pas de duplication
            final int cpCount = 0;
            assertEquals( "Check values with external CPD tool", cpCount, details.size() );

            // On r�cup�re la mesure du nombre de lignes dupliqu�es
            final int dupLinesNb = 0; // Nombre de lignes dupliqu�es au total
            assertEquals( "Check values with external CPD tool", dupLinesNb,
                          measure.getDuplicateLinesNumber().intValue() );
        }
        catch ( Exception e )
        {
            e.printStackTrace();
            fail( "unexpected exception" );
        }
    }

    /**
     * Test d'ex�cution en jsp
     */
    public void testJspExecute()
    {
        try
        {
            CpdTask task = createTask( new String[] { "jsp" } );
            task.run();
            assertEquals( AbstractTask.TERMINATED, task.getStatus() );

            // On v�rifie les r�sultats
            MeasureDAOImpl dao = MeasureDAOImpl.getInstance();
            assertEquals( 1, dao.count( getSession() ).intValue() );
            List coll = dao.findAll( getSession() );

            // On r�cup�re la mesure de trasngression
            CpdTransgressionBO measure =
                (CpdTransgressionBO) dao.load( getSession(), new Long( mProject.getId() ), new Long( mAudit.getId() ),
                                               CpdTransgressionBO.class );
            // 1 metric pour les details, 1 pour le langage et 1 pour le total
            final int metricsCount = 3;
            assertEquals( metricsCount, measure.getMetrics().size() );
            assertEquals( measure.getDuplicateLinesForLanguage( "jsp" ), measure.getDuplicateLinesNumber() );
            Collection details = measure.getDetails();
            // Le fichier jsp est cl�n� et modifi� de fa�on � contenir 2 morceaux de code identiques
            final int cpCount = 2;
            assertEquals( "Check values with external CPD tool", cpCount, details.size() );

            // On r�cup�re la mesure du nombre de lignes dupliqu�es
            final int dupLinesNb = JSP_DUPLICATE_LINES; // Nombre de lignes dupliqu�es au total
            assertEquals( "Check values with external CPD tool", dupLinesNb,
                          measure.getDuplicateLinesNumber().intValue() );
        }
        catch ( Exception e )
        {
            e.printStackTrace();
            fail( "unexpected exception" );
        }
    }

    /**
     * Test d'ex�cution en j2ee (java et jsp)
     */
    public void testJ2eeExecute()
    {
        try
        {
            CpdTask task = createTask( new String[] { "java", "jsp" } );
            task.run();
            assertEquals( AbstractTask.TERMINATED, task.getStatus() );

            // On v�rifie les r�sultats
            MeasureDAOImpl dao = MeasureDAOImpl.getInstance();
            assertEquals( 1, dao.count( getSession() ).intValue() );
            List coll = dao.findAll( getSession() );

            // On r�cup�re la mesure de transgression
            CpdTransgressionBO measure =
                (CpdTransgressionBO) dao.load( getSession(), new Long( mProject.getId() ), new Long( mAudit.getId() ),
                                               CpdTransgressionBO.class );
            // 2 metric pour les details, 2 pour le langage et 1 pour le total
            final int metricsCount = 5;
            assertEquals( metricsCount, measure.getMetrics().size() );
            assertEquals( JSP_DUPLICATE_LINES, measure.getDuplicateLinesForLanguage( "jsp" ).intValue() );
            assertEquals( JAVA_DUPLICATE_LINES, measure.getDuplicateLinesForLanguage( "java" ).intValue() );
            Collection details = measure.getDetails();
            // Le fichier jsp est cl�n� et modifi� de fa�on � contenir 9 morceaux de code identiques
            final int cpCount = 9;
            assertEquals( "Check values with external CPD tool", cpCount, details.size() );

            // On r�cup�re la mesure du nombre de lignes dupliqu�es
            final int dupLinesNb = JAVA_DUPLICATE_LINES + JSP_DUPLICATE_LINES; // Nombre de lignes dupliqu�es au total
            assertEquals( "Check values with external CPD tool", dupLinesNb,
                          measure.getDuplicateLinesNumber().intValue() );
        }
        catch ( Exception e )
        {
            e.printStackTrace();
            fail( "unexpected exception" );
        }
    }

    /**
     * Test d'ex�cution en c++
     */
    public void testCppExecute()
    {
        try
        {
            CpdTask task = createTask( new String[] { "cpp" } );
            task.run();
            assertEquals( AbstractTask.TERMINATED, task.getStatus() );

            // On v�rifie les r�sultats
            MeasureDAOImpl dao = MeasureDAOImpl.getInstance();
            assertEquals( 1, dao.count( getSession() ).intValue() );
            List coll = dao.findAll( getSession() );

            // On r�cup�re la mesure de trasngression
            CpdTransgressionBO measure =
                (CpdTransgressionBO) dao.load( getSession(), new Long( mProject.getId() ), new Long( mAudit.getId() ),
                                               CpdTransgressionBO.class );
            assertEquals( measure.getDuplicateLinesForLanguage( "cpp" ), measure.getDuplicateLinesNumber() );
            // 1 metric pour les details, 1 pour le langage et 1 pour le total
            final int metricsCount = 3;
            assertEquals( metricsCount, measure.getMetrics().size() );
            Collection details = measure.getDetails();
            // Le fichier jsp est cl�n� et modifi� de fa�on � contenir 1 morceau de code identique
            final int cpCount = 1;
            assertEquals( "Check values with external CPD tool", cpCount, details.size() );

            // On r�cup�re la mesure du nombre de lignes dupliqu�es
            final int dupLinesNb = CPP_DUPLICATE_LINES; // Nombre de lignes dupliqu�es au total
            assertEquals( "Check values with external CPD tool", dupLinesNb,
                          measure.getDuplicateLinesNumber().intValue() );
        }
        catch ( Exception e )
        {
            e.printStackTrace();
            fail( "unexpected exception" );
        }
    }
}
