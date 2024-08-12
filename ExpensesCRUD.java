//KAI
import java.io.*;
import java.util.*;

public class ExpensesCRUD {
    private File expensesFile;
    private Set<Integer> existingExpenseIds;

    public ExpensesCRUD() {
        expensesFile = new File("C:\\Code Examples\\Budget Tracker\\expenses.txt");
        existingExpenseIds = getAllExpenseIds();
    }

    private Set<Integer> getAllExpenseIds() {
        Set<Integer> ids = new HashSet<>();
        try (Scanner scan = new Scanner(new FileReader(expensesFile))) {
            while (scan.hasNextLine()) {
                try {
                    int expenseId = scan.nextInt();
                    ids.add(expenseId);
                    scan.nextLine(); 
                } catch (Exception err) {
                    continue;
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return ids;
    }

    public List<ExpensesObject> getAllExpenses() {
        List<ExpensesObject> expenseList = new ArrayList<>();
        try (Scanner scan = new Scanner(new FileReader(expensesFile))) {
            while (scan.hasNextLine()) {
                try {
                    ExpensesObject expense = new ExpensesObject();
                    expense.setUserId(scan.nextInt());
                    expense.setExpenseId(scan.nextInt());
                    expense.setAmount(scan.nextInt());
                    expense.setRemarks(scan.nextLine().trim());

                    expenseList.add(expense);
                } catch (Exception err) {
                    continue;
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return expenseList;
    }

    public void createExpense(ExpensesObject expense) {
        boolean flag = true;

        if (existingExpenseIds.contains(expense.getExpenseId())) {
            flag = false;
            System.out.println("Error: Duplicate Expense ID error");
        }

        if (flag) {
            try (FileWriter fw = new FileWriter(expensesFile, true);
                 BufferedWriter bw = new BufferedWriter(fw)) {
                bw.write(expense.getUserId() + " ");
                bw.write(expense.getExpenseId() + " ");
                bw.write(expense.getAmount() + " ");
                bw.write(expense.getRemarks() + "\n");

                existingExpenseIds.add(expense.getExpenseId());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public List<ExpensesObject> getExpensesByUserId(int userId) {
        List<ExpensesObject> expensesList = new ArrayList<>();
        try (Scanner scan = new Scanner(new FileReader(expensesFile))) {
            while (scan.hasNextLine()) {
                try {
                    ExpensesObject expense = new ExpensesObject();
                    expense.setUserId(scan.nextInt());
                    if (expense.getUserId() == userId) {
                        expense.setExpenseId(scan.nextInt());
                        expense.setAmount(scan.nextInt());
                        expense.setRemarks(scan.nextLine().trim());

                        expensesList.add(expense);
                    } else {
                        scan.nextLine(); 
                    }
                } catch (Exception err) {
                    continue;
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return expensesList;
    }
}