package javaxt.utils;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.SimpleJavaFileObject;
import javax.tools.ToolProvider;
import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject.Kind;
import javax.tools.ForwardingJavaFileManager;
import javax.tools.StandardJavaFileManager;
import javax.tools.FileObject;


//******************************************************************************
//**  Compiler Class
//******************************************************************************
/**
 *   Dynamic, in-memory, Java compiler. Sample usage:
 <pre>
        StringBuilder src = new StringBuilder();
        src.append("public class MyClass {\n");
        src.append("    public String toString() {\n");
        src.append("        return \"Hello, Peter I am \" + ");
        src.append("this.getClass().getSimpleName();\n");
        src.append("    }\n");
        src.append("}\n");        

        Class _class = Compiler.compile("MyClass", src.toString());
        Object instance = _class.newInstance();
        System.out.println(instance);
 </pre>  
 * 
 *   Original source code can be found here: <br/>
 *   http://www.javablogging.com/dynamic-in-memory-compilation/
 *
 ******************************************************************************/


public class Compiler {
    
    
  //**************************************************************************
  //** compile
  //**************************************************************************
  /** Used to compile a block of code. 
   *  @param className Full name of the class that will be compiled. If class 
   *  should be in some package, className should contain it too 
   *  (ex. "com.example.MyClass")
   *  @param src Source code.
   */ 
    public static Class compile(String className, String src) throws Exception {

      //Get an instance of a JavaCompiler. This requires access to the JDK.
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        
      //Then we create a custom file manager
        JavaFileManager fileManager = new ClassFileManager(
            compiler.getStandardFileManager(null, null, null));

      //Dynamic compiling requires specifying a list of "files" to compile. In 
      //our case this is a list containing one "file" which is in our case our
      //own implementation (see details below)
        java.util.List<JavaFileObject> jfiles = new java.util.ArrayList<JavaFileObject>();
        jfiles.add(new CharSequenceJavaFileObject(className, src));

      //Specify a task for the compiler. Compiler should use our file manager  
      //and our list of "files". Then we run the compilation with call()
        compiler.getTask(null, fileManager, null, null, null, jfiles).call();

      //Load the class and return
        return fileManager.getClassLoader(null).loadClass(className);        
    }

    

    private static class CharSequenceJavaFileObject extends SimpleJavaFileObject {

        /**
        * CharSequence representing the source code to be compiled
        */
        private CharSequence content;

        /**
        * This constructor will store the source code in the
        * internal "content" variable and register it as a
        * source code, using a URI containing the class full name
        *
        * @param className
        *            name of the public class in the source code
        * @param content
        *            source code to compile
        */
        public CharSequenceJavaFileObject(String className,
            CharSequence content) {
            super(java.net.URI.create("string:///" + className.replace('.', '/')
                + Kind.SOURCE.extension), Kind.SOURCE);
            this.content = content;
        }

        /**
        * Answers the CharSequence to be compiled. It will give
        * the source code stored in variable "content"
        */
        @Override
        public CharSequence getCharContent(
            boolean ignoreEncodingErrors) {
            return content;
        }
    }
    
    private static class JavaClassObject extends SimpleJavaFileObject {

        /**
        * Byte code created by the compiler will be stored in this
        * ByteArrayOutputStream so that we can later get the
        * byte array out of it
        * and put it in the memory as an instance of our class.
        */
        protected final java.io.ByteArrayOutputStream bos =
            new java.io.ByteArrayOutputStream();

        /**
        * Registers the compiled class object under URI
        * containing the class full name
        *
        * @param name
        *            Full name of the compiled class
        * @param kind
        *            Kind of the data. It will be CLASS in our case
        */
        public JavaClassObject(String name, Kind kind) {
            super(java.net.URI.create("string:///" + name.replace('.', '/')
                + kind.extension), kind);
        }

        /**
        * Will be used by our file manager to get the byte code that
        * can be put into memory to instantiate our class
        *
        * @return compiled byte code
        */
        public byte[] getBytes() {
            return bos.toByteArray();
        }

        /**
        * Will provide the compiler with an output stream that leads
        * to our byte array. This way the compiler will write everything
        * into the byte array that we will instantiate later
        */
        @Override
        public java.io.OutputStream openOutputStream() throws java.io.IOException {
            return bos;
        }
    }



    private static class ClassFileManager extends ForwardingJavaFileManager {
        /**
        * Instance of JavaClassObject that will store the
        * compiled bytecode of our class
        */
        private JavaClassObject jclassObject;

        /**
        * Will initialize the manager with the specified
        * standard java file manager
        *
        * @param standardManger
        */
        public ClassFileManager(StandardJavaFileManager
            standardManager) {
            super(standardManager);
        }

        /**
        * Will be used by us to get the class loader for our
        * compiled class. It creates an anonymous class
        * extending the SecureClassLoader which uses the
        * byte code created by the compiler and stored in
        * the JavaClassObject, and returns the Class for it
        */
        @Override
        public ClassLoader getClassLoader(Location location) {
            return new java.security.SecureClassLoader() {
                //@Override
                protected Class<?> findClass(String name)
                    throws ClassNotFoundException {
                    byte[] b = jclassObject.getBytes();
                    return super.defineClass(name, jclassObject
                        .getBytes(), 0, b.length);
                }
            };
        }

        /**
        * Gives the compiler an instance of the JavaClassObject
        * so that the compiler can write the byte code into it.
        */
        @Override
        public JavaFileObject getJavaFileForOutput(Location location,
            String className, Kind kind, FileObject sibling)
                throws java.io.IOException {
                jclassObject = new JavaClassObject(className, kind);
            return jclassObject;
        }
    }
}