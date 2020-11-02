//Besar Kapllani - 500943601


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

// Active University Course

public class ActiveCourse extends Course {
    private ArrayList<Student> students;
    private String semester;
    private ArrayList<Integer> lectureStart;
    private ArrayList<Integer> lectureDuration;
    private ArrayList<String> lectureDay;


    public ActiveCourse(String name, String code, String descr, String fmt, String semester, ArrayList<Student> students) {
        super(name, code, descr, fmt);
        this.semester = semester;
        this.students = new ArrayList<Student>(students);
        this.lectureDay = new ArrayList<String>();
        this.lectureStart = new ArrayList<Integer>();
        this.lectureDuration = new ArrayList<Integer>();
    }

    //added the following 6 get and set methods for the use of the scheduler
    public void setLectureStart(int time){
        lectureStart.add(time);
    }

    public void setLectureDuration(int dur){
        lectureDuration.add(dur);
    }

    public void setLectureDay(String day){
        lectureDay.add(day);
    }

    public int getLectureStart(int index){
        return lectureStart.get(index);
    }

    public int getLectureDuration(int index){
        return lectureDuration.get(index);
    }

    public String getLectureDay(int index){
        return lectureDay.get(index);
    }

    public int getCourseAmount(){ //added for the use of scheduler
        return lectureDay.size();
    }

    public void removeScheduling(){ //added for scheduler, clears the arraylists that store the schedule values
            lectureDuration.clear();
            lectureStart.clear();
            lectureDay.clear();
    }

    public String getSemester(){
        return semester;
    }

    public int getSize(){ //added method to return the enrollment in the course
        return students.size();
    }

    public Student getStudent(int index){ //added method to return a student object enrolled in the course
        return students.get(index);
    }

    public void addStudent(Student stu){ //added this method to add students to the activecourse array
        students.add(stu);
    }

    public void removeStudent(Student stu){ //added this method to remove students from the activecourse array
        students.remove(stu);
    }

    // Prints the students in this course (name and student id)
    public void printClassList(){
        for (int i = 0; i < students.size(); i++){
            System.out.println(students.get(i));
        }
    }

    // Prints the grade of each student in this course (along with name and student id)
    public void printGrades(){
        for (int i = 0; i < students.size(); i++){
            System.out.println(students.get(i) + " " + getGrade(students.get(i).getId()));
        }
    }

    // Returns the numeric grade in this course for this student
    // If student not found in course, return 0
    public double getGrade(String studentId){
        int studentindex = -1;
        for (int i = 0; i < students.size(); i++){
            if (students.get(i).getId().equals(studentId)){ //if the student id is found
                studentindex = i;
            }
        }
        if (studentindex != -1){ //if the student was found in the students array
            for (int i = 0; i < students.get(studentindex).courses.size(); i++){ // Search the student's list of credit courses to find the course code that matches this active course
                if (students.get(studentindex).courses.get(i).getCode().equals(this.getCode())){
                    return students.get(studentindex).courses.get(i).getGrade();
                }
            }
        }
        return 0;
    }

    // Returns a String containing the course information as well as the semester and the number of students enrolled in the course
    public String getDescription(){
        return super.getDescription() + " " + getSemester() + " Enrollment: " + students.size() + "\n";
    }

    // Sort the students in the course by name using the Collections.sort() method with appropriate arguments
    public void sortByName(){
        Collections.sort(students, new NameComparator());
    }

    // This class is used to compare two Student objects based on student name
    private class NameComparator implements Comparator<Student>{
        public int compare(Student a, Student b){
            return (a.getName().compareTo(b.getName()));
        }
    }

    // Sort the students in the course by student id using the Collections.sort() method with appropriate arguments
    public void sortById(){
        Collections.sort(students, new IdComparator());
    }

    // This class is used to compare two Student objects based on student id
    private class IdComparator implements Comparator<Student>{
        public int compare(Student a, Student b){
            return (a.getId().compareTo(b.getId()));
        }
    }
}
