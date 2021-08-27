package nearestNeigh;

import javax.swing.plaf.synth.SynthOptionPaneUI;
import java.util.*;

/**
 * This class is required to be implemented.  Naive approach implementation.
 */
public class NaiveNN implements NearestNeigh {
    List<Point> buildPoints = new ArrayList<>();
    Point point = new Point();

    @Override
    public void buildIndex(List<Point> points) {
        for (int row = 0; row < points.size(); row++) {
            buildPoints.add(points.get(row));
        }
    }

    @Override
    public List<Point> search(Point searchTerm, int k) {
        List<Point> buildSearchPoints = new ArrayList<>();
        if (k > 0) {
            List<Point> searchResult = new ArrayList<>();
            List<List<Object>> distanceList = new ArrayList<>();
            int i = 0;
            for (int row = 0; row < buildPoints.size(); row++) {
                List<Object> newList = new ArrayList<>();
                if (buildPoints.get(row).cat == searchTerm.cat) {
                    i++;
                    point.lon = buildPoints.get(row).lon;
                    point.lat = buildPoints.get(row).lat;
                    String ID = buildPoints.get(row).id;
                    Double distance = point.distTo(searchTerm);
                    if (newList.isEmpty()) {
                        newList.add(0, ID);
                        newList.add(0, distance);
                    } else {
                        newList.add(i, ID);
                        newList.add(i, distance);
                    }
                    buildSearchPoints.add(buildPoints.get(row));
                }
                if (!newList.isEmpty()) {
                    distanceList.add(newList);
                }
            }
            if (k < distanceList.size()) {
                for (int j = 0; j < k; j++) {
                    int minIndex = minIndex(distanceList);
                    searchResult.add(buildSearchPoints.get(minIndex));
                    distanceList.remove(minIndex);
                    buildSearchPoints.remove(minIndex);
                }
            } else {
                for (int j = 0; j < buildSearchPoints.size(); j++) {
                    searchResult.add(buildSearchPoints.get(j));
                }
            }
            buildSearchPoints.clear();
            return searchResult;
        } else {
            System.err.println("Value of K should be grater than 1");
        }

        System.out.println();
        // To be implemented.
        return null;
    }

    @Override
    public boolean addPoint(Point point) {
        boolean result = false;
        for (int row = 0; row < buildPoints.size(); row++) {
            if ((point.id.equals(buildPoints.get(row).id)) && (point.cat.equals(buildPoints.get(row).cat))
                    && (point.lat == buildPoints.get(row).lat) && (point.lon == buildPoints.get(row).lon)) {
                break;
            }else{
                if(row == buildPoints.size()-1){
                    buildPoints.add(point);
                    result = true;
                }
            }
        }
        return result;
    }

    @Override
    public boolean deletePoint(Point point) {
        boolean result = false;
        for (int row = 0; row < buildPoints.size(); row++) {
            if (point.id.equals(buildPoints.get(row).id) && point.cat.equals(buildPoints.get(row).cat)
                    && point.lat == buildPoints.get(row).lat && point.lon == buildPoints.get(row).lon) {
                buildPoints.remove(row);
                result = true;
                break;
            }
        }
        return result;

    }

    @Override
    public boolean isPointIn(Point point) {
        boolean result = false;
        for (int row = 0; row < buildPoints.size(); row++) {
            if (point.id.equals(buildPoints.get(row).id) && point.cat.equals(buildPoints.get(row).cat)
                    && point.lat == buildPoints.get(row).lat && point.lon == buildPoints.get(row).lon) {
                buildPoints.remove(row);
                result = true;
                break;
            }
        }
        return result;
    }

    //To find index of minimum value
    public static int minIndex(List<List<Object>> distanceList) {
        Double min = Double.parseDouble(distanceList.get(0).get(0).toString());
        int index = 0;
        for (int row = 1; row < distanceList.size(); row++) {
            Double value = Double.parseDouble(distanceList.get(row).get(0).toString());
            if (value < min) {
                min = value;
                index = row;
            }
        }
        return index;
    }
}
