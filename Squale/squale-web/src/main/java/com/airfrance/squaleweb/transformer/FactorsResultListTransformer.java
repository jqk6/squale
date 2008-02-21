package com.airfrance.squaleweb.transformer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Map.Entry;

import com.airfrance.squalecommon.datatransfertobject.component.AuditGridDTO;
import com.airfrance.squalecommon.datatransfertobject.component.ComponentDTO;
import com.airfrance.squalecommon.datatransfertobject.result.ResultsDTO;
import com.airfrance.squalecommon.datatransfertobject.rule.QualityGridDTO;
import com.airfrance.squaleweb.applicationlayer.formbean.results.FactorsResultListForm;
import com.airfrance.squaleweb.applicationlayer.formbean.results.ProjectFactorsForm;
import com.airfrance.squaleweb.applicationlayer.formbean.results.ResultListForm;
import com.airfrance.welcom.struts.bean.WActionForm;
import com.airfrance.welcom.struts.transformer.WTransformerException;
import com.airfrance.welcom.struts.transformer.WTransformerFactory;

/**
 * Transformation d'une liste de r�sultats en fonction de facteurs
 */
public class FactorsResultListTransformer extends AbstractListTransformer {

    /**
      * @param pObject le tableau de ProjectDTO � transformer en formulaires.
      * @throws WTransformerException si un pb appara�t.
      * @return le formulaire associ�
      */
    public WActionForm objToForm(Object[] pObject) throws WTransformerException {
        ResultListForm form = new ResultListForm();
        objToForm(pObject, form);
        return form;
    }

    /**
     * @param pObject le tableau de ProjectDTO � transformer en formulaires.
     * @param pForm le formulaire � remplir.
     * @throws WTransformerException si un pb appara�t.
     */
    public void objToForm(Object[] pObject, WActionForm pForm) throws WTransformerException {
        Collection applications = (Collection) pObject[0];
        Collection results = (Collection) pObject[1];
        ResultListForm form = (ResultListForm) pForm;
        // On fait un parcours de la liste des projets
        // pour en extraire toutes les grilles
        Collection grids = extractGrids(results);

        // On fait une map par grille existante
        Hashtable gridsForm = new Hashtable();
        Iterator gridsIt = grids.iterator();
        while (gridsIt.hasNext()) {
            // Cr�ation du formulaire qui regroupe les r�sultats autour d'une m�me
            // grille
            FactorsResultListForm factorsForm = new FactorsResultListForm();
            QualityGridDTO grid = (QualityGridDTO) gridsIt.next();
            factorsForm.setFactors(grid.getFactors());
            factorsForm.setGridName(grid.getName());
            factorsForm.setGridUpdateDate(grid.getUpdateDate());
            gridsForm.put(grid, factorsForm);
        }
        form.setList(new ArrayList(gridsForm.values()));

        // On parcourt maintenant chaque r�sultat et en fonction du type de grille
        // on ajoute des donn�es dans le form correspondant

        // On parcourt la liste des r�sultats projet par projet
        Iterator projectResultsIterator = results.iterator();
        while (projectResultsIterator.hasNext()) {
            List projectResults = (List) projectResultsIterator.next();
            /*#########################################
             * Mise en forme des r�sultats
             */
            // Le premier �l�ment de la liste correspond au dernier audit
            Map latestResults = ((ResultsDTO) projectResults.get(0)).getResultMap();
            // On supprime la liste des facteurs renvoy�e par la m�thode
            // avec null comme clef
            latestResults.remove(null);
            // On trie la map pour avoir le m�me ordre dans les r�sultats
            // des audits.
            latestResults = new TreeMap(latestResults);
            Map preLatestResults = null;
            // L'�l�ment suivant s'il existe correspond � l'avant dernier
            // audit et permettra d'obtenir des tendances
            if (projectResults.size() > 1) {
                preLatestResults = ((ResultsDTO) projectResults.get(1)).getResultMap();
                // On supprime la liste des facteurs renvoy�e avec la clef
                // null
                preLatestResults.remove(null);
            }

            Iterator auditGridterator = latestResults.entrySet().iterator();
            // On parcourt chacun des projets pour mettre
            // en forme les r�sultats
            while (auditGridterator.hasNext()) {
                Map.Entry entry = (Entry) auditGridterator.next();
                AuditGridDTO auditGrid = (AuditGridDTO) entry.getKey();
                ComponentDTO component = auditGrid.getProject();
                // Java bean contenant les r�sultats
                ProjectFactorsForm bean = new ProjectFactorsForm(component);
                // On affecte l'id de l'audit courant au form
                bean.setCurrentAuditId("" + auditGrid.getAudit().getID());
                bean.setApplicationId("" + component.getIDParent());
                bean.setApplicationName(TransformerUtils.getApplicationName(component.getIDParent(), applications));
                ArrayList params = new ArrayList();
                params.add(entry);
                // Ajout des tendances � passer en param�tre
                Map.Entry prevEntry = getPreviousResult(component, preLatestResults);
                if (prevEntry!=null) {
                    params.add(prevEntry);
                }
                // Conversion welcome
                WTransformerFactory.objToForm(ProjectFactorsTransformer.class, bean, new Object[] { component, params });
                // R�cup�ration de la grille et placement de celle-ci
                FactorsResultListForm f = (FactorsResultListForm) gridsForm.get(auditGrid.getGrid());
                f.getResults().add(bean);
                // si il y a des r�sultats comparable, on l'indique
                form.setComparableAudits(bean.getComparableAudits());
            }
        }
    }

    /**
     * Obtention du r�sultat pr�c�dent
     * @param pComponent composant
     * @param pPreviousResults r�sultat pr�c�dent ou null si non pr�sents
     * @return r�sultat pr�c�dent ou null si non existant
     */
    private Map.Entry getPreviousResult(ComponentDTO pComponent, Map pPreviousResults) {
        Map.Entry result = null;
        if (pPreviousResults!=null) {
            Iterator it = pPreviousResults.entrySet().iterator();
            // On cherche dans les r�sultats la grille qui correspond au composant
            while (it.hasNext()&&(result==null)) {
                Map.Entry prevEntry = (Entry) it.next();
                AuditGridDTO prevAuditGrid = (AuditGridDTO) prevEntry.getKey();
                if (prevAuditGrid.getProject().getID()==pComponent.getID()) {
                    result = prevEntry;
                }
            }
        }
        return result;
    }
    
    /**
     * Obtention des grilles qualit�
     * @param pResults r�sultats � explorer
     * @return ensemble des grilles qualit�
     */
    private Collection extractGrids(Collection pResults) {
        TreeSet grids = new TreeSet();
        // Le TreeSet contiendra la liste des grilles qualit� utilis�es
        // dans les r�sultats
        Iterator iterator = pResults.iterator();
        // Parcours des r�sultats par application
        // chaque application contient les r�sultats par projet
        while (iterator.hasNext()) {
            Iterator results = ((Collection) iterator.next()).iterator();
            // Parcours des r�sultats par projet et par audit
            // on se limite au dernier audit : la grille qualit� des audits
            // pr�cedents est inutile pour le calcul des tendances
            boolean lastaudit = true;
            while (results.hasNext() && lastaudit) {
                lastaudit = false;
                ResultsDTO result = (ResultsDTO) results.next();
                Iterator keys = result.getResultMap().keySet().iterator();
                // Parcours de chaque projet
                while (keys.hasNext()) {
                    Object key = keys.next();
                    if (key instanceof AuditGridDTO) {
                        AuditGridDTO dto = (AuditGridDTO) key;
                        // Ajout de la grille dans un set ce qui �vite les duplications
                        grids.add(dto.getGrid());
                    }
                }
            }
        }
        return grids;
    }

    /**
     * @param pForm le formulaire � lire.
     * @param pObject le tableau de ProjectDTO qui r�cup�re les donn�es du formulaire.
     * @throws WTransformerException si un pb appara�t.
     */
    public void formToObj(WActionForm pForm, Object[] pObject) throws WTransformerException {
    }

}
