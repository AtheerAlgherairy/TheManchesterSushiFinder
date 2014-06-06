/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.manchestersushifinder;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Frame;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import java.awt.event.ActionEvent;

/**
 *
 * @author Atheer
 */

public class AboutDialog extends JDialog {

	public AboutDialog(Frame frame) {
		super(frame, " About", true);
		createUI();
	}

	protected void createUI() {
		AboutPanel myAboutPanel = new  AboutPanel(); 
		getContentPane().add(myAboutPanel);          
		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
                
		JButton OKbutton = new JButton(new AbstractAction("OK") {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		buttonPanel.add(OKbutton);
		buttonPanel.setBorder(BorderFactory.createEmptyBorder(7, 7, 7, 12));
		getContentPane().add(buttonPanel, BorderLayout.SOUTH);
		this.pack();
                int screenWidth=(int) getToolkit().getScreenSize().getWidth();
                int screenHeight=(int) getToolkit().getScreenSize().getHeight();
                
                //set the location of the dialog to be nearly middle the screen
		this.setLocation(screenWidth / 2 - 200, screenHeight / 2-200 );
		this.setResizable(false);
	}
}

