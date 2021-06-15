package co.test.meli.decoder.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import co.test.meli.decoder.dto.MessageDecoderRequestDTO;
import co.test.meli.decoder.dto.MessageDecoderRequestSplitDTO;
import co.test.meli.decoder.dto.MessageDecoderResponseDTO;
import co.test.meli.decoder.entities.Satelite;
import co.test.meli.decoder.exception.DecoderException;
import co.test.meli.decoder.service.LocationService;
import co.test.meli.decoder.service.MessageDecoderService;
import co.test.meli.decoder.utils.Constants;

@RestController
@RequestMapping(path = "${context.path}")
public class MessageReceiverController {

	@Autowired
	LocationService location;
	@Autowired
	MessageDecoderService msg;
	@Autowired
	private Environment env;

	@PostMapping(path= "/topsecret",consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<Object> topsecret(RequestEntity<MessageDecoderRequestDTO> request) {

		try {
			MessageDecoderRequestDTO req = (MessageDecoderRequestDTO)request.getBody();
			MessageDecoderResponseDTO res = new MessageDecoderResponseDTO();

			res.setPosition(location.processLocation(req.getSatellites()));
			res.setMessage(msg.processMessages(req.getSatellites()));

			return ResponseEntity.status(HttpStatus.OK).body(res);
		} catch (DecoderException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		}
	}

	@RequestMapping(path= "/topsecret_split/{satellite_name}", method = {RequestMethod.POST,RequestMethod.GET},
			consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<Object> topsecret_splitByPost(@PathVariable("satellite_name") String name,
			RequestEntity<MessageDecoderRequestSplitDTO> request, HttpServletRequest httpRequest) {

		try {
			MessageDecoderRequestSplitDTO req = (MessageDecoderRequestSplitDTO) request.getBody();
			MessageDecoderResponseDTO res = new MessageDecoderResponseDTO();
			clean(httpRequest);
			
			List<Satelite> satelites = manageSatelitesInSession(name, req, httpRequest);
			
			res.setPosition(location.processLocation(satelites));
			res.setMessage(msg.processMessages(satelites));
			return ResponseEntity.status(HttpStatus.OK).body(res);

		} catch (DecoderException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		}
	}
	

	/**
	 * Metodo que limpia la sesion una vez se completa la informacion de los satelites
	 * @param httpRequest
	 */
	private void clean(HttpServletRequest httpRequest) {
		
		@SuppressWarnings("unchecked")
		List<Satelite> satelites = (List<Satelite>) httpRequest.getSession().getAttribute(Constants.KEYWORD_SATELITE_LIST);
		int satelitesCount = env.getProperty(Constants.KEYWORD_SATELITES_NAME).split(Constants.SEPARATOR).length;
		
		if(null != satelites && 
				satelites.size() == satelitesCount) {
			httpRequest.getSession().invalidate();
		}
	}

	/**
	 * Metodo que administra el listado de satelites almacenado en session
	 * @param name
	 * @param reqDTO
	 * @param httpRequest
	 * @return
	 */
	private List<Satelite> manageSatelitesInSession(String name,MessageDecoderRequestSplitDTO reqDTO, HttpServletRequest httpRequest) {
		
		@SuppressWarnings("unchecked")
		List<Satelite> satelites = (List<Satelite>) httpRequest.getSession().getAttribute(Constants.KEYWORD_SATELITE_LIST);
		boolean exist = Boolean.FALSE;

		if(null == satelites || satelites.isEmpty()) {
			satelites = new ArrayList<Satelite>();
		}
		
		if(satelites.size() == 0) {
			satelites.add(new Satelite(name,reqDTO.getDistance(),reqDTO.getMessage()));
		} else {
			for(Satelite satelite : satelites) {
				if(satelite.getName().equals(name)) {
					exist = Boolean.TRUE;
				}
			}
			if(!exist) {
				satelites.add(new Satelite(name,reqDTO.getDistance(),reqDTO.getMessage()));
			}
		}
		httpRequest.getSession().setAttribute(Constants.KEYWORD_SATELITE_LIST, satelites);
		return satelites;
	}
	
}
