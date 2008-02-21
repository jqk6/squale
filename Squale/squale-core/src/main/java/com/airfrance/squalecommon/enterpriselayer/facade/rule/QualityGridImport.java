package com.airfrance.squalecommon.enterpriselayer.facade.rule;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import com.airfrance.jraf.commons.exception.JrafDaoException;
import com.airfrance.jraf.commons.exception.JrafEnterpriseException;
import com.airfrance.jraf.helper.PersistenceHelper;
import com.airfrance.jraf.provider.persistence.hibernate.facade.FacadeHelper;
import com.airfrance.jraf.spi.persistence.IPersistenceProvider;
import com.airfrance.jraf.spi.persistence.ISession;
import com.airfrance.squalecommon.daolayer.component.ProjectDAOImpl;
import com.airfrance.squalecommon.daolayer.rule.QualityGridDAOImpl;
import com.airfrance.squalecommon.datatransfertobject.rule.QualityGridDTO;
import com.airfrance.squalecommon.datatransfertobject.transform.rule.QualityGridTransform;
import com.airfrance.squalecommon.enterpriselayer.businessobject.rule.QualityGridBO;
import com.airfrance.squalecommon.enterpriselayer.facade.rule.xml.GridImport;

/**
 * Importation de grille qualit�
 */
public class QualityGridImport {
    /**
     * provider de persistence
     */
    private static final IPersistenceProvider PERSISTENTPROVIDER = PersistenceHelper.getPersistenceProvider();

    /**
     * Importation d'une grille sans cr�ation dans la base
     * @param pStream flux de grille
     * @param pErrors erreurs de traitement ou vide si aucune erreur n'est rencontr�e
     * @return collection de grilles import�es sous la forme de QualityGridDTO
     * @throws JrafEnterpriseException si erreur
     */
    public static Collection importGrid(InputStream pStream, StringBuffer pErrors) throws JrafEnterpriseException {
        GridImport gridImport = new GridImport();
        // Importation des grilles
        Collection grids = gridImport.importGrid(pStream, pErrors);
        // Conversion de chacune des grilles
        // et v�rification de leur existence
        ArrayList result = new ArrayList();
        Iterator gridsIt = grids.iterator();
        ISession session = null;
        try {
            // r�cup�ration d'une session
            session = PERSISTENTPROVIDER.getSession();
            // V�rification de la grille
            QualityGridChecker gridChecker = new QualityGridChecker();
            // Parcours des grilles
            while (gridsIt.hasNext()) {
                QualityGridBO gridBO = (QualityGridBO) gridsIt.next();
                // V�rification de la grille
                gridChecker.checkGrid(gridBO, pErrors);
                QualityGridDTO gridDTO = QualityGridTransform.bo2Dto(gridBO);
                // Affectation de l'ID dans le cas d'une grille d�j� pr�sente dans
                // la base de donn�es
                QualityGridBO existingGrid = QualityGridDAOImpl.getInstance().findWhereName(session, gridBO.getName());
                if (existingGrid != null) {
                    gridDTO.setId(existingGrid.getId()); // Cette information sera exploit�e par la partie WEB
                }
                result.add(gridDTO);
            }
        } catch (JrafDaoException e) {
            // Renvoi d'une exception
            FacadeHelper.convertException(e, QualityGridFacade.class.getName() + ".get");
        } finally {
            // Fermeture de la session
            FacadeHelper.closeSession(session, QualityGridFacade.class.getName() + ".get");
        }
        return result;
    }

    /**
     * Importation d'une grille et cr�ation dans la base
     * @param pStream flux de grille
     * @param pErrors erreurs de traitement ou vide si aucune erreur n'est rencontr�e
     * @return collection de grilles import�es sous la forme de QualityGridDTO
     * @throws JrafEnterpriseException si erreur
     */
    public static Collection createGrid(InputStream pStream, StringBuffer pErrors) throws JrafEnterpriseException {
        GridImport gridImport = new GridImport();
        // Importation des grilles
        Collection grids = gridImport.importGrid(pStream, pErrors);
        // Conversion de chacune des grilles
        // et v�rification de leur existence
        ArrayList result = new ArrayList();
        Iterator gridsIt = grids.iterator();
        ISession session = null;
        try {
            // r�cup�ration d'une session
            session = PERSISTENTPROVIDER.getSession();
            QualityGridDAOImpl gridDAO = QualityGridDAOImpl.getInstance();
            ProjectDAOImpl projectDAO = ProjectDAOImpl.getInstance();
            // Parcours des grilles
            while (gridsIt.hasNext()) {
                QualityGridBO gridBO = (QualityGridBO) gridsIt.next();
                gridBO = gridDAO.createGrid(session, gridBO);
                // Mise � jour des projets utilisant la m�me grille qualit�
                projectDAO.updateQualityGrid(session, gridBO);
                // Transformation en DTO
                QualityGridDTO gridDTO = QualityGridTransform.bo2Dto(gridBO);
                result.add(gridDTO);
            }
        } catch (JrafDaoException e) {
            FacadeHelper.convertException(e, QualityGridFacade.class.getName() + ".get");
        } finally {
            FacadeHelper.closeSession(session, QualityGridFacade.class.getName() + ".get");
        }
        return result;
    }
}
