package com.airfrance.squalix.tools.cpd;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.sourceforge.pmd.cpd.CPD;
import net.sourceforge.pmd.cpd.Language;

import com.airfrance.squalecommon.enterpriselayer.businessobject.component.parameters.ListParameterBO;
import com.airfrance.squalecommon.enterpriselayer.businessobject.component.parameters.MapParameterBO;
import com.airfrance.squalecommon.enterpriselayer.businessobject.component.parameters.ParametersConstants;
import com.airfrance.squalix.core.TaskData;
import com.airfrance.squalix.core.exception.ConfigurationException;
import com.airfrance.squalix.util.buildpath.BuildProjectPath;
import com.airfrance.squalix.util.file.FileUtility;

/**
 * Traitement Cpd
 * Le traitement Cpd est d�pendant du type de langage, chaque sous classe
 * red�finit le comportement de la d�tection
 */
public abstract class AbstractCpdProcessing {
    /**
     * Logger
     */
    private static final Log LOGGER = LogFactory.getLog(AbstractCpdProcessing.class);

    /**
     * D�tection des copier-coller
     * @param pData donn�es de t�che
     * @param pProjectParams param�tres de projet
     * @return d�tections de copier/coller
     * @throws ConfigurationException si erreur
     * @throws IOException si erreur
     */
    public Iterator process(TaskData pData, MapParameterBO pProjectParams) throws ConfigurationException, IOException {
        CPD cpd = new CPD(getTokenThreshold(), getLanguage());
        // On ajoute les noms de r�pertoire � traiter
        buildFilesToProcess(cpd, pData, pProjectParams);
        cpd.go();
        return cpd.getMatches();
    }

    /**
     * Obtention du langage
     * @return langage
     */
    protected abstract Language getLanguage();

    /**
     * Obtention du nombre minimal de l�x�mes identiques
     * @return nb minimal de l�x�mes identiques
     */
    protected abstract int getTokenThreshold();

    /**
     * Obtention des sources
     * @param pProjectParams param�tres du projet
     * @return sources sous la forme de ListParameterBO(StringParameterBO)
     */
    protected ListParameterBO getSourcesDirs(MapParameterBO pProjectParams) {
        return (ListParameterBO) pProjectParams.getParameters().get(ParametersConstants.SOURCES);
    }
    
    /**
     * Obtention des r�pertoires exclus de la compilation
     * @param pProjectParams param�tres du projet
     * @return r�pertories exclus sous la forme de ListParameterBO(StringParameterBO)
     */
    protected ListParameterBO getExcludedDirs(MapParameterBO pProjectParams) {
        return (ListParameterBO) pProjectParams.getParameters().get(ParametersConstants.EXCLUDED_DIRS);
    }

    /**
     * Obtention de l'extension
     * @return extension � traiter
     */
    protected abstract String[] getExtensions();

    /**
     * Construction des r�pertoires � analyser
     * @param pCPD copy/paste detector
     * @param pData donn�es de la t�che
     * @param pProjectParams param�tres du projet
     * @throws ConfigurationException si erreur
     * @throws IOException si erreur
     */
    protected void buildFilesToProcess(CPD pCPD, TaskData pData, MapParameterBO pProjectParams) throws ConfigurationException, IOException {
        // On prend le view path
        String viewPath = (String) pData.getData(TaskData.VIEW_PATH);
        if (viewPath == null) {
            String message = CpdMessages.getString("exception.variable.not_found", TaskData.VIEW_PATH);
            LOGGER.error(message);
            // Renvoi d'une exception de configuration
            throw new ConfigurationException(message);
        }
        // Pour chaque r�pertoire source on ajoute celui-ci
        // On r�cup�re les chemins relatifs des r�pertoires contenant les .java du projet
        ListParameterBO sources = getSourcesDirs(pProjectParams);
        if (sources == null) {
            String message = CpdMessages.getString("exception.sources.notset", getLanguage());
            LOGGER.error(message);
            // Renvoi d'une exception de configuration
            throw new ConfigurationException(message);
        }
        // Prise en compte des patterns d'exclusion et d'inclusion
        ListParameterBO included = (ListParameterBO) pProjectParams.getParameters().get(ParametersConstants.INCLUDED_PATTERNS);
        ListParameterBO excluded = (ListParameterBO) pProjectParams.getParameters().get(ParametersConstants.EXCLUDED_PATTERNS);
        List srcs = BuildProjectPath.buildProjectPath(viewPath, sources.getParameters());
        List includedFileNames = FileUtility.getIncludedFiles(viewPath, srcs, included, excluded, getExcludedDirs(pProjectParams), getExtensions());

        // Conversion en fichiers
        // On ne tient pas compte de la casse
        for (Iterator it = includedFileNames.iterator(); it.hasNext();) {
            pCPD.add(new File((String) it.next()));
        }
    }
}
