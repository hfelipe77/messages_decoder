package co.test.meli.decoder.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import co.test.meli.decoder.entities.Satelite;
import co.test.meli.decoder.exception.DecoderException;
import co.test.meli.decoder.utils.Constants;

@Service
public class MessageDecoderService {

	@Autowired
	private Environment env;

	/**
	 * Metodo que procesa los mensajes recibidos por los satelites
	 * @param satelites
	 * @return
	 * @throws DecoderException
	 */
	public String processMessages(List<Satelite> satelites) throws DecoderException {

		List<List<String>> messagesReceived = new ArrayList<List<String>>();

		for(Satelite satelite : satelites) {

			List<String> list = new ArrayList<String>();

			for (int i = 0; i < satelite.getMessage().length; i++) {
				list.add(satelite.getMessage()[i]);
			}
			messagesReceived.add(list);
		}

		int words = countWords(messagesReceived);
		determineGap(messagesReceived, words);

		return getMessage(messagesReceived);
	}

	/**
	 * Metodo que cuenta la totalidad de palabras en los mensajes recibidos
	 * por los satelites
	 * @param messages
	 * @return cantidad de palabras
	 * @throws DecoderException
	 */
	private int countWords(List<List<String>> messages) throws DecoderException {

		try {
			List<String> words = new ArrayList<String>();

			for(List<String> list : messages) {
				for (int i = 0; i < list.size(); i++) {
					if(!words.contains(list.get(i)) && !Constants.EMPTY_STRING.equals(list.get(i))) {
						words.add(list.get(i));
					}
				}
			}
			return words.size();
		} catch (Exception e) {
			throw new DecoderException(env.getProperty(Constants.KEYWORD_MESSAGE_EXCEPTION));
		}
	}


	/**
	 * Metodo que determina el desfase entre los mensajes recibidos
	 * @param messages
	 * @param countWords
	 * @throws DecoderException
	 */
	private void determineGap(List<List<String>> messages, int countWords) throws DecoderException {

		try {
			for (int i = 0; i < messages.size(); i++) {

				int diff = messages.get(i).size() - countWords;

				if(diff > 0) {
					for (int j = 0; j < diff; j++) {
						messages.get(i).remove(Constants.EMPTY_STRING);
					}
				}
			}
		} catch (Exception e) {
			throw new DecoderException(env.getProperty(Constants.KEYWORD_MESSAGE_EXCEPTION));
		}
	}


	/**
	 * Metodo que obtiene el mensaje final
	 * @param messages
	 * @return
	 * @throws DecoderException
	 */
	private String getMessage(List<List<String>> messages) throws DecoderException {

		try {
			List<String> message = new ArrayList<String>();

			for(int j=0; j < messages.get(0).size(); j++) {

				for (int i = 0; i < messages.size(); i++) {

					if(!Constants.EMPTY_STRING.equals(messages.get(i).get(j))
							&& !message.contains(messages.get(i).get(j) + Constants.BLANK_SPACE)) {
						message.add(messages.get(i).get(j) + Constants.BLANK_SPACE);
					}
				}
			}
			return message.toString();
		} catch (Exception e) {
			throw new DecoderException(env.getProperty(Constants.KEYWORD_MESSAGE_EXCEPTION));
		}
	}


}
