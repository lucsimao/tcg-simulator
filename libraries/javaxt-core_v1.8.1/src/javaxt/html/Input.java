package javaxt.html;

//******************************************************************************
//**  HTML Input
//******************************************************************************
/**
 *   Used to represent a single html form input.
 *
 ******************************************************************************/

public class Input {
    
    private String name;
    private Object value;
    
    public Input(String name, String value){
        this.name = name;
        this.value = value;
    }
    
    public Input(String name, javaxt.io.File file){
        this.name = name;
        this.value = file;
    }

    public String getName(){
        return name;
    }
    
    public Object getValue(){
        return value;
    }
    
    
    public String toString(){
        return name + "=" + (value==null ? "" : 
            (this.isFile() ? ((javaxt.io.File) value).getName() : value ));
    }
    
    
    public byte[] toByteArray(){
        if (value!=null){
            if (this.isFile()){
                return ((javaxt.io.File) value).getBytes().toByteArray();
            }
            else{
                try{
                    return value.toString().getBytes("UTF-8");
                }
                catch(java.io.UnsupportedEncodingException e){}
            }
        }
        return null;
    }
    
  /** Returns the size of the value. */
    public long getSize(){
        if (value==null) return 0;
        else{
            if (this.isFile()) return ((javaxt.io.File) value).getSize();
            else return this.toByteArray().length;
        }
    }
    
    public boolean isFile(){
        if (value!=null){
            return (value instanceof javaxt.io.File);
        }
        return false;
    }
}