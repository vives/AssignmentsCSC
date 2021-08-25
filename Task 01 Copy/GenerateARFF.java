import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/*This GenerateARFF program implements an application that generates ARFF File*/
public class GenerateARFF {

    /*This is a main method which runs all the defined functions */
    public static void main(String[] args) {
        String readFile = "rawarffdata.txt";
        String writeFile = "SampleData.ARFF";
        ArrayList<ArrayList<Object>> arffFileArr = new ArrayList<>();
        generateArffFile(arffFileArr, readFile, writeFile);
    }

    /*This method is used to read a given file*/
    public static void readFile(ArrayList<ArrayList<Object>> arr, String filename, String delimiter) {
        BufferedReader reader = null;
        try {
            File file = new File(filename);
            if (!file.exists() || file.length() == 0) {
                System.out.println(filename + " does not exit or is empty");
            } else {
                reader = new BufferedReader(new FileReader(filename));
                String line = reader.readLine();
                while (line != null) {
                    ArrayList<Object> words = new ArrayList<>();
                    Object[] lines = line.trim().split(delimiter);
                    for (int j = 0; j < lines.length; j++) {
                        words.add(lines[j]);
                    }
                    arr.add(words);
                    line = reader.readLine();
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Could not find file " + e.getMessage());
        } catch (IOException e) {
            System.out.println("Error while reading file " + e.getMessage());
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                System.out.println("Error while closing BufferedReader " + e.getMessage());
            }
        }
    }

    /*This method is used to write a file*/
    public static void writeFile(ArrayList<ArrayList<Object>> arrayList, String filename, String delimiter) {
        BufferedWriter writer = null;
        File file = new File(filename);
        try {
            if (!file.exists()) {
                System.out.println("Creating a File " + filename);
                file.createNewFile();
                System.out.println(filename + " has been created successfully");
            }
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < arrayList.size(); i++) {
                for (int j = 0; j < arrayList.get(i).size(); j++) {
                    builder.append(arrayList.get(i).get(j) + delimiter);
                }
                builder.append("\n");
            }
            writer = new BufferedWriter(new FileWriter(filename));
            writer.write(builder.toString());
            System.out.println("Content has been written successfully");
        } catch (IOException e) {
            System.out.println("Error while writing file " + e.getMessage());
        } finally {
            try {
                writer.close();
            } catch (IOException e) {
                System.out.println("Error while closing BufferedWriter " + e.getMessage());
            }
        }
    }

    /*This method is used to create ARFF file*/
    public static void generateArffFile(ArrayList<ArrayList<Object>> arr, String readFileName, String writeFileName) {
        String delimiter = " ";
        String numerical = "n";
        String categorical = "c";
        readFile(arr, readFileName, delimiter);
        if (!arr.isEmpty()) {
            ArrayList<ArrayList<Object>> ArffFileArrayList = new ArrayList<>();
            ArrayList<Object> list = new ArrayList<>();
            list.add("@relation " + arr.get(0).get(0));
            ArffFileArrayList.add(list);
            for (int column = 0; column < arr.get(1).size(); column++) {
                ArrayList<Object> element = new ArrayList<>();
                ArrayList<Object> newElement = new ArrayList<>();
                String elements;
                for (int row = 1; row < arr.size(); row++) {
                    element.add(arr.get(row).get(column));
                }
                if (element.get(1).equals(numerical)) {
                    String buildNumericalString = "@attribute " + element.get(0) + " REAL";
                    newElement.add(buildNumericalString);
                } else if (element.get(1).equals(categorical)) {
                    List<Object> listUnique = element.stream().distinct().collect(Collectors.toList());
                    if (listUnique.size() > 0) {
                        elements = "{";
                        for (int index = 2; index < listUnique.size(); index++) {
                            elements += "'" + listUnique.get(index) + "'";
                            if (index < listUnique.size() - 1) {
                                elements += ',';
                            }
                        }
                        elements += "}";
                        String buildCategoricalString = "@attribute " + element.get(0) + elements;
                        newElement.add(buildCategoricalString);
                    } else {
                        System.out.println("Attribute " + element.get(0) + "has no value");
                    }
                }
                if (!newElement.isEmpty()) {
                    ArffFileArrayList.add(newElement);
                }
            }
            addData(arr, ArffFileArrayList);
            writeFile(ArffFileArrayList, writeFileName, delimiter);
        } else {
            System.out.println(readFileName + " does not have any data to create " + writeFileName);
        }
    }

    /*This method is used to create @data content*/
    public static void addData(ArrayList<ArrayList<Object>> arr, ArrayList<ArrayList<Object>> dataArr) {
        ArrayList<Object> data = new ArrayList<>();
        data.add("@data");
        dataArr.add(data);
        for (int row = 3; row < arr.size(); row++) {
            ArrayList<Object> dataArrayList = new ArrayList<>();
            String dataElement = "";
            for (int column = 0; column < arr.get(1).size(); column++) {
                dataElement += arr.get(row).get(column) + ",";
            }
            dataArrayList.add(dataElement);
            dataArr.add(dataArrayList);
        }
    }
}