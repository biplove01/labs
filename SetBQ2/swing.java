import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class RegistrationForm extends JFrame {
    private JTextField idTxt = new JTextField(10);
    private JTextField userTxt = new JTextField(10);
    private JPasswordField passTxt = new JPasswordField(10);
    private JPasswordField repassTxt = new JPasswordField(10);
    private JRadioButton male = new JRadioButton("Male");
    private JRadioButton female = new JRadioButton("Female");
    private JCheckBox javaChk = new JCheckBox("Java");
    private JCheckBox pythonChk = new JCheckBox("Python");
    private JComboBox<String> countryCombo = new JComboBox<>(new String[]{"Select", "USA", "India", "UK"});
    private JTextArea displayArea = new JTextArea(8, 30);
    private JButton submitBtn = new JButton("Submit");
    private JButton resetBtn = new JButton("Reset");
    private JButton showBtn = new JButton("Show Records");

    public RegistrationForm() {
        setTitle("Registration Form");
        setLayout(new FlowLayout());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Add components
        add(new JLabel("ID:")); add(idTxt);
        add(new JLabel("Username:")); add(userTxt);
        add(new JLabel("Password:")); add(passTxt);
        add(new JLabel("RePassword:")); add(repassTxt);
        add(new JLabel("Gender:")); add(male); add(female);
        add(new JLabel("Course:")); add(javaChk); add(pythonChk);
        add(new JLabel("Country:")); add(countryCombo);
        add(submitBtn); add(resetBtn); add(showBtn);
        add(new JScrollPane(displayArea));

        // Button actions
        submitBtn.addActionListener(e -> submitForm());
        resetBtn.addActionListener(e -> resetForm());
        showBtn.addActionListener(e -> displayRecords());

        // Create database table on startup
        createTable();

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private Connection getConnection() throws SQLException {
        // H2 database file stored in current directory
        return DriverManager.getConnection("jdbc:h2:./userdb", "sa", "");
    }

    private void createTable() {
        String sql = "CREATE TABLE IF NOT EXISTS users (" +
                     "id INT PRIMARY KEY, " +
                     "username VARCHAR(50), " +
                     "password VARCHAR(50), " +
                     "gender VARCHAR(10), " +
                     "course VARCHAR(100), " +
                     "country VARCHAR(50))";
        try (Connection con = getConnection();
             Statement stmt = con.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "DB Error: " + e.getMessage());
        }
    }

    private void submitForm() {
        String id = idTxt.getText().trim();
        String username = userTxt.getText().trim();
        String password = new String(passTxt.getPassword());
        String repassword = new String(repassTxt.getPassword());

        // Validations
        if (id.isEmpty() || username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "ID, Username and Password are required!");
            return;
        }
        if (!password.equals(repassword)) {
            JOptionPane.showMessageDialog(this, "Passwords do not match!");
            return;
        }
        if (!male.isSelected() && !female.isSelected()) {
            JOptionPane.showMessageDialog(this, "Please select gender!");
            return;
        }
        if (countryCombo.getSelectedIndex() == 0) {
            JOptionPane.showMessageDialog(this, "Please select a country!");
            return;
        }

        // Get courses
        StringBuilder courses = new StringBuilder();
        if (javaChk.isSelected()) courses.append("Java ");
        if (pythonChk.isSelected()) courses.append("Python");
        String courseStr = courses.toString().trim();
        if (courseStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Select at least one course!");
            return;
        }

        String gender = male.isSelected() ? "Male" : "Female";
        String country = (String) countryCombo.getSelectedItem();

        // Insert using PreparedStatement
        String insertSQL = "INSERT INTO users (id, username, password, gender, course, country) VALUES (?,?,?,?,?,?)";
        try (Connection con = getConnection();
             PreparedStatement pstmt = con.prepareStatement(insertSQL)) {
            pstmt.setInt(1, Integer.parseInt(id));
            pstmt.setString(2, username);
            pstmt.setString(3, password);
            pstmt.setString(4, gender);
            pstmt.setString(5, courseStr);
            pstmt.setString(6, country);
            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Registration Successful!");
            resetForm();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "ID must be a number!");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Database error: " + e.getMessage());
        }
    }

    private void displayRecords() {
        displayArea.setText("");
        String query = "SELECT * FROM users ORDER BY id";
        try (Connection con = getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            StringBuilder sb = new StringBuilder();
            sb.append("ID\tUsername\tGender\tCourse\tCountry\n");
            sb.append("--------------------------------------------\n");
            boolean hasData = false;
            while (rs.next()) {
                hasData = true;
                sb.append(rs.getInt("id")).append("\t")
                  .append(rs.getString("username")).append("\t")
                  .append(rs.getString("gender")).append("\t")
                  .append(rs.getString("course")).append("\t")
                  .append(rs.getString("country")).append("\n");
            }
            if (!hasData) sb.append("No records found.");
            displayArea.setText(sb.toString());
        } catch (SQLException e) {
            displayArea.setText("Error: " + e.getMessage());
        }
    }

    private void resetForm() {
        idTxt.setText("");
        userTxt.setText("");
        passTxt.setText("");
        repassTxt.setText("");
        male.setSelected(false);
        female.setSelected(false);
        javaChk.setSelected(false);
        pythonChk.setSelected(false);
        countryCombo.setSelectedIndex(0);
        displayArea.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new RegistrationForm());
    }
}
