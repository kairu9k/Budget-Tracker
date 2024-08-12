//KAI
public class ExpensesObject {
    private int userId;
    private int expenseId; 
    private int amount;
    private String remarks;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) { 
        this.userId = userId;
    }

    public int getExpenseId() { 
        return expenseId;
    }

    public void setExpenseId(int expenseId) { 
        this.expenseId = expenseId;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}