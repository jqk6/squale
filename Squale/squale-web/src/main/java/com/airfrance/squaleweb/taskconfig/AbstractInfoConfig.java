package com.airfrance.squaleweb.taskconfig;

/**
 * Classe d�finissant les infos g�n�rique n�cessaire pour les champs d'une JSP
 */
public abstract class AbstractInfoConfig
{

    /** La cl� du label */
    private String mKey;

    /** Le nom du champ */
    private String mProperty;

    /**
     * @return renvoi la cl� pour le label
     */
    public String getKey()
    {
        return mKey;
    }

    /**
     * @return renvoi le nom du champ
     */
    public String getProperty()
    {
        return mProperty;
    }

    /**
     * @param pKey insert la cl� pour le field
     */
    public void setKey( String pKey )
    {
        mKey = pKey;
    }

    /**
     * @param pProperty insert le nom du champ
     */
    public void setProperty( String pProperty )
    {
        mProperty = pProperty;
    }

    /**
     * constructeur
     * 
     * @param pKey Cl� pour le label
     * @param pProperty Nom du champ
     */
    public AbstractInfoConfig( String pKey, String pProperty )
    {
        mKey = pKey;
        mProperty = pProperty;
    }

}
