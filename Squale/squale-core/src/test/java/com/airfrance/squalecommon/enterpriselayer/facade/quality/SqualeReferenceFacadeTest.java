package com.airfrance.squalecommon.enterpriselayer.facade.quality;

import java.util.ArrayList;
import java.util.Collection;

import com.airfrance.jraf.commons.exception.JrafEnterpriseException;
import com.airfrance.squalecommon.SqualeTestCase;

/**
 * @author M400841 Pour changer le mod�le de ce commentaire de type g�n�r�, allez � :
 *         Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 */
public class SqualeReferenceFacadeTest
    extends SqualeTestCase
{

    /**
     * test sur la r�cup�ration des r�sultats des projets
     * 
     * @throws JrafEnterpriseException en cas d'�chec
     */
    public void testGetProjectResults()
        throws JrafEnterpriseException
    {

        // Initialisation du retour
        Collection references = null;

        // Excution de la m�thode
        references = (Collection) SqualeReferenceFacade.getProjectResults( null, null, true, new Long( 0 ) );
        assertNotNull( references );

        // Excution de la m�thode
        references = (Collection) SqualeReferenceFacade.getProjectResults( null, null, false, new Long( 0 ) );
        assertNotNull( references );

        // TODO

    }

    /**
     * Test pour void deleteAuditList(Collection)
     * 
     * @throws JrafEnterpriseException en cas d'�chec
     */
    public void testDeleteAuditListCollection()
        throws JrafEnterpriseException
    {
        // Initialisation des parametres de la methode
        Collection projectsToRemove =
            new ArrayList( SqualeReferenceFacade.getProjectResults( new Integer( 2 ), new Integer( 0 ), false,
                                                                    new Long( 0 ) ) );

        // Exceution de la m�thode
        SqualeReferenceFacade.deleteAuditList( projectsToRemove );
        assertNull( null );

        // TODO tests plus complets a effectuer

    }

    /**
     * Test pour void validateAuditList(Collection)
     * 
     * @throws JrafEnterpriseException en cas d'�chec du test
     */
    public void testValidateAuditListCollection()
        throws JrafEnterpriseException
    {
        // Initialisation des parametres de la methode
        Collection projectsToValidate =
            new ArrayList( SqualeReferenceFacade.getProjectResults( new Integer( 2 ), new Integer( 0 ), false,
                                                                    new Long( 0 ) ) );

        // Exceution de la m�thode
        SqualeReferenceFacade.validateAuditList( projectsToValidate );
        assertNull( null );

        // TODO tests plus complets a effectuer

    }

}
