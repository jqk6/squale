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
 * Cr�� le 13 sept. 05
 *
 * Pour changer le mod�le de ce fichier g�n�r�, allez � :
 * Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 */
package org.squale.squaleweb.applicationlayer.formbean.results;

import java.util.List;

import org.squale.squaleweb.applicationlayer.formbean.RootForm;

/**
 * @author M400841 Pour changer le mod�le de ce commentaire de type g�n�r�, allez � :
 *         Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 */
public class TopListForm
    extends RootForm
{

    /**
     * List de ComponentForm coorespondant au top
     */
    private List mComponentListForm = null;

    /**
     * D�finit si on recharge la liste des m�triques
     */
    private String mReload = "";

    /**
     * Type du composant
     */
    private String mComponentType = null;

    /**
     * Tre s�lectionn� pour les tops
     */
    private String mTre = null;

    /**
     * @return Type du composant
     */
    public String getComponentType()
    {
        return mComponentType;
    }

    /**
     * @return List de ComponentForm correspondat au top
     */
    public List getComponentListForm()
    {
        return mComponentListForm;
    }

    /**
     * @return tre s�lectionn� pour les tops
     */
    public String getTre()
    {
        return mTre;
    }

    /**
     * @param pComponentType type du composant
     */
    public void setComponentType( String pComponentType )
    {
        mComponentType = pComponentType;
    }

    /**
     * @param pComponentListForm List de ComponentForm correspodant au top
     */
    public void setComponentListForm( List pComponentListForm )
    {
        mComponentListForm = pComponentListForm;
    }

    /**
     * @param pTre tre selectionn�
     */
    public void setTre( String pTre )
    {
        mTre = pTre;
    }

    /**
     * @return reload pour savoir si on recharge la page ou on affiche les r�sultats
     */
    public String getReload()
    {
        return mReload;
    }

    /**
     * @param pReload permet de savoir si on recharge la page ou on affiche les r�sultats
     */
    public void setReload( String pReload )
    {
        mReload = pReload;
    }

}
