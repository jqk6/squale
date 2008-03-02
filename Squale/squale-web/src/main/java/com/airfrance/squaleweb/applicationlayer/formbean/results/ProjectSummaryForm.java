package com.airfrance.squaleweb.applicationlayer.formbean.results;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMapping;

import com.airfrance.squaleweb.applicationlayer.formbean.RootForm;
import com.airfrance.squaleweb.util.graph.GraphMaker;
import com.airfrance.welcom.struts.bean.WActionForm;

/**
 * Contient les listes de r�sultats d'un projet
 * 
 * @author M400842
 */
public class ProjectSummaryForm
    extends RootForm
{

    /**
     * Notes des facteurs sur le projet
     */
    private ProjectFactorsForm mFactors = new ProjectFactorsForm();

    /**
     * Volum�trie du projet
     */
    private ResultListForm mVolumetry = new ResultListForm();

    /**
     * bool�en indiquant si le projet a provoqu� des erreurs
     */
    private Boolean mHaveErrors = new Boolean( false );

    /**
     * Affichage de tous les facteurs
     */
    private boolean mAllFactors = true;

    /** Indique si le projet peut �tre exporter sous Eclipse */
    private Boolean mExportIDE = new Boolean( true );

    /**
     * bool�en conditionnant l'affichage de la case � cocher "tous les facteurs"
     */
    private Boolean mDisplayCheckBoxFactors = new Boolean( true );

    /** le kiviat de niveau projet */
    private GraphMaker kiviat;

    /** le kiviat de niveau projet */
    private GraphMaker barGraph;

    /** le bubble de niveau projet */
    private GraphMaker histoBarGraph;

    /** Les r�sultats du projet */
    private WActionForm results;

    /**
     * @return le kiviat
     */
    public GraphMaker getKiviat()
    {
        return kiviat;
    }

    /**
     * @param pKiviat le nouveau kiviat
     */
    public void setKiviat( GraphMaker pKiviat )
    {
        kiviat = pKiviat;
    }

    /**
     * @return le barGraph
     */
    public GraphMaker getBarGraph()
    {
        return barGraph;
    }

    /**
     * @param pBarGraph le nouveau BarGraph
     */
    public void setBarGraph( GraphMaker pBarGraph )
    {
        barGraph = pBarGraph;
    }

    /**
     * @return le Graphe en barre pour des pas de 0,1
     */
    public GraphMaker getHistoBarGraph()
    {
        return histoBarGraph;
    }

    /**
     * @param pHisto le nouveau graph pour des pas de 0,1
     */
    public void setHistoBarGraph( GraphMaker pHisto )
    {
        histoBarGraph = pHisto;
    }

    /**
     * @return les facteurs
     */
    public ProjectFactorsForm getFactors()
    {
        return mFactors;
    }

    /**
     * @return la volum�trie
     */
    public ResultListForm getVolumetry()
    {
        return mVolumetry;
    }

    /**
     * @param pFactors les notes des facteurs
     */
    public void setFactors( ProjectFactorsForm pFactors )
    {
        mFactors = pFactors;
    }

    /**
     * @param pVolumetry volum�trie
     */
    public void setVolumetry( ResultListForm pVolumetry )
    {
        mVolumetry = pVolumetry;

    }

    /**
     * @return mHaveErrors
     */
    public Boolean getHaveErrors()
    {
        return mHaveErrors;
    }

    /**
     * @param pHaveErrors la nouvelle valeur
     */
    public void setHaveErrors( Boolean pHaveErrors )
    {
        mHaveErrors = pHaveErrors;
    }

    /**
     * @param pAllFactors affichage de tous les facteurs
     */
    public void setAllFactors( boolean pAllFactors )
    {
        mAllFactors = pAllFactors;
    }

    /**
     * @return true si tous les facteurs sont affich�s
     */
    public boolean isAllFactors()
    {
        return mAllFactors;
    }

    /**
     * @return mDisplayCheckBoxFactors affichage de la case � cocher "Tous les facteurs"
     */
    public Boolean getDisplayCheckBoxFactors()
    {
        return mDisplayCheckBoxFactors;
    }

    /**
     * @param pDisplayCheckBoxFactors affichage de la case � cocher "Tous les facteurs"
     */
    public void setDisplayCheckBoxFactors( Boolean pDisplayCheckBoxFactors )
    {
        mDisplayCheckBoxFactors = pDisplayCheckBoxFactors;
    }

    /**
     * @see org.apache.struts.action.ActionForm#reset(org.apache.struts.action.ActionMapping,
     *      javax.servlet.http.HttpServletRequest) {@inheritDoc}
     */
    public void reset( ActionMapping mapping, HttpServletRequest request )
    {
        super.reset( mapping, request );
        // Reinitialisation du checkbox
        mAllFactors = false;
    }

    /**
     * @return les r�sultats
     */
    public WActionForm getResults()
    {
        return results;
    }

    /**
     * @param pForm les r�sultats
     */
    public void setResults( WActionForm pForm )
    {
        results = pForm;
    }

    /**
     * @return true si le projet peut �tre exporter sous eclipse
     */
    public Boolean getExportIDE()
    {
        return mExportIDE;
    }

    /**
     * @param pExportIDE true si le projet peut �tre exporter sous eclipse
     */
    public void setExportIDE( Boolean pExportIDE )
    {
        mExportIDE = pExportIDE;
    }

}
