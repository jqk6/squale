/*
 * Cr�� le 22 juil. 05
 *
 * Pour changer le mod�le de ce fichier g�n�r�, allez � :
 * Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 */
package com.airfrance.welcom.addons.access.excel.filereader;

import java.io.IOException;
import java.net.URL;

import jxl.Workbook;
import jxl.read.biff.BiffException;

import com.airfrance.welcom.outils.Util;

/**
 * @author M327837 Pour changer le mod�le de ce commentaire de type g�n�r�, allez � :
 *         Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 */
public class AccessKeyReaderFactory
{

    /**
     * Initialise un reader en fonction de la version du fichier Excel
     * 
     * @param url : url du fichier Excel
     * @return : Le reader
     * @throws AccessKeyReaderException Probleme sur la lecture
     */
    public static IAccessKeyReader read( final URL url )
        throws AccessKeyReaderException
    {
        try
        {

            final Workbook workbook = Workbook.getWorkbook( url.openStream() );
            final String version = workbook.findCellByName( "VERSION" ).getContents();

            if ( Util.isEquals( version, "Welcom AccesKey 1.0" ) )
            {
                return new AccessKeyReader10( workbook );
            }
            else
            {
                throw new AccessKeyReaderException( "Version ou type de fichier de droit d'acc�s non support�" );
            }

        }
        catch ( final BiffException e )
        {
            throw new AccessKeyReaderException( e.getMessage() );
        }
        catch ( final IOException e )
        {
            throw new AccessKeyReaderException( e.getMessage() );
        }

    }

}
