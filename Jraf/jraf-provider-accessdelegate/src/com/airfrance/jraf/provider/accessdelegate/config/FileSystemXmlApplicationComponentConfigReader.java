/*
 * Created on Dec 28, 2004
 */
package com.airfrance.jraf.provider.accessdelegate.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.airfrance.jraf.commons.exception.JrafConfigException;

/**
 * <p>Title : FileSystemXmlApplicationComponentConfigReader.java</p>
 * <p>Description : Lecteur de fichier de configuration XML a partir du systeme de fichier.</p>
 * <p>Copyright : Copyright (c) 2004</p>
 * <p>Company : AIRFRANCE</p>
 */
public class FileSystemXmlApplicationComponentConfigReader
	extends AbstractApplicationComponentConfigReader {

	/** logger */
	private static final Log log =
		LogFactory.getLog(FileSystemXmlApplicationComponentConfigReader.class);

	/**
	 * Constructeur IOC type 2
	 */
	public FileSystemXmlApplicationComponentConfigReader() {
	}

	/**
	 * Constructeur IOC type 3
	 */
	public FileSystemXmlApplicationComponentConfigReader(String fileName) {
		setConfigFileName(fileName);
	}

	/* (non-Javadoc)
	 * @see com.airfrance.jraf.provider.accessdelegate.config.AbstractApplicationComponentConfigReader#readConfig()
	 */
	/**
	 * Lis le fichier de configuration d'un plugin
	 * @param in_fileName chemin du fichier de configuration 
	 */
	public Map readConfig() throws JrafConfigException {

		InputStream lc_stream = null;
		if (getConfigFileName() == null) {
			String message =
				"Le chemin du fichier de configuration n'est pas renseigne.";
			log.fatal(message);
			throw new JrafConfigException(message);
		}

		File lc_file = new File(getConfigFileName());
		if (lc_file == null || !lc_file.exists()) {
			String message =
				"Le chemin du fichier de configuration n'est pas valide.";
			log.fatal(message);
			throw new JrafConfigException(message);

		}

		try {
			lc_stream = new FileInputStream(lc_file);
			return super.readConfig(lc_stream);
		} catch (FileNotFoundException e) {
			try {
				log.fatal(
					"Le fichier '"
						+ getConfigFileName()
						+ "' est introuvable a l'emplacement '"
						+ new File(".").toURL().toExternalForm()
						+ "'",
					e);

				throw new JrafConfigException(
					"Le fichier '"
						+ getConfigFileName()
						+ "' est introuvable a l'emplacement '"
						+ new File(".").toURL().toExternalForm()
						+ "'",
					e);
			} catch (MalformedURLException urlException) {
				throw new JrafConfigException(urlException.getMessage());
			}
		}

	}

}
