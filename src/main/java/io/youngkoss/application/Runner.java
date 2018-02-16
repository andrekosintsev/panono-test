package io.youngkoss.application;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import io.youngkoss.application.db.DataSource;
import io.youngkoss.application.enums.EnumOperation;
import io.youngkoss.application.pjo.Operation;
import io.youngkoss.application.pjo.Shape;

/**
 * @author ykoss
 *
 */
@Component
public class Runner {

	/**
	 * 
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(Runner.class.getName());
	@Autowired
	private ResourceLoader resourceLoader;

	private Map<Integer, Integer> mapShapes = new HashMap<Integer, Integer>();

	@Autowired
	@Qualifier("DataSource")
	private DataSource dataSource;

	/**
	 * 
	 */
	@PostConstruct
	public void runOnInit() {

		List<Shape> shapes = new ArrayList<Shape>();
		List<Operation> operations = new ArrayList<Operation>();

		BufferedReader bufferedReader = null;
		try {
			String line = null;
			Resource fileResource = resourceLoader.getResource(Constants.CLASSPATH_SHAPES_DB);
			bufferedReader = new BufferedReader(new InputStreamReader(fileResource.getInputStream()));
			while ((line = bufferedReader.readLine()) != null) {
				String[] splittedValue = line.split("\\|");
				if ((null != splittedValue) && (splittedValue.length != 0)) {
					shapes.add(new Shape(splittedValue[0].trim(), splittedValue[1].trim()));
				}
			}
			bufferedReader.close();
			Shape.initializeShapes(dataSource, shapes);
			printResult();
			mapShapes.clear();
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}

		try {
			String linesInOperations = null;
			Resource fileResource = resourceLoader.getResource(Constants.CLASSPATH_OPERATIONS_DATA);
			bufferedReader = new BufferedReader(new InputStreamReader(fileResource.getInputStream()));
			while ((linesInOperations = bufferedReader.readLine()) != null) {
				String[] splittedValue = linesInOperations.split("\\|");
				if ((null != splittedValue) && (splittedValue.length != 0)) {
					if (splittedValue.length == 3) {
						operations.add(new Operation(splittedValue[0].trim(), splittedValue[1].trim(),
								splittedValue[2].trim()));
					} else {
						operations.add(new Operation(splittedValue[0].trim(), splittedValue[1].trim(),
								Constants.NOTHING_TO_DO_JUST_DELETE));
					}
				}
			}
			bufferedReader.close();
			Operation.initializeOperations(dataSource, operations);
			Map<EnumOperation, List<Shape>> result = Operation.invokeOperation(dataSource);
			updateInDb(result);
			printResult();
			List<Shape> toPrintResult = Shape.selectShapesToPrint(dataSource);
			try (Writer writer = new BufferedWriter(
					new OutputStreamWriter(new FileOutputStream("/Users/ykoss/Desktop/updated.db"), "utf-8"))) {
				for (Shape shape : toPrintResult) {
					writer.write(shape.getUuid() + "|" + shape.getCoordinates() + "\n");
				}
			}
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
	}

	private void updateInDb(Map<EnumOperation, List<Shape>> mapShapes) throws IOException {
		for (EnumOperation keyElement : mapShapes.keySet()) {
			switch (keyElement) {
			case ADD_POINT:
				Shape.updateShapes(dataSource, mapShapes.get(keyElement));
				break;
			case CREATE_SHAPE:
				Shape.initializeShapes(dataSource, mapShapes.get(keyElement));
				break;
			case DELETE_POINT:
				Shape.updateShapes(dataSource, mapShapes.get(keyElement));
				break;
			case DELETE_SHAPE:
				Shape.deleteShapes(dataSource, mapShapes.get(keyElement));
				break;
			default:
				break;

			}
		}
	}

	private void printResult() throws IOException {
		List<Integer> angles = Shape.getAnglesPerFigure(dataSource);
		for (int angleCount : angles) {
			if (mapShapes.get(angleCount) == null) {
				mapShapes.put(angleCount, 1);
			} else {
				int count = mapShapes.get(angleCount);
				count++;
				mapShapes.put(angleCount, count);
			}
		}
		for (Integer keyElement : mapShapes.keySet()) {
			LOGGER.info(describeFigure(keyElement) + " count is :" + mapShapes.get(keyElement));
		}
	}

	private String describeFigure(int key) {
		switch (key) {
		case 3:
			return Constants.TRIANGLES;
		case 4:
			return Constants.RECTANGLES;
		case 5:
			return Constants.PENTAGONS;
		case 6:
			return Constants.HEXAGONS;
		case 7:
			return Constants.HEPTAGONS;
		default:
			return Constants.HEY_STOP_WITH_SUCH_CRAZY_SHAPE;
		}
	}

}
