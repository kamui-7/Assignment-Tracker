package application;

import java.sql.*;
import java.util.*;
import java.io.*;

public class DataBase {
	
	public static String[] classes = {"Class_2B", "Class_3B", "Class_4B", "Class_3A"};
	public static String labFile;
	public static String[] studentFile = new String[4];
	public static Connection conn = createConnection();

	public static Connection createConnection() {
		Connection conn = null;
		try {
			conn = DriverManager.getConnection("jdbc:sqlite:StudentsProjects.db");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return conn;
	}
	
	public void createTable(String tablename) throws SQLException {
		
		
		String sql = "CREATE TABLE IF NOT EXISTS " + tablename + "(\n"
				+ "    entryId integer PRIMARY KEY AUTOINCREMENT,\n"
				+ "    studentId integer,\n"
				+ "    studentName text NOT NULL,\n"
				+ "    projectName text NOT NULL,\n"
				+ "    points integer,\n"
				+ "    completed boolean,\n"
				+ "    datefinished date)";
		Statement createstmt = null;
		try {
			createstmt = conn.createStatement();
			createstmt.execute(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		createstmt.close();
		
		
	}

	public ArrayList<String> get_students(String filepath) throws FileNotFoundException {
		File studentsFile = new File(filepath);
		Scanner reader = new Scanner(studentsFile);
		
		ArrayList<String> allStudents = new ArrayList<String>();
		int count = 0;
		while (reader.hasNext()) {
			allStudents.add(reader.nextLine());
			count ++;
		}
		reader.close();
		return allStudents;
		  
	}

	public Map<String,Integer> get_labs(String filepath) throws FileNotFoundException{
		Map< String,Integer> all_labs =  
                new HashMap< String,Integer>(); 
		
		File labFile = new File(filepath);
		Scanner reader = new Scanner(labFile);
		
		while (reader.hasNext()) {
			all_labs.put(reader.next(),reader.nextInt());
		}
		reader.close();
		return all_labs;
	}
	public void insert(String tablename ,int id, String name, String project,int points,int finished, String finishedat) throws SQLException{
		String insertsql = "INSERT INTO " + tablename + "(studentId,studentName,projectName,points, completed,datefinished) "
				+ "VALUES(?,?,?,?,?,?)";
		PreparedStatement pstmt = conn.prepareStatement(insertsql);
		pstmt.setInt(1,id);
		pstmt.setString(2,name);
		pstmt.setString(3, project);
		pstmt.setInt(4,points);
		pstmt.setInt(5, finished);
		pstmt.setString(6, finishedat);
		pstmt.executeUpdate();

	}
	public void insert_all_data(String tablename,String className) throws FileNotFoundException {
		Map< String,Integer> all_labs =  this.get_labs(DataBase.labFile);
		ArrayList<String> students_2B = this.get_students(DataBase.studentFile[0]);
		ArrayList<String> students_3B = this.get_students(DataBase.studentFile[1]);
		ArrayList<String> students_4B = this.get_students(DataBase.studentFile[2]);
		ArrayList<String> students_3A = this.get_students(DataBase.studentFile[3]);
		switch (className){
		case ("2B"):
			for (int student = 0; student < students_2B.size();student++) {
				for (Map.Entry<String,Integer> lab : all_labs.entrySet()) {
					try {
						insert(tablename,student,students_2B.get(student),lab.getKey(), lab.getValue(),0,"");
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}
		
		break;
			
		case ("3B"):
			for (int student = 0; student < students_3B.size();student++) {
				for (Map.Entry<String,Integer> lab : all_labs.entrySet()) {
					try {
						insert(tablename,student,students_3B.get(student),lab.getKey(), lab.getValue(),0,"");
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}
			break;
		
		case ("4B"):
			for (int student = 0; student < students_4B.size();student++) {
				for (Map.Entry<String,Integer> lab : all_labs.entrySet()) {
					try {
						insert(tablename,student,students_4B.get(student),lab.getKey(), lab.getValue(),0,"");
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}
		break;
		
		case ("3A"):
			for (int student = 0; student < students_3A.size();student++) {
				for (Map.Entry<String,Integer> lab : all_labs.entrySet()) {
					try {
						insert(tablename,student,students_3A.get(student),lab.getKey(), lab.getValue(),0,"");
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}
		break;
		
		}
	}
	public void setup_database() throws FileNotFoundException, SQLException {
		for (int i=0; i< DataBase.classes.length; i++) {
			createTable(DataBase.classes[i]);
			String className = DataBase.classes[i].substring(6);
			insert_all_data(DataBase.classes[i],className);
		}
		
	}

	
	public void query(String query) throws SQLException {
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery(query);
		ResultSetMetaData rsmd = rs.getMetaData();
		int columnsNumber = rsmd.getColumnCount();

		
		while (rs.next()) {
			   
		       for (int i = 1; i <= columnsNumber; i++) {
		    	   if (i > 1) System.out.printf("  |  ");
		    	   String columnValue = rs.getString(i);
		    	   
		    	   System.out.format("%s",columnValue);
		       }
		       System.out.println("");
	   }
	}
	
	
	public void truncate_db() throws SQLException {
		Statement stmt = conn.createStatement();
		for (int x=0;x < classes.length;x++) {
			String sqlCommand = "DROP TABLE IF EXISTS " + classes[x] ;
			stmt.executeUpdate(sqlCommand);
		}
		stmt.close();
	}
	
	public String get_file_by_name(String type) {
		switch (type) {
			case ("Lab"):
				return DataBase.labFile;
			case ("2B"):
				return DataBase.studentFile[0];
			case ("3B"):
				return DataBase.studentFile[1];
			case ("4B"):
				return DataBase.studentFile[2];
			case ("3A"):
				return DataBase.studentFile[3];
		}
		return "";
		
	}
	
	public ArrayList<Student> get_stu_db(String table) throws SQLException
	{
		String query = "SELECT DISTINCT studentName FROM " + table;
		
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery(query);
		
		ArrayList<Student> students = new ArrayList<Student>();
				
		while (rs.next()) {
			students.add(new Student(rs.getString("studentName")));
		}
		return students;
		
	}
	
	public ArrayList<Project> get_lab_db(String table, String studentname) throws SQLException
	{
		String query = "SELECT projectName, points, completed, datefinished FROM Class_" + table + " WHERE studentName = '" + studentname + "'";
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery(query);
		
		ArrayList<Project> projects = new ArrayList<Project>();
		
		ResultSetMetaData rsmd = rs.getMetaData();
		int columnsNumber = rsmd.getColumnCount();
		
		while (rs.next()) {
			
			projects.add(new Project(rs.getString("projectName"),
									rs.getInt("points"),
									rs.getBoolean("completed"),
									rs.getString("datefinished")
										));
			
			
				
			}
		for (Project x : projects) {
			System.out.println(x.getProjectName() + ":   " + x.isCompleted().get());
		}
		
		return projects;
		
	}
	public void update_row(String student, String className, Boolean completed, String dateFinished, String lab) throws SQLException {
		String sql = "UPDATE " + className + " SET completed = ? , datefinished = ? WHERE studentName = ? and projectName = ?";

		PreparedStatement pstmt = conn.prepareStatement(sql);
		
		if (completed) {
			pstmt.setInt(1, 1);
		}
		pstmt.setString(2, dateFinished);
		pstmt.setString(3, student);
		pstmt.setString(4, lab);
		
        pstmt.executeUpdate();
				
		
	}
	
	
	
}