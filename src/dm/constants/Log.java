/** 
* @author Simão 
* @version 0.1 - 6 de jul de 2017
* 
*/
package dm.constants;

public class Log {

	public static void messageLog(String TAG, String message){
		System.out.println(TAG + " -- " + message);
	}
	public static void errorLog(String TAG, String message){
		System.err.println(TAG + " -- " + message);
	}
}
