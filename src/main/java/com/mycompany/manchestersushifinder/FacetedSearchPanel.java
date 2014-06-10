/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.manchestersushifinder;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.lang.reflect.Array;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Set;
import java.util.TreeSet;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;

/**
 *
 * @author Atheer
 */
public class FacetedSearchPanel extends javax.swing.JPanel {

    private OntologyClass myOntologyClass;
    private Set<OWLClass> selectedClasses = new TreeSet<OWLClass>();
    private String lang;
    private ArrayList<OWLClass> ingredientsCharacteristics;
    public static JList characheristicsList;

    public FacetedSearchPanel(String lang, OntologyClass myOntologyClass, ArrayList<OWLClass> ingredientsCharacteristics) {
        initComponents();
        this.lang = lang;
        this.myOntologyClass = myOntologyClass;
        this.ingredientsCharacteristics = ingredientsCharacteristics;
        fillIngredientsCharacteristicsLists();
        buildList();
    }

    public void fillIngredientsCharacteristicsLists() {
        if (!ingredientsCharacteristics.isEmpty()) {
            addCharacteristicsCheckboxes(ingredientsCharacteristics);
        }

    }

    private void addCharacteristicsCheckboxes(ArrayList<OWLClass> classes) {

        JPanel dynamicPanel = new JPanel();
        int numOfClasses = classes.size();
        CheckListItem[] myCheckList = new CheckListItem[numOfClasses];

        for (int i = 0; i < numOfClasses; i++) {

            myCheckList[i] = new CheckListItem(classes.get(i));
        }
        characheristicsList = new JList(myCheckList);
        characheristicsList.setPreferredSize(new Dimension(200, 100));
        characheristicsList.setMinimumSize(new Dimension(200, 100));
        characheristicsList.setVisibleRowCount(5);
        characheristicsList.setCellRenderer(new CheckListRenderer());
        characheristicsList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        characheristicsList.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent event) {
                JList list = (JList) event.getSource();
                int index = list.locationToIndex(event.getPoint());
                CheckListItem item = (CheckListItem) list.getModel().getElementAt(index);
                item.setSelected(!item.isSelected());

                if (item.isSelected()) {
                    selectedClasses.add(item.getOWLClass());
                    buildList();
                } else {
                    selectedClasses.remove(item.getOWLClass());
                    buildList();
                }
                list.repaint(list.getCellBounds(index, index));
            }
        });

        characheristicsList.validate();
        JScrollPane scrollPane= new JScrollPane(characheristicsList);    
        dynamicPanel.add(scrollPane);    
        dynamicPanel.doLayout();
        dynamicPanel.validate();
        characteristicsPanel.add(dynamicPanel);
        characteristicsPanel.validate();
    }

    private void buildList() {
        JScrollPane scrollPane = new JScrollPane();
        DefaultListModel ingredientsListModel = new DefaultListModel();


        QueryTemplateEngine myEngine = new QueryTemplateEngine(myOntologyClass.getDf(), myOntologyClass.getOntologyIRI(), myOntologyClass.getReasoner());
        Collection resultCollection = myEngine.getIngredientsWithCharacteristics(selectedClasses);
        Object[] arr = resultCollection.toArray();

        for (int i = 0; i < arr.length; i++) {
            ingredientsListModel.addElement(arr[i]);
        }

        JList ingredientsList = new JList(ingredientsListModel);
        ListCellRenderer myRenderer = new OWLClassListCellRenderer();


        FacetedSearchPanel.ingredientsList.setCellRenderer(myRenderer);
        FacetedSearchPanel.ingredientsList.setDragEnabled(true);

        FacetedSearchPanel.ingredientsList.setModel(ingredientsListModel);
        FacetedSearchPanel.ingredientsList.validate();
        FacetedSearchPanel.ingredientsList.repaint();
        scrollPane.add(ingredientsList);
        scrollPane.validate();
        ingredientsPanel.add(scrollPane);
        ingredientsPanel.validate();


    }

    class OWLClassListCellRenderer implements ListCellRenderer {

        protected DefaultListCellRenderer defaultRenderer = new DefaultListCellRenderer();

        public Component getListCellRendererComponent(JList list, Object value, int index,
                boolean isSelected, boolean cellHasFocus) {

            JLabel rendererLabel = (JLabel) defaultRenderer.getListCellRendererComponent(list, value, index,
                    isSelected, cellHasFocus);

            String str = value.toString().substring(1, value.toString().length() - 1);
            IRI classIRI = IRI.create(str);
            OWLClass cls = Global.myOntology.getDf().getOWLClass(classIRI);
            rendererLabel.setText(Global.myOntology.getOWLClassAlternativeLanguage(cls, QueryInterface.selectedLanguage));

            return rendererLabel;
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        ingredientsPanel = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        ingredientsList = new javax.swing.JList();
        characteristicsPanel = new javax.swing.JPanel();

        setLayout(new java.awt.BorderLayout());

        ingredientsPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Ingredients"));

        jScrollPane1.setViewportView(ingredientsList);

        javax.swing.GroupLayout ingredientsPanelLayout = new javax.swing.GroupLayout(ingredientsPanel);
        ingredientsPanel.setLayout(ingredientsPanelLayout);
        ingredientsPanelLayout.setHorizontalGroup(
            ingredientsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ingredientsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 223, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        ingredientsPanelLayout.setVerticalGroup(
            ingredientsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ingredientsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 429, Short.MAX_VALUE)
                .addContainerGap())
        );

        add(ingredientsPanel, java.awt.BorderLayout.CENTER);

        characteristicsPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Characterstics:"));
        characteristicsPanel.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        characteristicsPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));
        add(characteristicsPanel, java.awt.BorderLayout.PAGE_START);
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    public static javax.swing.JPanel characteristicsPanel;
    public static javax.swing.JList ingredientsList;
    private javax.swing.JPanel ingredientsPanel;
    public javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables
}

class CheckListRenderer extends JCheckBox implements ListCellRenderer {

    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean hasFocus) {
        setEnabled(list.isEnabled());
        setSelected(((CheckListItem) value).isSelected());
        setFont(list.getFont());
        setBackground(list.getBackground());
        setForeground(list.getForeground());
        setText(Global.myOntology.getOWLClassAlternativeLanguage(((CheckListItem) value).getOWLClass(), QueryInterface.selectedLanguage));
        return this;
    }
}

class CheckListItem {

    private String label;
    private OWLClass cls;
    private boolean isSelected = false;

    public CheckListItem(OWLClass cls) {

        this.cls = cls;
        String str = cls.toString().substring(1, cls.toString().length() - 1);
        IRI classIRI = IRI.create(str);
        OWLClass c = Global.myOntology.getDf().getOWLClass(classIRI);
        this.label = Global.myOntology.getOWLClassAlternativeLanguage(c, QueryInterface.selectedLanguage);

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

    public OWLClass getOWLClass() {
        return cls;
    }
}
