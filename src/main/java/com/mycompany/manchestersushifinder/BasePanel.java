package com.mycompany.manchestersushifinder;

import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class BasePanel extends JPanel {

    //Each BasePanel contians the base name, description and list of characteristics' icons.
    private String baseDescription;
    private String baseName;
    private List<URL> icons = null;
    //to manage the dimension of the panel. 
    private Dimension prefferedSize;
    
    //specify fonts for base name and description:
    static final Font BASE_NAME_FONT = new Font("SansSerif", Font.BOLD, 16);
    static final Font DESCRIPTION_FONT = new Font("SansSerif", Font.PLAIN, 14);


    public BasePanel(String baseName, String baseDescription, List<URL> icons) {
        this.baseName = baseName;
        this.baseDescription = baseDescription;
        this.icons = new ArrayList<URL>();
        this.icons = icons;
        prefferedSize = new Dimension(600, 150);
    }

    public Dimension getPreferredSize() {
        return prefferedSize;
    }

    //override the paintComponent
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int x = 20; //x coordinate for the panel
        int y = 7; //y coordinate for the panel 

        //cast the Graphics object passed into a component’s rendering method to a Graphics2D object
        Graphics2D g2 = (Graphics2D) g;

        //To specify the rendering mode to be used(ANTIALIASING or smooth edges).
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        Shape clip = g2.getClip();
        g2.setColor(Color.WHITE);
        g2.fill(clip);

        //Draw the base name and increase the y coordinate
        g2.setColor(getForeground());
        g2.setFont(BASE_NAME_FONT);
        y += g2.getFontMetrics().getHeight();
        g2.drawString(baseName, x, y);


        //For icons:
        int iconX = 0; //x coordinate for icon 
        int iconY = 0; //y coordinate for icon
        int baseNameWidth = g2.getFont().getStringBounds(baseName, g2.getFontRenderContext()).getBounds().width;
        iconX = x + baseNameWidth + 10; //10 is the space between the base name and icon.

        //Draw the icons of each base and manage the coordinates(x,y) to draw the icon  :
        for (int i = 0; i < icons.size(); i++) {
            if (icons.get(i) != null) {
                URL url = icons.get(i);
                ImageIcon imageIcon = new ImageIcon(url);
                iconY = y - imageIcon.getIconHeight();
                g2.drawImage(imageIcon.getImage(), iconX, iconY, this);

                if (i != icons.size() - 1) //if it is not the last icon in the list
                {
                    iconX += imageIcon.getIconWidth(); //increase the offset
                }
            }
        }
        //------------------------------------	
       //Addetional space:
        x += 30; 
        y += 11;
        
        //Draw the description and increase the y coordinate
        g2.setFont(DESCRIPTION_FONT);
        y += g2.getFontMetrics().getHeight() + 5;

        //only if there is a baseDescription, draw it
        if (baseDescription != null) {

            //g2.drawString(baseDescription, x, y);
            drawStringMultiLine(g2,baseDescription, 700,  x, y);
        }


    }
    
    public static void drawStringMultiLine(Graphics2D g, String text, int lineWidth, int x, int y) {
    FontMetrics m = g.getFontMetrics();
    if(m.stringWidth(text) < lineWidth) {
        g.drawString(text, x, y);
    } else {
        String[] words = text.split(" ");
        String currentLine = words[0];
        for(int i = 1; i < words.length; i++) {
            if(m.stringWidth(currentLine+words[i]) < lineWidth) {
                currentLine += " "+words[i];
            } else {
                g.drawString(currentLine, x, y);
                y += m.getHeight();
                currentLine = words[i];
            }
        }
        if(currentLine.trim().length() > 0) {
            g.drawString(currentLine, x, y);
            y += m.getHeight();
        }
    }
}
}
