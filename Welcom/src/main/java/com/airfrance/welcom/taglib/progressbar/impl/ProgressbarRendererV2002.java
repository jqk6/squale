/*
 * Cr�� le 16 oct. 07
 *
 * Pour changer le mod�le de ce fichier g�n�r�, allez � :
 * Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 */
package com.airfrance.welcom.taglib.progressbar.impl;

import com.airfrance.welcom.outils.WelcomConfigurator;
import com.airfrance.welcom.taglib.progressbar.IProgressbarRenderer;

/**
 * @author 6361371
 *
 * Pour changer le mod�le de ce commentaire de type g�n�r�, allez � :
 * Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 */
public class ProgressbarRendererV2002 extends AbstractProgressbarRenderer{


	public String drawRealProgressBar(String id)  {		
		StringBuffer result = new StringBuffer();
		result.append("<span style=\"float:left;width:100%;background-color:#FFFFFF;border:1px solid #888;height:10px\" >");
		result.append("	<span style=\"float:left;width:0%;background-color:#383D90;height:10px\" id=\"" + id +"_all_td1\"></span>");
		result.append("</span>");
		return result.toString();
	}
}
