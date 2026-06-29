package singleton;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

// clase singleton para manejar una sola conexion a mysql
public class ConexionDB {

    // unica instancia de la clase
    private static ConexionDB instancia;

    // objeto de conexion a la base de datos
    private Connection connection;

    // si existe variable de entorno de railway la usa, sino usa la conexion local
    private static final String URL = System.getenv("DB_URL") != null
            ? System.getenv("DB_URL")
            : "jdbc:mysql://localhost:3306/InventarioAtix?useSSL=false&serverTimezone=America/Lima";

    // si existe variable de entorno de railway la usa, sino usa root local
    private static final String USER = System.getenv("DB_USER") != null
            ? System.getenv("DB_USER")
            : "root";

    // si existe variable de entorno de railway la usa, sino usa la contrasena local
    private static final String PASSWORD = System.getenv("DB_PASSWORD") != null
            ? System.getenv("DB_PASSWORD")
            : "Root_123";

    // constructor privado que establece la conexion
    private ConexionDB() {
        try {
            // carga el driver de mysql
            Class.forName("com.mysql.cj.jdbc.Driver");
            // establece la conexion con la base de datos
            this.connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("conexion exitosa a mysql");
        } catch (ClassNotFoundException e) {
            System.err.println("driver no encontrado: " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("error de conexion: " + e.getMessage());
        }
    }

    // retorna la instancia unica del singleton
    public static ConexionDB getInstancia() {
        if (instancia == null) {
            instancia = new ConexionDB();
        }
        return instancia;
    }

    // retorna la conexion, reconecta si es necesario
    public Connection getConnection() {
        try {
            // si la conexion es nula o esta cerrada, intenta reconectar directamente
            if (connection == null || connection.isClosed()) {
                // carga el driver de mysql
                Class.forName("com.mysql.cj.jdbc.Driver");
                // obtiene los valores de las variables de entorno o usa los locales
                String url = System.getenv("DB_URL") != null
                        ? System.getenv("DB_URL")
                        : "jdbc:mysql://localhost:3306/InventarioAtix?useSSL=false&serverTimezone=America/Lima";
                String user = System.getenv("DB_USER") != null
                        ? System.getenv("DB_USER")
                        : "root";
                String password = System.getenv("DB_PASSWORD") != null
                        ? System.getenv("DB_PASSWORD")
                        : "Root_123";
                // reconecta con los datos correctos
                this.connection = DriverManager.getConnection(url, user, password);
                System.out.println("reconexion exitosa");
            }
        } catch (Exception e) {
            System.err.println("error verificando conexion: " + e.getMessage());
        }
        return connection;
    }
}
