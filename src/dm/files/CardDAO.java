/** 
* @author Simão 
* @version 0.1 - 30 de abr de 2017
* 
*/
package dm.files;

import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import dm.cards.abstracts.Card;

public class CardDAO {

	private FileOutputStream fileOutputStream;
	private ObjectOutputStream objectOutputStream;
	private FileInputStream fileInputStream;
	private ObjectInputStream objectInputStream;
		
	public void saveToFile(String file,Card card) throws FileNotFoundException, IOException{
		saveToFile(file,false,card);
	}
	
	public void saveToEndFile(String file,Card card) throws FileNotFoundException, IOException{
		saveToFile(file,true,card);
	}
	
	private void saveToFile(String file,boolean append,Card card) throws IOException, FileNotFoundException {
		fileOutputStream = new FileOutputStream(file,append);
		objectOutputStream = new ObjectOutputStream(fileOutputStream);
		
		objectOutputStream.writeObject(card);
		objectOutputStream.flush();
		fileOutputStream.close();
	}
	
	public Object readFile(String file) throws IOException,FileNotFoundException, ClassNotFoundException{
		Object object;
		fileInputStream = new FileInputStream(file);
		objectInputStream = new ObjectInputStream(fileInputStream);
		object = objectInputStream.readObject();
		fileInputStream.close();
		return object;
	}
	public List<Object> readAllFile(String file) throws IOException,FileNotFoundException, ClassNotFoundException{
		Object object;
		List<Object> list = new ArrayList<>();
		fileInputStream = new FileInputStream(file);
		while(true){
			try{
			objectInputStream = new ObjectInputStream(fileInputStream);
			object = objectInputStream.readObject();
			list.add(object);
			}catch (EOFException e) {
				break;
			}
		}
		objectInputStream.close();
		fileInputStream.close();
		return list ;
	}
	
	
}
