/*
 * File   : $Source: /alkacon/cvs/opencms/src-modules/org/opencms/ade/containerpage/client/ui/Attic/CmsListCollectorEditor.java,v $
 * Date   : $Date: 2011/05/03 10:48:51 $
 * Version: $Revision: 1.13 $
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

import org.opencms.ade.containerpage.client.CmsContainerpageController;
import org.opencms.gwt.client.ui.A_CmsDirectEditButtons;
import org.opencms.gwt.client.ui.CmsDeleteWarningDialog;
import org.opencms.gwt.client.util.CmsDomUtil;
import org.opencms.gwt.client.util.CmsPositionBean;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.http.client.URL;
import com.google.gwt.user.client.Command;

/**
 * Class to provide direct edit buttons within list collector elements.<p>
 * 
 * @author Tobias Herrmann
 * @author Ruediger Kurz
 * 
 * @version $Revision: 1.13 $
 * 
 * @since 8.0.0
 */
public class CmsListCollectorEditor extends A_CmsDirectEditButtons {

    /**
     * Creates a new instance.<p>
     * 
     * @param editable the editable element 
     * @param parentId the parent id 
     */
    public CmsListCollectorEditor(Element editable, String parentId) {

        super(editable, parentId);
    }

    /**
     * @see org.opencms.gwt.client.ui.A_CmsDirectEditButtons#setPosition(org.opencms.gwt.client.util.CmsPositionBean, com.google.gwt.user.client.Element)
     */
    @Override
    public void setPosition(CmsPositionBean position, com.google.gwt.user.client.Element containerElement) {

        m_position = position;
        Element parent = CmsDomUtil.getPositioningParent(getElement());
        if (!containerElement.isOrHasChild(parent)) {
            // the container element should have position relative,
            // so don't use any positioning parent that is not a child of the container-element
            parent = containerElement;
        }
        Style style = getElement().getStyle();
        style.setRight(
            parent.getOffsetWidth() - (m_position.getLeft() + m_position.getWidth() - parent.getAbsoluteLeft()),
            Unit.PX);
        int top = m_position.getTop() - parent.getAbsoluteTop();
        if (top < 25) {
            // if top is <25 the buttons might overlap with the option bar, so increase to 25
            top = 25;
        }
        style.setTop(top, Unit.PX);
    }

    /**
     * Delete the editable element from page and VFS.<p>
     */
    protected void deleteElement() {

        CmsContainerpageController.get().deleteElement(m_editableData.getStructureId(), m_parentResourceId);
    }

    /**
     * @see org.opencms.gwt.client.ui.A_CmsDirectEditButtons#onClickDelete()
     */
    @Override
    protected void onClickDelete() {

        removeHighlighting();
        openWarningDialog();
        CmsDomUtil.ensureMouseOut(m_delete.getElement());
        CmsDomUtil.ensureMouseOut(getElement());
    }

    /**
     * @see org.opencms.gwt.client.ui.A_CmsDirectEditButtons#onClickEdit()
     */
    @Override
    protected void onClickEdit() {

        openEditDialog(false);
        removeHighlighting();
    }

    /**
     * @see org.opencms.gwt.client.ui.A_CmsDirectEditButtons#onClickNew()
     */
    @Override
    protected void onClickNew() {

        openEditDialog(true);
        removeHighlighting();
    }

    /**
     * Opens the content editor.<p>
     * 
     * @param isNew <code>true</code> to create and edit a new resource
     */
    protected void openEditDialog(boolean isNew) {

        if (isNew) {
            CmsContainerpageController.get().getContentEditorHandler().openDialog(
                m_parentResourceId,
                m_editableData.getSitePath() + "&amp;newlink=" + URL.encodeQueryString(m_editableData.getNewLink()),
                true);
        } else {
            CmsContainerpageController.get().getContentEditorHandler().openDialog(
                m_editableData.getStructureId(),
                m_editableData.getSitePath(),
                false,
                m_parentResourceId);
        }
    }

    /**
     * Shows the delete warning dialog.<p>
     */
    protected void openWarningDialog() {

        CmsDeleteWarningDialog dialog = new CmsDeleteWarningDialog(m_editableData.getSitePath());
        Command callback = new Command() {

            /**
             * @see com.google.gwt.user.client.Command#execute()
             */
            public void execute() {

                deleteElement();
            }
        };
        dialog.loadAndShow(callback);
    }

}
