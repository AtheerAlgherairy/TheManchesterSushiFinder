/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.manchestersushifinder;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

/**
 *
 * @author Atheer
 */
public class Global {
    
    public static OntologyClass myOntology;
    public static Configurations myConfig;
    public static boolean useDefault=true;
    public static String configFileURL=null;
    
    Global()
    {
        
        try {
            myConfig=new Configurations();
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(Global.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SAXException ex) {
            Logger.getLogger(Global.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Global.class.getName()).log(Level.SEVERE, null, ex);
        }
        
         //if the files are in src/main/resources   
        //myOntology= new OntologyClass(getClass().getResource(myConfig.getOntologyLocation()).toString());
       
            myOntology= new OntologyClass(myConfig.getOntologyLocation());
       
    }
}
