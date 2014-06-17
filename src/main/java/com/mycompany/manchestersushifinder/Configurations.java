/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.manchestersushifinder;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author Atheer
 */
public class Configurations {

    static File fXmlFile;
    DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
    Document doc = null;
    DocumentBuilder docBuilder;
    Element docElem;
    String configFileURL = null;
   

    public Configurations() throws ParserConfigurationException, SAXException, IOException {
        //fXmlFile = new File(getConfigFileURL());
        //docBuilder = docBuilderFactory.newDocumentBuilder();
        //doc = docBuilder.parse(fXmlFile);
        if (Global.useDefault)
        {
        doc=getDocument("config.xml");
        }
        else
        {
          doc=getDocument(Global.configFileURL);  
        }
    }

    public Element getRootElement() {
        return doc.getDocumentElement();
    }

    public ArrayList<String[]> getBases() {

        ArrayList<String[]> listOfBases = new ArrayList<String[]>();


        Element element = (Element) doc.getDocumentElement().getElementsByTagName("QueryTemplates").item(0);
        NodeList list = element.getChildNodes();
        for (int i = 0; i < list.getLength(); i++) {
            Node n = list.item(i);
            if (n instanceof Element) {

                Element el = (Element) n;
                if (el.getAttribute("show").equalsIgnoreCase("yes")) {
                    String text = el.getAttribute("name");
                    String id = el.getAttribute("Id");

                    listOfBases.add(new String[]{id, text});

                }
            }
        }
        return listOfBases;
    }

    public ArrayList<String> getAvailableLanguages() {

        ArrayList<String> listOfLanguages = new ArrayList<String>();

        Element element = (Element) doc.getDocumentElement().getElementsByTagName("AvailableLanguages").item(0);
        NodeList list = element.getChildNodes();
        for (int i = 0; i < list.getLength(); i++) {
            Node n = list.item(i);
            if (n instanceof Element) {

                Element el = (Element) n;
                String text = el.getAttribute("name");
                listOfLanguages.add(text);

            }
        }
        return listOfLanguages;
    }

    public IRI getIngredientClass() {
        String ingredientClassName = null;
        Element element = (Element) doc.getDocumentElement().getElementsByTagName("IngredientClass").item(0);
        if (element != null) {
            ingredientClassName = element.getAttribute("name");
        }
        return IRI.create(ingredientClassName);
    }

    public IRI getResultsAnnotaionProperty() {
        String annotaionPropertyName = null;
        Element element = (Element) doc.getDocumentElement().getElementsByTagName("ResultsAnnotationProperty").item(0);
        if (element != null) {
            annotaionPropertyName = element.getAttribute("name");
        }
        return IRI.create(annotaionPropertyName);
    }

    public String getOntologyLocation() {
        String ontologyURL = null;
        Element element = (Element) doc.getDocumentElement().getElementsByTagName("OntologyLocation").item(0);
        if (element != null) {
            
            if (Global.useDefault)
            {
               
               ontologyURL = element.getAttribute("url");
                //ontologyURL=getClass().getResource(element.getAttribute("url")).toString();
            }
            else
            {
            ontologyURL = element.getAttribute("url");
            }
        }
        return ontologyURL;
    }

     private Document getDocument(String fileName) {
        try {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        
            File file = new File(fileName);
            if(file.exists()) {
                return documentBuilder.parse(file);
            }
            else {
                InputStream is = getClass().getClassLoader().getResourceAsStream(fileName);
                return documentBuilder.parse(is);
            }
        }
        catch(IOException ioEx) {
            ioEx.printStackTrace();
        }
        catch(SAXException saxEx) {
            saxEx.printStackTrace();
        }
        catch(ParserConfigurationException e) {
            e.printStackTrace();
        }
        return null;
    }
     
    public String getClassRenderingUse() {
        String classRenderingUse = null;
        Element element = (Element) doc.getDocumentElement().getElementsByTagName("ClassRendering").item(0);
        if (element != null) {
            classRenderingUse = element.getAttribute("use");
        }
        return classRenderingUse;
        //if there is no selected language, use IRI or Label without language to render class names??
    }

    public void setConfigFileURL(String configFileURL) {
        this.configFileURL = configFileURL;
    }

    public static String getConfigFileURL() {

        if (Global.useDefault) {
            URL f = StartFrame.class.getResource("/config.xml");
            String filePathString = f.getFile();
            return filePathString;
        } else {
            return Global.configFileURL;
        }
    }

    public String getIncludedLabel() {
        Element element = (Element) doc.getDocumentElement().getElementsByTagName("IncludedLabel").item(0);
        if (element != null) {
            if (element.hasAttribute("text") && !element.getAttribute("text").isEmpty()) {
                return element.getAttribute("text").toString();
            }
        }
        return "Included Things";
    }

    public String getExcludedLabel() {
        Element element = (Element) doc.getDocumentElement().getElementsByTagName("ExcludedLabel").item(0);
        if (element != null) {
            if (element.hasAttribute("text") && !element.getAttribute("text").isEmpty()) {
                return element.getAttribute("text").toString();
            }
        }
        return "Excluded Things";
    }

    public String getTitleLabel() {
        Element element = (Element) doc.getDocumentElement().getElementsByTagName("TitleLabel").item(0);
        if (element != null) {
            if (element.hasAttribute("text") && !element.getAttribute("text").isEmpty()) {
                return element.getAttribute("text").toString();
            }
        }
        return "Restaurant Name Here.";
    }

    public BufferedImage getLogoImage() throws MalformedURLException, IOException {
        BufferedImage logo = null;
        String URLString;
        Element element = (Element) doc.getDocumentElement().getElementsByTagName("Logo").item(0);
        if (element != null) {

            if (element.hasAttribute("URL")) {
                URLString = element.getAttribute("URL").toString();
                if (!URLString.isEmpty()) {
                    //if the default config.xml is being used then get the image from resources:
                    if (Global.useDefault) {
                        URL url = getClass().getResource(URLString); 
                        logo = ImageIO.read(url);
                    }
                    else
                    {
                       URL url = new URL(URLString);
                       logo = ImageIO.read(url);
                    }

                }
            }

        }
        return logo;
    }

    public URL getResultCharacteristicIcon(String className) throws MalformedURLException {

        URL url = null;
        Element element = (Element) doc.getDocumentElement().getElementsByTagName("ResultsCharacteristics").item(0);
        NodeList nl = element.getChildNodes();
        for (int i = 0; i < nl.getLength(); i++) {
            Node n = nl.item(i);
            if (n instanceof Element) {
                Element el = (Element) n;
                if (el.getAttribute("class").toString().equals(className)) {
                    if (Global.useDefault)
                    {
                      url=getClass().getResource(el.getAttribute("url"));  
                    }
                    else
                    {
                    url = new URL(el.getAttribute("url"));
                    }

                }

            }
        }//end for

        return url;
    }

    public ArrayList<OWLClass> getResultCharacteristics() {

        ArrayList<OWLClass> cls = new ArrayList<OWLClass>();
        IRI classIRI = null;
        Element element = (Element) doc.getDocumentElement().getElementsByTagName("ResultsCharacteristics").item(0);
        if (element != null) {
            NodeList nl = element.getChildNodes();
            for (int i = 0; i < nl.getLength(); i++) {
                Node n = nl.item(i);
                if (n instanceof Element) {
                    Element el = (Element) n;
                    classIRI = IRI.create(el.getAttribute("class").toString());
                    OWLClass c = Global.myOntology.getDf().getOWLClass(classIRI);
                    cls.add(c);
                }
            }//end for
        }
        return cls;

    }
    
    
      public ArrayList<OWLClass> getIngredientsClassifications() {

        ArrayList<OWLClass> cls = new ArrayList<OWLClass>();
        IRI classIRI = null;
        Element element = (Element) doc.getDocumentElement().getElementsByTagName("IngredientsClassifications").item(0);
        if (element != null) {
            NodeList nl = element.getChildNodes();
            for (int i = 0; i < nl.getLength(); i++) {
                Node n = nl.item(i);
                if (n instanceof Element) {
                    Element el = (Element) n;
                    classIRI = IRI.create(el.getAttribute("class").toString());
                    OWLClass c = Global.myOntology.getDf().getOWLClass(classIRI);
                    cls.add(c);
                }
            }//end for
        }
        return cls;

    }
      
  public ArrayList<OWLObjectProperty> getIngredientsFactes() {

        ArrayList<OWLObjectProperty> properties = new ArrayList<OWLObjectProperty>();
        IRI propertyIRI = null;
        Element element = (Element) doc.getDocumentElement().getElementsByTagName("IngredientsFacets").item(0);
        if (element != null) {
            NodeList nl = element.getChildNodes();
            for (int i = 0; i < nl.getLength(); i++) {
                Node n = nl.item(i);
                if (n instanceof Element) {
                    Element el = (Element) n;
                    propertyIRI = IRI.create(el.getAttribute("property").toString());
                    OWLObjectProperty p = Global.myOntology.getDf().getOWLObjectProperty(propertyIRI);
                    properties.add(p);
                }
            }//end for
        }
        return properties;

    }
    //--------------------------------------------------
    //This function returns the element with attName=attValue
    //To be used in returning query template based on its ID

    public Element getNodeWithAttribute(Node root, String attrName, String attrValue) {
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
}
