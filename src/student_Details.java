import net.proteanit.sql.DbUtils;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.text.SimpleDateFormat;

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
    private JTextField txtTotalamountpaid;
    private JTextField txtOutstandingbalance;
    private JTextField txtduedate;
    private JTextField txtclearancestatus;
    private JTextField txtDateOfLastRecord;
    private JButton cancelButton;

    public static void main(String[] args) {
        JFrame frame = new JFrame("student_Details");
        frame.setContentPane(new student_Details().Main);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }


    Connection  con;
    PreparedStatement pst;

    public void connect() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/Javaconnectivity?zeroDateTimeBehavior=convertToNull", "root", "");
            jdbc:mysql://localhost:3306/your_database?zeroDateTimeBehavior=convertToNull
            System.out.println("Successful");
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();

        } catch (SQLException ex) {
            ex.printStackTrace(); // this is a comment
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
        final int fee = 50000;
        //listener for automatic calculation of outstanding balance and clearance status
        txtTotalamountpaid.getDocument().addDocumentListener(new DocumentListener() {
            private void calculateAndDisplayOutstandingBalance(){
                try{
                    int totalAmountPaid = Integer.parseInt(txtTotalamountpaid.getText());
                    int outstandingBalance =  fee - totalAmountPaid;

                    // update Outstanding Balance
                    txtOutstandingbalance.setText(String.valueOf(outstandingBalance));// Display in txtoutstandingBalance
                    //update clearance Status

                    if(outstandingBalance == 0){
                        txtclearancestatus.setText("Cleared");
                    }else{
                        txtclearancestatus.setText("Not cleared");
                    }
                } catch (NumberFormatException e) {
                    txtOutstandingbalance.setText("0");//default to 0 if input is invalid
                    txtclearancestatus.setText("Not Cleared");
                }
            }
            @Override
            public void insertUpdate(DocumentEvent e) {
                calculateAndDisplayOutstandingBalance();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                calculateAndDisplayOutstandingBalance();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                calculateAndDisplayOutstandingBalance();
            }
        });
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name, regnumber, school, department, Clearance_status, due_date, dateOfLastRecordUpdate;
                Integer Total_amount_paid = 0, Outstanding_balance = 0;
                Double yearOfStudy;

                name = txtname.getText();
                regnumber = txtregnumber.getText();
                yearOfStudy = Double.valueOf(txtyear.getText());
                school = txtschool.getText();
                department = txtdepartment.getText();
                Clearance_status = txtclearancestatus.getText();



                try {
                    due_date = txtduedate.getText();
                    dateOfLastRecordUpdate =  txtDateOfLastRecord.getText();

                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                    java.util.Date parsedDueDate = format.parse(due_date);
                    java.sql.Date sqlDueDate = new java.sql.Date(parsedDueDate.getTime());

                    // parse and format 'dateOfLastRecordUpdate'
                    java.util.Date parsedRecordDate = format.parse(dateOfLastRecordUpdate);
                    java.sql.Date sqlRecordDate = new java.sql.Date(parsedRecordDate.getTime());


                    Total_amount_paid = Integer.parseInt(txtTotalamountpaid.getText());
                    Outstanding_balance = Integer.parseInt(txtOutstandingbalance.getText());

                    due_date = txtduedate.getText();
                    dateOfLastRecordUpdate = txtDateOfLastRecord.getText();


                    pst = con.prepareStatement("INSERT INTO Student_details(Name, Reg_Number, Year_of_study, School, Department, Total_amount_paid, Outstanding_balance, Due_date, Clearance_status, Date_of_last_record_update)values(?,?,?,?,?,?,?,?,?,?)");
                    pst.setString(1, name);
                    pst.setString(2, regnumber);
                    pst.setDouble(3, yearOfStudy);
                    pst.setString(4, school);
                    pst.setString(5, department);
                    pst.setInt(6, Total_amount_paid);
                    pst.setInt(7, Outstanding_balance);
                    pst.setDate(8,sqlDueDate);
                    pst.setString(9, Clearance_status);
                    pst.setDate(10, sqlRecordDate);
                    pst.executeUpdate();
                    JOptionPane.showMessageDialog(null, "Record Added!!");
                    table_load();
                    txtname.setText("");
                    txtregnumber.setText("");
                    txtyear.setText("");
                    txtschool.setText("");
                    txtdepartment.setText("");
                    txtclearancestatus.setText("");
                    txtTotalamountpaid.setText("");
                    txtOutstandingbalance.setText("");
                    txtduedate.setText("");
                    txtDateOfLastRecord.setText("");

                } catch (SQLException | java.text.ParseException ex) {
                    ex.printStackTrace();
                }
            }
        });
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                try {
                    String id = txtid.getText();

                    pst = con.prepareStatement("SELECT Name,Reg_Number,Year_of_study,School,Department, Total_amount_paid, Outstanding_balance, Due_date, Clearance_status, Date_of_last_record_update FROM Student_details WHERE id = ?");
                    pst.setString(1, id);
                    ResultSet rs = pst.executeQuery();

                    if(rs.next()==true){
                        String Name = rs.getString(1);
                        String reg_number = rs.getString(2);
                        Double yearofStudy = rs.getDouble(3);
                        String school = rs.getString(4);
                        String department = rs.getString(5);
                        int Total_amount_paid = rs.getInt(6);
                        int Outstanding_balance = rs.getInt(7);
                        Date Due_date = rs.getDate(8);
                        String Clearance_status = rs.getString(9);
                        Date Date_of_last_record_update = rs.getDate(10);

                        txtname.setText(Name);
                        txtregnumber.setText(reg_number);
                        txtyear.setText(String.valueOf(yearofStudy));
                        txtschool.setText(school);
                        txtdepartment.setText(department);
                        txtTotalamountpaid.setText(String.valueOf(Total_amount_paid));
                        txtOutstandingbalance.setText(String.valueOf(Outstanding_balance));
                        txtduedate.setText(String.valueOf(Due_date));
                        txtclearancestatus.setText(String.valueOf(Clearance_status));
                        txtDateOfLastRecord.setText(String.valueOf(Date_of_last_record_update));



                    }
                    else{
                        txtname.setText("");
                        txtregnumber.setText("");
                        txtyear.setText("");
                        txtschool.setText("");
                        txtdepartment.setText("");
                        txtTotalamountpaid.setText("");
                        txtOutstandingbalance.setText("");
                        txtduedate.setText("");
                        txtclearancestatus.setText("");
                        txtDateOfLastRecord.setText("");
                        JOptionPane.showMessageDialog(null,"Invalid Student Number");
                    }


                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String studentid, Name, regNumber, School, Department, Clearance_status, dueDate, dateOfLastRecordUpdate;
                Integer Total_amount_paid, Outstanding_balance;
                Double yearofStudy;

                Name = txtname.getText();
                regNumber = txtregnumber.getText();
                yearofStudy = Double.valueOf(txtyear.getText());
                School = txtschool.getText();
                Department = txtdepartment.getText();
                Clearance_status = txtclearancestatus.getText();
                Total_amount_paid = Integer.valueOf(txtTotalamountpaid.getText());
                Outstanding_balance = Integer.valueOf(txtOutstandingbalance.getText());
                dueDate = txtduedate.getText();
                dateOfLastRecordUpdate = txtDateOfLastRecord.getText();
                studentid = txtid.getText();



                try{
                    //prepare the sql update statement with the correct order of parameters
                    pst = con.prepareStatement("UPDATE Student_details set Name = ?,Reg_Number = ?,Year_of_study = ?,School = ?,Department = ?, Total_amount_paid = ?, Outstanding_balance = ?, Due_date = ?, Clearance_status = ?, Date_of_last_record_update = ? WHERE id = ? ");
                    pst.setString(1, Name);
                    pst.setString(2, regNumber);
                    pst.setDouble(3, yearofStudy);
                    pst.setString(4,School);
                    pst.setString(5,Department);
                    pst.setInt(6, Outstanding_balance);
                    pst.setInt(7,Total_amount_paid);
                    pst.setString(8,dueDate);
                    pst.setString(9, Clearance_status);
                    pst.setString(10, dateOfLastRecordUpdate);
                    pst.setString(11,studentid);
                    pst.executeUpdate();
                    JOptionPane.showMessageDialog(null, "Record Updated");

                    //clear input fields after update
                    table_load();
                    txtname.setText("");
                    txtregnumber.setText("");
                    txtyear.setText("");
                    txtschool.setText("");
                    txtdepartment.setText("");
                    txtTotalamountpaid.setText("");
                    txtOutstandingbalance.setText("");
                    txtduedate.setText("");
                    txtclearancestatus.setText("");
                    txtDateOfLastRecord.setText("");
                    txtid.setText("");
                    txtname.requestFocus();

                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String studentid;

                studentid = txtid.getText();

                try{
                    pst = con.prepareStatement("DELETE FROM Student_details WHERE id = ?");

                    pst.setString(1, studentid);

                    pst.executeUpdate();
                    JOptionPane.showMessageDialog(null, "Record Deleted");
                    table_load();
                    txtname.setText("");
                    txtregnumber.setText("");
                    txtyear.setText("");
                    txtschool.setText("");
                    txtdepartment.setText("");
                    txtTotalamountpaid.setText("");
                    txtOutstandingbalance.setText("");
                    txtduedate.setText("");
                    txtclearancestatus.setText("");
                    txtDateOfLastRecord.setText("");
                    txtid.setText("");
                    txtname.requestFocus();


                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                txtid.setText("");
                txtname.setText("");
                txtclearancestatus.setText("");
                txtduedate.setText("");
                txtOutstandingbalance.setText("");
                txtDateOfLastRecord.setText("");
                txtTotalamountpaid.setText("");
                txtdepartment.setText("");
                txtschool.setText("");
                txtyear.setText("");
                txtregnumber.setText("");

                //reset focus to the first field
                txtname.requestFocus();

                JOptionPane.showMessageDialog(null, "inputs Cleared Successfully");
            }
        });
    }
}


