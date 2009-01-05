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
 * Cr�� le 17 f�vr. 06
 *Description : 
 *Cette classe est utilis�e dans le cadre des tests en mode 
 *Standalone. 
 */
package com.airfrance.jraf.bootstrap.test;

import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServlet;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;

import com.airfrance.jraf.bootstrap.ApplicationContextFactoryInitializer;
import com.airfrance.jraf.bootstrap.initializer.InitializableHelper;
import com.airfrance.jraf.bootstrap.initializer.Initializer;
import com.airfrance.jraf.commons.exception.JrafConfigException;
import com.airfrance.jraf.commons.exception.JrafFilePropertiesReadException;
import com.airfrance.jraf.commons.exception.JrafSpringApplicationContextInitException;
import com.airfrance.jraf.spi.bootstrap.IBootstrapConstants;
import com.airfrance.jraf.spi.bootstrap.IBootstrapProvider;
import com.airfrance.jraf.spi.initializer.IInitializable;

/**
 * @author 6391988
 *
 * Pour changer le mod�le de ce commentaire de type g�n�r�, allez � :
 * Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 */
public class ApplicationContextFactoryHelper  {
	public static ApplicationContextFactoryHelper instance = 
	new ApplicationContextFactoryHelper();
	/**Log. */
	private final static Log log =
		LogFactory.getLog(ApplicationContextFactoryHelper.class);
	private static ApplicationContext app_context = null;
	private static String root_path = null;
	private static String app_context_file = null;
	private final static String root_path_load = "ROOT_PATH";
	private final static String app_context_load = "APP_CONTEXT";
	
	
	public static ApplicationContextFactoryHelper getInstance()
	{
		return instance;
	}
	
	/** initailise properties. */
	private static boolean initProperties = false;
	static {
		try {
			initialize();
		/*	if(!getApp_context_file().equals(null))
			{ // Initialisation du fichier de configuration de
			  // spring. 
				ApplicationContextFactoryInitializer.init(
				getApp_context_file());
			}*/
		}catch(Exception e)
		{
			log.error("Probl�me d'initialisation du fichier de propri�t�s : "+ e);
		}
	}
	
	/**
	 * Initialisation des propri�t�s.
	 * @throws Exception
	 */
	private static void initialize() throws Exception {
		 // Initialisation
		 initProperties();    
	}
	
	/**
	 * Initialise les propri�t�s de Jraf.
	 * @param forceInit
	 * @throws Exception
	 */
	private static synchronized void initProperties(boolean forceInit)
		throws Exception {
		log.debug("Recup�ration de fichier de configuration ... ");
		//
		if (!forceInit && initProperties) {
			return;
		}
		// La fichier de propri�t�s de jraf.
		String propertiesFile = "jraf";
		ResourceBundle rb = null;
		String message;
		String param = null;
		//log.debug("Lecture du fichier de propri�t�s de jraf ...");
		try {
			rb = ResourceBundle.getBundle(propertiesFile);
		} catch (Exception e) {
			// Throws InternalException
			message = propertiesFile + ".properties resource introuvable ";
			throw new JrafFilePropertiesReadException(message);
		}
		// Properties initialisation
		try {
			param = root_path_load;
			root_path = rb.getString(param);
			param = app_context_load;
			app_context_file = rb.getString(param);
			// On v�rifie si le fichier les param�tres sont
			// bien charg�s.
			if(null == root_path || "".equals(root_path)|| 
				null == app_context_file || "".equals(app_context_file))
			{
				throw new JrafFilePropertiesReadException("Un ou plusieurs param�tres du fichier de propri�t�s  = null");
			}
			else		
			log.info("Recup�ration des param�tres effectu�e. ");

		} catch (Exception e) {
			// Throws InternalException
			message =
				"Le fichier de propri�t�s "
					+ propertiesFile
					+ ".properties est mal format�, veuillez v�rifier le param�tre ."
					+ param
					+ ") : "
					+ e.getMessage();
			throw new JrafFilePropertiesReadException(message);
		}
		initProperties = true;
	}
	
	/**
	 * Indicateur d'initialisation.
	 * @throws Exception
	 */
	private static void initProperties()
		throws Exception {
		initProperties(false);
	}
	
	/**
	 * Retourne le bean instanci�.
	 * @param beanName
	 * @return Object.
	 */
	public static ApplicationContext getApplicationContext(String fileName)
	{
		ApplicationContextFactoryInitializer.init(fileName);
		return ApplicationContextFactoryInitializer.getApplicationContext();
	}

	/**
	 * Renvoie le context.
	 * @return app_context
	 */
	public static ApplicationContext getApp_context() {
		return app_context;
	}
	
	/**
	 * Le fichier de configuration xml de spring.
	 * @return app_context_file
	 */
	public static String getApp_context_file() {
		return app_context_file;
	}

	/**
	 * Le chemin d'installation de l'application.
	 * @return root_path
	 */
	public static String getRoot_path() {
		return root_path;
	}
	
	/**
	 * 
	 * @param argc0
	 */
	public static void main(String[] argc0)
	{	
		ApplicationContext ctx = ApplicationContextFactoryHelper.getApplicationContext("applicationTest.xml");		
		log.debug(""+ctx.getBeanDefinitionCount());
	}
}
