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

package org.opencms.ade.containerpage.shared;

import org.opencms.gwt.shared.CmsAdditionalInfoBean;
import org.opencms.util.CmsStringUtil;
import org.opencms.xml.content.CmsXmlContentProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * Bean holding all element information including it's formatted contents.<p>
 * 
 * @since 8.0.0
 */
public class CmsContainerElementData extends CmsContainerElement {

    /** The contents by container type. */
    private Map<String, String> m_contents;

    /** The required css resources. */
    private Set<String> m_cssResources;

    /** The group-container description. */
    private String m_description;

    /** The inheritance infos off all sub-items. */
    private List<CmsInheritanceInfo> m_inheritanceInfos = new ArrayList<CmsInheritanceInfo>();

    /** The inheritance name. */
    private String m_inheritanceName;

    /** The last user modifying the element. */
    private String m_lastModifiedByUser;

    /** The date of last modification. */
    private long m_lastModifiedDate;

    /** The element navText property. */
    private String m_navText;

    /** The setting for this container element. */
    private Map<String, CmsXmlContentProperty> m_settingConfig;

    /** The settings for this container entry. */
    private Map<String, String> m_settings;

    /** The contained sub-item id's. */
    private List<String> m_subItems = new ArrayList<String>();

    /** The element title property. */
    private String m_title;

    /** The supported container types of a group-container. */
    private Set<String> m_types;

    /**
     * Returns the contents.<p>
     *
     * @return the contents
     */
    public Map<String, String> getContents() {

        return m_contents;
    }

    /**
     * Returns the required css resources.<p>
     *
     * @return the required css resources
     */
    public Set<String> getCssResources() {

        return m_cssResources;
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
     * Returns the individual element settings formated with nice-names to be used as additional-info.<p>
     * 
     * @return the settings list
     */
    public List<CmsAdditionalInfoBean> getFormatedIndividualSettings() {

        List<CmsAdditionalInfoBean> result = new ArrayList<CmsAdditionalInfoBean>();
        if (m_settings != null) {
            for (Entry<String, String> settingEntry : m_settings.entrySet()) {
                String settingKey = settingEntry.getKey();
                if (m_settingConfig.containsKey(settingEntry.getKey())) {
                    String niceName = m_settingConfig.get(settingEntry.getKey()).getNiceName();
                    if (CmsStringUtil.isNotEmptyOrWhitespaceOnly(m_settingConfig.get(settingEntry.getKey()).getNiceName())) {
                        settingKey = niceName;
                    }
                }
                result.add(new CmsAdditionalInfoBean(settingKey, settingEntry.getValue(), null));
            }
        }
        return result;
    }

    /**
     * Returns the inheritance infos off all sub-items.
     * @return the inheritance infos off all sub-items.
     */
    public List<CmsInheritanceInfo> getInheritanceInfos() {

        if (isInheritContainer()) {
            return m_inheritanceInfos;
        } else {
            throw new UnsupportedOperationException("Only inherit containers have inheritance infos");
        }
    }

    /**
     * Returns the inheritance name.<p>
     *
     * @return the inheritance name
     */
    public String getInheritanceName() {

        return m_inheritanceName;
    }

    /**
     * Returns the last modifying user.<p>
     *
     * @return the last modifying user
     */
    public String getLastModifiedByUser() {

        return m_lastModifiedByUser;
    }

    /**
     * Returns the last modification date.<p>
     *
     * @return the last modification date
     */
    public long getLastModifiedDate() {

        return m_lastModifiedDate;
    }

    /**
     * Returns the navText.<p>
     *
     * @return the navText
     */
    public String getNavText() {

        return m_navText;
    }

    /**
     * Gets the setting configuration for this container element.<p>
     * 
     * @return the setting configuration map 
     */
    public Map<String, CmsXmlContentProperty> getSettingConfig() {

        return m_settingConfig;
    }

    /**
     * Returns the settings for this container element.<p>
     * 
     * @return a map of settings
     */
    public Map<String, String> getSettings() {

        return m_settings;
    }

    /**
     * Returns the sub-items.<p>
     *
     * @return the sub-items
     */
    public List<String> getSubItems() {

        if (isGroupContainer()) {
            return m_subItems;
        } else if (isInheritContainer()) {
            List<String> result = new ArrayList<String>();
            for (CmsInheritanceInfo info : m_inheritanceInfos) {
                if (info.isVisibile()) {
                    result.add(info.getClientId());
                }
            }
            return result;
        } else {
            throw new UnsupportedOperationException("Only group or inherit containers have sub-items");
        }
    }

    /**
     * Returns the title.<p>
     *
     * @return the title
     */
    public String getTitle() {

        return m_title;
    }

    /**
     * Returns the supported container types.<p>
     *
     * @return the supported container types
     */
    public Set<String> getTypes() {

        return m_types;
    }

    /**
     * @see org.opencms.ade.containerpage.shared.CmsContainerElement#hasSettings()
     */
    @Override
    public boolean hasSettings() {

        return !getSettingConfig().isEmpty();
    }

    /**
     * Sets the contents.<p>
     *
     * @param contents the contents to set
     */
    public void setContents(Map<String, String> contents) {

        m_contents = contents;
    }

    /**
     * Sets the required css resources.<p>
     *
     * @param cssResources the required css resources to set
     */
    public void setCssResources(Set<String> cssResources) {

        m_cssResources = cssResources;
    }

    /**
     * Sets the description.<p>
     *
     * @param description the description to set
     */
    public void setDescription(String description) {

        m_description = description;
    }

    /**
     * Sets the inheritance infos.<p>
     *
     * @param inheritanceInfos the inheritance infos to set
     */
    public void setInheritanceInfos(List<CmsInheritanceInfo> inheritanceInfos) {

        m_inheritanceInfos = inheritanceInfos;
    }

    /**
     * Sets the inheritance name.<p>
     *
     * @param inheritanceName the inheritance name to set
     */
    public void setInheritanceName(String inheritanceName) {

        m_inheritanceName = inheritanceName;
    }

    /**
     * Sets the modifying userdByUser.<p>
     *
     * @param lastModifiedByUser the last modifying user to set
     */
    public void setLastModifiedByUser(String lastModifiedByUser) {

        m_lastModifiedByUser = lastModifiedByUser;
    }

    /**
     * Sets the last modification date.<p>
     *
     * @param lastModifiedDate the last modification date to set
     */
    public void setLastModifiedDate(long lastModifiedDate) {

        m_lastModifiedDate = lastModifiedDate;
    }

    /**
     * Sets the navText.<p>
     *
     * @param navText the navText to set
     */
    public void setNavText(String navText) {

        m_navText = navText;
    }

    /**
     * Sets the setting configuration of this container element.<p>
     * 
     * @param settingConfig the new setting configuration 
     */
    public void setSettingConfig(Map<String, CmsXmlContentProperty> settingConfig) {

        m_settingConfig = settingConfig;
    }

    /**
     * Sets the settings for this container element.<p>
     * 
     * @param settings the new settings
     */
    public void setSettings(Map<String, String> settings) {

        m_settings = settings;
    }

    /**
     * Sets the sub-items.<p>
     *
     * @param subItems the sub-items to set
     */
    public void setSubItems(List<String> subItems) {

        m_subItems = subItems;
    }

    /**
     * Sets the title.<p>
     *
     * @param title the title to set
     */
    public void setTitle(String title) {

        m_title = title;
    }

    /**
     * Sets the supported container types.<p>
     *
     * @param types the supported container types to set
     */
    public void setTypes(Set<String> types) {

        m_types = types;
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {

        StringBuffer result = new StringBuffer();
        result.append("Title: ").append(m_title).append("  File: ").append(getSitePath()).append("  ClientId: ").append(
            getClientId());
        return result.toString();
    }

}
