/*
 * Cr�� le 16 f�vr. 06
 *
 * Pour changer le mod�le de ce fichier g�n�r�, allez � :
 * Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 */
package com.airfrance.jraf.bootstrap;

import javax.servlet.ServletContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.airfrance.jraf.commons.exception.JrafIllegalStateException;

/**
 * @author DIKITE Tidiani
 * Cette classe a pour r�le d'initialiser le context spring.
 * L'initialisation du context n'est effectu�e qu'une fois, sauf 
 * dans le cadre de l'ex�cution de tests unitaires JUNIT.
 * Voir les commentaires sur des m�thodes pour plus d'informations.
 * */
public class ApplicationContextFactoryInitializer {

	/** logger */
	private static final Log log =
		LogFactory.getLog(ApplicationContextFactoryInitializer.class);
		
	/**Objet � initialiser.*/
	private static Object initObject = null;
	
	/**La limite d'init. Une seule initialisation est permise dans un contexte 
	 * autre que cleui des tests unitaires. */
	private static int limite = 0;
	
	/**
	 * Initialise l'application contexte.
	 * @param object - Object
	 */
	public static void init(Object object) {
		// Initialisation du contexte dans le cadre de tests unitaires.
		// Dans ce cas de figure, le contexte peut �tre initialis�
		// plusieurs fois.
		if(object.equals("applicationTest.xml")) {
			log.debug("Initialisation du context spring dans un cas de tests unitaires ...");
			initObject = object;
		}
		else 
		{// Cas de figure o� l'application est ex�cut�e dans un contexte autre 
			// que celui des tests unitaires.
			if (limite > 0) {
			throw new JrafIllegalStateException("Impossible d'initialiser l'Application context une seconde fois. ELle est d�j� initialis�e !");
			}
			initObject = object;
			limite++;
		}
	}

	/**
	 * Retourne l'application context selon le type d'environnement
	 * d'ex�cution.
	 * @return ApplicationCOntext.
	 */
	public static ApplicationContext getApplicationContext() {
		if (initObject == null) {
			throw new JrafIllegalStateException("Application context n'est pas initialis�e ...");
		// Test si l'instance de l'objet pass� en param�tre est 
		// un ServletContext.
		} else if (initObject instanceof ServletContext) {
			log.debug("D�but d'initialisation du fichier de configuration spring en mode servlet.");
			ServletContext servletContext = (ServletContext) initObject;
			return WebApplicationContextUtils.getRequiredWebApplicationContext(
				servletContext);
		// Test si l'instance de l'objet est une chaine de caract�re.
		} else if (initObject instanceof String) {
			log.debug("Initialisation du fichier de configuration spring : " + initObject);
			String contextResourceLocation = (String) initObject;
			ApplicationContext applicationContext = null;
			try {
				applicationContext = new ClassPathXmlApplicationContext(contextResourceLocation);
			} catch (BeansException e) {
				e.printStackTrace();
				throw e;
			}
			
			return applicationContext;
		} else {
			// Lancement de l'exception.
			throw new JrafIllegalStateException("Le contexte doit �tre initialis� avec un String ou un ServletContext .");
		}
	}
}
