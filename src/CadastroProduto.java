import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.sql.*;

public class CadastroProduto extends JFrame {

    public CadastroProduto() {
        // Defina o título da janela
        setTitle("Cadastro de Produto");

        // Configuração do tamanho da janela
        setSize(600, 400);

        // Fechar a aplicação ao clicar no botão de fechar
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Adicione componentes à sua janela aqui

    }

    public static void main(String[] args) {
        // Crie a instância do CadastroProduto e torne a janela visível
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new CadastroProduto().setVisible(true);
            }
        });
    }
}
