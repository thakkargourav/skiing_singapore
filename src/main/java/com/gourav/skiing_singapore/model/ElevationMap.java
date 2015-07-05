package com.gourav.skiing_singapore.model;

import static com.google.common.base.Strings.isNullOrEmpty;

import com.google.common.base.MoreObjects;
import com.google.common.base.Splitter;
import com.google.common.base.Stopwatch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * ElevationMap is class the stores a 2D-Array of Elevations. It calculates and finds the steepest path with max
 * distance from each elevation point.
 *
 * @author Gourav Thakkar (gourav@evivehealth.com)
 */
public class ElevationMap {
    private static final Logger LOGGER = LoggerFactory.getLogger(ElevationMap.class);
    private static final Splitter SPLITTER = Splitter.onPattern("\\s").omitEmptyStrings().trimResults();

    private final Elevation[][] elevations;
    private final int rowSize;
    private final int columnSize;

    public ElevationMap(Elevation[][] elevations, int rowSize, int columnSize) {
        this.elevations = elevations;
        this.rowSize = rowSize;
        this.columnSize = columnSize;
        initializeAdjSteepNodes();
        calculateMaxDistanceOfAllPoints();
    }

    /**
     * A method that creates the Elevation map by reading the values from a file.<br>
     * An digital form the map looks like the number grid below:
     *
     * <pre>
     * 4 4
     * 4 8 7 3
     * 2 5 9 3
     * 6 3 2 5
     * 4 4 1 6
     * </pre>
     *
     * The first line (4 4) indicates that this is a 4x4 map
     *
     * @param filePath Path for the file
     * @return ElevationMap created from the file
     * @exception IllegalArgumentException If the first line does not contain exactly two values or number of rows and
     *            columns does not match the values specified in first line.
     * @exception NumberFormatException If values contain any other character other than 0-9
     */
    public static ElevationMap fromFile(Path filePath) {
        try (BufferedReader br = Files.newBufferedReader(filePath)) {
            final String line = br.readLine();
            if (isNullOrEmpty(line)) {
                throw new IllegalArgumentException("Empty file.");
            }
            final List<String> mapSize = SPLITTER.splitToList(line);
            if (mapSize.size() != 2) {
                throw new IllegalArgumentException(
                        "Invalid first line. First line should contain only 2 values i.e size of the map");
            } else {
                final int rows = Integer.parseInt(mapSize.get(0));
                final int columns = Integer.parseInt(mapSize.get(1));
                final Elevation[][] elevationPoints = br.lines().map(SPLITTER::splitToList).map(row -> {
                    if (row.size() != columns) {
                        throw new IllegalArgumentException("No of columns doesn't match the given size");
                    }
                    return row.stream().map(Integer::parseInt).map(point -> new Elevation(point))
                            .toArray(size -> new Elevation[size]);
                }).toArray(Elevation[][]::new);
                if (elevationPoints.length != rows) {
                    throw new IllegalArgumentException("No of rows doesn't match the given size");
                }
                return new ElevationMap(elevationPoints, rows, columns);
            }
        } catch (IOException e) {
            LOGGER.error("Error occurred in while reading values from file: {}", e.getMessage(), e);
        }
        return null;
    }

    public Elevation[][] getElevations() {
        return elevations;
    }

    public int getRowSize() {
        return rowSize;
    }

    public int getColumnSize() {
        return columnSize;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).add("elevations", Arrays.deepToString(elevations))
                .add("rowSize", rowSize).add("columnSize", columnSize).toString();
    }

    /**
     * This method initialises the adjacent steep nodes for each elevation in the map.
     *
     */
    private void initializeAdjSteepNodes() {
        IntStream.range(0, rowSize).forEach(i -> IntStream.range(0, columnSize).forEach(j -> {
            final List<Elevation> adjSteepNodes = elevations[i][j].getAdjSteepNodes();
            final int value = elevations[i][j].getValue();
            if (validateBoundary(i, j - 1) && elevations[i][j - 1].getValue() < value)
                adjSteepNodes.add(elevations[i][j - 1]);
            if (validateBoundary(i, j + 1) && elevations[i][j + 1].getValue() < value)
                adjSteepNodes.add(elevations[i][j + 1]);
            if (validateBoundary(i - 1, j) && elevations[i - 1][j].getValue() < value)
                adjSteepNodes.add(elevations[i - 1][j]);
            if (validateBoundary(i + 1, j) && elevations[i + 1][j].getValue() < value)
                adjSteepNodes.add(elevations[i + 1][j]);
        }));
    }

    /**
     * Validates while checking the adjacent steep elevation points. Avoids array index out of bounds.
     *
     * @param i position in x-axis
     * @param j position in y-axis
     * @return true if the point lies with in the map
     */
    private boolean validateBoundary(int i, int j) {
        if (i >= 0 && j >= 0 && i < rowSize && j < columnSize) {
            return true;
        }
        return false;
    }

    /**
     * Method to calculate max distance of all the elevation points, which in turn calls the recursive distance of each
     * point.
     */
    private void calculateMaxDistanceOfAllPoints() {
        for (int i = 0; i < rowSize; i++) {
            for (int j = 0; j < columnSize; j++) {
                elevations[i][j].calculateDistance();
            }
        }
    }

    /**
     * Get highest elevation(steepest and longest path) in the entire map. In case of a match in max distance, check
     * which fall is higher.
     *
     */
    public Elevation highestElevation() {
        final Optional<Elevation> highestPoint =
                Stream.of(elevations).flatMap(row -> Stream.of(row)).sorted().findFirst();
        return highestPoint.orElse(null);
    }


    public static void main(String[] args) {
        if (args.length != 1) {
            LOGGER.error(
                    "Wrong Arguements. Please start the program using following command:\njava -jar <path_to_jar> <path_to_file>");
            return;
        }
        final Stopwatch stopwatch = Stopwatch.createStarted();
        try {
            ElevationMap elevationMap = ElevationMap.fromFile(Paths.get(args[0]));
            final Elevation highestElevation = elevationMap.highestElevation();
            stopwatch.stop();
            LOGGER.info("Steepest and longest Path: {}", highestElevation.getPath());
            LOGGER.info("Max Distance: {}", highestElevation.getMaxDistance());
            LOGGER.info("Max Drop: {}", highestElevation.getMaxDrop());
            LOGGER.info("Total execution time: {} Milliseconds", stopwatch.elapsed(TimeUnit.MILLISECONDS));
        } catch (Exception e) {
            LOGGER.error("Error occurrend in main:" + e.getMessage());
        }

    }


}
