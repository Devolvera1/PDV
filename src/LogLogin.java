import javax.swing.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class LogLogin extends JFrame {
    public LogLogin() {
        setTitle("Log de login");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }



    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            LogLogin frame = new LogLogin();
            frame.setVisible(true);
            frame.setAlwaysOnTop(false);
        });
    }
}