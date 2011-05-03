/*
 * File   : $Source: /alkacon/cvs/opencms/src-modules/org/opencms/gwt/client/ui/Attic/CmsAlertDialog.java,v $
 * Date   : $Date: 2011/05/03 10:48:53 $
 * Version: $Revision: 1.11 $
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

package org.opencms.gwt.client.ui;

import org.opencms.gwt.client.Messages;
import org.opencms.gwt.client.ui.I_CmsButton.ButtonColor;
import org.opencms.gwt.client.ui.I_CmsButton.ButtonStyle;
import org.opencms.gwt.client.ui.css.I_CmsLayoutBundle;

import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Provides an alert dialog with a button.<p>
 * 
 * @author Michael Moossen
 * 
 * @version $Revision: 1.11 $
 * 
 * @since 8.0.0
 */
public class CmsAlertDialog extends CmsPopup {

    /** The panel for the bottom widgets. */
    private FlowPanel m_bottomWidgets;

    /** The 'close' button. */
    private CmsPushButton m_closeButton;

    /** The content text. */
    private FlowPanel m_content;

    /** The action handler. */
    private I_CmsCloseDialogHandler m_handler;

    /** The panel for the top widgets. */
    private FlowPanel m_topWidgets;

    /** The warning message. */
    private FlexTable m_warningMessage;

    /** 
     * Constructor.<p>
     */
    public CmsAlertDialog() {

        this("", "");
    }

    /**
     * Constructor.<p>
     * 
     * @param title the title and heading of the dialog
     * @param content the content text
     */
    public CmsAlertDialog(String title, String content) {

        this(title, content, Messages.get().key(Messages.GUI_CLOSE_0));
    }

    /**
     * Constructor.<p>
     * 
     * @param title the title and heading of the dialog
     * @param content the content text
     * @param buttonText the button text
     */
    public CmsAlertDialog(String title, String content, String buttonText) {

        this(title, content, buttonText, null);
    }

    /**
     * Constructor.<p>
     * 
     * @param title the title and heading of the dialog
     * @param content the content text
     * @param buttonText the button text
     * @param buttonIconClass the button icon class
     */
    public CmsAlertDialog(String title, String content, String buttonText, String buttonIconClass) {

        super(title, 439);
        super.setAutoHideEnabled(false);
        super.setModal(true);
        setGlassEnabled(true);

        // create the dialogs content panel
        m_content = new FlowPanel();
        m_content.addStyleName(I_CmsLayoutBundle.INSTANCE.dialogCss().alertMainContent());

        // create the top widget panel
        m_topWidgets = new FlowPanel();
        m_topWidgets.addStyleName(I_CmsLayoutBundle.INSTANCE.dialogCss().alertTopContent());
        m_topWidgets.getElement().getStyle().setDisplay(Display.NONE);
        m_content.add(m_topWidgets);

        // create the warning message
        m_warningMessage = new FlexTable();
        m_warningMessage.addStyleName(I_CmsLayoutBundle.INSTANCE.dialogCss().warningContent());
        m_warningMessage.addStyleName(I_CmsLayoutBundle.INSTANCE.generalCss().cornerAll());
        m_warningMessage.setText(0, 0, "");
        m_warningMessage.setHTML(0, 1, content);

        m_content.add(m_warningMessage);

        // create the bottom widget panel
        m_bottomWidgets = new FlowPanel();
        m_bottomWidgets.addStyleName(I_CmsLayoutBundle.INSTANCE.dialogCss().alertBottomContent());
        m_bottomWidgets.getElement().getStyle().setDisplay(Display.NONE);
        m_content.add(m_bottomWidgets);

        // set the content to the popup
        setMainContent(m_content);

        // add the close button
        m_closeButton = new CmsPushButton();
        m_closeButton.setText(buttonText);
        m_closeButton.setImageClass(buttonIconClass);
        m_closeButton.setUseMinWidth(true);
        m_closeButton.setButtonStyle(ButtonStyle.TEXT, ButtonColor.BLUE);
        m_closeButton.addClickHandler(new ClickHandler() {

            /**
             * @see com.google.gwt.event.dom.client.ClickHandler#onClick(com.google.gwt.event.dom.client.ClickEvent)
             */
            public void onClick(ClickEvent event) {

                onClose();
            }
        });
        addButton(m_closeButton);
    }

    /**
     * Adds a widget to this dialogs bottom content.<p>
     * 
     * @param w the widget to add
     */
    public void addBottomWidget(Widget w) {

        m_content.removeStyleName(I_CmsLayoutBundle.INSTANCE.dialogCss().alertMainContent());
        m_bottomWidgets.getElement().getStyle().clearDisplay();
        m_bottomWidgets.add(w);

    }

    /**
     * Adds a widget to this dialogs top content.<p>
     * 
     * @param w the widget to add
     */
    public void addTopWidget(Widget w) {

        m_content.removeStyleName(I_CmsLayoutBundle.INSTANCE.dialogCss().alertMainContent());
        m_topWidgets.getElement().getStyle().clearDisplay();
        m_topWidgets.add(w);
    }

    /**
     * @see org.opencms.gwt.client.ui.CmsPopup#center()
     */
    @Override
    public void center() {

        super.center();
        getCloseButton().setEnabled(true);
    }

    /**
     * @see org.opencms.gwt.client.ui.CmsPopup#setAutoHideEnabled(boolean)
     */
    @Override
    public void setAutoHideEnabled(boolean autoHide) {

        // prevent enabling auto hide
    }

    /**
     * Sets the cancel/close button icon class.<p>
     * 
     * @param iconClass the icon class
     */
    public void setCloseIconClass(String iconClass) {

        getCloseButton().setImageClass(iconClass);
    }

    /**
     * Sets the close button text.<p>
     * 
     * @param text the button text
     */
    public void setCloseText(String text) {

        m_closeButton.setText(text);
    }

    /**
     * Sets the dialog handler.<p>
     * 
     * @param handler the handler to set
     */
    public void setHandler(I_CmsCloseDialogHandler handler) {

        m_handler = handler;
    }

    /**
     * @see org.opencms.gwt.client.ui.CmsPopup#setModal(boolean)
     */
    @Override
    public void setModal(boolean modal) {

        // it is always modal
    }

    /**
     * Sets the warning text (HTML possible).<p>
     * 
     * @param warningText the warning text to set
     */
    public void setWarningMessage(String warningText) {

        m_warningMessage.setHTML(0, 1, warningText);
    }

    /**
     * @see org.opencms.gwt.client.ui.CmsPopup#show()
     */
    @Override
    public void show() {

        super.show();
        getCloseButton().setEnabled(true);
    }

    /**
     * Returns the button widget.<p>
     * 
     * @return the button
     */
    protected CmsPushButton getCloseButton() {

        return m_closeButton;
    }

    /**
     * Returns the dialog handler.<p>
     * 
     * @return the dialog handler
     */
    protected I_CmsCloseDialogHandler getHandler() {

        return m_handler;
    }

    /**
     * Executed on 'close' click. <p>
     */
    protected void onClose() {

        getCloseButton().setEnabled(false);
        if (getHandler() != null) {
            getHandler().onClose();
        }
        hide();
    }
}
