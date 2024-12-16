import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

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


        btnOk.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String email = tfEmail.getText();
                String password = String.valueOf(pfPassword.getPassword());
        
                user = getAuthenticateUser(email, password);
        
                if (user != null) {
                    // Close the login form
                    dispose();
        
                    // Open the student_Details form
                    JFrame studentDetailsFrame = new JFrame("Student Details");
                    studentDetailsFrame.setContentPane(new student_Details().Main); // Load the student_Details panel
                    studentDetailsFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    studentDetailsFrame.pack();
                    studentDetailsFrame.setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(loginuserform.this,
                            "Email or Password invalid",
                            "Try Again",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        
        btnCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        setVisible(true); // makes dialog visible
    }
    public User user;
    private User getAuthenticateUser(String email, String password){
        User user = null;
        final String DB_URL = "jdbc:mysql://localhost:3306/Javaconnectivity"; // sql connectivity
        final String USERNAME = "root";
        final String PASSWORD = "";

        try{
            Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);

            // checks if user exist on the database
            Statement stmt = conn.createStatement();
            String sql = "SELECT * FROM users WHERE email=? AND password=?";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, password);

            ResultSet resultSet = preparedStatement.executeQuery();

            if(resultSet.next()){
                user = new User();
                user.name = resultSet.getString("name");
                user.email = resultSet.getString("email");
                user.phone = resultSet.getString("phone");
                user.password = resultSet.getString("password");
            }

            stmt.close();
            conn.close();

        }catch (Exception e){
            e.printStackTrace();
        }

        return user;
    }

    public static void main(String[] args) {
        loginuserform loginform = new loginuserform(null);
        JFrame parentFrame = new JFrame();
        new loginuserform(parentFrame);
        User user = loginform.user; // if the user credentials are correct, then the authentication is complete.
        if(user != null){
            JOptionPane.showMessageDialog(null,
                    "Successful Authentication of: " + user.name
                    + "\nEmail: " + user.email
                    +"\nphone:" + user.phone,
                    "\nLogin Success",
                    JOptionPane.INFORMATION_MESSAGE);



        }
        else{
            JOptionPane.showMessageDialog(null,"Authentication Canceled", "Authetication Canceled", JOptionPane.INFORMATION_MESSAGE);

        }
    }
}
