package com.airfrance.squaleweb.comparator;

import java.util.Comparator;

import com.airfrance.squalecommon.datatransfertobject.component.AuditDTO;
import com.airfrance.squaleweb.applicationlayer.formbean.component.AuditForm;

/**
 * 
 * Compare les audits par rapport � leurs dates.
 * @author M400842
 *
 */
public class AuditComparator implements Comparator {

    /**
     * Compare les audits en utilisant leurs dates.
     * @param pAudit1 la premi�re application
     * @param pAudit2 la seconde application
     * @return la comparaison des dates r�elles des audits.
     */
    public int compare(Object pAudit1, Object pAudit2) {
        int result = 1;
        // V�rification du type des objets        
        if (pAudit1 instanceof AuditForm) {
            // Si les �l�ments sont comparables
            if (((AuditForm) pAudit1).getRealDate() != null) {
                if (((AuditForm) pAudit2).getRealDate() != null) {
                    // on compare selon la date r�elle de l'audit
                    // en prenant compte de la date historique
                    result = ((AuditForm) pAudit1).getRealDate().compareTo(((AuditForm) pAudit2).getRealDate());
                } else {
                    result = -1;
                }
            }
        } else {
            // On compare � l'identique dans le cas d'un DTO
            if (((AuditDTO) pAudit1).getRealDate() != null) {
                if (((AuditDTO) pAudit2).getRealDate() != null) {
                    // Comparaison selon la date r�elle de l'audit
                    result = ((AuditDTO) pAudit1).getRealDate().compareTo(((AuditDTO) pAudit2).getRealDate());
                } else {
                    result = -1;
                }
            }
        }
        return result;
    }
}
