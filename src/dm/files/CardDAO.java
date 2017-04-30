/** 
* @author Simão 
* @version 0.1 - 30 de abr de 2017
* 
*/
package dm.files;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import dm.cards.abstracts.Card;
import dm.constants.FilesConstants;


/**
 * @author Simão
 *
 */
public class CardDAO {

	private FileOutputStream fileOutputStream;
	private ObjectOutputStream objectOutputStream;
	private FileInputStream fileInputStream;
	private ObjectInputStream objectInputStream;
		
	public void saveToFile(String file,Card card) throws FileNotFoundException, IOException{
		saveToFile(file,false,card);
	}
	
	public void saveToFile(Card card) throws FileNotFoundException, IOException{
		saveToFile(getFile().getPath(),card);
	}
	
	public void saveToEndFile(Card card) throws FileNotFoundException, IOException{
		saveToEndFile(getFile().getPath(),card);
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
	
	
	
	public Card readFile(String file) throws IOException,FileNotFoundException, ClassNotFoundException{
		Card card;
		fileInputStream = new FileInputStream(file);
		objectInputStream = new ObjectInputStream(fileInputStream);
		card = (Card) objectInputStream.readObject();
		fileInputStream.close();
		return card;
	}
	public List<Card> readAllFile(String file) throws IOException,FileNotFoundException, ClassNotFoundException{
		Card card;
		List<Card> list = new ArrayList<>();
		fileInputStream = new FileInputStream(file);
		while(true){
			try{
			objectInputStream = new ObjectInputStream(fileInputStream);
			card = (Card) objectInputStream.readObject();
			list.add(card);
			}catch (EOFException e) {
				break;
			}
		}
//		objectInputStream.close();
		fileInputStream.close();
		return list ;
	}
	
	
	private File getFile() throws IOException{
		String filename = FilesConstants.CARDS_DB + "/" + 
				 "cards" + FilesConstants.EXTENSION;
		File file = new File(filename);
		file.getParentFile().mkdirs();
		file.createNewFile();
		return  file;
	}
	
}
