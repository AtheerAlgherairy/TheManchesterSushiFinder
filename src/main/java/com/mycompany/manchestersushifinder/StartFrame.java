/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.manchestersushifinder;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 *
 * @author Atheer
 */
public class StartFrame extends javax.swing.JFrame {

    public static IngredientsTreeBuilder ingTreeBuilder;
    public static JTree myTree;
    public static Global global;
    public static QueryInterface qframe;
    public static FacetedSearchPanel secondView;
    public static DirectSearchPanel thirdView;
    public static NewFacetedSearchPanel fourthView;
    
    Timer timer = new Timer(600, new ActionListener() {
        private String text = "Please wait for loading ...";
        private boolean flag = true;

        public void actionPerformed(ActionEvent e) {
            if (flag) {
                jLabel2.setText(text);
                flag = false;
            } else {
                jLabel2.setText("");
                flag = true;
            }
        }
    });

    /**
     * Creates new form StartFrame
     */
    public StartFrame() {
        initComponents();

        timer.start();
        Runnable r = new Runnable() {
            public void run() {
                global = new Global(); //loading the configuration file & ontology

                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        setupQueryInterface();
                        timer.stop();
                        jLabel2.setText("");
                        dispose();
                    }
                });
            }
        };
        Thread t = new Thread(r);
        t.start();

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setAlwaysOnTop(true);

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel1.setText("The Manchester Sushi Finder");

        jMenu1.setText("Sushi Finder");

        jMenuItem1.setText("About...");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem1);

        jMenuBar1.add(jMenu1);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(102, 102, 102)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 357, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(113, 113, 113)
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 335, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(110, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 79, Short.MAX_VALUE)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(51, 51, 51))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    public static void setupQueryInterface() {
        qframe = new QueryInterface();

        // Creating firstView panel 
        JPanel firstView=new JPanel();
        //Build the tree.
        ingTreeBuilder = new IngredientsTreeBuilder("default");
        myTree = ingTreeBuilder.buildTree();
        myTree.setDragEnabled(true);
        myTree.setToolTipText("Drag and Drop..");
        JScrollPane jscrollpane1 = new JScrollPane(myTree);
        jscrollpane1.validate();
        //Add the tree to the firstView panel
        firstView.add(jscrollpane1);

        //qframe.TreePanel.add(jscrollpane1);



        //----------Creating Second View: FacetedSearchPanel---
        OntologyClass myOntologyClass;
        if (Global.myOntology == null) {
            myOntologyClass = new OntologyClass(Global.myConfig.getOntologyLocation());
        } else {
            myOntologyClass = Global.myOntology;
        }
       
          secondView = new FacetedSearchPanel("default", myOntologyClass, Global.myConfig.getIngredientsClassifications());
            
        
            fourthView = new NewFacetedSearchPanel("default", myOntologyClass, Global.myConfig.getIngredientsFactes());
        //------------Creating Third View: DirectSearchPanel--------------
            thirdView=new DirectSearchPanel(myOntologyClass);
            
        
        //Creating Tabs: 1-firstView (tree) 2-secondView (faceted)
        JTabbedPane tabbedPane = new JTabbedPane();     
        tabbedPane.addTab("Tree",jscrollpane1);
       // tabbedPane.setMnemonicAt(0, KeyEvent.VK_1);
        
         if(!Global.myConfig.getIngredientsClassifications().isEmpty())
         {
        tabbedPane.addTab("Classes",secondView);
       // tabbedPane.setMnemonicAt(1, KeyEvent.VK_2);
         }
         
        tabbedPane.addTab("Search",thirdView);
      //  tabbedPane.setMnemonicAt(2, KeyEvent.VK_3);
        
         if(!Global.myConfig.getIngredientsFactes().isEmpty())
         {
        tabbedPane.addTab("Facets",fourthView);
       // tabbedPane.setMnemonicAt(3, KeyEvent.VK_4);
         }
        qframe.BrowsingPanel.add(tabbedPane);
        qframe.BrowsingPanel.validate();



        try {

            //2- form configurations, set the labels and logo.
            qframe.ExcludedPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder(Global.myConfig.getExcludedLabel()),
                    BorderFactory.createEmptyBorder(7, 7, 7, 7)));
            qframe.IncludedPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder(Global.myConfig.getIncludedLabel()),
                    BorderFactory.createEmptyBorder(7, 7, 7, 7)));

            qframe.TitleLabel.setText(Global.myConfig.getTitleLabel());
            BufferedImage icon = Global.myConfig.getLogoImage();
            Image img = icon;
            BufferedImage bi = new BufferedImage(qframe.ImageLabel.getWidth(), qframe.ImageLabel.getHeight(), BufferedImage.TYPE_INT_ARGB);
            Graphics g = bi.createGraphics();
            g.drawImage(img, qframe.ImageLabel.getX(), qframe.ImageLabel.getY(), qframe.ImageLabel.getWidth(), qframe.ImageLabel.getHeight(), null);
            ImageIcon newIcon = new ImageIcon(bi);
            qframe.ImageLabel.setIcon(newIcon);
            // ------------------------------


        } catch (IOException ex) {
            Logger.getLogger(QueryInterface.class.getName()).log(Level.SEVERE, null, ex);
        }
        qframe.jSplitPane1.setDividerLocation(250);
        qframe.setResizable(false);

        // timer.stop();

        qframe.setVisible(true);
        //this.setVisible(false);
    }

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed

        AboutDialog dialog = new AboutDialog(this);
        dialog.validate();
        dialog.setVisible(true);

    }//GEN-LAST:event_jMenuItem1ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {

        // Set system look and feel
        try {
            // Set cross-platform L&F (called "Metal")
            UIManager.setLookAndFeel(
                    UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(StartFrame.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            Logger.getLogger(StartFrame.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(StartFrame.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedLookAndFeelException ex) {
            Logger.getLogger(StartFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        /* Create and display the form */

        StartFrame start = new StartFrame();
        start.setVisible(true);

    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    // End of variables declaration//GEN-END:variables
}

class loadThread extends Thread {

    JLabel label;

    loadThread(JLabel label) {
        this.label = label;
    }

    public void run() {
        for (int i = 0; i < 10; i++) {
            label.setText("Loading" + i);
        }
    }
}