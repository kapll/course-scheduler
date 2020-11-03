public class CreditCourse extends Course {
    private String semester;
    public double grade;
    public boolean active;

    public CreditCourse(String name, String code, String descr, String fmt, String semester, double grade){
        super(name, code, descr, fmt);
        this.semester = semester;
        this.grade = grade;
    }

    public boolean getActive(){
        return active;
    }

    public void setActive(){
        this.active = true;
    }

    public void setInactive(){
        this.active = false;
    }

    //added method: getGrade() so the student's grade can be accessed
    public double getGrade(){
        return grade;
    }

    //added method: setGrade() so the student's final grade can be set
    public void setGrade(double mark){
        this.grade = mark;
    }

    public String displayGrade(){
        //print out info about this course plus which semester and the grade achieved
        return super.getInfo() + " " + semester + " " + convertNumericGrade(grade);
    }

}
