import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.sql.*;

public class CadastroUsuario extends JFrame {
    private JButton adicionarButton;
    private JButton editarButton;
    private JButton excluirButton;
    private JTable table;
    private DefaultTableModel tableModel;
    private JToolBar toolBar;

    public CadastroUsuario() {
        setTitle("Cadastro de Usuário");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setVisible(true);

        adicionarButton = new JButton("Adicionar Usuário", new ImageIcon("src/Icon/add.png"));
        editarButton = new JButton("Editar Usuário", new ImageIcon("src/Icon/delet.png"));
        excluirButton = new JButton("Excluir Usuário", new ImageIcon("src/Icon/delet.png"));

        toolBar = new JToolBar();

        toolBar.add(adicionarButton);
        toolBar.add(editarButton);
        toolBar.add(excluirButton);
        toolBar.setFloatable(false);

        tableModel = new DefaultTableModel();
        tableModel.setColumnIdentifiers(new Object[]{
                "ID", "Username", "Nome Completo", "Função", "Status", "Data de Criação", "Último Acesso"
        });

        table = new JTable(tableModel) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table.setRowHeight(25);
        table.setFont(new Font("Arial", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        table.getTableHeader().setBackground(new Color(0, 120, 215));
        table.getTableHeader().setForeground(Color.WHITE);

        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(tableModel);
        table.setRowSorter(sorter);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Lista de Usuários"));

        setLayout(new BorderLayout());
        add(toolBar, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        carregarDados();
        excluirButton.addActionListener(e -> excluirUsuario());
        editarButton.addActionListener(e -> editarUsuario());
        adicionarButton.addActionListener(e -> abrirJanelaAdicionarUsuario());
    }

    private void carregarDados() {
        tableModel.setRowCount(0);

        String url = "jdbc:mysql://127.0.0.1:3306/PDV_BCO";
        String user = "root";
        String password = "Cross-fire1";

        try (Connection connection = DriverManager.getConnection(url, user, password);
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
                        resultSet.getTimestamp("ultimo_acesso")
                });
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro ao carregar dados: " + e.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void excluirUsuario() {
        int row = table.getSelectedRow();
        if (row != -1) {
            int userId = (int) tableModel.getValueAt(row, 0);
            int confirm = JOptionPane.showConfirmDialog(this, "Deseja realmente excluir este usuário?",
                    "Confirmar exclusão", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                try (Connection connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/PDV_BCO", "root", "Cross-fire1");
                     PreparedStatement ps = connection.prepareStatement("DELETE FROM usuarios WHERE id = ?")) {
                    ps.setInt(1, userId);
                    ps.executeUpdate();
                    tableModel.removeRow(row);
                    JOptionPane.showMessageDialog(this, "Usuário excluído com sucesso!");
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Erro ao excluir usuário: " + ex.getMessage(),
                            "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Selecione um usuário para excluir.", "Aviso", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void editarUsuario() {
        int row = table.getSelectedRow();
        if (row != -1) {
            int userId = (int) tableModel.getValueAt(row, 0);
            String username = (String) tableModel.getValueAt(row, 1);
            String nomeCompleto = (String) tableModel.getValueAt(row, 2);
            String funcao = (String) tableModel.getValueAt(row, 3);
            String status = (String) tableModel.getValueAt(row, 4);
            String senha = "";

            String url = "jdbc:mysql://127.0.0.1:3306/PDV_BCO";
            String user = "root";
            String password = "Cross-fire1";

            try (Connection connection = DriverManager.getConnection(url, user, password);
                 PreparedStatement ps = connection.prepareStatement("SELECT senha FROM usuarios WHERE id = ?")) {
                ps.setInt(1, userId);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    senha = rs.getString("senha");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Erro ao carregar a senha: " + e.getMessage(),
                        "Erro", JOptionPane.ERROR_MESSAGE);
            }

            JTextField usernameField = new JTextField(username);
            JTextField nomeCompletoField = new JTextField(nomeCompleto);
            JTextField senhaField = new JTextField(senha);
            JTextField funcaoField = new JTextField(funcao);

            String[] statusOptions = {"Ativo", "Inativo", "Bloqueado"};
            JComboBox<String> statusField = new JComboBox<>(statusOptions);
            statusField.setSelectedItem(status);

            JPanel panel = new JPanel(new GridLayout(0, 1));
            panel.add(new JLabel("Username:"));
            panel.add(usernameField);
            panel.add(new JLabel("Nome Completo:"));
            panel.add(nomeCompletoField);
            panel.add(new JLabel("Senha:"));
            panel.add(senhaField);
            panel.add(new JLabel("Função:"));
            panel.add(funcaoField);
            panel.add(new JLabel("Status:"));
            panel.add(statusField);

            int result = JOptionPane.showConfirmDialog(this, panel, "Editar Usuário", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if (result == JOptionPane.OK_OPTION) {
                try (Connection connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/PDV_BCO", "root", "Cross-fire1");
                     PreparedStatement ps = connection.prepareStatement("UPDATE usuarios SET username = ?, nome_completo = ?, senha = ?, funcao = ?, status = ?, modificado_por = ? WHERE id = ?")) {

                    ps.setString(1, usernameField.getText());
                    ps.setString(2, nomeCompletoField.getText());
                    ps.setString(3, senhaField.getText());
                    ps.setString(4, funcaoField.getText());
                    ps.setString(5, (String) statusField.getSelectedItem());
                    ps.setString(6, "admin");
                    ps.setInt(7, userId);
                    ps.executeUpdate();
                    carregarDados();

                    JOptionPane.showMessageDialog(this, "Usuário atualizado com sucesso!");
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Erro ao atualizar usuário: " + ex.getMessage(),
                            "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Selecione um usuário para editar.", "Aviso", JOptionPane.WARNING_MESSAGE);
        }
    }


    private void abrirJanelaAdicionarUsuario() {
        JFrame janelaAdicionarUsuario = new JFrame("Adicionar Novo Usuário");
        janelaAdicionarUsuario.setSize(400, 400);
        janelaAdicionarUsuario.setLayout(new GridLayout(0, 1));
        janelaAdicionarUsuario.setResizable(false);
        janelaAdicionarUsuario.setLocationRelativeTo(null);

        JTextField usernameField = new JTextField();
        JTextField nomeCompletoField = new JTextField();
        JTextField senhaField = new JTextField();
        JTextField funcaoField = new JTextField();
        JTextField criadoPorField = new JTextField();

        String[] statusOptions = {"Ativo", "Inativo", "Bloqueado"};
        JComboBox<String> statusField = new JComboBox<>(statusOptions);

        janelaAdicionarUsuario.add(new JLabel("Username:"));
        janelaAdicionarUsuario.add(usernameField);
        janelaAdicionarUsuario.add(new JLabel("Nome Completo:"));
        janelaAdicionarUsuario.add(nomeCompletoField);
        janelaAdicionarUsuario.add(new JLabel("Senha:"));
        janelaAdicionarUsuario.add(senhaField);
        janelaAdicionarUsuario.add(new JLabel("Função:"));
        janelaAdicionarUsuario.add(funcaoField);
        janelaAdicionarUsuario.add(new JLabel("Status:"));
        janelaAdicionarUsuario.add(statusField);
        janelaAdicionarUsuario.add(new JLabel("Criado por:"));
        janelaAdicionarUsuario.add(criadoPorField);

        JButton salvarButton = new JButton("Salvar");
        JButton cancelarButton = new JButton("Cancelar");
        janelaAdicionarUsuario.add(salvarButton);
        janelaAdicionarUsuario.add(cancelarButton);

        salvarButton.addActionListener(e -> {
            String username = usernameField.getText();
            String nomeCompleto = nomeCompletoField.getText();
            String senha = senhaField.getText();
            String funcao = funcaoField.getText();
            String status = (String) statusField.getSelectedItem();
            String criadoPor = criadoPorField.getText();

            try (Connection connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/PDV_BCO", "root", "Cross-fire1");
                 PreparedStatement ps = connection.prepareStatement("INSERT INTO usuarios (username, nome_completo, senha, funcao, status, criado_por) VALUES (?, ?, ?, ?, ?, ?)")) {
                ps.setString(1, username);
                ps.setString(2, nomeCompleto);
                ps.setString(3, senha);
                ps.setString(4, funcao);
                ps.setString(5, status);
                ps.setString(6, criadoPor);
                ps.executeUpdate();

                carregarDados();
                janelaAdicionarUsuario.dispose();
                JOptionPane.showMessageDialog(this, "Usuário adicionado com sucesso!");
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Erro ao adicionar usuário: " + ex.getMessage(),
                        "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });

        cancelarButton.addActionListener(e -> janelaAdicionarUsuario.dispose());

        janelaAdicionarUsuario.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(CadastroUsuario::new);
    }
}
