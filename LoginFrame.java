import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginFrame extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton registerButton;
    private JLabel imageLabel;

    public LoginFrame() {
        setTitle("Login Example");
        setSize(1200, 950);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        JPanel contentPanel = new JPanel();
        setLayout(new GridLayout(6, 2));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        imageLabel = new JLabel();
        ImageIcon imageIcon = new ImageIcon("/Users/tanmaykale/IdeaProjects/JFrame_connectivity/src/logo.png"); // Replace with the actual path to your image
        imageIcon = new ImageIcon(imageIcon.getImage().getScaledInstance(120, 120, Image.SCALE_SMOOTH));
        imageLabel.setIcon(imageIcon);
        imageLabel.setHorizontalAlignment(JLabel.CENTER);

        JLabel usernameLabel = new JLabel("Username:");
        JLabel passwordLabel = new JLabel("Password");
        usernameField = new JTextField();
        passwordField = new JPasswordField();
        loginButton = new JButton("Login");
        registerButton = new JButton("Register");

        add(imageLabel);
        add(new JLabel()); // Placeholder
        add(usernameLabel);
        add(usernameField);
        add(passwordLabel);
        add(passwordField);
        add(new JLabel());
        add(loginButton);
        add(new JLabel());
        add(registerButton);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                char[] password = passwordField.getPassword();
                if (isValidLogin(username, new String(password))) {
//                    JOptionPane.showMessageDialog(null, "Login Successful");
                    openInformationFrame();
                } else {
                    JOptionPane.showMessageDialog(null, "Login Failed. Please try again.");
                }
                passwordField.setText("");
            }
        });

        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                char[] password = passwordField.getPassword();
                if (registerUser(username, new String(password))) {
                    JOptionPane.showMessageDialog(null, "Registration Successful");
                } else {
                    JOptionPane.showMessageDialog(null, "Registration Failed. Please try again.");
                }
                // Clear the input fields after registration
                usernameField.setText("");
                passwordField.setText("");
            }
        });
    }

    private boolean isValidLogin(String username, String password) {
        String jdbcUrl = "jdbc:mysql://localhost:3306/example?useSSL=false";
        String dbUser = "root";
        String dbPassword = "tanmay@12345";

        try {
            Connection connection = DriverManager.getConnection(jdbcUrl, dbUser, dbPassword);
            String query = "SELECT * FROM users WHERE username = ? AND password = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean registerUser(String username, String password) {
        String jdbcUrl = "jdbc:mysql://localhost:3306/example?useSSL=false";
        String dbUser = "root";
        String dbPassword = "tanmay@12345";

        try {
            Connection connection = DriverManager.getConnection(jdbcUrl, dbUser, dbPassword);
            String query = "INSERT INTO users (username, password) VALUES (?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    private void openInformationFrame() {
        InformationFrame mainFrame = new InformationFrame();
        mainFrame.setVisible(true);
        dispose(); // Close the login frame
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }

            LoginFrame loginFrame = new LoginFrame();
            loginFrame.setVisible(true);
        });
    }
}
