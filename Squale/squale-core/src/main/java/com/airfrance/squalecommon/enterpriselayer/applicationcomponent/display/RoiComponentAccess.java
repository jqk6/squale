package com.airfrance.squalecommon.enterpriselayer.applicationcomponent.display;

import com.airfrance.jraf.commons.exception.JrafEnterpriseException;
import com.airfrance.jraf.provider.accessdelegate.DefaultExecuteComponent;
import com.airfrance.squalecommon.datatransfertobject.result.RoiDTO;
import com.airfrance.squalecommon.enterpriselayer.facade.roi.RoiFacade;

/**
 * Classe permettant la gestion du ROI
 */
public class RoiComponentAccess
    extends DefaultExecuteComponent
{

    /**
     * Modifie la formule du ROI
     * 
     * @param pRoiDto le ROI
     * @param pErrors pour r�cup�rer les erreurs
     * @return le ROI sous forme DTO modifi�
     * @throws JrafEnterpriseException si erreur
     */
    public RoiDTO updateFormula( RoiDTO pRoiDto, StringBuffer pErrors )
        throws JrafEnterpriseException
    {
        return RoiFacade.updateFormula( pRoiDto, pErrors );
    }

    /**
     * R�cup�re les informations n�cessaires au traitement du ROI
     * 
     * @param pApplicationId l'id de l'application dont on veut le ROI -1 si le veut pour toutes les applications
     * @return le roi sous forme DTO
     * @throws JrafEnterpriseException si erreur
     */
    public RoiDTO getROI( Long pApplicationId )
        throws JrafEnterpriseException
    {
        return RoiFacade.getROI( pApplicationId );
    }
}
