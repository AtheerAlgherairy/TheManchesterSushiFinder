/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.manchestersushifinder;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;
import javax.swing.AbstractButton;
import javax.swing.DefaultListModel;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLObjectProperty;



/**
 *
 * @author Atheer
 */
public class NewFacetedSearchPanel extends javax.swing.JPanel {

    private OntologyClass myOntologyClass;
    private Set<OWLClassExpression> selectedClasses = new TreeSet<OWLClassExpression>();
    private String lang;
    private ArrayList<OWLObjectProperty> factesProperties;
    ArrayList<Data> facets=new ArrayList<Data> ();
    //JPanel dynamicPanel = new JPanel();
    QueryTemplateEngine myEngine;


    
    public NewFacetedSearchPanel(String lang, OntologyClass myOntologyClass, ArrayList<OWLObjectProperty> factesProperties) {
           
        myEngine= new QueryTemplateEngine(myOntologyClass.getDf(), myOntologyClass.getOntologyIRI(), myOntologyClass.getReasoner());
        initComponents();
        this.lang = lang;
        this.myOntologyClass = myOntologyClass;
        this.factesProperties = factesProperties;
        fillFactesProperties();
        addFactesCheckboxes();
       buildList();
    }

    private void fillFactesProperties() {
        if (!factesProperties.isEmpty()) {
            for(int i=0;i<factesProperties.size();i++)
            {
               // OWLClassExpression firstExpr = null;
                if (!factesProperties.get(i).getRanges(myOntologyClass.getOntology()).isEmpty())
                {
                    OWLObjectProperty p=factesProperties.get(i);
                   Set<OWLClassExpression> expressions=p.getRanges(myOntologyClass.getOntology());
                   OWLClassExpression resultExpr = myOntologyClass.getDf().getOWLObjectIntersectionOf(expressions);
                   
                  // Object[] expressionsArray= expressions.toArray();
                   
                    //firstExpr= (OWLClassExpression) expressionsArray[0];
                    
                   // Data f=new Data (p,(OWLClass) firstExpr);
                    Data f=new Data (p,resultExpr);
                 // Object[] subClassesArr=myEngine.onlySatisfiableClasses(myOntologyClass.getReasoner().getSubClasses(firstExpr, false)).toArray();
                   Object[] subClassesArr=myEngine.onlySatisfiableClasses(myOntologyClass.getReasoner().getSubClasses(resultExpr, false)).toArray();
                    ArrayList<OWLClass> a=new ArrayList<OWLClass>();
                    
                    for(int k=0;k<subClassesArr.length;k++)
                        a.add((OWLClass) subClassesArr[k]);
                    
                    f.setSubClasses(a);
                    f.setnumOfSubClasses(a.size());
                    facets.add(f);
                   
                }
          
            }
            
        }

    }

   public void addFactesCheckboxes() {

       facetsPanel.removeAll();
       facetsPanel.repaint();
       selectedClasses.removeAll(selectedClasses);
       buildList();
        
        int numOfFactes=facets.size();   
        int numOfAllRows=0;
      
        for(int i=0;i<facets.size();i++)
            numOfAllRows=numOfAllRows+facets.get(i).getNumOfSubClasses();
        
        
        numOfAllRows=numOfAllRows+numOfFactes; //num of subclasses + facets
        Component[] facetsList=new Component[numOfAllRows];
        
        JPanel p=new JPanel();
        JScrollPane scrollPane= new JScrollPane();
        scrollPane.removeAll();
        scrollPane.setPreferredSize(new Dimension(190,100));
        int count=0;

    
        for (int i = 0; i < numOfFactes; i++) {
          
           JLabel facetLabel=new JLabel(facets.get(i).getPropertyName());
           facetsList[count]=facetLabel;
       
           facetsPanel.add(facetLabel);
            count++;
          
            for(int k=0;k<facets.get(i).getsubClassesNames().size();k++)
            {
                if(count<numOfAllRows)
                {
                JCheckBox cb=new JCheckBox( facets.get(i).getsubClassesNames().get(k).toString());
               cb.setName(facets.get(i).getsubClasses().get(k).toString()+facets.get(i).getProperty());
               cb.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
              
               AbstractButton abstractButton = (AbstractButton) e.getSource();
              boolean selected = abstractButton.getModel().isSelected();
              
             String str1 = abstractButton.getName().substring(1, abstractButton.getName().indexOf(">"));
             String str2 = abstractButton.getName().substring(abstractButton.getName().indexOf(">")+2, abstractButton.getName().lastIndexOf(">"));
             IRI classIRI = IRI.create(str1);
              OWLClass c = myOntologyClass.getDf().getOWLClass(classIRI);
              
              IRI propertyIRI = IRI.create(str2);
              OWLObjectProperty p =myOntologyClass.getDf().getOWLObjectProperty(propertyIRI);
              
              if(selected)
              {
                  Set<OWLClassExpression> classes = new HashSet<OWLClassExpression>();
                  //Ingredients
                 // classes.add(myOntologyClass.getDf().getOWLClass(Global.myConfig.getIngredientClass()));
                  // Create the existential restrictions of things that we want to include.
                  classes.add(myOntologyClass.getDf().getOWLObjectSomeValuesFrom(p, c));
                  //intersection of all previous
                  OWLClassExpression resultExpr = myOntologyClass.getDf().getOWLObjectIntersectionOf(classes);
                  selectedClasses.add(resultExpr);

                   buildList();
                  
              }
              else
              {
                 Set<OWLClassExpression> classes = new HashSet<OWLClassExpression>();
                  // Create the existential restrictions of things that we want to include.
                  classes.add(myOntologyClass.getDf().getOWLObjectSomeValuesFrom(p, c));
                  //intersection of all previous
                  OWLClassExpression resultExpr = myOntologyClass.getDf().getOWLObjectIntersectionOf(classes);
                  selectedClasses.remove(resultExpr);

                   buildList();
              }

         
            }
         });

               facetsList[count]= cb;
               facetsPanel.add(cb);
               
            
                count++;
                
                }
                else
                {
                 
                JCheckBox cb=new JCheckBox( facets.get(i).getsubClassesNames().get(k).toString());
               
               facetsList[count]= cb;
             facetsPanel.add(cb);
             
                }
                    
            }
           
        }
      
        
       
       /* for(int i=0;i<facetsList.length;i++)
        {
            characteristicsPanel.add(facetsList[i]);
        }*/
        
        facetsPanel.validate();
   
    }

    private void buildList() {
      
        DefaultListModel ingredientsListModel = new DefaultListModel();
        QueryTemplateEngine myEngine = new QueryTemplateEngine(myOntologyClass.getDf(), myOntologyClass.getOntologyIRI(), myOntologyClass.getReasoner());
        Collection resultCollection = myEngine.getIngredientsWithCharacteristics(selectedClasses);
        Object[] arr = resultCollection.toArray();

        for (int i = 0; i < arr.length; i++) {
            ingredientsListModel.addElement(arr[i]);
        }

        ListCellRenderer myRenderer = new OWLClassListCellRenderer(QueryInterface.selectedLanguage);
        NewFacetedSearchPanel.ingredientsList.setCellRenderer(myRenderer);
        NewFacetedSearchPanel.ingredientsList.setDragEnabled(true);
        NewFacetedSearchPanel.ingredientsList.setModel(ingredientsListModel);
        NewFacetedSearchPanel.ingredientsList.validate();
        NewFacetedSearchPanel.ingredientsList.repaint();
        ingredientsPanel.validate();

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
        facetsPanel = new javax.swing.JPanel();

        setLayout(new java.awt.BorderLayout());

        ingredientsPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Ingredients"));

        jScrollPane1.setViewportView(ingredientsList);

        javax.swing.GroupLayout ingredientsPanelLayout = new javax.swing.GroupLayout(ingredientsPanel);
        ingredientsPanel.setLayout(ingredientsPanelLayout);
        ingredientsPanelLayout.setHorizontalGroup(
            ingredientsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ingredientsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 214, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(32, Short.MAX_VALUE))
        );
        ingredientsPanelLayout.setVerticalGroup(
            ingredientsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ingredientsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 439, Short.MAX_VALUE)
                .addContainerGap())
        );

        add(ingredientsPanel, java.awt.BorderLayout.CENTER);

        facetsPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Characterstics:"));
        facetsPanel.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        facetsPanel.setLayout(new javax.swing.BoxLayout(facetsPanel, javax.swing.BoxLayout.Y_AXIS));
        add(facetsPanel, java.awt.BorderLayout.PAGE_START);
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    public static javax.swing.JPanel facetsPanel;
    public static javax.swing.JList ingredientsList;
    private javax.swing.JPanel ingredientsPanel;
    public javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables
}

 class Data{
    private OWLClassExpression rangeClass;
    private Integer numOfSubClasses;
    private ArrayList<OWLClass> subClasses;
    private OWLObjectProperty property;

   Data(OWLObjectProperty property, OWLClassExpression rangeClass)
    {
        this.rangeClass=rangeClass;
        this.property=property;
    }
    public void setProperty(OWLObjectProperty property) {
        this.property = property;
    }

    public OWLObjectProperty getProperty() {
        return property;
    }
    
    public OWLClassExpression getRangeClass() {
        return rangeClass;
    }
    
    public String getRangeClassName()
    {
       return Global.myOntology.getOWLClassAlternativeLanguage((OWLClass) rangeClass, QueryInterface.selectedLanguage); 
    }
    
        public String getPropertyName()
    {
       return Global.myOntology.getOWLObjectPropertyAlternativeLanguage(property, QueryInterface.selectedLanguage); 
    }
    
    public  ArrayList<OWLClass> getsubClasses()
    {
        return subClasses;
    }

    public  ArrayList<String> getsubClassesNames()
    {
         ArrayList<String> subClassesNames=new ArrayList<String>();
         
         for(int i=0;i<subClasses.size();i++)
             subClassesNames.add(Global.myOntology.getOWLClassAlternativeLanguage(subClasses.get(i), QueryInterface.selectedLanguage));
       
         return subClassesNames;
    }
    
    public void setSubClasses(ArrayList<OWLClass> subClasses)
    {
       this.subClasses=subClasses;
    }
    public void setOWLClass(OWLClass cls) {
        this.rangeClass = cls;
    }

    public Integer getNumOfSubClasses() {
        return numOfSubClasses;
    }

    public void setnumOfSubClasses(Integer numOfSubClasses) {
        this.numOfSubClasses = numOfSubClasses;
    }
}

