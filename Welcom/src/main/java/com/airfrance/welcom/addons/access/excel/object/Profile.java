/*
 * Cr�� le 22 juil. 05
 *
 * Pour changer le mod�le de ce fichier g�n�r�, allez � :
 * Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 */
package com.airfrance.welcom.addons.access.excel.object;

import com.airfrance.welcom.outils.Util;

/**
 * @author M327837
 *
 * Pour changer le mod�le de ce commentaire de type g�n�r�, allez � :
 * Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 */
public class Profile {

    /** Id du profil */
    private String idProfile = "";

    /** Name */
    private String name = "";

    /**
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * @param string name
     */
    public void setName(final String string) {
        name = string;
    }

    /**
     * @return idProfile
     */
    public String getIdProfile() {
        return idProfile;
    }

    /**
     * @param string idProfile
     */
    public void setIdProfile(final String string) {
        idProfile = string;
    }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(final Object obj) {

        return Util.isEquals(this.getIdProfile(), ((Profile) obj).getIdProfile()) && Util.isEquals(this.getName(), ((Profile) obj).getName());
    }

    /**
     * @see java.lang.Object#toString()
     */
    public String toString() {

        return " Profile [ " + this.getIdProfile() + " - " + this.getName() + " ]";
    }
}
