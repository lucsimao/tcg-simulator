package javaxt.html;

//******************************************************************************
//**  HTML Parser
//******************************************************************************
/**
 *   A simple html parser used to extract blocks of html from a document.
 *
 ******************************************************************************/

public class Parser {
    
    private String html;


  //**************************************************************************
  //** Constructor
  //**************************************************************************
    public Parser(String html){
        this.html = html;
    }

    
  //**************************************************************************
  //** getHTML
  //**************************************************************************
    public String getHTML(){
        return html;
    }


  //**************************************************************************
  //** setHTML
  //**************************************************************************
  /** Used to reset the "scope" of the parser 
   */ 
    public void setHTML(String html){
        this.html = html;
    }


  //**************************************************************************
  //** getElementByID
  //**************************************************************************
  /** Returns an HTML Element with a given id. Returns null if the element was 
   *  not found.
   */
    public Element getElementByID(String id){
        return getElementByAttributes(null, "id", id);
    }


  //**************************************************************************
  //** getElementByTagName
  //**************************************************************************
  /** Returns an array of HTML Elements found in the HTML document with given
   *  tag name.
   */
    public Element[] getElementsByTagName(String tagName){
        String orgHTML = html;
        java.util.ArrayList<Element> elements = new java.util.ArrayList<Element>();
        Element e = getElementByTagName(tagName);
        while (e!=null){
            elements.add(e);
            int idx = html.indexOf(e.outerHTML);
            String a = html.substring(0, idx);
            String b = html.substring(idx+e.outerHTML.length());
            html = a + b;
            e = getElementByTagName(tagName);
        }
        
        html = orgHTML;
        return elements.toArray(new Element[elements.size()]);
    }


  //**************************************************************************
  //** getElementByTagName
  //**************************************************************************
  /** Returns an array of HTML Elements found in the HTML document with given
   *  tag name, attribute, and attribute value (e.g. "div", "class", "hdr2").
   */
    public Element[] getElements(String tagName, String attributeName, String attributeValue){
        String orgHTML = html;
        java.util.ArrayList<Element> elements = new java.util.ArrayList<Element>();
        Element e = getElementByAttributes(tagName, attributeName, attributeValue);
        while (e!=null){
            elements.add(e);
            int idx = html.indexOf(e.outerHTML);
            String a = html.substring(0, idx);
            String b = html.substring(idx+e.outerHTML.length());
            html = a + b;
            e = getElementByAttributes(tagName, attributeName, attributeValue);
        }

        html = orgHTML;
        return elements.toArray(new Element[elements.size()]);
    }


  //**************************************************************************
  //** getElementByTagName
  //**************************************************************************
  /** Returns the first HTML Element found in the HTML document with given tag
   *  name. Returns null if an element was not found.
   */
    public Element getElementByTagName(String tagName){
        return getElementByAttributes(tagName, null, null);
    }


  //**************************************************************************
  //** getElementByAttributes
  //**************************************************************************
  /** Returns the first HTML Element found in the HTML document with given tag
   *  name and attribute. Returns null if an element was not found.
   */
    public Element getElementByAttributes(String tagName, String attributeName, String attributeValue){
        String s = html + " ";
        String c = "";
        
        boolean concat = false;
        int absStart = 0;
        int absEnd = 0;
        int numStartTags = 0;
        int numEndTags = 0;
         
        int outerStart = 0;
        int outerEnd = 0;

        StringBuffer tag = new StringBuffer();
        Element el = null;

        boolean insideQuote = false;
        boolean insideComment = false;
                 
        for (int i=0; i<s.length(); i++){

            c = s.substring(i,i+1); 


          //If we find the start of an html element, start assembling the tag
            if (c.equals("<") && !insideQuote && !insideComment){
                concat = true;
                absEnd = i;
            }
              

          //Check whether we are inside or outside a quote
            if (c.equals("\"")) {
                if (!insideQuote) insideQuote = true;
                else insideQuote = false;
            }
            

          //Check whether we are inside or outside a javascript or css comment
            if (c.equals("/")) {
                if (insideComment){
                    insideComment = (s.substring(i-1,i+1).equals("*/"));
                }
                else{
                    insideComment = (s.substring(i,i+2).equals("/*"));
                }
            }



            if (concat==true){
                tag.append(c);
            }


              
          //If we find the end of an html element, analyze the tag
            if ((c.equals(">") && !insideQuote && !insideComment) && concat==true){

                concat = false;
                Element Tag = new Element(tag.toString());

                if (el==null){
                    if (Tag.isStartTag()){
                        if (tagName==null || Tag.getName().equalsIgnoreCase(tagName)){ //<--Tag name is null when getElementByID

                            if (attributeName==null || Tag.getAttribute(attributeName).equalsIgnoreCase(attributeValue)){ //<--Filter by attributes!
                                absStart = i+1;
                                el = Tag;
                                outerStart = absStart - tag.length();


                              //Special case for getElementByID
                                if (tagName==null) tagName = Tag.getName();


                              //Special case for tags that self terminate
                                if (Tag.isEndTag()){
                                    //Element.innerHTML = null;
                                    el.outerHTML = tag.toString();
                                    return el;
                                }
                                
                            }
                        }
                    }
                }
                else {

                    if (Tag.getName().equalsIgnoreCase(tagName)){

                        
                        if (Tag.isStartTag()) numStartTags +=1;
                        //if (!Tag.isStartTag()) numEndTags +=1;
                        if (Tag.isEndTag()) numEndTags +=1;


                      //This is all new!
                        boolean foundEnd = false;
                        if (Tag.isStartTag() && Tag.isEndTag()){
                            foundEnd = false;
                        }
                        else if(!Tag.isStartTag() && Tag.isEndTag()){
                            foundEnd = (numEndTags>numStartTags);
                        }
                        else{
                            //System.out.println(numEndTags + " vs " + numStartTags);
                            foundEnd = (numEndTags>=numStartTags);
                        }



                        if (foundEnd){ // if (numEndTags>=numStartTags){
                            el.innerHTML = html.substring(absStart,absEnd);
                            outerEnd = i+1;
                            el.outerHTML = html.substring(outerStart,outerEnd);
                            return el;
                        }
                    }


                }

                 
              //Clear tag variable for the next pass
                tag = new StringBuffer();
            }


        }


      //Last ditch effort!
        if (el!=null){
            if (el.getOuterHTML()==null){
                el.outerHTML = el.getTag();
            }
            return el;
        }

        return null;
    }


  //**************************************************************************
  //** getImageLinks
  //**************************************************************************
  /** Returns a list of links to images. The links may include relative paths.
   *  Use the getAbsolutePath method to resolve the relative paths to a fully
   *  qualified url.
   */
    public String[] getImageLinks(){
        java.util.ArrayList<String> links = new java.util.ArrayList<String>();
        for (Element img : getElementsByTagName("img")){
            String src = img.getAttribute("src");
            if (src.length()>0) links.add(src);
        }
        return links.toArray(new String[links.size()]);
    }

    
  //**************************************************************************
  //** stripHTMLTags
  //**************************************************************************
  /** Used to remove any html tags from a block of text 
   */
    public static String stripHTMLTags(String html){
        
        String s = html + " ";
        String c = "";
        boolean concat = false;   
        String tag = "";
                 
        for (int i = 0; i < s.length(); i++){
              
             c = s.substring(i,i+1); 
               
             if (c.equals("<")){       
                 concat = true;
             }
              
             
             if (concat==true){
                 tag += c;
             } 
              
              
             if (c.equals(">") && concat==true){    
                 concat = false;
                 
                 html = html.replace(tag,"");
                         
               //Clear tag variable for the next pass
                 tag = "";     
             }
   
        }

        //html = html.replaceAll("\\s+"," ");
        
        return html.replace("&nbsp;", " ").trim();
    }
    
    
  //**************************************************************************
  //** MapPath
  //**************************************************************************
  /** Returns a fully qualified URL for a given path. Returns null if the  
   *  function fails to resolve the path.
   *  @param relPath Relative path to a file (e.g. "../images/header.jpg")
   *  @param url URL that is sourcing the relPath (e.g. "http://acme.com/about/")
   *  @return Using the examples cited in the 2 parameters, return a URL
   *  "http://acme.com/images/header.jpg"
   */
    public static String MapPath(String relPath, java.net.URL url){

      //Check if relPath is a fully qualified URL. If so, return the relPath.
        try{
            new java.net.URL(relPath);
            return relPath;
        }
        catch(Exception e){}

        
      //Remove "./" prefix in the relPath
        if (relPath.length()>2){
            if (relPath.substring(0,2).equals("./")){
                relPath = relPath.substring(2,relPath.length());
            }
        }
        
        
        String[] arrRelPath = relPath.split("/");
        try{
            String urlBase = url.getProtocol() + "://" + url.getHost();
            if (url.getPort()!=80) urlBase+= ":" + url.getPort();
            
            

          //Build Path
            String urlPath = "";
            String newPath;
            if (relPath.substring(0,1).equals("/")){
                newPath = relPath;
            }
            else{
            
                urlPath = "/";
                String[] arr = url.getPath().split("/");
                for (int i=0; i<=(arr.length-arrRelPath.length); i++){
                     String dir = arr[i];
                     if (dir.length()>0){

                         urlPath += dir + "/";
                     }
                }

                
              //This can be cleaned-up a bit...
                if (relPath.substring(0,1).equals("/")){
                    newPath = relPath.substring(1,relPath.length());
                }
                else if (relPath.substring(0,2).equals("./")){
                    newPath = relPath.substring(2,relPath.length());
                }
                else if (relPath.substring(0,3).equals("../")){
                    newPath = relPath.replace("../", "");
                }
                else{
                    newPath = relPath;
                }
            }

            return urlBase + urlPath + newPath;
        }
        catch(Exception e){}
        return null;
    }


  //**************************************************************************
  //** getAbsolutePath
  //**************************************************************************
  /** Returns a fully qualified URL for a given path. See MapPath() method for 
   *  more information.
   *  @deprecated Use MapPath()
   */
    public static String getAbsolutePath(String relPath, String url){
        try{
            return MapPath(relPath, new java.net.URL(url));
        }
        catch(Exception e){}
        return null;
    }
}