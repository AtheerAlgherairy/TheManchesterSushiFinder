/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.manchestersushifinder;

import noNamespace.FinderConfigurationDocument.FinderConfiguration.AvailableLanguages;
import noNamespace.FinderConfigurationDocument.FinderConfiguration.AvailableLanguages.Language;
import noNamespace.FinderConfigurationDocument.FinderConfiguration.IngredientsClassifications.Classification;
import noNamespace.FinderConfigurationDocument.FinderConfiguration.IngredientsFacets.Facet;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import noNamespace.FinderConfigurationDocument;
import org.apache.xmlbeans.XmlException;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLObjectProperty;

/**
 *
 * @author Atheer
 */
public class ModifyConfigurationFileDialog extends javax.swing.JDialog {

    File fXmlFile;
    FinderConfigurationDocument doc;
    OntologyClass myOntology;
    DefaultListModel myClassificationModel;
     DefaultListModel myFacetsPropModel;

    public ModifyConfigurationFileDialog(java.awt.Dialog parent, boolean modal, File fXmlFile) {
        super(parent, modal);

        initComponents();
        this.fXmlFile = fXmlFile;

        //===================================
        myClassificationModel = new DefaultListModel();
       classificationList.setModel(myClassificationModel);
       ListCellRenderer myRenderer = new OWLClassListCellRenderer("default");
       classificationList.setCellRenderer(myRenderer);
              classificationList.addKeyListener(new KeyListener() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_DELETE) {
                    if (myClassificationModel.getSize() != 0) {
                        if (classificationList.getSelectedIndex() != -1) {
                            myClassificationModel.remove(classificationList.getSelectedIndex());
                            classificationList.revalidate();
                            classificationList.repaint();
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
         //===================================
        myFacetsPropModel = new DefaultListModel();
       facetsPropList.setModel(myFacetsPropModel);
      
       facetsPropList.setCellRenderer(myRenderer);
              facetsPropList.addKeyListener(new KeyListener() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_DELETE) {
                    if (myFacetsPropModel.getSize() != 0) {
                        if (facetsPropList.getSelectedIndex() != -1) {
                            myFacetsPropModel.remove(facetsPropList.getSelectedIndex());
                            facetsPropList.revalidate();
                            facetsPropList.repaint();
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
         //===================================
          
        doc = null;
        try {
            doc = FinderConfigurationDocument.Factory.parse(fXmlFile);
        } catch (XmlException ex) {
            Logger.getLogger(SelectConfigurationFileDialog.class.getName()).log(Level.SEVERE, null, ex);

        } catch (IOException ex) {
            Logger.getLogger(SelectConfigurationFileDialog.class.getName()).log(Level.SEVERE, null, ex);
        }


        if (doc != null && doc.validate()) {
            setupInterface(doc);
        }


        setupOntologyURLDocumentListner();


    }

    private void setupInterface(FinderConfigurationDocument myDoc) {

        //======================1- Appearance=================================
        includedListLabel.setText(myDoc.getFinderConfiguration().getIncludedLabel().getText());
        excludedListLabel.setText(myDoc.getFinderConfiguration().getExcludedLabel().getText());
        titleLabel.setText(myDoc.getFinderConfiguration().getTitleLabel().getText());
        logoURL.setText(myDoc.getFinderConfiguration().getLogo().getURL().toString());


        //logo Image?
        String URLString = myDoc.getFinderConfiguration().getLogo().getURL().toString().trim();
        if (!URLString.isEmpty()) {
            BufferedImage logo = null;
            try {
                URL url = new URL(URLString);
                logo = ImageIO.read(url);
            } catch (IOException ex) {
                Logger.getLogger(ModifyConfigurationFileDialog.class.getName()).log(Level.SEVERE, null, ex);
            }

            if (logo != null) {
                Image img = logo;
                BufferedImage bi = new BufferedImage(logoLabel.getWidth(), logoLabel.getHeight(), BufferedImage.TYPE_INT_ARGB);
                Graphics g = bi.createGraphics();
                g.drawImage(img, logoLabel.getX(), logoLabel.getY(), logoLabel.getWidth(), logoLabel.getHeight(), null);
                ImageIcon newIcon = new ImageIcon(bi);
                logoLabel.setIcon(newIcon);
            }
        }


        //======================2- Ontology=================================

        ontologyURL.setText(myDoc.getFinderConfiguration().getOntologyLocation().getUrl().toString());
        String defaultRenderUse = myDoc.getFinderConfiguration().getClassRendering().getUse().toString();
        for (int i = 0; i < classRenderCombo.getItemCount(); i++) {
            if (classRenderCombo.getItemAt(i).toString().equalsIgnoreCase(defaultRenderUse)) {
                classRenderCombo.setSelectedIndex(i);
            }
        }
        myOntology = new OntologyClass(ontologyURL.getText().trim(), true);
        fillAllCombos(myOntology);
        String ingredientClassIRI = myDoc.getFinderConfiguration().getIngredientClass().getClass1().toString();

        for (int i = 0; i < classesCombo.getItemCount(); i++) {
            if (classesCombo.getItemAt(i).toString().equalsIgnoreCase(ingredientClassIRI)) {
                classesCombo.setSelectedIndex(i);
            }
        }


        //Result Annotation Property
        String resultAnnoPropertyIRI = myDoc.getFinderConfiguration().getResultsAnnotationProperty().getProperty().toString();

        for (int i = 0; i < annoPropertiesCombo.getItemCount(); i++) {
            if (annoPropertiesCombo.getItemAt(i).toString().equalsIgnoreCase(resultAnnoPropertyIRI)) {
                annoPropertiesCombo.setSelectedIndex(i);
            }
        }

        //Available languages:
        Language[] langsElementsArray = myDoc.getFinderConfiguration().getAvailableLanguages().getLanguageArray();

        ArrayList<String> selectedLangNames = new ArrayList<String>();

        for (int k = 0; k < langsElementsArray.length; k++) {
            selectedLangNames.add(langsElementsArray[k].getName().toString().trim());
        }

        JList list;
        ArrayList<String> langs = new ArrayList<String>();
        langs = availableLanguages(myOntology);
        int numOfLangs = langs.size();

        //If there are languages in the ontology
        if (numOfLangs != 0) {

            CheckListItem[] myCheckList = new CheckListItem[numOfLangs];

            for (int i = 0; i < numOfLangs; i++) {

                myCheckList[i] = new CheckListItem(langs.get(i).toString());
                if (selectedLangNames.contains(langs.get(i).toString())) {
                    myCheckList[i].setSelected(true);
                }

            }
            list = new JList(myCheckList);
            list.setCellRenderer(new CheckListRenderer());
            list.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

            list.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent event) {
                    JList list = (JList) event.getSource();
                    int index = list.locationToIndex(event.getPoint());
                    CheckListItem item = (CheckListItem) list.getModel().getElementAt(index);
                    item.setSelected(!item.isSelected());
                    list.repaint(list.getCellBounds(index, index));
                }
            });


            list.validate();

            // list.setPreferredSize(new Dimension(200, 100));
            JScrollPane scrollPane = new JScrollPane(list, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
            scrollPane.setPreferredSize(new Dimension(100, 100));
            scrollPane.validate();


            languagesPanel.add(scrollPane);
            languagesPanel.doLayout();
            languagesPanel.validate();

        } else {

            JLabel myLabel = new JLabel("No available languages in the ontology..");
            languagesPanel.add(myLabel);
            languagesPanel.validate();

        }



        //======================3- different Views=================================
        //Available Ingredient classification
     
        if (myDoc.getFinderConfiguration().getIngredientsClassifications() != null) {
            Classification[] classificationElementsArray = myDoc.getFinderConfiguration().getIngredientsClassifications().getClassificationArray();
            for (int k = 0; k < classificationElementsArray.length; k++) {
                myClassificationModel.addElement("<"+classificationElementsArray[k].getClass1().toString().trim()+">");
            }
        }
        
        //Available Facets
        
             if (myDoc.getFinderConfiguration().getIngredientsFacets()!= null) {
            Facet[] facetsElementsArray = myDoc.getFinderConfiguration().getIngredientsFacets().getFacetArray();
            for (int k = 0; k < facetsElementsArray.length; k++) {
                myFacetsPropModel.addElement("<"+facetsElementsArray[k].getProperty().toString().trim()+">");
            }
        }
        
    }

    private void fillAllCombos(OntologyClass ontology) {
        annoPropertiesCombo.removeAllItems();
        classesCombo.removeAllItems();

        for (OWLAnnotationProperty ann : ontology.getOntology().getAnnotationPropertiesInSignature()) {
            annoPropertiesCombo.addItem(ann.getIRI());
        }

        DefaultListModel classesModel = new DefaultListModel();
        for (OWLClass cls : ontology.getOntology().getClassesInSignature()) {
            classesModel.addElement(cls.getIRI());

        }

        classesModel = sortListModel(classesModel);
        for (int i = 0; i < classesModel.getSize(); i++) {
            classesCombo.addItem(classesModel.get(i));
        }
    }

    private DefaultListModel sortListModel(DefaultListModel model) {
        //Sort the filteredModel :
        List<String> mylist = Collections.list(model.elements());
        Collections.sort(mylist);

        model.removeAllElements();
        for (int i = 0; i < mylist.size(); i++) {
            model.addElement(mylist.get(i));
        }
        return model;
    }

    //-----------------------------------------
    private void setupOntologyURLDocumentListner() {
        ontologyURL.getDocument().addDocumentListener(new DocumentListener() {
            public void changedUpdate(DocumentEvent e) {
                changed();
            }

            public void removeUpdate(DocumentEvent e) {
                changed();
            }

            public void insertUpdate(DocumentEvent e) {
                changed();
            }

            public void changed() {
                if (ontologyURL.getText().equals("")) {
                } else {
                }

            }
        });

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        FirstTabPanel = new javax.swing.JPanel();
        appearancePanel = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        uploadLogoButton = new javax.swing.JButton();
        titleLabel = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        logoURL = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        includedListLabel = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        excludedListLabel = new javax.swing.JTextField();
        logoLabel = new javax.swing.JLabel();
        SecondTabPanel = new javax.swing.JPanel();
        OntologyPanel = new javax.swing.JPanel();
        ontologyBasicPanel = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        classesCombo = new javax.swing.JComboBox();
        classRenderCombo = new javax.swing.JComboBox();
        ontologyURL = new javax.swing.JTextField();
        languagesPanel = new javax.swing.JPanel();
        ThirdRabPanel1 = new javax.swing.JPanel();
        IngredientsClassificationPanel = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        classificationList = new javax.swing.JList();
        addClassificationButton = new javax.swing.JButton();
        removeClassificationButton = new javax.swing.JButton();
        FacetsPropertiesPanel = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        facetsPropList = new javax.swing.JList();
        addFacetsButton = new javax.swing.JButton();
        removeFacetsButton = new javax.swing.JButton();
        ThirdTabPanel = new javax.swing.JPanel();
        QueryTemplatePanel = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        FourthTabPanel = new javax.swing.JPanel();
        ResultsPanel = new javax.swing.JPanel();
        resultsIconPanel = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        annoPropertiesCombo = new javax.swing.JComboBox();
        FifthTabPanel = new javax.swing.JPanel();
        SanctionsPanel = new javax.swing.JPanel();
        saveAsButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("New configuration file");
        setResizable(false);

        jTabbedPane1.setAutoscrolls(true);

        FirstTabPanel.setMaximumSize(new java.awt.Dimension(915, 617));

        appearancePanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Interface Appearance"));

        jLabel1.setText("Logo URL");

        uploadLogoButton.setText("Browse...");
        uploadLogoButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                uploadLogoButtonActionPerformed(evt);
            }
        });

        jLabel4.setText("Title label");

        jLabel5.setText("Included label");

        jLabel6.setText("Excluded label");

        javax.swing.GroupLayout appearancePanelLayout = new javax.swing.GroupLayout(appearancePanel);
        appearancePanel.setLayout(appearancePanelLayout);
        appearancePanelLayout.setHorizontalGroup(
            appearancePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(appearancePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(appearancePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addGroup(appearancePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(appearancePanelLayout.createSequentialGroup()
                        .addComponent(logoURL, javax.swing.GroupLayout.PREFERRED_SIZE, 411, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(41, 41, 41)
                        .addComponent(uploadLogoButton, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(119, Short.MAX_VALUE))
                    .addGroup(appearancePanelLayout.createSequentialGroup()
                        .addGroup(appearancePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(excludedListLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 213, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(titleLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 213, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(includedListLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 213, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(logoLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(157, 157, 157))))
        );
        appearancePanelLayout.setVerticalGroup(
            appearancePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(appearancePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(appearancePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(uploadLogoButton)
                    .addComponent(logoURL, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(11, 11, 11)
                .addGroup(appearancePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(appearancePanelLayout.createSequentialGroup()
                        .addComponent(logoLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(appearancePanelLayout.createSequentialGroup()
                        .addGroup(appearancePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(titleLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(appearancePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(includedListLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(11, 11, 11)
                        .addGroup(appearancePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(excludedListLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(11, 11, 11))))
        );

        javax.swing.GroupLayout FirstTabPanelLayout = new javax.swing.GroupLayout(FirstTabPanel);
        FirstTabPanel.setLayout(FirstTabPanelLayout);
        FirstTabPanelLayout.setHorizontalGroup(
            FirstTabPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(FirstTabPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(appearancePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10))
        );
        FirstTabPanelLayout.setVerticalGroup(
            FirstTabPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(FirstTabPanelLayout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addComponent(appearancePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(199, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Appearance", FirstTabPanel);

        OntologyPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Ontology"));

        jLabel7.setText("Ingredients  class");

        jLabel10.setText("Ontology location");

        jLabel25.setText("The default class rendering use:");

        classesCombo.setEditable(true);
        classesCombo.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        classesCombo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                classesComboActionPerformed(evt);
            }
        });

        classRenderCombo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "IRI", "Label" }));

        ontologyURL.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ontologyURLActionPerformed(evt);
            }
        });
        ontologyURL.addInputMethodListener(new java.awt.event.InputMethodListener() {
            public void caretPositionChanged(java.awt.event.InputMethodEvent evt) {
            }
            public void inputMethodTextChanged(java.awt.event.InputMethodEvent evt) {
                ontologyURLInputMethodTextChanged(evt);
            }
        });

        javax.swing.GroupLayout ontologyBasicPanelLayout = new javax.swing.GroupLayout(ontologyBasicPanel);
        ontologyBasicPanel.setLayout(ontologyBasicPanelLayout);
        ontologyBasicPanelLayout.setHorizontalGroup(
            ontologyBasicPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ontologyBasicPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(ontologyBasicPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(ontologyBasicPanelLayout.createSequentialGroup()
                        .addComponent(jLabel25, javax.swing.GroupLayout.PREFERRED_SIZE, 195, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(classRenderCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(ontologyBasicPanelLayout.createSequentialGroup()
                        .addGroup(ontologyBasicPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(25, 25, 25)
                        .addGroup(ontologyBasicPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(classesCombo, 0, 562, Short.MAX_VALUE)
                            .addComponent(ontologyURL))))
                .addContainerGap(23, Short.MAX_VALUE))
        );

        ontologyBasicPanelLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {classesCombo, ontologyURL});

        ontologyBasicPanelLayout.setVerticalGroup(
            ontologyBasicPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ontologyBasicPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(ontologyBasicPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ontologyURL, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(ontologyBasicPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(classesCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(15, 15, 15)
                .addGroup(ontologyBasicPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel25, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(classRenderCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        ontologyBasicPanelLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {classesCombo, ontologyURL});

        languagesPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Selected Languages"));
        languagesPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        javax.swing.GroupLayout OntologyPanelLayout = new javax.swing.GroupLayout(OntologyPanel);
        OntologyPanel.setLayout(OntologyPanelLayout);
        OntologyPanelLayout.setHorizontalGroup(
            OntologyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(OntologyPanelLayout.createSequentialGroup()
                .addGroup(OntologyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(OntologyPanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(languagesPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 456, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(OntologyPanelLayout.createSequentialGroup()
                        .addGap(4, 4, 4)
                        .addComponent(ontologyBasicPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(29, Short.MAX_VALUE))
        );
        OntologyPanelLayout.setVerticalGroup(
            OntologyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(OntologyPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(ontologyBasicPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(languagesPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(27, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout SecondTabPanelLayout = new javax.swing.GroupLayout(SecondTabPanel);
        SecondTabPanel.setLayout(SecondTabPanelLayout);
        SecondTabPanelLayout.setHorizontalGroup(
            SecondTabPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, SecondTabPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(OntologyPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(52, 52, 52))
        );
        SecondTabPanelLayout.setVerticalGroup(
            SecondTabPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(SecondTabPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(OntologyPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(159, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Ontology", SecondTabPanel);

        IngredientsClassificationPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Ingredients Classifications"));

        jScrollPane1.setViewportView(classificationList);

        addClassificationButton.setText("Add");
        addClassificationButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addClassificationButtonActionPerformed(evt);
            }
        });

        removeClassificationButton.setText("Remove");
        removeClassificationButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeClassificationButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout IngredientsClassificationPanelLayout = new javax.swing.GroupLayout(IngredientsClassificationPanel);
        IngredientsClassificationPanel.setLayout(IngredientsClassificationPanelLayout);
        IngredientsClassificationPanelLayout.setHorizontalGroup(
            IngredientsClassificationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(IngredientsClassificationPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 430, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20)
                .addGroup(IngredientsClassificationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(addClassificationButton, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(removeClassificationButton))
                .addContainerGap(110, Short.MAX_VALUE))
        );

        IngredientsClassificationPanelLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {addClassificationButton, removeClassificationButton});

        IngredientsClassificationPanelLayout.setVerticalGroup(
            IngredientsClassificationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(IngredientsClassificationPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(IngredientsClassificationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(IngredientsClassificationPanelLayout.createSequentialGroup()
                        .addComponent(addClassificationButton)
                        .addGap(18, 18, 18)
                        .addComponent(removeClassificationButton)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        IngredientsClassificationPanelLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {addClassificationButton, removeClassificationButton});

        FacetsPropertiesPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Facets Properties"));

        jScrollPane2.setViewportView(facetsPropList);

        addFacetsButton.setText("Add");
        addFacetsButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addFacetsButtonActionPerformed(evt);
            }
        });

        removeFacetsButton.setText("Remove");
        removeFacetsButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeFacetsButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout FacetsPropertiesPanelLayout = new javax.swing.GroupLayout(FacetsPropertiesPanel);
        FacetsPropertiesPanel.setLayout(FacetsPropertiesPanelLayout);
        FacetsPropertiesPanelLayout.setHorizontalGroup(
            FacetsPropertiesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(FacetsPropertiesPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 430, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20)
                .addGroup(FacetsPropertiesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(addFacetsButton)
                    .addComponent(removeFacetsButton))
                .addContainerGap(110, Short.MAX_VALUE))
        );

        FacetsPropertiesPanelLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {addFacetsButton, removeFacetsButton});

        FacetsPropertiesPanelLayout.setVerticalGroup(
            FacetsPropertiesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(FacetsPropertiesPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(FacetsPropertiesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(FacetsPropertiesPanelLayout.createSequentialGroup()
                        .addComponent(addFacetsButton)
                        .addGap(18, 18, 18)
                        .addComponent(removeFacetsButton)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        FacetsPropertiesPanelLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {addFacetsButton, removeFacetsButton});

        javax.swing.GroupLayout ThirdRabPanel1Layout = new javax.swing.GroupLayout(ThirdRabPanel1);
        ThirdRabPanel1.setLayout(ThirdRabPanel1Layout);
        ThirdRabPanel1Layout.setHorizontalGroup(
            ThirdRabPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ThirdRabPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(ThirdRabPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(IngredientsClassificationPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(FacetsPropertiesPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(207, Short.MAX_VALUE))
        );

        ThirdRabPanel1Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {FacetsPropertiesPanel, IngredientsClassificationPanel});

        ThirdRabPanel1Layout.setVerticalGroup(
            ThirdRabPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ThirdRabPanel1Layout.createSequentialGroup()
                .addContainerGap(23, Short.MAX_VALUE)
                .addComponent(IngredientsClassificationPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(11, 11, 11)
                .addComponent(FacetsPropertiesPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(48, 48, 48))
        );

        ThirdRabPanel1Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {FacetsPropertiesPanel, IngredientsClassificationPanel});

        jTabbedPane1.addTab("Different Views", ThirdRabPanel1);

        ThirdTabPanel.setMaximumSize(new java.awt.Dimension(915, 617));

        QueryTemplatePanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Query Templates"));

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Template"));

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 702, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 215, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout QueryTemplatePanelLayout = new javax.swing.GroupLayout(QueryTemplatePanel);
        QueryTemplatePanel.setLayout(QueryTemplatePanelLayout);
        QueryTemplatePanelLayout.setHorizontalGroup(
            QueryTemplatePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(QueryTemplatePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(39, Short.MAX_VALUE))
        );
        QueryTemplatePanelLayout.setVerticalGroup(
            QueryTemplatePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(QueryTemplatePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(72, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout ThirdTabPanelLayout = new javax.swing.GroupLayout(ThirdTabPanel);
        ThirdTabPanel.setLayout(ThirdTabPanelLayout);
        ThirdTabPanelLayout.setHorizontalGroup(
            ThirdTabPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ThirdTabPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(QueryTemplatePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(85, Short.MAX_VALUE))
        );
        ThirdTabPanelLayout.setVerticalGroup(
            ThirdTabPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ThirdTabPanelLayout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addComponent(QueryTemplatePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(29, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Query Templates", ThirdTabPanel);

        ResultsPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Results icons and descriptions"));

        resultsIconPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Icons"));

        javax.swing.GroupLayout resultsIconPanelLayout = new javax.swing.GroupLayout(resultsIconPanel);
        resultsIconPanel.setLayout(resultsIconPanelLayout);
        resultsIconPanelLayout.setHorizontalGroup(
            resultsIconPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 764, Short.MAX_VALUE)
        );
        resultsIconPanelLayout.setVerticalGroup(
            resultsIconPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 79, Short.MAX_VALUE)
        );

        jLabel8.setText("Description annotation property");

        annoPropertiesCombo.setEditable(true);
        annoPropertiesCombo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                annoPropertiesComboActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout ResultsPanelLayout = new javax.swing.GroupLayout(ResultsPanel);
        ResultsPanel.setLayout(ResultsPanelLayout);
        ResultsPanelLayout.setHorizontalGroup(
            ResultsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ResultsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(ResultsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(resultsIconPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(ResultsPanelLayout.createSequentialGroup()
                        .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 206, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(annoPropertiesCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 596, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        ResultsPanelLayout.setVerticalGroup(
            ResultsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ResultsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(ResultsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(annoPropertiesCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(resultsIconPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(40, 40, 40))
        );

        javax.swing.GroupLayout FourthTabPanelLayout = new javax.swing.GroupLayout(FourthTabPanel);
        FourthTabPanel.setLayout(FourthTabPanelLayout);
        FourthTabPanelLayout.setHorizontalGroup(
            FourthTabPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(FourthTabPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(ResultsPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(22, Short.MAX_VALUE))
        );
        FourthTabPanelLayout.setVerticalGroup(
            FourthTabPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(FourthTabPanelLayout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addComponent(ResultsPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(155, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Results", FourthTabPanel);

        SanctionsPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Santions Assertions"));

        javax.swing.GroupLayout SanctionsPanelLayout = new javax.swing.GroupLayout(SanctionsPanel);
        SanctionsPanel.setLayout(SanctionsPanelLayout);
        SanctionsPanelLayout.setHorizontalGroup(
            SanctionsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 734, Short.MAX_VALUE)
        );
        SanctionsPanelLayout.setVerticalGroup(
            SanctionsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 125, Short.MAX_VALUE)
        );

        saveAsButton.setText("Save As");
        saveAsButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveAsButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout FifthTabPanelLayout = new javax.swing.GroupLayout(FifthTabPanel);
        FifthTabPanel.setLayout(FifthTabPanelLayout);
        FifthTabPanelLayout.setHorizontalGroup(
            FifthTabPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(FifthTabPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(FifthTabPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(SanctionsPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(FifthTabPanelLayout.createSequentialGroup()
                        .addGap(614, 614, 614)
                        .addComponent(saveAsButton, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(92, Short.MAX_VALUE))
        );
        FifthTabPanelLayout.setVerticalGroup(
            FifthTabPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(FifthTabPanelLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(SanctionsPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 164, Short.MAX_VALUE)
                .addComponent(saveAsButton)
                .addGap(47, 47, 47))
        );

        jTabbedPane1.addTab("Sanctions", FifthTabPanel);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 875, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 430, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(15, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    ArrayList<String> availableLanguages(OntologyClass myOntology) {

        OWLLiteral val = null;

        ArrayList<String> langs = new ArrayList<String>();
        for (OWLClass cls : myOntology.getOntology().getClassesInSignature()) {
            for (OWLAnnotation ann : cls.getAnnotations(myOntology.getOntology(), myOntology.getDf().getRDFSLabel())) {
                val = (OWLLiteral) ann.getValue();
                if (val.hasLang()) {
                    if (!langs.contains(val.getLang())) {
                        langs.add(val.getLang());
                    }
                }

            }
        }
        return langs;

    }

    private void saveAsButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveAsButtonActionPerformed
    }//GEN-LAST:event_saveAsButtonActionPerformed

    private void uploadLogoButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_uploadLogoButtonActionPerformed

        JFileChooser fileChooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Image Files", "jpg", "png", "gif", "jpeg");
        fileChooser.setFileFilter(filter);
        fileChooser.addChoosableFileFilter(filter);
        fileChooser.setAcceptAllFileFilterUsed(false);
        fileChooser.showOpenDialog(this);
        File file = fileChooser.getSelectedFile();
        if (file != null) {

            try {
                logoURL.setText(file.toURI().toURL().toString());
            } catch (MalformedURLException ex) {
                Logger.getLogger(ModifyConfigurationFileDialog.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }//GEN-LAST:event_uploadLogoButtonActionPerformed

    private void classesComboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_classesComboActionPerformed

        /*  if (classesCombo.getSelectedIndex() == -1) {
         componentClassTextField.setText("");
         componentClassTextField.setEditable(false);
         } else {
         componentClassTextField.setText(classesCombo.getSelectedItem().toString());
         componentClassTextField.setEditable(false);
         }*/
    }//GEN-LAST:event_classesComboActionPerformed

    private void ontologyURLInputMethodTextChanged(java.awt.event.InputMethodEvent evt) {//GEN-FIRST:event_ontologyURLInputMethodTextChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_ontologyURLInputMethodTextChanged

    private void ontologyURLActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ontologyURLActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ontologyURLActionPerformed

    private void annoPropertiesComboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_annoPropertiesComboActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_annoPropertiesComboActionPerformed

    private void addClassificationButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addClassificationButtonActionPerformed

        //Show dialog to add classification
        JComboBox combo = new JComboBox();
        DefaultListModel classesModel = new DefaultListModel();
        for (OWLClass cls : myOntology.getOntology().getClassesInSignature()) {
            classesModel.addElement(cls.getIRI());

        }

        classesModel = sortListModel(classesModel);
        for (int i = 0; i < classesModel.getSize(); i++) {
            combo.addItem(classesModel.get(i));
        }
        

        
         String message = "Select class to be added:";
            Object[] params = {message, combo};
            int n = JOptionPane.showConfirmDialog(this, params, "Add Classification", JOptionPane.OK_CANCEL_OPTION);

             if (n == 0) { //yes?
                  JComboBox combo1 = new JComboBox();
                  combo1=(JComboBox) params[1];
                  myClassificationModel.addElement("<"+combo1.getSelectedItem().toString()+">");
                 
             }
            
             


    }//GEN-LAST:event_addClassificationButtonActionPerformed

    private void removeClassificationButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeClassificationButtonActionPerformed
        // TODO add your handling code here:
        
        if (!myClassificationModel.isEmpty()) {

            if (classificationList.getSelectedIndex() != -1) {
                myClassificationModel.remove(classificationList.getSelectedIndex());
                classificationList.revalidate();
                classificationList.repaint();
            }
        }
    }//GEN-LAST:event_removeClassificationButtonActionPerformed

    private void addFacetsButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addFacetsButtonActionPerformed
        // TODO add your handling code here:
         //Show dialog to add classification
        JComboBox combo = new JComboBox();
        DefaultListModel objectPropModel = new DefaultListModel();
        for (OWLObjectProperty prop : myOntology.getOntology().getObjectPropertiesInSignature()) {
            objectPropModel.addElement(prop.getIRI());

        }

        objectPropModel = sortListModel(objectPropModel);
        for (int i = 0; i < objectPropModel.getSize(); i++) {
            combo.addItem(objectPropModel.get(i));
        }
    
        
         String message = "Select property to be added:";
            Object[] params = {message, combo};
            int n = JOptionPane.showConfirmDialog(this, params, "Add Facets Property", JOptionPane.OK_CANCEL_OPTION);

             if (n == 0) { //yes?
                  JComboBox combo1 = new JComboBox();
                  combo1=(JComboBox) params[1];
                  myFacetsPropModel.addElement("<"+combo1.getSelectedItem().toString()+">");
                 
             }
            
        
        
    }//GEN-LAST:event_addFacetsButtonActionPerformed

    private void removeFacetsButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeFacetsButtonActionPerformed
        // TODO add your handling code here:
        
         
        if (!myFacetsPropModel.isEmpty()) {

            if (facetsPropList.getSelectedIndex() != -1) {
                myFacetsPropModel.remove(facetsPropList.getSelectedIndex());
                facetsPropList.revalidate();
                facetsPropList.repaint();
            }
        }
        
    }//GEN-LAST:event_removeFacetsButtonActionPerformed
    /**
     * @param args the command line arguments
     */
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel FacetsPropertiesPanel;
    private javax.swing.JPanel FifthTabPanel;
    private javax.swing.JPanel FirstTabPanel;
    private javax.swing.JPanel FourthTabPanel;
    private javax.swing.JPanel IngredientsClassificationPanel;
    private javax.swing.JPanel OntologyPanel;
    private javax.swing.JPanel QueryTemplatePanel;
    private javax.swing.JPanel ResultsPanel;
    private javax.swing.JPanel SanctionsPanel;
    private javax.swing.JPanel SecondTabPanel;
    private javax.swing.JPanel ThirdRabPanel1;
    private javax.swing.JPanel ThirdTabPanel;
    private javax.swing.JButton addClassificationButton;
    private javax.swing.JButton addFacetsButton;
    private javax.swing.JComboBox annoPropertiesCombo;
    private javax.swing.JPanel appearancePanel;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JComboBox classRenderCombo;
    private javax.swing.JComboBox classesCombo;
    private javax.swing.JList classificationList;
    private javax.swing.JTextField excludedListLabel;
    private javax.swing.JList facetsPropList;
    private javax.swing.JTextField includedListLabel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JPanel languagesPanel;
    public javax.swing.JLabel logoLabel;
    private javax.swing.JTextField logoURL;
    private javax.swing.JPanel ontologyBasicPanel;
    private javax.swing.JTextField ontologyURL;
    private javax.swing.JButton removeClassificationButton;
    private javax.swing.JButton removeFacetsButton;
    private javax.swing.JPanel resultsIconPanel;
    private javax.swing.JButton saveAsButton;
    private javax.swing.JTextField titleLabel;
    private javax.swing.JButton uploadLogoButton;
    // End of variables declaration//GEN-END:variables

    class CheckListRenderer extends JCheckBox implements ListCellRenderer {

        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean hasFocus) {
            setEnabled(list.isEnabled());
            setSelected(((CheckListItem) value).isSelected);
            setFont(list.getFont());
            setBackground(list.getBackground());
            setForeground(list.getForeground());
            setText(value.toString());
            return this;
        }
    }

    class CheckListItem {

        private String label;
        private boolean isSelected = false;

        public CheckListItem(String label) {
            this.label = label;
        }

        public boolean isSelected() {
            return isSelected;
        }

        public void setSelected(boolean isSelected) {
            this.isSelected = isSelected;
        }

        public String toString() {
            return label;
        }
    }
}

