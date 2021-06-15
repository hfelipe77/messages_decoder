package co.test.meli.decoder.dto;

public class MessageDecoderRequestSplitDTO {

	double distance;
	String[] message;
	
	
	public double getDistance() {
		return distance;
	}
	public void setDistance(double distance) {
		this.distance = distance;
	}
	public String[] getMessage() {
		return message;
	}
	public void setMessage(String[] message) {
		this.message = message;
	}
}
