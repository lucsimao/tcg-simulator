package javaxt.utils;

//******************************************************************************
//**  String Library - By Peter Borissow
//******************************************************************************
/**
 * Used to provide functionality similar to Microsoft VB String Class
 *
 ******************************************************************************/

public class string {


    /** Defeats instantiation. */
    private string(){}
    
  //**************************************************************************
  //** Replace
  //**************************************************************************    
    public static String Replace(String str, String target, String replacement){ 
        return replace(str, target, replacement); 
    }
    
    public static String replace(String str, String target, String replacement){
        CharSequence Target = (CharSequence) target;
        CharSequence Replacement = (CharSequence) replacement;
        return str.replace(Target,Replacement);
    }

    
  //**************************************************************************
  //** Replace (Case Insensitive)
  //************************************************************************** 
    
    public static String Replace(String str, String target, String replacement, boolean IgnoreCase){ 
        return replace(str, target, replacement, IgnoreCase); 
    }
    public static String replace(String str, String target, String replacement, boolean IgnoreCase){
        
        if (target.equals("")){
            return str;
        }
        
        if (str.toLowerCase().indexOf(target.toLowerCase())<0){
            return str;
        }
        
        if (IgnoreCase==false){
            return replace(str, target, replacement);
        }
        else{
            
            String orgStr = str;
            int lenTarget = target.length();
            
            str = str.toLowerCase();
            target = target.toLowerCase();
            
            String[] arr = split(str,target);
            StringBuffer s = new StringBuffer();
            int StartPos = 0;
            int EndPos = 0;
            for (int i=0; i<arr.length; i++){
                 
                
                 EndPos = StartPos + arr[i].length() + lenTarget;
                 String seg = orgStr.substring(StartPos,EndPos-lenTarget);
                 
                 boolean append = false;
                 if (i<arr.length-1){
                     append = true;
                 }
                 else {   //if (i<arr.length){                 
                     if (arr.length == 1 ){
                         append = true;
                     }
                     else{
                     
                     }
                     
                 }
                 
                 if (append){
                     seg += replacement;
                 }
                 
                 
                 
                 s.append(seg);
                 StartPos = EndPos;
            }
            
            
            
            return s.toString();
        }
    }
    
    
  //**************************************************************************
  //** Split
  //************************************************************************** 
    public static String[] Split(String str, String ch){return split(str,ch);}
    public static String[] split(String str, String ch){
        
        //String metaChars = "([{\^-$|])?*+.";
        
        String inValidChars = "\\.[]|(){^-$?*+";        
        for (int i=0; i < inValidChars.length(); i++){ 
             String target = inValidChars.charAt(i) + "";
             ch = Replace(ch,target,"\\" + target);
        }  
        
        return str.split(ch);
    }  
    

    
  //**************************************************************************
  //** lcase/ucase
  //**************************************************************************    
    public static String Lcase(String str){ return lcase(str); }
    public static String lcase(String str){ return str.toLowerCase(); }  
    
    public static String Ucase(String str){ return ucase(str); }
    public static String ucase(String str){ return str.toUpperCase(); }    
    
    
    
  //**************************************************************************
  //** instr
  //**************************************************************************    
    public static int InStr(String str, String ch){ return instr(str, ch); }
    public static int instr(String str, String ch){ return str.indexOf(ch)+1; }
    
  //**************************************************************************
  //** contains (was "inStr")
  //**************************************************************************     
    public static boolean contains(String str, String ch){
        return str.contains((CharSequence) ch);
    }
    
    public static boolean contains(String str, String ch, boolean IgnoreCase){
        if (IgnoreCase){
            str = str.toLowerCase();
            ch = ch.toLowerCase();
        }
        return str.contains((CharSequence) ch);
    }
    
    
  //**************************************************************************
  //** instrRev
  //**************************************************************************    
    public static int instrRev(String str, String ch){
        return str.lastIndexOf(ch)+1;
    }

    
  //**************************************************************************
  //** Len
  //**************************************************************************
  /**  Returns the length of a string */
    
    public static int Len(String str){ return len(str);}
    public static int len(String str){ return str.length(); }

    
  //**************************************************************************
  //** Left
  //**************************************************************************  
  /**  Returns characters on the left side of a string */
    
    public static String Left(String str, int n){return left(str,n);}
    public static String left(String str, int n){
	if (n <= 0)
	    return "";
	else if (n > str.length())
	    return str;
	else
	    return str.substring(0,n);
    }


  //**************************************************************************
  //** Right
  //**************************************************************************    
  /**  Returns characters on the right side of a string */
    
    public static String Right(String str, int n){return right(str,n);}
    public static String right(String str, int n){
        if (n <= 0)
           return "";
        else if (n > str.length())
           return str;
        else {
           int iLen = str.length();
           return str.substring(iLen - n, iLen);
        }
    }

    
  //**************************************************************************
  //** Mid
  //**************************************************************************  
    public static String Mid(String Target, int Start, int Length){
        Start = Start - 1;
        return Target.substring(Start, Start+Length);
    }
    
    
  //**************************************************************************
  //** Trim
  //**************************************************************************     
    public static String Trim(String str){return trim(str);}
    public static String trim(String str){return str.trim();}
    
    
  //**************************************************************************
  //** RTrim
  //************************************************************************** 
    public static String RTrim(String str){
        
        while (str.endsWith(" ") || 
               str.endsWith("\r") || 
               str.endsWith("\n") || 
               str.endsWith("\t")) {
               str = str.substring(0,str.length()-1);
        }
        return str;

    }

    
  //**************************************************************************
  //** LTrim
  //************************************************************************** 
    
    public static String LTrim(String str){
        
        while (str.startsWith(" ") || 
               str.startsWith("\r") || 
               str.startsWith("\n") || 
               str.startsWith("\t")) {
               str = str.substring(1);
        }
        return str;
    }   
    
    
  //**************************************************************************
  //** isUpperCase
  //************************************************************************** 
  /**  Used to determine whether the string starts with a capital letter */
    
    public static boolean isUpperCase(String str){
        if (str==null) return false;
        else str = str.trim();
        
        if (str.length()==0) return false;
        else {
            char c = str.charAt(0);
            return (new Character(c)).isUpperCase(c);
        }
    }
    
    
  //**************************************************************************
  //** hasUpperCase
  //************************************************************************** 
  /**  Used to determine whether the string contains upper-case letters. */
    
    public static boolean hasUpperCase(String str){
        if (str==null) return false;
        else str = str.trim();
        
        for (int i=0; i<str.length(); i++){
            char c = str.charAt(i);
            if ( (new Character(c)).isUpperCase(c) ) return true;
        }
        
        return false;
    }
    
    
  //**************************************************************************
  //** isPunctuation
  //************************************************************************** 
  /**  Used to determine whether the string ends with a punctuation */
    
    public static boolean isPunctuation(String str){
        String chars = ".,?!";
        for (int i=0; i < chars.length(); i++){ 
             String x = chars.charAt(i) + "";
             if (str.equals(x)){
                 return true;
             }
        }
        return false;
    }
    
    
  //**************************************************************************
  //** UpdateCase
  //**************************************************************************
    
    public static String UpdateCase(String str){
        String[] arr = str.split(" ");
        StringBuffer out = new StringBuffer();
        for (int i=0; i<arr.length; i++){
             String row = arr[i];
             if (row.length()>0){
                 String A = arr[i].substring(0,1).toUpperCase();
                 String B = arr[i].substring(1,arr[i].length()).toLowerCase();
                 out.append(A + B + " ");
             }
             else{
                 out.append(" ");
             }
        }
        str = out.toString();
        if (str.length()>0){
            str = str.substring(0, str.length()-1);
        }
        return str;
    }
    
    
    
    public static boolean isNumeric(String str){
        try{
            double d = Double.valueOf(str).doubleValue();
            return true;
        }
        catch(Exception e){
            return false;
        }
    }
    

    
  //**************************************************************************
  //** Conversions from String to Integer
  //**************************************************************************  
    
  /** Used to convert a string to an int. Rounds to the nearest int. */
    public static int toInt(String d){ return toInt(toDouble(d)); }
    
  /** Used to convert a double to an int. Rounds the double to the nearest int. */
    public static int toInt(double d){ return (int)Math.round(round(d,0)); }
    
  /** Used to convert a string to an int. Rounds to the nearest int. */
    public static int cint(String str){return toInt(str); }
    
  /** Used to convert a double to an int. Rounds the double to the nearest int. */
    public static int cint(Double d){ return toInt(d);  }
    
    
  //**************************************************************************
  //** Conversions from String to Double
  //**************************************************************************  
    
  /** Used to convert a string to a double. */
    public static double toDouble(String d){ return Double.valueOf(d).doubleValue(); }
    
  /** Used to convert a string to a double. */
    public static double cdbl(String d){ return toDouble(d); }
    
    
  //**************************************************************************
  //** Conversions from Double to String
  //**************************************************************************
    
  /** Used to convert an int to a string. */
    public static String toString(int i){ return "" + i + ""; }
    
  /** Used to convert an int to a string. */
    public static String cstr(int i){ return toString(i); }
    
    
  //**************************************************************************
  //** Conversions from Double to String
  //**************************************************************************
    
    public static String toString(double d){ return cstr(d); }
    
    public static String cstr(double d){

        return new java.text.DecimalFormat("#.#######################").format( d);
        /*
        String s = d + "";
        if (s.contains((CharSequence) "E-")){
            //System.out.println(s);

            int e = Integer.valueOf(s.substring(s.indexOf("E")+1)).intValue(); 
            s = s.substring(0,s.indexOf("E"));
            
            //System.out.println(e);
            
            if (e<0){
                e = -e;
                e = e - (s.indexOf("."));                        
                s = s.replace((CharSequence)".", (CharSequence)"");            
                for (int i=0; i<e; i++){
                     s = "0" + s;
                }
                s = "0." + s;
            }
            else{ //this is incomplete/wrong
                int i = s.indexOf(".") + e;
                s = s.replace((CharSequence)".", (CharSequence)"");
                s = s.substring(0,i) + "." + s.substring(i);
            }
            
            return s;
        }
        else{
            return s;
        }
         *
         */
    } 
    
    

  //**************************************************************************
  //** Conversions from String to Boolean
  //**************************************************************************
    
  /** Used to convert an int to a string. */
    public static boolean toBoolean(String s){ 
        if (s==null) return false;
        else{
            s = s.toLowerCase().trim();
            if (s.equals("true")) return true;
            else return false;
        }
    }
    
    
  //**************************************************************************
  //** round double
  //**************************************************************************  
    public static double round(double value, int decimalPlace){
        double power_of_ten = 1;
        while (decimalPlace-- > 0){
            power_of_ten *= 10.0;
        }
        return Math.round(value * power_of_ten) / power_of_ten;
    }
    
    
    
    
    
/*
  //**************************************************************************
  //** Copy this section into your app
  //**************************************************************************
    
    private String replace(String str, String find, String replacement){ 
          return s.replace(str,find,replacement); }
    private String[] split(String str, String ch){ return s.split(str,ch); }   
    private String lcase(String str){return s.lcase(str); }
    private String ucase(String str){return s.ucase(str); }
    private int instr(String str, String ch){ return s.instr(str,ch); }
    private int instrRev(String str, String ch){ return s.instrRev(str,ch); }
    private int len(String str){ return s.len(str); }
    private String left(String str, int n){ return s.left(str,n); }
    private String right(String str, int n){ return s.right(str,n); } 
    private String trim(String str){ return s.trim(str); } 
    private int cint(String str){return s.cint(str); }
    private double cdbl(String str){return s.cdbl(str); }
    private String cstr(int n){return s.cstr(n); }
    private String cstr(double n){return s.cstr(n); }  
*/    
    
}
