package com.airfrance.jraf.provider.logging;

import java.util.Hashtable;
import java.util.Map;

import org.apache.commons.logging.LogFactory;

import com.airfrance.jraf.spi.logging.ILogger;
import com.airfrance.jraf.spi.logging.ILoggingProvider;
import com.airfrance.jraf.spi.provider.IProvider;

/**
 * <p>Project: JRAF 
 * <p>Module: jrafProviderLogging
 * <p>Title : LoggingProviderImpl.java</p>
 * <p>Description : </p>
 * <p>Copyright : Copyright (c) 2004</p>
 * <p>Company : AIRFRANCE </p>
 *  M�canisme de trace en attendant la version am�lior�e du MessageManager
 *  Cette version est impl�ment�e sous forme de singleton. Elle utilise le commons-logging qui propose
 *  une sur couche ind�pendante du module de trace utilis�. Si le jar de log4j est
 *  pr�sent, il est utilis�. 
 *  Cette version, minimise les probl�mes de performances li�es aux traces en associant
 *  un seul logger � chaque classe (g�r� par HashTable). Le fichier de configuration log4j.properties est fourni
 *  par JRAF.
 */
public class LoggingProviderImpl implements ILoggingProvider {

	/**
	 * Constructeur vide (IOC type 2).
	 */
	public LoggingProviderImpl() {
	}

	/**
	 * Creation d'un logger
	 * @param clazz classe a logger
	 */
	public ILogger getInstance(Class clazz) {
		return new LoggerImpl(LogFactory.getLog(clazz));
	}

	/**
	 * Creation d'un logger
	 * @param clazz classe a logger
	 */
	public ILogger getInstance(String clazz) {
		return new LoggerImpl(LogFactory.getLog(clazz));
	}

}
