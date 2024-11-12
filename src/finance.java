import net.proteanit.sql.DbUtils;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

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
        btnsave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                final int fee = 50000;
                String RegNumber, Clearance_status, due_date, dateOfLastRecordUpdate;
                Integer Total_amount_paid, Outstanding_balance;

                RegNumber = txtreg_number.getText();
                Clearance_status = txtclearancestatus.getText();
                Total_amount_paid = Integer.valueOf(txtTotalamountpaid.getText());
                Outstanding_balance = fee - Integer.valueOf(txtTotalamountpaid.getText());
                due_date = txtduedate.getText();
                dateOfLastRecordUpdate = txtDateOfLastRecord.getText();

                try {
                    pst = con.prepareStatement("INSERT INTO finance(Reg_number, Total_amount_paid, Outstanding_balance, Due_date, Clearance_status, Date_of_last_record_update)values(?,?,?,?,?,?)");
                    pst.setString(1, RegNumber);
                    pst.setString(2, String.valueOf(Total_amount_paid));
                    pst.setString(3, String.valueOf(Outstanding_balance));
                    pst.setString(4, due_date);
                    pst.setString(5, Clearance_status);
                    pst.setString(6,dateOfLastRecordUpdate);
                    pst.executeUpdate();
                    JOptionPane.showMessageDialog(null, "Record Added!!");
                    table_load();
                    txtreg_number.setText("");
                    txtclearancestatus.setText("");
                    txtTotalamountpaid.setText("");
                    txtOutstandingbalance.setText("");
                    txtduedate.setText("");
                    txtDateOfLastRecord.setText("");

                } catch (SQLException ex) {
                    ex.printStackTrace();
                }

            }
        });
    }









}


