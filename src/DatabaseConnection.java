import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DatabaseConnection {

    private static final String URL = "jdbc:mysql://127.0.0.1:3306/PDV_BCO";
    private static final String USER = "root";
    private static final String PASSWORD = "Cross-fire1";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public boolean authenticateUser(String username, String password) {
        String query = "SELECT * FROM usuarios WHERE username = ? AND senha = ?";

        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);

            ResultSet resultSet = preparedStatement.executeQuery();

            return resultSet.next();

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void updateLastAccess(String username, LocalDateTime dataUltimoAcesso, String enderecoIP) {
        String updateQuery = "UPDATE usuarios SET ultimo_acesso = ?, endereco_ip = ? WHERE username = ?";
        String historyQuery = "INSERT INTO historico_acessos (usuario_id, data_acesso, endereco_ip) " +
                "SELECT id, ?, ? FROM usuarios WHERE username = ?";

        try (Connection connection = getConnection();
             PreparedStatement updateStmt = connection.prepareStatement(updateQuery);
             PreparedStatement historyStmt = connection.prepareStatement(historyQuery)) {

            // Atualiza o último acesso e IP
            updateStmt.setObject(1, dataUltimoAcesso);
            updateStmt.setString(2, enderecoIP);
            updateStmt.setString(3, username);
            updateStmt.executeUpdate();

            // Insere no histórico de acessos
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String formattedDate = dataUltimoAcesso.format(formatter);

            historyStmt.setString(1, formattedDate);
            historyStmt.setString(2, enderecoIP);
            historyStmt.setString(3, username);
            historyStmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
