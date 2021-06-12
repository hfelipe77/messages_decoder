package co.test.meli.decoder.dto;

import java.util.List;

import co.test.meli.decoder.entities.Satelite;

public class MessageDecoderRequestDTO {

	List<Satelite> satellites;

	public List<Satelite> getSatellites() {
		return satellites;
	}

	public void setSatellites(List<Satelite> satellites) {
		this.satellites = satellites;
	}
	
	
}
