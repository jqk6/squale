package com.airfrance.squalecommon.enterpriselayer.facade.rule;

import java.io.InputStream;
import java.util.Collection;

import com.airfrance.jraf.spi.persistence.ISession;
import com.airfrance.squalecommon.SqualeTestCase;
import com.airfrance.squalecommon.daolayer.component.AbstractComponentDAOImpl;
import com.airfrance.squalecommon.daolayer.result.MarkDAOImpl;
import com.airfrance.squalecommon.daolayer.result.MeasureDAOImpl;
import com.airfrance.squalecommon.daolayer.result.QualityResultDAOImpl;
import com.airfrance.squalecommon.daolayer.rule.QualityGridDAOImpl;
import com.airfrance.squalecommon.datatransfertobject.rule.QualityGridDTO;
import com.airfrance.squalecommon.enterpriselayer.businessobject.component.ApplicationBO;
import com.airfrance.squalecommon.enterpriselayer.businessobject.component.AuditBO;
import com.airfrance.squalecommon.enterpriselayer.businessobject.component.ClassBO;
import com.airfrance.squalecommon.enterpriselayer.businessobject.component.MethodBO;
import com.airfrance.squalecommon.enterpriselayer.businessobject.component.PackageBO;
import com.airfrance.squalecommon.enterpriselayer.businessobject.component.ProjectBO;
import com.airfrance.squalecommon.enterpriselayer.businessobject.result.FactorResultBO;
import com.airfrance.squalecommon.enterpriselayer.businessobject.result.MarkBO;
import com.airfrance.squalecommon.enterpriselayer.businessobject.result.mccabe.McCabeQAClassMetricsBO;
import com.airfrance.squalecommon.enterpriselayer.businessobject.rule.QualityGridBO;

/**
 * Test de calcul d'audit
 */
public class AuditComputingTest
    extends SqualeTestCase
{
    /**
     * Test de calcul d'audit Un audit est cr�� artificiellement avec une grille qualit� de base, les m�triques sont
     * elles aussi cr��es en dur dans le test. Une phase d'audit est ensuite r�alis�e avec une v�rification des
     * r�sultats obtenus.
     */
    public void testComputeAuditResult()
    {
        ISession session;
        try
        {
            session = getSession();
            // Chargement de la grille
            InputStream stream = getClass().getClassLoader().getResourceAsStream( "data/grid/grid_compute.xml" );
            StringBuffer errors = new StringBuffer();
            Collection grids;
            grids = QualityGridImport.createGrid( stream, errors );
            QualityGridBO grid =
                (QualityGridBO) QualityGridDAOImpl.getInstance().load(
                                                                       session,
                                                                       new Long(
                                                                                 ( (QualityGridDTO) grids.iterator().next() ).getId() ) );
            // Cr�ation de l'application
            ApplicationBO application = getComponentFactory().createApplication( session );
            // Cr�ation du projet
            ProjectBO project = getComponentFactory().createProject( session, application, grid );
            // Cr�ation de l'audit
            AuditBO audit = getComponentFactory().createAudit( session, project );
            // Cr�ation du package
            PackageBO pkg = getComponentFactory().createPackage( session, project );
            pkg.addAudit( audit );
            AbstractComponentDAOImpl.getInstance().save( session, pkg );
            // Cr�ation de la classe
            ClassBO cls = getComponentFactory().createClass( session, pkg );
            cls.addAudit( audit );
            AbstractComponentDAOImpl.getInstance().save( session, cls );
            // Cr�ation de la classe2
            ClassBO cls2 = getComponentFactory().createClass( session, pkg );
            cls2.addAudit( audit );
            AbstractComponentDAOImpl.getInstance().save( session, cls2 );
            // Cr�ation de la m�thode
            MethodBO method = getComponentFactory().createMethod( session, cls );
            method.addAudit( audit );
            AbstractComponentDAOImpl.getInstance().save( session, cls );
            /*
             * McCabe : - cls : Maxvg = 2; Sumvg = 2; wmc = 8; - method : nsloc = 2; Checkstyle : - project : totalError =
             * 20;
             */

            getComponentFactory().createMeasures( session, audit, project, cls, method );
            // Nouvelle mesure pour la classe 2
            McCabeQAClassMetricsBO classMetrics = new McCabeQAClassMetricsBO();
            classMetrics.setAudit( audit );
            classMetrics.setComponent( cls2 );
            // On veut une note � 2 pour le composant
            classMetrics.setMaxvg( new Integer( 350 ) );
            classMetrics.setSumvg( new Integer( 1000 ) );
            classMetrics.setWmc( new Integer( 8 ) );
            MeasureDAOImpl.getInstance().create( getSession(), classMetrics );

            // Calcul de l'audit
            AuditComputing.computeAuditResult( session, project, audit );
            // V�rification des calculs
            /*
             * D'apr�s la grille "grid_compute.xml" on doit avoir : practice 1 class1 = 0 car 2(mccabe.maxvg) >=
             * 0.5*2(mccabe.sumvg) et 8(mccabe.wmc) >= 8 (classe) practice 1 class2 = 2 car 350(mccabe.maxvg) >=
             * 0.3*1000(mccabe.sumvg) et 8(mccabe.wmc) >= 8 (classe) --> practice 1 = 1.0 car f(x)=x est la fonction de
             * pond�ration donc f^-1(1/2*(0+2)=1 practice 2 = 3*(2.0/3.0)**(100.0*20/1000) = 1.33334 (projet) practice 3 =
             * 3 car aucune condition n'est remplie (mccabe.nsloc = 2 < 40) (methode)
             */
            // Note sur la classe
            final float expectedClassMark = 0f;
            Collection classMarks =
                MarkDAOImpl.getInstance().findWhere( session, new Long( cls.getId() ), new Long( audit.getId() ) );
            assertEquals( 1, classMarks.size() );
            assertEquals( new Float( expectedClassMark ),
                          new Float( ( (MarkBO) classMarks.iterator().next() ).getValue() ) );
            // Note sur la classe2
            final float expectedClassMark2 = 2f;
            Collection classMarks2 =
                MarkDAOImpl.getInstance().findWhere( session, new Long( cls2.getId() ), new Long( audit.getId() ) );
            assertEquals( 1, classMarks2.size() );
            assertEquals( new Float( expectedClassMark2 ),
                          new Float( ( (MarkBO) classMarks2.iterator().next() ).getValue() ) );
            // R�sultat pour practice1
            float expectedPractice1Mark = 1f;
            // Note sur la m�thode
            final float expectedMethodMark = 3f;
            Collection methodMarks =
                MarkDAOImpl.getInstance().findWhere( session, new Long( method.getId() ), new Long( audit.getId() ) );
            assertEquals( 1, methodMarks.size() );
            assertEquals( new Float( expectedMethodMark ),
                          new Float( ( (MarkBO) methodMarks.iterator().next() ).getValue() ) );
            // Note sur le projet
            final float expectedProjectMark =
                new Float( 3 * Math.pow( ( 2.0 / 3.0 ), 100.0 * 20 / 1000 ) ).floatValue();
            Collection factorResults =
                QualityResultDAOImpl.getInstance().findWhere( session, new Long( project.getId() ),
                                                              new Long( audit.getId() ) );
            assertEquals( 2, factorResults.size() );
            // La pond�ration sur les pratiques est identique
            Collection factorResults2 =
                QualityResultDAOImpl.getInstance().findWhere( session, new Long( project.getId() ),
                                                              new Long( audit.getId() ) );
            assertEquals( ( expectedPractice1Mark + expectedMethodMark + expectedProjectMark ) / 3f,
                          ( (FactorResultBO) factorResults2.iterator().next() ).getMeanMark(), 0.1 );
        }
        catch ( Exception e )
        {
            e.printStackTrace();
            fail( "unexpected exception" );
        }
    }

}