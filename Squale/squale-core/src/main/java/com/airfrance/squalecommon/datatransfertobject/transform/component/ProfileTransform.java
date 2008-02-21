/*
 * Cr�� le 12 juil. 05
 *
 * Pour changer le mod�le de ce fichier g�n�r�, allez � :
 * Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 */
package com.airfrance.squalecommon.datatransfertobject.transform.component;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.airfrance.squalecommon.datatransfertobject.component.ProfileDTO;
import com.airfrance.squalecommon.enterpriselayer.businessobject.profile.AtomicRightsBO;
import com.airfrance.squalecommon.enterpriselayer.businessobject.profile.ProfileBO;

/**
 * @author M400841
 *
 * Pour changer le mod�le de ce commentaire de type g�n�r�, allez � :
 * Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 */
public class ProfileTransform implements Serializable {


    /**
     * Constructeur prive
     */
    private ProfileTransform() {
    }

    /**
     * DTO -> BO pour un Profile
     * @param pProfileDTO ProfileDTO � transformer
     * @return ProfileBO
     * @deprecated non utilise car on ne renverra pas de profile
     */
    public static ProfileBO dto2Bo(ProfileDTO pProfileDTO) {
        
        // Initialisation
        ProfileBO profileBO = new ProfileBO();
        Map rightsBO = new HashMap(); // droits
        
        profileBO.setName(pProfileDTO.getName());
        profileBO.setRights(rightsBO);
        
        return profileBO;
        
    }

    /**
     * Utilis� pour r�cup�rer un utilisateur
     * BO -> DTO pour un Profile
     * @param pProfileBO ProfileBO
     * @return ProfileDTO
     */
    public static ProfileDTO bo2Dto(ProfileBO pProfileBO) {
        
        // Initialisation
        ProfileDTO profileDTO = new ProfileDTO();
        Map rightsDTO = new HashMap(); // droits
        
        Iterator rightsIterator = pProfileBO.getRights().keySet().iterator();
        AtomicRightsBO currentKey = null;
        while(rightsIterator.hasNext()){
            currentKey = (AtomicRightsBO) rightsIterator.next();
            // Ajout dans la map DTO du nom du droit atomique et de la valeur 
            // correspondante a la cl�
            rightsDTO.put(currentKey.getName(), pProfileBO.getRights().get(currentKey));
        }
        
        
        profileDTO.setName(pProfileBO.getName());
        profileDTO.setRights(rightsDTO);
        
        return profileDTO;
    }

}
