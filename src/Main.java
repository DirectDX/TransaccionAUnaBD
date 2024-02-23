import java.sql.*;


// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {
    public static void main(String[] args) {
        Connection connection = null;
        try {

            connection = getConnection(); // establecemos la conexion con el metodo de abajo
            connection.setAutoCommit(false); // seteamos el autocommit false para poder volver para atras

            Statement statement = connection.createStatement();
                // creo la tabla y la elimino porque sino no puedo correr el programa varias veces y eso me da mucha fiaca,
                // como que tengo que ir creando nuevos pacientes cada ves y no da viste
            statement.execute("DROP TABLE IF EXISTS PACIENTE; CREATE TABLE PACIENTE (ID INT PRIMARY KEY, " +
                    "NOMBRE VARCHAR(50) NOT NULL, " +
                    "APELLIDO VARCHAR(50) NOT NULL, " +
                    "DOMICILIO VARCHAR(50) NOT NULL, " +
                    "DNI INT NOT NULL, " +
                    "FECHA_DE_ALTA DATE NOT NULL, " +
                    "USUARIO VARCHAR(50) NOT NULL, " +
                    "CONTRASEÑA VARCHAR(50) NOT NULL)");

            // Ejecutar la operación INSERT dentro de la transacción
            String query = "INSERT INTO PACIENTE (ID, NOMBRE, APELLIDO, DOMICILIO, DNI, FECHA_DE_ALTA, USUARIO, CONTRASEÑA) VALUES (?,?,?,?,?,?,?,?)";
            try (PreparedStatement pstmt = connection.prepareStatement(query)) {
                pstmt.setInt(1, 1);
                pstmt.setString(2, "Jero");
                pstmt.setString(3, "apellido");
                pstmt.setString(4, "domicilio");
                pstmt.setInt(5, 12345);
                pstmt.setDate(6, Date.valueOf("2024-02-23"));
                pstmt.setString(7, "usuario");
                pstmt.setString(8, "password");
                pstmt.executeUpdate();
            }
            // cambiamos la contraseña del usuario a eleccion (Jero)
            String query2 = "UPDATE PACIENTE SET CONTRASEÑA = ? WHERE NOMBRE = ?";
            try (PreparedStatement pstmt = connection.prepareStatement(query2)) {
                pstmt.setString(1, "NuevaContraseña"); // Aquí debes proporcionar la nueva contraseña real
                pstmt.setString(2, "Jero"); // aca pones el usuario que queres
                pstmt.executeUpdate();
            }
            // Consultar los pacientes
            String consulta = "SELECT * FROM PACIENTE";
            try (PreparedStatement pstmtConsulta = connection.prepareStatement(consulta)) {

                ResultSet rs = pstmtConsulta.executeQuery();

                // Mostrar los resultados de la consulta
                while (rs.next()) {
                    System.out.println("ID: " + rs.getInt("ID"));
                    System.out.println("Nombre: " + rs.getString("NOMBRE"));
                    System.out.println("Apellido: " + rs.getString("APELLIDO"));
                    System.out.println("Domicilio: " + rs.getString("DOMICILIO"));
                    System.out.println("DNI: " + rs.getInt("DNI"));
                    System.out.println("Fecha de Alta: " + rs.getDate("FECHA_DE_ALTA"));
                    System.out.println("Usuario: " + rs.getString("USUARIO"));
                    System.out.println("Contraseña: " + rs.getString("CONTRASEÑA"));
                }
            }

            connection.commit();
            System.out.println("Perfecto");

        }

        catch (Exception e) {
            e.printStackTrace();
            // Si ocurre un error, hacer rollback para revertir los cambios en la transacción
            try {
                if (connection != null) {
                    connection.rollback();
                    System.out.println("Transacción revertida.");
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } finally {
            // Cerrar la conexión
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }



    // salimos del metodo main
    // hacemos un metodo para establecer una conexion
    private static Connection getConnection() throws ClassNotFoundException, SQLException /* Exception tambien funciona */ {
        Class.forName("org.h2.Driver");
        return DriverManager.getConnection("jdbc:h2:./practica9","sa", "sa");
    }

}