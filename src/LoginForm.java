import javax.swing.*;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.LocalDateTime;

public class LoginForm extends JFrame {
    private JPanel MainLoginForm;
    private JTextField Login_Form;
    private JButton btn_Ok;
    private JButton btn_cancelar;
    private JPasswordField passwordField1;

    public LoginForm() {
        setContentPane(MainLoginForm); // Define o conteúdo da janela
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Fecha apenas a janela atual
        setBounds(500, 300, 300, 150);
        setResizable(false);
        setUndecorated(true); // Remove a barra de título
        setLocationRelativeTo(null);

        btn_Ok.addAncestorListener(new AncestorListener() {
            @Override
            public void ancestorAdded(AncestorEvent event) {
                btn_Ok.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                        .put(KeyStroke.getKeyStroke("ENTER"), "enterPressed");
                btn_Ok.getActionMap().put("enterPressed", new AbstractAction() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        btn_Ok.doClick();
                    }
                });
            }

            @Override
            public void ancestorRemoved(AncestorEvent event) {}

            @Override
            public void ancestorMoved(AncestorEvent event) {}
        });


        btn_Ok.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = Login_Form.getText();
                String password = new String(passwordField1.getPassword());


                DatabaseConnection dbConnection = new DatabaseConnection();

                if (dbConnection.authenticateUser(username, password)) {
                    // Capturar a data e hora do acesso
                    LocalDateTime dataUltimoAcesso = LocalDateTime.now();

                    // Capturar o endereço IP da máquina
                    String enderecoIP = "";
                    try {
                        InetAddress inetAddress = InetAddress.getLocalHost();
                        enderecoIP = inetAddress.getHostAddress();
                    } catch (UnknownHostException ex) {
                        ex.printStackTrace();
                    }

                    // Atualizar o último acesso e endereço IP
                    dbConnection.updateLastAccess(username, dataUltimoAcesso, enderecoIP);

                    // Abrir a página principal
                    PaginaPrincipal PaginaPrincipal = new PaginaPrincipal();
                    PaginaPrincipal.setVisible(true);
                    dispose(); // Fecha a tela de login
                } else {
                    JOptionPane.showMessageDialog(null, "Usuário ou senha incorretos.");
                }
            }
        });

        btn_cancelar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0); // Fecha o aplicativo
            }
        });
    }

    public static void main(String[] args) {
        // Cria e exibe a tela de login
        LoginForm loginForm = new LoginForm();
        loginForm.setVisible(true);
    }
}
