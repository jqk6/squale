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
package com.airfrance.welcom.struts.bean;

import java.lang.reflect.InvocationTargetException;
import java.sql.CallableStatement;
import java.sql.SQLException;

import org.apache.commons.beanutils.PropertyUtils;

/**
 * Cette super-classe contient les m�thode de cryptage/d�cryptage des attributs d'un bean. Elle doit �tre h�rit�e par
 * les bean qui ont des attributs crypt�s. Les sous-classes doivent d�finir un tableau cryptableAttr suivant: Date de
 * cr�ation : (07/10/2002 15:56:50)
 * 
 * @author: Administrator
 */
public class JCryptable
    extends JActionForm
{
    /**
     * 
     */
    private static final long serialVersionUID = 8188744622028503758L;

    /** le cryptableAttr array */
    private String cryptableAttr[] = null;

    /**
     * Crypte les attributs crypt�s d'un Bean Date de cr�ation : (07/10/2002 15:57:23)
     * 
     * @param conn la connection
     * @throws JCryptableException exception pouvant etre levee
     */
    public void crypte( final java.sql.Connection conn )
        throws JCryptableException
    {
        try
        {
            final int nbAttr = cryptableAttr.length;

            // Pour chaque attr � crypter, appelle la proc�dure stock�e de cryptage aupr�s de la BDD
            for ( int i = 0; i < nbAttr; i++ )
            {
                final String attr = cryptableAttr[i];

                final String value = (String) PropertyUtils.getProperty( this, attr );

                if ( ( value != null ) && ( value.length() > 0 ) )
                {
                    final CallableStatement cs = conn.prepareCall( "{? = call crypte_FCT(?)}" );
                    cs.registerOutParameter( 1, java.sql.Types.VARCHAR );
                    cs.setString( 2, value );

                    cs.executeUpdate();
                    PropertyUtils.setProperty( this, attr, cs.getString( 1 ) );
                    cs.close();
                }
            }
        }
        catch ( final SQLException ex )
        {
            throw new JCryptableException( ex.getMessage() );
        }
        catch ( final IllegalAccessException ex )
        {
            throw new JCryptableException( ex.getMessage() );
        }
        catch ( final InvocationTargetException ex )
        {
            throw new JCryptableException( ex.getMessage() );
        }
        catch ( final NoSuchMethodException ex )
        {
            throw new JCryptableException( ex.getMessage() );
        }
    }

    /**
     * Decrypte les attributs crypt�s d'un Bean Date de cr�ation : (07/10/2002 15:57:13)
     * 
     * @param conn la connection
     * @throws JCryptableException exception pouvant etre levee
     */
    public void decrypte( final java.sql.Connection conn )
        throws JCryptableException
    {
        try
        {
            if ( cryptableAttr != null )
            {
                final int nbAttr = cryptableAttr.length;

                // Pour chaque attr crypt�, appelle la proc�dure stock�e de d�cryptage aupr�s de la BDD
                for ( int i = 0; i < nbAttr; i++ )
                {
                    final String attr = cryptableAttr[i];

                    final String value = (String) PropertyUtils.getProperty( this, attr );

                    if ( ( value != null ) && ( value.length() > 0 ) )
                    {
                        final CallableStatement cs = conn.prepareCall( "{? = call decrypte_FCT(?)}" );
                        cs.registerOutParameter( 1, java.sql.Types.VARCHAR );
                        cs.setString( 2, value );

                        cs.executeUpdate();
                        PropertyUtils.setProperty( this, attr, cs.getString( 1 ) );
                        cs.close();
                    }
                }
            }
        }
        catch ( final SQLException ex )
        {
            throw new JCryptableException( ex.getMessage() );
        }
        catch ( final IllegalAccessException ex )
        {
            throw new JCryptableException( ex.getMessage() );
        }
        catch ( final InvocationTargetException ex )
        {
            throw new JCryptableException( ex.getMessage() );
        }
        catch ( final NoSuchMethodException ex )
        {
            throw new JCryptableException( ex.getMessage() );
        }
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (07/10/2002 16:56:21)
     * 
     * @return java.lang.String[]
     */
    public java.lang.String[] getCryptableAttr()
    {
        return cryptableAttr;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (07/10/2002 16:56:21)
     * 
     * @param newCryptableAttr java.lang.String[]
     */
    public void setCryptableAttr( final java.lang.String newCryptableAttr[] )
    {
        cryptableAttr = newCryptableAttr;
    }
}