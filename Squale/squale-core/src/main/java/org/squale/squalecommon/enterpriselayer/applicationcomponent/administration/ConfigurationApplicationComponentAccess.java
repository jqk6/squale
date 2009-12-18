/**
 * Copyright (C) 2008-2010, Squale Project - http://www.squale.org
 *
 * This file is part of Squale.
 *
 * Squale is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or any later version.
 *
 * Squale is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Squale.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.squale.squalecommon.enterpriselayer.applicationcomponent.administration;

import java.io.InputStream;
import java.util.Collection;
import java.util.List;

import org.squale.jraf.commons.exception.JrafEnterpriseException;
import org.squale.jraf.provider.accessdelegate.DefaultExecuteComponent;
import org.squale.squalecommon.datatransfertobject.config.AdminParamsDTO;
import org.squale.squalecommon.datatransfertobject.config.SqualixConfigurationDTO;
import org.squale.squalecommon.enterpriselayer.businessobject.config.AdminParamsBO;
import org.squale.squalecommon.enterpriselayer.facade.config.ConfigurationImport;
import org.squale.squalecommon.enterpriselayer.facade.config.SqualixConfigFacade;

/**
 * Manipulation de la configuration Squalix
 */
public class ConfigurationApplicationComponentAccess
    extends DefaultExecuteComponent
{

    /**
     * Constructeur par d�faut
     */
    public ConfigurationApplicationComponentAccess()
    {
    }

    /**
     * Obtention de la configuration
     * 
     * @return la configuration Squalix actuelle
     * @throws JrafEnterpriseException si une erreur survient
     */
    public SqualixConfigurationDTO getConfiguration()
        throws JrafEnterpriseException
    {
        return SqualixConfigFacade.getConfig();
    }

    /**
     * Obtention des r�cup�rateurs de sources
     * 
     * @return les r�cup�rateurs de sources actuels
     * @throws JrafEnterpriseException si une erreur survient
     */
    public Collection getSourceManagements()
        throws JrafEnterpriseException
    {
        return SqualixConfigFacade.getSourceManagements();
    }

    /**
     * Obtention des profiles squalix
     * 
     * @return les profiles Squalix actuels
     * @throws JrafEnterpriseException si une erreur survient
     */
    public Collection getProfiles()
        throws JrafEnterpriseException
    {
        return SqualixConfigFacade.getProfiles();
    }

    /**
     * Cr�ation de la configuration Squalix
     * 
     * @param pStream flux associ�
     * @param pErrors erreur rencontr�es
     * @return la configuration cr�ee
     * @throws JrafEnterpriseException si erreur
     */
    public SqualixConfigurationDTO createConfig( InputStream pStream, StringBuffer pErrors )
        throws JrafEnterpriseException
    {
        return ConfigurationImport.createConfig( pStream, pErrors );
    }

    /**
     * Importation de la configuration Squalix
     * 
     * @param pStream flux associ�
     * @param pErrors erreur rencontr�es
     * @return la configuration import�e
     * @throws JrafEnterpriseException si erreur
     */
    public SqualixConfigurationDTO importConfig( InputStream pStream, StringBuffer pErrors )
        throws JrafEnterpriseException
    {
        return ConfigurationImport.importConfig( pStream, pErrors );
    }

    /**
     * Obtention des t�ches configurables d'un profile ou d'un source manager
     * 
     * @param pProfileName le nom du profile
     * @param pManagerName le nom du source manager
     * @return les t�ches configurables
     * @throws JrafEnterpriseException si erreur
     */
    public Collection getConfigurableTasks( String pManagerName, String pProfileName )
        throws JrafEnterpriseException
    {
        return SqualixConfigFacade.getConfigurableTasks( pManagerName, pProfileName );
    }

    /**
     * This method search the admin-params linked to the export server
     * 
     * @return The export server found
     * @throws JrafEnterpriseException If an error occurs during the search
     */
    public String getSharedRepositoryExportServer()
        throws JrafEnterpriseException
    {
        String adminParam = null;
        List<AdminParamsDTO> adminParamsList;
        adminParamsList = SqualixConfigFacade.getAdminParamsByKey( AdminParamsBO.SQUALIX_SERVER_NAME );
        if ( adminParamsList != null && adminParamsList.size() == 1 )
        {
            adminParam = adminParamsList.get( 0 ).getParamValue();
        }
        return adminParam;
    }
}
