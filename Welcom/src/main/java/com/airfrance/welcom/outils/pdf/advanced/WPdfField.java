package com.airfrance.welcom.outils.pdf.advanced;

/*
 * Cr�� le 3 nov. 04
 *
 * Pour changer le mod�le de ce fichier g�n�r�, allez � :
 * Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 */

/**
 * @author M327837 Pour changer le mod�le de ce commentaire de type g�n�r�, allez � :
 *         Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 */
public class WPdfField
{
    /** le name */
    private String name = "";

    /** la valeur par defaut */
    private String defaultValue = "";

    /** la value */
    private String value = "";

    /** le type */
    private WPdfFieldType type = WPdfFieldType.INCONNU;

    /**
     * @return defaultValue
     */
    public String getDefaultValue()
    {
        return defaultValue;
    }

    /**
     * @return name
     */
    public String getName()
    {
        return name;
    }

    /**
     * @return type
     */
    public WPdfFieldType getType()
    {
        return type;
    }

    /**
     * @return value
     */
    public String getValue()
    {
        return value;
    }

    /**
     * @param string defaultValue
     */
    public void setDefaultValue( final String string )
    {
        defaultValue = string;
    }

    /**
     * @param string name
     */
    public void setName( final String string )
    {
        name = string;
    }

    /**
     * @param pType type
     */
    public void setType( final WPdfFieldType pType )
    {
        type = pType;
    }

    /**
     * @param string value
     */
    public void setValue( final String string )
    {
        value = string;
    }

    /**
     * @see java.lang.Object#toString()
     */
    public String toString()
    {
        return name + " : ('" + defaultValue + "','" + value + "','" + type + "')";
    }

}
