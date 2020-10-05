package javaxt.webservices;

//******************************************************************************
//**  Option Class
//******************************************************************************
/**
 *   Enter class description here
 *
 ******************************************************************************/

public class Option {

    private String name;
    private String value;

  //**************************************************************************
  //** Constructor
  //**************************************************************************
  /** Creates a new instance of Option. */

    public Option(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName(){return name;}
    public String getValue(){return value;}
}
