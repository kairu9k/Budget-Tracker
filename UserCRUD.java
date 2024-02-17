import java.util.*;
import java.io.*;

public class UserCRUD {
    File users;
    private String expenses;
    public UserCRUD(){
        users = new File("C:\\Code Examples\\Budget Tracker\\user.txt");
    }

    public List<UserObject> getAllUsers(){
        ArrayList<UserObject> user_list = new ArrayList<>();

        try {
            Scanner scan = new Scanner(new FileReader(users));
            while(scan.hasNextLine()){
                try{
                    UserObject user = new UserObject();
                    user.setId(scan.nextInt());
                    user.setName(scan.next());
                    user.setBudget(scan.nextInt());
                    user_list.add(user);
                }
                catch (Exception err){
                    continue;
                }
            }
            scan.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return user_list;
    }
    public void createUser(UserObject user){ 
        boolean flag = true;
        List <UserObject> user_list = getAllUsers();
        FileWriter fw;
        BufferedWriter bw;

        for (UserObject record : user_list){
            try {
                if ( record.getId() == user.getId() ) {
                    throw new ErrorHandler("Duplicate User ID error");
                }
            } catch ( ErrorHandler err ) {
                flag = false;
                System.out.println("Error: " + err.getMessage());
            }
        }
        if (flag) {
            try {
                fw = new FileWriter(users , true);
                bw = new BufferedWriter(fw);
                bw.write(user.getId()+"");
                bw.write(" ");
                bw.write(user.getName().toUpperCase());
                bw.write(" ");
                bw.write(user.getBudget()+"");
                bw.write("\n");
                bw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            }
        }


public UserObject retrieveUser(int Id) {
    List<UserObject> user_list = getAllUsers();

    for (UserObject user : user_list) {
        if (user.getId() == Id) {
            return user;
        }
    }
    return null;
}


    public void updateUser( UserObject record){
        List<UserObject> user_list = getAllUsers();
        for(UserObject user : user_list){
            if ( user.getId() == record.getId()) {
                user_list.set(user_list.indexOf(user), record);
                
                break;
            }
    }
    String format = "";
    for (UserObject user : user_list){
        format += user.getId() + " " + user.getName().toUpperCase() +" " + user.getBudget() + "\n";
    }

    try {
        Formatter formatFile = new Formatter(users);
        formatFile.format("%S", format);
        formatFile.close();
    } catch (FileNotFoundException e) {
        e.printStackTrace();
}
    }
    public void deleteUser(int Id) {
    List<UserObject> user_list = getAllUsers();
    UserObject userToRemove = null;

    for (UserObject user : user_list) {
        if (user.getId() == Id) {
            userToRemove = user;
            break;
        }
    }

    if (userToRemove != null) {
        user_list.remove(userToRemove);
        saveUsersToFile(user_list);
        System.out.println("User with ID " + Id + " has been deleted.");
    } else {
        System.out.println("User with ID " + Id + " does not exist.");
    }
}

private void saveUsersToFile(List<UserObject> user_list) {
    try {
        FileWriter fw = new FileWriter(users, false);
        BufferedWriter bw = new BufferedWriter(fw);

        for (UserObject user : user_list) {
            bw.write(user.getId() + "");
            bw.write(" ");
            bw.write(user.getName().toUpperCase() + "");
            bw.write(" ");
            bw.write(user.getBudget() + "");
            bw.write("\n");
        }

        bw.close();
    } catch (IOException e) {
        e.printStackTrace();
    }
}

    public List<ExpensesObject> getExpensesByUserId(int userId) {
    List<ExpensesObject> expensesList = new ArrayList<>();

    try {
        Scanner scan = new Scanner(new FileReader(expenses));
        while (scan.hasNextLine()) {
            try {
                ExpensesObject expense = new ExpensesObject();
                expense.setUserId(scan.nextInt());
                expense.setExpenseId(scan.nextInt());
                expense.setAmount(scan.nextInt());
                expense.setRemarks(scan.next());
                if (expense.getExpenseId() == userId) {
                    expensesList.add(expense);
                }
            } catch (Exception err) {
                continue;
            }
        }
        scan.close();
    } catch (FileNotFoundException e) {
        e.printStackTrace();
    }

    return expensesList;
    }
 }