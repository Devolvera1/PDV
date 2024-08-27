import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginForm extends JFrame {
    private JPanel MainLoginForm;
    private JTextField Login_Form;
    private JButton btn_Ok;
    private JButton btn_cancelar;
    private JPasswordField passwordField1;

    public LoginForm() {
        btn_Ok.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = Login_Form.getText();
                String password = new String(passwordField1.getPassword());

                DatabaseConnection dbConnection = new DatabaseConnection();

                if (dbConnection.authenticateUser(username, password)) {
                    //Implementar a proxima pagina.
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(null, "Usuário ou senha incorretos.");
                }
            }
        });

        btn_cancelar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
    }

    public static void main(String[] args) {
        LoginForm h = new LoginForm();
        h.setContentPane(h.MainLoginForm);
        h.setBounds(500, 300, 300, 150);
        h.setResizable(false);
        h.setUndecorated(true);  // Remove a barra de título
        h.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        h.setLocationRelativeTo(null);
        h.setVisible(true);
    }
}
