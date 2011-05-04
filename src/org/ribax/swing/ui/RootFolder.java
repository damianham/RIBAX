/*
 * RIBAX, Making Web Applications Easy 
 * Copyright (C) 2006 Damian Hamill and individual contributors as indicated
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.ribax.swing.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Cursor;
import java.util.ArrayList;
import java.util.Enumeration;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.ToolTipManager;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import org.jdom.Element;

import utils.log.BasicLogger;
import utils.xml.XMLutils;

import org.ribax.common.Messages;
import org.ribax.swing.RIBAXApplet;
import org.ribax.swing.parameters.ParameterSet;

/**
 * RootFolders are used to break an application into distinct segments
 * which are displayed simultaneously in a JSplitPane.  Each RootFolder can contain all
 * the elements of a RIBAX GUI including the expandable folder tree.
 *
 * @version <tt>$Revision: $</tt>
 * @author  <a href="mailto:damian@ribax.org">Damian Hamill</a>
 */
public class RootFolder extends JPanel implements TabButtonListener {

    public static final long serialVersionUID = 1;
    private static final String BUNDLE_NAME = "org.ribax.swing.ui.messages"; //$NON-NLS-1$
    private static BasicLogger LOG = new BasicLogger(RootFolder.class.getName());
    /** An expandable JTree which may be displayed in the left side of the JSplitPane */
    protected JTree tree;
    /** The root node in the tree which contains a handle to the root Folder 
     * 
     * @see org.ribax.swing.ui.Folder
     * */
    protected DefaultMutableTreeNode rootNode = null;
    /** The right side of a JSplitPane where the selected Folder is displayed */
    protected JPanel mainPanel = new JPanel();
    /** Each RootFolder can have a set of global parameters */
    private ParameterSet globalParams = null;
    /** Each RootFolder can have a button panel */
    private ButtonPanel buttonPanel = null;
    private RIBAXApplet applet = null;
    /** Flags whether the root node in the tree is visible */
    private boolean rootIsVisible = true;
    /** Flags whether to display handles in the expandable JTree */
    private boolean showHandles = true;
    /** The Folder that is currently displayed */
    private Object nodeInfo = null;
    /** The name of the root folder  */
    protected String name = ""; //$NON-NLS-1$
    /** The displayable title  */
    protected String title = ""; //$NON-NLS-1$
    /** A short text description  */
    protected String description = null;
    /** Tooltip text for the root folder panel */
    protected String tooltip = null;

    /**
     * Constructor that creates a RootFolder using the given Element tree as a description
     * of the contents.
     * 
     * @param applet the parent applet
     * @param root the Element tree containing the description of the contents of 
     * this RootFolder
     */
    public RootFolder(RIBAXApplet applet, Element root) {
        this.applet = applet;

        // use BorderLayout for this and the mainPanel
        // Borderlayout.CENTRE ensures the components expand to fit the available space
        mainPanel.setLayout(new BorderLayout());
        setLayout(new BorderLayout());

        // read the description of this RootFolder
        rootNode = readDescription(root);

        // layout the RootFolder GUI components
        layoutComponents();
    }

    /**
     * Read the description of this RootFolder into a Folder and create the GUI 
     * components of the Folder.
     * 
     * @param node  the element tree containing the Folder description
     * @return a node that is the root node in an expandable JTree
     */
    private DefaultMutableTreeNode readDescription(Element node) {
        Element e;

        // create a new Folder that contains the contents of this RootFolder
        Folder f = new Folder(null);

        // get some basic attributes
        name = XMLutils.getElementString("name", node); //$NON-NLS-1$
        title = XMLutils.getElementString("title", node); //$NON-NLS-1$
        description = XMLutils.getElementString("description", node); //$NON-NLS-1$
        tooltip = XMLutils.getElementString("tooltip", node); //$NON-NLS-1$

        // read any global parameters
        if ((e = node.getChild("parameters")) != null) { //$NON-NLS-1$
            globalParams = ParameterSet.readParameters(e);
            ParameterSet.globalParameterSet = globalParams;
        }

        // read a button panel
        if ((e = node.getChild("buttons")) != null) { //$NON-NLS-1$
            buttonPanel = new ButtonPanel(this);
            buttonPanel.readButtons(e);
        }

        // is the root node visible
        Boolean bval = XMLutils.getElementBoolean("visible", node); //$NON-NLS-1$
        if (bval != null) {
            rootIsVisible = bval.booleanValue();
        }

        // are the tree handles visible
        bval = XMLutils.getElementBoolean("showHandles", node); //$NON-NLS-1$
        if (bval != null) {
            showHandles = bval.booleanValue();
        }


        // create a new JTree node with the Folder as the data object
        DefaultMutableTreeNode newnode = new DefaultMutableTreeNode(f);

        // read the Folder from the element tree
        f.readFolder(newnode, node);

        return newnode;
    }

    /**
     * Layout the GUI components for this RootFolder.  If the tree has more than 1 node then 
     * we need to display the tree as well as the main panel.  However if the tree has only 1
     * node then there is no point displaying the tree as only 1 node can be selected so just
     * display the main panel as the contents of this RootFolder and dispense with the
     * JTree.
     * 
     */
    private void layoutComponents() {
        if (rootNode == null) {
            return;
        }
        if (rootNode.getChildCount() > 0) {
            layoutWithFolderTree();
        } else {
            layoutSingleFolder();
        }
    }

    /**
     * Layout the GUI components for this RootFolder with an expandable JTree on the left
     * of a SplitPane and the main panel on the right.
     */
    private void layoutWithFolderTree() {

        // get the JTree Component
        tree = getFolderTree();

        if (rootIsVisible == false) // hide the root node
        {
            tree.setRootVisible(false);
        }

        // Enable tool tips.
        ToolTipManager.sharedInstance().registerComponent(tree);

        // embed the JTree in a scrollpane
        JScrollPane treeScrollPane = new JScrollPane(tree);

        // the left and right sides of the JSplitPane
        JComponent leftside, rightside = mainPanel;

        if (globalParams == null && buttonPanel == null) {
            //Create a split pane with the treePane on the left and the main panel on the right
            leftside = treeScrollPane;
        } else {
            /* the left side has global parameters and/or buttons so we need to create a
             * vertical split pane for the tree and the parameters/buttons            
             */
            JComponent globalParamsBox = globalParams;

            // globalparams could be null but buttonpanel could not be
            if (buttonPanel != null) {
                // add buttonPanel to a vertical box with globalParams
                Box box = Box.createVerticalBox();

                if (globalParams != null) {
                    box.add(globalParams);
                    box.add(Box.createVerticalGlue());
                }
                box.add(buttonPanel);

                globalParamsBox = box;
            }

            // create the vertical split pane
            leftside = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
                    treeScrollPane, new JScrollPane(globalParamsBox));
        }

        // create the horizontal split pane with the left and right sides
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                leftside, rightside);
        splitPane.setOneTouchExpandable(true);

        // set the divider location to be 150 pixels from the left
        splitPane.setDividerLocation(150);

        // add the split pane to this component
        add(splitPane, BorderLayout.CENTER);

    }

    /**
     *  Layout the GUI components for this RootFolder with the main panel containing the 
     *  folder occupying all of the availabe space.  However if global parameters 
     *   have been defined then we use a split pane anyway.
     */
    private void layoutSingleFolder() {

        // if there are global parameters then add them to the left side
        // and create a split pane anyway
        if (globalParams == null) {
            // no parameters have been defined so use all available space for the main panel
            add(mainPanel, BorderLayout.CENTER);

            // if a set of buttons have been defined then add them to the panel
            if (buttonPanel != null) {
                add(buttonPanel, buttonPanel.getLayoutLocation());
            }
        } else {
            // create a split pane with the global parameters on the left side
            // and the main panel on the right
            JComponent globalParamsBox = globalParams;

            // if a set of buttons have been defined then add them to the left side
            if (buttonPanel != null) {
                Box box = Box.createVerticalBox();

                // add buttonPanel to a vertical box with globalParams
                box.add(globalParams);
                box.add(Box.createVerticalGlue());
                box.add(buttonPanel);

                globalParamsBox = box;
            }
            JScrollPane leftside = new JScrollPane(globalParamsBox);

            JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                    leftside, mainPanel);
            splitPane.setOneTouchExpandable(true);

            // set the divider location to be 150 pixels from the left
            splitPane.setDividerLocation(150);

            // add the split pane to this panel
            add(splitPane, BorderLayout.CENTER);
        }
    }

    /**
     * Create a new JTree using the root node as the root of the tree
     * @return the newly created JTree
     */
    private JTree getFolderTree() {

        if (rootNode == null) {
            throw new RuntimeException(Messages.getString(BUNDLE_NAME, "RootFolder.10")); //$NON-NLS-1$
        }

        final JTree tree = new JTree(rootNode);

        // this tree is not editable
        tree.setEditable(false);

        // setup some default properties
        tree.putClientProperty("JTree.lineStyle", "Angled"); //$NON-NLS-1$ //$NON-NLS-2$
        tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        tree.setShowsRootHandles(showHandles);

        // set the cell renderer to display text and/or images
        tree.setCellRenderer(new FolderIconRenderer());

        /* when the user clicks on a tree node then load the Folder for the node
         * that is clicked
         */
        tree.addTreeSelectionListener(new TreeSelectionListener() {

            public void valueChanged(TreeSelectionEvent e) {
                @SuppressWarnings("unused")
                TreePath treePath = e.getNewLeadSelectionPath();
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();

                if (node == null) {
                    return;
                }

                Object nodeInfo = node.getUserObject();
                loadNodeObject(nodeInfo);

            }
        });

        // the maximum hieght of each row in the JTree
        int maxRowHeight = -1;

        /* determine the maximum height of the nodes in the JTree by getting the 
         * maximum hieght of each Folder node in the tree.  It is helpful if using images
         * for Folder nodes that all images are the same height.
         */
        Enumeration<TreeNode> e = rootNode.breadthFirstEnumeration();
        DefaultMutableTreeNode node;

        Folder f = getFolderForNode(rootNode);

        // determine the maximum height of the root Folder node
        if (f != null) {
            ImageIcon icon;
            if ((icon = f.getFolderIcon()) != null) {
                maxRowHeight = Math.max(maxRowHeight, icon.getIconHeight());
            }
        }

        // determine the maximum height of all child nodes
        for (; e.hasMoreElements();) {
            node = (DefaultMutableTreeNode) e.nextElement();
            f = getFolderForNode(node);

            if (f != null) {
                ImageIcon icon;
                if ((icon = f.getFolderIcon()) != null) {
                    maxRowHeight = Math.max(maxRowHeight, icon.getIconHeight());
                }
            }
        }

        // set the rowheight of the JTree to the maximum of the current height and the
        // maximum Folder node height
        int rowHeight = tree.getRowHeight();
        if (maxRowHeight > rowHeight) {
            tree.setRowHeight(maxRowHeight);
        }

        return tree;
    }

    /**
     * Load the top level Folder for this RootFolder into the main panel
     */
    public void loadRootFolder() {
        // get the Folder for the root node
        Object nodeInfo = rootNode.getUserObject();

        // load it into the main panel
        loadNodeObject(nodeInfo);

        // kick off garbage collection
        System.gc();
    }

    /**
     * Load the given Folder into the main panel.
     * 
     * @param node the user object from the JTree node that should be a Folder/DataItem
     * 
     * @see org.ribax.swing.ui.Folder
     * @see org.ribax.swing.ui.DataItem
     */
    private void loadNodeObject(Object node) {

        if (node == null) {
            return;
        }

        try {
            setCursor(new Cursor(Cursor.WAIT_CURSOR));

            this.nodeInfo = node;

            if (LOG.isDebugEnabled()) {
                LOG.debug(name + Messages.getString(BUNDLE_NAME, "RootFolder.13") + nodeInfo.toString()); //$NON-NLS-1$
            }
            // the node object must be a DataItem
            if (nodeInfo instanceof DataItem) {

                // get the name or title of the Folder/DataItem
                String nodeName = ((DataItem) nodeInfo).getDataItemName();
                if (nodeName == null) {
                    nodeName = ((DataItem) nodeInfo).getTitle();
                }

                applet.reportStatus(name + Messages.getString(BUNDLE_NAME, "RootFolder.14") + nodeName + Messages.getString(BUNDLE_NAME, "RootFolder.15")); //$NON-NLS-1$ //$NON-NLS-2$

                // tell the Folder to load it's data if it hasn't already done so
                if (globalParams == null) {
                    ((DataItem) nodeInfo).loadData(null, Messages.getString(BUNDLE_NAME, "RootFolder.16")); //$NON-NLS-1$
                } else {
                    ((DataItem) nodeInfo).loadData(globalParams.getNameValuePairs(), "LoadData"); //$NON-NLS-1$
                }
                applet.reportStatus(name + Messages.getString(BUNDLE_NAME, "RootFolder.18") + nodeName); //$NON-NLS-1$
            } else {
                LOG.info(name + Messages.getString(BUNDLE_NAME, "RootFolder.19") + nodeInfo.getClass().getName()); //$NON-NLS-1$
            }

            if (LOG.isDebugEnabled()) {
                LOG.debug(name + Messages.getString(BUNDLE_NAME, "RootFolder.20") + nodeInfo.toString()); //$NON-NLS-1$
            }
        } finally {
            setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        }

        // update the UI so the selected Folder appears in the main panel
        mainPanel.removeAll();
        mainPanel.add((JComponent) nodeInfo, BorderLayout.CENTER);

        mainPanel.validate();
        mainPanel.repaint();
    }

    /**
     * Tell every item in the tree that it's data is out of date
     */
    private void invalidateItemTree() {
        Enumeration<TreeNode> e = rootNode.breadthFirstEnumeration();
        DefaultMutableTreeNode node;

        // invalidate the root object
        Object nodeInfo = rootNode.getUserObject();
        ((DataItem) nodeInfo).invalidateData();

        // invalidate all of the root object's descendents
        for (; e.hasMoreElements();) {
            node = (DefaultMutableTreeNode) e.nextElement();
            nodeInfo = node.getUserObject();
            ((DataItem) nodeInfo).invalidateData();
        }
    }

    /**
     * Tell every DataItem to stop whatever they are doing (like streaming).
     */
    public void stop() {
        Enumeration<TreeNode> e = rootNode.breadthFirstEnumeration();
        DefaultMutableTreeNode node;

        // tell the root Folder to stop
        Object nodeInfo = rootNode.getUserObject();
        ((DataItem) nodeInfo).close();

        // tell all of the root Folder's descendents to stop
        for (; e.hasMoreElements();) {
            node = (DefaultMutableTreeNode) e.nextElement();
            nodeInfo = node.getUserObject();
            ((DataItem) nodeInfo).close();
        }
    }

    /** 
     * Handle a user button click from one of the buttons on the ButtonPanel in this
     * RootFolder.
     * 
     * @see org.ribax.swing.ui.TabButtonListener#doAction(int, java.lang.String)
     */
    public void doAction(int buttonType, String action) {

        if (nodeInfo == null) {
            return;
        }

        ArrayList params = null;

        if (globalParams != null) {
            params = globalParams.getNameValuePairs();
        }

        // pass the action to the current selected Folder
        if (buttonType == TabButton.REFRESH) {
            ((DataItem) nodeInfo).invalidateData();
            ((DataItem) nodeInfo).refresh(params, action);
        } else if (buttonType == TabButton.UPDATE) {
            ((DataItem) nodeInfo).updateData(params, action);
        } else if (buttonType == TabButton.RELOAD) {
            invalidateItemTree();
            ((DataItem) nodeInfo).refresh(params, action);
        }
    }

    /**
     * A class to display an image or text string to represent a Folder in an expandable
     * Folder JTree.
     * 
     * @author damian
     *
     */
    @SuppressWarnings("serial")
    private class FolderIconRenderer extends DefaultTreeCellRenderer {

        public FolderIconRenderer() {
        }

        /* (non-Javadoc)
         * @see javax.swing.tree.DefaultTreeCellRenderer#getTreeCellRendererComponent(javax.swing.JTree, java.lang.Object, boolean, boolean, boolean, int, boolean)
         */
        public Component getTreeCellRendererComponent(
                JTree tree,
                Object value,
                boolean sel,
                boolean expanded,
                boolean leaf,
                int row,
                boolean hasFocus) {

            super.getTreeCellRendererComponent(
                    tree, value, sel,
                    expanded, leaf, row,
                    hasFocus);

            Folder f = getFolderForNode(value);

            if (f != null) {
                String folderName = f.getTitle();

                if (folderName == null || folderName.length() == 0) {
                    folderName = f.getDataItemName();
                }

                if (f.getFolderIcon() != null) {
                    setIcon(f.getFolderIcon());
                } else {
                    setText(folderName);
                }
                setToolTipText(f.getTooltip());
            } else {
                setToolTipText(null); //no tool tip
            }

            return this;
        }
    }

    /**
     * Get the Folder for the given tree node.
     * 
     * @param value the node containing the Folder
     * @return the Folder user object or null if it is not a Folder
     */
    protected static Folder getFolderForNode(Object value) {
        DefaultMutableTreeNode node =
                (DefaultMutableTreeNode) value;
        Object nodeInfo =
                (Object) (node.getUserObject());

        if (nodeInfo instanceof Folder) {
            return (Folder) nodeInfo;
        }

        return null;
    }
}
