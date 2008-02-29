package com.airfrance.squalecommon.datatransfertobject.transform.component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.airfrance.squalecommon.SqualeTestCase;
import com.airfrance.squalecommon.datatransfertobject.component.ComponentDTO;
import com.airfrance.squalecommon.datatransfertobject.component.UserDTO;
import com.airfrance.squalecommon.enterpriselayer.businessobject.component.ApplicationBO;
import com.airfrance.squalecommon.enterpriselayer.businessobject.profile.ProfileBO;
import com.airfrance.squalecommon.enterpriselayer.businessobject.profile.UserBO;

/**
 * @author M400841
 *
 * Pour changer le mod�le de ce commentaire de type g�n�r�, allez � :
 * Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 */
public class UserTransformTest extends SqualeTestCase {

    /**
     * teste la transformation d'un UserDTO en UserBO
     */
    public void testDto2Bo() {
        
        // Initialisation du retour et autres
        UserBO userBO = null;
        
        //boolean isApplicationMapped = true; // Permet de savoir si tous les fichiers sont mapp�s
        
        // Initialisation des projets relatifs � un utilisateur
        Collection applications = new ArrayList();
        ComponentDTO application1 = new ComponentDTO();
        application1.setID(1);
        application1.setName("squale");
        ComponentDTO application2 = new ComponentDTO();
        application2.setID(2);
        application2.setName("squalix");
        applications.add(application1);
        applications.add(application2);
        
        UserDTO userDTO = new UserDTO(); // a initialiser
        userDTO.setEmail("buzz@hotmail.com");
        userDTO.setFullName("alexandre bozas");
        userDTO.setID(1);
        userDTO.setMatricule("m400841");
        userDTO.setPassword("xyz");
        userDTO.setProfiles(new HashMap());
        
        userBO = UserTransform.dto2Bo(userDTO);
        userBO.setDefaultProfile(new ProfileBO());
        assertEquals(userBO.getEmail(), userDTO.getEmail());
        assertEquals(userBO.getFullName(), userDTO.getFullName());
        assertEquals(userBO.getMatricule(), userDTO.getMatricule());
        assertEquals(userBO.getPassword(), userDTO.getPassword());
        assertEquals(userBO.getId(), userDTO.getID());
       
       // TODO Verifier par un test que le projet soit bien construit
       // --> A ete verifie en debug
             
        
    }
    /**
     * teste la transformation d'un UserBO en UserDTO
     */
    public void testBo2Dto() {
        
        // Initialisation du retour et autres
        UserDTO userDTO = null;
        //boolean isApplicationMapped = true; // Permet de savoir si tous les fichiers sont mapp�s
        
        // Initialisation des projets relatifs � un utilisateur
        Map applications = new HashMap();
        ApplicationBO application1 = new ApplicationBO();
        application1.setId(1);
        application1.setName("squale");
        ApplicationBO application2 = new ApplicationBO();
        application2.setId(2);
        application2.setName("squalix");
        applications.put(application1, null);
        applications.put(application2, null);
        
        UserBO userBO = new UserBO(); // a initialiser
        // Initialisation du profil par defaut
        ProfileBO defaultProfile = new ProfileBO();
        defaultProfile.setName("admin");
        
        userBO.setDefaultProfile(defaultProfile);
        userBO.setEmail("buzz@hotmail.com");
        userBO.setFullName("alexandre bozas");
        userBO.setId(1);
        userBO.setMatricule("m400841");
        userBO.setPassword("xyz");
        userBO.setRights(applications);
        
        userDTO = UserTransform.bo2Dto(userBO);
        
        assertEquals(userBO.getEmail(), userDTO.getEmail());
        assertEquals(userBO.getDefaultProfile().getName(), userDTO.getDefaultProfile().getName());
        assertEquals(userBO.getFullName(), userDTO.getFullName());
        assertEquals(userBO.getMatricule(), userDTO.getMatricule());
        assertEquals(userBO.getPassword(), userDTO.getPassword());
        assertEquals(userBO.getId(), userDTO.getID());
       
       // Verifier que le projet est bien construit

    }

}
