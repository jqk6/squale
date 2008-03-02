package com.airfrance.squalecommon.enterpriselayer.facade.macker;

import org.xml.sax.Attributes;

import com.airfrance.squalecommon.enterpriselayer.businessobject.rulechecking.RuleBO;
import com.airfrance.squalecommon.util.xml.FactoryAdapter;

/**
 * Factory pour les r�gles
 */
public class RuleFactory
    extends FactoryAdapter
{

    /** La cat�gorie de la r�gle par d�faut */
    public static final String CATEGORY = "layerrespect";

    /** Le code de la r�gle par d�faut */
    public static final String CODE = "Illegal reference";

    /** La s�v�rit� de la r�gle par d�faut */
    public static final String SEVERITY = "error";

    /**
     * Cr�e une r�gle en lui attribuant une cat�gorie, un code et une s�v�rit� par d�faut.
     * 
     * @see org.apache.commons.digester.ObjectCreationFactory#createObject(org.xml.sax.Attributes)
     */
    public Object createObject( Attributes arg0 )
        throws Exception
    {
        RuleBO rule = new RuleBO();
        rule.setCategory( CATEGORY );
        rule.setCode( CODE );
        rule.setSeverity( SEVERITY );
        return rule;
    }

}
