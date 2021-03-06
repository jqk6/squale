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
package org.squale.squalecommon.enterpriselayer.businessobject.config.web;

/**
 * La configuration pour la cr�ation du scatterplot
 * 
 * @hibernate.subclass lazy="true" discriminator-value="bubble"
 */
public class BubbleConfBO
    extends AbstractDisplayConfBO
{

    /**
     * le tre des abssices au sens m�tier
     */
    private String mXTre;

    /**
     * le tre des ordonn�es au sens m�tier
     */
    private String mYTre;

    /**
     * La position de l'axe horizontal
     */
    private long mHorizontalAxisPos;

    /**
     * la position de l'axe vertical
     */
    private long mVerticalAxisPos;

    /**
     * Constructeur par d�faut
     */
    public BubbleConfBO()
    {
        this( "", "", 0, 0 );
    }

    /**
     * Constructeur
     * 
     * @param pXTre le tre des abssices au sens m�tier
     * @param pYTre le tre des ordonn�es au sens m�tier
     * @param pHpos la position de l'axe horizontal
     * @param pVpos la position de l'axe vertical
     */
    public BubbleConfBO( String pXTre, String pYTre, long pHpos, long pVpos )
    {
        mXTre = pXTre;
        mYTre = pYTre;
        mHorizontalAxisPos = pHpos;
        mVerticalAxisPos = pVpos;
    }

    /**
     * @return le tre m�tier sur l'axe des v
     * @hibernate.property name="xTre" column="X_TRE" type="string" length="400" update="true" insert="true"
     */
    public String getXTre()
    {
        return mXTre;
    }

    /**
     * @return le tre m�tier sur l'axe des y
     * @hibernate.property name="yTre" column="Y_TRE" type="string" length="400" update="true" insert="true"
     */
    public String getYTre()
    {
        return mYTre;
    }

    /**
     * @param pXtre la nouvelle valeur du tre au sens m�tier
     */
    public void setXTre( String pXtre )
    {
        mXTre = pXtre;
    }

    /**
     * @param pYtre la nouvelle valeur du tre au sens m�tier
     */
    public void setYTre( String pYtre )
    {
        mYTre = pYtre;
    }

    /**
     * @return la position de l'axe horizontal
     * @hibernate.property name="xPos" column="X_POS" type="long" length="3" update="true" insert="true"
     */
    public long getHorizontalAxisPos()
    {
        return mHorizontalAxisPos;
    }

    /**
     * @return la position de l'axe vertical
     * @hibernate.property name="yPos" column="Y_POS" type="long" length="3" update="true" insert="true"
     */
    public long getVerticalAxisPos()
    {
        return mVerticalAxisPos;
    }

    /**
     * @param pHorizontalPos la position de l'axe horizontal
     */
    public void setHorizontalAxisPos( long pHorizontalPos )
    {
        mHorizontalAxisPos = pHorizontalPos;
    }

    /**
     * @param pVerticalPos la position de l'axe vertical
     */
    public void setVerticalAxisPos( long pVerticalPos )
    {
        mVerticalAxisPos = pVerticalPos;
    }

    /**
     * {@inheritDoc}
     * 
     * @param pVisitor {@inheritDoc}
     * @param pArgument {@inheritDoc}
     * @return {@inheritDoc}
     * @see org.squale.squalecommon.enterpriselayer.businessobject.config.web.AbstractDisplayConfBO#accept(org.squale.squalecommon.enterpriselayer.businessobject.config.web.DisplayConfVisitor,
     *      java.lang.Object)
     */
    public Object accept( DisplayConfVisitor pVisitor, Object pArgument )
    {
        return pVisitor.visit( this, pArgument );
    }

    
    /**
     * Redefinition of the hashCode method
     * {@inheritDoc} 
     * @return return the hash number of the object
     */
    public int hashCode(){
        return super.hashCode();
    }
    
    /**
     * {@inheritDoc}
     * 
     * @param obj {@inheritDoc}
     * @return {@inheritDoc}
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals( Object obj )
    {
        boolean result = false;
        if ( obj instanceof BubbleConfBO )
        {
            BubbleConfBO bubble = (BubbleConfBO) obj;
            result = bubble.getHorizontalAxisPos() == getHorizontalAxisPos();
            result &= bubble.getVerticalAxisPos() == getVerticalAxisPos();
            result &= bubble.getXTre().equals( getXTre() );
            result &= bubble.getYTre().equals( getYTre() );
        }
        return result;
    }
}
