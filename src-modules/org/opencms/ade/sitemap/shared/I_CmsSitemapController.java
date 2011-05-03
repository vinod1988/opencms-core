/*
 * File   : $Source: /alkacon/cvs/opencms/src-modules/org/opencms/ade/sitemap/shared/Attic/I_CmsSitemapController.java,v $
 * Date   : $Date: 2011/05/03 10:49:06 $
 * Version: $Revision: 1.3 $
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

package org.opencms.ade.sitemap.shared;

import org.opencms.util.CmsUUID;

import java.util.Map;

/**
 * The interface to the sitemap controller.<p>
 * 
 * This interface allows classes which are shared between client and server to access client-only functionality without
 * statically depending on client-only code.<p> 
 * 
 * @author Georg Westenberger
 * 
 * @version $Revision: 1.3 $
 * 
 * @since 8.0.0
 */
public interface I_CmsSitemapController {

    /**
     * Gets the property map for the given id.<p>
     * 
     * @param id a structure id 
     * 
     * @return the property map for that structure id 
     */
    Map<String, CmsClientProperty> getPropertiesForId(CmsUUID id);

    /**
     * Initializes the sitemap entry.<p>
     * 
     * @param entry the sitemap entry to initialize
     */
    void initialize(CmsClientSitemapEntry entry);

    /**
     * This method is used to establish a unique property map object for each id, but replaces the contents of the 
     * map object with new values for each call.<p>
     * 
     * The first call to the method with a given id will just return the map passed in. The n-th call to the method
     * with a given id will return the map object passed in with the first method call for that id, but with its contents
     * replaced by the contents of the map passed in with the n-th call for that id.<p>
     * 
     * The purpose of this is to avoid multiple redundant copies of the same logical map of properties being stored
     * in multiple places.<p>
     * 
     * @param id the map identifying the resource to which the properties belong 
     * @param properties the new properties for the given id
     *  
     * @return the original properties object for the given id, but with its contents replaced
     */
    Map<String, CmsClientProperty> replaceProperties(CmsUUID id, Map<String, CmsClientProperty> properties);

}
