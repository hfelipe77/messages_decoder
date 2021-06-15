package co.test.meli.decoder.service;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.math3.fitting.leastsquares.LevenbergMarquardtOptimizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.lemmingapex.trilateration.NonLinearLeastSquaresSolver;
import com.lemmingapex.trilateration.TrilaterationFunction;

import co.test.meli.decoder.entities.Position;
import co.test.meli.decoder.entities.Satelite;
import co.test.meli.decoder.exception.DecoderException;
import co.test.meli.decoder.utils.Constants;

@Service
public class LocationService {

	@Autowired
	private Environment env;

	/**
	 * Metodo que procesa la informacion recibida por los satelites
	 * @param satelites
	 * @return Posicion (x,y) del emisor de los mensajes
	 * @throws DecoderException
	 */
	public Position processLocation(List<Satelite> satelites) throws DecoderException {
		
		if(satelites.size() >= Constants.MINIMUM_POSITIONS_AND_DISTANCES) {
			double[] distances = getSatelitesDistances(satelites);
			double[][] satelitePositions = getSatelitePositions(satelites);
			
			return getLocation(distances,satelitePositions);
		}else {
			throw new DecoderException(env.getProperty(Constants.KEYWORD_INFORMATION_EXCEPTION));
		}
	}

	/**
	 * Metodo que obtiene las distancias a los satelites
	 * @param satelites
	 * @return
	 * @throws DecoderException
	 */
	private double[] getSatelitesDistances(List<Satelite> satelites) throws DecoderException {
		
		try {
			double[] distances = new double [satelites.size()];
			
			for (int i = 0; i < satelites.size(); i++) {
				distances[i] = satelites.get(i).getDistance();
			}
			return distances;
		} catch (Exception e) {
			throw new DecoderException(env.getProperty(Constants.KEYWORD_POSITION_EXCEPTION));
		}
	}
	
	/**
	 * Metodo que obtiene las posiciones (x,y) de los satelites
	 * @param satelites
	 * @return 
	 * @throws DecoderException
	 */
	private double[][] getSatelitePositions(List<Satelite> satelites) throws DecoderException{
		
		try {
			
			String[] satelitePosition;
			double[][] satelitePositions = new double[satelites.size()][Integer.parseInt(env.getProperty(Constants.KEYWORD_DIMENSIONS))];
					
			for (int i = 0; i < satelites.size(); i++) {
				satelitePosition = env.getProperty(satelites.get(i).getName()+Constants.KEYWORD_POSITION)
						.split(Constants.SEPARATOR);
				
				satelitePositions[i] = Arrays.stream(satelitePosition).map(Double::valueOf)
	                    .mapToDouble(Double::doubleValue)
	                    .toArray();
			}
			return satelitePositions;
		} catch (Exception e) {
			throw new DecoderException(env.getProperty(Constants.KEYWORD_POSITION_EXCEPTION));
		}
	}
	
	/**
	 * Metodo que obtiene una posicion segun las distancias y posiciones de los satelites receptores
	 * @param distances
	 * @param satelitePositions
	 * @return
	 * @throws DecoderException
	 */
	private Position getLocation(double[] distances, double[][] satelitePositions ) throws DecoderException {
		
		try {
			Position position = new Position();
			TrilaterationFunction trilaterationFunction = new TrilaterationFunction(satelitePositions, distances);
			NonLinearLeastSquaresSolver nSolver = new NonLinearLeastSquaresSolver(trilaterationFunction, new LevenbergMarquardtOptimizer());

			double[] pos = nSolver.solve().getPoint().toArray();
			
			position.setX(pos[0]);
			position.setY(pos[1]);
			return position;
			
		} catch (Exception e) {
			throw new DecoderException(env.getProperty(Constants.KEYWORD_POSITION_EXCEPTION));
		}
	}

}
