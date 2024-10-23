package umg.principal.DaseDatos.conexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:sqlite:C:/SQLiteDB/DBproductos.db";


    public static Connection getConnection() throws SQLException, SQLException {
        return DriverManager.getConnection(URL);
    }
}
