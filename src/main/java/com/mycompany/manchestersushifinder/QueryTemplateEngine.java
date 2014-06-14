/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.manchestersushifinder;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;
import javax.swing.DefaultListModel;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLObjectComplementOf;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.reasoner.NodeSet;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author Atheer
 */
public class QueryTemplateEngine {

    DefaultListModel IncludedList;
    DefaultListModel ExcludedList; 
    Element tempElement;
    OWLDataFactory df;
    IRI ontologyIRI;
    Boolean satisfiable;
    OWLReasoner reasoner;

    public QueryTemplateEngine(DefaultListModel IncludedList, DefaultListModel ExcludedList, Element tempElement, OWLDataFactory df, IRI ontologyIRI,OWLReasoner reasoner) {
        this.IncludedList = IncludedList;
        this.ExcludedList = ExcludedList;
        this.tempElement = tempElement;
        this.df = df;
        this.ontologyIRI = ontologyIRI;
     
        this.reasoner=reasoner;
    }
    
   public QueryTemplateEngine(OWLDataFactory df, IRI ontologyIRI,OWLReasoner reasoner) {
        this.df = df;
        this.ontologyIRI = ontologyIRI;
        this.reasoner=reasoner;
    }

      public Boolean getSatisfiable() {
        return satisfiable;
    }

    public Collection getQueryResults() {
          Set<OWLClass> includedThings = new TreeSet<OWLClass>();
          Set<OWLClass> excludedThings = new TreeSet<OWLClass>();
          
        //fill the sets from lists
        fillSets(includedThings,IncludedList);
        fillSets(excludedThings,ExcludedList);
        
        OWLClassExpression expr = null;
        
        try{
        if (checkTemplate(tempElement)) {
            expr = getResultedClassExprForSimpleTemplate(tempElement, includedThings, excludedThings, df, ontologyIRI);
        } //6- check if the query type is complex one (base,property,refernce to other template)
        //call (getResultedClassExprForComplexTemplate) function to construct the class expression
        else {
            expr = getResultedClassExprForComplexTemplate(tempElement, includedThings, excludedThings, df, ontologyIRI);

        }
        }catch(Exception e)
        {
            JOptionPane pane = new JOptionPane("The tool is not configured properly..\nQuery templates are not found\n"
                 , JOptionPane.ERROR_MESSAGE);
             JDialog dialog = pane.createDialog("Query Error");
                dialog.setAlwaysOnTop(true);
            dialog.setVisible(true);

        }
       
        //----------Check satisfiablity--------
         if(reasoner.isSatisfiable(expr))
            satisfiable=true;
        else
            satisfiable=false;
         //-----------------------------------
   
          return onlySatisfiableClasses(reasoner.getSubClasses(expr, false));
  
 
       
    }
    
    public Boolean isSubClassOf(OWLClassExpression class1, OWLClass class2) {
        Boolean result = false;
        for (OWLClass c : onlySatisfiableClasses(reasoner.getSubClasses(class1, false))) {
            if (class2.equals(c)) {
                result = true;
                break;
            }
        }
        return result;
    }
    
    private Set<OWLClass> onlySatisfiableClasses(NodeSet<OWLClass> set) {
        Set<OWLClass> result = new TreeSet<OWLClass>();
        for (OWLClass cls : set.getFlattened()) {
            if (reasoner.isSatisfiable(cls)) {
                result.add(cls);
            }
        }
        return result;
    }
    

       
    private void fillSets(Set<OWLClass> setOfClasses,DefaultListModel list )
    {
            
        for(int i=0;i<list.size();i++)
        {
            String str=list.get(i).toString().substring(1, list.get(i).toString().length()-1); 
            IRI classIRI= IRI.create(str);
            OWLClass cls = Global.myOntology.getDf().getOWLClass(classIRI);
            setOfClasses.add(cls);
                
        }
    }

    //This function returns the element with attName=attValue
    //To be used in returning query template based on its ID
    public static Element getNodeWithAttribute(Node root, String attrName, String attrValue) {
        NodeList nl = root.getChildNodes();
        for (int i = 0; i < nl.getLength(); i++) {
            Node n = nl.item(i);
            if (n instanceof Element) {
                Element el = (Element) n;
                if (el.getAttribute(attrName).equals(attrValue)) {
                    return el;
                } else {
                    el = getNodeWithAttribute(n, attrName, attrValue); //search recursively
                    if (el != null) {
                        return el;
                    }
                }
            }
        }
        return null;
    }

//This fuction takes an element which represents a query template and
//This fuction returns TRUE if it is simple template i.e. contains (base, property and component) elements
    public static Boolean checkTemplate(Element e) {
        Boolean baseFlag = false;
        Boolean propertyFlag = false;
        Boolean componentFlag = false;

        NodeList nl = e.getChildNodes();
        for (int i = 0; i < nl.getLength(); i++) {
            Node n = nl.item(i);
            if (n instanceof Element) {
                Element el = (Element) n;
                if (el.getTagName().equals("BaseClass")) {
                    baseFlag = true;
                }
                if (el.getTagName().equals("Property")) {
                    propertyFlag = true;
                }
                if (el.getTagName().equals("ComponentClass")) {
                    componentFlag = true;
                }
            }
        }//end for
        return (baseFlag && propertyFlag && componentFlag);
    }

//This function for the simple type of query template.
//it takes element that represent the query template and set of included things and excluded things
//The output is the class expression
    public static OWLClassExpression getResultedClassExprForSimpleTemplate(Element e, Set<OWLClass> includedThings, Set<OWLClass> excludedThings, OWLDataFactory df, IRI ontologyIRI) {
        Element baseElem = (Element) e.getElementsByTagName("BaseClass").item(0);
        Element propertyElem = (Element) e.getElementsByTagName("Property").item(0);

        OWLClass baseClass = df.getOWLClass(IRI.create( baseElem.getAttribute("name")));
        OWLObjectProperty objProp = df.getOWLObjectProperty(IRI.create(propertyElem.getAttribute("name")));

        // Create a hash set to stor the baseClass and (existential restrictions)of our description
        Set<OWLClassExpression> classes = new HashSet<OWLClassExpression>();
        // Everthing must be a BaseClass so add baseClass
        classes.add(baseClass);
        // Create the existential restrictions of things that we want to include.
        for (OWLClass inThing : includedThings) {
            classes.add(df.getOWLObjectSomeValuesFrom(objProp, inThing));
        }

        // Create the negative of existential restrictions of things that we want to exclude
        for (OWLClass exThing : excludedThings) {
            OWLClassExpression existentialRestriction = df.getOWLObjectSomeValuesFrom(objProp, exThing);
            OWLObjectComplementOf negative = df.getOWLObjectComplementOf(existentialRestriction);
            classes.add(negative);
        }

        //intersection of all previous
        OWLClassExpression resultExpr = df.getOWLObjectIntersectionOf(classes);
        return resultExpr;
    }
    
    //For Faceted Search (view)
    public Collection getIngredientsWithCharacteristics(Set<OWLClass> classes)
    {
            classes.add(df.getOWLClass(Global.myConfig.getIngredientClass()));
            OWLClassExpression resultExpr = df.getOWLObjectIntersectionOf(classes);
            
            return onlySatisfiableClasses(reasoner.getSubClasses(resultExpr, false));
    }
    //For Direct Search view
    public Collection getAllIngredients()
    {
          
            OWLClassExpression resultExpr = df.getOWLClass(Global.myConfig.getIngredientClass());
            return onlySatisfiableClasses(reasoner.getSubClasses(resultExpr, false));
    }
    
    //-------------------------------------------------
    

    public static OWLClassExpression getResultedClassExprForComplexTemplate(Element e, Set<OWLClass> includedThings, Set<OWLClass> excludedThings, OWLDataFactory df, IRI ontologyIRI) {
        Element baseElem = (Element) e.getElementsByTagName("BaseClass").item(0);
        Element propertyElem = (Element) e.getElementsByTagName("Property").item(0);

        OWLClass baseClass = df.getOWLClass(IRI.create(baseElem.getAttribute("name")));
        OWLObjectProperty objProp = df.getOWLObjectProperty(IRI.create(propertyElem.getAttribute("name")));

        // Create a hash set to stor the baseClass and (existential restrictions)of our description
        Set<OWLClassExpression> classes = new HashSet<OWLClassExpression>();
        // Everthing must be a BaseClass so add baseClass
        classes.add(baseClass);
        // Create the existential restrictions of things that we want to include.
        for (OWLClass inThing : includedThings) {
            classes.add(df.getOWLObjectSomeValuesFrom(objProp, getInclusionCriteria(e, inThing, df, ontologyIRI)));
        }

        // Create the negative of existential restrictions of things that we want to exclude
        for (OWLClass exThing : excludedThings) {
            OWLClassExpression existentialRestriction = df.getOWLObjectSomeValuesFrom(objProp, getInclusionCriteria(e, exThing, df, ontologyIRI));
            OWLObjectComplementOf negative = df.getOWLObjectComplementOf(existentialRestriction);
            classes.add(negative);
        }

        //intersection of all previous
        OWLClassExpression resultExpr = df.getOWLObjectIntersectionOf(classes);
        return resultExpr;
    }

    public static OWLClassExpression getInclusionCriteria(Element e, OWLClass Thing, OWLDataFactory df, IRI ontologyIRI) {

        //get the query template where ref=ID
        Element tempRefElem = (Element) e.getElementsByTagName("Template").item(0);
        String refID = tempRefElem.getAttribute("ref");
        Element tempElement = getNodeWithAttribute(e.getOwnerDocument().getDocumentElement(), "Id", refID);
        if (checkTemplate(tempElement)) //if simple template stop recursion and return expression 
        {

            Element baseElem = (Element) tempElement.getElementsByTagName("BaseClass").item(0);
            Element propertyElem = (Element) tempElement.getElementsByTagName("Property").item(0);

            OWLClass baseClass = df.getOWLClass(IRI.create(baseElem.getAttribute("name")));
            OWLObjectProperty objProp = df.getOWLObjectProperty(IRI.create(propertyElem.getAttribute("name")));

            Set<OWLClassExpression> classes = new HashSet<OWLClassExpression>();
            OWLClassExpression cls = df.getOWLObjectSomeValuesFrom(objProp, Thing);
            classes.add(cls);
            classes.add(baseClass);

            return df.getOWLObjectIntersectionOf(classes);

        } else {
            Element baseElem = (Element) tempElement.getElementsByTagName("BaseClass").item(0);
            Element propertyElem = (Element) tempElement.getElementsByTagName("Property").item(0);

            OWLClass baseClass = df.getOWLClass(IRI.create(baseElem.getAttribute("name")));
            OWLObjectProperty objProp = df.getOWLObjectProperty(IRI.create(propertyElem.getAttribute("name")));

            Set<OWLClassExpression> classes = new HashSet<OWLClassExpression>();
            //HERE: RECURSION!!
            OWLClassExpression cls = df.getOWLObjectSomeValuesFrom(objProp, getInclusionCriteria(tempElement, Thing, df, ontologyIRI));
            classes.add(cls);
            classes.add(baseClass);

            return df.getOWLObjectIntersectionOf(classes);

        }



    }

    
}
