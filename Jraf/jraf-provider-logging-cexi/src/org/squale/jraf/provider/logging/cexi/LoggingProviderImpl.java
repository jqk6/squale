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
package org.squale.jraf.provider.logging.cexi;

import org.apache.commons.logging.LogFactory;

import org.squale.jraf.spi.logging.ILogger;
import org.squale.jraf.spi.logging.ILoggingProvider;
import org.squale.jraf.spi.provider.IProvider;

/**
 * <p>Project: JRAF 
 * <p>Module: jrafProviderLogging
 * <p>Title : LoggingProviderImpl.java</p>
 * <p>Description : </p>
 * <p>Copyright : Copyright (c) 2004</p>
 *  
 *  M�canisme de trace en attendant la version am�lior�e du MessageManager
 *  Cette version est impl�ment�e sous forme de singleton. Elle utilise le commons-logging qui propose
 *  une sur couche ind�pendante du module de trace utilis�. Si le jar de log4j est
 *  pr�sent, il est utilis�. 
 *  Cette version, minimise les probl�mes de performances li�es aux traces en associant
 *  un seul logger � chaque classe (g�r� par HashTable). Le fichier de configuration log4j.properties est fourni
 *  par JRAF.
 */
public class LoggingProviderImpl implements ILoggingProvider, IProvider {

	/**
	 * Constructeur vide IOC type 2
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
