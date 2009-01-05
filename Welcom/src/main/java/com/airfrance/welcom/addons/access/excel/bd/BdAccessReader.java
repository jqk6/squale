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
/*
 * Cr�� le 25 juil. 05
 *
 * Pour changer le mod�le de ce fichier g�n�r�, allez � :
 * Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 */
package com.airfrance.welcom.addons.access.excel.bd;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.airfrance.welcom.addons.access.excel.object.AccessKey;
import com.airfrance.welcom.addons.access.excel.object.Profile;
import com.airfrance.welcom.addons.access.excel.object.ProfileAccessKey;
import com.airfrance.welcom.addons.config.AddonsConfig;
import com.airfrance.welcom.outils.jdbc.WJdbc;
import com.airfrance.welcom.outils.jdbc.WResultSetUtils;
import com.airfrance.welcom.outils.jdbc.WStatement;

/**
 * @author M327837 Pour changer le mod�le de ce commentaire de type g�n�r�, allez � :
 *         Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 */
public class BdAccessReader
{

    /** Cache */
    private ArrayList cacheAccessKeyList = null;

    /** Cache */
    private ArrayList cacheProfileAccessKeyList = null;

    /** Cache */
    private ArrayList cacheProfileList = null;

    /**
     * @param resetCache : Recherche les elements
     * @param jdbc : connection JDBC
     * @return Retourne la liste des profiles / accessKey
     * @throws SQLException erreur sql
     */
    public ArrayList getProfileAccessKey( final WJdbc jdbc, final boolean resetCache )
        throws SQLException
    {
        if ( resetCache || ( cacheProfileAccessKeyList == null ) )
        {
            readProfileAccessKey( jdbc );
        }
        return cacheProfileAccessKeyList;
    }

    /**
     * @param resetCache : Recherche les elements
     * @param jdbc : connection JDBC
     * @return Retourne la liste des profiles
     * @throws SQLException : Erreur SQL
     */
    public ArrayList getProfile( final WJdbc jdbc, final boolean resetCache )
        throws SQLException
    {
        if ( resetCache || ( cacheProfileList == null ) )
        {
            readProfile( jdbc );
        }
        return cacheProfileList;
    }

    /**
     * @param resetCache : Recherche les elements
     * @param jdbc : connection JDBC
     * @return Retourne la liste des droits diponibles
     * @throws SQLException erreur sql
     */
    public ArrayList getAccessKey( final WJdbc jdbc, final boolean resetCache )
        throws SQLException
    {
        if ( resetCache || ( cacheAccessKeyList == null ) )
        {
            readAccessKey( jdbc );
        }
        return cacheAccessKeyList;
    }

    /**
     * Lecture des droits de la base / profil
     * 
     * @param jdbc : connection JDBC
     * @throws SQLException erreur sql
     */
    public void readProfileAccessKey( final WJdbc jdbc )
        throws SQLException
    {

        final WStatement sta = jdbc.getWStatement();
        sta.add( "select * from " + AddonsConfig.WEL_PROFILE_ACCESSKEY_INT );
        final ResultSet rs = sta.executeQuery();
        cacheProfileAccessKeyList = WResultSetUtils.populateInArrayList( ProfileAccessKey.class, rs );
        sta.close();

    }

    /**
     * Lecture des droits de la base
     * 
     * @param jdbc : connection JDBC
     * @throws SQLException erreur sql
     */
    public void readProfile( final WJdbc jdbc )
        throws SQLException
    {

        final WStatement sta = jdbc.getWStatement();
        sta.add( "select * from " + AddonsConfig.WEL_PROFILE );
        final ResultSet rs = sta.executeQuery();
        cacheProfileList = WResultSetUtils.populateInArrayList( Profile.class, rs );
        sta.close();

    }

    /**
     * Lecture des droits de la base
     * 
     * @param jdbc : connection JDBC
     * @throws SQLException erreur sql
     */
    public void readAccessKey( final WJdbc jdbc )
        throws SQLException
    {

        final WStatement sta = jdbc.getWStatement();
        sta.add( "select * from " + AddonsConfig.WEL_ACCESSKEY );
        final ResultSet rs = sta.executeQuery();
        cacheAccessKeyList = WResultSetUtils.populateInArrayList( AccessKey.class, rs );
        sta.close();

    }

}
