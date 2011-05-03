/*
 * File   : $Source: /alkacon/cvs/opencms/src-modules/org/opencms/ade/galleries/shared/Attic/CmsResultItemBean.java,v $
 * Date   : $Date: 2011/05/03 10:48:50 $
 * Version: $Revision: 1.6 $
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

package org.opencms.ade.galleries.shared;

import org.opencms.gwt.shared.CmsListInfoBean;
import org.opencms.gwt.shared.sort.I_CmsHasPath;
import org.opencms.gwt.shared.sort.I_CmsHasTitle;
import org.opencms.gwt.shared.sort.I_CmsHasType;
import org.opencms.util.CmsStringUtil;

/**
 * A specific bean holding all info to be displayed in {@link org.opencms.ade.galleries.client.ui.CmsResultListItem}s.<p>
 * 
 * @see org.opencms.ade.galleries.client.ui.CmsResultListItem
 * 
 * @author Polina Smagina
 * 
 * @version $Revision: 1.6 $
 * 
 * @since 8.0.0
 */
public class CmsResultItemBean extends CmsListInfoBean implements I_CmsHasTitle, I_CmsHasPath, I_CmsHasType {

    /** The structured id of the resource. */
    private String m_clientId;

    /** The result item description. */
    private String m_description;

    /** The reason this resource may not be edited. Editable if empty. */
    private String m_noEditReson;

    /** The resource path as a unique resource id. */
    private String m_path;

    /** The resource type name. */
    private String m_type;

    /**
     * Returns the structured id.<p>
     *
     * @return the structured id
     */
    public String getClientId() {

        return m_clientId;
    }

    /**
     * Returns the description.<p>
     *
     * @return the description
     */
    public String getDescription() {

        return m_description;
    }

    /**
     * Returns the noEditReson.<p>
     *
     * @return the noEditReson
     */
    public String getNoEditReson() {

        return m_noEditReson;
    }

    /**
     * Returns the resourcePath.<p>
     *
     * @return the resourcePath
     */
    public String getPath() {

        return m_path;
    }

    /**
     * Returns the resource type name.<p>
     *
     * @return the resource type name
     */
    public String getType() {

        return m_type;
    }

    /**
     * Returns if the represented resource is editable by the current user.<p>
     * 
     * @return <code>true</code> if editable
     */
    public boolean isEditable() {

        return CmsStringUtil.isEmptyOrWhitespaceOnly(m_noEditReson);
    }

    /**
     * Sets the structure id.<p>
     *
     * @param clientId the structure id to set
     */
    public void setClientId(String clientId) {

        m_clientId = clientId;
    }

    /**
     * Sets the description.<p>
     * 
     * Also used as sub-title.<p>
     *
     * @param description the description to set
     */
    public void setDescription(String description) {

        super.setSubTitle(description);
        m_description = description;
    }

    /**
     * Sets the reason this resource may not be edited.<p>
     *
     * @param noEditReson the reason this resource may not be edited to set
     */
    public void setNoEditReson(String noEditReson) {

        m_noEditReson = noEditReson;
    }

    /**
     * Sets the resource path.<p>
     *
     * @param path the resource path to set
     */
    public void setPath(String path) {

        m_path = path;
    }

    /**
     * @see org.opencms.gwt.shared.CmsListInfoBean#setSubTitle(java.lang.String)
     */
    @Override
    public void setSubTitle(String subTitle) {

        setDescription(subTitle);
    }

    /**
     * Sets the resource type name.<p>
     * 
     * @param type the resource type name to set
     */
    public void setType(String type) {

        m_type = type;
    }
}
