package javaxt.webservices;
import org.w3c.dom.*;

//******************************************************************************
//**  Parameters Class
//******************************************************************************
/**
 *   A convenience class used to represent multiple parameters.
 *
 ******************************************************************************/

public class Parameters {

    private String vbCrLf = "\r\n";

    private Parameter[] Parameters = null;
    private StringBuffer xml = null;
    private StringBuffer html = null;

    public Parameters(Parameter[] Parameters){
        this.Parameters = Parameters;
    }

    public Parameter[] getArray(){
        return Parameters;
    }

    
    public int getLength(){
        return Parameters.length;
    }


    public void setValue(Parameter p){
        if (Parameters==null) return;
        Parameter parameter = getParameter(p.getName());
        if (parameter!=null) parameter.setValue(p.getValue());
    }

    /** Used to set a parameter value. Use "/" character to separate nodes */
    public void setValue(String parameterName, Object parameterValue){
        if (Parameters==null) return;
        Parameter parameter = getParameter(parameterName);
        if (parameter!=null) parameter.setValue(parameterValue);
    }

    public Object getValue(String ParameterName){
        Parameter p = getParameter(ParameterName);
        return p==null? null : p.getValue();
    }

    public Parameter getParameter(String ParameterName){
        if (Parameters==null) return null;
        else return getParameter(Parameters, ParameterName);
    }

    public Parameter getParameter(int i){
        return Parameters[i];
    }


    private Parameter getParameter(Parameter[] Parameters, String ParameterName){
        String A, B = "";
        if (ParameterName.contains((CharSequence) "/")){
            String[] arr = ParameterName.split("/");
            A = arr[0];
            B = ParameterName.substring(A.length() + 1);
            //System.out.println(A + " vs " + B);
            ParameterName = A;
        }
        for (int i=0; i<Parameters.length; i++){
             if (Parameters[i].getName().equalsIgnoreCase(ParameterName)){
                 if (B.length()==0){
                     //System.out.println("Return " + Parameters[i].getName());
                     return Parameters[i];
                 }
                 else{
                     //System.out.println("getParameter");
                     return getParameter(Parameters[i].getChildren(), B);
                 }
             }
        }
        return null;
    }


    public String toString(){
        return toString(null);
    }


    public String toString(String ns){
        if (Parameters!=null){
            xml = new StringBuffer();
            getParameters(Parameters, ns);
            return xml.toString();
        }
        else{
            return "";
        }
    }




    private String getAttributes(){
        StringBuffer attr = new StringBuffer();
        if (Parameters!=null){
            for (Parameter parameter : Parameters){
                if (parameter.IsAttribute){
                    
                    if (parameter.isComplex()){
                        Parameter[] children = parameter.getChildren();
                        if (children!=null){
                            attr.append(new Parameters(children).getAttributes());
                        }
                    }                    
                    else{
                        attr.append(" ");
                        attr.append(parameter.getName());
                        attr.append("=\"");
                        attr.append(javaxt.xml.DOM.escapeXml((String) parameter.getValue()) );
                        attr.append("\"");
                    }
                }
            }
        }
        return attr.toString();
    }


    private void getParameters(Parameter[] Parameters, String ns){
        if (Parameters!=null){
            for (int i=0; i<Parameters.length; i++){
                 Parameter Parameter = Parameters[i];
                 getParameter(Parameter, ns);
            }
        }
    }

    private void getParameter(Parameter parameter, String ns){

        if (parameter.IsAttribute){
            //xml.append("<" + parameter.getName());
        }
        else{
            if (parameter.isComplex()){
                getParameters(parameter.getChildren(), ns);
            }
            else{
                xml.append(parameter.toXML(getAttributes(), ns));
            }
        }
    }


// <editor-fold defaultstate="collapsed" desc="Convert Parameters to HTML/Web Form. Click on the + sign on the left to edit the code.">


        /** Used to generate html form inputs */
        public String toHTML(){

           String buttons =
           "<div><br>" +
           "<input type=\"submit\" value=\"Invoke\" name=\"Invoke\" class=\"button\">&nbsp;&nbsp;" +
           "<input type=\"reset\" value=\"Reset\" name=\"Reset\" class=\"button\">" +
           "</div>" + vbCrLf;

            if (Parameters==null){
                return buttons;
            }
            else{
                html = new StringBuffer();
                html.append(vbCrLf);
                html.append("<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" style=\"border-collapse:collapse;\">" + vbCrLf);
                addRows(Parameters);
                html.append("<tr><td colspan=\"2\"></td><td align=\"center\">" + buttons + "</td><td></td></tr>"  + vbCrLf);
                html.append("</table>" + vbCrLf);

                return html.toString();
            }


        }

        private void addRows(Parameter[] Parameters){
            if (Parameters!=null){
                for (int i=0; i<Parameters.length; i++){
                     Parameter Parameter = Parameters[i];
                     addRow(Parameter);
                }
            }
        }

        private void addRow(Parameter Parameter){

               String ParameterName = Parameter.getName();
               String ParameterType = Parameter.getType();
               //String ParameterValue = Parameter.getValue();

               boolean isRequired = Parameter.isRequired();
               boolean isComplex = Parameter.isComplex();

               Option[] Options = Parameter.getOptions();

               String InputText = "";
               String InputName = "";
               String InputValue = "";
               String InputHTML = "";

             //Set Input Text
               if (isRequired){
                   InputText = ParameterName + "<span style=\"color:#FF0000;\">*</span>";
               }
               else{
                   InputText = ParameterName;
               }

             //Set Input Name
               InputName = getParentName(Parameter.getParentNode()) + ParameterName;

             //Set Input HTML
               if (ParameterType.equalsIgnoreCase("String")){
                   String type = "text";
                   if (ParameterName.toLowerCase().contains((CharSequence) "password")){
                       type = "password";
                   }
                   InputHTML = "<input style=\"width:275px;\" type=\"" + type + "\" size=\"40\" name=\"" + InputName + "\">";
               }
               else if (ParameterType.equalsIgnoreCase("Boolean")){
                   InputHTML =
                   "<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" style=\"border-collapse:collapse;\">" +
                   "<tr>" +
                     "<td><input type=\"radio\" value=\"TRUE\" checked name=\"" + InputName + "\"></td>" +
                     "<td>TRUE</td>" +
                     "<td>&nbsp;&nbsp; </td>" +
                     "<td><input type=\"radio\" value=\"FALSE\" checked name=\"" + InputName + "\"></td>" +
                     "<td>FALSE</td>" +
                   "</tr>" +
                   "</table>";
               }
               else if (ParameterType.equalsIgnoreCase("base64Binary")){
                   InputHTML = "<input style=\"width:275px;\" type=\"file\" size=\"40\" name=\"" + InputName + "\">";
               }
               else{
                   InputHTML = "<input style=\"width:275px;\" type=\"text\" size=\"40\" name=\"" + InputName + "\">";
               }

               if (isComplex){
                   InputHTML = "";
               }

               if (Options!=null){
                   InputHTML = "<select style=\"width:275px;\" name=\"" + InputName + "\">";
                   for (int i=0; i<Options.length; i++){
                        Option Option = Options[i];
                        InputHTML += "<option value=\"" + Option.getValue() + "\">" + Option.getName() + "</option>";
                   }
                   InputHTML += "</select>";
               }

               html.append("<tr>" + vbCrLf);
               html.append("<td></td>" + vbCrLf); //spacer or plus/minus sign
               html.append("<td valign=\"top\">" + InputText + "</td>" + vbCrLf);
               html.append("<td valign=\"top\" style=\"padding-left:5px;padding-right:5px;\">" + InputHTML + "</td>" + vbCrLf);
               html.append("<td valign=\"top\" class=\"smgrytxt\"><i>" + ParameterType + "</i></td>" + vbCrLf);
               html.append("</tr>" + vbCrLf);


            if (Parameter.isComplex()){
                addRows(Parameter.getChildren());
            }
        }

        private String getParentName(Node ParameterNode){
            String ret = "";
            while (ParameterNode!=null){
                if (ParameterNode.getNodeType()==1){
                    if (ParameterNode.getNodeName().equals("parameter")){
                        NamedNodeMap attr = ParameterNode.getAttributes();
                        String ParameterName = javaxt.xml.DOM.getAttributeValue(attr,"name");
                        ret = ParameterName + "/" + ret;
                    }
                }
                ParameterNode = ParameterNode.getParentNode();
            }
            return ret;
        }
// </editor-fold>



}



