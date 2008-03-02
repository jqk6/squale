package com.airfrance.squalix.core;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;

import com.airfrance.jraf.commons.exception.JrafDaoException;
import com.airfrance.squalecommon.SqualeTestCase;
import com.airfrance.squalecommon.daolayer.component.AuditDAOImpl;
import com.airfrance.squalecommon.enterpriselayer.businessobject.component.AuditBO;

/**
 */
public class PartitionRotationTest
    extends SqualeTestCase
{

    /** la date au moment de la cr�ation de l'audit */
    private Date mCurrentDate;

    /**
     * Set-up
     * 
     * @throws Exception en cas de probl�mes (non-Javadoc)
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp()
        throws Exception
    {
        super.setUp();
        getSession().beginTransaction();
        // cr�ation de l'audit de rotation
        AuditBO audit = new AuditBO();
        audit.setId( 1 );
        // le nom
        audit.setName( AuditDAOImpl.ROTATION_AUDIT_NAME );
        // On prend la date courant
        mCurrentDate = Calendar.getInstance().getTime();
        audit.setDate( mCurrentDate );
        audit.setType( AuditBO.MILESTONE );
        AuditDAOImpl.getInstance().create( getSession(), audit );
        getSession().commitTransactionWithoutClose();
    }

    /**
     * teste la fonction de r�cup�ration de l'audit de rotation
     * 
     * @throws JrafDaoException en cas d'�chec
     */
    public void testFindRepartitionAudit()
        throws JrafDaoException
    {
        AuditDAOImpl dao = AuditDAOImpl.getInstance();
        Collection coll = dao.findRotationAudit( getSession() );
        assertTrue( coll != null && coll.size() == 1 );
    }

    /**
     * teste la fonction de r�cup�ration de l'audit de rotation
     * 
     * @throws JrafDaoException en cas d'�chec
     */
    public void testReportAudit()
        throws JrafDaoException
    {
        AuditDAOImpl dao = AuditDAOImpl.getInstance();
        // on r�cup�re l'audit de rotation cr�e
        // en v�rifiant qu'on a bien la date de cr�ation
        // affect�e par le setUp
        getSession().beginTransaction();
        Collection coll = dao.findRotationAudit( getSession() );
        getSession().commitTransactionWithoutClose();
        assertTrue( coll != null && coll.size() == 1 );
        AuditBO rotation = (AuditBO) coll.iterator().next();
        assertTrue( rotation.getDate() == mCurrentDate );

        // On effectue le d�calage par l'appel de la m�thode
        getSession().beginTransaction();
        dao.reportRotationAudit( getSession() );
        getSession().commitTransactionWithoutClose();

        // On r�cup�re � nouveau l'audit, et on v�rifie que le d�calage a bien �t� pris en compte
        getSession().beginTransaction();
        coll = dao.findRotationAudit( getSession() );
        getSession().commitTransactionWithoutClose();
        assertTrue( coll != null && coll.size() == 1 );
        rotation = (AuditBO) coll.iterator().next();
        // La date que l'on devrait retrouver
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime( mCurrentDate );
        cal.add( Calendar.WEEK_OF_YEAR, AuditDAOImpl.ROTATION_DELAY_IN_WEEKS );
        Date nextDate = cal.getTime();
        assertTrue( rotation.getDate().getTime() == nextDate.getTime() );

    }

}
