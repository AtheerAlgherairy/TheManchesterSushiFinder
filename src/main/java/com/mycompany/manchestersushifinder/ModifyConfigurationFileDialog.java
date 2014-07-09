/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.manchestersushifinder;

import static com.mycompany.manchestersushifinder.NewConfigurationFileDialog.ontologyLocation;
import java.awt.Color;
import noNamespace.FinderConfigurationDocument.FinderConfiguration.AvailableLanguages.Language;
import noNamespace.FinderConfigurationDocument.FinderConfiguration.Sanctions.Sanction;
import noNamespace.ComplexTemplate;
import noNamespace.SimpleTemplate;
import noNamespace.FinderConfigurationDocument.FinderConfiguration.ResultsCharacteristics.ResultsCharacteristic;
import noNamespace.FinderConfigurationDocument.FinderConfiguration.IngredientsClassifications.Classification;
import noNamespace.FinderConfigurationDocument.FinderConfiguration.IngredientsFacets.Facet;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
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
import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import noNamespace.FinderConfigurationDocument;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlOptions;
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
     JList list;
    FinderConfigurationDocument doc;
    public static OntologyClass myOntologyClass;
    DefaultListModel myClassificationModel;
    DefaultListModel myFacetsPropModel;
    DefaultListModel myIconsModel;
    DefaultListModel mySanctionsModel;
    ArrayList<String[]> templates = new ArrayList<String[]>();
    static String IngredientClassIRIVariable;
    static boolean myOntologyIsUploaded = false;
    static int counter = 0;

    public ModifyConfigurationFileDialog(java.awt.Dialog parent, boolean modal, File fXmlFile) {
        super(parent, modal);

        initComponents();

        buttonGroup1.add(classRadioButton);
        buttonGroup1.add(templateRadioButton);

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
        myIconsModel = new DefaultListModel();
        resultsIconsList.setModel(myIconsModel);
       resultsIconsList.setCellRenderer(new ImageListCellRenderer());
        resultsIconsList.addKeyListener(new KeyListener() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_DELETE) {
                    if (myIconsModel.getSize() != 0) {
                        if (resultsIconsList.getSelectedIndex() != -1) {
                            myIconsModel.remove(resultsIconsList.getSelectedIndex());
                            resultsIconsList.revalidate();
                            resultsIconsList.repaint();
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

        //====================================
        mySanctionsModel=new DefaultListModel();
        sanctionsList.setModel(mySanctionsModel);
        sanctionsList.setCellRenderer(new SanctionsListCellRenderer());
        sanctionsList.addKeyListener(new KeyListener() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_DELETE) {
                    if (mySanctionsModel.getSize() != 0) {
                        if (sanctionsList.getSelectedIndex() != -1) {
                            mySanctionsModel.remove(sanctionsList.getSelectedIndex());
                            sanctionsList.revalidate();
                            sanctionsList.repaint();
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
    //================================================
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

        ItemListener ingredientItemListener = new ItemListener() {
            public void itemStateChanged(ItemEvent evt) {
                JComboBox cb = (JComboBox) evt.getSource();
                Object item = evt.getItem();
                if (evt.getStateChange() == ItemEvent.SELECTED) {
                    IngredientClassIRIVariable = item.toString().trim();
                    if (!componentClassTextField.getText().isEmpty()) {
                        componentClassTextField.setText(IngredientClassIRIVariable);
                    }
                    // Item was just selected
                } else if (evt.getStateChange() == ItemEvent.DESELECTED) {
                }
            }
        };
        classesCombo.addItemListener(ingredientItemListener);


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
        myOntologyClass = new OntologyClass(ontologyURL.getText().trim(), true);
        myOntologyIsUploaded = true;
        fillAllCombos(myOntologyClass);
        String ingredientClass = myDoc.getFinderConfiguration().getIngredientClass().getClass1().toString();
        IngredientClassIRIVariable = ingredientClass;
        componentClassTextField.setText(IngredientClassIRIVariable);
        for (int i = 0; i < classesCombo.getItemCount(); i++) {
            if (classesCombo.getItemAt(i).toString().equalsIgnoreCase(ingredientClass)) {
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

       
        ArrayList<String> langs = new ArrayList<String>();
        langs = availableLanguages(myOntologyClass);
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
                myClassificationModel.addElement("<" + classificationElementsArray[k].getClass1().toString().trim() + ">");
            }
        }

        //Available Facets

        if (myDoc.getFinderConfiguration().getIngredientsFacets() != null) {
            Facet[] facetsElementsArray = myDoc.getFinderConfiguration().getIngredientsFacets().getFacetArray();
            for (int k = 0; k < facetsElementsArray.length; k++) {
                myFacetsPropModel.addElement("<" + facetsElementsArray[k].getProperty().toString().trim() + ">");
            }
        }

        //=========================4-Templates===============================

        SimpleTemplate[] simpleTemplateArray = myDoc.getFinderConfiguration().getQueryTemplates().getSimpleTemplateArray();
        ComplexTemplate[] complexTemplateArray = myDoc.getFinderConfiguration().getQueryTemplates().getComplexTemplateArray();
        int numOfComplexTemplates = complexTemplateArray.length;
        int numOfSimpleTemplates = simpleTemplateArray.length;
        int numOfAvailableTemplates = numOfComplexTemplates + numOfSimpleTemplates;
        counter = numOfAvailableTemplates;


        for (int i = 0; i < numOfSimpleTemplates; i++) {
            templatesCombo.addItem(simpleTemplateArray[i].getId() + "-" + simpleTemplateArray[i].getName());
            String templateID = simpleTemplateArray[i].getId().toString();
            String templateName = simpleTemplateArray[i].getName();
            String baseClassIRI = simpleTemplateArray[i].getBaseClass().getName().toString();
            String thirdPart = simpleTemplateArray[i].getComponentClass().getName().toString();
            String show = simpleTemplateArray[i].getShow().toString();
            String objPropIRI = simpleTemplateArray[i].getProperty().getName().toString();

            templates.add(new String[]{templateID, templateName, show, baseClassIRI, objPropIRI, thirdPart, "Simple"});
        }
        for (int i = 0; i < numOfComplexTemplates; i++) {
            templatesCombo.addItem(complexTemplateArray[i].getId() + "-" + complexTemplateArray[i].getName());
            String templateID = complexTemplateArray[i].getId().toString();
            String templateName = complexTemplateArray[i].getName();
            String baseClassIRI = complexTemplateArray[i].getBaseClass().getName().toString();
            String thirdPart = complexTemplateArray[i].getTemplate().getRef().toString();
            String show = complexTemplateArray[i].getShow().toString();
            String objPropIRI = complexTemplateArray[i].getProperty().getName().toString();

            templates.add(new String[]{templateID, templateName, show, baseClassIRI, objPropIRI, thirdPart, "Complex"});
        }
        templatesCombo.setSelectedIndex(-1);

        ItemListener itemListener = new ItemListener() {
            public void itemStateChanged(ItemEvent evt) {
                JComboBox cb = (JComboBox) evt.getSource();

                Object item = evt.getItem();

                if (cb.getSelectedIndex() == -1) {
                    availableTemplatesCombo.removeAllItems();

                    for (int i = 0; i < templates.size(); i++) {
                        availableTemplatesCombo.addItem(templates.get(i)[0] + "-" + templates.get(i)[1]);
                    }
                }
                if (evt.getStateChange() == ItemEvent.SELECTED) {
                    availableTemplatesCombo.removeAllItems();

                    String tempID = item.toString().substring(0, item.toString().indexOf("-"));

                    templateIdTextField.setText(tempID);

                    for (int i = 0; i < templates.size(); i++) {
                        if (templates.get(i)[0].equalsIgnoreCase(tempID.trim())) {
                            templateNameText.setText(templates.get(i)[1]);
                            if (templates.get(i)[2].equalsIgnoreCase("yes")) {
                                showCheckBox.setSelected(true);
                            } else {
                                showCheckBox.setSelected(false);
                            }
                            if (templates.get(i)[6].equalsIgnoreCase("Simple")) {
                                classRadioButton.setSelected(true);
                                templateRadioButton.setSelected(false);
                                componentClassTextField.setText(IngredientClassIRIVariable);
                                availableTemplatesCombo.setSelectedIndex(-1);

                            } else { //Complex

                                for (int k = 0; k < availableTemplatesCombo.getItemCount(); k++) {
                                    if (availableTemplatesCombo.getItemAt(k).toString().contains(templates.get(i)[5].toString().trim())) {
                                        availableTemplatesCombo.setSelectedIndex(k);
                                    }
                                }
                                classRadioButton.setSelected(false);

                                templateRadioButton.setSelected(true);
                            }

                            for (int k = 0; k < baseClassCombo.getItemCount(); k++) {
                                if (baseClassCombo.getItemAt(k).toString().equalsIgnoreCase(templates.get(i)[3].toString().trim())) {
                                    baseClassCombo.setSelectedIndex(k);
                                }
                            }

                            for (int k = 0; k < objPropCombo.getItemCount(); k++) {
                                if (objPropCombo.getItemAt(k).toString().equalsIgnoreCase(templates.get(i)[4].toString().trim())) {
                                    objPropCombo.setSelectedIndex(k);
                                }
                            }



                        } else {

                            //1- Add other different templates 2- that do not refer to this current template
                            if (!templates.get(i)[5].equalsIgnoreCase(tempID)) {
                                availableTemplatesCombo.addItem(templates.get(i)[0] + "-" + templates.get(i)[1]);
                            }

                        }
                    }

                    for (int i = 0; i < templates.size(); i++) {
                        if (templates.get(i)[0].equalsIgnoreCase(tempID.trim())) {
                            if (templates.get(i)[6].equalsIgnoreCase("Simple")) {
                                availableTemplatesCombo.setSelectedIndex(-1);
                                availableTemplatesCombo.setEnabled(false);
                            } else {
                                availableTemplatesCombo.setEnabled(true);
                            }
                        }
                    }
                    // Item was just selected
                } else if (evt.getStateChange() == ItemEvent.DESELECTED) {

                    templateNameText.setText("");
                    showCheckBox.setSelected(false);
                    templateRadioButton.setSelected(false);
                    classRadioButton.setSelected(false);
                    baseClassCombo.setSelectedIndex(-1);
                    objPropCombo.setSelectedIndex(-1);
                    availableTemplatesCombo.setSelectedIndex(-1);

                    // Item is no longer selected
                }
            }
        };
        templatesCombo.addItemListener(itemListener);

        //===================5-Results===========================


        if (myDoc.getFinderConfiguration().getResultsCharacteristics() != null) {
            ResultsCharacteristic[] resultsCharacteristicElementsArray = myDoc.getFinderConfiguration().getResultsCharacteristics().getResultsCharacteristicArray();
            for (int k = 0; k < resultsCharacteristicElementsArray.length; k++) {
                String classIRI = resultsCharacteristicElementsArray[k].getClass1().toString().trim();
                String iconURLString = resultsCharacteristicElementsArray[k].getUrl().toString().trim();

                URL iconURL = null;
                try {
                    //Get the image
                    iconURL = new URL(iconURLString);
                } catch (MalformedURLException ex) {
                    Logger.getLogger(ModifyConfigurationFileDialog.class.getName()).log(Level.SEVERE, null, ex);
                }
                Icon iconImage = new ImageIcon(iconURL);
                // add the image to jlabel with text
                JLabel myLabel = new JLabel(classIRI, iconImage, JLabel.LEFT);

                //add the label to the panel
                JPanel myPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
                myPanel.add(myLabel);

                //Add the panel to the list model
                myIconsModel.addElement(myPanel);


            }
        }
        
        //================================6 Sanctions ===========================
        
        if (myDoc.getFinderConfiguration().getSanctions()!= null) {
            Sanction[] sanctionElementsArray = myDoc.getFinderConfiguration().getSanctions().getSanctionArray();
           for (int k = 0; k < sanctionElementsArray.length; k++) {
                
                String classD = sanctionElementsArray[k].getClassD().toString().trim();
                String classC = sanctionElementsArray[k].getClassC().toString().trim();
                String property=sanctionElementsArray[k].getProperty().toString().trim();

                mySanctionsModel.addElement(classC+","+property+","+classD);


            }
        } 


    }

    private void fillAllCombos(OntologyClass ontology) {
        annoPropertiesCombo.removeAllItems();
        classesCombo.removeAllItems();
        objPropCombo.removeAllItems();
        baseClassCombo.removeAllItems();

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
            baseClassCombo.addItem(classesModel.get(i));
        }
        baseClassCombo.setSelectedIndex(-1);

        DefaultListModel propertiesModel = new DefaultListModel();
        for (OWLObjectProperty prop : ontology.getOntology().getObjectPropertiesInSignature()) {
            propertiesModel.addElement(prop.getIRI());
        }
        propertiesModel = sortListModel(propertiesModel);
        for (int i = 0; i < propertiesModel.getSize(); i++) {
            objPropCombo.addItem(propertiesModel.get(i));

        }
        objPropCombo.setSelectedIndex(-1);




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
        jLabel2 = new javax.swing.JLabel();
        templatesCombo = new javax.swing.JComboBox();
        jPanel3 = new javax.swing.JPanel();
        baseClassCombo = new javax.swing.JComboBox();
        templateRadioButton = new javax.swing.JRadioButton();
        classRadioButton = new javax.swing.JRadioButton();
        jLabel16 = new javax.swing.JLabel();
        componentClassTextField = new javax.swing.JTextField();
        objPropCombo = new javax.swing.JComboBox();
        availableTemplatesCombo = new javax.swing.JComboBox();
        jLabel14 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        templateIdTextField = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        templateNameText = new javax.swing.JTextField();
        showCheckBox = new javax.swing.JCheckBox();
        jLabel17 = new javax.swing.JLabel();
        saveTemplateInfo = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        FourthTabPanel = new javax.swing.JPanel();
        ResultsPanel = new javax.swing.JPanel();
        resultsIconPanel = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        resultsIconsList = new javax.swing.JList();
        addIconButton = new javax.swing.JButton();
        removeIconButton = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        annoPropertiesCombo = new javax.swing.JComboBox();
        FifthTabPanel = new javax.swing.JPanel();
        SanctionsPanel = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        sanctionsList = new javax.swing.JList();
        addSanctionButton = new javax.swing.JButton();
        removeSanctionButton = new javax.swing.JButton();
        saveButton = new javax.swing.JButton();

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

        jLabel2.setText("Current Templates:");

        templatesCombo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                templatesComboActionPerformed(evt);
            }
        });

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Template Information:"));

        baseClassCombo.setEditable(true);
        baseClassCombo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                baseClassComboActionPerformed(evt);
            }
        });

        templateRadioButton.setText("Or Other Template");
        templateRadioButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                templateRadioButtonActionPerformed(evt);
            }
        });

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

        availableTemplatesCombo.setEditable(true);

        jLabel14.setText("Base class (IRI)");

        jLabel12.setText("Template ID");

        templateIdTextField.setEditable(false);

        jLabel13.setText("Name");

        jLabel17.setText("Show");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(179, 179, 179)
                .addComponent(templateIdTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(templateNameText, javax.swing.GroupLayout.PREFERRED_SIZE, 207, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(164, 164, 164))
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(classRadioButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(templateRadioButton, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(jLabel12, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel16, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel17, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(20, 20, 20)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(baseClassCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 599, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(objPropCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 599, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(componentClassTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 599, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(availableTemplatesCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(181, 181, 181)
                        .addComponent(showCheckBox)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(templateIdTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(templateNameText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(11, 11, 11)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(showCheckBox)
                    .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(11, 11, 11)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(baseClassCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(11, 11, 11)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(objPropCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(11, 11, 11)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(classRadioButton)
                    .addComponent(componentClassTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(11, 11, 11)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(templateRadioButton)
                    .addComponent(availableTemplatesCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(11, Short.MAX_VALUE))
        );

        saveTemplateInfo.setText("Save New Template");
        saveTemplateInfo.setEnabled(false);
        saveTemplateInfo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveTemplateInfoActionPerformed(evt);
            }
        });

        jButton1.setText("Remove Template");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("Add New Template");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout QueryTemplatePanelLayout = new javax.swing.GroupLayout(QueryTemplatePanel);
        QueryTemplatePanel.setLayout(QueryTemplatePanelLayout);
        QueryTemplatePanelLayout.setHorizontalGroup(
            QueryTemplatePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(QueryTemplatePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(31, 31, 31)
                .addComponent(templatesCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(97, 97, 97)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(QueryTemplatePanelLayout.createSequentialGroup()
                .addGroup(QueryTemplatePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(saveTemplateInfo, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 30, Short.MAX_VALUE))
        );
        QueryTemplatePanelLayout.setVerticalGroup(
            QueryTemplatePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(QueryTemplatePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(QueryTemplatePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(QueryTemplatePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(templatesCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButton1)
                        .addComponent(jButton2))
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(saveTemplateInfo))
        );

        QueryTemplatePanelLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jLabel2, templatesCombo});

        QueryTemplatePanelLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jButton1, jButton2});

        javax.swing.GroupLayout ThirdTabPanelLayout = new javax.swing.GroupLayout(ThirdTabPanel);
        ThirdTabPanel.setLayout(ThirdTabPanelLayout);
        ThirdTabPanelLayout.setHorizontalGroup(
            ThirdTabPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ThirdTabPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(QueryTemplatePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(16, Short.MAX_VALUE))
        );
        ThirdTabPanelLayout.setVerticalGroup(
            ThirdTabPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ThirdTabPanelLayout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addComponent(QueryTemplatePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(23, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Query Templates", ThirdTabPanel);

        ResultsPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Results icons and descriptions"));

        resultsIconPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Results Icons"));

        resultsIconsList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        jScrollPane3.setViewportView(resultsIconsList);

        addIconButton.setText("Add");
        addIconButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addIconButtonActionPerformed(evt);
            }
        });

        removeIconButton.setText("Remove");
        removeIconButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeIconButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout resultsIconPanelLayout = new javax.swing.GroupLayout(resultsIconPanel);
        resultsIconPanel.setLayout(resultsIconPanelLayout);
        resultsIconPanelLayout.setHorizontalGroup(
            resultsIconPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(resultsIconPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 648, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(resultsIconPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(addIconButton)
                    .addComponent(removeIconButton))
                .addContainerGap(27, Short.MAX_VALUE))
        );

        resultsIconPanelLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {addIconButton, removeIconButton});

        resultsIconPanelLayout.setVerticalGroup(
            resultsIconPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(resultsIconPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(resultsIconPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(resultsIconPanelLayout.createSequentialGroup()
                        .addComponent(addIconButton)
                        .addGap(18, 18, 18)
                        .addComponent(removeIconButton))
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(23, Short.MAX_VALUE))
        );

        resultsIconPanelLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {addIconButton, removeIconButton});

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
                .addContainerGap(34, Short.MAX_VALUE))
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
                .addContainerGap(34, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Results", FourthTabPanel);

        SanctionsPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Santions Assertions"));

        sanctionsList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        jScrollPane4.setViewportView(sanctionsList);

        addSanctionButton.setText("Add");
        addSanctionButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addSanctionButtonActionPerformed(evt);
            }
        });

        removeSanctionButton.setText("Remove");
        removeSanctionButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeSanctionButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout SanctionsPanelLayout = new javax.swing.GroupLayout(SanctionsPanel);
        SanctionsPanel.setLayout(SanctionsPanelLayout);
        SanctionsPanelLayout.setHorizontalGroup(
            SanctionsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(SanctionsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 591, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(SanctionsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(addSanctionButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(removeSanctionButton, javax.swing.GroupLayout.DEFAULT_SIZE, 80, Short.MAX_VALUE))
                .addContainerGap(57, Short.MAX_VALUE))
        );

        SanctionsPanelLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {addSanctionButton, removeSanctionButton});

        SanctionsPanelLayout.setVerticalGroup(
            SanctionsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(SanctionsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 184, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(SanctionsPanelLayout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addComponent(addSanctionButton)
                .addGap(18, 18, 18)
                .addComponent(removeSanctionButton)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        SanctionsPanelLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {addSanctionButton, removeSanctionButton});

        saveButton.setText("Save Changes");
        saveButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout FifthTabPanelLayout = new javax.swing.GroupLayout(FifthTabPanel);
        FifthTabPanel.setLayout(FifthTabPanelLayout);
        FifthTabPanelLayout.setHorizontalGroup(
            FifthTabPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(FifthTabPanelLayout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(SanctionsPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(92, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, FifthTabPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(saveButton, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(65, 65, 65))
        );
        FifthTabPanelLayout.setVerticalGroup(
            FifthTabPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(FifthTabPanelLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(SanctionsPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(56, 56, 56)
                .addComponent(saveButton, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(66, Short.MAX_VALUE))
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
        for (OWLClass cls : myOntologyClass.getOntology().getClassesInSignature()) {
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
            combo1 = (JComboBox) params[1];
            myClassificationModel.addElement("<" + combo1.getSelectedItem().toString() + ">");

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
        for (OWLObjectProperty prop : myOntologyClass.getOntology().getObjectPropertiesInSignature()) {
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
            combo1 = (JComboBox) params[1];
            myFacetsPropModel.addElement("<" + combo1.getSelectedItem().toString() + ">");

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

    private void baseClassComboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_baseClassComboActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_baseClassComboActionPerformed

    private void templateRadioButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_templateRadioButtonActionPerformed
        // TODO add your handling code here:
        availableTemplatesCombo.setEnabled(true);
    }//GEN-LAST:event_templateRadioButtonActionPerformed

    private void classRadioButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_classRadioButtonActionPerformed
        // TODO add your handling code here:
        availableTemplatesCombo.setEnabled(false);
    }//GEN-LAST:event_classRadioButtonActionPerformed

    private void objPropComboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_objPropComboActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_objPropComboActionPerformed

    private void templatesComboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_templatesComboActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_templatesComboActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        if (templatesCombo.getItemCount() >= 1) {
            if (templatesCombo.getSelectedItem() != null) {
                boolean deleteFlag = true;
                ArrayList<String> usedByTemplate = new ArrayList<String>();
                String item = templatesCombo.getSelectedItem().toString();
                String temp = item.substring(0, item.indexOf("-"));

                for (int i = 0; i < templates.size(); i++) {
                    //Check whether the template is referred to by other template
                    if (templates.get(i)[5].equalsIgnoreCase(temp) && templates.get(i)[6].equalsIgnoreCase("Complex")) {
                        deleteFlag = false;
                        usedByTemplate.add(templates.get(i)[0] + "-" + templates.get(i)[1]);
                    }
                }

                if (deleteFlag) //We can safely remove the item form the combo and templates array
                {
                    int n = JOptionPane.showConfirmDialog(this, "Are you sure do you want to remove the selected template?", "Remove Warning", JOptionPane.YES_NO_OPTION);
                    if (n == 0) //yes?
                    {
                        templatesCombo.removeItemAt(templatesCombo.getSelectedIndex());

                        for (int i = 0; i < availableTemplatesCombo.getItemCount(); i++) {
                            if (availableTemplatesCombo.getItemAt(i).toString().equalsIgnoreCase(item)) {
                                availableTemplatesCombo.removeItemAt(i);
                            }
                        }
                        for (int i = 0; i < templates.size(); i++) {

                            if (templates.get(i)[0].equalsIgnoreCase(temp)) {
                                templates.remove(i);
                            }
                        }
                    }
                } else {

                    JOptionPane.showMessageDialog(this, "You can't remove the selected template.\nIt is used by the following template(s):\n" + usedByTemplate, "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please select a template first..", "Error", JOptionPane.ERROR_MESSAGE);
            }

        } else {
            JOptionPane.showMessageDialog(this, "There is no template to be removed..", "Error", JOptionPane.ERROR_MESSAGE);
        }


    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        counter = counter + 1;
        templateIdTextField.setText("ID" + counter);
        templateNameText.setText("");
        showCheckBox.setSelected(true);
        baseClassCombo.setSelectedIndex(-1);
        objPropCombo.setSelectedIndex(-1);
        templatesCombo.setSelectedIndex(-1);

        componentClassTextField.setText(IngredientClassIRIVariable);

        availableTemplatesCombo.removeAllItems();
        for (int i = 0; i < templates.size(); i++) {
            availableTemplatesCombo.addItem(templates.get(i)[0] + "-" + templates.get(i)[1]);
        }
        availableTemplatesCombo.setSelectedIndex(-1);
        classRadioButton.setSelected(true);
        templateRadioButton.setSelected(false);

        saveTemplateInfo.setEnabled(true);


    }//GEN-LAST:event_jButton2ActionPerformed

    private void saveTemplateInfoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveTemplateInfoActionPerformed
        // TODO add your handling code here:

        String baseClassIRI = null;
        String objPropIRI = null;
        String thirdPart = null;
        String templateName = null;
        String show = null;
        String tempIDAndName = null;
        String templateID = templateIdTextField.getText();


        boolean flag1 = false; //temp Name
        boolean flag2 = false; //Show
        boolean flag3 = false; //base
        boolean flag4 = false; //property
        boolean flag5 = false; //simple template
        boolean flag6 = false; //complex template

        if (myOntologyIsUploaded) {

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
                    JOptionPane.showMessageDialog(this, "You have to select a component class first! \n [in Ontology Tab]", "Error", JOptionPane.ERROR_MESSAGE);
                }


            } else {
                if (availableTemplatesCombo.getSelectedItem() != null) {

                    tempIDAndName = availableTemplatesCombo.getSelectedItem().toString();
                    thirdPart = tempIDAndName.substring(0, tempIDAndName.indexOf("-")).trim();
                    flag6 = true;
                } else {
                    JOptionPane.showMessageDialog(this, "You have to select template from the list." + "[or select the first option (component class)]", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }


            if (flag1 && flag2 && flag3 && flag4 && (flag5 || flag6)) {
                String infoMsg;
                if (flag5) {
                    infoMsg = "\nID: [" + templateID + "]\nName: [" + templateName + "]\nShow: [" + show + "]\nBase class: [" + baseClassIRI + "]\nProperty: [" + objPropIRI + "]\nComponent class: [" + thirdPart + "]";
                } else {
                    infoMsg = "\nID: [" + templateID + "]\nName: [" + templateName + "]\nShow: [" + show + "]\nBase class: [" + baseClassIRI + "]\nProperty: [" + objPropIRI + "]\nTemplate: [" + tempIDAndName + "]";

                }
                int ans;
                ans = JOptionPane.showConfirmDialog(this, "Are you sure you want to add the following template??\n\nTemplate Info:" + infoMsg, "Add template", JOptionPane.YES_NO_OPTION);
                if (ans == JOptionPane.YES_OPTION) {

                    //------------add to available templates---------------------
                    templatesCombo.addItem(templateID + "-" + templateName);

                    saveTemplateInfo.setEnabled(false);
                    //templatesCombo.setSelectedIndex(-1);
                    //objPropCombo.setSelectedIndex(-1);
                    //baseClassCombo.setSelectedIndex(-1);
                    //classRadioButton.setSelected(true);
                    //templateNameText.setText("");

                    //-------------add to templates array list--------
                    if (flag5) {
                        templates.add(new String[]{templateID, templateName, show, baseClassIRI, objPropIRI, thirdPart, "Simple"});
                        //showCheckBox.setSelected(true);
                    } else {
                        templates.add(new String[]{templateID, templateName, show, baseClassIRI, objPropIRI, thirdPart, "Complex"});
                        //showCheckBox.setSelected(true);
                    }
                }


            } else {
                JOptionPane.showMessageDialog(this, "ERROR: sorry this template will not be added. ", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "You have to upload your ontology first!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_saveTemplateInfoActionPerformed

    private void addIconButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addIconButtonActionPerformed
        // TODO add your handling code here:

        //Show dialog to add class & file chooser for Icon URL
        JComboBox combo = new JComboBox();
        DefaultListModel classesModel = new DefaultListModel();
        for (OWLClass cls : myOntologyClass.getOntology().getClassesInSignature()) {
            classesModel.addElement(cls.getIRI());

        }

        classesModel = sortListModel(classesModel);
        for (int i = 0; i < classesModel.getSize(); i++) {
            combo.addItem(classesModel.get(i));
        }
        
         JPanel classpanel=new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel classLabel=new JLabel("Class: ");
        classpanel.add(classLabel);
        classpanel.add(combo);

        final JFileChooser fileChooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Image Files", "jpg", "png", "gif", "jpeg");
        fileChooser.setFileFilter(filter);
        fileChooser.addChoosableFileFilter(filter);
        fileChooser.setAcceptAllFileFilterUsed(false);
       
        JPanel panel=new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel myLabel=new JLabel("Icon URL");
      
        panel.add(myLabel);
        final JTextField myText=new JTextField();
      
         myText.setMaximumSize(new Dimension(400,23));
       myText.setMinimumSize(new Dimension(400,23));
        myText.setPreferredSize(new Dimension(400,23));
        myText.repaint();
       
        panel.add(myText);
        
       Action backAction = new AbstractAction("Browse") {
            public void actionPerformed(ActionEvent e) {
               
              fileChooser.showOpenDialog(ModifyConfigurationFileDialog.this);
              File file = fileChooser.getSelectedFile();
            String URLString = null;
            if (file != null) {

                try {
                    URLString = file.toURI().toURL().toString();
                    myText.setText(URLString);
                } catch (MalformedURLException ex) {
                    Logger.getLogger(NewConfigurationFileDialog.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
                 
            }

        
        };
        JButton myButton=new JButton(backAction);
       myButton.setMaximumSize(new Dimension(80,23));
       myButton.setMinimumSize(new Dimension(80,23));
        myButton.setPreferredSize(new Dimension(80,23));
        myButton.repaint();
        
        panel.add(myButton);

        String message = "Please Select class and Icon to be added:";
        Object[] params = {message, classpanel,panel};
        int n = JOptionPane.showConfirmDialog(this, params, "Add Result Icon", JOptionPane.OK_CANCEL_OPTION);

        if (n == 0) { //yes?
            
            JPanel myPanel=new JPanel();
            myPanel=(JPanel) params[1];
            
            JComboBox combo1 = new JComboBox();
            combo1 = (JComboBox) myPanel.getComponent(1);
            
            JPanel p=new JPanel();
            p=(JPanel) params[2];
           
            JTextField m=new JTextField();
             m=(JTextField) p.getComponent(1);
            
             URL iconURL=null;
            try {
                iconURL = new URL(m.getText());
            } catch (MalformedURLException ex) {
                Logger.getLogger(ModifyConfigurationFileDialog.class.getName()).log(Level.SEVERE, null, ex);
            }
             
            Icon iconImage = new ImageIcon(iconURL);

            JLabel label = new JLabel(combo1.getSelectedItem().toString(), iconImage, JLabel.LEFT);
            JPanel myPanel1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
            myPanel1.add(label);
            myIconsModel.addElement(myPanel1);

        }


    }//GEN-LAST:event_addIconButtonActionPerformed

    private void removeIconButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeIconButtonActionPerformed
        // TODO add your handling code here:
        
         if (!myIconsModel.isEmpty()) {

            if (resultsIconsList.getSelectedIndex() != -1) {
                myIconsModel.remove(resultsIconsList.getSelectedIndex());
                resultsIconsList.revalidate();
                resultsIconsList.repaint();
            }
        }
    }//GEN-LAST:event_removeIconButtonActionPerformed

    private void removeSanctionButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeSanctionButtonActionPerformed
        // TODO add your handling code here:
             if (!mySanctionsModel.isEmpty()) {

            if (sanctionsList.getSelectedIndex() != -1) {
                mySanctionsModel.remove(sanctionsList.getSelectedIndex());
                sanctionsList.revalidate();
                sanctionsList.repaint();
            }
        }
        
    }//GEN-LAST:event_removeSanctionButtonActionPerformed

    private void addSanctionButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addSanctionButtonActionPerformed
        // TODO add your handling code here:
        
        //Show dialog to add new sanction [class c, property & class D]
        JComboBox combo = new JComboBox();
        DefaultListModel classesModel = new DefaultListModel();
        for (OWLClass cls : myOntologyClass.getOntology().getClassesInSignature()) {
            classesModel.addElement(cls.getIRI());
        }
        classesModel = sortListModel(classesModel);
        for (int i = 0; i < classesModel.getSize(); i++) {
            combo.addItem(classesModel.get(i));
        }
        
        JComboBox combo2 = new JComboBox();
        DefaultListModel popertiesModel = new DefaultListModel();
        for (OWLObjectProperty p : myOntologyClass.getOntology().getObjectPropertiesInSignature()) {
            popertiesModel.addElement(p.getIRI());
        }
        popertiesModel = sortListModel(popertiesModel);
        for (int i = 0; i < popertiesModel.getSize(); i++) {
            combo2.addItem(popertiesModel.get(i));
        }
        
         JComboBox combo3 = new JComboBox();
        DefaultListModel classesModel3 = new DefaultListModel();
        for (OWLClass cls : myOntologyClass.getOntology().getClassesInSignature()) {
            classesModel3.addElement(cls.getIRI());
        }
        classesModel3 = sortListModel(classesModel3);      
        for (int i = 0; i < classesModel3.getSize(); i++) {
            combo3.addItem(classesModel3.get(i));
        }
        
         JPanel panel1=new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel myLabel=new JLabel("Class C:");
        panel1.add(myLabel);
        panel1.add(combo);
        
        JPanel panel2=new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel myLabel2=new JLabel("Property:");
        panel2.add(myLabel2);
        panel2.add(combo2);
        
         JPanel panel3=new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel myLabel3=new JLabel("Class D:");
        panel3.add(myLabel3);
        panel3.add(combo3);
        
         String message = "Please Select class C, property and class D for the new assertion:";
        Object[] params = {message,panel1, panel2,panel3};
        int n = JOptionPane.showConfirmDialog(this, params, "Add Sanction Assertion", JOptionPane.OK_CANCEL_OPTION);

        if (n == 0) { //if OK
            
            JPanel p1=new JPanel();
            p1=(JPanel) params[1];
            JComboBox firstCombo = new JComboBox();
            firstCombo = (JComboBox) p1.getComponent(1);
            
            JPanel p2=new JPanel();
            p2=(JPanel) params[2];
            JComboBox secondCombo = new JComboBox();
            secondCombo = (JComboBox) p2.getComponent(1);
            
            JPanel p3=new JPanel();
            p3=(JPanel) params[3];
            JComboBox thirdCombo = new JComboBox();
            thirdCombo = (JComboBox) p3.getComponent(1);
            
            mySanctionsModel.addElement(firstCombo.getSelectedItem()+","+secondCombo.getSelectedItem()+","+thirdCombo.getSelectedItem());
            
            
        }
        
        
        
    }//GEN-LAST:event_addSanctionButtonActionPerformed

    private void saveButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveButtonActionPerformed

        
        // TODO add your handling code here:
        
          int ans = JOptionPane.showConfirmDialog(this,"Are you sure you want to save changes?" , "Save Changes", JOptionPane.YES_NO_OPTION);

        if (ans == 0) { //if OK
     
        if (logoURL.getText() != null) {
            doc.getFinderConfiguration().getLogo().setURL(logoURL.getText().trim()); //get the URL
        }

       
        if (includedListLabel.getText() != null) {
             doc.getFinderConfiguration().getIncludedLabel().setText(includedListLabel.getText().trim());
        }

      
        if (excludedListLabel.getText() != null) {
             doc.getFinderConfiguration().getExcludedLabel().setText(excludedListLabel.getText().trim());
        }

       
        if (titleLabel.getText() != null) {
             doc.getFinderConfiguration().getTitleLabel().setText(titleLabel.getText().trim());
        }

       

        if (!ontologyLocation.isEmpty()) {
             doc.getFinderConfiguration().getOntologyLocation().setUrl(ontologyLocation);
        }

       
        if (classesCombo.getSelectedItem() != null) {
             doc.getFinderConfiguration().getIngredientClass().setClass1(classesCombo.getSelectedItem().toString());
        }

      
        if (annoPropertiesCombo.getSelectedItem() != null) {
             doc.getFinderConfiguration().getResultsAnnotationProperty().setProperty(annoPropertiesCombo.getSelectedItem().toString());
        }

     
        //-----------remove all languages-------------
     
        for(int i=0;i<doc.getFinderConfiguration().getAvailableLanguages().sizeOfLanguageArray();i++)
        {
            if(doc.getFinderConfiguration().getAvailableLanguages().getLanguageArray(i)!=null)
            {
            doc.getFinderConfiguration().getAvailableLanguages().removeLanguage(i);
            }
            i--;
        }

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
              
                doc.getFinderConfiguration().getAvailableLanguages().addNewLanguage().setName("default");
                
            }


            for (int i = 0; i < selectedLangs.size(); i++) {
                String value = selectedLangs.get(i).toString().trim().toLowerCase();
                 doc.getFinderConfiguration().getAvailableLanguages().addNewLanguage().setName(value);
            }
        }



        if (classRenderCombo.getSelectedItem() != null) {
            doc.getFinderConfiguration().getClassRendering().setUse(classRenderCombo.getSelectedItem().toString());
        } else {
            doc.getFinderConfiguration().getClassRendering().setUse("IRI");
        }

       //-----result icons-----------
         
        for(int i=0;i<doc.getFinderConfiguration().getResultsCharacteristics().sizeOfResultsCharacteristicArray();i++)
        {
            if(doc.getFinderConfiguration().getResultsCharacteristics().getResultsCharacteristicArray(i)!=null)
            {
            doc.getFinderConfiguration().getResultsCharacteristics().removeResultsCharacteristic(i);
            }
            i--;
        }
        for (int i = 0; i < myIconsModel.size() ;i++) {
            
            Object object= myIconsModel.get(i);
            JPanel panel=new JPanel();
                    panel=(JPanel) object;
                 JLabel label=new JLabel();
               label=(JLabel)panel.getComponent(0);
      
                String value = label.getText().toString();
                String iconURL=label.getIcon().toString();
                ResultsCharacteristic res= doc.getFinderConfiguration().getResultsCharacteristics().addNewResultsCharacteristic();
                res.setClass1(value);
                res.setUrl(iconURL);
              
        }
        
        //-----Ingredients Facets-----------
        
        //check if there is (IngredientsFacets) element or not
        // if yes, remove all childern 
        if(doc.getFinderConfiguration().getIngredientsFacets()!=null)
        {
        for(int i=0;i<doc.getFinderConfiguration().getIngredientsFacets().sizeOfFacetArray();i++)
        {
            if(doc.getFinderConfiguration().getIngredientsFacets().getFacetArray(i)!=null)
            {
            doc.getFinderConfiguration().getIngredientsFacets().removeFacet(i);
            }
            i--;
        }
        } 
        else //if no, create new one.
        {
              doc.getFinderConfiguration().addNewIngredientsFacets();
        }
        
        for (int i = 0; i < myFacetsPropModel.size() ;i++) {
            String propIRI=myFacetsPropModel.get(i).toString().substring(1, myFacetsPropModel.get(i).toString().length()-1);
           doc.getFinderConfiguration().getIngredientsFacets().addNewFacet().setProperty(propIRI);
        }
        
      //-----Ingredients Classifications-----------
        
        //check if there is (IngredientsClassifications) element or not
        // if yes, remove all childern 
        if(doc.getFinderConfiguration().getIngredientsClassifications()!=null)
        {
        for(int i=0;i<doc.getFinderConfiguration().getIngredientsClassifications().sizeOfClassificationArray();i++)
        {
            if(doc.getFinderConfiguration().getIngredientsClassifications().getClassificationArray(i)!=null)
            {
            doc.getFinderConfiguration().getIngredientsClassifications().removeClassification(i);
            }
            i--;
        }
        } 
        else //if no, create new one.
        {
              doc.getFinderConfiguration().addNewIngredientsClassifications();
        }
        
        for (int i = 0; i < myClassificationModel.size() ;i++) {
            String classIRI=myClassificationModel.get(i).toString().substring(1, myClassificationModel.get(i).toString().length()-1);
           doc.getFinderConfiguration().getIngredientsClassifications().addNewClassification().setClass1(classIRI);
        }

        
        //-----------------------------------
        //Query Template
        
        //Remove all complex Template
         for(int i=0;i<doc.getFinderConfiguration().getQueryTemplates().sizeOfComplexTemplateArray();i++)
        {
            if(doc.getFinderConfiguration().getQueryTemplates().getComplexTemplateArray(i)!=null)
            {
            doc.getFinderConfiguration().getQueryTemplates().removeComplexTemplate(i);
            }
            i--;
        }
         
        //Remove all Simple Template
         for(int i=0;i<doc.getFinderConfiguration().getQueryTemplates().sizeOfSimpleTemplateArray();i++)
        {
            if(doc.getFinderConfiguration().getQueryTemplates().getSimpleTemplateArray(i)!=null)
            {
            doc.getFinderConfiguration().getQueryTemplates().removeSimpleTemplate(i);
            }
            i--;
        }

         //Add all templates..
         
           if (!templates.isEmpty()) {
            for (int i = 0; i < templates.size(); i++) {
                if (templates.get(i)[6].equalsIgnoreCase("Simple")) { //template type

                    SimpleTemplate simpleTemplateElement = doc.getFinderConfiguration().getQueryTemplates().addNewSimpleTemplate();
                    simpleTemplateElement.setId(templates.get(i)[0]); //id
                    simpleTemplateElement.setName(templates.get(i)[1]); //name

                    if (templates.get(i)[2].equalsIgnoreCase("yes")) //show
                    {
                        simpleTemplateElement.setShow(SimpleTemplate.Show.YES);
                    } else {
                        simpleTemplateElement.setShow(SimpleTemplate.Show.NO);
                    }

                    SimpleTemplate.BaseClass baseClassElement = simpleTemplateElement.addNewBaseClass();
                    baseClassElement.setName(templates.get(i)[3]); //base IRI

                    SimpleTemplate.Property propertyElement = simpleTemplateElement.addNewProperty();
                    propertyElement.setName(templates.get(i)[4]); //property IRI

                    SimpleTemplate.ComponentClass componentClassElement = simpleTemplateElement.addNewComponentClass();
                    componentClassElement.setName(templates.get(i)[5]); //component IRI


                } else //complex template
                {
                    ComplexTemplate complexTemplateElement = doc.getFinderConfiguration().getQueryTemplates().addNewComplexTemplate();
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

           //----------------------------------------------
           
           //Sanctions    
        //check if there is (Sanctions) element or not
        // if yes, remove all childern 
        if(doc.getFinderConfiguration().getSanctions()!=null)
        {
        for(int i=0;i<doc.getFinderConfiguration().getSanctions().sizeOfSanctionArray();i++)
        {
            if(doc.getFinderConfiguration().getSanctions().getSanctionArray(i)!=null)
            {
            doc.getFinderConfiguration().getSanctions().removeSanction(i);
            }
            i--;
        }
        } 
        else //if no, create new one.
        {
              doc.getFinderConfiguration().addNewSanctions();
        }
        
        for (int i = 0; i < mySanctionsModel.size() ;i++) {
              String classC = mySanctionsModel.get(i).toString().substring(0, mySanctionsModel.get(i).toString().indexOf(","));
            String property = mySanctionsModel.get(i).toString().substring(mySanctionsModel.get(i).toString().indexOf(",")+1, mySanctionsModel.get(i).toString().lastIndexOf(","));
             String classD = mySanctionsModel.get(i).toString().substring(mySanctionsModel.get(i).toString().lastIndexOf(",")+1);
            
            Sanction sanctionElement= doc.getFinderConfiguration().getSanctions().addNewSanction();
           sanctionElement.setClassC(classC);
           sanctionElement.setClassD(classD);
           sanctionElement.setProperty(property);
           
           
        }

           
           

        if (doc.validate()) {
           
                    XmlOptions xmlOptions = new XmlOptions();
                    xmlOptions.setSavePrettyPrint();
                    xmlOptions.setSavePrettyPrintIndent(4);
            try {
                doc.save(fXmlFile, xmlOptions);
                
               //After Saving the new file. Do you want to use it?
                    int answer;
                    answer = JOptionPane.showConfirmDialog(this, "Do you want to use this configuration file??\n\n", "Modified Configuration File", JOptionPane.YES_NO_OPTION);
                    if (answer == JOptionPane.YES_OPTION) {
                        Global.useDefault = false;
                        Global.configFileURL = fXmlFile.getPath().toString();
                        StartFrame.qframe.dispose();
                        StartFrame newFrame = new StartFrame();
                        newFrame.setVisible(true);
                        this.dispose();

                    } else {
                        this.dispose();
                    }
                
            } catch (IOException ex) {
                Logger.getLogger(ModifyConfigurationFileDialog.class.getName()).log(Level.SEVERE, null, ex);
            }
                   
                    
        } else {
            JOptionPane.showMessageDialog(this, "  Not valid Sorry.. Cannot Save the file!", "Error", JOptionPane.ERROR_MESSAGE);
        }

        }
    }//GEN-LAST:event_saveButtonActionPerformed
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
    private javax.swing.JButton addIconButton;
    private javax.swing.JButton addSanctionButton;
    private javax.swing.JComboBox annoPropertiesCombo;
    private javax.swing.JPanel appearancePanel;
    private javax.swing.JComboBox availableTemplatesCombo;
    private javax.swing.JComboBox baseClassCombo;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JRadioButton classRadioButton;
    private javax.swing.JComboBox classRenderCombo;
    private javax.swing.JComboBox classesCombo;
    private javax.swing.JList classificationList;
    public javax.swing.JTextField componentClassTextField;
    private javax.swing.JTextField excludedListLabel;
    private javax.swing.JList facetsPropList;
    private javax.swing.JTextField includedListLabel;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JPanel languagesPanel;
    public javax.swing.JLabel logoLabel;
    private javax.swing.JTextField logoURL;
    private javax.swing.JComboBox objPropCombo;
    private javax.swing.JPanel ontologyBasicPanel;
    private javax.swing.JTextField ontologyURL;
    private javax.swing.JButton removeClassificationButton;
    private javax.swing.JButton removeFacetsButton;
    private javax.swing.JButton removeIconButton;
    private javax.swing.JButton removeSanctionButton;
    private javax.swing.JPanel resultsIconPanel;
    private javax.swing.JList resultsIconsList;
    private javax.swing.JList sanctionsList;
    private javax.swing.JButton saveButton;
    private javax.swing.JButton saveTemplateInfo;
    private javax.swing.JCheckBox showCheckBox;
    private javax.swing.JTextField templateIdTextField;
    private javax.swing.JTextField templateNameText;
    private javax.swing.JRadioButton templateRadioButton;
    private javax.swing.JComboBox templatesCombo;
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

class ImageListCellRenderer implements ListCellRenderer {

    public Component getListCellRendererComponent(JList jlist,
            Object value,
            int cellIndex,
            boolean isSelected,
            boolean cellHasFocus) {
        if (value instanceof JPanel) {

           
            Component component = (Component) value;         
            component.setForeground(Color.white);
            component.setBackground(isSelected ? Color.LIGHT_GRAY : Color.white);
            return component;
        } else {
            // TODO - I get one String here when the JList is first rendered; proper way to deal with this?
            //System.out.println("Got something besides a JPanel: " + value.getClass().getCanonicalName());
            return new JLabel("???");
        }
    }
}

class SanctionsListCellRenderer implements ListCellRenderer {


   
    protected DefaultListCellRenderer defaultRenderer = new DefaultListCellRenderer();

    public Component getListCellRendererComponent(JList list, Object value, int index,
            boolean isSelected, boolean cellHasFocus) {

        JLabel rendererLabel = (JLabel) defaultRenderer.getListCellRendererComponent(list, value, index,
                isSelected, cellHasFocus);

      String label="";
            String classC = value.toString().substring(0, value.toString().indexOf(","));
          
            IRI classCIRI = IRI.create(classC);
            OWLClass cls = ModifyConfigurationFileDialog.myOntologyClass.getDf().getOWLClass(classCIRI);
            label="[Class C: "+ModifyConfigurationFileDialog.myOntologyClass.getOWLClassAlternativeLanguage(cls, "default");
            
            String property = value.toString().substring(value.toString().indexOf(",")+1, value.toString().lastIndexOf(","));
            IRI propIRI = IRI.create(property);
            OWLObjectProperty prop = ModifyConfigurationFileDialog.myOntologyClass.getDf().getOWLObjectProperty(propIRI);
            label=label+" , Property R:"+ModifyConfigurationFileDialog.myOntologyClass.getOWLObjectPropertyAlternativeLanguage(prop, "default");
            
            String classD = value.toString().substring(value.toString().lastIndexOf(",")+1);
            IRI classDIRI = IRI.create(classD);
            OWLClass cls1 =ModifyConfigurationFileDialog.myOntologyClass.getDf().getOWLClass(classDIRI);
            label=label+" , Class D: "+ModifyConfigurationFileDialog.myOntologyClass.getOWLClassAlternativeLanguage(cls1, "default");
            
            rendererLabel.setText(label+"]");

        
        return rendererLabel;
    }
}
