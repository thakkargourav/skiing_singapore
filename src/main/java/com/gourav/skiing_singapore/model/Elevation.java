package com.gourav.skiing_singapore.model;

import com.google.common.base.MoreObjects;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

/**
 * Elevation represents a point with some particular attributes that help us to find a path in {@link ElevationMap} with
 * highest elevation.
 *
 * @author Gourav Thakkar (gourav@evivehealth.com)
 *
 */
/**
 * @author Gourav Thakkar (gourav@evivehealth.com)
 */
public class Elevation implements Comparable<Elevation> {
    /**
     * Elevation of the point of map.
     */
    private int value;
    /**
     * The max distance that can be covered starting this point. If no other point can be visted from this point, then
     * maxDistance is 1.
     */
    private int maxDistance;
    /**
     * List of adjacent points which have value less than this point.
     */
    private List<Elevation> adjSteepNodes;
    /**
     * The value of ending elevation for the path that can be travelled from this point.
     */
    private int endingElevation;
    /**
     * A flag that can be used to check if we have visited this point.
     */
    private boolean visited;
    /**
     * The next deepest point adjacent to this point.
     */
    private Elevation nextDeepest;

    public Elevation(int value) {
        this.value = value;
        this.endingElevation = value;
        this.maxDistance = 1;
        this.visited = false;
        this.adjSteepNodes = new ArrayList<Elevation>();
        this.nextDeepest = null;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getMaxDistance() {
        return maxDistance;
    }

    public void setMaxDistance(int maxDistance) {
        this.maxDistance = maxDistance;
    }

    public List<Elevation> getAdjSteepNodes() {
        return adjSteepNodes;
    }

    public void setAdjSteepNodes(List<Elevation> adjSteepNodes) {
        this.adjSteepNodes = adjSteepNodes;
    }

    public int getEndingElevation() {
        return endingElevation;
    }

    public void setEndingElevation(int endingElevation) {
        this.endingElevation = endingElevation;
    }

    public boolean isVisited() {
        return visited;
    }

    public void setVisited(boolean visited) {
        this.visited = visited;
    }

    public Elevation getNextDeepest() {
        return nextDeepest;
    }

    public void setNextDeepest(Elevation nextDeepest) {
        this.nextDeepest = nextDeepest;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).add("value", value).add("maxDistance", maxDistance)
                .add("adjSteepNodes", adjSteepNodes).add("endingElevation", endingElevation).add("visited", visited)
                .add("nextDeepest", nextDeepest).toString();
    }

    /*
     * Compares the elevation based on maxDistance, then the max drop(i.e value-endingElevation) in reversed order.
     */
    @Override
    public int compareTo(Elevation that) {
        return Comparator.comparingInt(Elevation::getMaxDistance).thenComparingInt(Elevation::getMaxDrop).reversed()
                .compare(this, that);
    }

    /**
     * Method to recursively search distance of a point and update the maximum distance, next deepest point, and its
     * ending elevation point on its way up.
     *
     * @return the Elevation after the calculations.
     */
    protected Elevation calculateDistance() {
        if (!visited) {
            visited = true;
            Optional<Elevation> maxElevation =
                    adjSteepNodes.stream().map(Elevation::calculateDistance).sorted().findFirst();
            maxElevation.ifPresent(elevation -> {
                maxDistance = elevation.getMaxDistance() + 1;
                nextDeepest = elevation;
                endingElevation = elevation.getEndingElevation();
            });
        }
        return this;
    }

    /**
     *
     * This method returns the path from the starting Elevation of this point
     *
     * @return the path that can be travelled from this point
     */
    public String getPath() {
        Elevation elevation = this;
        StringBuilder path = new StringBuilder();
        while (elevation.getNextDeepest() != null) {
            path.append(elevation.getValue()).append(" -> ");
            elevation = elevation.getNextDeepest();
        }
        path.append(elevation.getValue());
        return path.toString();
    }

    /**
     *
     * This method returns the max drop from this point
     *
     * @return the max drop from this point
     */
    public int getMaxDrop() {
        return value - endingElevation;
    }
}
