import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;

public class LogLogin extends JFrame {

    private static final String DB_URL = "jdbc:mysql://127.0.0.1:3306/PDV_BCO";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "Cross-fire1";

    private JTable table;
    private DefaultTableModel tableModel;

    public LogLogin() {
        setTitle("Log de Login");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Configuração da JTable
        tableModel = new DefaultTableModel();
        tableModel.setColumnIdentifiers(new Object[]{
                "ID", "Username", "Nome Completo", "Função", "Status", "Data de Criação",
                "Último Acesso", "Criado Por", "Data de Modificação", "Modificado Por", "Endereço IP"
        });

        table = new JTable(tableModel) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Desabilitar edição de células
            }
        };
        table.setRowSorter(new TableRowSorter<>(tableModel));
        JScrollPane scrollPane = new JScrollPane(table);

        add(scrollPane, BorderLayout.CENTER);
        carregarDados();

        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) { // Permitir abrir o histórico com um duplo clique
                    int row = table.getSelectedRow();
                    if (row != -1) {
                        int userId = (int) tableModel.getValueAt(row, 0);
                        String username = (String) tableModel.getValueAt(row, 1);
                        mostrarHistoricoAcessos(userId, username);
                    }
                }
            }
        });
    }

    private void carregarDados() {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM usuarios")) {

            while (resultSet.next()) {
                tableModel.addRow(new Object[]{
                        resultSet.getInt("id"),
                        resultSet.getString("username"),
                        resultSet.getString("nome_completo"),
                        resultSet.getString("funcao"),
                        resultSet.getString("status"),
                        resultSet.getTimestamp("data_criacao"),
                        resultSet.getTimestamp("ultimo_acesso"),
                        resultSet.getString("criado_por"),
                        resultSet.getTimestamp("data_modificacao"),
                        resultSet.getString("modificado_por"),
                        resultSet.getString("endereco_ip")
                });
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro ao carregar dados: " + e.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void mostrarHistoricoAcessos(int userId, String username) {
        JFrame historicoFrame = new JFrame("Histórico de Acessos - " + username);
        historicoFrame.setSize(800, 600);
        historicoFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        historicoFrame.setLocationRelativeTo(null);

        DefaultTableModel historicoTableModel = new DefaultTableModel();
        historicoTableModel.setColumnIdentifiers(new Object[]{
                "ID", "Data de Acesso", "Endereço IP"
        });

        JTable historicoTable = new JTable(historicoTableModel) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JScrollPane scrollPane = new JScrollPane(historicoTable);

        historicoFrame.add(scrollPane, BorderLayout.CENTER);
        carregarHistoricoAcessos(userId, historicoTableModel);

        historicoFrame.setVisible(true);
    }

    private void carregarHistoricoAcessos(int userId, DefaultTableModel historicoTableModel) {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "SELECT * FROM historico_acessos WHERE usuario_id = ?")) {

            preparedStatement.setInt(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                historicoTableModel.addRow(new Object[]{
                        resultSet.getInt("id"),
                        resultSet.getTimestamp("data_acesso"),
                        resultSet.getString("endereco_ip")
                });
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erro ao carregar histórico: " + e.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            LogLogin frame = new LogLogin();
            frame.setVisible(true);
            frame.setAlwaysOnTop(false);
        });
    }
}
