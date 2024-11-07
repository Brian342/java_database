import javax.swing.*;
import java.awt.*;

public class loginuserform extends JDialog{
    private JTextField tfEmail;
    private JPasswordField pfPassword;
    private JButton btnOk;
    private JButton btnCancel;
    private JPanel loginuserformpanel;

    public loginuserform(JFrame parent){
        super(parent);
        setTitle("loginUser");
        setContentPane(loginuserformpanel);
        setMinimumSize(new Dimension(450, 474));
        setModal(true);
        setLocationRelativeTo(parent); //display dialog in middle of frame
        setDefaultCloseOperation(DISPOSE_ON_CLOSE); // terminate dialog when click on close button
        setVisible(true); // makes dialog visible


    }

    public static void main(String[] args) {
        loginuserform loginform = new loginuserform(null);
    }
}
