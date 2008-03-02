package com.airfrance.squalix.tools.external.bugtracking.qc;

import java.util.Collection;
import java.util.Iterator;

import com.airfrance.squalecommon.SqualeTestCase;
import com.airfrance.squalecommon.daolayer.component.ProjectDAOImpl;
import com.airfrance.squalecommon.daolayer.result.MeasureDAOImpl;
import com.airfrance.squalecommon.enterpriselayer.businessobject.component.ApplicationBO;
import com.airfrance.squalecommon.enterpriselayer.businessobject.component.AuditBO;
import com.airfrance.squalecommon.enterpriselayer.businessobject.component.ProjectBO;
import com.airfrance.squalecommon.enterpriselayer.businessobject.result.external.bugtracking.ExtBugTrackingMetricsBO;

import com.airfrance.squalix.core.TaskException;

/**
 * Classe de test pour la classe ExtBugTrackingQCTaskStub
 */
public class ExtBugTrackingQCTaskStubTest
    extends SqualeTestCase
{

    /** La tache */
    private ExtBugTrackingQCTaskStub mExtQCTask;

    /** L'identifiant de l'audit */
    private Long mAuditId;

    /** L'identifiant du projet */
    private Long mProjectId;

    /**
     * Constructeur pour ExtBugTrackingQCTaskTest.
     * 
     * @param arg0
     */
    public ExtBugTrackingQCTaskStubTest()
    {
        super( "Test tache externe QC" );
    }

    /**
     * Initialisation de la tache pour le test
     */
    protected void setUp()
        throws Exception
    {
        super.setUp();
        mExtQCTask = new ExtBugTrackingQCTaskStub();
        getSession().beginTransaction();
        ApplicationBO appli = getComponentFactory().createApplication( getSession() );
        ProjectBO project = getComponentFactory().createProject( getSession(), appli, null );

        AuditBO audit = getComponentFactory().createAudit( getSession(), project );
        ProjectDAOImpl.getInstance().save( getSession(), project );
        getSession().commitTransactionWithoutClose();

        mAuditId = new Long( audit.getId() );
        mProjectId = new Long( project.getId() );
        mExtQCTask.setApplicationId( new Long( appli.getId() ) );
        mExtQCTask.setProjectId( mProjectId );
        mExtQCTask.setAuditId( mAuditId );
    }

    /**
     * Finalisation du test
     */
    protected void tearDown()
        throws Exception
    {
        super.tearDown();
    }

    /**
     * Test de la m�thode ex�cute()
     */
    public void testExecute()
        throws Exception
    {
        mExtQCTask.run();
        try
        {
            Collection res = MeasureDAOImpl.getInstance().findWhere( getSession(), mProjectId, mAuditId );
            int size = res.size();
            assertEquals( "nb de r�sultat dans la collection diff�rent de 1", res.size(), 1 );
            Iterator it = res.iterator();
            ExtBugTrackingMetricsBO metric = (ExtBugTrackingMetricsBO) it.next();
            assertEquals( "nb total de defects", metric.getNumberOfDefects(), new Integer( 20 ) );
            assertEquals( "nb de defects ouvert", metric.getDefectsOpen(), new Integer( 2 ) );
            assertEquals( "nb de defects assign�", metric.getDefectsAssigned(), new Integer( 4 ) );
            assertEquals( "nb de defects trait�", metric.getDefectsTreated(), new Integer( 6 ) );
            assertEquals( "nb de defects clos", metric.getDefectsClose(), new Integer( 8 ) );
            assertEquals( "nb de defects en anomalie", metric.getDefectsAnomaly(), new Integer( 5 ) );
            assertEquals( "nb de defects en �volution", metric.getDefectsEvolution(), new Integer( 15 ) );
            assertEquals( "nb de defects de niveau haut", metric.getDefectsHigh(), new Integer( 3 ) );
            assertEquals( "nb de defects de niveau moyen", metric.getDefectsMedium(), new Integer( 7 ) );
            assertEquals( "nb de defects de niveau bas", metric.getDefectsLow(), new Integer( 10 ) );
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
    }

    /**
     * test de la m�thode recup()
     * 
     * @throws Exception
     */
    public void testRecup()
        throws TaskException
    {
        mExtQCTask.recup();
        ExtBugTrackingMetricsBO metric = mExtQCTask.getDefects();
        assertEquals( "nb total de defects", metric.getNumberOfDefects(), new Integer( 20 ) );
        assertEquals( "nb de defects ouvert", metric.getDefectsOpen(), new Integer( 2 ) );
        assertEquals( "nb de defects assign�", metric.getDefectsAssigned(), new Integer( 4 ) );
        assertEquals( "nb de defects trait�", metric.getDefectsTreated(), new Integer( 6 ) );
        assertEquals( "nb de defects clos", metric.getDefectsClose(), new Integer( 8 ) );
        assertEquals( "nb de defects en anomalie", metric.getDefectsAnomaly(), new Integer( 5 ) );
        assertEquals( "nb de defects en �volution", metric.getDefectsEvolution(), new Integer( 15 ) );
        assertEquals( "nb de defects de niveau haut", metric.getDefectsHigh(), new Integer( 3 ) );
        assertEquals( "nb de defects de niveau moyen", metric.getDefectsMedium(), new Integer( 7 ) );
        assertEquals( "nb de defects de niveau bas", metric.getDefectsLow(), new Integer( 10 ) );
    }
}
