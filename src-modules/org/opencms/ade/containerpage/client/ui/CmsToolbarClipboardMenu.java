/*
 * File   : $Source: /alkacon/cvs/opencms/src-modules/org/opencms/ade/containerpage/client/ui/Attic/CmsToolbarClipboardMenu.java,v $
 * Date   : $Date: 2011/05/03 10:48:51 $
 * Version: $Revision: 1.23 $
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

package org.opencms.ade.containerpage.client.ui;

import org.opencms.ade.containerpage.client.CmsContainerpageHandler;
import org.opencms.ade.containerpage.client.CmsFavoritesDNDController;
import org.opencms.ade.containerpage.client.Messages;
import org.opencms.ade.containerpage.client.ui.css.I_CmsLayoutBundle;
import org.opencms.gwt.client.ui.A_CmsToolbarMenu;
import org.opencms.gwt.client.ui.CmsList;
import org.opencms.gwt.client.ui.CmsListItem;
import org.opencms.gwt.client.ui.CmsTabbedPanel;
import org.opencms.gwt.client.ui.I_CmsButton;
import org.opencms.gwt.client.ui.css.I_CmsImageBundle;
import org.opencms.gwt.client.util.CmsDebugLog;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * The clip-board tool-bar menu.<p>
 * 
 * @author Tobias Herrmann
 * 
 * @version $Revision: 1.23 $
 * 
 * @since 8.0.0
 */
public class CmsToolbarClipboardMenu extends A_CmsToolbarMenu<CmsContainerpageHandler> {

    /** The favorite list widget. */
    protected CmsFavoriteTab m_favorites;

    /** Flag to indicate if the favorites are being edited. */
    protected boolean m_isEditingFavorites;

    /** The main content widget. */
    private FlowPanel m_content;

    /** The favorites editing drag and drop controller. */
    private CmsFavoritesDNDController m_dndController;

    /** The favorite list drop-zone. */
    private CmsList<CmsListItem> m_dropzone;

    /** The recent list widget. */
    private CmsRecentTab m_recent;

    /** The favorite and recent list tabs. */
    private CmsTabbedPanel<Widget> m_tabs;

    /**
     * Constructor.<p>
     * 
     * @param handler the container-page handler
     */
    public CmsToolbarClipboardMenu(CmsContainerpageHandler handler) {

        super(I_CmsButton.ButtonData.CLIPBOARD, handler);

        m_content = new FlowPanel();
        m_tabs = new CmsTabbedPanel<Widget>();
        m_tabs.addSelectionHandler(new SelectionHandler<Integer>() {

            /**
             * @see com.google.gwt.event.logical.shared.SelectionHandler#onSelection(com.google.gwt.event.logical.shared.SelectionEvent)
             */
            public void onSelection(SelectionEvent<Integer> event) {

                if (m_isEditingFavorites) {
                    m_favorites.saveFavorites();
                }
            }
        });
        m_favorites = new CmsFavoriteTab(this);
        m_recent = new CmsRecentTab();

        m_tabs.add(m_favorites, Messages.get().key(Messages.GUI_TAB_FAVORITES_TITLE_0));
        m_tabs.add(m_recent, Messages.get().key(Messages.GUI_TAB_RECENT_TITLE_0));
        SimplePanel tabsContainer = new SimplePanel();
        tabsContainer.addStyleName(I_CmsLayoutBundle.INSTANCE.containerpageCss().menuTabContainer());
        tabsContainer.add(m_tabs);
        m_content.add(tabsContainer);

        m_dropzone = new CmsList<CmsListItem>();
        m_dropzone.setStyleName(I_CmsLayoutBundle.INSTANCE.containerpageCss().clipboardDropzone());
        m_dropzone.setDropEnabled(true);
        m_content.add(m_dropzone);
        setMenuWidget(m_content);
        m_dndController = new CmsFavoritesDNDController();
    }

    /**
     * Adds an element to the favorite list widget.<p>
     * 
     * @param listItem the item widget
     */
    public void addToFavorites(CmsListItem listItem) {

        m_favorites.addListItem(listItem);
    }

    /**
     * Adds an element to the recent list widget.<p>
     * 
     * @param listItem the item widget
     */
    public void addToRecent(CmsListItem listItem) {

        m_recent.addListItem(listItem);
    }

    /**
     * Clears the contents of the favorite list widget.<p>
     */
    public void clearFavorites() {

        m_favorites.clearList();
    }

    /**
     * Clears the contents of the recent list widget.<p>
     */
    public void clearRecent() {

        m_recent.clearList();
    }

    /**
     * Enables the favorite list editing.<p>
     */
    public void enableFavoritesEdit() {

        m_isEditingFavorites = true;
        getHandler().enableFavoriteEditing(true, m_dndController);

        Iterator<Widget> it = m_favorites.iterator();
        while (it.hasNext()) {

            CmsMenuListItem element = (CmsMenuListItem)it.next();
            element.setMoveIconStyle(I_CmsImageBundle.INSTANCE.style().changeOrderIcon(), Messages.get().key(
                Messages.GUI_BUTTON_CHANGE_ORDER_TEXT_0));
            element.showRemoveButton();
        }
    }

    /**
     * Returns the tool-bar drop-zone.<p>
     *
     * @return the drop-zone
     */
    public CmsList<CmsListItem> getDropzone() {

        return m_dropzone;
    }

    /**
     * @see org.opencms.gwt.client.ui.I_CmsToolbarButton#onToolbarActivate()
     */
    public void onToolbarActivate() {

        getHandler().loadFavorites();
        getHandler().loadRecent();
    }

    /**
     * @see org.opencms.gwt.client.ui.I_CmsToolbarButton#onToolbarDeactivate()
     */
    public void onToolbarDeactivate() {

        if (m_isEditingFavorites) {
            m_favorites.saveFavorites();
        }
    }

    /**
     * Reloads the favorite list.<p>
     */
    public void reloadFavorites() {

        m_isEditingFavorites = false;
        getHandler().enableFavoriteEditing(false, m_dndController);
        getHandler().loadFavorites();
    }

    /**
     * Saves the favorite list.<p>
     */
    public void saveFavorites() {

        m_isEditingFavorites = false;
        getHandler().enableFavoriteEditing(false, m_dndController);
        List<String> clientIds = new ArrayList<String>();
        Iterator<Widget> it = m_favorites.iterator();
        while (it.hasNext()) {
            try {
                CmsMenuListItem element = (CmsMenuListItem)it.next();
                element.hideRemoveButton();
                element.setMoveIconStyle(
                    I_CmsImageBundle.INSTANCE.style().moveIcon(),
                    org.opencms.gwt.client.Messages.get().key(org.opencms.gwt.client.Messages.GUI_TOOLBAR_MOVE_TO_0));
                clientIds.add(element.getId());
            } catch (ClassCastException e) {
                CmsDebugLog.getInstance().printLine("Could not cast widget");
            }
        }
        getHandler().saveFavoriteList(clientIds);
    }

    /**
     * Opens the menu showing the favorite list drop-zone and hiding all other menu content.<p>
     * 
     * @param show <code>true</code> to show the drop-zone
     */
    public void showDropzone(boolean show) {

        if (show) {
            m_content.addStyleName(I_CmsLayoutBundle.INSTANCE.containerpageCss().showDropzone());
            openMenu();
        } else {
            if (isOpen()) {
                m_content.removeStyleName(I_CmsLayoutBundle.INSTANCE.containerpageCss().showDropzone());
                closeMenu();
            }
        }
    }

}
