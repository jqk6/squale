package com.airfrance.squalecommon.datatransfertobject.transform.component.parameters;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.airfrance.squalecommon.datatransfertobject.component.parameters.ListParameterDTO;
import com.airfrance.squalecommon.datatransfertobject.component.parameters.MapParameterDTO;
import com.airfrance.squalecommon.datatransfertobject.component.parameters.StringParameterDTO;
import com.airfrance.squalecommon.enterpriselayer.businessobject.component.parameters.ListParameterBO;
import com.airfrance.squalecommon.enterpriselayer.businessobject.component.parameters.MapParameterBO;
import com.airfrance.squalecommon.enterpriselayer.businessobject.component.parameters.StringParameterBO;

/**
 * Transforme dto <-> bo pour un MapParameter
 */
public class MapParameterTransform
{

    /**
     * @param pMapParameterDTO le DTO � transformer en BO
     * @return le BO
     */
    public static MapParameterBO dto2Bo( MapParameterDTO pMapParameterDTO )
    {
        MapParameterBO result = new MapParameterBO();
        result.setId( pMapParameterDTO.getId() );
        // On r�cup�re les param�tres associ�s
        Map paramsDTO = pMapParameterDTO.getParameters();
        // On va remplir la map avec des objets transform�s en BO.
        Map paramsBO = new HashMap();
        // On va parcourir toute la map et transformer tous ses �l�ments. Il faut
        // donc r�cup�rer l'ensemble des cl�s pour pouvoir ensuite r�cup�rer les �l�ments
        // associ�s.
        java.util.Set keys = paramsDTO.keySet();
        for ( Iterator it = keys.iterator(); it.hasNext(); )
        {
            String currentKey = (String) it.next();
            // si l'�l�ment courant est une map, on rappelle r�cursivement la m�thode
            // pour transformer aussi tous ses �l�ments.
            if ( paramsDTO.get( currentKey ) instanceof MapParameterDTO )
            {
                paramsBO.put( currentKey, MapParameterTransform.dto2Bo( (MapParameterDTO) paramsDTO.get( currentKey ) ) );
            }
            else
            {
                // Si il s'agit d'une liste, on va appeler la m�thode qui transforme cette objet
                // ainsi que tous ses �l�ments en BO.
                if ( paramsDTO.get( currentKey ) instanceof ListParameterDTO )
                {
                    paramsBO.put( currentKey,
                                  ListParameterTransform.dto2Bo( (ListParameterDTO) paramsDTO.get( currentKey ) ) );
                }
                else
                {
                    if ( paramsDTO.get( currentKey ) instanceof StringParameterDTO )
                    {
                        paramsBO.put(
                                      currentKey,
                                      StringParameterTransform.dto2Bo( (StringParameterDTO) paramsDTO.get( currentKey ) ) );
                    }
                }
            }
        }
        result.setParameters( paramsBO );
        return result;
    }

    /**
     * @param pMapParameterBO le BO � transformer en DTO
     * @return le DTO
     */
    public static MapParameterDTO bo2Dto( MapParameterBO pMapParameterBO )
    {
        MapParameterDTO result = new MapParameterDTO();
        result.setId( pMapParameterBO.getId() );
        // On r�cup�re les param�tres associ�s
        Map paramsBO = pMapParameterBO.getParameters();
        // On va remplir la map avec des objets transform�s en DTO.
        Map paramsDTO = new HashMap();
        // On va parcourir toute la map et transformer tous ses �l�ments. Il faut
        // donc r�cup�rer l'ensemble des cl�s pour pouvoir ensuite r�cup�rer les �l�ments
        // associ�s.
        java.util.Set keys = paramsBO.keySet();
        for ( Iterator it = keys.iterator(); it.hasNext(); )
        {
            String currentKey = (String) it.next();
            // si l'�l�ment courant est une map, on rappelle r�cursivement la m�thode
            // pour transformer aussi tous ses �l�ments.
            if ( paramsBO.get( currentKey ) instanceof MapParameterBO )
            {
                paramsDTO.put( currentKey, MapParameterTransform.bo2Dto( (MapParameterBO) paramsBO.get( currentKey ) ) );
            }
            else
            {
                // Si il s'agit d'une liste, on va appeler la m�thode qui transforme cette objet
                // ainsi que tous ses �l�ments en DTO.
                if ( paramsBO.get( currentKey ) instanceof ListParameterBO )
                {
                    paramsDTO.put( currentKey,
                                   ListParameterTransform.bo2Dto( (ListParameterBO) paramsBO.get( currentKey ) ) );
                }
                else
                {
                    if ( paramsBO.get( currentKey ) instanceof StringParameterBO )
                    {
                        paramsDTO.put( currentKey,
                                       StringParameterTransform.bo2Dto( (StringParameterBO) paramsBO.get( currentKey ) ) );
                    }
                }
            }
        }
        result.setParameters( paramsDTO );
        return result;
    }
}