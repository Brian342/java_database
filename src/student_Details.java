import net.proteanit.sql.DbUtils;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class student_Details {
    private JTextField txtname;
    private JTextField txtregnumber;
    private JTextField txtyear;
    private JTable table1;
    private JTextField txtschool;
    private JTextField txtdepartment;
    private JButton saveButton;
    private JButton updateButton;
    private JButton deleteButton;
    private JButton searchButton;
    private JTextField txtid;
    private JPanel Main;
    private JScrollPane table_1;

    public static void main(String[] args) {
        JFrame frame = new JFrame("student_Details");
        frame.setContentPane(new student_Details().Main);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }


    Connection con;
    PreparedStatement pst;

    public void connect() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/Javaconnectivity", "root", "");
            System.out.println("Successful");
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }


    void table_load(){
        try{
            pst = con.prepareStatement("SELECT * FROM Student_details");
            ResultSet rs = pst.executeQuery();
            table1.setModel(DbUtils.resultSetToTableModel(rs));
        }catch (SQLException e){
            e.printStackTrace();
        }

    }



    public student_Details() {
        connect();
        table_load();
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name, regnumber, yearOfStudy, school, department;

                name = txtname.getText();
                regnumber = txtregnumber.getText();
                yearOfStudy = txtregnumber.getText();
                school = txtschool.getText();
                department = txtdepartment.getText();

                try {
                    pst = con.prepareStatement("INSERT INTO Student_details(Name,Reg_Number,Year_of_study,School,Department)values(?,?,?,?,?)");
                    pst.setString(1, name);
                    pst.setString(2, regnumber);
                    pst.setString(3, yearOfStudy);
                    pst.setString(4, school);
                    pst.setString(5, department);
                    pst.executeUpdate();
                    JOptionPane.showMessageDialog(null, "Record Added!!");
                    //table_load();
                    txtname.setText("");
                    txtregnumber.setText("");
                    txtyear.setText("");
                    txtschool.setText("");
                    txtdepartment.setText("");

                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                try {
                    String id = txtid.getText();

                    pst = con.prepareStatement("SELECT Name,Reg_Number,Year_of_study,School,Department FROM Student_details WHERE id = ?");
                    pst.setString(1, id);
                    ResultSet rs = pst.executeQuery();

                    if(rs.next()==true){
                        String Name = rs.getString(1);
                        String reg_number = rs.getString(2);
                        String yearofStudy = rs.getString(3);
                        String school = rs.getString(4);
                        String department = rs.getString(5);

                        txtname.setText(Name);
                        txtregnumber.setText(reg_number);
                        txtyear.setText(yearofStudy);
                        txtschool.setText(school);
                        txtdepartment.setText(department);



                    }
                    else{
                        txtname.setText("");
                        txtregnumber.setText("");
                        txtyear.setText("");
                        txtschool.setText("");
                        txtdepartment.setText("");
                        JOptionPane.showMessageDialog(null,"Invalid Student Number");
                    }


                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String studentid, Name, regNumber, yearofStudy, School, Department;

                Name = txtname.getText();
                regNumber = txtregnumber.getText();
                yearofStudy = txtregnumber.getText();
                School = txtschool.getText();
                Department = txtdepartment.getText();
                studentid = txtid.getText();

                try{
                    pst = con.prepareStatement("UPDATE Student_details set Name = ?,Reg_Number = ?,Year_of_study = ?,School = ?,Department = ? WHERE id = ? ");
                    pst.setString(1, Name);
                    pst.setString(2, regNumber);
                    pst.setString(3, yearofStudy);
                    pst.setString(4,School);
                    pst.setString(5,Department);
                    pst.setString(6,studentid);
                    pst.executeUpdate();
                    JOptionPane.showMessageDialog(null, "Record Updated");
                    //table_load()
                    txtname.setText("");
                    txtregnumber.setText("");
                    txtyear.setText("");
                    txtschool.setText("");
                    txtdepartment.setText("");

                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
    }
}


