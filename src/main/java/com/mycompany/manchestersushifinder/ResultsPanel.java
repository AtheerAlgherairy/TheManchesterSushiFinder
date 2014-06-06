package com.mycompany.manchestersushifinder;

import org.semanticweb.owlapi.model.OWLClass;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;


public class ResultsPanel extends JPanel {

    private QueryTemplateEngine myEngine;
    private ArrayList<OWLClass> ResultsCharacteristics;
    private Box box;
    QueryInterface queryFrame;

    public ResultsPanel(QueryTemplateEngine myEngine, ArrayList<OWLClass> ResultsCharacteristics, QueryInterface queryFrame) {

        this.myEngine = myEngine; 
        this.ResultsCharacteristics = ResultsCharacteristics;
        this.queryFrame=queryFrame;

        createUI();
    }

    private void createUI() {
        setLayout(new BorderLayout(7, 7));
        
        
        //Creating Back button and its action.
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        
        Action backAction = new AbstractAction("Back") {
            public void actionPerformed(ActionEvent e) {
               
             queryFrame.showChooserPanel();
                 
            }
        };
        JButton backButton=new JButton(backAction);
       backButton.setMaximumSize(new Dimension(67,23));
       backButton.setMinimumSize(new Dimension(67,23));
        backButton.setPreferredSize(new Dimension(67,23));
        backButton.repaint();
        buttonPanel.add(backButton);
        add(buttonPanel, BorderLayout.SOUTH);
        box = new Box(BoxLayout.Y_AXIS);
        box.setBackground(Color.WHITE);
        add(new JScrollPane(box));
        
    }

    
 

    //Create a (BasePanel) for each OWL Class result.
    private BasePanel[] createBasePanels(Collection<OWLClass> results) throws MalformedURLException {
        BasePanel[] panels = new BasePanel[results.size()];
        int counter = 0;
        for (Iterator iterator = results.iterator(); iterator.hasNext(); counter++) {
            OWLClass cls = (OWLClass) iterator.next();
            String baseName = Global.myOntology.getOWLClassAlternativeLanguage(cls, QueryInterface.selectedLanguage);
            String description = Global.myOntology.getResultAnnotation(cls, Global.myConfig.getResultsAnnotaionProperty(), QueryInterface.selectedLanguage);


            //-------------- get all  Results icoms for this cls----------------------
            java.util.List<URL> icons = new ArrayList<URL>();
            for (int i = 0; i < ResultsCharacteristics.size(); i++) {

                String className = ResultsCharacteristics.get(i).getIRI().toString();
                OWLClass resultClass = ResultsCharacteristics.get(i);
              
                //Check if the result class is subclass of this Characteristics class?
                if (myEngine.isSubClassOf(resultClass, cls)) {
                    icons.add(Global.myConfig.getResultCharacteristicIcon(className));
                }
            }
            //---------------------------------------------------
            panels[counter] = new BasePanel(baseName, description,icons);
        }
        return panels;
    }

    //Set the created (BasePanels) in a box in the ResultsPanel.
       public void setBasePanels(Collection<OWLClass> results) throws MalformedURLException {
        
        box.removeAll();
        BasePanel[] panels=createBasePanels(results);  
        for (int i = 0; i < panels.length; i++) {
            box.add(panels[i]);
            box.add(Box.createVerticalStrut(3));
        }
       revalidate();
       box.revalidate();
   
    }
}
