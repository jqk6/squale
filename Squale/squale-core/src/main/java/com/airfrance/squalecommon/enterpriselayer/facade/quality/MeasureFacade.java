package com.airfrance.squalecommon.enterpriselayer.facade.quality;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jfree.chart.JFreeChart;

import com.airfrance.jraf.commons.exception.JrafDaoException;
import com.airfrance.jraf.commons.exception.JrafEnterpriseException;
import com.airfrance.jraf.helper.PersistenceHelper;
import com.airfrance.jraf.provider.persistence.hibernate.facade.FacadeHelper;
import com.airfrance.jraf.spi.enterpriselayer.IFacade;
import com.airfrance.jraf.spi.persistence.IPersistenceProvider;
import com.airfrance.jraf.spi.persistence.ISession;
import com.airfrance.squalecommon.daolayer.component.ApplicationDAOImpl;
import com.airfrance.squalecommon.daolayer.component.AuditDAOImpl;
import com.airfrance.squalecommon.daolayer.component.AuditDisplayConfDAOImpl;
import com.airfrance.squalecommon.daolayer.component.AuditGridDAOImpl;
import com.airfrance.squalecommon.daolayer.component.ProjectDAOImpl;
import com.airfrance.squalecommon.daolayer.result.MarkDAOImpl;
import com.airfrance.squalecommon.daolayer.result.MeasureDAOImpl;
import com.airfrance.squalecommon.daolayer.result.MetricDAOImpl;
import com.airfrance.squalecommon.daolayer.result.QualityResultDAOImpl;
import com.airfrance.squalecommon.datatransfertobject.component.AuditDTO;
import com.airfrance.squalecommon.datatransfertobject.component.ComponentDTO;
import com.airfrance.squalecommon.datatransfertobject.result.ResultsDTO;
import com.airfrance.squalecommon.datatransfertobject.transform.component.AuditTransform;
import com.airfrance.squalecommon.datatransfertobject.transform.result.MeasureTransform;
import com.airfrance.squalecommon.enterpriselayer.businessobject.component.ApplicationBO;
import com.airfrance.squalecommon.enterpriselayer.businessobject.component.AuditBO;
import com.airfrance.squalecommon.enterpriselayer.businessobject.component.AuditDisplayConfBO;
import com.airfrance.squalecommon.enterpriselayer.businessobject.component.AuditGridBO;
import com.airfrance.squalecommon.enterpriselayer.businessobject.component.ProjectBO;
import com.airfrance.squalecommon.enterpriselayer.businessobject.config.web.BubbleConfBO;
import com.airfrance.squalecommon.enterpriselayer.businessobject.config.web.DisplayConfConstants;
import com.airfrance.squalecommon.enterpriselayer.businessobject.config.web.VolumetryConfBO;
import com.airfrance.squalecommon.enterpriselayer.businessobject.result.FactorResultBO;
import com.airfrance.squalecommon.enterpriselayer.businessobject.result.IntegerMetricBO;
import com.airfrance.squalecommon.enterpriselayer.businessobject.result.MarkBO;
import com.airfrance.squalecommon.enterpriselayer.businessobject.result.MeasureBO;
import com.airfrance.squalecommon.util.mapping.Mapping;

/**
 */
public class MeasureFacade implements IFacade {

    /**
     * Cle de tre relatif a l'historique
     */
    private static final String HISTORIC_TYPE = "tre.graph.histo";

    /** log */
    private static Log LOG = LogFactory.getLog(MeasureFacade.class);

    /**
     * provider de persistence
     */
    private static final IPersistenceProvider PERSISTENTPROVIDER = PersistenceHelper.getPersistenceProvider();

    /**
     * Permet de r�cup�rer une valeur pour un type de r�sultat, un composant 
     * et un audit donn�
     * Format de ResultsDTO : 1 ligne :
     * -- null en cl� et liste contenant le resultat en valeur
     * @param pAudit AuditDTO contenant l'ID de l'audit
     * @param pComponent ComponentDTO contenant l'ID du composant
     * @param pKeyTRE Cl� du type �l�mentaire de r�sultat
     * @return ResultsDTO correspondant � une valeur
     * @throws JrafEnterpriseException exception JRAF
     * @roseuid 42CBFFB4016D
     */
    public static ResultsDTO getMeasures(AuditDTO pAudit, ComponentDTO pComponent, String pKeyTRE) throws JrafEnterpriseException {
        ResultsDTO results = null;
        MeasureDAOImpl measureDAO = MeasureDAOImpl.getInstance();
        ISession session = null;

        try {
            session = PERSISTENTPROVIDER.getSession();

            Long idComponent = new Long(pComponent.getID());
            Long idAudit = new Long(pAudit.getID());

            MeasureBO measure = measureDAO.load(session, idComponent, idAudit, Mapping.getMetricClass(pKeyTRE));

            results = MeasureTransform.bo2dto(measure, pKeyTRE);

        } catch (JrafDaoException e) {
            FacadeHelper.convertException(e, MeasureFacade.class.getName() + ".getMeasures");
        } finally {
            FacadeHelper.closeSession(session, MeasureFacade.class.getName() + ".getMeasures");
        }

        return results;
    }

    /**
     * Permet de r�cup�rer une liste de valeurs pour une liste de types de r�sultat, 
     * un type de composant et un audit donn�
     * Format de ResultsDTO : 2 lignes :
     * -- null en cl� et liste des cl�s des TREs en valeur
     * -- ComponentDTO en cl� et liste des r�sultats associ�es en valeur
     * @param pAudit AuditDTO contenant l'ID de l'audit
     * @param pComponent ComponentDTO contenant l'ID du composant
     * @return ResultsDTO
     * @throws JrafEnterpriseException exception JRAF
     * @roseuid 42CBFFB40181
     */
    public static ResultsDTO getAllMeasures(AuditDTO pAudit, ComponentDTO pComponent) throws JrafEnterpriseException {
        // D�claration du ResultsDTO � retourner
        ResultsDTO results = null;
        Collection trClasses = null;
        MeasureDAOImpl measureDAO = MeasureDAOImpl.getInstance();
        // Session Hibernate
        ISession session = null;

        try {
            // R�cup�ration d'une session
            session = PERSISTENTPROVIDER.getSession();

            // R�cup�ration des id de l'audit et du composant
            Long idComponent = new Long(pComponent.getID());
            Long idAudit = new Long(pAudit.getID());
            // R�cup�ration des mesures souhait�es
            Collection measures = measureDAO.findWhere(session, idComponent, idAudit);
            // transformation de la liste de mesures en ResultsDTO
            results = MeasureTransform.bo2dtoByTRE(null, measures, pComponent);
            // Pour la 3.0, on conserve les mesures mcCabe concernant le nombre de m�thodes et de classes sur un projet
            // On supprime (ATTENTION : seulement de l'affichage) donc les donn�es RSM �quivalentes pour ne pas faire doublon
            // Toutefois on garde le stockage des donn�es en base
            // On r�cup�re les index des cl�s pour pouvoir supprimer les valeurs aussi
            int indexClasses = ((List) (results.getResultMap().get(null))).indexOf("rsm.project.numberOfClasses");
            int indexMeth = ((List) (results.getResultMap().get(null))).indexOf("rsm.project.numberOfMethods");
            // Supprime les cl�s
             ((List) (results.getResultMap().get(null))).remove("rsm.project.numberOfClasses");
            ((List) (results.getResultMap().get(null))).remove("rsm.project.numberOfMethods");
            // Supprime les valeurs
            //On supprime d'abord le plus grand index pour �viter les d�calages
            if (indexClasses != -1) {
                if (indexMeth != -1) {
                    if (indexClasses > indexMeth) {
                        ((List) (results.getResultMap().get(pComponent))).remove(indexClasses);
                        ((List) (results.getResultMap().get(pComponent))).remove(indexMeth);
                    } else {
                        ((List) (results.getResultMap().get(pComponent))).remove(indexMeth);
                        ((List) (results.getResultMap().get(pComponent))).remove(indexClasses);
                    }
                } else {
                    ((List) (results.getResultMap().get(pComponent))).remove(indexClasses);
                }
            } else {
                if (indexMeth != -1) {
                    ((List) (results.getResultMap().get(pComponent))).remove(indexMeth);
                }
            }

        } catch (JrafDaoException e) {
            FacadeHelper.convertException(e, MeasureFacade.class.getName() + ".getMeasuresByTRE");
        } finally {
            FacadeHelper.closeSession(session, MeasureFacade.class.getName() + ".getMeasuresByTRE");
        }
        return results;
    }

    /**
     * Permet de r�cup�rer une liste des "tops" composant / measure pour 
     * un audit et un tre donn�s
     * Format de ResultsDTO : 2 lignes :
     * -- null en cl� et liste des ComponentDTOs 
     * -- AuditDTO en cl� et liste des r�sultats associ�es en valeur
     * @param pProject projet 
     * @param pComponentType Type de composant (Method ou Class)
     * @param pAudit Audit
     * @param pTre Tre � retrouver et tri�
     * @param pMax nombre maxi de resultat retoun�
     * @return ResultsDTO tri�
     * @throws JrafEnterpriseException exception JRAF
     */
    public static ResultsDTO getTop(ComponentDTO pProject, String pComponentType, AuditDTO pAudit, String pTre, Integer pMax) throws JrafEnterpriseException {
        ResultsDTO results = null;
        MeasureDAOImpl measureDAO = MeasureDAOImpl.getInstance();
        // Session Hibernate
        ISession session = null;
        try {
            session = PERSISTENTPROVIDER.getSession();
            List component_measures = (List) measureDAO.findTop(session, pProject.getID(), Mapping.getComponentClass(pComponentType), pAudit.getID(), pTre, pMax);
            if (component_measures.size() > 0) {
                // Transformation de la liste de mesures en ResultDTO
                results = MeasureTransform.bo2dtoByAuditOrComponent(null, component_measures, pAudit);
            }
        } catch (JrafDaoException e) {
            FacadeHelper.convertException(e, MeasureFacade.class.getName() + ".getMeasuresByTRE");
        } finally {
            FacadeHelper.closeSession(session, MeasureFacade.class.getName() + ".getMeasuresByTRE");
        }
        return results;
    }

    /**
     * Permet de r�cup�rer des valeurs pour une liste de types de r�sultat, une liste 
     * de types de composant et un audit donn�s
     * Format de ResultsDTO : 2 lignes :
     * -- null en cl� et liste des cl�s de TREs en valeur
     * -- ComponentDTO en cl� et liste des r�sultats associ�es en valeur ( n fois )
     * @param pAudit AuditDTO contenant l'ID de l'audit
     * @param pTreKey l'id du tre correspondant � la pratique
     * @param pTREKeys liste des IDs des types de r�sultats souhait�s
     * @param pComponents liste de ComponentDTO souhait�s
     * @return ResultsDTO
     * @throws JrafEnterpriseException exception JRAF
     * @roseuid 42CBFFB401C0
     */
    public static ResultsDTO getMeasuresByTREAndComponent(Long pTreKey, List pTREKeys, List pComponents, AuditDTO pAudit) throws JrafEnterpriseException {
        ResultsDTO results = null;
        Collection trClasses = null;
        MeasureDAOImpl measureDAO = MeasureDAOImpl.getInstance();
        // Session Hibernate
        ISession session = null;

        try {
            // r�cup�ration d'une session
            session = PERSISTENTPROVIDER.getSession();
            // R�cup�ration de la collection de classes de TREs
            trClasses = getTREClasses(pTREKeys);

            if (trClasses.size() > 0) {
                // initialisation du ResultsDTO et ajout des cl�s
                results = new ResultsDTO();
                results.put(null, pTREKeys);
                Iterator itComp = pComponents.iterator();
                while (itComp.hasNext()) {
                    // Pour chaque composant :
                    ComponentDTO component = (ComponentDTO) itComp.next();
                    Long idComponent = new Long(component.getID());
                    Long idAudit = new Long(pAudit.getID());
                    // R�cup�ration des mesures souhait�es
                    Collection measures = measureDAO.findWhere(session, idComponent, idAudit, trClasses);
                    if (measures.size() > 0) {
                        // ajout au R�sult DTO des mesures souhait�es
                        results = MeasureTransform.bo2dtoByTRE(results, measures, pTREKeys, component);
                        // On r�cup�re aussi la note de la pratique associ�e � chaque composant
                        // que l'on place en d�but de liste
                        MarkBO mark = MarkDAOImpl.getInstance().load(session, new Long(component.getID()), new Long(pAudit.getID()), pTreKey);
                        ((List) results.getResultMap().get(component)).add(0, new Float(mark.getValue()));
                    }
                }
            }
        } catch (JrafDaoException e) {
            LOG.error(MeasureFacade.class.getName() + ".getMeasuresByTREAndComponent", e);
        } finally {
            FacadeHelper.closeSession(session, MeasureFacade.class.getName() + ".getMeasuresByTREAndComponent");
        }
        return results;
    }

    /**
     * R�cup�re les classes de TRE � partir de la liste de TREs
     * @param pTREKeys liste de cl�s de TREs
     * @return une collection de classe de TRE
     */
    private static Collection getTREClasses(List pTREKeys) {
        Set trClasses = new HashSet();
        Iterator it = pTREKeys.iterator();
        while (it.hasNext()) {
            // pour chaque cl� de type de r�sultat
            String treKey = (String) it.next();
            // on ajoute la classe correspondante � la liste de classe de TRE
            trClasses.add(Mapping.getMetricClass(treKey));
        }

        return trClasses;
    }

    /**
     * Constructeur vide
     * @roseuid 42CBFFB40203
     */
    private MeasureFacade() {
    }

    /**
     * Creation d'une mesure de type Kiviat pour une application
     * @param pAuditId  Id de l'audit
     * @throws JrafEnterpriseException  en cas de pb Hibernate
     * @return les donn�es n�cessaires � la conception du Kiviat via une Applet soit : 
     * Map dont la cl� contient les projets et la valeur contient une map (facteurs, notes)
     */
    public static Map getApplicationKiviat(Long pAuditId) throws JrafEnterpriseException {
        MeasureDAOImpl measureDAO = MeasureDAOImpl.getInstance();
        // Session Hibernate
        ISession session = null;
        Map result = new HashMap();
        try {
            // r�cup�ration d'une session
            session = PERSISTENTPROVIDER.getSession();
            ApplicationBO application = (ApplicationBO) ApplicationDAOImpl.getInstance().loadByAuditId(session, pAuditId);
            Iterator itp = application.getChildren().iterator();
            while (itp.hasNext()) {
                // On ajoute les notes de chaque projets sur le kiviat
                ProjectBO project = (ProjectBO) itp.next();
                SortedMap values = new TreeMap();
                Long idProject = new Long(project.getId());
                // recupere les facteurs du projet
                Collection factorResults = QualityResultDAOImpl.getInstance().findWhere(session, idProject, pAuditId);
                // et cree le map nom => note correspondant
                Iterator itf = factorResults.iterator();
                while (itf.hasNext()) {
                    FactorResultBO factor = (FactorResultBO) itf.next();
                    // le -1 est trait� directement par le kiviatMaker
                    Float value = new Float(factor.getMeanMark());
                    // le nom internationalis� est g�r� dans le kiviatMaker
                    String name = factor.getRule().getName();
                    values.put(name, value);
                }
                result.put(project.getName(), values);
            }

        } catch (Exception e) {
            FacadeHelper.convertException(e, MeasureFacade.class.getName() + ".getMeasures");
        } finally {
            FacadeHelper.closeSession(session, MeasureFacade.class.getName() + ".getMeasures");
        }
        return result;
    }

    /**
     * Creation d'une mesure de type Kiviat pour un projet
     * @param pProjectId Id du projet
     * @param pAuditId  Id de l'audit
     * @param pAllFactors tous les facteurs (= "true") ou seulement ceux ayant une note ?
     * @throws JrafEnterpriseException  en cas de pb Hibernate
     * @return tableau d'objets : la map des donn�es + le bool�en pour affichage de la case � cocher tous les facteurs 
     */
    public static Object[] getProjectKiviat(Long pProjectId, Long pAuditId, String pAllFactors) throws JrafEnterpriseException {
        Map result = new HashMap();
        JFreeChart projectKiviat = null;
        MeasureDAOImpl measureDAO = MeasureDAOImpl.getInstance();
        // Session Hibernate
        ISession session = null;
        // Bool�en conditonnanant l'affichage de la case � cocher "tous les facteurs" dans la page Jsp
        boolean displayCheckBoxFactors = true;
        try {
            // r�cup�ration d'une session
            session = PERSISTENTPROVIDER.getSession();
            // On ajoute les notes de chaque projets sur le kiviat
            ProjectBO project = (ProjectBO) ProjectDAOImpl.getInstance().load(session, pProjectId);
            SortedMap values = new TreeMap();
            // recupere les facteurs du projet
            Collection factorResults = QualityResultDAOImpl.getInstance().findWhere(session, pProjectId, pAuditId);
            // et cree le map nom => note correspondant
            Iterator it = factorResults.iterator();
            ArrayList nullValuesList = new ArrayList();
            while (it.hasNext()) {
                FactorResultBO factor = (FactorResultBO) it.next();
                // le -1 est trait� directement par le kiviatMaker
                Float value = new Float(factor.getMeanMark());
                // ajoute la note dans le titre
                // TODO prendre le v�ritable nom du facteur                 
                String name = factor.getRule().getName();
                if (value.floatValue() >= 0) {
                    // avec 1 seul chiffre apr�s la virgule
                    NumberFormat nf = NumberFormat.getInstance();
                    nf.setMinimumFractionDigits(1);
                    nf.setMaximumFractionDigits(1);
                    name = name + " (" + nf.format(value) + ")";
                } else {
                    // M�morisation temporaire des facteurs pour lesquels les notes sont nulles : sera utile si l'option
                    // "Tous les facteurs" est coch�e pour afficher uniquement les facteurs ayant une note.
                    nullValuesList.add(name);
                }
                values.put(name, value);
            }
            final int FACTORS_MIN = 3;
            if (nullValuesList.size() <= 0 || values.size() <= FACTORS_MIN) {
                displayCheckBoxFactors = false;
            }
            // Seulement les facteurs ayant une note ? ==> suppression des facteurs ayant une note nulle.
            // Mais trois facteurs doivent au moins s'afficher (nuls ou pas !)       
            values = deleteFactors(values, nullValuesList, pAllFactors, FACTORS_MIN);

            // recup�re le nom de l'audit 
            String name = null;
            AuditBO audit = (AuditBO) AuditDAOImpl.getInstance().load(session, pAuditId);
            if (audit.getType().compareTo(AuditBO.MILESTONE) == 0) {
                name = audit.getName();
            }
            if (null == name) {
                DateFormat df = DateFormat.getDateInstance(DateFormat.LONG);
                name = df.format(audit.getDate());
            }
            result.put(name, values);

        } catch (Exception e) {
            FacadeHelper.convertException(e, MeasureFacade.class.getName() + ".getMeasures");
        } finally {
            FacadeHelper.closeSession(session, MeasureFacade.class.getName() + ".getMeasures");
        }
        Object[] kiviatObject = { result, new Boolean(displayCheckBoxFactors)};
        return kiviatObject;
    }

    /**
     * Affichage des facteurs du Kiviat : tous les facteurs ou seulement ceux ayant une note
     * @param pValues les donn�es � afficher : facteur + note
     * @param pNullValuesList la liste des facteurs dont la note est nulle
     * @param pAllFactors tous les facteurs (="true") ou seulement les facteurs ayant une note
     * @param pFactorsMin nombre de facteurs minimal que l'on doit afficher sur le Kiviat
     * @return values les donn�es r�ellement � afficher : facteur + note
     */
    private static SortedMap deleteFactors(SortedMap pValues, ArrayList pNullValuesList, String pAllFactors, int pFactorsMin) {
        SortedMap values = new TreeMap();
        // Seulement les facteurs ayant une note ? ==> suppression des facteurs ayant une note nulle.
        // Mais trois facteurs doivent au moins s'afficher (nuls ou pas !)    
        if (pValues.size() > pFactorsMin && !pAllFactors.equals("true")) {
            // Nombre de suppressions possible             
            int nbTotalDeletions = pValues.size() - pFactorsMin;
            // Nombre de suppressions effectu�
            int nbCurrentDeletions = 0;
            // Parcours de la liste des facteurs avec une note nulle, pour les supprimer de l'affichage
            Iterator itList = pNullValuesList.iterator();
            while (itList.hasNext() && nbCurrentDeletions < nbTotalDeletions) {
                String keyListe = (String) itList.next();
                pValues.remove(keyListe);
                nbCurrentDeletions += 1;
            }
        }
        values.putAll(pValues);
        return values;
    }

    /**
     * Creation d'une mesure de type Bubble pour un projet
     * @param pProjectId Id du projet
     * @param pAuditId  Id de l'audit
     * @throws JrafEnterpriseException  en cas de pb Hibernate
     * @return chart Scatterplot de l'Audit
     * @throws JrafEnterpriseException si pb Hibernate
     */
    public static Object[] getProjectBubble(Long pProjectId, Long pAuditId) throws JrafEnterpriseException {
        // R�cup�ration des valeurs    
        int i = 0;
        Collection measures = new ArrayList(0);
        MeasureDAOImpl measureDAO = MeasureDAOImpl.getInstance();
        // Session Hibernate
        ISession session = null;
        Object[] result = null;
        try {
            // r�cup�ration d'une session
            session = PERSISTENTPROVIDER.getSession();

            // On r�cup�re la configuration du scatterplot pour ce projet et cet audit
            AuditDisplayConfBO auditConf = AuditDisplayConfDAOImpl.getInstance().findConfigurationWhere(session, pProjectId, pAuditId, "bubble");
            if (null != auditConf) {
                BubbleConfBO bubble = (BubbleConfBO) auditConf.getDisplayConf();

                String[] tre = { bubble.getXTre(), bubble.getYTre()};
                // r�cup�ration des mesures distinctes rattach�es � l'audit et au projet
                measures = MeasureDAOImpl.getInstance().findDistinct(session, pProjectId.longValue(), pAuditId.longValue(), tre);

                // recuperation des 2 tableaux evgs, vgs contenant les mesures          
                double[] xMeasures = new double[measures.size()];
                double[] yMeasures = new double[measures.size()];
                double[] total = new double[measures.size()];
                long[] components = new long[measures.size()];
                Iterator it = measures.iterator();
                // Constantes pour la r�cup�rations des valeurs dans le tableau des r�sultats remont�s de la base
                final int VG_ID = 0;
                final int EVG_ID = 1;
                final int TOTAL_ID = 2;
                final int COMPONENT_ID = 3;
                while (it.hasNext()) {
                    Object[] res = (Object[]) it.next();
                    // on ajoute ses valeurs            
                    Integer vg = (Integer) res[VG_ID];
                    yMeasures[i] = vg.doubleValue();
                    Integer evg = (Integer) res[EVG_ID];
                    xMeasures[i] = evg.doubleValue();
                    Integer tt = (Integer) res[TOTAL_ID];
                    total[i] = tt.doubleValue();
                    Long cmp = (Long) res[COMPONENT_ID];
                    components[i] = cmp.longValue();
                    i++;
                }
                // Positions des axes
                Long horizontal = new Long(bubble.getHorizontalAxisPos());
                Long vertical = new Long(bubble.getVerticalAxisPos());
                // construction des param�tres de la s�rie
                String displayedXTre = bubble.getXTre().substring(bubble.getXTre().lastIndexOf(".") + 1);
                String displayedYTre = bubble.getXTre().substring(bubble.getXTre().lastIndexOf(".") + 1);
                String measuresKinds = "(" + displayedXTre + "," + displayedYTre + ",total)";
                result = new Object[] { horizontal, vertical, measuresKinds, yMeasures, xMeasures, total, components, yMeasures, xMeasures, total };
            }
        } catch (Exception e) {
            FacadeHelper.convertException(e, MeasureFacade.class.getName() + ".getMeasures");
        } finally {
            FacadeHelper.closeSession(session, MeasureFacade.class.getName() + ".getMeasures");
        }
        return result;
    }

    /**
     * Creation du Piechart d'une application 
     * @param pAuditId id de l'audit
     * @throws JrafEnterpriseException si pb Hibernate
     * @return les donn�es n�cessaires � la conception du PieChart via une Applet soit :
     * Map dont la cl� contient les projets et la valeur le nombre de lignes du projet
     */
    public static Object[] getApplicationPieChart(Long pAuditId) throws JrafEnterpriseException {
        // R�cup�ration des v(g) et des ev(g)        
        Collection measures;
        MeasureDAOImpl measureDAO = MeasureDAOImpl.getInstance();
        MetricDAOImpl metricDAO = MetricDAOImpl.getInstance();
        AuditDisplayConfDAOImpl auditConfDao = AuditDisplayConfDAOImpl.getInstance();
        // Session Hibernate
        ISession session = null;
        Object[] result = new Object[2];

        try {
            // r�cup�ration d'une session
            session = PERSISTENTPROVIDER.getSession();

            ApplicationBO application = (ApplicationBO) ApplicationDAOImpl.getInstance().loadByAuditId(session, pAuditId);
            // On retrouve les ids de tous les projets de l'application :
            Set projects = new HashSet();
            Iterator itChildren = application.getChildren().iterator();
            // Pour chaque enfant du projet :
            while (itChildren.hasNext()) {
                // Une application ne contient que des projets
                ProjectBO projectBO = (ProjectBO) itChildren.next();
                // On ajoute le projet seulement si celui-ci a �t� audit�
                // (cas par exemple d'une d�sactivation)
                if (projectBO.containsAuditById(pAuditId.longValue())) {
                    projects.add(projectBO);
                }
            }

            // r�cup�ration du nombre de ligne de code de chaque projet
            Map volum = new HashMap();
            Map url = new HashMap();
            // volum�trie par grille (ne sert que pour l'export PDF)
            Map volumByGrid = new HashMap();
            Iterator itProjects = projects.iterator();
            int i = 0;
            while (itProjects.hasNext()) {
                int accLines = 0;
                Long nbLignes = new Long(0);
                ProjectBO project = (ProjectBO) itProjects.next();
                Long idProject = new Long(project.getId());
                // R�cup�ration des mesures correspondant � l'audit et
                // au projet concern� � l'aide de la configuration de la volum�trie pour une application
                AuditDisplayConfBO auditConf =
                    (AuditDisplayConfBO) auditConfDao.findConfigurationWhere(session, idProject, pAuditId, DisplayConfConstants.VOLUMETRY_SUBCLASS, DisplayConfConstants.VOLUMETRY_APPLICATION_TYPE);
                if (null != auditConf) {
                    VolumetryConfBO volumConf = (VolumetryConfBO) auditConf.getDisplayConf();
                    // On ajoute les volum�tries trouv�es en fonction des tres
                    for (Iterator it = volumConf.getTres().iterator(); it.hasNext();) {
                        IntegerMetricBO metric = metricDAO.findIntegerMetricWhere(session, idProject.longValue(), pAuditId.longValue(), (String)it.next());
                        if(null != metric) {
                            accLines += ((Integer)metric.getValue()).intValue();
                        }
                    }
                    nbLignes = new Long(accLines);
                    // On ajoute la valeur au nb de ligne pour la grille du projet
                    setApplicationVolumetryByGrid(session, idProject, pAuditId, volumByGrid, nbLignes);
                } // si il n'y a pas de configuration, c'est qu'il y a un probl�me en base

                // Placement du nombre de lignes en regard du nom de projet si le nombre de lignes
                // et > 0
                if(0 < nbLignes.longValue()) {
                    volum.put(project.getName(), nbLignes);
                    url.put(project.getName(), idProject);
                }

                i++;
            }
            result = new Object[] { volum, url, volumByGrid };
        } catch (Exception e) {
            FacadeHelper.convertException(e, MeasureFacade.class.getName() + ".getMeasures");
        } finally {
            FacadeHelper.closeSession(session, MeasureFacade.class.getName() + ".getMeasures");
        }
        return result;
    }

    /**
     * Ajoute le nombre de lignes d'un projet pour une grille donn�e
     * @param session la session
     * @param idProject l'id du projet
     * @param pAuditId l'id de l'audit
     * @param volumByGrid la map du nombre de lignes par grille
     * @param nbLines le nombre de ligne � ajouter pour la grille
     * @throws JrafDaoException si erreur
     */
    private static void setApplicationVolumetryByGrid(ISession session, Long idProject, Long pAuditId, Map volumByGrid, Long nbLines) throws JrafDaoException {
        // R�cup�ration des grilles
        AuditGridDAOImpl auditGridDao = AuditGridDAOImpl.getInstance();
        // On r�cup�re la grille associ�e au projet pour cet audit
        AuditGridBO auditGrid = auditGridDao.findWhere(session, idProject, pAuditId);
        // on ajoute la valeur au nb de ligne pour la grille du projet
        Long nbLinesforGrid = (Long) volumByGrid.get(auditGrid.getGrid().getName());
        // Si on a d�j� eu un projet associ� � cette grille, on fait la somme des lignes
        if (null != nbLinesforGrid) {
            volumByGrid.put(auditGrid.getGrid().getName(), new Long(nbLinesforGrid.longValue() + nbLines.longValue()));
        } else {
            volumByGrid.put(auditGrid.getGrid().getName(), nbLines);
        }

    }

    /**
     * R�cup�re les valeurs permettant d'avoir l'historique relatif a des cles de tre et un composant
     * @param pComponent un composant
     * @param pTreLabel label de tre
     * @param pTreKey cles de tre concerne par l'historique
     * @param pAuditDate date a partir de laquelle on souhaite recuperer tous les audits
     * @param pRuleId id de la r�gle qualit� ou null s'il s'agit d'une mesure
     * @return La map correspondant aux points
     * @throws JrafEnterpriseException exception Jraf
     */
    public static Map getHistoricResults(ComponentDTO pComponent, String pTreKey, String pTreLabel, Date pAuditDate, Long pRuleId) throws JrafEnterpriseException {
        //Session Hibernate
        ISession session = null;
        // Initialisation des differentes Daos
        AuditDAOImpl auditDao = AuditDAOImpl.getInstance();
        List currentAudits = null; // liste des audits relatifs au composant
        Map values = null;
        try {
            session = PERSISTENTPROVIDER.getSession();
            // recuperation des audits relatifs a un composant tri�s par date r�elle
            currentAudits = auditDao.findAfter(session, pComponent.getID(), pAuditDate);
            // Initialisation de la Map
            values = new HashMap();
            Iterator auditIterator = currentAudits.iterator();
            AuditBO currentAuditBO = null; // AuditBO courant
            Long currentAuditId = null;
            AuditDTO currentAudit = null; // audit parcouru
            ResultsDTO currentResults = null; // ResultsDTO courant recupere
            while (auditIterator.hasNext()) {
                // recuperation de l'audit parcouru
                currentAuditBO = (AuditBO) auditIterator.next();
                currentAuditId = new Long(currentAuditBO.getId());
                // recuperation de l'application associ�e a l'audit
                ApplicationBO currentApplication = (ApplicationBO) ApplicationDAOImpl.getInstance().loadByAuditId(session, currentAuditId);
                currentAudit = AuditTransform.bo2Dto(currentAuditBO, currentApplication.getId());
                if (pRuleId == null) {
                    currentResults = MeasureFacade.getMeasures(currentAudit, pComponent, pTreKey);
                } else {
                    currentResults = QualityResultFacade.getQResults(currentAudit, pComponent, pRuleId);
                }
                // recuperation de la valeur associee du ResultsDTO
                Object objectValue = ((ArrayList) currentResults.getResultMap().get(null)).get(0);
                // On ajoute la valeur � la table (en fonction du type de l'objet)
                addHistoricValues(values, currentAudit.getRealDate(), objectValue);
            }
        } catch (JrafDaoException e) {
            FacadeHelper.convertException(e, MeasureFacade.class.getName() + ".getHistoricResults");
        } finally {
            FacadeHelper.closeSession(session, MeasureFacade.class.getName() + ".getHistoricResults");
        }
        return values;
    }

    /**
     * Ajoute une entr�e � la table des valeurs du graphe historique
     * @param values la table des valeurs
     * @param key la cl�
     * @param value la valeur
     */
    private static void addHistoricValues(Map values, Date key, Object value) {
        if (value != null) {
            // Petit test � faire sur le type de la note
            // Dans le cas ou c'est un bool�en, on met alors 1 si le bool�en
            // est � true, 0 sinon
            // Dans le cas d'une note, on met la valeur
            // Initialisation � 0 par d�faut
            Number numberToAdd = new Integer("0");
            if (value instanceof Boolean) {
                // boole�n valant "true"
                if (((Boolean) value).booleanValue()) {
                    numberToAdd = new Integer("1");
                }
                //else bool�en valant false, rien � faire car valeur par d�faut
            } else if(value.toString().matches("[0-9]+(\\.[0-9]+)?")){ // R�cup�re directement le nombre
                numberToAdd = (Number) value;
            }
            if (numberToAdd.intValue() != -1) {
                values.put(key, numberToAdd);
            }
        }
    }

    /**
     * Permet de dessiner l'historique relatif a des cles de tre et un composant
     * @param pComponent un composant
     * @param pTreLabel label de tre
     * @param pTreKey cles de tre concerne par l'historique
     * @param pAuditDate date a partir de laquelle on souhaite recuperer tous les audits
     * @param pRuleId id de la r�gle qualit� ou null s'il s'agit d'une mesure
     * @return GraphDTO
     * @throws JrafEnterpriseException exception Jraf
     */
    public static Object[] getHistoricGraph(ComponentDTO pComponent, String pTreKey, String pTreLabel, Date pAuditDate, Long pRuleId) throws JrafEnterpriseException {
        Object[] result = new Object[2];
        // Session Hibernate
        ISession session = null;
        // Initialisation des differentes Daos
        AuditDAOImpl auditDao = AuditDAOImpl.getInstance();
        List currentAudits = null; // liste des audits relatifs au composant
        Map values = MeasureFacade.getHistoricResults(pComponent, pTreKey, pTreLabel, pAuditDate, pRuleId);
        result = new Object[] { pTreLabel, values, HISTORIC_TYPE };
        return result;

    }

    /**
     * Retourne les mesures associ�es � la volum�trie d'un projet d'apr�s sa configuration en base
     * pour un audit donn� sous la forme de ResultsDTO :
     * ResultsDTO.getResultMap.get(null) --> les noms des tres
     * ResultsDTO.getResultMap.get(pProject) --> les valeurs associ�es
     * @param pAuditId l'id de l'audit
     * @param pProject le projet
     * @return l'ensemble des mesures de la volum�trie du projet
     * @throws JrafEnterpriseException si erreur
     */
    public static ResultsDTO getProjectVolumetry(Long pAuditId, ComponentDTO pProject) throws JrafEnterpriseException {
        // D�claration du ResultsDTO � retourner
        ResultsDTO results = new ResultsDTO();
        MetricDAOImpl metricDAO = MetricDAOImpl.getInstance();
        // Session Hibernate
        ISession session = null;

        try {
            // R�cup�ration d'une session
            session = PERSISTENTPROVIDER.getSession();

            // R�cup�ration de la configuration de la volum�trie pour ce projet et cet audit
            AuditDisplayConfBO auditConf =
                AuditDisplayConfDAOImpl.getInstance().findConfigurationWhere(
                    session,
                    new Long(pProject.getID()),
                    pAuditId,
                    DisplayConfConstants.VOLUMETRY_SUBCLASS,
                    DisplayConfConstants.VOLUMETRY_PROJECT_TYPE);
            if (null != auditConf) {
                VolumetryConfBO volumConf = (VolumetryConfBO) auditConf.getDisplayConf();
                // Pour chaque nom de tre, on r�cup�re la valeur de la m�trique associ�e
                // et on construit ainsi les r�sultats � retourner
                List tres = new ArrayList();
                List values = new ArrayList();
                for (Iterator it = volumConf.getTres().iterator(); it.hasNext();) {
                    String treName = (String) it.next();
                    IntegerMetricBO metric = metricDAO.findIntegerMetricWhere(session, pProject.getID(), pAuditId.longValue(), treName);
                    if (null != metric) {
                        // On ajoute � la liste des r�sultats
                        tres.add(treName);
                        values.add(metric.getValue());
                    }
                }
                results.getResultMap().put(null, tres);
                results.getResultMap().put(pProject, values);
            } // Sinon il y a un probl�me en base
        } catch (JrafDaoException e) {
            FacadeHelper.convertException(e, MeasureFacade.class.getName() + ".getMeasuresByTRE");
        } finally {
            FacadeHelper.closeSession(session, MeasureFacade.class.getName() + ".getMeasuresByTRE");
        }
        return results;
    }
}
