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
 * Created on Dec 27, 2004
 */
package org.squale.jraf.commons.util;

/**
 * <p>Title : ParamUtils.java</p>
 * <p>Description : Permet la cr�ation de tableaux de parametres</p>
 * <p>Copyright : Copyright (c) 2004</p>
 * 
 */
public class ParamUtils {

	/**
	 * Cree un tableau d'objet avec 1 element
	 * @param o1
	 * @return
	 */
	public final static Object[] buildParameters(Object o1) {

		Object[] params = new Object[1];
		params[0] = o1;
		return params;
	}

	/**
	 * Cree un tableau d'objet avec 2 elements
	 * @param o1
	 * @param o2
	 * @return
	 */
	public final static Object[] buildParameters(Object o1, Object o2) {

		Object[] params = new Object[2];
		params[0] = o1;
		params[1] = o2;
		return params;
	}

	/**
	 * Cree un tableau d'objet avec 3 elements
	 * @param o1
	 * @param o2
	 * @param o3
	 * @return
	 */
	public final static Object[] buildParameters(
		Object o1,
		Object o2,
		Object o3) {

		Object[] params = new Object[3];
		params[0] = o1;
		params[1] = o2;
		params[2] = o3;
		return params;
	}

	/**
	 * Cree un tableau d'objet avec 4 elements
	 * @param o1
	 * @param o2
	 * @param o3
	 * @param o4
	 * @return
	 */
	public final static Object[] buildParameters(
		Object o1,
		Object o2,
		Object o3,
		Object o4) {

		Object[] params = new Object[4];
		params[0] = o1;
		params[1] = o2;
		params[2] = o3;
		params[3] = o4;
		return params;
	}

	/**
	 * Cree un tableau d'objet avec 5 elements
	 * @param o1
	 * @param o2
	 * @param o3
	 * @param o4
	 * @param o5
	 * @return
	 */
	public final static Object[] buildParameters(
		Object o1,
		Object o2,
		Object o3,
		Object o4,
		Object o5) {

		Object[] params = new Object[5];
		params[0] = o1;
		params[1] = o2;
		params[2] = o3;
		params[3] = o4;
		params[4] = o5;
		return params;
	}

	/**
	 * Cree un tableau d'objet avec 5 elements
	 * @param o1
	 * @param o2
	 * @param o3
	 * @param o4
	 * @param o5
	 * @param o6
	 * @return
	 */
	public final static Object[] buildParameters(
		Object o1,
		Object o2,
		Object o3,
		Object o4,
		Object o5,
		Object o6) {

		Object[] params = new Object[6];
		params[0] = o1;
		params[1] = o2;
		params[2] = o3;
		params[3] = o4;
		params[4] = o5;
		params[5] = o6;
		return params;
	}

}
