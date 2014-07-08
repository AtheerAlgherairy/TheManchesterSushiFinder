/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.manchestersushifinder;

import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Rectangle;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.DropMode;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTree;
import javax.swing.ListCellRenderer;
import javax.swing.TransferHandler;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.w3c.dom.Element;

/**
 *
 * @author Atheer
 */
public class QueryInterface extends javax.swing.JFrame {

    public DefaultListModel IncludedList = new DefaultListModel();
    public DefaultListModel ExcludedList = new DefaultListModel();
    public JList dragFrom;
    ArrayList<String[]> arr = Global.myConfig.getBases();
    public String selectedTemplate = arr.get(0)[0]; //first Id in the list
    ResultsPanel resultsPanel;
    public int numOfRadioButtons = 0;
    public static String selectedLanguage;
    public static Sanctions mySanctions;
    Set<OWLClassExpression> additionalDescriptions;

    /**
     * Creates new form QueryInterface
     */
    //this function is to be used by (Back) button in resultsPanel  
    public void showChooserPanel() {
        ((CardLayout) mainPanel.getLayout()).first(mainPanel);
    }

    public QueryInterface() {
        selectedLanguage = "default";
        initComponents();

        additionalDescriptions = new HashSet<OWLClassExpression>();
        mySanctions = new Sanctions(Global.myOntology, Global.myConfig.getSanctions());
        //------------------ Alternative Languages Radio Buttons--------    
        //Adding Radio Buttons: 
        ButtonGroup group = new ButtonGroup();
        ArrayList<String> array = Global.myConfig.getAvailableLanguages();
        group.clearSelection();
        if (!array.isEmpty()) {

            for (int i = 0; i < array.size(); i++) {
                JRadioButton b = new JRadioButton();
                b.setName("Button_" + i);
                // Register a listener for the radio buttons.
                b.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        JRadioButton btn = (JRadioButton) e.getSource();
                        selectedLanguage = btn.getText();

                        //re-render the two lists in the query interface with the selected language
                        jList1.setCellRenderer(new OWLClassListCellRenderer(selectedLanguage));
                        jList2.setCellRenderer(new OWLClassListCellRenderer(selectedLanguage));

                        //re-render the two lists in the faceted search panel with the selected language
                        FacetedSearchPanel.ingredientsList.setCellRenderer(new OWLClassListCellRenderer(selectedLanguage));
                        FacetedSearchPanel.CharacteristicsList.setCellRenderer(new CheckListRenderer());

                        //re-render the list in the direct search panel with the selected language
                        DirectSearchPanel.componentsList.setCellRenderer(new OWLClassListCellRenderer(selectedLanguage));


                        //re-render the new faceted search:
                        StartFrame.fourthView.addFactesCheckboxes();
                        //************************************************



                        //re-render the tree with the selected language
                        DefaultTreeCellRenderer renderer = new OWLClassTreeCellRenderer(selectedLanguage);
                        java.net.URL imgURL1 = QueryInterface.class.getResource("/icon1.png");
                        java.net.URL imgURL2 = QueryInterface.class.getResource("/icon2.png");
                        ImageIcon closedIcon = new ImageIcon(imgURL1);
                        ImageIcon openIcon = new ImageIcon(imgURL2);
                        renderer.setClosedIcon(closedIcon);
                        renderer.setOpenIcon(openIcon);
                        renderer.setLeafIcon(openIcon);

                        StartFrame.myTree.setCellRenderer(renderer);

                        BrowsingPanel.validate();


                    }
                });
                b.setText(array.get(i));

                group.add(b);
                if (i == 0) {
                    b.setSelected(true);
                }
                languagePanel.add(b);
                languagePanel.validate();
                numOfRadioButtons++;
            }

        } else {
            languagePanel.setVisible(false);
        }



//27 April--------Add data listener--------------

        IncludedList.addListDataListener(new ListDataListener() {
            public void contentsChanged(ListDataEvent e) {
                updateNumberOfResults();
            }

            public void intervalAdded(ListDataEvent e) {
                updateNumberOfResults();


                //------20 June---------------------
                Object value = IncludedList.getElementAt(IncludedList.getSize() - 1);
                String str = value.toString().substring(1, value.toString().length() - 1);
                IRI classIRI = IRI.create(str);
                OWLClass cls = Global.myOntology.getDf().getOWLClass(classIRI);
                getReasonableOptions(cls, "included");
                //--------------------------------------

            }

            public void intervalRemoved(ListDataEvent e) {
                updateNumberOfResults();
            }
        });

        ExcludedList.addListDataListener(new ListDataListener() {
            public void contentsChanged(ListDataEvent e) {
                updateNumberOfResults();
            }

            public void intervalAdded(ListDataEvent e) {
                updateNumberOfResults();


                //------20 June---------------------
                Object value = ExcludedList.getElementAt(ExcludedList.getSize() - 1);
                String str = value.toString().substring(1, value.toString().length() - 1);
                IRI classIRI = IRI.create(str);
                OWLClass cls = Global.myOntology.getDf().getOWLClass(classIRI);
                getReasonableOptions(cls, "excluded");
                //--------------------------------------
            }

            public void intervalRemoved(ListDataEvent e) {
                updateNumberOfResults();
            }
        });
//-----------------------------------------------




        //1- JList1 is the included list view.
        //2- Allow dropping to the list and deleting using delete key. 
        //3- Set Custom cell renderer of how cells will appear to the user

        this.jList1.setModel(IncludedList);
        this.jList1.setTransferHandler(new ToTransferHandler(TransferHandler.COPY));
        this.jList1.setDropMode(DropMode.INSERT);
        this.jList1.setDragEnabled(true);
        ListCellRenderer myRenderer = new OWLClassListCellRenderer(selectedLanguage);

        this.jList1.setCellRenderer(myRenderer);


        this.jList1.addKeyListener(new KeyListener() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_DELETE) {
                    if (IncludedList.getSize() != 0) {

                        if (jList1.getSelectedIndex() != -1) {
                            IncludedList.remove(jList1.getSelectedIndex());
                            jList1.revalidate();
                            jList1.repaint();
                        }
                    }
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }

            @Override
            public void keyTyped(KeyEvent e) {
            }
        });

        //1- JList2 is the excluded list view.
        //2- Allow dropping to the list and deleting using delete key.
        //3- Set Custom cell renderer of how cells will appear to the user
        this.jList2.setModel(ExcludedList);
        this.jList2.setTransferHandler(new ToTransferHandler(TransferHandler.COPY));
        this.jList2.setDropMode(DropMode.INSERT);

        this.jList2.setCellRenderer(myRenderer);
        this.jList2.addKeyListener(new KeyListener() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_DELETE) {

                    if (!ExcludedList.isEmpty()) {

                        if (jList2.getSelectedIndex() != -1) {
                            ExcludedList.remove(jList2.getSelectedIndex());
                            jList2.revalidate();
                            jList2.repaint();
                        }
                    }
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }

            @Override
            public void keyTyped(KeyEvent e) {
            }
        });




        //----------------------------------------------------
        //from configurations, set the radio buttons based on available query templates.
        // ArrayList<String> array = GLobal.myConfig.getBases();

        ButtonGroup group2 = new ButtonGroup();

        for (int i = 0; i < arr.size(); i++) {
            JRadioButton b = new JRadioButton();
            b.setName("Button_" + arr.get(i)[0]); //first column in each row=t emplate ID
            b.addActionListener(listener2);
            b.setText(arr.get(i)[1]); //Second column in each row= template name
            // b.setFont(new java.awt.Font("Tahoma", 1, 14));
            group2.add(b);
            if (i == 0) {
                b.setSelected(true);
            }
            this.RadioButtonsPanel.add(b);
            this.RadioButtonsPanel.validate();
        }


    }
    ActionListener listener2 = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {

            JRadioButton btn = (JRadioButton) e.getSource();
            selectedTemplate = btn.getName().substring(btn.getName().indexOf("_") + 1);

            updateNumberOfResults();

        }
    };

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        HeaderPanel = new javax.swing.JPanel();
        TitleLabel = new java.awt.Label();
        ImageLabel = new javax.swing.JLabel();
        mainPanel = new javax.swing.JPanel();
        ChooserPanel = new javax.swing.JPanel();
        ContentPanel = new javax.swing.JPanel();
        jSplitPane1 = new javax.swing.JSplitPane();
        BrowsingPanel = new javax.swing.JPanel();
        SelectedThingsPanel = new javax.swing.JPanel();
        IncludedPanel = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        includedRemoveKey = new javax.swing.JButton();
        ExcludedPanel = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jList2 = new javax.swing.JList();
        excludedRemoveKey = new javax.swing.JButton();
        FooterPanel = new javax.swing.JPanel();
        RadioButtonsPanel = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        languagePanel = new javax.swing.JPanel();
        noOfResultsLabel = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu2 = new javax.swing.JMenu();
        jMenuItem3 = new javax.swing.JMenuItem();
        jMenuItem4 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        jMenuItem1 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Query Interface");
        setAlwaysOnTop(true);
        setBackground(new java.awt.Color(0, 0, 0));

        HeaderPanel.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        TitleLabel.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        TitleLabel.setText("Resturant Name");

        javax.swing.GroupLayout HeaderPanelLayout = new javax.swing.GroupLayout(HeaderPanel);
        HeaderPanel.setLayout(HeaderPanelLayout);
        HeaderPanelLayout.setHorizontalGroup(
            HeaderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(HeaderPanelLayout.createSequentialGroup()
                .addComponent(ImageLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(TitleLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 596, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(30, Short.MAX_VALUE))
        );
        HeaderPanelLayout.setVerticalGroup(
            HeaderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(ImageLabel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, HeaderPanelLayout.createSequentialGroup()
                .addContainerGap(20, Short.MAX_VALUE)
                .addComponent(TitleLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(21, 21, 21))
        );

        mainPanel.setLayout(new java.awt.CardLayout());

        ContentPanel.setLayout(new java.awt.BorderLayout());

        BrowsingPanel.setPreferredSize(new java.awt.Dimension(59, 17));
        BrowsingPanel.setLayout(new java.awt.BorderLayout(7, 7));
        jSplitPane1.setLeftComponent(BrowsingPanel);

        SelectedThingsPanel.setMinimumSize(new java.awt.Dimension(300, 480));
        SelectedThingsPanel.setLayout(new javax.swing.BoxLayout(SelectedThingsPanel, javax.swing.BoxLayout.Y_AXIS));

        IncludedPanel.setPreferredSize(new java.awt.Dimension(450, 170));

        jScrollPane1.setMaximumSize(new java.awt.Dimension(129, 420));
        jScrollPane1.setMinimumSize(new java.awt.Dimension(129, 420));
        jScrollPane1.setPreferredSize(new java.awt.Dimension(129, 420));

        jList1.setDragEnabled(true);
        jList1.setDropMode(javax.swing.DropMode.ON_OR_INSERT);
        jList1.setSelectionBackground(new java.awt.Color(234, 231, 231));
        jList1.setSelectionForeground(new java.awt.Color(0, 0, 0));
        jList1.setVerifyInputWhenFocusTarget(false);
        jScrollPane1.setViewportView(jList1);

        includedRemoveKey.setText("Rem");
        includedRemoveKey.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                includedRemoveKeyActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout IncludedPanelLayout = new javax.swing.GroupLayout(IncludedPanel);
        IncludedPanel.setLayout(IncludedPanelLayout);
        IncludedPanelLayout.setHorizontalGroup(
            IncludedPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, IncludedPanelLayout.createSequentialGroup()
                .addContainerGap(18, Short.MAX_VALUE)
                .addGroup(IncludedPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(includedRemoveKey)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 444, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(251, 251, 251))
        );
        IncludedPanelLayout.setVerticalGroup(
            IncludedPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(IncludedPanelLayout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(includedRemoveKey)
                .addContainerGap(42, Short.MAX_VALUE))
        );

        SelectedThingsPanel.add(IncludedPanel);

        ExcludedPanel.setPreferredSize(new java.awt.Dimension(450, 170));

        jScrollPane2.setMaximumSize(new java.awt.Dimension(129, 420));
        jScrollPane2.setMinimumSize(new java.awt.Dimension(129, 420));
        jScrollPane2.setPreferredSize(new java.awt.Dimension(420, 129));

        jList2.setDragEnabled(true);
        jList2.setDropMode(javax.swing.DropMode.ON_OR_INSERT);
        jList2.setSelectionBackground(new java.awt.Color(234, 231, 231));
        jList2.setSelectionForeground(new java.awt.Color(0, 0, 0));
        jScrollPane2.setViewportView(jList2);

        excludedRemoveKey.setText("Rem");
        excludedRemoveKey.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                excludedRemoveKeyActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout ExcludedPanelLayout = new javax.swing.GroupLayout(ExcludedPanel);
        ExcludedPanel.setLayout(ExcludedPanelLayout);
        ExcludedPanelLayout.setHorizontalGroup(
            ExcludedPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, ExcludedPanelLayout.createSequentialGroup()
                .addContainerGap(18, Short.MAX_VALUE)
                .addGroup(ExcludedPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(excludedRemoveKey)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 444, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(251, 251, 251))
        );
        ExcludedPanelLayout.setVerticalGroup(
            ExcludedPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, ExcludedPanelLayout.createSequentialGroup()
                .addContainerGap(42, Short.MAX_VALUE)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(excludedRemoveKey)
                .addGap(10, 10, 10))
        );

        SelectedThingsPanel.add(ExcludedPanel);

        jSplitPane1.setRightComponent(SelectedThingsPanel);

        ContentPanel.add(jSplitPane1, java.awt.BorderLayout.CENTER);

        RadioButtonsPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Base Thing(s)"));
        RadioButtonsPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        jButton1.setText("Results");
        jButton1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        languagePanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Alternative Language(s)"));
        languagePanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        noOfResultsLabel.setText("       ");
        noOfResultsLabel.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        noOfResultsLabel.setMaximumSize(new java.awt.Dimension(25, 16));
        noOfResultsLabel.setMinimumSize(new java.awt.Dimension(25, 16));

        javax.swing.GroupLayout FooterPanelLayout = new javax.swing.GroupLayout(FooterPanel);
        FooterPanel.setLayout(FooterPanelLayout);
        FooterPanelLayout.setHorizontalGroup(
            FooterPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(FooterPanelLayout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(FooterPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(RadioButtonsPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 662, Short.MAX_VALUE)
                    .addComponent(languagePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 18, Short.MAX_VALUE)
                .addGroup(FooterPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, 89, Short.MAX_VALUE)
                    .addComponent(noOfResultsLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        FooterPanelLayout.setVerticalGroup(
            FooterPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(FooterPanelLayout.createSequentialGroup()
                .addGroup(FooterPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(FooterPanelLayout.createSequentialGroup()
                        .addComponent(RadioButtonsPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(languagePanel, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(FooterPanelLayout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(noOfResultsLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(0, 0, 0))
        );

        javax.swing.GroupLayout ChooserPanelLayout = new javax.swing.GroupLayout(ChooserPanel);
        ChooserPanel.setLayout(ChooserPanelLayout);
        ChooserPanelLayout.setHorizontalGroup(
            ChooserPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ChooserPanelLayout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(ChooserPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(ContentPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addGroup(ChooserPanelLayout.createSequentialGroup()
                        .addComponent(FooterPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        ChooserPanelLayout.setVerticalGroup(
            ChooserPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ChooserPanelLayout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(ContentPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 414, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(FooterPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 8, Short.MAX_VALUE))
        );

        mainPanel.add(ChooserPanel, "card2");

        jMenu2.setText("Sushi Finder");

        jMenuItem3.setText("New Configuration File");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem3);

        jMenuItem4.setText("Modify Configuration File");
        jMenuItem4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem4ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem4);

        jMenuItem2.setText("Select Configuration File");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem2);
        jMenu2.add(jSeparator1);

        jMenuItem1.setText("About...");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem1);

        jMenuBar1.add(jMenu2);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(HeaderPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 13, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addComponent(mainPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)))
                .addGap(20, 20, 20))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(1, 1, 1)
                .addComponent(HeaderPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(mainPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 546, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed


        //Which Radio Button is selected?
        Element templateElement = Global.myConfig.getNodeWithAttribute(Global.myConfig.getRootElement(), "Id", selectedTemplate);
        QueryTemplateEngine myEngine = new QueryTemplateEngine(IncludedList, ExcludedList, templateElement, Global.myOntology.getDf(), Global.myOntology.getOntologyIRI(), Global.myOntology.getReasoner());
        Collection<OWLClass> resultCollection = myEngine.getQueryResults();


        //--------------Check Whether the result class is expect to appear or not?---------
        //------------- Using the configuration element (DontShowResult)
       /* String[] dontShowAnnotation = Global.myConfig.getDontShowResultAnnotationIRIAndValue();

         if (dontShowAnnotation != null) {

         OWLLiteral val = null;
         Collection<OWLClass> toBeRemoved = new ArrayList<OWLClass>();

         for (Iterator<OWLClass> it = resultCollection.iterator(); it.hasNext();) {
         OWLClass cl = it.next();
         IRI annotationIRI = IRI.create(dontShowAnnotation[0]);
         for (OWLAnnotation ann : cl.getAnnotations(Global.myOntology.getOntology(), Global.myOntology.getDf().getOWLAnnotationProperty(annotationIRI))) {
         val = (OWLLiteral) ann.getValue();
         if (val.getLiteral().equals(dontShowAnnotation[1])) {
         toBeRemoved.add(cl);

                      

         break;
         }
         }

         }

         resultCollection.removeAll(toBeRemoved);
         } */


        ArrayList<IRI> dontShowClasses = Global.myConfig.getExcludedClassesFromResults();
        if (dontShowClasses != null) {

            Collection<OWLClass> toBeRemoved = new ArrayList<OWLClass>();

            for (Iterator<OWLClass> it = resultCollection.iterator(); it.hasNext();) {
                OWLClass cl = it.next();

                for (int j = 0; j < dontShowClasses.size(); j++) {
                    if (dontShowClasses.get(j).equals(cl.getIRI())) {
                        toBeRemoved.add(cl);
                        break;
                    }
                }
            }
            resultCollection.removeAll(toBeRemoved);
        }

        //---------------------------------------------------------
        if (myEngine.getSatisfiable()) {

            //---------fill the available Results Characteristics from config.xml--------
            ArrayList<OWLClass> ResultsCharacteristics = new ArrayList<OWLClass>();
            ResultsCharacteristics = Global.myConfig.getResultCharacteristics();
            ResultsPanel myPanel = new ResultsPanel(myEngine, ResultsCharacteristics, this);

            try {
                myPanel.setBasePanels(resultCollection);
            } catch (MalformedURLException ex) {
                Logger.getLogger(QueryInterface.class.getName()).log(Level.SEVERE, null, ex);

            }

            mainPanel.add(myPanel);
            ((CardLayout) mainPanel.getLayout()).last(mainPanel);
            mainPanel.validate();


        } else {
            JOptionPane.showMessageDialog(this, "Sorry. This is unsatisfiable ..\n", "Query Error", JOptionPane.ERROR_MESSAGE);

        }
    }//GEN-LAST:event_jButton1ActionPerformed
//Extensional Feedback---------------------------------    

    public void updateNumberOfResults() {
        Element templateElement = Global.myConfig.getNodeWithAttribute(Global.myConfig.getRootElement(), "Id", selectedTemplate);
        QueryTemplateEngine myEngine = new QueryTemplateEngine(IncludedList, ExcludedList, templateElement, Global.myOntology.getDf(), Global.myOntology.getOntologyIRI(), Global.myOntology.getReasoner());
        Collection<OWLClass> resultCollection = myEngine.getQueryResults();

        //--------------Check Whether the result class is expect to appear or not?---------
        //------------- Using the configuration element (DontShowResult)
       /* String[] dontShowAnnotation = Global.myConfig.getDontShowResultAnnotationIRIAndValue();

         if (dontShowAnnotation != null) {
         OWLLiteral val = null;
         Collection<OWLClass> toBeRemoved = new ArrayList<OWLClass>();

         for (Iterator<OWLClass> it = resultCollection.iterator(); it.hasNext();) {
         OWLClass cl = it.next();
         IRI annotationIRI = IRI.create(dontShowAnnotation[0]);
         for (OWLAnnotation ann : cl.getAnnotations(Global.myOntology.getOntology(), Global.myOntology.getDf().getOWLAnnotationProperty(annotationIRI))) {
         val = (OWLLiteral) ann.getValue();
         if (val.getLiteral().equals(dontShowAnnotation[1])) {
         toBeRemoved.add(cl);

         //resultCollection.remove(cl);

         break;
         }
         }
         }
         resultCollection.removeAll(toBeRemoved);
         }*/

        ArrayList<IRI> dontShowClasses = Global.myConfig.getExcludedClassesFromResults();
        if (dontShowClasses != null) {

            Collection<OWLClass> toBeRemoved = new ArrayList<OWLClass>();

            for (Iterator<OWLClass> it = resultCollection.iterator(); it.hasNext();) {
                OWLClass cl = it.next();

                for (int j = 0; j < dontShowClasses.size(); j++) {
                    if (dontShowClasses.get(j).equals(cl.getIRI())) {
                        toBeRemoved.add(cl);
                        break;
                    }
                }
            }
            resultCollection.removeAll(toBeRemoved);
        }
        //-------------------------------------------------------------------------------
        this.noOfResultsLabel.setText("Results (" + resultCollection.size() + ")");
    }
    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed

        AboutDialog dialog = new AboutDialog(this);
        dialog.validate();
        dialog.setVisible(true);

        // TODO add your handling code here:
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed


        NewConfigurationFileDialog newDoalog = new NewConfigurationFileDialog(this, false);
        newDoalog.setVisible(true);
    }//GEN-LAST:event_jMenuItem3ActionPerformed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed

        SelectConfigurationFileDialog selectDoalog = new SelectConfigurationFileDialog(this, false, true);
        selectDoalog.setVisible(true);
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void includedRemoveKeyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_includedRemoveKeyActionPerformed

        if (!IncludedList.isEmpty()) {

            if (jList1.getSelectedIndex() != -1) {
                IncludedList.remove(jList1.getSelectedIndex());
                jList1.revalidate();
                jList1.repaint();
            }
        }
    }//GEN-LAST:event_includedRemoveKeyActionPerformed

    //-------20 june--------------
    void getReasonableOptions(OWLClass cls, String type) {

        String currentClassName = Global.myOntology.getOWLClassAlternativeLanguage(cls, QueryInterface.selectedLanguage);

        mySanctions.calculateSetOfReasonableClasses(cls);
        int num = mySanctions.getSetOfR().size();

        if (num != 0) {
            JCheckBox[] checkboxes = new JCheckBox[num];
            String[][] arr = new String[num][2];

            for (int i = 0; i < num; i++) {
                OWLClass c = mySanctions.getSetOfR().get(i);
                OWLClassExpression expr = mySanctions.getExpressions().get(i);
                String className = Global.myOntology.getOWLClassAlternativeLanguage(c, QueryInterface.selectedLanguage);

                arr[i][0] = expr.toString();
                //arr[i][1] = className.toString();

                arr[i][1] = c.toString();

            }

            Arrays.sort(arr, new ColumnComparator(0));


            JPanel[] listOfPanels = new JPanel[mySanctions.getListOfProperties().size()];

            final Set<OWLClassExpression> selectedClasses = new HashSet<OWLClassExpression>();
            for (int i = 0; i < mySanctions.getListOfProperties().size(); i++) {

                final OWLObjectProperty prop = mySanctions.getListOfProperties().get(i);
                JLabel propName = new JLabel(Global.myOntology.getOWLObjectPropertyAlternativeLanguage(prop, QueryInterface.selectedLanguage));
                JPanel newPanel = new JPanel();
                newPanel.add(propName);

                JComboBox combo = new JComboBox();
                combo.addItem("#Nonee");

                for (int k = 0; k < num; k++) {
                    if (arr[k][0].contains(prop.toString())) {
                        combo.addItem(arr[k][1]);
                    }
                }

                combo.addItemListener(new ItemListener() {
                    public void itemStateChanged(ItemEvent arg0) {
                        if (arg0.getStateChange() == ItemEvent.SELECTED) {
                            Object item = arg0.getItem();

                            if (!item.equals("#Nonee")) {
                                String myClassStr = item.toString().substring(item.toString().lastIndexOf("<") + 1, item.toString().lastIndexOf(">"));
                                IRI classIRI = IRI.create(myClassStr);
                                OWLClass myClass = Global.myOntology.getDf().getOWLClass(classIRI);

                                // add the descriptions   e.g (hasCuttingStyle some sliced)
                                selectedClasses.add(Global.myOntology.getDf().getOWLObjectSomeValuesFrom(prop, myClass));

                            }
                        }
                        if (arg0.getStateChange() == ItemEvent.DESELECTED) {
                            Object item = arg0.getItem();
                            if (!item.equals("#Nonee")) {
                                String myClassStr = item.toString().substring(item.toString().lastIndexOf("<") + 1, item.toString().lastIndexOf(">"));
                                IRI classIRI = IRI.create(myClassStr);
                                OWLClass myClass = Global.myOntology.getDf().getOWLClass(classIRI);

                                // remove the descriptions   e.g (hasCuttingStyle some sliced)
                                selectedClasses.remove(Global.myOntology.getDf().getOWLObjectSomeValuesFrom(prop, myClass));

                            }
                        }

                    }
                });
                combo.setName(prop.toString());
                combo.setRenderer(
                        new OWLClassListCellRenderer(selectedLanguage));
                if (combo.getItemCount() > 1) {
                    newPanel.add(combo);
                    listOfPanels[i] = newPanel;
                }
            }



            String message = "Do you want to specify further characteristics for the " + currentClassName + " ?";
            Object[] params = {message, listOfPanels};
            int n = JOptionPane.showConfirmDialog(this, params, "Further characteristics", JOptionPane.YES_NO_OPTION);


            if (n == 0) { //yes?

                if (!selectedClasses.isEmpty()) {
                    // add the ingredient (e.g salmon)
                    selectedClasses.add(cls);

                    OWLClassExpression resultExpr = Global.myOntology.getDf().getOWLObjectIntersectionOf(selectedClasses);


                    if (type.equalsIgnoreCase("included")) {
                        IncludedList.remove(IncludedList.getSize() - 1);
                        IncludedList.addElement(resultExpr);
                    } else {
                        ExcludedList.remove(ExcludedList.getSize() - 1);
                        ExcludedList.addElement(resultExpr);
                    }
                }
            }

        }


    }

    private void excludedRemoveKeyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_excludedRemoveKeyActionPerformed

        if (!ExcludedList.isEmpty()) {

            if (jList2.getSelectedIndex() != -1) {
                ExcludedList.remove(jList2.getSelectedIndex());
                jList2.revalidate();
                jList2.repaint();


            }
        }
    }//GEN-LAST:event_excludedRemoveKeyActionPerformed

    private void jMenuItem4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem4ActionPerformed
        SelectConfigurationFileDialog selectDoalog = new SelectConfigurationFileDialog(this, false, false);
        selectDoalog.setVisible(true);



    }//GEN-LAST:event_jMenuItem4ActionPerformed

    /**
     * @param args the command line arguments
     */
    public class OWLClassTreeCellRenderer extends DefaultTreeCellRenderer {

        String lang;

        public OWLClassTreeCellRenderer(String lang) {
            this.lang = lang;
        }

        @Override
        public Component getTreeCellRendererComponent(JTree tree,
                Object value,
                boolean sel,
                boolean expanded,
                boolean leaf,
                int row,
                boolean hasFocus) {
            JLabel label = (JLabel) super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
            Object obj = ((DefaultMutableTreeNode) value).getUserObject();
            if (obj instanceof OWLClass) {
                label.setText(Global.myOntology.getOWLClassAlternativeLanguage((OWLClass) obj, lang));
            } else {
                label.setText(value.toString());
            }
            return label;
        }
    }
// public static void main(String[] args) {
//}
    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JPanel BrowsingPanel;
    public javax.swing.JPanel ChooserPanel;
    public javax.swing.JPanel ContentPanel;
    public javax.swing.JPanel ExcludedPanel;
    public javax.swing.JPanel FooterPanel;
    public javax.swing.JPanel HeaderPanel;
    public javax.swing.JLabel ImageLabel;
    public javax.swing.JPanel IncludedPanel;
    public javax.swing.JPanel RadioButtonsPanel;
    private javax.swing.JPanel SelectedThingsPanel;
    public java.awt.Label TitleLabel;
    private javax.swing.JButton excludedRemoveKey;
    private javax.swing.JButton includedRemoveKey;
    private javax.swing.JButton jButton1;
    public final javax.swing.JList jList1 = new javax.swing.JList();
    private javax.swing.JList jList2;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JPopupMenu.Separator jSeparator1;
    public javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JPanel languagePanel;
    public javax.swing.JPanel mainPanel;
    public javax.swing.JLabel noOfResultsLabel;
    // End of variables declaration//GEN-END:variables
}

//Transfer Handler for dropping items into lists.
class ToTransferHandler extends TransferHandler {

    int action;

    public ToTransferHandler(int action) {
        this.action = action;
    }

    public boolean canImport(TransferHandler.TransferSupport support) {

        // for the demo, we'll only support drops (not clipboard paste)
        if (!support.isDrop()) {
            return false;
        }

        // we only import Strings
        if (!support.isDataFlavorSupported(DataFlavor.stringFlavor)) {
            return false;
        }

        boolean actionSupported = (action & support.getSourceDropActions()) == action;
        if (actionSupported) {
            support.setDropAction(action);
            return true;
        }

        return false;
    }

    public boolean importData(TransferHandler.TransferSupport support) {
        // if we can't handle the import, say so
        if (!canImport(support)) {
            return false;
        }

        // fetch the drop location
        JList.DropLocation dl = (JList.DropLocation) support.getDropLocation();

        int index = dl.getIndex();

        // fetch the data and bail if this fails
        String data;
        try {
            data = (String) support.getTransferable().getTransferData(DataFlavor.stringFlavor);
        } catch (UnsupportedFlavorException e) {
            return false;
        } catch (java.io.IOException e) {
            return false;
        }

        JList list = (JList) support.getComponent();
        DefaultListModel model = (DefaultListModel) list.getModel();
        model.insertElementAt(data, index);

        Rectangle rect = list.getCellBounds(index, index);
        list.scrollRectToVisible(rect);
        list.setSelectedIndex(index);
        list.requestFocusInWindow();


        return true;


    }
}

class OWLClassListCellRenderer implements ListCellRenderer {

    String lang;

    OWLClassListCellRenderer(String lang) {
        this.lang = lang;
    }
    protected DefaultListCellRenderer defaultRenderer = new DefaultListCellRenderer();

    public Component getListCellRendererComponent(JList list, Object value, int index,
            boolean isSelected, boolean cellHasFocus) {

        JLabel rendererLabel = (JLabel) defaultRenderer.getListCellRendererComponent(list, value, index,
                isSelected, cellHasFocus);

        if (value.toString().contains("ObjectIntersectionOf") || value.toString().contains("ObjectSomeValuesFrom")) {

            String resultLabel = "";
            int startPosition = value.toString().indexOf("ObjectIntersectionOf(<") + 22;
            int lastPosition = value.toString().indexOf(">");
            String ingredientClass = value.toString().substring(startPosition, lastPosition);

            IRI ingredientClassIRI = IRI.create(ingredientClass);
            OWLClass currentClass = Global.myOntology.getDf().getOWLClass(ingredientClassIRI);
            resultLabel += Global.myOntology.getOWLClassAlternativeLanguage(currentClass, lang) + " (";

            //-----------------------------------------
            startPosition += ingredientClass.length();
            lastPosition = value.toString().lastIndexOf(")");
            String newString = value.toString().substring(startPosition, lastPosition);

            String[] parts = newString.split("ObjectSomeValuesFrom");
            for (int i = 0; i < parts.length; i++) {
                if (parts[i].length() > 2) {

                    startPosition = parts[i].indexOf("<") + 1;
                    lastPosition = parts[i].indexOf(">");
                    String propStr = parts[i].substring(startPosition, lastPosition);

                    IRI propIRI = IRI.create(propStr);
                    OWLObjectProperty prop = Global.myOntology.getDf().getOWLObjectProperty(propIRI);

                    resultLabel += " " + Global.myOntology.getOWLObjectPropertyAlternativeLanguage(prop, lang) + ": ";

                    startPosition = parts[i].lastIndexOf("<") + 1;
                    lastPosition = parts[i].lastIndexOf(">");
                    String classStr = parts[i].substring(startPosition, lastPosition);

                    IRI classIRI = IRI.create(classStr);
                    OWLClass c = Global.myOntology.getDf().getOWLClass(classIRI);
                    resultLabel += Global.myOntology.getOWLClassAlternativeLanguage(c, lang);
                }
            }


            rendererLabel.setText(resultLabel + ")");

        } else {
            String str = value.toString().substring(1, value.toString().length() - 1);
            IRI classIRI = IRI.create(str);

            OWLClass cls = Global.myOntology.getDf().getOWLClass(classIRI);
            rendererLabel.setText(Global.myOntology.getOWLClassAlternativeLanguage(cls, lang));

        }
        return rendererLabel;
    }
}

//Class that extends Comparator
class ColumnComparator implements Comparator {

    int columnToSort;

    ColumnComparator(int columnToSort) {
        this.columnToSort = columnToSort;
    }
    //overriding compare method

    public int compare(Object o1, Object o2) {
        String[] row1 = (String[]) o1;
        String[] row2 = (String[]) o2;
        //compare the columns to sort
        return row1[columnToSort].compareTo(row2[columnToSort]);
    }
}