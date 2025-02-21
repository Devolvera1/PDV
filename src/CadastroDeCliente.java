import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.sql.*;

public class CadastroDeCliente extends JFrame {
    private JButton adicionarButton;
    private JButton editarButton;
    private JButton excluirButton;
    private JButton atualizarButton;
    private JTable table;
    private DefaultTableModel tableModel;
    private JToolBar toolBar;

    public CadastroDeCliente() {
        setTitle("Cadastro de Cliente");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);

        adicionarButton = new JButton("Adicionar Cliente", new ImageIcon("src/Icon/add.png"));
        editarButton = new JButton("Editar Cliente", new ImageIcon("src/Icon/delet.png"));
        excluirButton = new JButton("Excluir Cliente", new ImageIcon("src/Icon/delet.png"));
        atualizarButton = new JButton(new ImageIcon("src/Icon/refresh.png"));

        toolBar = new JToolBar();

        toolBar.add(adicionarButton);
        toolBar.add(editarButton);
        toolBar.add(excluirButton);
        toolBar.add(Box.createHorizontalGlue());
        toolBar.add(atualizarButton);
        toolBar.setFloatable(false);

        tableModel = new DefaultTableModel();
        tableModel.setColumnIdentifiers(new Object[]{
                "ID", "Nome", "Tipo de Documento", "Documento", "Telefone", "E-mail", "Endereço", "Data de Cadastro"
        });

        table = new JTable(tableModel) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table.setAutoResizeMode(table.AUTO_RESIZE_ALL_COLUMNS);
        table.setFont(new Font("Arial", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        table.getTableHeader().setBackground(new Color(0, 120, 215));
        table.getTableHeader().setForeground(Color.WHITE);

        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(tableModel);
        table.setRowSorter(sorter);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Lista de Clientes"));

        setLayout(new BorderLayout());
        add(toolBar, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        carregarDados();

        excluirButton.addActionListener(e -> excluirCliente());
        editarButton.addActionListener(e -> editarCliente());
        adicionarButton.addActionListener(e -> abrirJanelaAdicionarCliente());
        atualizarButton.addActionListener(e -> carregarDados());
    }

    private void carregarDados() {
        tableModel.setRowCount(0);

        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM clientes")) {

            while (resultSet.next()) {
                tableModel.addRow(new Object[]{
                        resultSet.getInt("id"),
                        resultSet.getString("nome"),
                        resultSet.getString("tipo_documento"),
                        resultSet.getString("documento"),
                        resultSet.getString("telefone"),
                        resultSet.getString("email"),
                        resultSet.getString("endereco"),
                        resultSet.getTimestamp("data_cadastro")
                });
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro ao carregar dados: " + e.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void editarCliente() {
        int row = table.getSelectedRow();
        if (row != -1) {
            int userId = (int) tableModel.getValueAt(row, 0);
            String nome = (String) tableModel.getValueAt(row, 1);
            String tipoDocumento = (String) tableModel.getValueAt(row, 2);
            String documento = (String) tableModel.getValueAt(row, 3);
            String telefone = (String) tableModel.getValueAt(row, 4);
            String email = (String) tableModel.getValueAt(row, 5);
            String endereco = (String) tableModel.getValueAt(row, 6);

            JTextField nomeField = new JTextField(nome);
            JTextField tipoDocumentoField = new JTextField(tipoDocumento);
            JTextField documentoField = new JTextField(documento);
            JTextField telefoneField = new JTextField(telefone);
            JTextField emailField = new JTextField(email);
            JTextField enderecoField = new JTextField(endereco);

            JPanel panel = new JPanel(new GridLayout(0, 1));
            panel.add(new JLabel("Nome:"));
            panel.add(nomeField);
            panel.add(new JLabel("Tipo de Documento:"));
            panel.add(tipoDocumentoField);
            panel.add(new JLabel("Documento:"));
            panel.add(documentoField);
            panel.add(new JLabel("Telefone:"));
            panel.add(telefoneField);
            panel.add(new JLabel("E-mail:"));
            panel.add(emailField);
            panel.add(new JLabel("Endereço:"));
            panel.add(enderecoField);

            int result = JOptionPane.showConfirmDialog(this, panel, "Editar Cliente", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if (result == JOptionPane.OK_OPTION) {
                try (Connection connection = DatabaseConnection.getConnection();
                     PreparedStatement ps = connection.prepareStatement("UPDATE clientes SET nome = ?, tipo_documento = ?, documento = ?, telefone = ?, email = ?, endereco = ? WHERE id = ?")) {

                    ps.setString(1, nomeField.getText());
                    ps.setString(2, tipoDocumentoField.getText());
                    ps.setString(3, documentoField.getText());
                    ps.setString(4, telefoneField.getText());
                    ps.setString(5, emailField.getText());
                    ps.setString(6, enderecoField.getText());
                    ps.setInt(7, userId);
                    ps.executeUpdate();
                    carregarDados();

                    JOptionPane.showMessageDialog(this, "Cliente atualizado com sucesso!");
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Erro ao atualizar cliente: " + ex.getMessage(),
                            "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Selecione um cliente para editar.", "Aviso", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void excluirCliente() {
        int row = table.getSelectedRow();
        if (row != -1) {
            int userId = (int) tableModel.getValueAt(row, 0);
            int confirm = JOptionPane.showConfirmDialog(this, "Deseja realmente excluir este cliente?",
                    "Confirmar exclusão", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                try (Connection connection = DatabaseConnection.getConnection();
                     PreparedStatement ps = connection.prepareStatement("DELETE FROM clientes WHERE id = ?")) {
                    ps.setInt(1, userId);
                    ps.executeUpdate();
                    tableModel.removeRow(row);
                    JOptionPane.showMessageDialog(this, "Cliente excluído com sucesso!");
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Erro ao excluir cliente: " + ex.getMessage(),
                            "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Selecione um cliente para excluir.", "Aviso", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void abrirJanelaAdicionarCliente() {
        JFrame janelaAdicionarCliente = new JFrame("Adicionar Novo Cliente");
        janelaAdicionarCliente.setSize(400, 400);
        janelaAdicionarCliente.setLayout(new GridLayout(0, 1));
        janelaAdicionarCliente.setResizable(false);
        janelaAdicionarCliente.setLocationRelativeTo(null);

        JTextField nomeField = new JTextField();
        JTextField tipoDocumentoField = new JTextField();
        JTextField documentoField = new JTextField();
        JTextField telefoneField = new JTextField();
        JTextField emailField = new JTextField();
        JTextField enderecoField = new JTextField();

        janelaAdicionarCliente.add(new JLabel("Nome:"));
        janelaAdicionarCliente.add(nomeField);
        janelaAdicionarCliente.add(new JLabel("Tipo de Documento:"));
        janelaAdicionarCliente.add(tipoDocumentoField);
        janelaAdicionarCliente.add(new JLabel("Documento:"));
        janelaAdicionarCliente.add(documentoField);
        janelaAdicionarCliente.add(new JLabel("Telefone:"));
        janelaAdicionarCliente.add(telefoneField);
        janelaAdicionarCliente.add(new JLabel("E-mail:"));
        janelaAdicionarCliente.add(emailField);
        janelaAdicionarCliente.add(new JLabel("Endereço:"));
        janelaAdicionarCliente.add(enderecoField);

        JButton salvarButton = new JButton("Salvar");
        JButton cancelarButton = new JButton("Cancelar");
        janelaAdicionarCliente.add(salvarButton);
        janelaAdicionarCliente.add(cancelarButton);

        salvarButton.addActionListener(e -> {
            String nome = nomeField.getText();
            String tipoDocumento = tipoDocumentoField.getText();
            String documento = documentoField.getText();
            String telefone = telefoneField.getText();
            String email = emailField.getText();
            String endereco = enderecoField.getText();

            try (Connection connection = DatabaseConnection.getConnection();
                 PreparedStatement ps = connection.prepareStatement("INSERT INTO clientes (nome, tipo_documento, documento, telefone, email, endereco) VALUES (?, ?, ?, ?, ?, ?)")) {
                ps.setString(1, nome);
                ps.setString(2, tipoDocumento);
                ps.setString(3, documento);
                ps.setString(4, telefone);
                ps.setString(5, email);
                ps.setString(6, endereco);
                ps.executeUpdate();

                carregarDados();
                janelaAdicionarCliente.dispose();
                JOptionPane.showMessageDialog(this, "Cliente adicionado com sucesso!");
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Erro ao adicionar cliente: " + ex.getMessage(),
                        "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });

        cancelarButton.addActionListener(e -> janelaAdicionarCliente.dispose());

        janelaAdicionarCliente.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(CadastroDeCliente::new);
    }
}
