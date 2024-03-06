import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

// Import statements remain the same as in the previous code

public class InformationFrame extends JFrame {
    private JTextField incidentNumberField;
    private JTextField descriptionField;
    private JTextField actionRequiredField;
    private JButton addButton;
    private JTable incidentTable;
    private DefaultTableModel incidentTableModel;

    private JTextField mimIncidentNumberField;
    private JTextField mimDescriptionField;
    private JTextField mimActionRequiredField;
    private JButton mimAddButton;
    private JTable mimHandoverTable;
    private DefaultTableModel mimHandoverTableModel;

    private JTextField mailCategoryNumberField;
    private JTextField mailCategoryDescriptionField;
    private JTextField mailCategoryActionRequiredField;
    private JButton mailCategoryAddButton;
    private JTable mailCategoryTable;
    private DefaultTableModel mailCategoryTableModel;

    private JTextField rackspaceNumberField;
    private JTextField rackspaceDescriptionField;
    private JTextField rackspaceActionRequiredField;
    private JButton rackspaceAddButton;
    private JTable rackspaceTable;
    private DefaultTableModel rackspaceTableModel;

    public InformationFrame() {
        setTitle("Shift Handover Table");
        setSize(1200, 600); // Adjust the window size as needed
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(1, 4)); // Use a 1x4 grid layout to accommodate all tables

        JPanel incidentPanel = createTablePanel("incident");
        JPanel mimHandoverPanel = createTablePanel("mim_handover");
        JPanel mailCategoryPanel = createTablePanel("mail_category");
        JPanel rackspacePanel = createTablePanel("rackspace_handover");

        add(incidentPanel);
        add(mimHandoverPanel);
        add(mailCategoryPanel);
        add(rackspacePanel);
    }

    private JPanel createTablePanel(String tableName) {
        JPanel tablePanel = new JPanel(new BorderLayout());

        JPanel inputPanel = new JPanel(new GridLayout(5, 2));

        JLabel numberLabel = new JLabel("Number:");
        JLabel descriptionLabel = new JLabel("Description:");
        JLabel actionRequiredLabel = new JLabel("Action Required:");

        JTextField numberField = new JTextField();
        JTextField descriptionField = new JTextField();
        JTextField actionRequiredField = new JTextField();
        JButton addButton = new JButton("Add Entry");
        JButton deleteButton = new JButton("Delete Entry");

        inputPanel.add(numberLabel);
        inputPanel.add(numberField);
        inputPanel.add(descriptionLabel);
        inputPanel.add(descriptionField);
        inputPanel.add(actionRequiredLabel);
        inputPanel.add(actionRequiredField);
        inputPanel.add(new JLabel());
        inputPanel.add(addButton);
        inputPanel.add(deleteButton);

        String[] columnNames = {"Number", "Description", "Action Required"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        JTable table = new JTable(tableModel);
        JScrollPane tableScrollPane = new JScrollPane(table);

        table.setBackground(Color.LIGHT_GRAY);
        table.setForeground(Color.BLACK);

        tablePanel.add(inputPanel, BorderLayout.NORTH);
        tablePanel.add(tableScrollPane, BorderLayout.CENTER);

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String number = numberField.getText();
                String description = descriptionField.getText();
                String actionRequired = actionRequiredField.getText();

                if (addEntry(tableName, number, description, actionRequired, tableModel)) {
                    tableModel.addRow(new Object[]{number, description, actionRequired});
                } else {
                    JOptionPane.showMessageDialog(null, "Failed to add entry. Please try again.");
                }

                numberField.setText("");
                descriptionField.setText("");
                actionRequiredField.setText("");
            }
        });
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow != -1) {
                    tableModel.removeRow(selectedRow);
                } else {
                    JOptionPane.showMessageDialog(null, "Select a row to delete.");
                }
            }
        });

        return tablePanel;
    }

    private boolean addEntry(String tableName, String number, String description, String actionRequired, DefaultTableModel tableModel) {
        String jdbcUrl = "jdbc:mysql://localhost:3306/example";
        String dbUser = "root";
        String dbPassword = "tanmay@12345";

        try (Connection connection = DriverManager.getConnection(jdbcUrl, dbUser, dbPassword)) {
            String query = "INSERT INTO " + tableName + " (number, description, action_required) VALUES (?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, number);
            preparedStatement.setString(2, description);
            preparedStatement.setString(3, actionRequired);
            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }

            InformationFrame infoFrame = new InformationFrame();
            infoFrame.setVisible(true);
        });
    }
}

