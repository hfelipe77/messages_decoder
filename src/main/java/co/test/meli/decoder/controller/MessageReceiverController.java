package co.test.meli.decoder.controller;

import org.apache.commons.math3.fitting.leastsquares.LevenbergMarquardtOptimizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lemmingapex.trilateration.NonLinearLeastSquaresSolver;
import com.lemmingapex.trilateration.TrilaterationFunction;

import co.test.meli.decoder.dto.MessageDecoderRequestDTO;
import co.test.meli.decoder.dto.MessageDecoderResponseDTO;
import co.test.meli.decoder.entities.Position;
import co.test.meli.decoder.service.LocationService;
import co.test.meli.decoder.service.MessageDecoderService;

@RestController
@RequestMapping(path = "${context.path}")
public class MessageReceiverController {
	
	@Autowired
	LocationService service;
	@Autowired
	MessageDecoderService msg;
	
	@PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<Object> topsecret(RequestEntity<MessageDecoderRequestDTO>  request) {
		
		MessageDecoderRequestDTO req = (MessageDecoderRequestDTO)request.getBody();
		
		double[][] pointsList = {{-500,-200},{100,-100}, {500,100}};
		double[] distances = {100.0,115.5,142.7};
		
		TrilaterationFunction trilaterationFunction = new TrilaterationFunction(pointsList, distances);
		NonLinearLeastSquaresSolver nSolver = new NonLinearLeastSquaresSolver(trilaterationFunction, new LevenbergMarquardtOptimizer());

		double[] pos = nSolver.solve().getPoint().toArray();
		MessageDecoderResponseDTO res = new MessageDecoderResponseDTO();
		Position position = new Position();
		position.setX(pos[0]);
		position.setY(pos[1]);
		res.setPosition(position);
		res.setMessage("mensaje de prueba");
		
		return ResponseEntity.status(HttpStatus.OK).body(res);
	}

}
