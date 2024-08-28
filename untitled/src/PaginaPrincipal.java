import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PaginaPrincipal extends JFrame {
    private JPanel panel1;
    private JButton cadastrosButton;
    private JButton vendasButton;
    private JButton receberButton;
    private JButton despesasButton;
    private JButton sairButton;
    private JButton ferramentasButton;
    private JButton relatóriosButton;
    private JPopupMenu cadastroMenu;
    private JPopupMenu sairMenu;
    private JButton produtosButton;


    public PaginaPrincipal() {
        // Configurações da  janela
        setTitle("Análise de Vendas 1.1");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();

        //Botões add
        panel1 = new JPanel();
        panel1.add(cadastrosButton = new JButton("Cadastros"));
        panel1.add(vendasButton = new JButton("Vendas"));
        panel1.add(receberButton = new JButton("Receber"));
        panel1.add(despesasButton = new JButton("Despesas"));
        panel1.add(produtosButton = new JButton("Produtos"));
        panel1.add(ferramentasButton = new JButton("Ferramentas"));
        panel1.add(relatóriosButton = new JButton("Relatórios"));
        panel1.add(sairButton = new JButton("Sair"));
        add(panel1, BorderLayout.NORTH);
        panel1.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));

        cadastroMenu = new JPopupMenu();
        JMenuItem UsuarioItem = new JMenuItem("Cadastro de Usuário");
        JMenuItem ClienteItem = new JMenuItem("Cadastro de Cliente");

        cadastroMenu.add(UsuarioItem);
        cadastroMenu.add(ClienteItem);

        cadastrosButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cadastroMenu.show(cadastrosButton, 0, cadastrosButton.getHeight());
            }
        });

        sairMenu = new JPopupMenu();
        JMenuItem TrocarUsuario = new JMenuItem("Trocar de Usuário");
        JMenuItem Sair = new JMenuItem("Sair");

        sairMenu.add(TrocarUsuario);
        sairMenu.add(Sair);

        sairButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sairMenu.show(sairButton, 0, sairButton.getHeight());
            }
        });

        Sair.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        TrocarUsuario.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                LoginForm LoginForm = new LoginForm();
                LoginForm.setVisible(true);
                dispose();
            }
        });

        if (gd.isFullScreenSupported()) {
            gd.setFullScreenWindow(this);
        } else {
            setExtendedState(JFrame.MAXIMIZED_BOTH);
        }
        vendasButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
    }
    public static void main(String[] args) {
        PaginaPrincipal principal = new PaginaPrincipal();
        principal.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        principal.setVisible(true);
    }
}
