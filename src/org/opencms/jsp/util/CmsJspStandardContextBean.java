/*
 * This library is part of OpenCms -
 * the Open Source Content Management System
 *
 * Copyright (c) Alkacon Software GmbH (http://www.alkacon.com)
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

package org.opencms.jsp.util;

import org.opencms.ade.configuration.CmsADEConfigData;
import org.opencms.ade.detailpage.CmsDetailPageInfo;
import org.opencms.ade.detailpage.CmsDetailPageResourceHandler;
import org.opencms.file.CmsObject;
import org.opencms.file.CmsRequestContext;
import org.opencms.file.CmsResource;
import org.opencms.flex.CmsFlexController;
import org.opencms.jsp.CmsJspBean;
import org.opencms.jsp.Messages;
import org.opencms.main.CmsException;
import org.opencms.main.CmsLog;
import org.opencms.main.CmsRuntimeException;
import org.opencms.main.OpenCms;
import org.opencms.util.CmsUUID;
import org.opencms.xml.containerpage.CmsContainerBean;
import org.opencms.xml.containerpage.CmsContainerElementBean;
import org.opencms.xml.containerpage.CmsContainerPageBean;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.ServletRequest;

import org.apache.commons.logging.Log;

import com.google.common.base.Function;
import com.google.common.collect.MapMaker;

/**
 * Allows convenient access to the most important OpenCms functions on a JSP page,
 * indented to be used from a JSP with the JSTL or EL.<p>
 * 
 * This bean is available by default in the context of an OpenCms managed JSP.<p> 
 * 
 * @since 8.0
 */
public final class CmsJspStandardContextBean {

    /** The attribute name of the cms object.*/
    public static final String ATTRIBUTE_CMS_OBJECT = "__cmsObject";

    /** The attribute name of the standard JSP context bean. */
    public static final String ATTRIBUTE_NAME = "cms";

    /** The logger instance for this class. */
    protected static final Log LOG = CmsLog.getLog(CmsJspStandardContextBean.class);

    /** OpenCms user context. */
    protected CmsObject m_cms;

    /** The container the currently rendered element is part of. */
    private CmsContainerBean m_container;

    /** The current detail content resource if available. */
    private CmsResource m_detailContentResource;

    /** Flag to indicate if element was just edited. */
    private boolean m_edited;

    /** The currently rendered element. */
    private CmsContainerElementBean m_element;

    /** The currently displayed container page. */
    private CmsContainerPageBean m_page;

    /** The VFS content access bean. */
    private CmsJspVfsAccessBean m_vfsBean;

    /**
     * Creates an empty instance.<p>
     */
    private CmsJspStandardContextBean() {

        // NOOP
    }

    /**
     * Creates a new standard JSP context bean.
     * 
     * @param req the current servlet request
     */
    private CmsJspStandardContextBean(ServletRequest req) {

        CmsFlexController controller = CmsFlexController.getController(req);
        CmsObject cms;
        if (controller != null) {
            cms = controller.getCmsObject();
        } else {
            cms = (CmsObject)req.getAttribute(ATTRIBUTE_CMS_OBJECT);
        }
        if (cms == null) {
            // cms object unavailable - this request was not initialized properly
            throw new CmsRuntimeException(Messages.get().container(
                Messages.ERR_MISSING_CMS_CONTROLLER_1,
                CmsJspBean.class.getName()));
        }
        updateCmsObject(cms);

        m_detailContentResource = CmsDetailPageResourceHandler.getDetailResource(req);
    }

    /**
     * Creates a new instance of the standard JSP context bean.<p>
     * 
     * To prevent multiple creations of the bean during a request, the OpenCms request context 
     * attributes are used to cache the created VFS access utility bean.<p>
     * 
     * @param req the current servlet request
     * 
     * @return a new instance of the standard JSP context bean
     */
    public static CmsJspStandardContextBean getInstance(ServletRequest req) {

        Object attribute = req.getAttribute(ATTRIBUTE_NAME);
        CmsJspStandardContextBean result;
        if ((attribute != null) && (attribute instanceof CmsJspStandardContextBean)) {
            result = (CmsJspStandardContextBean)attribute;
        } else {
            result = new CmsJspStandardContextBean(req);
            req.setAttribute(ATTRIBUTE_NAME, result);
        }
        return result;
    }

    /**
     * Returns a copy of this JSP context bean.<p>
     * 
     * @return a copy of this JSP context bean
     */
    public CmsJspStandardContextBean createCopy() {

        CmsJspStandardContextBean result = new CmsJspStandardContextBean();
        result.m_container = getContainer();
        if (getDetailContent() != null) {
            result.m_detailContentResource = getDetailContent().getCopy();
        }
        result.m_element = getElement();
        result.m_page = getPage();
        return result;
    }

    /**
     * Returns a caching hash specific to the element, it's properties and the current container width.<p>
     * 
     * @return the caching hash
     */
    public String elementCachingHash() {

        if ((m_element != null) && (m_container != null)) {
            return m_element.editorHash()
                + "w:"
                + m_container.getWidth()
                + "cName:"
                + m_container.getName()
                + "cType:"
                + m_container.getType();
        }
        return "";
    }

    /**
     * Returns the container the currently rendered element is part of.<p>
     * 
     * @return the currently the currently rendered element is part of
     */
    public CmsContainerBean getContainer() {

        return m_container;
    }

    /**
     * Returns the current detail content, or <code>null</code> if no detail content is requested.<p>
     * 
     * @return the current detail content, or <code>null</code> if no detail content is requested.<p>
     */
    public CmsResource getDetailContent() {

        return m_detailContentResource;
    }

    /**
     * Returns the structure id of the current detail content, or <code>null</code> if no detail content is requested.<p>
     * 
     * @return the structure id of the current detail content, or <code>null</code> if no detail content is requested.<p>
     */
    public CmsUUID getDetailContentId() {

        return m_detailContentResource == null ? null : m_detailContentResource.getStructureId();
    }

    /**
     * Returns the detail content site path. Returns <code>null</code> if not available.<p>
     * 
     * @return the detail content site path
     */
    public String getDetailContentSitePath() {

        return ((m_cms == null) || (m_detailContentResource == null))
        ? null
        : m_cms.getSitePath(m_detailContentResource);
    }

    /**    
     * Returns the currently rendered element.<p>
     * 
     * @return the currently rendered element
     */
    public CmsContainerElementBean getElement() {

        return m_element;
    }

    /**
     * Returns a lazy map which computes the detail page link as a value when given the name of a (named) dynamic function
     * as a key.<p>
     * 
     * @return a lazy map for computing function detail page links  
     */
    public Map<String, String> getFunctionDetail() {

        MapMaker mm = new MapMaker();
        return mm.makeComputingMap(new Function<String, String>() {

            public String apply(String key) {

                String detailType = CmsDetailPageInfo.FUNCTION_PREFIX + key;
                CmsADEConfigData config = OpenCms.getADEManager().lookupConfiguration(
                    m_cms,
                    m_cms.addSiteRoot(m_cms.getRequestContext().getUri()));
                List<CmsDetailPageInfo> detailPages = config.getDetailPagesForType(detailType);
                if ((detailPages == null) || (detailPages.size() == 0)) {
                    return "";
                }
                CmsDetailPageInfo mainDetailPage = detailPages.get(0);
                CmsUUID id = mainDetailPage.getId();
                CmsResource detailRes;
                try {
                    detailRes = m_cms.readResource(id);
                    return OpenCms.getLinkManager().substituteLink(m_cms, detailRes);
                } catch (CmsException e) {
                    LOG.warn(e.getLocalizedMessage(), e);
                    return "";
                }
            }
        });
    }

    /**
     * Returns the current locale.<p>
     * 
     * @return the current locale
     */
    public Locale getLocale() {

        return getRequestContext().getLocale();
    }

    /**
     * Returns the currently displayed container page.<p>
     * 
     * @return the currently displayed container page
     */
    public CmsContainerPageBean getPage() {

        return m_page;
    }

    /**
     * Returns the request context.<p>
     * 
     * @return the request context
     */
    public CmsRequestContext getRequestContext() {

        return m_cms.getRequestContext();
    }

    /**
     * Returns an initialized VFS access bean.<p>
     * 
     * @return an initialized VFS access bean
     */
    public CmsJspVfsAccessBean getVfs() {

        if (m_vfsBean == null) {
            // create a new VVFS access bean
            m_vfsBean = CmsJspVfsAccessBean.create(m_cms);
        }
        return m_vfsBean;
    }

    /**
     * Checks whether a detail page is available for the container element.<p>
     * 
     * @return true if there is a detail page for the container element
     */
    public boolean isDetailPageAvailable() {

        if (m_cms == null) {
            return false;
        }
        CmsContainerElementBean element = getElement();
        if (element == null) {
            return false;
        }
        if (element.isInMemoryOnly()) {
            return false;
        }
        CmsResource res = element.getResource();
        if (res == null) {
            return false;
        }
        try {
            String detailPage = OpenCms.getADEManager().getDetailPageFinder().getDetailPage(
                m_cms,
                res.getRootPath(),
                m_cms.getRequestContext().getUri());
            return detailPage != null;
        } catch (CmsException e) {
            LOG.warn(e.getLocalizedMessage(), e);
            return false;
        }
    }

    /**
     * Returns <code>true</code> if this is a request to a detail resource, <code>false</code> otherwise.<p>
     * 
     * Same as to check if {@link #getDetailContent()} is <code>null</code>.<p>
     * 
     * @return <code>true</code> if this is a request to a detail resource, <code>false</code> otherwise
     */
    public boolean isDetailRequest() {

        return m_detailContentResource != null;
    }

    /**
     * Returns the flag to indicate if in drag and drop mode.<p>
     *
     * @return <code>true</code> if in drag and drop mode
     */
    public boolean isEdited() {

        return m_edited;
    }

    /**
     * Sets the container the currently rendered element is part of.<p>
     *
     * @param container the container the currently rendered element is part of
     */
    public void setContainer(CmsContainerBean container) {

        m_container = container;
    }

    /**
     * Sets the flag to indicate if in drag and drop mode.<p>
     *
     * @param edited <code>true</code> if in drag and drop mode
     */
    public void setEdited(boolean edited) {

        m_edited = edited;
    }

    /**
     * Sets the currently rendered element.<p>
     *
     * @param element the currently rendered element to set
     */
    public void setElement(CmsContainerElementBean element) {

        m_element = element;
    }

    /**
     * Sets the currently displayed container page.<p>
     *
     * @param page the currently displayed container page to set
     */
    public void setPage(CmsContainerPageBean page) {

        m_page = page;
    }

    /** 
     * Updates the internally stored OpenCms user context.<p>
     * 
     * @param cms the new OpenCms user context
     */
    public void updateCmsObject(CmsObject cms) {

        try {
            m_cms = OpenCms.initCmsObject(cms);
        } catch (CmsException e) {
            // should not happen
            m_cms = cms;
        }
    }
}