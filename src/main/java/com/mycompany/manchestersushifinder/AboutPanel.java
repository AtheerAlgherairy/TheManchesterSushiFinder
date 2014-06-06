/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.manchestersushifinder;

import java.awt.Dimension;
import java.net.URL;
import javax.swing.ImageIcon;

/**
 *
 * @author Atheer
 */
public class AboutPanel extends javax.swing.JPanel {

    private ImageIcon image;

    /**
     * Creates new form aPanel
     */
    public AboutPanel() {

        initComponents();
        
        //For the About image
        try {
            URL url = getClass().getResource("/AboutPic.png");
            image = new ImageIcon(url);
        } catch (Exception e) {
            e.printStackTrace();
        }

        ImageLabel.setPreferredSize(new Dimension(image.getIconWidth(), image.getIconHeight()));
        ImageIcon newIcon = new ImageIcon(image.getImage());
        ImageLabel.setIcon(newIcon);
        ontologyLocationLabel.setText("Ontology location: " + Global.myConfig.getOntologyLocation().toString());

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        authorLabel = new javax.swing.JLabel();
        supervisorLabel = new javax.swing.JLabel();
        copyrightLabel = new javax.swing.JLabel();
        ontologyLocationLabel = new javax.swing.JLabel();
        ImageLabel = new javax.swing.JLabel();

        authorLabel.setText("Author: Atheer Algherairy - Email: asalgerairy@ud.edu.sa");
        authorLabel.setToolTipText("");

        supervisorLabel.setText("Supervised by: Sean Bechhofer");

        copyrightLabel.setText("Copyright The University Of Manchester");

        ontologyLocationLabel.setText("Ontology location:");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(35, 35, 35)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(ImageLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(copyrightLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 479, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(supervisorLabel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(authorLabel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 471, Short.MAX_VALUE))
                    .addComponent(ontologyLocationLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 490, Short.MAX_VALUE))
                .addContainerGap(58, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(ImageLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 28, Short.MAX_VALUE)
                .addComponent(copyrightLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(authorLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(supervisorLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ontologyLocationLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(23, 23, 23))
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel ImageLabel;
    private javax.swing.JLabel authorLabel;
    private javax.swing.JLabel copyrightLabel;
    private javax.swing.JLabel ontologyLocationLabel;
    private javax.swing.JLabel supervisorLabel;
    // End of variables declaration//GEN-END:variables
}
