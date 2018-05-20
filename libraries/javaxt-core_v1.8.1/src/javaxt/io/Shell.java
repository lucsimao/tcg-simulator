package javaxt.io;
import java.io.*;

//******************************************************************************
//**  Shell Class
//******************************************************************************
/**
 *   Used to execute command line applications and return the corresponding
 *   output streams (standard output and error output streams). 
 *
 ******************************************************************************/

public class Shell {

    private java.io.File executable;
    private String[] inputs;
    private java.util.List<String> output = new java.util.LinkedList<String>();
    private java.util.List<String> errors = new java.util.LinkedList<String>();
    private long startTime;
    private long ellapsedTime;
    private Process process;

  //**************************************************************************
  //** Constructor
  //**************************************************************************
  /** Creates a new instance of Shell.
   *  @param executable Path to the executable to run
   *  @param parameters Command line args passed to the executable
   */
    public Shell(java.io.File executable, String[] parameters) {

        this.executable = executable;
        this.ellapsedTime = -1;

        if (parameters==null){
            parameters = new String[0];
        }

        inputs = new String[parameters.length+1];
        inputs[0] = executable.toString();
        for (int i=0; i<parameters.length; i++){
            inputs[i+1] = parameters[i];
        }
    }

  //**************************************************************************
  //** Constructor
  //**************************************************************************
  /** Creates a new instance of Shell.
   *  @param executable Path to the executable to run
   *  @param parameters Command line args passed to the executable
   */
    public Shell(javaxt.io.File executable, String[] parameters) {
        this(executable.toFile(), parameters);
    }

  //**************************************************************************
  //** Constructor
  //**************************************************************************
  /** Creates a new instance of Shell.
   *  @param cmd Command to execute. Example: "cmd /c dir C:\\temp"
   */
    public Shell(String cmd){

        this.executable = null;
        this.ellapsedTime = -1;

        String arr[] = cmd.trim().split(" ");
        java.util.Vector<String> parameters = new java.util.Vector<String>();
        for (int i=0; i<arr.length; i++){
            String str = arr[i].trim();
            if (str.length()>0) parameters.add(str);
        }
        inputs = new String[parameters.size()];
        for (int i=0; i<parameters.size(); i++){
            inputs[i] = parameters.get(i);
        }
        java.io.File file = new java.io.File(inputs[0]);
        if (file.exists() && file.isFile()){
            executable = file;
        }
    }

  //**************************************************************************
  //** Constructor
  //**************************************************************************
  /** Creates a new instance of Shell.
   *  @param cmdarray Command to execute, along with the command line args in a
   *  String array.
   */
    public Shell(String[] cmdarray){
        this.executable = null;
        this.ellapsedTime = -1;
        inputs = cmdarray;
        java.io.File file = new java.io.File(inputs[0]);
        if (file.exists() && file.isFile()){
            executable = file;
        }
    }


  //**************************************************************************
  //** getOutput
  //**************************************************************************
  /** Used to retrieve the standard output stream. Returns a List that can be
   *  parsed while the executable is still running or after is has been run.
   *  The difference lies in when the run method is invoked. The following is 
   *  an example of how to process the output stream while the app is running.
   *  The getOutput() is called BEFORE the run() method is invoked.
   <pre>
        File exe = new File("C:\\Program Files\\PostgreSQL\\8.4\\bin\\shp2pgsql.exe");
        String[] options = new String[]{"-W", "UTF-8", "-s", "4326", "C:\country.shp", "t_country"};

        javaxt.io.Shell cmd = new javaxt.io.Shell(exe, options);
        java.util.List&lt;String&gt; output = cmd.getOutput();
        cmd.run();

        String line;
        while (true){
            synchronized (output) {
                while (output.isEmpty()) {
                  try {
                      output.wait();
                  }
                  catch (InterruptedException e) {
                  }
                }
                line = output.remove(0);
            }

            if (line!=null){
                System.out.println(line);
            }
            else{
                break;
            }
        }
   </pre>
   * If you want to get the entire output all at once, just call the getOutput()
   * AFTER the run() method. Example:
   <pre>
        javaxt.io.Shell cmd = new javaxt.io.Shell(exe, options);
        cmd.run();
        java.util.List<String> output = cmd.getOutput();
        for (int i=0; i<output.size(); i++){
            System.out.println(output.get(i));
        }
   </pre>
   *
   */
    public java.util.List getOutput(){ return output; }


  //**************************************************************************
  //** getErrors
  //**************************************************************************
  /** Used to retrieve the error output stream. Returns a List that can be
   *  parsed while the executable is still running or after is has been run.
   */
    public java.util.List getErrors(){ return errors; }



  //**************************************************************************
  //** run
  //**************************************************************************
  /** Used to execute the process specified in the constructor and populate
   *  the output streams. This is an overloaded method equivalent to calling
   *  run(false); 
   */
    public void run(){
        try{
            run(false);
        }
        catch(Exception e){
        }
    }


  //**************************************************************************
  //** run
  //**************************************************************************
  /** Used to execute the process specified in the constructor and populate
   *  the output streams.
   *
   *  @param throwExceptions If true, throws out any exceptions that may have
   *  been thrown while executing the process.
   */
    public void run(boolean throwExceptions) throws IOException, InterruptedException {

        ellapsedTime = -1;
        startTime = new java.util.Date().getTime();

        try{

          //Run executable via Command Line and pick up the output stream
            Runtime runtime = Runtime.getRuntime();            
            if (executable!=null){
                process = runtime.exec(inputs, null, executable.getParentFile());
            }
            else{
                process = runtime.exec(inputs);
            }


            StreamReader s1 = new StreamReader(output, process.getInputStream());
            StreamReader s2 = new StreamReader(errors, process.getErrorStream());
            s1.start();
            s2.start();
            process.waitFor();
            s1.join();
            s2.join();


          //Clean up!
            cleanUp();

        }
        catch(IOException e){
            if (throwExceptions) throw e;
            return;
        }
        catch(InterruptedException e){
            if (throwExceptions) throw e;
            return;
        }
        ellapsedTime = new java.util.Date().getTime()-startTime;
    }


  //**************************************************************************
  //** stop
  //**************************************************************************
  /** Used to stop the current process. Note that this method does not stop
   *  or kill process grandchildren. This is a limitation of Java, not this
   *  class per se. See Sun bug 4770092 for more details.
   */
    public void stop(){
        if (process!=null){
            process.destroy();
            cleanUp();
            ellapsedTime = new java.util.Date().getTime()-startTime;
        }
    }


  //**************************************************************************
  //** cleanUp
  //**************************************************************************
  /** Used to clean up system resources after a process has been terminated.
   *  Based on recommendations found in "Five Common java.lang.Process Pitfalls"
   *  by Kyle Cartmell (http://kylecartmell.com/?p=9).
   */
    private void cleanUp(){

      //Explicitly clean up every instance of Process by calling close on each stream
        try{process.getInputStream().close();} catch(Exception ex){}
        try{process.getErrorStream().close();} catch(Exception ex){}
        try{process.getOutputStream().close();} catch(Exception ex){}

      //Explicitly destroy the process even if the process is already terminated
        try{process.destroy();} catch(Exception ex){}

        process = null;
    }


  //**************************************************************************
  //** getEllapsedTime
  //**************************************************************************
  /** Used to return the total time (milliseconds) it took to execute the
   *  process.
   */
    public long getEllapsedTime(){
        return ellapsedTime;
    }


    /*
    public void run(byte[] b){

        try{

          //Run executable via Command Line and pick up the output stream
            Runtime runtime = Runtime.getRuntime();
            Process process = runtime.exec(inputs, null, executable.getParentFile());

            //process.getOutputStream().write(b);

            System.out.println(b.length);

            BufferedOutputStream commandsStdIn = new BufferedOutputStream(process.getOutputStream());
            commandsStdIn.write(b);
            commandsStdIn.flush();
            commandsStdIn.close();



            StreamReader s1 = new StreamReader(output, process.getInputStream());
            StreamReader s2 = new StreamReader(errors, process.getErrorStream());
            s1.start();
            s2.start();
            process.waitFor();

        }
        catch(Exception e){
            e.printStackTrace();
        }

    }
    */

  //**************************************************************************
  //** StreamReader Class
  //**************************************************************************
  /** Thread used to read the standard input and output streams. */

    private class StreamReader implements Runnable {

        java.util.List list;
        InputStream is;
        Thread thread;


        public StreamReader (java.util.List list, InputStream is){
            this.list = list;
            this.is = is;
        }

        public void start () {
            thread = new Thread(this);
            thread.start();
        }

        public void run () {
            try {
                InputStreamReader isr = new InputStreamReader (is);
                BufferedReader br = new BufferedReader (isr);

                while (true) {
                    String s = br.readLine();
                    if (s == null) break;
                    //System.out.println(s);
                    if (list!=null) list.add(s);
                }

                list.add(null);
                is.close();

            }
            catch (Exception ex) {
                //System.out.println ("Problem reading stream... :" + ex);
                ex.printStackTrace ();
            }
        }
        
        public void join() throws InterruptedException {
            thread.join();
        }

    } //End StreamReader Class

}