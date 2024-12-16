import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class usercreation extends JDialog{
    private JTextField tfName;
    private JTextField tfphone;
    private JTextField tfEmail;
    private JPasswordField pfPassword;
    private JPasswordField pfconfirmpassword;
    private JButton btnRegister;
    private JButton btnCancel;
    private JPanel registerPanel;
    private JButton Login;
    private JButton btnNavigateToLogin; // Add the navigation button

    public usercreation(JFrame parent){
        super(parent);
        setTitle("Create a new Account");
        setContentPane(registerPanel);
        setMinimumSize(new Dimension(450, 474));
        setModal(true);
        setLocationRelativeTo(parent); // displays the dialog in the middle of the frame
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);


        btnRegister.addActionListener(e -> registerUser());
        btnCancel.addActionListener(e -> dispose());

        setVisible(true);

        Login.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // opens another form when clicked
                JFrame parentFrame = new JFrame();
                loginuserform loginuser = new loginuserform(parentFrame);
                loginuser.setVisible(true);

                dispose();

            }
        });
    }

    private void registerUser() {
        String name = tfName.getText();
        String email = tfEmail.getText();
        String phone = tfphone.getText();
        String password = String.valueOf(pfPassword.getPassword());
        String confirmPassword = String.valueOf(pfconfirmpassword.getPassword());
    
        if (name.isEmpty() || email.isEmpty() || phone.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please enter all fields",
                    "Try Again",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
    
        if (!password.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(this,
                    "Confirm password does not match",
                    "Try Again",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
    
        user = addUserToDatabase(name, email, phone, password);
        if (user != null) {
            // Show success message
            JOptionPane.showMessageDialog(this,
                    "Registration Successful for: " + user.name,
                    "Registration Success",
                    JOptionPane.INFORMATION_MESSAGE);
    
            // Navigate to loginuserform
            JFrame parentFrame = (JFrame) this.getParent(); // Cast the parent dialog
            new loginuserform(parentFrame);
    
            // Dispose of the current dialog
            dispose();
        } else {
            JOptionPane.showMessageDialog(this,
                    "Registration Failed. Please try again.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
    

    public User user;

    private User addUserToDatabase(String name, String email, String phone, String password){
        User user = null;
        final String DB_URL = "jdbc:mysql://localhost:3306/Javaconnectivity"; // sql connectivity
        final String USERNAME = "root";
        final String PASSWORD = "";

        try{
            Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
            //connected to database successfully
            Statement stmt = conn.createStatement();
            String sql = "INSERT INTO users (name, email, phone, password) " +
                    "VALUES (?, ?, ?, ?)";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, email);
            preparedStatement.setString(3, phone);
            preparedStatement.setString(4, password);

            //Insert row into the table
            int addedRows = preparedStatement.executeUpdate();
            if (addedRows > 0){
                user = new User();
                user.name = name;
                user.email = email;
                user.phone = phone;
                user.password = password;
            }
            stmt.close();
            conn.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return user;
    }

    public static void main(String[] args) {
        usercreation mylogin = new usercreation(null);
        User user = mylogin.user;
        if (user != null){
            JOptionPane.showMessageDialog(null, "Successful registration of: " + user.name, "Registration Success", JOptionPane.INFORMATION_MESSAGE);
        }
        else JOptionPane.showMessageDialog(null, "Registration Canceled", "Canceled Registration", JOptionPane.INFORMATION_MESSAGE);
//        System.out.println("Registration canceled");
    }
}
