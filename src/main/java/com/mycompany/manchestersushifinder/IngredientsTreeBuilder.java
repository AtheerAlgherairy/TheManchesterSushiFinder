/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.manchestersushifinder;

import java.awt.Component;
import java.util.HashMap;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.MutableTreeNode;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.reasoner.NodeSet;

/**
 *
 * @author Atheer
 */
public class IngredientsTreeBuilder {

    OntologyClass myOntologyClass;
    private HashMap classNodeMap;
    String lang;

    public IngredientsTreeBuilder(String lang) {
        this.lang = lang;
        
        if (Global.myOntology == null) {
            myOntologyClass = new OntologyClass(Global.myConfig.getOntologyLocation());
        } else {
            myOntologyClass = Global.myOntology;
        }
        classNodeMap = new HashMap();

    }

    protected JTree buildTree() {
        IRI ingredientClassIRI = Global.myConfig.getIngredientClass();
        OWLClass ingreidentClass = myOntologyClass.getDf().getOWLClass(ingredientClassIRI);

        MutableTreeNode rootNode = new DefaultMutableTreeNode(ingreidentClass);
        for (OWLClassExpression owlClassExpression : myOntologyClass.getIngredients()) {
            OWLClass cls = (OWLClass) owlClassExpression;
            addClassToTree(cls, rootNode);
        }

        JTree t = new JTree(rootNode);
        t.setShowsRootHandles(true);
        
        //Setup the defaultTreeCellRenderer and its open and close icons :
        DefaultTreeCellRenderer renderer = new OWLClassTreeCellRenderer(lang);
        
        java.net.URL imgURL1 = IngredientsTreeBuilder.class.getResource("/icon1.png");
        java.net.URL imgURL2 = IngredientsTreeBuilder.class.getResource("/icon2.png");
        ImageIcon closedIcon = new ImageIcon(imgURL1);
        ImageIcon openIcon = new ImageIcon(imgURL2);
        renderer.setClosedIcon(closedIcon);
        renderer.setOpenIcon(openIcon);
        renderer.setLeafIcon(openIcon);


        t.setToolTipText("Drag and Drop");
        t.setCellRenderer(renderer);

        return t;
    }

    protected void addClassToTree(OWLClass cls, MutableTreeNode treeNode) {
        
        // Create a node for cls:
        MutableTreeNode node = new DefaultMutableTreeNode(cls);
        classNodeMap.put(cls, node);
        treeNode.insert(node, 0);
        
        //get the subclasses of "cls"
        NodeSet<OWLClass> allSubClasses = myOntologyClass.getReasoner().getSubClasses(cls, true); 
       
        //Avoid OWL:Nothing and add subclasses of cls
        if (!allSubClasses.containsEntity(myOntologyClass.getDf().getOWLNothing())) {
            for (OWLClass c : allSubClasses.getFlattened()) {
                addClassToTree(c, node);
            }
        }


    }

    public class OWLClassTreeCellRenderer extends DefaultTreeCellRenderer {

        String lang;

        public OWLClassTreeCellRenderer(String lang) {
            this.lang = lang;
        }

        @Override
        public Component getTreeCellRendererComponent(JTree tree,
                Object value,
                boolean sel,
                boolean expanded,
                boolean leaf,
                int row,
                boolean hasFocus) {
            JLabel label = (JLabel) super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
            Object obj = ((DefaultMutableTreeNode) value).getUserObject();
            if (obj instanceof OWLClass) {
                label.setText(myOntologyClass.getOWLClassAlternativeLanguage((OWLClass) obj, lang));
            } else {
                label.setText(value.toString());
            }
            return label;
        }
    }
}
