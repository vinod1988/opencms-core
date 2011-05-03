/*
 * File   : $Source: /alkacon/cvs/opencms/src-modules/org/opencms/gwt/shared/Attic/CmsLinkBean.java,v $
 * Date   : $Date: 2011/05/03 10:49:02 $
 * Version: $Revision: 1.2 $
 *
 * This library is part of OpenCms -
 * the Open Source Content Management System
 *
 * Copyright (C) 2002 - 2011 Alkacon Software (http://www.alkacon.com)
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * For further information about Alkacon Software, please see the
 * company website: http://www.alkacon.com
 *
 * For further information about OpenCms, please see the
 * project website: http://www.opencms.org
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

package org.opencms.gwt.shared;

/**
 * A bean for representing a link, which can be external or internal.<p>
 * 
 * @author Georg Westenberger
 * 
 * @version $Revision: 1.2 $
 * 
 * @since 8.0.0
 */
public class CmsLinkBean {

    /** The link target.*/
    private String m_link;

    /** True if this is an internal link. */
    private boolean m_internal;

    /**
     * Creates a new link bean.<p>
     * 
     * @param link the link target
     * @param internal true if this is an internal link
     */
    public CmsLinkBean(String link, boolean internal) {

        m_link = link;
        m_internal = internal;
    }

    /**
     * Returns the link target.<p>
     * 
     * @return the link target
     */
    public String getLink() {

        return m_link;
    }

    /**
     * Returns true if this is an internal link.<p>
     *  
     * @return true if this is an internal link
     */
    public boolean isInternal() {

        return m_internal;
    }
}
