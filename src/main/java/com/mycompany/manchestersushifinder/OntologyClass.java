/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.manchestersushifinder;

import org.semanticweb.owlapi.util.BidirectionalShortFormProvider;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.Collection;
import javax.swing.JOptionPane;
import org.semanticweb.HermiT.Reasoner;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;
import org.semanticweb.owlapi.util.ShortFormProvider;
import org.semanticweb.owlapi.util.SimpleShortFormProvider;

/**
 *
 * @author Atheer
 */
public class OntologyClass {

    private OWLReasoner reasoner;
    private OWLOntology ontology;
    private OWLDataFactory dataFactory;
    public static ShortFormProvider shortFormProvider;
    private OWLOntologyManager manager;
    private IRI ontologyIRI;
    private String ontologyLocation;
     private BidirectionalShortFormProvider bidiShortFormProvider;



    
    //This constructer to be used from NewConfigurationFileDialog
    public OntologyClass(String ontologyLocation, boolean temp)
    {
        this.ontologyLocation=ontologyLocation;
        loadMyOntology();
        setupReasoner();   
        shortFormProvider = new SimpleShortFormProvider();
        
    }
   
    public OntologyClass(String ontologyLocation) {
        if (Global.useDefault)
        {
            this.ontologyLocation=ontologyLocation;
             InputStream is = getClass().getClassLoader().getResourceAsStream(ontologyLocation);
            loadMyOntology(is);
        setupReasoner();   
        shortFormProvider = new SimpleShortFormProvider();
        }
        else {
        this.ontologyLocation=ontologyLocation;
        loadMyOntology();
        setupReasoner();   
        shortFormProvider = new SimpleShortFormProvider();
        }
      

    }
    
   
       
    public OWLDataFactory getDf() {
        return dataFactory;
    }

    public IRI getOntologyIRI() {
        return ontologyIRI;
    }
    
     public OWLOntology getOntology() {
        return ontology;
    }
    
   public OWLReasoner getReasoner() {
        return reasoner;
    }


   //Load the ontology and setup the manager and data factory
   private void loadMyOntology() {
        try {
              
            URL url=new URL(ontologyLocation);
            File myFile = new File(url.getFile());
            manager = OWLManager.createOWLOntologyManager();
            dataFactory = manager.getOWLDataFactory();
            ontology = manager.loadOntologyFromOntologyDocument(myFile);
            ontologyIRI = ontology.getOntologyID().getOntologyIRI();
            

            
        } catch (final Throwable e) {
            JOptionPane.showMessageDialog(null, "Sorry, could not load the ontology." + "[" + e.getMessage() + "]", "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
    }

    private void loadMyOntology(InputStream is) {
        try {
              
            //URL url=new URL(ontologyLocation);
            //File myFile = new File(url.getFile());
            manager = OWLManager.createOWLOntologyManager();
            dataFactory = manager.getOWLDataFactory();
            ontology = manager.loadOntologyFromOntologyDocument(is);
            ontologyIRI = ontology.getOntologyID().getOntologyIRI();
            

            
        } catch (final Throwable e) {
            JOptionPane.showMessageDialog(null, "Sorry, could not load the ontology." + "[" + e.getMessage() + "]", "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
    }
   
    //Set up Hermit reasoner:
    private void setupReasoner() {
        
        try {

            OWLReasonerFactory factory = new Reasoner.ReasonerFactory();
            reasoner = factory.createReasoner(ontology);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error in the reasoner..\n"
                    + "[" + e.getMessage() + "]", "Reasoner Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public String getOWLObjectPropertyAlternativeLanguage(OWLObjectProperty property, String selectedLang)
    {
        Boolean flag = false;
        OWLLiteral val = null;
        String renderedName=null;
        String langName=null;
        
        //If there is a label with the selected language.. return it
        
        for (OWLAnnotation ann : property.getAnnotations(ontology, dataFactory.getRDFSLabel())) {
            val = (OWLLiteral) ann.getValue();
            if (val.hasLang(selectedLang)) {
                 flag = true;
                 langName=val.getLiteral();
                 break;  
            }
         
        }
        
        if(langName!=null)
        {
            flag = false;
            return langName;
        }
        
      
        //Prepare the short form provider and no camel case:
        String shortForm=shortFormProvider.getShortForm(property); 
        String name= stringWithNoCamelCase(shortForm); 
            
        // if there is no label with the selected language.. Based on the information in the config.xml
        if (!flag) 
        {
             //Check if the priority is to use *label* with no language       
           if (Global.myConfig.getClassRenderingUse().equalsIgnoreCase("label"))
           {
               renderedName=null;
               OWLLiteral value = null; 
               
               
                for (OWLAnnotation ann : property.getAnnotations(ontology, dataFactory.getRDFSLabel()))
                {             
                             value = (OWLLiteral) ann.getValue();
                             if(value.getLang().isEmpty())
                             {
                             renderedName=value.getLiteral();
                             break; 
                             }
                }
               
              if( renderedName != null)
                  return renderedName;    
           }
        
           //Or if the priority is to use IRI and short form provider.  
           if( Global.myConfig.getClassRenderingUse().equalsIgnoreCase("IRI"))
           {
            return name;
           }
 
        } 
        
        //At the end if there is no labels at all, the IRI and short form provider will be used
           return name;
    }
    
    public String getOWLClassAlternativeLanguage(OWLClass cl, String selectedLang) {
        Boolean flag = false;
        OWLLiteral val = null;
        String renderedName=null;
        String langName=null;
        
        //If there is a label with the selected language.. return it
        
        for (OWLAnnotation ann : cl.getAnnotations(ontology, dataFactory.getRDFSLabel())) {
            val = (OWLLiteral) ann.getValue();
            if (val.hasLang(selectedLang)) {
                 flag = true;
                 langName=val.getLiteral();
                 break;  
            }
         
        }
        
        if(langName!=null)
        {
            flag = false;
            return langName;
        }
        
      
        //Prepare the short form provider and no camel case:
        String shortForm=shortFormProvider.getShortForm(cl); 
        String name= stringWithNoCamelCase(shortForm); 
  
            
        // if there is no label with the selected language.. Based on the information in the config.xml
        if (!flag) 
        {
             //Check if the priority is to use *label* with no language       
           if (Global.myConfig.getClassRenderingUse().equalsIgnoreCase("label"))
           {
               renderedName=null;
               OWLLiteral value = null; 
               
               
                for (OWLAnnotation ann : cl.getAnnotations(ontology, dataFactory.getRDFSLabel()))
                {             
                             value = (OWLLiteral) ann.getValue();
                             if(value.getLang().isEmpty())
                             {
                             renderedName=value.getLiteral();
                             break; 
                             }
                }
               
              if( renderedName != null)
                  return renderedName;    
           }
        
           //Or if the priority is to use IRI and short form provider.  
           if( Global.myConfig.getClassRenderingUse().equalsIgnoreCase("IRI"))
           {
            return name;
           }
 
        } 
        
        //At the end if there is no labels at all, the IRI and short form provider will be used
           return name;
        
        
    }

   public String stringWithNoCamelCase(String shortForm) {
       
        StringBuilder myStringBuilder = new StringBuilder();
        char letter = 0;
        //for each character in the short form string
        for (int i = 0; i < shortForm.length(); i++) {   
            //if the character is an upper case and not the first letter in 
            //the string, add a space then the character.
            if (letter != 0 && Character.isUpperCase(shortForm.charAt(i)) && letter!=' ') {
                myStringBuilder.append(" ");
            }
            myStringBuilder.append(shortForm.charAt(i));
            letter = shortForm.charAt(i);
        }
        return myStringBuilder.toString();
    }

   public Collection<OWLClass> getIngredients() {
        IRI ingredientClassIRI = null;
        OWLClass ingreidentClass = null;
        try {

            Configurations config = new Configurations();
            ingredientClassIRI = config.getIngredientClass();
            ingreidentClass = dataFactory.getOWLClass(ingredientClassIRI);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Cannot find ingredient class..\n"
                    + "[" + e.getMessage() + "]", "Ingredient Class Error", JOptionPane.ERROR_MESSAGE);
        }
        return reasoner.getSubClasses(ingreidentClass, true).getFlattened();
    }
        
    public String getResultAnnotation(OWLClass cls, IRI annoPropertyIRI, String selectedLang) {

        OWLLiteral val = null;
        Boolean thereIsLang = false;
        String description1 = "";
        String description2 = "";
      
        for (OWLAnnotation ann : cls.getAnnotations(ontology, dataFactory.getOWLAnnotationProperty(annoPropertyIRI))) {
            val = (OWLLiteral) ann.getValue();
            if (val.hasLang(selectedLang)) {
                thereIsLang = true;
                //because we may have number of a number of the same annotation property 
                description1 = description1.concat(val.getLiteral());
            }
        }

        if (!thereIsLang) {
            for (OWLAnnotation ann : cls.getAnnotations(ontology, dataFactory.getOWLAnnotationProperty(annoPropertyIRI))) {
                val = (OWLLiteral) ann.getValue();
                if (!val.hasLang()) {
                    description2 = description2.concat(val.getLiteral());
                }
            }
        }

        if (thereIsLang) {
            return description1;
        } else if(QueryInterface.selectedLanguage.equalsIgnoreCase("default")){
            return description2;
        } else
        {
            return null;
        }

    }

     
     
}
