/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.manchestersushifinder;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.reasoner.NodeSet;

/**
 *
 * @author Atheer
 */
public class Sanctions {

    private OntologyClass myOntologyClass;
    private ArrayList<IRI[]> listOfSanctions = new ArrayList<IRI[]>();
    private ArrayList<OWLClassExpression> listOfExpressions = new ArrayList<OWLClassExpression>();
     
    public Sanctions(OntologyClass myOntologyClass, ArrayList<IRI[]> listOfSanctions) {
        this.myOntologyClass = myOntologyClass;
       
        this.listOfSanctions = listOfSanctions;
    }

    
    //Check whether if class2 isSubClassOf class1 or not?
 public Boolean isSubClassOf(OWLClassExpression class1, OWLClass class2) {
        Boolean result = false;
        for (OWLClass c : onlySatisfiableClasses(myOntologyClass.getReasoner().getSubClasses(class1, false))) {
            if (class2.equals(c)) {
                result = true;
                break;
            }
        }
        return result;
    }

    //check if A and B are reasonable
    private Boolean areReasonable(OWLClass A, OWLClass B) {
        Boolean reasonable = false;

        if (!listOfSanctions.isEmpty()) {
            for (int i = 0; i < listOfSanctions.size(); i++) {
                OWLClass classC = myOntologyClass.getDf().getOWLClass(listOfSanctions.get(i)[0]);
                OWLObjectProperty propR = myOntologyClass.getDf().getOWLObjectProperty(listOfSanctions.get(i)[1]);
                OWLClass classD = myOntologyClass.getDf().getOWLClass(listOfSanctions.get(i)[2]);

                //Exist R.B
                OWLClassExpression resultExpr = myOntologyClass.getDf().getOWLObjectSomeValuesFrom(propR, B);

                    //if A isSubClassOf C and B isSubClassOf D
                if (isSubClassOf(classC,A) && isSubClassOf(classD,B)) {
                    listOfExpressions.add(resultExpr);
                    reasonable = true;
                    break;
                }

            }
        }
        return reasonable;

    }
    
    //Get the minimal non-redundant set of concepts that might reasonably be conjoined with Current class (ingredient)
    public ArrayList<OWLClass> getSetOfReasonableClasses(OWLClass ingredientClass)
    {
        //Set<OWLClass> cls=new HashSet<OWLClass>();
        
        ArrayList<OWLClass> cls=new ArrayList<OWLClass>();
        
        for(OWLClass c: myOntologyClass.getOntology().getClassesInSignature())
        {
          
            if(areReasonable(ingredientClass,c))
            {
                OWLClassExpression resultExpr = myOntologyClass.getDf().getOWLObjectIntersectionOf(ingredientClass,c);
                if(myOntologyClass.getReasoner().isSatisfiable(resultExpr))
                {
                    if(!isSubClassOf(resultExpr,ingredientClass))
                    {
                        cls.add(c);
                    }
                }
            }
        }
        return cls;
    }

    public ArrayList<OWLClassExpression> getExpressions()
    {
        return listOfExpressions;
    }
    private Set<OWLClass> onlySatisfiableClasses(NodeSet<OWLClass> set) {
        Set<OWLClass> result = new TreeSet<OWLClass>();
        for (OWLClass cls : set.getFlattened()) {
            if (myOntologyClass.getReasoner().isSatisfiable(cls)) {
                result.add(cls);
            }
        }
        return result;
    }
}