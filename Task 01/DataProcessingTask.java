import java.io.*;
import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;

//Performs data processing tasks
public class DataProcessingTask {

    //This is a main method which is used to do all functions
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        System.out.println("*****Welcome to Data Pre-Processing Program*****");
        System.out.println();
        System.out.println("1. Generate data file and name file");
        System.out.println("2. Normalized data file");
        System.out.println("3. Exit program");
        int sc = in.nextInt();
        String inputFileName = "rawdata.txt";
        String dataFileName = "data.txt";
        String nameFileName = "name.txt";
        String normalizeDataFileName = "normalizeddata.txt";
        String newNameFileName = "newname.txt";
        if (sc == 1) {
			
            ArrayList<ArrayList<Object>> nameArrayList = new ArrayList<>();
            ArrayList<ArrayList<Object>> newNameArrayList = new ArrayList<>();
            createDataFile(dataArrayList, newNameArrayList, inputFileName, dataFileName);
            if (!dataArrayList.isEmpty()) {
                createNameFile(nameArrayList, inputFileName, nameFileName);
            } else {
                System.out.println("Could not create " + nameFileName);
            }
            if (!newNameArrayList.isEmpty()) {
                createNewNameFile(newNameArrayList, newNameFileName);
            } else {
                System.out.println("Could not create " + newNameFileName);
            }
        } else if (sc == 2) {
            ArrayList<ArrayList<Object>> dataArrayList = new ArrayList<>();
            ArrayList<ArrayList<Object>> nameArrayList = new ArrayList<>();
            normalizeData(dataArrayList, nameArrayList, dataFileName, nameFileName, normalizeDataFileName);
        } else if (sc == 3) {
            System.out.println("Exit");
            System.exit(0);
        } else {
            System.out.println("Given number is not correct to process");
        }
    }

    //Performs file read operation
    public static void fileRead(ArrayList<ArrayList<Object>> dataList, String inputFilename, String delimiter) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(inputFilename));
            File file = new File(inputFilename);
            if (!file.exists()) {
                System.out.println(inputFilename + " does not exit");
            } else if (file.length() <= 0) {
                System.out.println(inputFilename + " is empty");
            } else {
                readLine(reader, dataList, delimiter);
            }
        } catch (IOException e) {
            System.out.println("Error has been occurred while reading file " + e.getMessage());
        } finally {
            closeBufferedReader(reader);
        }
    }

    //Reads a file line by line
    public static void readLine(BufferedReader reader, ArrayList<ArrayList<Object>> dataList, String delimiter)
            throws IOException {
        String line = reader.readLine();
        while (line != null) {
            ArrayList<Object> element = new ArrayList<>();
            if (!line.contains("?")) {
                Object[] linesData = line.trim().split(delimiter);
                for (int row = 0; row < linesData.length; row++) {
                    element.add(linesData[row]);
                }
                dataList.add(element);
                line = reader.readLine();
            } else {
                line = reader.readLine();
            }
        }
    }

    //Closes Buffered Reader
    public static void closeBufferedReader(BufferedReader reader) {
        try {
            if (reader != null) {
                reader.close();
            }
        } catch (IOException e) {
            System.out.println("Error while closing BufferedReader " + e.getMessage());
        }
    }

    //Performs file write operation
    public static void fileWrite(ArrayList<ArrayList<Object>> dataList, String outputFilename, String delimiter) {
        BufferedWriter writer = null;
        File file = new File(outputFilename);
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < dataList.size(); i++) {
                for (int j = 0; j < dataList.get(i).size(); j++) {
                    if (delimiter.equals(" ")) {
                        builder.append(dataList.get(i).get(j) + " ");
                    } else {
                        if (j < dataList.get(i).size() - 1) {
                            builder.append(dataList.get(i).get(j) + ",");
                        } else {
                            builder.append(dataList.get(i).get(j) + " ");
                        }
                    }
                }
                builder.append("\n");
            }
            writer = new BufferedWriter(new FileWriter(outputFilename));
            writer.write(builder.toString());
        } catch (IOException e) {
            System.out.println("Error while writing file " + outputFilename + e.getMessage());
        } finally {
            try {
                writer.close();
            } catch (IOException e) {
                System.out.println("Error while closing writer " + e.getMessage());
            }
        }
    }

    //Creates data.txt file
    public static void createDataFile(ArrayList<ArrayList<Object>> dataList, ArrayList<ArrayList<Object>> removedDataList,
                                      String inputFileName, String outputFileName) {
        String delimiter = " ";
        fileRead(dataList, inputFileName, delimiter);
        if (dataList.size() > 2) {
            removedData(dataList, removedDataList);
            dataList.remove(0);
            dataList.remove(0);
            fileWrite(dataList, outputFileName, " ");
        } else {
            System.out.println(inputFileName + " does not have data to perform");
            System.out.println("Could not create " + outputFileName);
        }
    }

    //Removes column attribute where that column has same domain value
    public static void removedData(ArrayList<ArrayList<Object>> dataList,
                                   ArrayList<ArrayList<Object>> removedDataList) {
        int count;
        int size = dataList.size();
        for (int column = 0; column < dataList.get(0).size(); column++) {
            ArrayList<Object> removedData = new ArrayList<>();
            Object firstData = dataList.get(2).get(column);
            count = 0;
            for (int row = 2; row < dataList.size() - 1; row++) {
                if (dataList.get(row + 1).get(column).equals(firstData)) {
                    count++;
                }
            }
            if (count == size - 3) {
                for (int i = 0; i < dataList.size(); i++) {
                    removedData.add(dataList.get(i).get(column));
                    dataList.get(i).remove(column);
                }
                if (column > 0) {
                    column = column - 1;
                }
            }
            if (!removedData.isEmpty()) {
                removedDataList.add(removedData);
            }
        }
    }

    //Creates name.txt file
    public static void createNameFile(ArrayList<ArrayList<Object>> dataList, String inputFileName,
                                      String outputFileName) {
        fileRead(dataList, inputFileName, " ");
        if (dataList.isEmpty() || dataList.size() <= 2) {
            System.out.println("could not create " + outputFileName);
        } else {
            ArrayList<ArrayList<Object>> nameArrayList = new ArrayList<>();

            for (int column = 0; column < dataList.get(0).size(); column++) {
                ArrayList<Object> data = new ArrayList<>();
                ArrayList<Object> newData = new ArrayList<>();
                addData(dataList, data, dataList.size(), column);

                if (data.get(1).equals("n")) {
                    newData.add(data.get(1));
                    newData.add(data.get(0));
                    data.remove(1);
                    data.remove(0);
                    newData.add(calculateMin(data));
                    newData.add(calculateMax(data));
                } else if (data.get(1).equals("c")) {
                    newData.add(data.get(1));
                    newData.add(data.get(0));
                    data.remove(1);
                    data.remove(0);
                    List<Object> uniqueList = data.stream().distinct().collect(Collectors.toList());
                    if (uniqueList.size() != 1) {
                        newData.add(uniqueList.size());
                        for (int i = 0; i < uniqueList.size(); i++) {
                            newData.add(uniqueList.get(i));
                        }
                    } else {
                        newData.remove(1);
                        newData.remove(0);
                    }
                }
                if (!newData.isEmpty()) {
                    nameArrayList.add(newData);
                }
            }
            fileWrite(nameArrayList, outputFileName, ",");
        }
    }

    //Adds element into ArrayList
    public static void addData(ArrayList<ArrayList<Object>> dataList, ArrayList<Object> data, int size, int j) {
        for (int i = 0; i < size; i++) {
            data.add(dataList.get(i).get(j));
        }
    }

    //Calculates minimum value of given list
    public static Double calculateMin(ArrayList dataList) {
        Double min = Double.parseDouble(dataList.get(0).toString());
        for (int i = 1; i < dataList.size(); i++) {
            Double minValue = Double.parseDouble(dataList.get(i).toString());
            if (minValue < min) {
                min = minValue;
            }
        }
        return min;
    }

    //Calculates maximum value of given list
    public static Double calculateMax(ArrayList dataList) {
        Double max = Double.parseDouble(dataList.get(0).toString());
        for (int i = 1; i < dataList.size(); i++) {
            Double maxValue = Double.parseDouble(dataList.get(i).toString());
            if (max < maxValue) {
                max = maxValue;
            }
        }
        return max;
    }

    private static void normalizeData(ArrayList<ArrayList<Object>> dataList, ArrayList<ArrayList<Object>> nameList,
                                      String dataFileName, String nameFileName, String normalizeDataFileName) {
        ArrayList<ArrayList<Object>> normalizeDataList = new ArrayList<>();

        fileRead(dataList, dataFileName, " ");
        fileRead(nameList, nameFileName, ",");
        if (dataList.isEmpty() || nameList.isEmpty()) {
            System.out.println("data.txt or name.txt file is empty");
        } else {
            DecimalFormat decimalFormat = new DecimalFormat("0.000");
            for (int row = 0; row < dataList.size(); row++) {
                int r = 0;
                ArrayList<Object> data = new ArrayList<>();
                for (int column = 0; column < dataList.get(0).size(); column++) {
                    if (r < nameList.size() && nameList.get(r).get(0).equals("n")) {
                        Double minVal = Double.parseDouble(nameList.get(r).get(2).toString());
                        Double maxVal = Double.parseDouble(nameList.get(r).get(3).toString());
                        if (maxVal > 1) {
                            if (minVal.compareTo(maxVal) != 0) {
                                Double attributeValue = Double.parseDouble(dataList.get(row).get(column).toString());
                                data.add(decimalFormat.format(calculateNormalizedValue(attributeValue, minVal, maxVal)));
                            }

                        } else {
                            data.add(decimalFormat.format(Double.parseDouble(dataList.get(row).get(column).toString())));
                        }
                    }
                    if (r < nameList.size() && nameList.get(r).get(0).equals("c")) {
                        data.add(dataList.get(row).get(column));
                    }
                    r++;
                }
                if (!data.isEmpty()) {
                    normalizeDataList.add(data);
                }
            }
            fileWrite(normalizeDataList, normalizeDataFileName, " ");
        }
    }

    //Calculates of normalize value
    public static Object calculateNormalizedValue(Double value, Double min, Double max) {
        return (value - min) / (max - min);
    }

    //Creates  newname.txt file
    public static void createNewNameFile(ArrayList<ArrayList<Object>> dataList, String newNameFileName) {
        ArrayList<ArrayList<Object>> nameFileArrayList = new ArrayList<>();

        for (int row = 0; row < dataList.size(); row++) {
            ArrayList<Object> data = new ArrayList<>();
            if (dataList.get(row).get(1).equals("n")) {
                data.add(dataList.get(row).get(1));
                data.add(dataList.get(row).get(0));
                Double MinMaxvalue = Double.parseDouble(dataList.get(row).get(2).toString());
                data.add(MinMaxvalue);
                data.add(MinMaxvalue);
            } else if (dataList.get(row).get(1).equals("c")) {
                data.add(dataList.get(row).get(1));
                data.add(dataList.get(row).get(0));
                data.add(dataList.get(row).size() - 2);
                data.add(dataList.get(row).get(2));

            }
            if (!data.isEmpty()) {
                nameFileArrayList.add(data);
            } else {
                System.out.println("There is no data to write into " + newNameFileName);
            }
        }
        fileWrite(nameFileArrayList, newNameFileName, ",");
    }
}