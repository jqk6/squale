package com.airfrance.squalecommon.datatransfertobject.transform.component;

import java.util.GregorianCalendar;

import com.airfrance.squalecommon.SqualeTestCase;
import com.airfrance.squalecommon.datatransfertobject.component.AuditDTO;
import com.airfrance.squalecommon.datatransfertobject.component.ComponentDTO;
import com.airfrance.squalecommon.enterpriselayer.businessobject.component.AuditBO;

/**
 * @author M400841 Pour changer le mod�le de ce commentaire de type g�n�r�, allez � :
 *         Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 */
public class AuditTransformTest
    extends SqualeTestCase
{

    /**
     * Ces tests ne sont plus utilis�s
     */
    public void testDto2Bo()
    {
        // Initialisation des parametres de la methode
        AuditDTO auditIn = new AuditDTO(); // � initialiser
        auditIn.setDate( GregorianCalendar.getInstance().getTime() );
        auditIn.setName( "bonjour" );
        auditIn.setID( 123 );

        ComponentDTO application = new ComponentDTO();
        application.setID( 123 );
        auditIn.setApplicationId( application.getID() );

        // Initialisation du retour
        AuditBO auditOut = null;

        // Execution de la methode
        auditOut = AuditTransform.dto2Bo( auditIn );

        // Verification de l'�galit� des attributs des classes
        assertEquals( auditIn.getID(), auditOut.getId() );
        assertSame( auditIn.getType(), auditOut.getType() );
        assertSame( auditIn.getName(), auditOut.getName() );
        assertSame( auditIn.getDate(), auditOut.getDate() );
        // assertEquals(auditIn.getApplicationId(), auditOut.getApplication().getId());

    }

    /**
     * Ces tests ne sont plus utilis�s
     */
    public void testBo2Dto()
    {

        // Initialisation des parametres de la methode
        AuditBO auditIn = new AuditBO(); // � initialiser integralement ???
        long applicationId = 3;
        auditIn.setDate( GregorianCalendar.getInstance().getTime() );
        auditIn.setName( "audit1" );
        auditIn.setId( 123 );

        // Initialisation du retour
        AuditDTO auditOut = null;

        // Execution de la methode
        auditOut = AuditTransform.bo2Dto( auditIn, applicationId );

        // Verification de l'�galit� des attributs des classes
        assertEquals( auditIn.getId(), auditOut.getID() );
        assertSame( auditIn.getType(), auditOut.getType() );
        assertSame( auditIn.getName(), auditOut.getName() );
        assertSame( auditIn.getDate(), auditOut.getDate() );
        // assertEquals(auditIn.getApplication().getId(), auditOut.getApplicationId());

    }

}
