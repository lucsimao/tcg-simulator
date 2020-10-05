package javaxt.utils;

//******************************************************************************
//**  Array Class
//******************************************************************************
/**
 *   Used to construct a 2d array of name/value pairs. The array is
 *   automatically resized whenever entries are added or deleted. This class
 *   serves much the same function as a HashMap except that this class
 *   makes it a little easier to create/retrieve entries with multiple values.
 *
 ******************************************************************************/

public class Array {

    java.util.HashMap entries;


  //**************************************************************************
  //** Constructor
  //**************************************************************************
  /** Creates a new instance of Array. */

    public Array() {
        entries = new java.util.HashMap();
    }


  //**************************************************************************
  //** Add
  //**************************************************************************
  /** Used to add an entry to the array. */

    public void add(Object key, Object value){

        java.util.List values = (java.util.List) entries.get(key);
        if (values==null) values = new java.util.Vector();
        values.add(value);
        entries.put(key, values);

    }


  //**************************************************************************
  //** Put
  //**************************************************************************
  /**  Used to add an entry to the array. This method is identical to the add
   *   method.
   */
    public void put(Object key, Object value){
        add(key, value);
    }


  //**************************************************************************
  //** Get
  //**************************************************************************
  /**  Used to retrieve an entry in the array. Returns an array of values
   *   associated with a given key.
   */
    public Value[] get(Object key){
        java.util.List list = (java.util.List) entries.get(key);
        Value[] values = new Value[list.size()];
        for (int i=0; i<values.length; i++){
            values[i] = new Value(list.get(i));
        }
        return values;
    }


  //**************************************************************************
  //** Remove
  //**************************************************************************
  /** Used to remove an entry from the array. */

    public void remove(Object key){
        entries.remove(key);
    }

}