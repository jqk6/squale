package com.airfrance.squalecommon.enterpriselayer.businessobject.component;

import java.util.Comparator;
import java.util.Date;

/**
 * Compare les auditBO par rapport � leur date
 */
public class AuditDateComparator
    implements Comparator
{

    /** Permet de savoir sur quelle date on compare */
    private boolean mExecutedDate;

    /**
     * Constructeur par d�faut
     */
    public AuditDateComparator()
    {
        mExecutedDate = false;
    }

    /**
     * @param pExecutedDate true si on compare selon le date d'ex�cution false si on compare sur la date des sources
     */
    public AuditDateComparator( boolean pExecutedDate )
    {
        mExecutedDate = pExecutedDate;
    }

    /**
     * {@inheritDoc} Compare les audits en utilisant leurs dates.
     * 
     * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
     */
    public int compare( Object pAudit1, Object pAudit2 )
    {
        int result = 1;
        // V�rification du type des objets
        if ( pAudit1 instanceof AuditBO )
        {
            // On r�cup�re par d�faut la date r�elle des audits
            Date date1 = ( (AuditBO) pAudit1 ).getRealDate();
            Date date2 = ( (AuditBO) pAudit2 ).getRealDate();
            // Si l'attribut pr�cisant le type de comparaison est renseign�
            // on compare selon la date d'ex�cution
            if ( mExecutedDate )
            {
                date1 = ( (AuditBO) pAudit1 ).getDate();
                date2 = ( (AuditBO) pAudit2 ).getDate();
            }
            // la date r�elle ou d'ex�cution peut �tre nulle
            if ( date1 != null )
            {
                // On fait donc la v�rification pour les deux audits
                if ( date2 != null )
                {
                    result = date1.compareTo( date2 );
                }
                else
                {
                    result = -1;
                }
            }
        }
        return result;
    }
}
