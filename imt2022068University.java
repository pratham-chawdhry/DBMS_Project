import java.sql.*;
import java.util.Scanner;
import java.util.Set;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

public class  imt2022068University{
    static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost:3306/universitydb?allowPublicKeyRetrieval=true&useSSL=false";

    static final String USER = "root";
    static final String PASSWORD = "pc02@December";

    public static void displayTable(String s, String[] temp, int x, Statement stmt) throws SQLException {
        ResultSet rs = stmt.executeQuery(s);
        int[] tempLengths = new int[x];
        Arrays.fill(tempLengths, 0);
        int sum = 0;

        while (rs.next()) {
            for (int i = 1; i <= x; i++) {
                String str = rs.getString(temp[i - 1]);
                tempLengths[i - 1] = Math.max(tempLengths[i - 1], str.length());
            }
        }

        for (int i = 1; i <= x; i++) {
            tempLengths[i - 1] = Math.max(tempLengths[ i - 1 ], temp[i - 1].length());
            sum += tempLengths[i - 1];
        }

        String spaces_one = new String(new char[sum + 4* x + (x-1)]).replace('\0', '-');
        System.out.println("+" + spaces_one + "+");       

        for (int i = 1; i <= x; i++) {
            if (i == 1) {
                System.out.print("|");
            }
            String str = temp[i - 1];
            int spacesCount = Math.max(0, tempLengths[i - 1] - str.length());
            String spaces = new String(new char[spacesCount]).replace('\0', ' ');
            System.out.print(String.format("%1$" + (1 * 2) + "s", "") + str +  spaces + String.format("%1$" + (1 * 2) + "s", "") + "|");
            if (i == x) {
                System.out.println("");
            }
        }

        String other_spaces = new String(new char[sum + 4* x + (x-1)]).replace('\0', '-');
        System.out.println("+" + other_spaces + "+");

        rs = stmt.executeQuery(s);
        while(rs.next()){
            System.out.print("|");

            for (int i = 1; i <= x; i++) {
                String str = rs.getString(temp[i - 1]);
                int spacesCount = Math.max(0, tempLengths[i - 1] - str.length());
                String spaces = new String(new char[spacesCount]).replace('\0', ' ');

                System.out.print(String.format("%1$" + (1 * 2) + "s", "") + rs.getString(temp[i - 1]) + spaces + String.format("%1$" + (1 * 2) + "s", "") + "|");
            }
            System.out.println("");
        }

        String spaces_second = new String(new char[sum + 4* x + (x-1)]).replace('\0', '-');
        System.out.println("+" + spaces_second + "+"); 
    }

    public static void selectTable(String tableName,String columns,Statement stmt) throws SQLException{
        String s="select "+columns+" from "+tableName;
        int x;
        String[] temp; 

        if(columns.equals("*")){
            if(tableName.equals("teacher")){
                x=4;
                String[] arr={"teacher_id","teacher_name","age","department_id"};
                temp=arr;
            }
            else if(tableName.equals("department")){
                x=2;
                String[] arr={"department_id","department_name"};
                temp=arr;
            }
            else if(tableName.equals("course")){
                x=3;
                String[] arr={"course_id","course_name","department_id"};
                temp=arr;
            }
            else if (tableName.equals("student")){
                x=4;
                String[] arr={"student_id","student_name","teacher_id", "course_id"};
                temp=arr;
            }
            else if (tableName.equals("insurance")){
                x=3;
                String[] arr={"people_id", "people_name", "teacher_id"};
                temp=arr;
            }
            else{
                x=0;
                temp=null;
            }
        }
        else{
            String[] arr=columns.split(",");
            System.out.println(arr);
            for (int i = 0; i < arr.length; i++) {
                arr[i] = arr[i].trim();
            }
            x=arr.length;
            temp=arr;
        }

        displayTable(s, temp, x, stmt);
    }

    public static void insertIntoTable(String tableName,String columns,String values,Statement stmt) throws SQLException{
        String s="insert into "+tableName+"("+columns+") values("+values+")";
        // stmt.executeUpdate(s);
        stmt.executeUpdate(s);
    }

    public static void updateTable(String tableName,String updateColumns,String updateValues,String searchColumns,String searchValues,Statement stmt) throws SQLException{
        String[] arr = updateColumns.split(",");
        String[] arr2 = updateValues.split(",");
        String[] arr3 = searchColumns.split(",");
        String[] arr4 = searchValues.split(",");
        String s="update " + tableName + " set " + arr[0] + "="+arr2[0];
        if(arr.length>1){
            for(int i=1;i<arr.length;i++){
                s = s + "," + arr[i] + "=" + arr2[i];
            }
        }
        s = s+" where " + arr3[0] + "="+arr4[0];
        if(arr3.length>1){
            for(int i=1;i<arr3.length;i++){
                s = s + " and " + arr3[i] + "=" + arr4[i];
            }
        }
        stmt.executeUpdate(s);
    }

    public static void deleteFromTable(String tableName,String searchColumns,String searchValues,int searchBy,Statement stmt) throws SQLException{
        String[] arr = searchColumns.split(",");
        String[] arr2 = searchValues.split(",");
        int x = arr.length;
        String s = "Select max("+tableName+"_id) from "+tableName;
        ResultSet rs = stmt.executeQuery(s);
        rs.next();
        int maxId = rs.getInt(1);
        s = "Select " + tableName + "_id from " + tableName + " where " + arr[0] + "="+arr2[0];
        if (searchBy == 1){
            for(int i = 1; i < x; i++){
                s = s + " and " + arr[i] + "=" + arr2[i];
            }
            rs = stmt.executeQuery(s);
        }
        else if(searchBy == 2){
            for(int i = 1; i < x; i++){
                s = s + " or " + arr[i] + "=" + arr2[i];
            }
            rs = stmt.executeQuery(s);
        }
        else{
            System.out.println("please enter valid searchBy value");
            return;
        }

        ArrayList<Integer> temp = new ArrayList<>();
        while(rs.next()){
            temp.add(rs.getInt(1));
        }
        int counter = temp.size();

        s = "delete from " + tableName + " where " + arr[0] + "=" + arr2[0];
        if(searchBy == 1){
            for(int i = 1; i < x; i++){
                s = s + " and " + arr[i] + "=" + arr2[i];
            }
            try{
                stmt.executeUpdate(s);
                System.out.println("Committed");
            }
            catch(SQLException e){
                System.out.println("Rollback");
                e.printStackTrace();
            }
        }
        else{
            for(int i = 1; i < x; i++){
                s = s + " or " + arr[i] + "=" + arr2[i];
            }
            try{
                stmt.executeUpdate(s);
                System.out.println("Committed");
            }
            catch(SQLException e){
                System.out.println("Rollback");
                e.printStackTrace();
            }
        }
    }

    public static String[] concatenateUnique(String [] arr1, String[] arr2) {
        Set<String> uniqueElements = new HashSet<>();
        for (String num : arr1) {
            uniqueElements.add(num);
        }
        for (String num : arr2) {
            uniqueElements.add(num);
        }

        String[] result = new String[uniqueElements.size()];
        int index = 0;
        for (String num : uniqueElements) {
            result[index++] = num;
        }

        return result;
    }

    public static void multiselect(int tableno1,Statement stmt) throws SQLException{
        String [] arr_teacher = {"teacher_id","teacher_name", "age", "department_id"}; 
        String [] arr_department = {"department_id","department_name"};
        String [] arr_course = {"course_id","course_name","department_id"};
        String [] arr_student = {"student_id","student_age", "student_name", "teacher_id", "course_id"};
        String [] arr_insurance = {"people_id","people_name","teacher_id"};

        if(tableno1 == 1){
            System.out.println("+-----------------------------+");
            System.out.println("|      table       |  number  |");
            System.out.println("+-----------------------------+");
            System.out.println("|  teacher         |  1       |");
            System.out.println("|  department      |  2       |");
            System.out.println("|  student         |  3       |");
            System.out.println("|  insurance       |  4       |");
            System.out.println("+-----------------------------+");

            Scanner sc = new Scanner(System.in);
            int tableno2;
            System.out.println("Enter second table number");
            tableno2 = sc.nextInt();

            switch(tableno2){
                case 1:
                    selectTable("teacher","*", stmt);
                    break;
                case 2:
                    String s="select teacher.*, department.* from teacher inner join department on teacher.department_id = department.department_id";
                    ResultSet rs = stmt.executeQuery(s);
                    String [] temp = concatenateUnique(arr_teacher, arr_department);
                    displayTable(s, temp, temp.length , stmt);
                    break;
                case 3:
                    s="select teacher.*, student.* from teacher inner join student on teacher.teacher_id = student.teacher_id";
                    rs = stmt.executeQuery(s);
                    temp = concatenateUnique(arr_teacher, arr_student);
                    displayTable(s, temp, temp.length, stmt);
                    break;
                case 4:
                    s="select teacher.*, insurance.* from teacher inner join insurance on teacher.teacher_id = insurance.teacher_id";
                    rs = stmt.executeQuery(s);
                    temp = concatenateUnique(arr_teacher, arr_insurance);
                    displayTable(s, temp, temp.length, stmt);
                    break;
                default:
                    System.out.println("enter valid table number");
            }
        }
        else if(tableno1 == 2){
            System.out.println("+-----------------------------+");
            System.out.println("|      table       |  number  |");
            System.out.println("+-----------------------------+");
            System.out.println("|  teacher         |  1       |");
            System.out.println("|  department      |  2       |");
            System.out.println("|  course          |  3       |");
            System.out.println("+-----------------------------+");

            Scanner sc = new Scanner(System.in);
            int tableno2;
            System.out.println("Enter second table number");
            tableno2 = sc.nextInt();

            switch(tableno2){
                case 1:
                    String s="select teacher.*, department.* from teacher inner join department on teacher.department_id = department.department_id";
                    ResultSet rs = stmt.executeQuery(s);
                    String [] temp = concatenateUnique(arr_teacher, arr_department);
                    displayTable(s, temp, temp.length , stmt);
                    break;
                case 2:
                    selectTable("department","*", stmt);
                    break;
                case 3:
                    s="select department.*, course.* from department inner join course on department.department_id = course.department_id";
                    rs = stmt.executeQuery(s);
                    temp = concatenateUnique(arr_department, arr_course);
                    displayTable(s, temp, temp.length, stmt);
                    break;
                default:
                    System.out.println("enter valid table number");
            }
        }
        else if(tableno1==3){
            System.out.println("+-----------------------------+");
            System.out.println("|      table       |  number  |");
            System.out.println("+-----------------------------+");
            System.out.println("|  department      |  1       |");
            System.out.println("|  course          |  2       |");
            System.out.println("|  student         |  3       |");
            System.out.println("+-----------------------------+");

            Scanner sc = new Scanner(System.in);
            int tableno2;
            System.out.println("Enter second table number");
            tableno2 = sc.nextInt();

            switch(tableno2){
                case 1:
                    String s="select department.*, course.* from department inner join course on department.department_id = course.department_id";
                    ResultSet rs = stmt.executeQuery(s);
                    String [] temp = concatenateUnique(arr_department, arr_course);
                    displayTable(s, temp, temp.length , stmt);
                    break;
                case 2:
                    selectTable("course","*", stmt);
                    break;
                case 3:
                    s="select course.*, student.* from course inner join student on course.course_id = student.course_id";
                    rs = stmt.executeQuery(s);
                    temp = concatenateUnique(arr_course, arr_student);
                    displayTable(s, temp, temp.length, stmt);
                    break;
                default:
                    System.out.println("enter valid table number");
            }
        }
        else if(tableno1 == 4){
            System.out.println("+-----------------------------+");
            System.out.println("|      table       |  number  |");
            System.out.println("+-----------------------------+");
            System.out.println("|  teacher         |  1       |");
            System.out.println("|  course          |  2       |");
            System.out.println("|  student         |  3       |");
            System.out.println("|  insurance       |  4       |");
            System.out.println("+-----------------------------+");

            Scanner sc = new Scanner(System.in);
            int tableno2;
            System.out.println("Enter second table number");
            tableno2 = sc.nextInt();

            switch(tableno2){
                case 1:
                    String s="select teacher.*, student.* from teacher inner join student on teacher.teacher_id = student.teacher_id";
                    ResultSet rs = stmt.executeQuery(s);
                    String [] temp = concatenateUnique(arr_teacher, arr_student);
                    displayTable(s, temp, temp.length , stmt);
                    break;
                case 2:
                    s="select course.*, student.* from course inner join student on course.course_id = student.course_id";
                    rs = stmt.executeQuery(s);
                    temp = concatenateUnique(arr_course, arr_student);
                    displayTable(s, temp, temp.length, stmt);
                    break;
                case 3:
                    selectTable("student","*", stmt);
                    break;
                case 4:
                    s ="select student.*, insurance.* from student inner join insurance on student.teacher_id = insurance.teacher_id";
                    rs = stmt.executeQuery(s);
                    temp = concatenateUnique(arr_student, arr_insurance);
                    displayTable(s, temp, temp.length, stmt);
                    break;
                default:
                    System.out.println("enter valid table number");
            }
        }
        else if(tableno1==5){
            System.out.println("+-----------------------------+");
            System.out.println("|      table       |  number  |");
            System.out.println("+-----------------------------+");
            System.out.println("|  teacher         |  1       |");
            System.out.println("|  student         |  2       |");
            System.out.println("|  insurance       |  3       |");
            System.out.println("+-----------------------------+");

            Scanner sc = new Scanner(System.in);
            int tableno2;
            System.out.println("Enter second table number");
            tableno2 = sc.nextInt();

            switch(tableno2){
                case 1:
                    String s="select teacher.*, insurance.* from teacher inner join insurance on teacher.teacher_id = insurance.teacher_id";
                    ResultSet rs = stmt.executeQuery(s);
                    String [] temp = concatenateUnique(arr_teacher, arr_insurance);
                    displayTable(s, temp, temp.length , stmt);
                    break;
                case 2:
                    s ="select student.*, insurance.* from student inner join insurance on student.teacher_id = insurance.teacher_id";
                    rs = stmt.executeQuery(s);
                    temp = concatenateUnique(arr_student, arr_insurance);
                    displayTable(s, temp, temp.length, stmt);
                    break;
                case 3:
                    selectTable("insurance","*", stmt);
                    break;
                default:
                    System.out.println("enter valid table number");
            }
        }
    }

    public static void main(String[] args) {
        Connection conn = null; 
        Statement stmt = null;
        Scanner sc = new Scanner(System.in);
        String garbage;
        try{
            Class.forName(JDBC_DRIVER);
            System.out.println("Connecting to database...");
            conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
            System.out.println("Creating statement...");
            stmt = conn.createStatement();
            conn.setAutoCommit(false);

            while(true){
                System.out.println("+--------------------------+");
                System.out.println("|    Command    |  Number  |");
                System.out.println("+--------------------------+");
                System.out.println("|  Select       |  1       |");
                System.out.println("|  Insert       |  2       |");
                System.out.println("|  Update       |  3       |");
                System.out.println("|  Delete       |  4       |");
                System.out.println("|  Multiselect  |  5       |");
                System.out.println("|  Exit         |  -1      |");
                System.out.println("+--------------------------+");

                System.out.print("enter number for command using above table: ");
                int x = sc.nextInt();

                if(x == -1){
                    break;
                }
                System.out.println("+-----------------------------+");
                System.out.println("|      table       |  number  |");
                System.out.println("+-----------------------------+");
                System.out.println("|  teacher         |  1       |");
                System.out.println("|  department      |  2       |");
                System.out.println("|  course          |  3       |");
                System.out.println("|  student         |  4       |");
                System.out.println("|  insurance       |  5       |");
                System.out.println("+-----------------------------+");
                if(x == 1){
                    System.out.print("enter number for table using above table: ");
                    int tableNo=sc.nextInt();
                    garbage=sc.nextLine();
                    System.out.print("enter comma-separated columns you want to select(* can also be used): ");
                    String columns=sc.nextLine();
                    switch (tableNo){
                        case 1:
                            selectTable("teacher",columns,stmt);
                            break;
                        case 2:
                            selectTable("department",columns,stmt);
                            break;
                        case 3:
                            selectTable("course",columns,stmt);
                            break;
                        case 4:
                            selectTable("student",columns,stmt);
                            break;
                        case 5:
                            selectTable("insurance",columns,stmt);
                            break;
                        default:
                            System.out.println("enter valid table number"); 
                            break;
                    }
                }
                else if(x == 2){
                    System.out.print("enter number for table using above table: ");
                    int tableNo=sc.nextInt();
                    garbage=sc.nextLine();
                    System.out.println("enter comma-separated columns into which you want to insert: ");
                    String columns=sc.nextLine();
                    System.out.println("enter comma-separated values you want to insert into thoes columns(multipe entries are not accepted): ");
                    String values=sc.nextLine();
                    
                    if(columns.split(",").length!=values.split(",").length){
                        System.out.println("enter same number of columns and values");
                        continue;
                    }

                    String [] arr = columns.split(",");

                    for(int i=0;i<arr.length;i++){
                        System.out.println(arr[i]+" "+values.split(",")[i]);
                    }

                    String s="insert into "+"("+columns+") values("+values+")";
                    System.out.println(s);


                    switch (tableNo){
                        case 1:
                            insertIntoTable("teacher",columns,values,stmt);
                            break;
                        case 2:
                            insertIntoTable("department",columns,values,stmt);
                            break;
                        case 3:
                            insertIntoTable("course",columns,values,stmt);
                            break;
                        case 4:
                            insertIntoTable("student",columns,values,stmt);
                            break;
                        case 5:
                            insertIntoTable("insurance",columns,values,stmt);
                            break;
                        default:
                            System.out.println("enter valid table number");
                            break;
                    }
                }

                else if(x == 3){
                    System.out.print("enter number for table using above table: ");
                    int tableNo=sc.nextInt();
                    garbage = sc.nextLine();
                    System.out.println("enter comma-separated columns you want to update: ");
                    String updateColumns=sc.nextLine();
                    System.out.println("enter comma-separated values you want to update into thoes columns: ");
                    String updateValues=sc.nextLine();
                    if(updateColumns.split(",").length!=updateValues.split(",").length){
                        System.out.println("enter same number of columns and values");
                        continue;
                    }
                    System.out.println("enter comma-separated columns you want to use to search: ");
                    String searchColumns=sc.nextLine();
                    System.out.println("enter comma-separated values you want to use to search: ");
                    String searchValues=sc.nextLine();
                    if(searchColumns.split(",").length!=searchValues.split(",").length){
                        System.out.println("enter same number of columns and values");
                        continue;
                    }
                    switch (tableNo){
                        case 1:
                            updateTable("teacher",updateColumns,updateValues,searchColumns,searchValues,stmt);
                            break;
                        case 2:
                            updateTable("department",updateColumns,updateValues,searchColumns,searchValues,stmt);
                            break;
                        case 3:
                            updateTable("course",updateColumns,updateValues,searchColumns,searchValues,stmt);
                            break;
                        case 4:
                            updateTable("student",updateColumns,updateValues,searchColumns,searchValues,stmt);
                            break;
                        case 5:
                            updateTable("insurance",updateColumns,updateValues,searchColumns,searchValues,stmt);
                            break;
                        default:
                            System.out.println("enter valid table number");
                            break;
                    }
                }
                else if( x == 4){
                    System.out.print("enter number for table using above table: ");
                    int tableNo=sc.nextInt();
                    garbage=sc.nextLine();
                    System.out.println("enter mode with which conditions are connected in case of multiple conditions( 1 for and and 2 for or): ");
                    int searchBy=sc.nextInt();
                    garbage=sc.nextLine();
                    System.out.println("enter comma-separated columns you want to search using: ");
                    String searchColumns=sc.nextLine();
                    System.out.println("enter comma-separated values you want to search using: ");
                    String searchValues=sc.nextLine();
                    if(searchColumns.split(",").length!=searchValues.split(",").length){
                        System.out.println("enter same number of columns and values");
                        continue;
                    }
                    switch (tableNo){
                        case 1:
                            deleteFromTable("teacher",searchColumns,searchValues,searchBy,stmt);
                            break;
                        case 2:
                            deleteFromTable("department",searchColumns,searchValues,searchBy,stmt);
                            break;
                        case 3:
                            deleteFromTable("course",searchColumns,searchValues,searchBy,stmt);
                            break;
                        case 4:
                            deleteFromTable("student",searchColumns,searchValues,searchBy,stmt);
                            break;
                        case 5:
                            deleteFromTable("insurance",searchColumns,searchValues,searchBy,stmt);
                            break;
                        default:
                            System.out.println("enter valid table number");
                            break;
                    }
                }
                else if(x==5){
                    System.out.println("Enter table number you want to select from: ");
                    int tableno1=sc.nextInt();
                    multiselect(tableno1, stmt);
                }
                conn.commit();
            }
        } catch(SQLException se){
            se.printStackTrace();
            try{
                System.out.println("Rolling back");
                conn.rollback();
            }
            catch(SQLException e){
                e.printStackTrace();
            }
        } catch(Exception e){
            e.printStackTrace();
        } finally{
            try{
                if (stmt != null)
               stmt.close();
            } catch (SQLException se2) {
            }
            try {
                if (conn != null)
                conn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
        System.out.println("End of Code");
        sc.close();
    }
}