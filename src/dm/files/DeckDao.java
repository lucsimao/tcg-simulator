/** 
* @author Simão 
* @version 0.1 - 30 de abr de 2017
* Classe criada para salvar os objetos deck em um arquivo
*/
package dm.files;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import dm.constants.FilesConstants;
import dm.fields.elements.decks.ExtraDeck;
import dm.fields.elements.decks.NormalDeck;
import dm.game.Player;

public class DeckDao {

	private FileOutputStream fileOutputStream;
	private ObjectOutputStream objectOutputStream;
	private FileInputStream fileInputStream;
	private ObjectInputStream objectInputStream;

	/**
	 * Salva o deck de um jogador em um arquivo padrão
	 * 
	 * @param player
	 *            o jogador dono do deck
	 */
	public void saveDeck(Player player) throws FileNotFoundException, IOException, NullPointerException {
		if (player == null) {
			throw new NullPointerException();
		}
		NormalDeck normalDeck = player.getDeck();
		ExtraDeck extraDeck = player.getExtraDeck();

		saveNormalDeck(normalDeck, player);
		saveExtraDeck(extraDeck, player);

	}

	public void saveDeck(NormalDeck deck,String deckName) throws FileNotFoundException, IOException {
		
		fileOutputStream = new FileOutputStream(getFile(deckName));
		objectOutputStream = new ObjectOutputStream(fileOutputStream);
		System.out.println("INFO - Salvando Deck Tamanho: " + deck.size());
		objectOutputStream.writeObject(deck);
		objectOutputStream.flush();
		fileOutputStream.close();
	}
	
	private File getFile(Player player, String name) throws IOException {
		String filename = FilesConstants.DECK + "/" + player.getName() + "/" + name + FilesConstants.EXTENSION;
		File file = new File(filename);
		file.getParentFile().mkdirs();
		file.createNewFile();
		return file;
	}

	private File getFile(String name) throws IOException {
		String filename = FilesConstants.DECK + "/" + name + FilesConstants.EXTENSION;
		File file = new File(filename);
		file.getParentFile().mkdirs();
		file.createNewFile();
		return file;
	}

	
	private void saveNormalDeck(NormalDeck normalDeck, Player player) throws FileNotFoundException, IOException {

		fileOutputStream = new FileOutputStream(getFile(player, "normaldeck"));
		objectOutputStream = new ObjectOutputStream(fileOutputStream);

		objectOutputStream.writeObject(normalDeck);
		objectOutputStream.flush();
		fileOutputStream.close();
	}

	private void saveExtraDeck(ExtraDeck extraDeck, Player player) throws FileNotFoundException, IOException {
		fileOutputStream = new FileOutputStream(getFile(player, "extradeck"));
		objectOutputStream = new ObjectOutputStream(fileOutputStream);

		objectOutputStream.writeObject(extraDeck);
		objectOutputStream.flush();
		fileOutputStream.close();
	}

	/**
	 * Lê o normaldeck de um jogador
	 * 
	 * @param player
	 *            o jogador dono do deck
	 * @return o deck do jogador
	 */
	public NormalDeck readNormalDeck(Player player) throws FileNotFoundException, IOException, ClassNotFoundException {
		NormalDeck normalDeck;

		fileInputStream = new FileInputStream(getFile(player, "normaldeck"));
		objectInputStream = new ObjectInputStream(fileInputStream);
		normalDeck = (NormalDeck) objectInputStream.readObject();
		objectInputStream.close();
		fileInputStream.close();

		return normalDeck;
	}

	/**
	 * Lê o extradeck de um jogador
	 * 
	 * @param player
	 *            o jogador dono do deck
	 * @return o deck do jogador
	 */
	public ExtraDeck readExtraDeck(Player player) throws FileNotFoundException, IOException, ClassNotFoundException {
		ExtraDeck extraDeck;

		fileInputStream = new FileInputStream(getFile(player, "extradeck"));
		objectInputStream = new ObjectInputStream(fileInputStream);
		extraDeck = (ExtraDeck) objectInputStream.readObject();
		objectInputStream.close();
		fileInputStream.close();

		return extraDeck;
	}

	public NormalDeck loadDeck(File deckFile) throws FileNotFoundException, IOException, ClassNotFoundException {
		NormalDeck normalDeck;
		fileInputStream = new FileInputStream(deckFile);
		objectInputStream = new ObjectInputStream(fileInputStream);
		normalDeck = (NormalDeck) objectInputStream.readObject();
		objectInputStream.close();
		fileInputStream.close();
		
		System.out.println("INFO - Deck carregado: " + normalDeck.size());
		
		
		return normalDeck;
	}



}
