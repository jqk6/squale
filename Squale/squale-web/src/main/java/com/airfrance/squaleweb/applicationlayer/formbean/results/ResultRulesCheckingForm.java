package com.airfrance.squaleweb.applicationlayer.formbean.results;

import java.util.ArrayList;
import java.util.List;

/**
 * Bean pour les r�sultats des transgressions
 */
public class ResultRulesCheckingForm
    extends ResultForm
{

    /* Constantes de severite pour remplir le tableau de repartition */

    /** Cas info */
    public static final int INFO_INT = 0;

    /** Cas warning */
    public static final int WARNING_INT = 1;

    /** Cas error */
    public static final int ERROR_INT = 2;

    /**
     * Liste des r�sultats
     */
    private List mList = new ArrayList();

    /**
     * @return la liste des r�sultats
     */
    public List getList()
    {
        return mList;
    }

    /**
     * @param pList la liste des r�sultats
     */
    public void setList( List pList )
    {
        mList = pList;

    }
}
