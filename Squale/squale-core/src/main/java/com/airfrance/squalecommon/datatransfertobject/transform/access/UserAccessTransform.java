package com.airfrance.squalecommon.datatransfertobject.transform.access;

import java.util.ArrayList;
import java.util.List;

import com.airfrance.squalecommon.datatransfertobject.access.UserAccessDTO;
import com.airfrance.squalecommon.enterpriselayer.businessobject.access.UserAccessBO;

/**
 * Transformeur pour les acc�s utilisateur
 */
public class UserAccessTransform
{

    /**
     * @param pAccessDTOs les acc�s sous forme de DTO
     * @return les acc�s sous forme BO
     */
    public static List dto2bo( List pAccessDTOs )
    {
        int dtoSize = pAccessDTOs.size();
        List stackBO = new ArrayList();
        for ( int i = 0; i < dtoSize; i++ )
        {
            stackBO.add( dto2bo( (UserAccessDTO) pAccessDTOs.get( i ) ) );
        }
        return stackBO;
    }

    /**
     * @param pAccessBOs les acc�s sous forme de BO
     * @return les acc�s sous forme DTO
     */
    public static List bo2dto( List pAccessBOs )
    {
        int boSize = pAccessBOs.size();
        List listDTO = new ArrayList( boSize );
        for ( int i = 0; i < boSize; i++ )
        {
            listDTO.add( bo2dto( (UserAccessBO) pAccessBOs.get( i ) ) );
        }
        return listDTO;
    }

    /**
     * @param pAccessDTO l'acc�s sous forme DTO
     * @return l'acc�s sous forme BO
     */
    public static UserAccessBO dto2bo( UserAccessDTO pAccessDTO )
    {
        UserAccessBO accessBO = new UserAccessBO();
        accessBO.setDate( pAccessDTO.getDate() );
        accessBO.setMatricule( pAccessDTO.getMatricule() );
        accessBO.setId( pAccessDTO.getId() );
        return accessBO;
    }

    /**
     * @param pAccessBO l'acc�s sous forme BO
     * @return l'acc�s sous forme DTO
     */
    public static UserAccessDTO bo2dto( UserAccessBO pAccessBO )
    {
        UserAccessDTO accessDTO = new UserAccessDTO();
        accessDTO.setDate( pAccessBO.getDate() );
        accessDTO.setMatricule( pAccessBO.getMatricule() );
        accessDTO.setId( pAccessBO.getId() );
        return accessDTO;
    }

}
