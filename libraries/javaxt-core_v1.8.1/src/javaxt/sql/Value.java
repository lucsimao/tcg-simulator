package javaxt.sql;

//******************************************************************************
//**  Value Class
//******************************************************************************
/**
 *   Used to represent a value for a given field in the database. The value can
 *   be converted into a number of Java primatives (strings, integers, doubles,
 *   booleans, etc).
 *
 ******************************************************************************/

public class Value extends javaxt.utils.Value {

  //**************************************************************************
  //** Constructor
  //**************************************************************************
  /** Creates a new instance of Value. */

    public Value(Object value){
        super(value);
    }

    public java.sql.Timestamp toTimeStamp(){
        Object obj = super.toObject();
        if (obj!=null){
            if (obj instanceof java.sql.Timestamp){
                return (java.sql.Timestamp) obj;
            }
            else{
                javaxt.utils.Date date = toDate();
                return new java.sql.Timestamp(date.getDate().getTime());
            }
        }
        return null;
    }

    public Object toArray(){
        Object obj = super.toObject();
        if (obj!=null){
            try{
                return ((java.sql.Array) obj).getArray();
            }
            catch(Exception e){
            }
        }

        return null;
    }
}
