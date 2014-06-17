/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.manchestersushifinder;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.ItemSelectable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultListModel;
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
import noNamespace.ComplexTemplate;
import noNamespace.FinderConfigurationDocument;
import noNamespace.FinderConfigurationDocument.FinderConfiguration.AvailableLanguages;
import noNamespace.FinderConfigurationDocument.FinderConfiguration.AvailableLanguages.Language;
import noNamespace.FinderConfigurationDocument.FinderConfiguration.ClassRendering;
import noNamespace.FinderConfigurationDocument.FinderConfiguration.ExcludedLabel;
import noNamespace.FinderConfigurationDocument.FinderConfiguration.IncludedLabel;
import noNamespace.FinderConfigurationDocument.FinderConfiguration.IngredientClass;
import noNamespace.FinderConfigurationDocument.FinderConfiguration.IngredientsClassifications;
import noNamespace.FinderConfigurationDocument.FinderConfiguration.IngredientsClassifications.Classification;
import noNamespace.FinderConfigurationDocument.FinderConfiguration.IngredientsFacets;
import noNamespace.FinderConfigurationDocument.FinderConfiguration.IngredientsFacets.Facet;
import noNamespace.FinderConfigurationDocument.FinderConfiguration.Logo;
import noNamespace.FinderConfigurationDocument.FinderConfiguration.OntologyLocation;
import noNamespace.FinderConfigurationDocument.FinderConfiguration.QueryTemplates;
import noNamespace.FinderConfigurationDocument.FinderConfiguration.ResultsAnnotationProperty;
import noNamespace.FinderConfigurationDocument.FinderConfiguration.ResultsCharacteristics;
import noNamespace.FinderConfigurationDocument.FinderConfiguration.ResultsCharacteristics.ResultsCharacteristic;
import noNamespace.FinderConfigurationDocument.FinderConfiguration.TitleLabel;
import noNamespace.SimpleTemplate;
import noNamespace.SimpleTemplate.BaseClass;
import noNamespace.SimpleTemplate.ComponentClass;
import noNamespace.SimpleTemplate.Property;
import noNamespace.SimpleTemplate.Show;
import org.apache.xmlbeans.XmlOptions;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLObjectProperty;

/**
 *
 * @author Atheer
 */
public class NewConfigurationFileDialog extends javax.swing.JDialog {

    FinderConfigurationDocument doc;
    FinderConfigurationDocument.FinderConfiguration finderConfigElement;
    public static OntologyClass myOntology;
    ArrayList<String> resultsCharacteristics = new ArrayList<String>();
    ArrayList<String> resultsIcons = new ArrayList<String>();
    
    //NEW-------------------------
    ArrayList<String> ingredientsClassifications = new ArrayList<String>();
    ArrayList<String> ingredientsFacetsProperties = new ArrayList<String>();
    //------------------------------
    public static int templateIDCount;
    public static boolean ontologyIsUploaded = false;
    public static String ontologyLocation = "";
    ArrayList<String[]> templates = new ArrayList<String[]>();
    JList list;

    /**
     * Creates new form NewFileDialog
     */
    public NewConfigurationFileDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);


        initComponents();

        setupOntologyURLDocumentListner();
        uploadButton.setEnabled(false);

        templateIDCount = 1;
        templateIdTextField.setText("ID" + templateIDCount);
        buttonGroup1.add(classRadioButton);
        buttonGroup1.add(templateRadioButton);
        jLabel23.setVisible(false);

    }

    static private String selectedString(ItemSelectable is) {
        Object selected[] = is.getSelectedObjects();
        return ((selected.length == 0) ? "null" : (String) selected[0]);
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
                    uploadButton.setEnabled(false);
                    
                    //NEW-------RESET EVERY LIST-----
                    ontologyIsUploaded=false;
                   ontologyLocation="";
                  classesCombo.removeAllItems();
                 resultCharClasseCombo.removeAllItems();
                 baseClassCombo.removeAllItems();
                 objPropCombo.removeAllItems();
                facetsPropertiesCombo.removeAllItems();
                ingredientsClassificationsCombo.removeAllItems();
                annoPropertiesCombo.removeAllItems();
                resultsCharacteristics = new ArrayList<String>();
                resultsIcons = new ArrayList<String>();
                templateIDCount = 1;
                templateIdTextField.setText("ID" + templateIDCount);
                templates = new ArrayList<String[]>();
                templatesCombo.removeAllItems();
                 ingredientsClassifications = new ArrayList<String>();
                 ingredientsFacetsProperties = new ArrayList<String>();
                 langsPanel.removeAll();
                 ontologyPanel.validate();
                 
                //-----------------------------
                    
                    
                } else {
                    uploadButton.setEnabled(true);
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
        FirstStepPanel = new javax.swing.JPanel();
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
        ontologyPanel = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        annoPropertiesCombo = new javax.swing.JComboBox();
        jLabel25 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        uploadOntologyButton = new javax.swing.JButton();
        uploadButton = new javax.swing.JButton();
        classesCombo = new javax.swing.JComboBox();
        classRenderCombo = new javax.swing.JComboBox();
        ontologyURL = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        IngredientsCharacteristicsPanel = new javax.swing.JPanel();
        jButton3 = new javax.swing.JButton();
        facetsPropertiesCombo = new javax.swing.JComboBox();
        ingredientsClassificationsCombo = new javax.swing.JComboBox();
        ingredientsClassificationButton = new javax.swing.JButton();
        jLabel9 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        langsPanel = new javax.swing.JPanel();
        SecondStepPanel = new javax.swing.JPanel();
        jButton2 = new javax.swing.JButton();
        QueryTemplatePanel = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        baseClassCombo = new javax.swing.JComboBox();
        templateRadioButton = new javax.swing.JRadioButton();
        classRadioButton = new javax.swing.JRadioButton();
        jLabel16 = new javax.swing.JLabel();
        componentClassTextField = new javax.swing.JTextField();
        objPropCombo = new javax.swing.JComboBox();
        templatesCombo = new javax.swing.JComboBox();
        jLabel14 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        templateIdTextField = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        templateNameText = new javax.swing.JTextField();
        showCheckBox = new javax.swing.JCheckBox();
        jLabel17 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        characteristicsPanel = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        iconURL = new javax.swing.JTextField();
        uploadIcon = new javax.swing.JButton();
        resultCharClasseCombo = new javax.swing.JComboBox();
        addLangButton1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("New configuration file");
        setResizable(false);

        jTabbedPane1.setAutoscrolls(true);

        FirstStepPanel.setMaximumSize(new java.awt.Dimension(915, 617));

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
                    .addComponent(titleLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 213, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(includedListLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 213, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(appearancePanelLayout.createSequentialGroup()
                        .addComponent(logoURL, javax.swing.GroupLayout.PREFERRED_SIZE, 411, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(41, 41, 41)
                        .addComponent(uploadLogoButton, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(excludedListLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 213, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
                .addGap(11, 11, 11))
        );

        ontologyPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Ontology"));

        jLabel7.setText("Ingredients  class");

        jLabel10.setText("Ontology location");

        annoPropertiesCombo.setEditable(true);
        annoPropertiesCombo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                annoPropertiesComboActionPerformed(evt);
            }
        });

        jLabel25.setText("The default class rendering use:");

        jLabel8.setText("Results annotation property");

        uploadOntologyButton.setText("Browse...");
        uploadOntologyButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                uploadOntologyButtonActionPerformed(evt);
            }
        });

        uploadButton.setText("Upload");
        uploadButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                uploadButtonActionPerformed(evt);
            }
        });

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

        jLabel18.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(255, 0, 0));
        jLabel18.setText("*");

        jLabel19.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel19.setForeground(new java.awt.Color(255, 0, 0));
        jLabel19.setText("*");

        IngredientsCharacteristicsPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Ingredients Characteristics"));

        jButton3.setText("Add Property");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        facetsPropertiesCombo.setEditable(true);
        facetsPropertiesCombo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                facetsPropertiesComboActionPerformed(evt);
            }
        });

        ingredientsClassificationsCombo.setEditable(true);
        ingredientsClassificationsCombo.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        ingredientsClassificationsCombo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ingredientsClassificationsComboActionPerformed(evt);
            }
        });

        ingredientsClassificationButton.setText("Add Class");
        ingredientsClassificationButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ingredientsClassificationButtonActionPerformed(evt);
            }
        });

        jLabel9.setText("Ingredients Classifications");
        jLabel9.setToolTipText("");

        jLabel11.setText("Facets Properties");

        javax.swing.GroupLayout IngredientsCharacteristicsPanelLayout = new javax.swing.GroupLayout(IngredientsCharacteristicsPanel);
        IngredientsCharacteristicsPanel.setLayout(IngredientsCharacteristicsPanelLayout);
        IngredientsCharacteristicsPanelLayout.setHorizontalGroup(
            IngredientsCharacteristicsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(IngredientsCharacteristicsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(IngredientsCharacteristicsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, 183, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(IngredientsCharacteristicsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(ingredientsClassificationsCombo, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(facetsPropertiesCombo, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(IngredientsCharacteristicsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, 121, Short.MAX_VALUE)
                    .addComponent(ingredientsClassificationButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        IngredientsCharacteristicsPanelLayout.setVerticalGroup(
            IngredientsCharacteristicsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(IngredientsCharacteristicsPanelLayout.createSequentialGroup()
                .addGap(11, 11, 11)
                .addGroup(IngredientsCharacteristicsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ingredientsClassificationsCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ingredientsClassificationButton)
                    .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(11, 11, 11)
                .addGroup(IngredientsCharacteristicsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(facetsPropertiesCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton3))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(25, 25, 25)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(ontologyURL, javax.swing.GroupLayout.PREFERRED_SIZE, 401, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel18)
                                .addGap(28, 28, 28)
                                .addComponent(uploadOntologyButton, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(uploadButton, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(classesCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 642, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(classRenderCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(annoPropertiesCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 641, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel19))))
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jLabel25, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(IngredientsCharacteristicsPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(uploadOntologyButton)
                        .addComponent(ontologyURL, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel18)
                        .addComponent(uploadButton)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(classesCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel19))
                .addGap(11, 11, 11)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(annoPropertiesCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel25, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(classRenderCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(IngredientsCharacteristicsPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        langsPanel.setLayout(new java.awt.BorderLayout());

        javax.swing.GroupLayout ontologyPanelLayout = new javax.swing.GroupLayout(ontologyPanel);
        ontologyPanel.setLayout(ontologyPanelLayout);
        ontologyPanelLayout.setHorizontalGroup(
            ontologyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ontologyPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(ontologyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(ontologyPanelLayout.createSequentialGroup()
                        .addComponent(langsPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 382, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 872, Short.MAX_VALUE)))
        );
        ontologyPanelLayout.setVerticalGroup(
            ontologyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ontologyPanelLayout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(langsPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout FirstStepPanelLayout = new javax.swing.GroupLayout(FirstStepPanel);
        FirstStepPanel.setLayout(FirstStepPanelLayout);
        FirstStepPanelLayout.setHorizontalGroup(
            FirstStepPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, FirstStepPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(FirstStepPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(appearancePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(ontologyPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(11, 11, 11))
        );
        FirstStepPanelLayout.setVerticalGroup(
            FirstStepPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(FirstStepPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(appearancePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(11, 11, 11)
                .addComponent(ontologyPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(122, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Step 1", FirstStepPanel);

        SecondStepPanel.setMaximumSize(new java.awt.Dimension(915, 617));

        jButton2.setText("Save as");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        QueryTemplatePanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Query Templates"));

        jButton1.setText("Add Template");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Template"));

        baseClassCombo.setEditable(true);
        baseClassCombo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                baseClassComboActionPerformed(evt);
            }
        });

        templateRadioButton.setText("Query Template");
        templateRadioButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                templateRadioButtonActionPerformed(evt);
            }
        });

        classRadioButton.setSelected(true);
        classRadioButton.setText("Component class (IRI)");
        classRadioButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                classRadioButtonActionPerformed(evt);
            }
        });

        jLabel16.setText("Object property (IRI)");

        componentClassTextField.setEditable(false);

        objPropCombo.setEditable(true);
        objPropCombo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                objPropComboActionPerformed(evt);
            }
        });

        templatesCombo.setEditable(true);

        jLabel14.setText("Base class (IRI)");

        jLabel12.setText("Template ID");

        templateIdTextField.setEditable(false);

        jLabel13.setText("Name");

        showCheckBox.setSelected(true);

        jLabel17.setText("Show");

        jLabel20.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel20.setForeground(new java.awt.Color(255, 0, 0));
        jLabel20.setText("*");

        jLabel21.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel21.setForeground(new java.awt.Color(255, 0, 0));
        jLabel21.setText("*");

        jLabel22.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel22.setForeground(new java.awt.Color(255, 0, 0));
        jLabel22.setText("*");

        jLabel23.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel23.setForeground(new java.awt.Color(255, 0, 0));
        jLabel23.setText("*");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(classRadioButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(templateRadioButton, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(20, 20, 20)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(templatesCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel23))
                            .addComponent(componentClassTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 599, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jLabel12, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel16, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel17, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGap(20, 20, 20)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(showCheckBox)
                                    .addComponent(baseClassCombo, 0, 599, Short.MAX_VALUE)
                                    .addComponent(objPropCombo, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addComponent(templateIdTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 93, Short.MAX_VALUE)
                                .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(templateNameText, javax.swing.GroupLayout.PREFERRED_SIZE, 207, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel20)
                                .addGap(97, 97, 97)))))
                .addGap(10, 10, 10)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel22, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel21, javax.swing.GroupLayout.Alignment.TRAILING))
                .addGap(33, 33, 33))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(templateIdTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(templateNameText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel20))
                .addGap(11, 11, 11)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(showCheckBox)
                    .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(11, 11, 11)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(baseClassCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel21))
                    .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(11, 11, 11)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(objPropCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel22))
                .addGap(11, 11, 11)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(classRadioButton)
                    .addComponent(componentClassTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(11, 11, 11)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(templateRadioButton)
                    .addComponent(templatesCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel23))
                .addGap(11, 11, 11))
        );

        javax.swing.GroupLayout QueryTemplatePanelLayout = new javax.swing.GroupLayout(QueryTemplatePanel);
        QueryTemplatePanel.setLayout(QueryTemplatePanelLayout);
        QueryTemplatePanelLayout.setHorizontalGroup(
            QueryTemplatePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(QueryTemplatePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(QueryTemplatePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(31, Short.MAX_VALUE))
        );
        QueryTemplatePanelLayout.setVerticalGroup(
            QueryTemplatePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(QueryTemplatePanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton1))
        );

        characteristicsPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Results Characteristics"));

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Characteristic"));

        jLabel2.setText("Class");

        jLabel3.setText("Icon URL");

        uploadIcon.setText("Browse...");
        uploadIcon.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                uploadIconActionPerformed(evt);
            }
        });

        resultCharClasseCombo.setEditable(true);
        resultCharClasseCombo.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        resultCharClasseCombo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                resultCharClasseComboActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, 151, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 58, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addComponent(iconURL)
                        .addGap(18, 18, 18)
                        .addComponent(uploadIcon, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(resultCharClasseCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 569, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(41, 41, 41))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(resultCharClasseCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(iconURL, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(uploadIcon))
                .addContainerGap())
        );

        addLangButton1.setText("Add Characteristic");
        addLangButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addLangButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout characteristicsPanelLayout = new javax.swing.GroupLayout(characteristicsPanel);
        characteristicsPanel.setLayout(characteristicsPanelLayout);
        characteristicsPanelLayout.setHorizontalGroup(
            characteristicsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(characteristicsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(characteristicsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(addLangButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        characteristicsPanelLayout.setVerticalGroup(
            characteristicsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(characteristicsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(addLangButton1)
                .addContainerGap(19, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout SecondStepPanelLayout = new javax.swing.GroupLayout(SecondStepPanel);
        SecondStepPanel.setLayout(SecondStepPanelLayout);
        SecondStepPanelLayout.setHorizontalGroup(
            SecondStepPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(SecondStepPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(SecondStepPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(characteristicsPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(QueryTemplatePanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, SecondStepPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(45, 45, 45))
        );
        SecondStepPanelLayout.setVerticalGroup(
            SecondStepPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, SecondStepPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(QueryTemplatePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(11, 11, 11)
                .addComponent(characteristicsPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(26, 26, 26)
                .addComponent(jButton2)
                .addContainerGap(46, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Step 2", SecondStepPanel);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane1)
                .addContainerGap())
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

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed

        //To create a FinderConfigurationDocument object and returns it
        doc = FinderConfigurationDocument.Factory.newInstance();

        // addNewFinderConfiguration()method is called on the document object to
        //create and add a new FinderConfiguration Element to document
        finderConfigElement = doc.addNewFinderConfiguration();

        //Appearance Panel
        Logo logoElement = finderConfigElement.addNewLogo();

        if (logoURL.getText() != null) {
            logoElement.setURL(logoURL.getText().trim()); //get the URL
        }

        IncludedLabel includedLabelElement = finderConfigElement.addNewIncludedLabel();
        if (includedListLabel.getText() != null) {
            includedLabelElement.setText(includedListLabel.getText().trim());
        }

        ExcludedLabel excludedLabelElement = finderConfigElement.addNewExcludedLabel();
        if (excludedListLabel.getText() != null) {
            excludedLabelElement.setText(excludedListLabel.getText().trim());
        }

        TitleLabel titleLabelElement = finderConfigElement.addNewTitleLabel();
        if (titleLabel.getText() != null) {
            titleLabelElement.setText(titleLabel.getText().trim());
        }

        //Appearance Panel
        OntologyLocation ontologyLocElement = finderConfigElement.addNewOntologyLocation();
       // if (ontologyURL.getText() != null) {
         //   ontologyLocElement.setUrl(ontologyURL.getText().trim());
       // }
        if(!ontologyLocation.isEmpty())
        {
            ontologyLocElement.setUrl(ontologyLocation);
        }

        IngredientClass ingredientClassElement = finderConfigElement.addNewIngredientClass();
        if (classesCombo.getSelectedItem() != null) {
            ingredientClassElement.setName(classesCombo.getSelectedItem().toString());
        }

        ResultsAnnotationProperty resultsAnnoPropElement = finderConfigElement.addNewResultsAnnotationProperty();
        if (annoPropertiesCombo.getSelectedItem() != null) {
            resultsAnnoPropElement.setName(annoPropertiesCombo.getSelectedItem().toString());
        }

        AvailableLanguages availableLangsElement = finderConfigElement.addNewAvailableLanguages();

        if (list != null) {
            ArrayList<String> selectedLangs = new ArrayList<String>();
            for (int i = 0; i < list.getModel().getSize(); i++) {
                CheckListItem item = (CheckListItem) list.getModel().getElementAt(i);
                if (item.isSelected()) {
                    selectedLangs.add(item.toString());
                }
            }

            if (!selectedLangs.isEmpty()) {
                //if there is no languages we dont need to have "default" so we can switch back to english
                Language languageElement = availableLangsElement.addNewLanguage();
                languageElement.setName("default");
            }


            for (int i = 0; i < selectedLangs.size(); i++) {
                String value = selectedLangs.get(i).toString().trim().toLowerCase();
                Language languageElement = availableLangsElement.addNewLanguage();
                languageElement.setName(value);
            }
        }






        ClassRendering classRenderingElement = finderConfigElement.addNewClassRendering();

        if (classRenderCombo.getSelectedItem() != null) {
            classRenderingElement.setUse(classRenderCombo.getSelectedItem().toString());
        } else {
            classRenderingElement.setUse("IRI");
        }

        ResultsCharacteristics resultsCharacteristicsElement = finderConfigElement.addNewResultsCharacteristics();
        if (!resultsCharacteristics.isEmpty() && !resultsIcons.isEmpty()) {
            for (int i = 0; i < resultsCharacteristics.size(); i++) {
                ResultsCharacteristic resultsCharacteristicElement = resultsCharacteristicsElement.addNewResultsCharacteristic();
                resultsCharacteristicElement.setClass1(resultsCharacteristics.get(i));
                resultsCharacteristicElement.setUrl(resultsIcons.get(i));
            }
        }

       IngredientsClassifications ingredientsClassificationsElement=finderConfigElement.addNewIngredientsClassifications();
       if (!ingredientsClassifications.isEmpty())
       {
            for (int i = 0; i < ingredientsClassifications.size(); i++) {
           Classification classificationElement=ingredientsClassificationsElement.addNewClassification();
           classificationElement.setClass1(ingredientsClassifications.get(i));
       }
       }
       
       
       IngredientsFacets ingredientsFacetsElement=finderConfigElement.addNewIngredientsFacets();
       if(!ingredientsFacetsProperties.isEmpty())
       {
           for(int i=0;i<ingredientsFacetsProperties.size();i++)
           {
               Facet facetElement=ingredientsFacetsElement.addNewFacet();
               facetElement.setProperty(ingredientsFacetsProperties.get(i));
           }
       }
    

        QueryTemplates queryTemplatesElement = finderConfigElement.addNewQueryTemplates();
        if (!templates.isEmpty()) {
            for (int i = 0; i < templates.size(); i++) {
                if (templates.get(i)[6].equalsIgnoreCase("Simple")) { //template type

                    SimpleTemplate simpleTemplateElement = queryTemplatesElement.addNewSimpleTemplate();
                    simpleTemplateElement.setId(templates.get(i)[0]); //id
                    simpleTemplateElement.setName(templates.get(i)[1]); //name

                    if (templates.get(i)[2].equalsIgnoreCase("yes")) //show
                    {
                        simpleTemplateElement.setShow(Show.YES);
                    } else {
                        simpleTemplateElement.setShow(Show.NO);
                    }

                    BaseClass baseClassElement = simpleTemplateElement.addNewBaseClass();
                    baseClassElement.setName(templates.get(i)[3]); //base IRI

                    Property propertyElement = simpleTemplateElement.addNewProperty();
                    propertyElement.setName(templates.get(i)[4]); //property IRI

                    ComponentClass componentClassElement = simpleTemplateElement.addNewComponentClass();
                    componentClassElement.setName(templates.get(i)[5]); //component IRI


                } else //complex template
                {
                    ComplexTemplate complexTemplateElement = queryTemplatesElement.addNewComplexTemplate();
                    complexTemplateElement.setId(templates.get(i)[0]); //id
                    complexTemplateElement.setName(templates.get(i)[1]); //name

                    if (templates.get(i)[2].equalsIgnoreCase("yes")) //show
                    {
                        complexTemplateElement.setShow(ComplexTemplate.Show.YES);
                    } else {
                        complexTemplateElement.setShow(ComplexTemplate.Show.NO);
                    }

                    ComplexTemplate.BaseClass baseClassElement = complexTemplateElement.addNewBaseClass();
                    baseClassElement.setName(templates.get(i)[3]); //base IRI

                    ComplexTemplate.Property propertyElement = complexTemplateElement.addNewProperty();
                    propertyElement.setName(templates.get(i)[4]); //property IRI

                    ComplexTemplate.Template templateElement = complexTemplateElement.addNewTemplate();
                    templateElement.setRef(templates.get(i)[5]); // other template ID 
                }
            }
        }


        if (doc.validate()) {
            JFileChooser saveFile = new JFileChooser();
            FileNameExtensionFilter filter = new FileNameExtensionFilter("XML File", "xml");
            saveFile.setFileFilter(filter);
            saveFile.addChoosableFileFilter(filter);
            saveFile.setAcceptAllFileFilterUsed(false);
            saveFile.setApproveButtonText("Save as");
            saveFile.showSaveDialog(this);

            if (saveFile.getSelectedFile() != null) {
                FileOutputStream file = null;
                try {
                    file = new FileOutputStream(saveFile.getSelectedFile().getPath().toString() + ".xml");
                    //XmlOptions to request the use of  whitespace and
                    //indent by 4 for nested levels
                    XmlOptions xmlOptions = new XmlOptions();
                    xmlOptions.setSavePrettyPrint();
                    xmlOptions.setSavePrettyPrintIndent(4);
                    doc.save(file, xmlOptions);
                    //After Saving the new file. Do you want to use it?
                    int answer;
                    answer = JOptionPane.showConfirmDialog(this, "Do you want to use this new configuration file??\n\n", "New Configuration File", JOptionPane.YES_NO_OPTION);
                    if (answer == JOptionPane.YES_OPTION) {
                        Global.useDefault = false;
                        Global.configFileURL = saveFile.getSelectedFile().getPath().toString() + ".xml";
                        StartFrame.qframe.dispose();
                        StartFrame newFrame = new StartFrame();
                        newFrame.setVisible(true);
                        this.dispose();

                    } else {
                        this.dispose();
                    }
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(NewConfigurationFileDialog.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(NewConfigurationFileDialog.class.getName()).log(Level.SEVERE, null, ex);
                } finally {
                    try {
                        file.close();
                    } catch (IOException ex) {
                        Logger.getLogger(NewConfigurationFileDialog.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

            }

        } else {
            JOptionPane.showMessageDialog(this, "  Not valid Sorry.. Cannot Save the file!", "Error", JOptionPane.ERROR_MESSAGE);
        }





    }//GEN-LAST:event_jButton2ActionPerformed

    private void uploadOntologyButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_uploadOntologyButtonActionPerformed


        JFileChooser fileChooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Ontology", "owl");
        fileChooser.setFileFilter(filter);
        fileChooser.addChoosableFileFilter(filter);
        fileChooser.setAcceptAllFileFilterUsed(false);
        fileChooser.showOpenDialog(this);
        File file = fileChooser.getSelectedFile();
        myOntology = null;


        if (file != null) {

            try {
                ontologyURL.setText(file.toURI().toURL().toString());
                uploadButton.setEnabled(true);
            } catch (MalformedURLException ex) {
                // Logger.getLogger(NewConfigurationFileDialog.class.getName()).log(Level.SEVERE, null, ex);

                JOptionPane.showMessageDialog(this, "  Sorry.. Cannot load the ontology", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }





        // TODO add your handling code here:
    }//GEN-LAST:event_uploadOntologyButtonActionPerformed

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
                Logger.getLogger(NewConfigurationFileDialog.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }//GEN-LAST:event_uploadLogoButtonActionPerformed

    private void annoPropertiesComboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_annoPropertiesComboActionPerformed
    }//GEN-LAST:event_annoPropertiesComboActionPerformed

    private void classesComboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_classesComboActionPerformed

        /*  if (classesCombo.getSelectedIndex() == -1) {
         componentClassTextField.setText("");
         componentClassTextField.setEditable(false);
         } else {
         componentClassTextField.setText(classesCombo.getSelectedItem().toString());
         componentClassTextField.setEditable(false);
         }*/
    }//GEN-LAST:event_classesComboActionPerformed

    private void uploadIconActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_uploadIconActionPerformed

        JFileChooser fileChooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Image Files", "jpg", "png", "gif", "jpeg");
        fileChooser.setFileFilter(filter);
        fileChooser.addChoosableFileFilter(filter);
        fileChooser.setAcceptAllFileFilterUsed(false);
        fileChooser.showOpenDialog(this);
        File file = fileChooser.getSelectedFile();
        if (file != null) {

            try {
                iconURL.setText(file.toURI().toURL().toString());
            } catch (MalformedURLException ex) {
                Logger.getLogger(NewConfigurationFileDialog.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_uploadIconActionPerformed

    private void resultCharClasseComboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_resultCharClasseComboActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_resultCharClasseComboActionPerformed

    private void addLangButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addLangButton1ActionPerformed


        if (ontologyIsUploaded) {

            if (resultCharClasseCombo.getSelectedItem()!=null) {

                if (!iconURL.getText().isEmpty()) {
                    resultsCharacteristics.add(resultCharClasseCombo.getSelectedItem().toString());
                    resultsIcons.add(iconURL.getText().trim());
                    iconURL.setText("");
                    resultCharClasseCombo.setSelectedIndex(-1);
                } else {
                    JOptionPane.showMessageDialog(this, "Characteristics icon is not selected.", "Error", JOptionPane.ERROR_MESSAGE);
                }

            } else {
                JOptionPane.showMessageDialog(this, "Characteristics class is not selected.", "Error", JOptionPane.ERROR_MESSAGE);
            }

        } else {
            JOptionPane.showMessageDialog(this, "You have to upload your ontology first!", "Error", JOptionPane.ERROR_MESSAGE);
        }


    }//GEN-LAST:event_addLangButton1ActionPerformed

    private void baseClassComboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_baseClassComboActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_baseClassComboActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed

        String baseClassIRI = null;
        String objPropIRI = null;
        String thirdPart = null;
        String templateID = templateIDCount + "";
        String templateName = null;
        String show = null;


        boolean flag1 = false; //temp Name
        boolean flag2 = false; //Show
        boolean flag3 = false; //base
        boolean flag4 = false; //property
        boolean flag5 = false; //simple template
        boolean flag6 = false; //complex template

        if (ontologyIsUploaded) {

            if (!templateNameText.getText().isEmpty()) {
                templateName = templateNameText.getText().trim();
                flag1 = true;

                if (baseClassCombo.getSelectedItem() != null) {
                    baseClassIRI = baseClassCombo.getSelectedItem().toString();
                    flag3 = true;

                    if (objPropCombo.getSelectedItem() != null) {

                        objPropIRI = objPropCombo.getSelectedItem().toString();
                        flag4 = true;
                    } else {
                        JOptionPane.showMessageDialog(this, "Please select an object property for the template..", "Error", JOptionPane.ERROR_MESSAGE);
                    }

                } else {
                    JOptionPane.showMessageDialog(this, "Please select a base class and object property for the template..", "Error", JOptionPane.ERROR_MESSAGE);
                }

            } else {
                JOptionPane.showMessageDialog(this, "Please type a name for the template..", "Error", JOptionPane.ERROR_MESSAGE);
            }


            if (showCheckBox.isSelected()) {
                show = "yes";
                flag2 = true;
            } else {
                show = "no";
                flag2 = true;
            }


            if (classRadioButton.isSelected()) {
                if (!componentClassTextField.getText().isEmpty()) {
                    thirdPart = componentClassTextField.getText();
                    flag5 = true; //simple template
                } else {
                    JOptionPane.showMessageDialog(this, "You have to select a component class first! \n [in step 1]", "Error", JOptionPane.ERROR_MESSAGE);
                }


            } else {
                if (templatesCombo.getSelectedItem() != null) {
                    thirdPart = templatesCombo.getSelectedItem().toString();
                    flag6 = true;
                } else {
                    JOptionPane.showMessageDialog(this, "You have to select template from the list." + "[or select the first option (component class)]", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }


            if (flag1 && flag2 && flag3 && flag4 && (flag5 || flag6)) {
                String infoMsg = "\nID: [" + templateID + "]\nName: [" + templateName + "]\nShow: [" + show + "]\nBase class: [" + baseClassIRI + "]\nProperty: [" + objPropIRI + "]\nComponent class: [" + thirdPart + "]";
                int ans;
                ans = JOptionPane.showConfirmDialog(this, "Are you sure you want to add the following template??\n\nTemplate Info:" + infoMsg, "Add template", JOptionPane.YES_NO_OPTION);
                if (ans == JOptionPane.YES_OPTION) {

                    //------------reset---------------------
                    templatesCombo.addItem("ID" + templateID);
                    templateIDCount++;
                    templateIdTextField.setText("ID" + templateIDCount);
                    templatesCombo.setSelectedIndex(-1);
                    objPropCombo.setSelectedIndex(-1);
                    baseClassCombo.setSelectedIndex(-1);
                    classRadioButton.setSelected(true);
                    templateNameText.setText("");

                    //-------------add to templates array list--------
                    if (flag5) {
                        templates.add(new String[]{"ID" + templateID, templateName, show, baseClassIRI, objPropIRI, thirdPart, "Simple"});
                    } else {
                        templates.add(new String[]{"ID" + templateID, templateName, show, baseClassIRI, objPropIRI, thirdPart, "Complex"});
                    }
                }


            } else {
                JOptionPane.showMessageDialog(this, "ERROR: sorry this template will not be added. ", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "You have to upload your ontology first!", "Error", JOptionPane.ERROR_MESSAGE);
        }








    }//GEN-LAST:event_jButton1ActionPerformed

    private void templateRadioButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_templateRadioButtonActionPerformed

        jLabel23.setVisible(true);
        // TODO add your handling code here:
    }//GEN-LAST:event_templateRadioButtonActionPerformed

    private void classRadioButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_classRadioButtonActionPerformed

        jLabel23.setVisible(false);

        // TODO add your handling code here:
    }//GEN-LAST:event_classRadioButtonActionPerformed

    private void ontologyURLInputMethodTextChanged(java.awt.event.InputMethodEvent evt) {//GEN-FIRST:event_ontologyURLInputMethodTextChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_ontologyURLInputMethodTextChanged

    private void ontologyURLActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ontologyURLActionPerformed

        // TODO add your handling code here:
    }//GEN-LAST:event_ontologyURLActionPerformed

    private void uploadButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_uploadButtonActionPerformed

        //fill two combo boxes: classesCombo and annoPropertiesCombo , and Available languages list
        if (!ontologyURL.getText().isEmpty()) {

            try {
                //URL url = new URL(ontologyURL.getText().trim());

                ontologyLocation=ontologyURL.getText().trim();
                myOntology = new OntologyClass(ontologyURL.getText().trim(), true);
                ontologyIsUploaded = true;
                annoPropertiesCombo.removeAllItems();
                for (OWLAnnotationProperty ann : myOntology.getOntology().getAnnotationPropertiesInSignature()) {
                    annoPropertiesCombo.addItem(ann.getIRI());
                }

                objPropCombo.removeAllItems();
                
                //NEW---------------------
                 facetsPropertiesCombo.removeAllItems();
               //-------------------------------
                 
                DefaultListModel objPropModel = new DefaultListModel();
                for (OWLObjectProperty prop : myOntology.getOntology().getObjectPropertiesInSignature()) {
                    objPropModel.addElement(prop.getIRI());
                    //objPropCombo.addItem(prop.getIRI());
                }

                objPropModel = sortListModel(objPropModel);
                for (int i = 0; i < objPropModel.getSize(); i++) {
                    objPropCombo.addItem(objPropModel.get(i));
                    //NEW---------------------
                     facetsPropertiesCombo.addItem(objPropModel.get(i));
                     //--------------------------------------------
                }

                classesCombo.removeAllItems();
                resultCharClasseCombo.removeAllItems();
                baseClassCombo.removeAllItems();
            
               
             
               
                //NEW------------------------
                 ingredientsClassificationsCombo.removeAllItems();
                 ingredientsClassifications = new ArrayList<String>();
                 ingredientsFacetsProperties = new ArrayList<String>();
                //-------------------
                resultsCharacteristics = new ArrayList<String>();
                resultsIcons = new ArrayList<String>();
                templateIDCount = 1;
                templateIdTextField.setText("ID" + templateIDCount);
                templates = new ArrayList<String[]>();
                templatesCombo.removeAllItems();
                //------------------------

                DefaultListModel classesModel = new DefaultListModel();
                for (OWLClass cls : myOntology.getOntology().getClassesInSignature()) {
                    classesModel.addElement(cls.getIRI());
                    // classesCombo.addItem(cls.getIRI());
                    //resultCharClasseCombo.addItem(cls.getIRI());
                    //baseClassCombo.addItem(cls.getIRI());
                }

                classesModel = sortListModel(classesModel);
                for (int i = 0; i < classesModel.getSize(); i++) {
                    classesCombo.addItem(classesModel.get(i));
                    resultCharClasseCombo.addItem(classesModel.get(i));
                    baseClassCombo.addItem(classesModel.get(i));
                 
                    //NEW--------------
                    ingredientsClassificationsCombo.addItem(classesModel.get(i));
                    //-------------------------------------------
                }
                //Add the available languages to checkBoxScrollPane



                JPanel dynamicPanel = new JPanel(new BorderLayout());
                langsPanel.removeAll();
                dynamicPanel.removeAll();

                ArrayList<String> langs = new ArrayList<String>();
                langs = availableLanguages(myOntology);
                int numOfLangs = langs.size();

                //If there are languages in the ontology
                if (numOfLangs != 0) {

                    CheckListItem[] myCheckList = new CheckListItem[numOfLangs];

                    for (int i = 0; i < numOfLangs; i++) {

                        myCheckList[i] = new CheckListItem(langs.get(i).toString());
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
                    langsPanel.removeAll();
                    JScrollPane scrollPane = new JScrollPane(list);
                    scrollPane.setPreferredSize(new Dimension(50, 100));
                    scrollPane.validate();
                    dynamicPanel.add(scrollPane);
                    dynamicPanel.doLayout();
                    dynamicPanel.validate();
                    JLabel myLabel = new JLabel();
                    myLabel.setText("Available Language(s) in the ontology:");
                    langsPanel.add(myLabel, BorderLayout.NORTH);
                    langsPanel.add(dynamicPanel, BorderLayout.SOUTH);
                    ontologyPanel.validate();
                }

                classesCombo.setSelectedIndex(-1);
                annoPropertiesCombo.setSelectedIndex(-1);
                resultCharClasseCombo.setSelectedIndex(-1);
                baseClassCombo.setSelectedIndex(-1);
                objPropCombo.setSelectedIndex(-1);
                
                //NEW---------------------
                facetsPropertiesCombo.setSelectedIndex(-1);
                ingredientsClassificationsCombo.setSelectedIndex(-1);
                //-----------------------------

                ItemListener itemListener = new ItemListener() {
                    public void itemStateChanged(ItemEvent evt) {
                        JComboBox cb = (JComboBox) evt.getSource();

                        Object item = evt.getItem();

                        if (evt.getStateChange() == ItemEvent.SELECTED) {
                            componentClassTextField.setText(item.toString());
                            // Item was just selected
                        } else if (evt.getStateChange() == ItemEvent.DESELECTED) {
                            componentClassTextField.setText("");
                            // Item is no longer selected
                        }
                    }
                };
                classesCombo.addItemListener(itemListener);

                // MyItemListener actionListener = new MyItemListener();
                //classesCombo.addItemListener(actionListener);




            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Sorry. could not load the ontology.", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (final Throwable e) {
                JOptionPane.showMessageDialog(this, "Sorry. could not load the ontology." + "[" + e.getMessage() + "]", "Error", JOptionPane.ERROR_MESSAGE);

            }




        } else {
            JOptionPane.showMessageDialog(this, "Please type a valid URL for ontology location!", "Error", JOptionPane.ERROR_MESSAGE);
        }









        // TODO add your handling code here:
    }//GEN-LAST:event_uploadButtonActionPerformed

    private void ingredientsClassificationsComboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ingredientsClassificationsComboActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ingredientsClassificationsComboActionPerformed

    private void facetsPropertiesComboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_facetsPropertiesComboActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_facetsPropertiesComboActionPerformed

    private void objPropComboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_objPropComboActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_objPropComboActionPerformed

    private void ingredientsClassificationButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ingredientsClassificationButtonActionPerformed

     if (ontologyIsUploaded) {

            if (ingredientsClassificationsCombo.getSelectedItem()!=null) {

                    ingredientsClassifications.add(ingredientsClassificationsCombo.getSelectedItem().toString());
                    ingredientsClassificationsCombo.setSelectedIndex(-1);
            

            } else {
                JOptionPane.showMessageDialog(this, "Please select class first.", "Error", JOptionPane.ERROR_MESSAGE);
            }

        } else {
            JOptionPane.showMessageDialog(this, "You have to upload your ontology first!", "Error", JOptionPane.ERROR_MESSAGE);
        }        
        
        
        // TODO add your handling code here:
    }//GEN-LAST:event_ingredientsClassificationButtonActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        
             if (ontologyIsUploaded) {

            if (facetsPropertiesCombo.getSelectedItem()!=null) {

                    ingredientsFacetsProperties.add(facetsPropertiesCombo.getSelectedItem().toString());
                    facetsPropertiesCombo.setSelectedIndex(-1);
            

            } else {
                JOptionPane.showMessageDialog(this, "Please selec property first.", "Error", JOptionPane.ERROR_MESSAGE);
            }

        } else {
            JOptionPane.showMessageDialog(this, "You have to upload your ontology first!", "Error", JOptionPane.ERROR_MESSAGE);
        }   
             
             
             
    }//GEN-LAST:event_jButton3ActionPerformed
    /**
     * @param args the command line arguments
     */
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel FirstStepPanel;
    private javax.swing.JPanel IngredientsCharacteristicsPanel;
    private javax.swing.JPanel QueryTemplatePanel;
    private javax.swing.JPanel SecondStepPanel;
    private javax.swing.JButton addLangButton1;
    private javax.swing.JComboBox annoPropertiesCombo;
    private javax.swing.JPanel appearancePanel;
    private javax.swing.JComboBox baseClassCombo;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JPanel characteristicsPanel;
    private javax.swing.JRadioButton classRadioButton;
    private javax.swing.JComboBox classRenderCombo;
    private javax.swing.JComboBox classesCombo;
    public javax.swing.JTextField componentClassTextField;
    private javax.swing.JTextField excludedListLabel;
    private javax.swing.JComboBox facetsPropertiesCombo;
    private javax.swing.JTextField iconURL;
    private javax.swing.JTextField includedListLabel;
    private javax.swing.JButton ingredientsClassificationButton;
    private javax.swing.JComboBox ingredientsClassificationsCombo;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JPanel langsPanel;
    private javax.swing.JTextField logoURL;
    private javax.swing.JComboBox objPropCombo;
    private javax.swing.JPanel ontologyPanel;
    private javax.swing.JTextField ontologyURL;
    private javax.swing.JComboBox resultCharClasseCombo;
    private javax.swing.JCheckBox showCheckBox;
    private javax.swing.JTextField templateIdTextField;
    private javax.swing.JTextField templateNameText;
    private javax.swing.JRadioButton templateRadioButton;
    private javax.swing.JComboBox templatesCombo;
    private javax.swing.JTextField titleLabel;
    private javax.swing.JButton uploadButton;
    private javax.swing.JButton uploadIcon;
    private javax.swing.JButton uploadLogoButton;
    private javax.swing.JButton uploadOntologyButton;
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

class MyItemListener implements ItemListener {
    // This method is called only if a new item has been selected.

    public void itemStateChanged(ItemEvent evt) {
        JComboBox cb = (JComboBox) evt.getSource();

        Object item = evt.getItem();

        if (evt.getStateChange() == ItemEvent.SELECTED) {
            System.out.println(item);
            // Item was just selected
        } else if (evt.getStateChange() == ItemEvent.DESELECTED) {
            // Item is no longer selected
        }
    }
}