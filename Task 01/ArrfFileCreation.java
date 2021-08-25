import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

//Performs ARRF file creation tasks
public class ArrfFileCreation {

    //This is a main method which is used to do all functions
    public static void main(String[] args) {
        String inputFile = "rawarffdata.txt";
        String outputFile = "SampleData.ARFF";
        ArrayList<ArrayList<Object>> dataList = new ArrayList<>();
        CreateArffFile(dataList, inputFile, outputFile);
    }

    //Creates ARRF File
    public static void CreateArffFile(ArrayList<ArrayList<Object>> dataList, String inputFileName,
                                      String outputFileName) {
        fileRead(dataList, inputFileName);
        if (dataList.isEmpty()) {
            System.out.println("Could not create " + outputFileName);
        } else {
            ArrayList<ArrayList<Object>> arffFileList = new ArrayList<>();
            ArrayList<Object> list = new ArrayList<>();
            list.add("@relation " + dataList.get(0).get(0));
            arffFileList.add(list);
            for (int column = 0; column < dataList.get(1).size(); column++) {
                ArrayList<Object> elementList = new ArrayList<>();
                ArrayList<Object> newList = new ArrayList<>();
                for (int row = 1; row < dataList.size(); row++) {
                    elementList.add(dataList.get(row).get(column));
                }
                buildString(elementList, newList);
                if (!newList.isEmpty()) {
                    arffFileList.add(newList);
                }
            }
            getDataElement(dataList, arffFileList);
            fileWrite(arffFileList, outputFileName);
        }
    }

    //Performs file read operation
    public static void fileRead(ArrayList<ArrayList<Object>> dataList, String inputFilename) {
        BufferedReader reader = null;
        File file = new File(inputFilename);
        if(!file.exists()){
            System.out.println(inputFilename + " does not exit");
        }else if(file.length() <=0){
            System.out.println(inputFilename + " is empty");
        }else{
            readLine(reader, dataList, inputFilename);
        }
    }

    //Reads a file line by line
    public static void readLine(BufferedReader reader, ArrayList<ArrayList<Object>> dataList, String inputFilename) {
        String delimiter = " ";
        try {
            reader = new BufferedReader(new FileReader(inputFilename));
            String line = reader.readLine();
            while (line != null) {
                ArrayList<Object> element = new ArrayList<>();
                Object[] linesData = line.trim().split(delimiter);
                for (int row = 0; row < linesData.length; row++) {
                    element.add(linesData[row]);
                }
                dataList.add(element);
                line = reader.readLine();
            }
        } catch (IOException e) {
            System.out.println("Error while reading file : " + inputFilename);
        } finally {
            closeBufferedReader(reader);
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
    public static void fileWrite(ArrayList<ArrayList<Object>> dataList, String outputFilename) {
        File file = new File(outputFilename);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                System.out.println("Error occurred while creating file : " + outputFilename);
            }
        }
        writeLine(dataList, outputFilename);
    }

    //Performs file write line by line
    public static void writeLine(ArrayList<ArrayList<Object>> dataList, String outputFilename) {
        BufferedWriter writer = null;
        StringBuilder builder = new StringBuilder();
        try {
            for (int row = 0; row < dataList.size(); row++) {
                for (int column = 0; column < dataList.get(row).size(); column++) {
                    builder.append(dataList.get(row).get(column) + " ");
                }
                builder.append("\n");
            }
            writer = new BufferedWriter(new FileWriter(outputFilename));
            writer.write(builder.toString());
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

    //Performs to build a string
    public static void buildString(ArrayList<Object> elementList, ArrayList<Object> newList) {

        if (elementList.get(1).equals("n")) {
            String numericalDataTypeString;
            numericalDataTypeString = "@attribute " + elementList.get(0) + " REAL";
            newList.add(numericalDataTypeString);
        } else if (elementList.get(1).equals("c")) {
            String categoricalDataTypeString;
            List<Object> uniqueList = elementList.stream().distinct().collect(Collectors.toList());
            if (uniqueList.size() > 0) {
                categoricalDataTypeString = "{";
                for (int index = 2; index < uniqueList.size(); index++) {
                    categoricalDataTypeString += "'" + uniqueList.get(index) + "'";
                    if (index < uniqueList.size() - 1) {
                        categoricalDataTypeString += ',';
                    }
                }
                categoricalDataTypeString += "}";
                String buildCategoricalDataTypeString = "@attribute " + elementList.get(0) + categoricalDataTypeString;
                newList.add(buildCategoricalDataTypeString);
            } else {
                System.out.println(elementList.get(0) + "does not have data to build string");
            }
        } else {
            System.out.println("Attribute" + "[" + elementList.get(0) + "] data Type is not equal to either numerical or categorical");
        }
    }

    //Gets each data element to prepare data string
    public static void getDataElement(ArrayList<ArrayList<Object>> dataArrayList,
                                      ArrayList<ArrayList<Object>> dataList) {
        ArrayList<Object> list = new ArrayList<>();
        list.add("@data");
        dataList.add(list);
        for (int row = 3; row < dataArrayList.size(); row++) {
            ArrayList<Object> listElement = new ArrayList<>();
            String value = "";
            for (int column = 0; column < dataArrayList.get(1).size(); column++) {
                value += dataArrayList.get(row).get(column) + ",";
            }
            listElement.add(value);
            dataList.add(listElement);
        }
    }
}