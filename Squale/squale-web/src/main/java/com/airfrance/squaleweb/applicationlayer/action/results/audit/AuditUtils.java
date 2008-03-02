package com.airfrance.squaleweb.applicationlayer.action.results.audit;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.airfrance.squaleweb.applicationlayer.formbean.component.AuditForm;

/**
 * factorisation du code util dans les actions AuditAction et ManagerAuditAction
 */
public class AuditUtils
{

    /**
     * R�cup�re la s�lection des audits � partir du param�tre et de la liste fournie.
     * 
     * @param pList la liste des audits (AuditForm).
     * @return la liste des audits s�lectionn�s.
     */
    public List getSelection( final List pList )
    {
        ArrayList auditsSelected = new ArrayList();
        // Il s'agit d'une s�lection par check boxes
        if ( pList != null )
        {
            // Au moins un coch�
            Iterator it = pList.iterator();
            AuditForm auditForm;
            // Parcours de la liste des audits
            while ( it.hasNext() )
            {
                auditForm = (AuditForm) it.next();
                // Ajout des audits s�lectionn�s
                if ( auditForm.isSelected() )
                {
                    auditsSelected.add( auditForm );
                }
            }
        }
        return auditsSelected;
    }
}
