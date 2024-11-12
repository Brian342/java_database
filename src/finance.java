import net.proteanit.sql.DbUtils;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class finance {
    private JTextField txtreg_number;
    private JTextField txtTotalamountpaid;
    private JTextField txtOutstandingbalance;
    private JTextField txtduedate;
    private JTextField txtclearancestatus;
    private JTextField txtDateOfLastRecord;
    private JButton btnsave;
    private JButton btncancel;
    private JPanel financepanel;
    private JButton searchButton;
    private JTextField txtsearch;
    private JTable table2;
    private JButton updateButton;
    private JButton deleteButton;
    private JScrollPane table_2;




    public static void main(String[] args) {
        JFrame frame = new JFrame("Finance");
        frame.setContentPane(new finance().financepanel);
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
            pst = con.prepareStatement("SELECT * FROM finance");
            ResultSet rs = pst.executeQuery();
            table2.setModel(DbUtils.resultSetToTableModel(rs));
        }catch (SQLException e){
            e.printStackTrace();
        }

    }

    public finance() {
        connect();
        table_load();
        final int fee = 50000;
        //Listener for automatic calculation of outstanding balance and clearance status
        txtTotalamountpaid.getDocument().addDocumentListener(new DocumentListener() {
            private void calculateAndDisplayOutstandingBalance(){
                try{
                    int totalAmountPaid = Integer.parseInt(txtTotalamountpaid.getText());
                    int outstandingBalance = fee - totalAmountPaid;

                    // update Outstanding Balance

                    txtOutstandingbalance.setText(String.valueOf(outstandingBalance)); // Display in txtOutstadingBalance

                    //update Clearance Status
                    if(outstandingBalance == 0){
                        txtclearancestatus.setText("Cleared");
                    }else{
                        txtclearancestatus.setText("Not Cleared");
                    }
                }catch (NumberFormatException ex){
                    txtOutstandingbalance.setText("0"); // Default to 0 if input is invalid
                    txtclearancestatus.setText(("Not Cleared"));
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
        btnsave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String RegNumber, Clearance_status, due_date, dateOfLastRecordUpdate;
                Integer Total_amount_paid = 0, Outstanding_balance = 0;


                RegNumber = txtreg_number.getText();
                Clearance_status = txtclearancestatus.getText();
                try {
                  due_date = txtduedate.getText();
                  dateOfLastRecordUpdate = txtDateOfLastRecord.getText();

                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                    Date parsedDueDate =  format.parse(due_date);
                    java.sql.Date sqlDueDate = new java.sql.Date(parsedDueDate.getTime());

                    // Parse and format `dateOfLastRecordUpdate`
                    Date parsedRecordDate = format.parse(dateOfLastRecordUpdate);
                    java.sql.Date sqlRecordDate = new java.sql.Date(parsedRecordDate.getTime());


                    Total_amount_paid = Integer.parseInt(txtTotalamountpaid.getText());
                    Outstanding_balance = Integer.parseInt(txtOutstandingbalance.getText());

                due_date = txtduedate.getText();
                dateOfLastRecordUpdate = txtDateOfLastRecord.getText();

                    pst = con.prepareStatement("INSERT INTO finance(Reg_number, Total_amount_paid, Outstanding_balance, Due_date, Clearance_status, Date_of_last_record_update)values(?,?,?,?,?,?)");
                    pst.setString(1, RegNumber);
                    pst.setInt(2, Total_amount_paid);
                    pst.setInt(3, Outstanding_balance);
                    pst.setDate(4, sqlDueDate);
                    pst.setString(5, Clearance_status);
                    pst.setDate(6, sqlRecordDate);
                    pst.executeUpdate();
                    JOptionPane.showMessageDialog(null, "Record Added!!");

                    table_load();
                    txtreg_number.setText("");
                    txtclearancestatus.setText("");
                    txtTotalamountpaid.setText("");
                    txtOutstandingbalance.setText("");
                    txtduedate.setText("");
                    txtDateOfLastRecord.setText("");

                } catch (SQLException |  java.text.ParseException ex) {
                    ex.printStackTrace();
                }

            }
        });
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                try{
                    String id = txtsearch.getText();

                    pst = con.prepareStatement("SELECT Reg_number, Total_amount_paid, Outstanding_balance, Due_date, Clearance_status, Date_of_last_record_update FROM finance WHERE id = ?");
                    pst.setString(1, id);
                    ResultSet rs = pst.executeQuery();

                    if (rs.next()==true){
                        String Reg_number = rs.getString(1);
                        int Total_amount_paid = rs.getInt(2);
                        int Outstanding_balance = rs.getInt(3);
                        Date Due_date = rs.getDate(4);
                        String Clearance_status = rs.getString(5);
                        Date Date_of_last_record_update = rs.getDate(6);

                        txtreg_number.setText(Reg_number);
                        txtTotalamountpaid.setText(String.valueOf(Total_amount_paid));
                        txtOutstandingbalance.setText(String.valueOf(Outstanding_balance));
                        txtduedate.setText(String.valueOf(Due_date));
                        txtclearancestatus.setText(String.valueOf(Clearance_status));
                        txtDateOfLastRecord.setText(String.valueOf(Date_of_last_record_update));


                    }
                    else{
                        txtreg_number.setText("");
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
                String financeid, Reg_number, Clearance_status,dueDate,dateOfLastRecordUpdate;
                Integer Total_amount_paid,Outstanding_balance;

                Reg_number = txtreg_number.getText();
                Clearance_status = txtclearancestatus.getText();
                Total_amount_paid = Integer.valueOf(txtTotalamountpaid.getText());
                Outstanding_balance = Integer.valueOf(txtOutstandingbalance.getText());
                dueDate = txtduedate.getText();
                dateOfLastRecordUpdate = txtDateOfLastRecord.getText();
                financeid = txtsearch.getText();

                try{
                    // prepare the sql update statement with the correct order of parameters
                    pst = con.prepareStatement("UPDATE finance set Reg_number = ?, Total_amount_paid = ?, Outstanding_balance = ?, Due_date = ?, Clearance_status = ?, Date_of_last_record_update = ? WHERE id = ? ");
                    pst.setString(1, Reg_number);
                    pst.setInt(2, Outstanding_balance);
                    pst.setInt(3, Total_amount_paid);
                    pst.setString(4,dueDate);
                    pst.setString(5, Clearance_status);
                    pst.setString(6,dateOfLastRecordUpdate);
                    pst.setString(7, financeid);
                    pst.executeUpdate();
                    JOptionPane.showMessageDialog(null, "Record Updated");

                    // clear input fields after update
                    table_load();
                    txtreg_number.setText("");
                    txtTotalamountpaid.setText("");
                    txtOutstandingbalance.setText("");
                    txtduedate.setText("");
                    txtclearancestatus.setText("");
                    txtDateOfLastRecord.setText("");
                    txtsearch.setText("");
                    txtreg_number.requestFocus();

                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String financialid;

                financialid = txtsearch.getText();

                try{
                    pst = con.prepareStatement("DELETE FROM finance WHERE id = ?");

                    pst.setString(1, financialid);

                    pst.executeUpdate();
                    JOptionPane.showMessageDialog(null, "Record Deleted");
                    txtreg_number.setText("");
                    txtTotalamountpaid.setText("");
                    txtOutstandingbalance.setText("");
                    txtduedate.setText("");
                    txtclearancestatus.setText("");
                    txtDateOfLastRecord.setText("");
                    txtsearch.setText("");
                    txtreg_number.requestFocus();

                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });
        btncancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Clear all the input fields
                txtreg_number.setText("");
                txtsearch.setText("");
                txtclearancestatus.setText("");
                txtduedate.setText("");
                txtTotalamountpaid.setText("");
                txtDateOfLastRecord.setText("");
                txtOutstandingbalance.setText("");

                //reset focus to the first field
                txtreg_number.requestFocus();

                JOptionPane.showMessageDialog(null, "Inputs Cleared Successfully");
            }
        });
    }









}


