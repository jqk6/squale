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
package org.squale.squaleweb.applicationlayer.action.export.xls;

import java.util.Locale;

import jxl.HeaderFooter;
import jxl.SheetSettings;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

import org.apache.struts.util.MessageResources;

import org.squale.squaleweb.applicationlayer.formbean.results.TopListForm;
import org.squale.squaleweb.resources.WebMessages;
import org.squale.welcom.outils.excel.ExcelData;
import org.squale.welcom.outils.excel.ExcelGenerateur;
import org.squale.welcom.outils.excel.ExcelGenerateurException;
import org.squale.welcom.outils.excel.ExcelTable;
import org.squale.welcom.outils.excel.ExcelWrapper;

/**
 * Gestion de l'export des tops
 */
public class ExcelDataTopList
    extends ExcelData
{

    /** le bean concern� */
    private TopListForm mBean;

    /** Le nom de l'utilisateur */
    private String mMatricule;

    /**
     * Constructeur
     * 
     * @param pLocale : c'est la locale de l'application pour l'internationnalisation
     * @param pMessages :un MessageResources initialis� avec le fichier ressource contenant les labels � utiliser dans
     *            le ficheir excel.
     * @param pBean le bean des tops
     * @param pMatricule le matricule de l'utilsateur
     */
    public ExcelDataTopList( final Locale pLocale, final MessageResources pMessages, TopListForm pBean,
                             String pMatricule )
    {
        super( pLocale, pMessages );
        mBean = pBean;
        mMatricule = pMatricule;
    }

    /**
     * @see org.squale.welcom.outils.excel.ExcelData#fill(org.squale.welcom.outils.excel.ExcelGenerateur)
     *      {@inheritDoc}
     */
    public void fill( ExcelGenerateur xlGenerateur )
        throws ExcelGenerateurException
    {
        ExcelWrapper monWrapper = (ExcelWrapper) xlGenerateur;
        WritableWorkbook workbook = monWrapper.getWorkbook();
        if ( null != mBean.getComponentListForm() )
        {
            ExcelTable et = new ExcelTable( messages, locale );
            et.setTable( mBean.getComponentListForm() );
            /* les en-t�tes du tableau */
            // le nom du composant
            et.addHeader( "component.name", "fullName" );
            // la note
            et.addHeader( mBean.getTre(), "metrics[0]" );
            try
            {
                et.writeTable( workbook );
                // On modifie l'en-t�te et pied-de-page
                WritableSheet sheet = workbook.getSheet( 0 );
                SheetSettings settings = sheet.getSettings();
                String title =
                    (String) WebMessages.getString( locale, "export.pdf.top.title",
                                                    new String[] { mBean.getProjectName() } );
                HeaderFooter head = new HeaderFooter();
                head.getCentre().append( title.replaceAll( "''", "'" ) );
                settings.setHeader( head );
                String footerLeft =
                    (String) WebMessages.getString( locale, "description.name.audit",
                                                    new String[] { mBean.getAuditDate() } );
                HeaderFooter footer =
                    SqualeExportExcelUtils.getFooter( locale, mBean.getApplicationName(), mBean.getProjectName(),
                                                      footerLeft, mMatricule );
                settings.setFooter( footer );
            }
            catch ( WriteException e )
            {
                e.printStackTrace();
            }
        }
    }

}
