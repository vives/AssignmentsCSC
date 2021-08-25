import java.io.*;
import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;

/*This DataPreProcessing program implements an application that performs data pre processing process*/
public class DataPreProcessing {

    /*This is a main method which runs all the defined functions */
    public static void main(String[] args) {
        Scanner myObj = new Scanner(System.in);
        System.out.println("*****Welcome to Data Pre-Processing Program*****" + "\n\n" +
                "1.Generate data file and name file" + "\n" + "2.Normalized data file" + "\n"
                + "3.Exit program");
        int number;
        do {
            System.out.println("Please enter a positive number!");
            while (!myObj.hasNextInt()) {
                System.out.println("That's not a number!");
                myObj.next();
            }
            number = myObj.nextInt();
        } while (number <= 0);
        String readFile = "rawdata.txt";
        String dataFile = "data.txt";
        String nameFile = "name.txt";
        String normalizeDataFile = "normalizeddata.txt";
        String newNameFile = "newname.txt";

        if (number == 1) {
            ArrayList<ArrayList<Object>> dataFileArr = new ArrayList<>();
            ArrayList<ArrayList<Object>> nameFileArr = new ArrayList<>();
            ArrayList<ArrayList<Object>> newNameFileArr = new ArrayList<>();
            generateDataFile(dataFileArr, newNameFileArr, readFile, dataFile);
            generateNameFile(nameFileArr, readFile, nameFile);
            if (newNameFileArr.size() > 0) {
                writeNewNameFile(newNameFileArr, newNameFile);
            } else {
                System.out.println(newNameFile + " does not create");
            }

        } else if (number == 2) {
            ArrayList<ArrayList<Object>> dataArr = new ArrayList<>();
            ArrayList<ArrayList<Object>> nameArr = new ArrayList<>();
            normalizeDataFile(dataArr, nameArr, dataFile, nameFile, normalizeDataFile);
        } else if (number == 3) {
            System.out.println("Exit");
            System.exit(0);
        } else {
            System.out.println("Please enter correct number from above mentioned list to perform " +
                    "Data Pre Processing Program");
        }
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
                    if (!line.contains("?")) {
                        Object[] lines = line.trim().split(delimiter);
                        for (int j = 0; j < lines.length; j++) {
                            words.add(lines[j]);
                        }
                        arr.add(words);
                        line = reader.readLine();
                    } else {
                        line = reader.readLine();
                    }
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("Error while reading file: " + e.getMessage());
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
                    if (delimiter.equals(" ")) {
                        builder.append(arrayList.get(i).get(j) + " ");
                    } else {
                        if (j < arrayList.get(i).size() - 1) {
                            builder.append(arrayList.get(i).get(j) + ",");
                        } else {
                            builder.append(arrayList.get(i).get(j) + " ");
                        }
                    }
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

    /*This method is used to create data.txt*/
    public static void generateDataFile(ArrayList<ArrayList<Object>> arr, ArrayList<ArrayList<Object>> removedColumnArr,
                                        String readFileName, String writeFileName) {
        String delimiter = " ";
        readFile(arr, readFileName, delimiter);
        if (arr.size() > 2) {
            findColumnCount(arr, removedColumnArr);
            arr.remove(0);
            arr.remove(0);
            writeFile(arr, writeFileName, delimiter);
        }else if (arr.size() == 1 || arr.size() == 2) {
            System.out.println(writeFileName + " does not create because " + readFileName +
                    " has only either attribute or data type OR both");
        }else{
            System.out.println(readFileName + " does not have any data to create " + writeFileName);
        }
    }

    /*This method is used to remove the attribute which has same domain value*/
    public static void findColumnCount(ArrayList<ArrayList<Object>> arr,
                                       ArrayList<ArrayList<Object>> removedColumnArr) {
        int count;
        int size = arr.size();
        for (int column = 0; column < arr.get(0).size(); column++) {
            ArrayList<Object> removedElement = new ArrayList<>();
            Object firstElement = arr.get(2).get(column);
            count = 0;
            for (int row = 2; row < arr.size() - 1; row++) {
                if (arr.get(row + 1).get(column).equals(firstElement)) {
                    count++;
                }
            }
            if (count == size - 3) {
                for (int e = 0; e < arr.size(); e++) {
                    removedElement.add(arr.get(e).get(column));
                    arr.get(e).remove(column);
                }
                if (column > 0) {
                    column = column - 1;
                }
            }
            if (!removedElement.isEmpty())
                removedColumnArr.add(removedElement);
        }
    }

    /*This method is used to create name.txt file*/
    public static void generateNameFile(ArrayList<ArrayList<Object>> arr, String readFileName, String writeFileName) {
        Double max;
        Double min;
        String delimiterRead = " ";
        String delimiterWrite = ",";
        String numerical = "n";
        String categorical = "c";
        readFile(arr, readFileName, delimiterRead);
        if (!arr.isEmpty() && arr.size() > 2) {
            ArrayList<ArrayList<Object>> nameFileArrayList = new ArrayList<>();

            for (int column = 0; column < arr.get(0).size(); column++) {
                ArrayList<Object> element = new ArrayList<>();
                ArrayList<Object> newElement = new ArrayList<>();
                for (int row = 0; row < arr.size(); row++) {
                    element.add(arr.get(row).get(column));
                }
                if (!element.get(1).equals(numerical) && !element.get(1).equals(categorical)) {
                    System.out.println("Given Data Type is not equal to numerical(n) or categorical(c) of attribute " +
                            "[" + element.get(0) + "]");
                } else if (element.get(1).equals(numerical)) {
                    min = findMin(element);
                    max = findMax(element);
                    newElement.add(element.get(1));
                    newElement.add(element.get(0));
                    newElement.add(min);
                    newElement.add(max);
                } else if (element.get(1).equals(categorical)) {
                    newElement.add(element.get(1));
                    newElement.add(element.get(0));
                    element.remove(1);
                    element.remove(0);
                    List<Object> listUnique = element.stream().distinct().collect(Collectors.toList());
                    if (listUnique.size() != 1) {
                        newElement.add(listUnique.size());
                        for (int index = 0; index < listUnique.size(); index++) {
                            newElement.add(listUnique.get(index));
                        }
                    } else {
                        newElement.remove(1);
                        newElement.remove(0);
                    }
                }
                if (!newElement.isEmpty()) {
                    nameFileArrayList.add(newElement);
                }
            }
            writeFile(nameFileArrayList, writeFileName, delimiterWrite);
        } else {
            System.out.println(readFileName + " does not have any data to create " + writeFileName);
        }
    }

    /*This method is used to find minimum value*/
    public static Double findMin(ArrayList arr) {
        Double minValue = Double.parseDouble(arr.get(2).toString());
        for (int i = 3; i < arr.size(); i++) {
            Double value = Double.parseDouble(arr.get(i).toString());
            if (value < minValue) {
                minValue = value;
            }
        }
        return minValue;
    }

    /*This method is used to find maximum value*/
    public static Double findMax(ArrayList arr) {
        Double maxValue = Double.parseDouble(arr.get(2).toString());
        for (int i = 3; i < arr.size(); i++) {
            Double value = Double.parseDouble(arr.get(i).toString());
            if (value > maxValue) {
                maxValue = value;
            }
        }
        return maxValue;
    }

    /*This method is used to create normalizeddata.txt file*/
    private static void normalizeDataFile(ArrayList<ArrayList<Object>> dataArr, ArrayList<ArrayList<Object>> nameArr,
                                          String dataFile, String nameFile, String normalizeDataFile) {
        String dataFileDelimiter = " ";
        String nameFileDelimiter = ",";
        ArrayList<ArrayList<Object>> normalizeDataArr = new ArrayList<>();
        readFile(dataArr, dataFile, dataFileDelimiter);
        readFile(nameArr, nameFile, nameFileDelimiter);
        if (!dataArr.isEmpty() && !nameArr.isEmpty()) {
            DecimalFormat df = new DecimalFormat("0.000");
            for (int row = 0; row < dataArr.size(); row++) {
                int r = 0;
                ArrayList<Object> element = new ArrayList<>();
                for (int column = 0; column < dataArr.get(0).size(); column++) {
                    if (r < nameArr.size() && nameArr.get(r).get(0).equals("n")) {
                        Double min = Double.parseDouble(nameArr.get(r).get(2).toString());
                        Double max = Double.parseDouble(nameArr.get(r).get(3).toString());
                        if (max > 1) {
                            if (min.compareTo(max) != 0) {
                                Double attributeValue = Double.parseDouble(dataArr.get(row).get(column).toString());
                                Object normalizedValue = (attributeValue - min) / (max - min);
                                element.add(df.format(normalizedValue));
                            }

                        } else {
                            element.add(df.format(Double.parseDouble(dataArr.get(row).get(column).toString())));
                        }
                    }
                    if (r < nameArr.size() && nameArr.get(r).get(0).equals("c")) {
                        element.add(dataArr.get(row).get(column));
                    }
                    r++;
                }
                if (!element.isEmpty()) {
                    normalizeDataArr.add(element);
                }
            }
            writeFile(normalizeDataArr, normalizeDataFile, dataFileDelimiter);
        } else {
            System.out.println(normalizeDataFile + " does not create");
        }
    }

    /*This method is used to create newname.txt file*/
    public static void writeNewNameFile(ArrayList<ArrayList<Object>> arr, String writeNewNameFile) {
        String delimiterWrite = ",";
        String numerical = "n";
        String categorical = "c";
        ArrayList<ArrayList<Object>> nameFileArrayList = new ArrayList<>();

        if (arr.size() > 0) {
            for (int row = 0; row < arr.size(); row++) {
                ArrayList<Object> element = new ArrayList<>();
                if (arr.get(row).get(1).equals(numerical)) {
                    element.add(arr.get(row).get(1));
                    element.add(arr.get(row).get(0));
                    Double value = Double.parseDouble(arr.get(row).get(2).toString());
                    element.add(value);
                    element.add(value);
                } else if (arr.get(row).get(1).equals(categorical)) {
                    element.add(arr.get(row).get(1));
                    element.add(arr.get(row).get(0));
                    element.add(arr.get(row).size() - 2);
                    element.add(arr.get(row).get(2));

                }
                if (!element.isEmpty()) {
                    nameFileArrayList.add(element);
                }
            }
            writeFile(nameFileArrayList, writeNewNameFile, delimiterWrite);
        } else {
            System.out.println(writeNewNameFile + " does not create");
        }
    }
}