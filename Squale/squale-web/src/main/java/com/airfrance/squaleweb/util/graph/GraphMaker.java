/*
 * Cr�� le 23 janv. 07
 *
 * Classe m�morisant les caract�ristiques li�es �
 * l'affichage d'un graphe JFreeChart comportant des liens HTML
 */
package com.airfrance.squaleweb.util.graph;

import javax.servlet.http.HttpServletRequest;

import org.jfree.chart.ChartRenderingInfo;
import org.jfree.chart.ChartUtilities;

import com.airfrance.squaleweb.resources.WebMessages;

/**
 * @author 6370258
 *
 */
public class GraphMaker {
    /**
     * D�finition de l'Image Map pour l'ex�cution des liens HTML
     */
    private String mapDescription;
    /**
     * Nom de l'image source
     */
    private String srcName;
    /**
     * Nom de l'image Map
     */
    private String useMapName;

    /**
     * @return mapDescription d�tail des liens
     */
    public String getMapDescription() {
        return mapDescription;
    }

    /**
     * @return srcName nom de l'image source
     */
    public String getSrcName() {
        return srcName;
    }

    /**
     * @return useMapName le nom de la Map dont on cherche les caract�ristiques
     */
    public String getUseMapName() {
        return useMapName;
    }

    /**
     * @param pMapDescription desciption de l'Image Map
     */
    public void setMapDescription(String pMapDescription) {
        mapDescription = pMapDescription;
    }

    /**
     * @param pSrcName nom de l'image source 
     */
    public void setSrcName(String pSrcName) {
        srcName = pSrcName;
    }

    /**
     * @param pUseMapName nom de l'Image Map
     */
    public void setUseMapName(String pUseMapName) {
        useMapName = pUseMapName;
    }

    /**
     * Factorisation du code mettant � jour les donn�s d'une image cliquables
     * @param fileName le nom du fichier
     * @param info le ChartRenderingInfo de l'image
     * @param pRequest la requete pour internationalisation
     */
    public GraphMaker(HttpServletRequest pRequest,String fileName,ChartRenderingInfo info) {
        setMapDescription(ChartUtilities.getImageMap(fileName, info));
        setSrcName(pRequest.getContextPath() + WebMessages.getString("default.path.display") + fileName);
        setUseMapName("#" + fileName);

    }

}
