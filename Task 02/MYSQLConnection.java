import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

//Initiates SQL connection
public class MYSQLConnection {

    //This method is used to get a connection
    public static Connection getConnection()
    {
        try {
            FileInputStream fs = new FileInputStream("resources/config.properties");

            Properties prop = new Properties();
            prop.load(fs);
            String connectionURL = "jdbc:mysql://" + prop.getProperty("HOST_NAME") + ":" + prop.getProperty("PORT") + "/"
                    + prop.getProperty("DATABASE_NAME");

          Connection con = DriverManager.getConnection(connectionURL, prop.getProperty("USER_NAME"),
                  prop.getProperty("PASSWORD"));
          return con;
        } catch (SQLException e) {
            System.out.println("Error while making MYSQL connection: " + e.getMessage());
        } catch (FileNotFoundException e) {
            System.out.println("Error while reading property value form config file: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("Error while reading config file: " + e.getMessage());
        }
        return null;
    }
}
