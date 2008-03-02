/*
 * Cr�� le 5 ao�t 05
 *
 * Pour changer le mod�le de ce fichier g�n�r�, allez � :
 * Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 */
package com.airfrance.squalecommon.enterpriselayer.businessobject.rule;

import org.apache.commons.lang.exception.NestableException;

/**
 * @author M400843
 */
public class ComputeException
    extends NestableException
{
    /**
     * Construit une ComputeException avec le message sp�cifi�
     * 
     * @param pMessage Le message
     */
    public ComputeException( String pMessage )
    {
        super( pMessage );
    }

    /**
     * Construit une ComputeException avec la cause sp�cifi�e
     * 
     * @param pException La cause de l'exception
     */
    public ComputeException( Throwable pException )
    {
        super( pException );
    }
}
