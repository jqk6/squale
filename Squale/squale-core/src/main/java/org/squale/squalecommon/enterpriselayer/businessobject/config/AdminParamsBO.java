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
package org.squale.squalecommon.enterpriselayer.businessobject.config;

/**
 * Bo for the admin-params. These admin-params are tag from the squlix-config.xml file. These admin-params are
 * configuration parameter for Squale.
 * 
 * @hibernate.class table="adminParams" lazy="true"
 */
public class AdminParamsBO
{

    /*
     * !!! BE CAREFUL !!! If you want that the admin Params will be removed before being recreate when a new
     * squale-config.xml file is upload, then its key should start with : configuration/admin-params
     */

    /**
     * Default name
     */
    public static final String ADMIN_PARAMS = "configuration/admin-params";

    /**
     * Need at least two values to compute criterias or factors : true or false
     */
    public static final String TWO_TO_COMPUTE = "configuration/admin-params/two-to-compute";

    /**
     * Default mail paramKey
     */
    public static final String MAIL = "configuration/admin-params/mail/";

    /**
     * Smtp server paramKey
     */
    public static final String MAIL_SMTP_SERVER = "configuration/admin-params/mail/smtp-server";

    /**
     * Sender address paramKey
     */
    public static final String MAIL_SENDER_ADDRESS = "configuration/admin-params/mail/sender-address";

    /**
     * Admin mailing list paramKey
     */
    public static final String MAIL_ADMIN_MAILING_LIST = "configuration/admin-params/mail/admin-mailing-list";

    /**
     * Admin mailing list paramKey
     */
    public static final String MAIL_SMTP_USERNAME = "configuration/admin-params/mail/smtp-username";

    /**
     * Admin mailing list paramKey
     */
    public static final String MAIL_SMTP_PASSWORD = "configuration/admin-params/mail/smtp-password";

    /**
     * Admin mailing list paramKey
     */
    public static final String MAIL_SMTP_AUTHENT_NEEDED = "configuration/admin-params/mail/smtp-authent-needed";

    /**
     * Mapping
     */
    public static final String MAPPING = "configuration/admin-params/shared-repository-export/mapping";

    /**
     * Key for mapping : language java, level method, metric cyclomatic complexity
     */
    public static final String SQUALIX_SERVER_NAME =
        "configuration/admin-params/shared-repository-export/squalix-server/name";

    /**
     * Entity_Id This attribute is not removed when a new squale-config.xml file is upload
     */
    public static final String ENTITY_ID = "entityId";

    /**
     * Object ID
     */
    private long id;

    /**
     * The parameter key
     */
    private String paramKey;

    /**
     * The parameter value
     */
    private String paramValue;

    /**
     * Constructor
     */
    public AdminParamsBO()
    {
        id = -1;
        paramKey = "";
        paramValue = "";
    }

    /**
     * Getter method for the paramKey variable.
     * 
     * @return the key of the parameter
     * @hibernate.property name="paramKey" column="paramKey" type="string" not-null="true" update="true" insert="true"
     */
    public String getParamKey()
    {
        return paramKey;
    }

    /**
     * Setter method for the paramKey variable
     * 
     * @param pParamKey The new parameter key value
     */
    public void setParamKey( String pParamKey )
    {
        paramKey = pParamKey;
    }

    /**
     * Getter method for the paramValue variable
     * 
     * @return the value of the parameter
     * @hibernate.property name="paramValue" column="paramaValue" type="string" not-null="true" update="true"
     *                     insert="true"
     */

    public String getParamValue()
    {
        return paramValue;
    }

    /**
     * Setter method for the paramValue variable
     * 
     * @param pParamValue The new value of the parameter
     */
    public void setParamValue( String pParamValue )
    {
        paramValue = pParamValue;
    }

    /**
     * Getter method for the id of the object
     * 
     * @return the ID of the object
     * @hibernate.id generator-class="native" type="long" column="AdminParamsId" unsaved-value="-1" length="19"
     * @hibernate.generator-param name="sequence" value="adminparams_sequence"
     */
    public long getId()
    {
        return id;
    }

    /**
     * Setter method for the id of the object
     * 
     * @param pId the new id of the object
     */
    public void setId( long pId )
    {
        id = pId;
    }

    /**
     * This method set new values for paramKey and paramVlaue
     * 
     * @param key The new value of paramKey
     * @param value The new value of paramValue
     */
    public void setAdminParam( String key, String value )
    {
        paramKey = key;
        paramValue = value;
    }
}
