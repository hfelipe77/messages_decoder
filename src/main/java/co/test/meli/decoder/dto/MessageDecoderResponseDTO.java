package co.test.meli.decoder.dto;

import co.test.meli.decoder.entities.Position;

public class MessageDecoderResponseDTO {

	public Position position;
	public String message;
	
	public Position getPosition() {
		return position;
	}
	public void setPosition(Position position) {
		this.position = position;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
}
