/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.manchestersushifinder;

import java.util.ArrayList;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLObjectProperty;

/**
 *
 * @author Atheer
 */
public class Sanctions {

    private OntologyClass myOntologyClass;
    private ArrayList<IRI[]> listOfSanctions = new ArrayList<IRI[]>();
    private ArrayList<OWLClassExpression> listOfExpressions = new ArrayList<OWLClassExpression>();
    private ArrayList<OWLClass> setOfR = new ArrayList<OWLClass>();
    private ArrayList<OWLObjectProperty> listOfProperties=new ArrayList<OWLObjectProperty>();
    
    public Sanctions(OntologyClass myOntologyClass, ArrayList<IRI[]> listOfSanctions) {
        this.myOntologyClass = myOntologyClass;
       
        this.listOfSanctions = listOfSanctions;
    }

    public ArrayList<OWLObjectProperty> getListOfProperties() {
        return listOfProperties;
    }

    
    //Check whether if complex expression class1 isSubClassOf complex expression class2 or not?
 public Boolean isSubClassOf(OWLClassExpression class1, OWLClassExpression class2) {
        return myOntologyClass.getReasoner().isEntailed(myOntologyClass.getDf().getOWLSubClassOfAxiom(class1, class2));
    }

 
   
    //check if A and B are reasonable
    private OWLClassExpression areReasonable(OWLClass A, OWLClass B) {
        OWLClassExpression resultExpr=null;

        if (!listOfSanctions.isEmpty()) {
            for (int i = 0; i < listOfSanctions.size(); i++) {
                OWLClass classC = myOntologyClass.getDf().getOWLClass(listOfSanctions.get(i)[0]);
                OWLObjectProperty propR = myOntologyClass.getDf().getOWLObjectProperty(listOfSanctions.get(i)[1]);
                OWLClass classD = myOntologyClass.getDf().getOWLClass(listOfSanctions.get(i)[2]);

                if(!listOfProperties.contains(propR))
                        listOfProperties.add(propR);
                //Exist R.D
               //OWLClassExpression expr = myOntologyClass.getDf().getOWLObjectSomeValuesFrom(propR, classD);
                
                    //if A isSubClassOf C and B isSubClassOf D
                if (isSubClassOf(A,classC) && isSubClassOf(B,classD)) {
                    resultExpr = myOntologyClass.getDf().getOWLObjectSomeValuesFrom(propR, B);
                    break;
                    
                }

            }
        }
        return resultExpr;

    }
    
    //Get the minimal non-redundant set of concepts that might reasonably be conjoined with Current class (ingredient)
    public void calculateSetOfReasonableClasses(OWLClass ingredientClass)
    {
 
        setOfR.removeAll(setOfR);
        listOfExpressions.removeAll(listOfExpressions);
        
        for(OWLClass c: myOntologyClass.getOntology().getClassesInSignature())
        {
          
            if(areReasonable(ingredientClass,c)!=null)
            {
               
               OWLClassExpression resultExpr = myOntologyClass.getDf().getOWLObjectIntersectionOf(ingredientClass,c);
                if(myOntologyClass.getReasoner().isSatisfiable(resultExpr))
                {
                    if(!isSubClassOf(ingredientClass,resultExpr))
                    {
                       
                            listOfExpressions.add(areReasonable(ingredientClass,c));
                            setOfR.add(c);
                       
                      
                    }
                }
            }
        }       
        fixTautological();    
    }

    public void fixTautological()
    {
          ArrayList<OWLClassExpression> newListOfExpressions = new ArrayList<OWLClassExpression>();
          ArrayList<OWLClass> newSetOfR = new ArrayList<OWLClass>();
    
        for(int i=0;i<setOfR.size();i++)
        {
            OWLClass c=setOfR.get(i);
            boolean flag=true;
            //----------check if D is not subsumed by some other D' in set R----
            for(int k=0;k<setOfR.size();k++)
            {
                     
                if(isSubClassOf(setOfR.get(k),c) && !c.equals(setOfR.get(k)))
                {
                 flag=false;
                   
                }
            }
            //-------------------------------------
            
            if(flag)
            {
               newListOfExpressions.add(listOfExpressions.get(i));
               newSetOfR.add(setOfR.get(i));
            }
        }
        
    
       listOfExpressions=newListOfExpressions;
       setOfR=newSetOfR;
    }
    
   public ArrayList<OWLClass> getSetOfR()
    {
        return setOfR;
    }
    public ArrayList<OWLClassExpression> getExpressions()
    {
        return listOfExpressions;
    }

}