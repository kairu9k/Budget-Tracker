import javax.swing.*;
import java.awt.*;
import java.util.List;

public class BudgetTrackerGUI extends JFrame {

    private UserCRUD userManager;
    private ExpensesCRUD expensesManager;

    public BudgetTrackerGUI() {
        userManager = new UserCRUD();
        expensesManager = new ExpensesCRUD();

        this.setTitle("Budget Tracking App");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(400, 400);
        this.setResizable(false);
        this.setLocationRelativeTo(null);

        JButton addUserButton = new JButton("Add User");
        addUserButton.addActionListener(e -> showAddUserDialog());

        JButton viewUsersButton = new JButton("View Users");
        viewUsersButton.addActionListener(e -> showViewUsersDialog());

        JButton addExpensesButton = new JButton("Add Expenses");
        addExpensesButton.addActionListener(e -> showAddExpensesDialog());

        JButton viewUserExpensesButton = new JButton("View User Expenses");
        viewUserExpensesButton.addActionListener(e -> showUserExpensesDialog());

        JButton editUserNameButton = new JButton("Edit User Name");
        editUserNameButton.addActionListener(e -> showEditUserNameDialog());

        JButton deleteUserButton = new JButton("Delete User");
        deleteUserButton.addActionListener(e -> showDeleteUserDialog());

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(6, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.add(addUserButton);
        panel.add(viewUsersButton);
        panel.add(addExpensesButton);
        panel.add(viewUserExpensesButton);
        panel.add(editUserNameButton);
        panel.add(deleteUserButton);

        add(panel);
    }

    private void showAddUserDialog() {
        JTextField userIdField = new JTextField();
        JTextField userNameField = new JTextField();
        JTextField budgetField = new JTextField();

        Object[] fields = {"User ID:", userIdField, "User Name:", userNameField, "Initial Budget:", budgetField};

        int result = JOptionPane.showConfirmDialog(null, fields, "Add New User", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            try {
                int userId = Integer.parseInt(userIdField.getText());
                String userName = userNameField.getText();
                int budget = Integer.parseInt(budgetField.getText());

                if (userName.isEmpty()) {
                    throw new ErrorHandler("Name must have a value. Thank you!");
                }

                addUser(userId, userName, budget);
            } catch (ErrorHandler err) {
                showErrorDialog("Error", err.getMessage());
            } catch (NumberFormatException e) {
                showErrorDialog("Error", "User ID and Budget must be valid numbers.");
            }
        }
    }

    private void showViewUsersDialog() {
        JTextArea usersTextArea = new JTextArea(getAllUsersAsString());
        usersTextArea.setEditable(false);

        JScrollPane scrollPane = new JScrollPane(usersTextArea);

        JOptionPane.showMessageDialog(null, scrollPane, "View Users", JOptionPane.PLAIN_MESSAGE);
    }

    private void showAddExpensesDialog() {
        JTextField userIdField = new JTextField();
        JTextField expensesAmountField = new JTextField();
        JTextField expensesRemarksField = new JTextField();

        Object[] fields = {"User ID:", userIdField, "Expenses Amount:", expensesAmountField, "Expenses Remarks:", expensesRemarksField};

        int result = JOptionPane.showConfirmDialog(null, fields, "Add Expenses", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            try {
                int userId = Integer.parseInt(userIdField.getText());
                int amount = Integer.parseInt(expensesAmountField.getText());
                String remarks = expensesRemarksField.getText();

                addExpenses(userId, amount, remarks);
            } catch (NumberFormatException e) {
                showErrorDialog("Error", "User ID and Expenses Amount must be valid numbers.");
            }
        }
    }

    private void showUserExpensesDialog() {
        JTextField userIdField = new JTextField();

        Object[] fields = {"User ID:", userIdField};

        int result = JOptionPane.showConfirmDialog(null, fields, "View User Expenses", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            try {
                int userId = Integer.parseInt(userIdField.getText());

                viewUserExpenses(userId);
            } catch (NumberFormatException e) {
                showErrorDialog("Error", "User ID must be a valid number.");
            }
        }
    }

    private void showEditUserNameDialog() {
        JTextField userIdField = new JTextField();
        JTextField newUserNameField = new JTextField();
    
        Object[] fields = {"User ID:", userIdField, "New User Name:", newUserNameField};
    
        int result = JOptionPane.showConfirmDialog(null, fields, "Edit User Name", JOptionPane.OK_CANCEL_OPTION);
    
        if (result == JOptionPane.OK_OPTION) {
            try {
                int userId = Integer.parseInt(userIdField.getText());
                String newUserName = newUserNameField.getText();
    
                if (newUserName.isEmpty()) {
                    throw new ErrorHandler("New User Name must have a value. Thank you!");
                }
    
                editUserName(userId, newUserName);
            } catch (ErrorHandler err) {
                showErrorDialog("Error", err.getMessage());
            } catch (NumberFormatException e) {
                showErrorDialog("Error", "User ID must be a valid number.");
            }
        }
    }

    private void showDeleteUserDialog() {

    }

    private String getAllUsersAsString() {
        StringBuilder usersString = new StringBuilder();
        userManager.getAllUsers().forEach(user -> {
            usersString.append("[ User ID: ").append(user.getId()).append(" || User Name: ").append(user.getName()).append(" || User Initial Budget: ").append(user.getBudget()).append("]\n");
        });
        return usersString.toString();
    }

    private void addUser(int userId, String userName, int budget) {
        UserObject user = new UserObject();
        user.setId(userId);
        user.setName(userName);
        user.setBudget(budget);

        userManager.createUser(user);
        showMessageDialog("Success", "User added successfully!");
    }

    private void addExpenses(int userId, int amount, String remarks) {
        ExpensesObject expenses = new ExpensesObject();
        expenses.setUserId(userId);
        expenses.setAmount(amount);
        expenses.setRemarks(remarks);

        expensesManager.createExpense(expenses);
        showMessageDialog("Success", "Expenses added successfully!");
    }

    private void viewUserExpenses(int userId) {
        List<ExpensesObject> expensesList = expensesManager.getExpensesByUserId(userId);
        UserObject user = userManager.retrieveUser(userId);

        if (user != null) {
            JTextArea userExpensesTextArea = new JTextArea(getUserExpensesAsString(expensesList, user));
            userExpensesTextArea.setEditable(false);

            JScrollPane scrollPane = new JScrollPane(userExpensesTextArea);

            JOptionPane.showMessageDialog(null, scrollPane, "View User Expenses", JOptionPane.PLAIN_MESSAGE);
        } else {
            showErrorDialog("Error", "User with ID " + userId + " does not exist.");
        }
    }

    private void editUserName(int userId, String newUserName) {
        UserObject user = userManager.retrieveUser(userId);
    
        if (user != null) {
            user.setName(newUserName);
            userManager.updateUser(user);
            showMessageDialog("Success", "User Name updated successfully!");
        } else {
            showErrorDialog("Error", "User with ID " + userId + " does not exist.");
        }
    }

    private String getUserExpensesAsString(List<ExpensesObject> expensesList, UserObject user) {
        StringBuilder userExpensesString = new StringBuilder();
        userExpensesString.append("User ID: ").append(user.getId()).append("\n");
        userExpensesString.append("User Name: ").append(user.getName()).append("\n");
        userExpensesString.append("Initial Budget: ").append(user.getBudget()).append("\n\n");

        for (ExpensesObject expenses : expensesList) {
            userExpensesString.append("[ Expense ID: ").append(user.getId()).append(" || Amount: ").append(expenses.getAmount()).append(" || Remarks: ").append(expenses.getRemarks()).append(" ]\n");
        }

        int remainingBudget = user.getBudget() - expensesList.stream().mapToInt(ExpensesObject::getAmount).sum();
        userExpensesString.append("\nRemaining Initial Cash: ").append(remainingBudget);

        return userExpensesString.toString();
    }

    private void showErrorDialog(String title, String content) {
        JOptionPane.showMessageDialog(null, content, title, JOptionPane.ERROR_MESSAGE);
    }

    private void showMessageDialog(String title, String content) {
        JOptionPane.showMessageDialog(null, content, title, JOptionPane.INFORMATION_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new BudgetTrackerGUI());
    }
}
