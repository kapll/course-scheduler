//Besar Kapllani - 500943601


import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.TreeMap;
import java.io.File;
import java.util.Scanner;

public class Registry {
   private TreeMap<String, Student> students = new TreeMap<String, Student>();
   private TreeMap<String, ActiveCourse> courses  = new TreeMap<String, ActiveCourse>();

	public Registry() throws IOException {
		File incomingstudent = new File("students.txt");
		Scanner filereader = new Scanner(incomingstudent);
		String studname; //should hold name
		String studid; //should hold id

		while (filereader.hasNext()){
			studname = filereader.next();
			studid = filereader.next();
			if (!StudentRegistrySimulator.isNumeric(studid)){ //if it's not a string of numbers
				throw new IOException(incomingstudent.getName()); //let studentregistrysimulator handle this
			}
			students.put(studid, new Student(studname, studid)); //otherwise create the student object
		}

		// sort the students alphabetically - see class Student
		ArrayList<Student> list = new ArrayList<Student>();

		// Add some active courses with students
		String courseName = "Computer Science II";
		String courseCode = "CPS209";
		String descr = "Learn how to write complex programs!";
		String format = "3Lec 2Lab";
		list.add(students.get("38467"));
		list.add(students.get("98345"));
		list.add(students.get("57643"));
		courses.put(courseCode, new ActiveCourse(courseName, courseCode, descr, format, "W2020", list));
		// Add course to student list of courses
		students.get("38467").addCourse(courseName, courseCode, descr, format, "W2020", 0);
		students.get("98345").addCourse(courseName, courseCode, descr, format, "W2020", 0);
		students.get("57643").addCourse(courseName, courseCode, descr, format, "W2020", 0);

		// CPS511
		list.clear();
		courseName = "Computer Graphics";
		courseCode = "CPS511";
		descr = "Learn how to write cool graphics programs";
		format = "3Lec";
		list.add(students.get("34562"));
		list.add(students.get("25347"));
		list.add(students.get("46532"));
		courses.put(courseCode, new ActiveCourse(courseName, courseCode, descr, format, "F2020", list));
		students.get("34562").addCourse(courseName, courseCode, descr, format, "W2020", 0);
		students.get("25347").addCourse(courseName, courseCode, descr, format, "W2020", 0);
		students.get("46532").addCourse(courseName, courseCode, descr, format, "W2020", 0);

		// CPS643
		list.clear();
		courseName = "Virtual Reality";
		courseCode = "CPS643";
		descr = "Learn how to write extremely cool virtual reality programs";
		format = "3Lec 2Lab";
		list.add(students.get("34562"));
		list.add(students.get("38467"));
		list.add(students.get("57643"));
		list.add(students.get("46532"));
		courses.put(courseCode, new ActiveCourse(courseName, courseCode, descr, format, "W2020", list));
		students.get("34562").addCourse(courseName, courseCode, descr, format, "W2020", 0);
		students.get("38467").addCourse(courseName, courseCode, descr, format, "W2020", 0);
		students.get("57643").addCourse(courseName, courseCode, descr, format, "W2020", 0);
		students.get("46532").addCourse(courseName, courseCode, descr, format, "W2020", 0);

		// CPS706
		list.clear();
		courseName = "Computer Networks";
		courseCode = "CPS706";
		descr = "Learn about Computer Networking";
		format = "3Lec 1Lab";
		list.add(students.get("46532"));
		list.add(students.get("25347"));
		list.add(students.get("57643"));
		courses.put(courseCode, new ActiveCourse(courseName,courseCode,descr,format,"W2020",list));
		students.get("46532").addCourse(courseName, courseCode, descr, format, "W2020", 0);
		students.get("25347").addCourse(courseName, courseCode, descr, format, "W2020", 0);
		students.get("57643").addCourse(courseName, courseCode, descr, format, "W2020", 0);

		// CPS616
		list.clear();
		courseName = "Algorithms";
		courseCode = "CPS616";
		descr = "Learn about Algorithms";
		format = "3Lec 1Lab";
		list.add(students.get("25347"));
		list.add(students.get("38467"));
		list.add(students.get("34562"));
		courses.put(courseCode, new ActiveCourse(courseName,courseCode,descr,format,"W2020",list));
		students.get("25347").addCourse(courseName, courseCode, descr, format, "W2020", 0);
		students.get("38467").addCourse(courseName, courseCode, descr, format, "W2020", 0);
		students.get("34562").addCourse(courseName, courseCode, descr, format, "W2020", 0);
	}

	public TreeMap<String, ActiveCourse> getCourses(){
		return courses;
	}

	// Add new student to the registry (students arraylist above)
	public boolean addNewStudent(String name, String id){
		Student newstu = new Student(name, id);
		if (students.containsKey(id)){
			System.out.println("Student " + name + " already registered");
			return false; //student was not added
		}
		students.put(id, newstu);
		return true; //student was added
	}

	// Remove student from registry
	public boolean removeStudent(String studentId){
		if (students.containsKey(studentId)){
			students.remove(studentId);
			return true; //student was removed
		}
		return false; //there was no student with this id in the registry
	}

	// Print all registered students
	public void printAllStudents(){
		for (String key : students.keySet()){
			System.out.println("ID: " + key + " Name: " + students.get(key).getName());
		}
	}

	// Given a studentId and a course code, add student to the active course
	public void addCourse(String studentId, String courseCode){
		boolean coursetaken = false;
		for (int i = 0; i < students.get(studentId).courses.size(); i++){
			if (students.get(studentId).courses.get(i).getCode().equalsIgnoreCase(courseCode)){ //if the coursecode given is found in the student's creditcourses
				coursetaken = true;
			}
		}
		if (!coursetaken){ //if the course was not taken
			courses.get(courseCode).addStudent(students.get(studentId)); //add the student to the course list
			students.get(studentId).addCourse(courses.get(courseCode).getName(), courses.get(courseCode).getCode(), courses.get(courseCode).getCourseDescription(), courses.get(courseCode).getFormat(), courses.get(courseCode).getSemester(), 0); //add the course to the student's course list
		}
	}

	// Given a studentId and a course code, drop student from the active course
	public void dropCourse(String studentId, String courseCode){
		courses.get(courseCode).removeStudent(students.get(studentId)); //remove the student from the list of students in the class
		students.get(studentId).removeActiveCourse(courseCode); // remove the credit course from the student's list of credit courses
	}

	// Print all active courses
	public void printActiveCourses(){
		for (String key : courses.keySet()) {
			System.out.println(courses.get(key).getDescription());
		}
	}

	// Print the list of students in an active course
	public void printClassList(String courseCode){
		courses.get(courseCode).printClassList();
	}

	// Given a course code, find course and sort class list by student name
	public void sortCourseByName(String courseCode){
		courses.get(courseCode).sortByName();
	}

	// Given a course code, find course and sort class list by student name
	public void sortCourseById(String courseCode){
		courses.get(courseCode).sortById();
	}

	// Given a course code, find course and print student names and grades
	public void printGrades(String courseCode){
		courses.get(courseCode).printGrades();
	}

	// Given a studentId, print all active courses of student
	public void printStudentCourses(String studentId){
		students.get(studentId).printActiveCourses();
	}

	// Given a studentId, print all completed courses and grades of student
	public void printStudentTranscript(String studentId){
		students.get(studentId).printTranscript();
	}

	// Given a course code, student id and numeric grade
	// set the final grade of the student
	public void setFinalGrade(String courseCode, String studentId, double grade){
		for (int k = 0; k < students.get(studentId).courses.size(); k++){
			if (students.get(studentId).courses.get(k).getCode().equalsIgnoreCase(courseCode)){ //looking for the course in the list of courses the student is taking
				students.get(studentId).courses.get(k).setGrade(grade); //set grade
				students.get(studentId).courses.get(k).setInactive(); //set inactive
			}
		}
	}
}
