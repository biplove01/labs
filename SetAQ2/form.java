import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class Form extends JFrame {

    private JTextField idField = new JTextField(10);
    private JTextField userField = new JTextField(10);
    private JPasswordField passField = new JPasswordField(10);
    private JPasswordField repassField = new JPasswordField(10);
    private JRadioButton male = new JRadioButton("Male");
    private JRadioButton female = new JRadioButton("Female");
    private JCheckBox java = new JCheckBox("Java");
    private JCheckBox python = new JCheckBox("Python");
    private JComboBox<String> country = new JComboBox<>(new String[]{"Select","USA","India","UK"});
    private JTextArea display = new JTextArea(5, 30);

    public Form() {
        createTable();
        setLayout(new FlowLayout());

        add(new JLabel("ID:")); add(idField);
        add(new JLabel("Username:")); add(userField);
        add(new JLabel("Password:")); add(passField);
        add(new JLabel("RePass:")); add(repassField);
        add(new JLabel("Gender:")); add(male); add(female);
        add(new JLabel("Course:")); add(java); add(python);
        add(new JLabel("Country:")); add(country);

        JButton submit = new JButton("Submit");
        JButton reset = new JButton("Reset");
        JButton show = new JButton("Show");
        add(submit); add(reset); add(show);
        add(new JScrollPane(display));

        submit.addActionListener(e -> submit());
        reset.addActionListener(e -> reset());
        show.addActionListener(e -> showRecords());

        setSize(400, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:h2:./userdb", "sa", "");
    }

    private void createTable() {
        try (Connection con = getConnection()) {
            con.createStatement().execute(
                "CREATE TABLE IF NOT EXISTS users (" +
                "id INT PRIMARY KEY, " +
                "username VARCHAR(50), " +
                "password VARCHAR(50), " +
                "gender VARCHAR(10), " +
                "course VARCHAR(100), " +
                "country VARCHAR(50))"
            );
        } catch (SQLException e) {
            System.out.println("Table creation error: " + e.getMessage());
        }
    }

    private void submit() {
        String pass = new String(passField.getPassword());
        String repass = new String(repassField.getPassword());

        if (idField.getText().isEmpty() || userField.getText().isEmpty() || pass.isEmpty()) {
            JOptionPane.showMessageDialog(this, "ID, Username & Password required!");
            return;
        }
        if (!pass.equals(repass)) {
            JOptionPane.showMessageDialog(this, "Passwords don't match!");
            return;
        }
        if (!male.isSelected() && !female.isSelected()) {
            JOptionPane.showMessageDialog(this, "Select gender!");
            return;
        }

        String courses = (java.isSelected() ? "Java " : "") + (python.isSelected() ? "Python" : "");
        String gender = male.isSelected() ? "Male" : "Female";

        try (Connection con = getConnection()) {
            PreparedStatement ps = con.prepareStatement("INSERT INTO users(id,username,password,gender,course,country) VALUES(?,?,?,?,?,?)");
            ps.setInt(1, Integer.parseInt(idField.getText()));
            ps.setString(2, userField.getText());
            ps.setString(3, pass);
            ps.setString(4, gender);
            ps.setString(5, courses);
            ps.setString(6, (String)country.getSelectedItem());
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Saved!");
            reset();
        } catch(Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void showRecords() {
        display.setText("");
        try (Connection con = getConnection()) {
            ResultSet rs = con.createStatement().executeQuery("SELECT * FROM users");
            while(rs.next()) {
                display.append(rs.getInt("id") + " | " + rs.getString("username") + " | " +
                              rs.getString("gender") + " | " + rs.getString("course") + "\n");
            }
        } catch(Exception ex) {
            display.setText("Error: " + ex.getMessage());
        }
    }

    private void reset() {
        idField.setText(""); userField.setText(""); passField.setText(""); repassField.setText("");
        male.setSelected(false); female.setSelected(false);
        java.setSelected(false); python.setSelected(false);
        country.setSelectedIndex(0); display.setText("");
    }

    public static void main(String[] args) {
        new Form();
    }
}
