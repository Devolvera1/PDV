import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.sql.*;

public class CadastroProduto extends JFrame {
    private JButton adicionarButton;
    private JButton editarButton;
    private JButton excluirButton;
    private JButton atualizarButton;
    private JTable table;
    private DefaultTableModel tableModel;
    private JToolBar toolBar;

    public CadastroProduto() {
        setTitle("Cadastro de Produto");
        setSize(1920, 1080);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);

        adicionarButton = new JButton("Adicionar Produto", new ImageIcon("src/Icon/mais.png"));
        editarButton = new JButton("Editar Produto", new ImageIcon("src/Icon/editar.png"));
        excluirButton = new JButton("Excluir Produto", new ImageIcon("src/Icon/delet.png"));
        atualizarButton = new JButton("Atualizar Lista", new ImageIcon("src/Icon/refresh.png"));

        toolBar = new JToolBar();
        toolBar.add(adicionarButton);
        toolBar.add(editarButton);
        toolBar.add(excluirButton);
        toolBar.add(Box.createHorizontalGlue());
        toolBar.add(atualizarButton);
        toolBar.setFloatable(false);

        tableModel = new DefaultTableModel();
        tableModel.setColumnIdentifiers(new Object[]{
                "ID", "Código", "Nome", "Descrição", "Preço", "QTD em estoque", "Data de Criação", "Data da Modificação"
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
        scrollPane.setBorder(BorderFactory.createTitledBorder("Lista de Produtos"));

        setLayout(new BorderLayout());
        add(toolBar, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        carregarDados();

        editarButton.addActionListener(e -> editar());
        excluirButton.addActionListener(e -> excluirproduto());
        adicionarButton.addActionListener(e -> novoProduto());
        atualizarButton.addActionListener(e -> carregarDados());

        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                System.exit(0);
            }
        });
    }

    private void carregarDados() {
        tableModel.setRowCount(0);

        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM produtos")) {

            while (resultSet.next()) {
                tableModel.addRow(new Object[]{
                        resultSet.getInt("id"),
                        resultSet.getString("codigo"),
                        resultSet.getString("nome"),
                        resultSet.getString("descricao"),
                        resultSet.getDouble("preco"),
                        resultSet.getInt("quantidade_estoque"),
                        resultSet.getString("data_criacao"),
                        resultSet.getString("data_modificacao"),
                });
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro ao carregar dados: " + e.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    private void editar() {
        int row = table.getSelectedRow();
        if (row != -1) {
            int produtoId = (int) tableModel.getValueAt(row, 0);
            String codigo = (String) tableModel.getValueAt(row, 1);
            String nome = (String) tableModel.getValueAt(row, 2);
            String descricao = (String) tableModel.getValueAt(row, 3);
            double preco = (double) tableModel.getValueAt(row, 4);
            int quantidade = (int) tableModel.getValueAt(row, 5);

            JTextField codigoField = new JTextField(codigo);
            JTextField nomeField = new JTextField(nome);
            JTextField descricaoField = new JTextField(descricao);
            JTextField precoField = new JTextField(String.valueOf(preco));
            JTextField quantidadeField = new JTextField(String.valueOf(quantidade));

            JPanel panel = new JPanel(new GridLayout(0, 1));
            panel.add(new JLabel("Código:"));
            panel.add(codigoField);
            panel.add(new JLabel("Nome:"));
            panel.add(nomeField);
            panel.add(new JLabel("Descrição:"));
            panel.add(descricaoField);
            panel.add(new JLabel("Preço:"));
            panel.add(precoField);
            panel.add(new JLabel("Quantidade em Estoque:"));
            panel.add(quantidadeField);

            int result = JOptionPane.showConfirmDialog(this, panel, "Editar Produto", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if (result == JOptionPane.OK_OPTION) {
                try (Connection connection = DatabaseConnection.getConnection();
                     PreparedStatement ps = connection.prepareStatement("UPDATE produtos SET codigo = ?, nome = ?, descricao = ?, preco = ?, quantidade_estoque = ? WHERE id = ?")) {

                    ps.setString(1, codigoField.getText());
                    ps.setString(2, nomeField.getText());
                    ps.setString(3, descricaoField.getText());
                    ps.setDouble(4, Double.parseDouble(precoField.getText()));
                    ps.setInt(5, Integer.parseInt(quantidadeField.getText()));
                    ps.setInt(6, produtoId);
                    ps.executeUpdate();
                    carregarDados();

                    JOptionPane.showMessageDialog(this, "Produto atualizado com sucesso!");
                } catch (SQLException | NumberFormatException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Erro ao atualizar produto: " + ex.getMessage(),
                            "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Selecione um produto para editar.", "Aviso", JOptionPane.WARNING_MESSAGE);
        }
    }


    private void excluirproduto() {
        int row = table.getSelectedRow();
        if (row != -1) {
            int produtoId = (int) tableModel.getValueAt(row, 0);
            int confirm = JOptionPane.showConfirmDialog(this, "Deseja realmente excluir este produto?",
                    "Confirmar exclusão", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                try (Connection connection = DatabaseConnection.getConnection();
                     PreparedStatement ps = connection.prepareStatement("DELETE FROM produtos WHERE id = ?")) {
                    ps.setInt(1, produtoId);
                    ps.executeUpdate();
                    tableModel.removeRow(row);
                    JOptionPane.showMessageDialog(this, "Produto excluído com sucesso!");
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Erro ao excluir produto: " + ex.getMessage(),
                            "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Selecione um produto para excluir.", "Aviso", JOptionPane.WARNING_MESSAGE);
        }
    }


    private void novoProduto() {
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
        SwingUtilities.invokeLater(() -> new CadastroProduto().setVisible(true));
    }
}
