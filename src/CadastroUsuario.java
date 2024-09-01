import javax.swing.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class CadastroUsuario extends JFrame {
    public CadastroUsuario() {
        setTitle("Cadastro de Usuário");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }



    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            CadastroUsuario frame = new CadastroUsuario();
            frame.setVisible(true);
            frame.setAlwaysOnTop(false);
        });
    }
}