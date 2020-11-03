import java.util.ArrayList;

public class Student implements Comparable<Student>{
    private String name;
    private String id;
    public ArrayList<CreditCourse> courses;


    public Student(String name, String id){
        this.name = name;
        this.id = id;
        courses = new ArrayList<CreditCourse>();
    }

    public String getId(){
        return id;
    }

    public String getName(){
        return name;
    }

    public int compareTo(Student other){
        return (this.getName().compareTo(other.getName()));
    }

    // add a credit course to list of courses for this student
    public void addCourse(String courseName, String courseCode, String descr, String format, String sem, double grade){
        CreditCourse a = new CreditCourse(courseName, courseCode, descr, format, sem, grade);
        a.setActive();
        courses.add(a);
    }

    // Prints all completed (i.e. non active) courses for this student (course code, course name, semester, letter grade
    public void printTranscript(){
        for (int i = 0; i < courses.size(); i++){
            if (!courses.get(i).getActive()){
                System.out.println(courses.get(i).displayGrade());
            }
        }
    }

    // Prints all active courses this student is enrolled in
    public void printActiveCourses(){
        for (int i = 0; i < courses.size(); i++){
            if (courses.get(i).getActive()){
                System.out.println(courses.get(i).getDescription());
            }
        }
    }

    // Drop a course (given by courseCode)
    // only remove it if it is an active course
    public void removeActiveCourse(String courseCode){
        for (int i = 0; i < courses.size(); i++){
            if (courses.get(i).getCode().equalsIgnoreCase(courseCode)){
                if (courses.get(i).getActive()){ //if active
                    courses.remove(i);
                    break;
                }
            }
        }
    }

    public String toString(){
        return "Student ID: " + id + " Name: " + name;
    }

    // if student names are equal *and* student ids are equal then return true
    // Hint: you will need to cast other parameter to a local Student reference variable
    public boolean equals(Object other){
        Student b = (Student) other;
        return (this.getName().equals(b.getName()) && this.getId().equals(b.getId()));
    }

}
