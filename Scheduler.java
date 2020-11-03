import java.util.TreeMap;
import java.util.Random;

public class Scheduler {
	TreeMap<String, ActiveCourse> schedule;

	//a 2d array will sort the schedule to be displayed
	String[][] table;

	//this is used to bridge the day of the week with the slot on the table
	TreeMap<String, Integer> dayslots = new TreeMap<String, Integer>();
	TreeMap<Integer, String> numslots = new TreeMap<Integer, String>();


	public Scheduler(TreeMap<String, ActiveCourse> courses){
		schedule = courses;
		//i needed some way to get the column on the chart a day would correspond to, as well as the day a column would correspond to
		dayslots.put("MON", 1); dayslots.put("TUE", 2); dayslots.put("WED", 3); dayslots.put("THUR", 4); dayslots.put("FRI", 5);
		numslots.put(1, "MON"); numslots.put(2, "TUE"); numslots.put(3, "WED"); numslots.put(4, "THUR"); numslots.put(5, "FRI");
	}

	public void setDayAndTime(String courseCode, String day, int startTime, int duration){
		if (!schedule.containsKey(courseCode.toUpperCase())){
			throw new UnknownCourseException("The given course code " + courseCode + "could not be found");
		}
		if (!(day.equalsIgnoreCase("Mon") || day.equalsIgnoreCase("Tue") || day.equalsIgnoreCase("Wed") || day.equalsIgnoreCase("Thur") || day.equalsIgnoreCase("Fri"))){
			throw new InvalidDayException("Day \"" + day + "\" is invalid, classes only run on weekdays");
		}
		if (startTime < 800 || startTime > 1700){
			throw new InvalidTimeException("Your given start time " + startTime + " is outside of the acceptable range");
		}
		if (duration != 1 && duration != 2 && duration != 3){
			throw new InvalidDurationException(duration + " hours is not a valid duration");
		}
		if (startTime + duration * 100 > 1700){
			throw new InvalidTimeException("This class runs too long to be booked at " + startTime);
		}
		if (schedule.get(courseCode).getCourseAmount() > 0){ //checking to see if we've added too may blocks
			int totalduration = duration;
			for (int i = 0; i < schedule.get(courseCode).getCourseAmount(); i++){
				totalduration += schedule.get(courseCode).getLectureDuration(i);
			}
			if (totalduration > 3){
				throw new InvalidDurationException("The total duration for this course would exceed 3 hours if you added " + duration + " to " + (totalduration-duration));
			}
		}
		for (String compare : schedule.keySet()){
			for (int i = 0; i < schedule.get(compare).getCourseAmount(); i++){
				if (schedule.get(compare).getLectureDay(i).equalsIgnoreCase(day)){ //if the class blocks are on the same day
					if (schedule.get(compare).getLectureDuration(i)*100 + schedule.get(compare).getLectureStart(i) > startTime && startTime + duration*100 > schedule.get(compare).getLectureStart(i)){ //if a class begins while another class is in session
						throw new LectureTimeCollisionException("This class's hours intersect " + schedule.get(compare).getName());
					}
				}
			}
		}
		//if there are no problems
		schedule.get(courseCode).setLectureDay(day);
		schedule.get(courseCode).setLectureStart(startTime);
		schedule.get(courseCode).setLectureDuration(duration);
	}

	public void setDayAndTime(String courseCode, int duration){ //automatic version
		boolean[][] occupied = new boolean[10][6];
		int dayofclass;
		int classstart;
		boolean done = false;
		if (!schedule.containsKey(courseCode.toUpperCase())){ //check code
			throw new UnknownCourseException("The given course code " + courseCode + "could not be found");
		}
		if (duration != 1 && duration != 2 && duration != 3){ //check duration
			throw new InvalidDurationException(duration + " hours is not a valid duration");
		}
		if (schedule.get(courseCode).getCourseAmount() > 0){ //checking to see if we've added too may blocks
			int totalduration = duration;
			for (int i = 0; i < schedule.get(courseCode).getCourseAmount(); i++){
				totalduration += schedule.get(courseCode).getLectureDuration(i);
			}
			if (totalduration > 3){
				throw new InvalidDurationException("The total duration for this course would exceed 3 hours if you added " + duration + " to " + (totalduration-duration));
			}
		}
		for (String keys : schedule.keySet()){
			for (int h = 0; h < schedule.get(keys).getCourseAmount(); h++){
				dayofclass = dayslots.get(schedule.get(keys).getLectureDay(h).toUpperCase());
				classstart = (schedule.get(keys).getLectureStart(h)/100)-7; //the first column 1 is 800, so 800/100 -  = 1, and so on for the other start times
				for (int i = 0; i < schedule.get(keys).getLectureDuration(h); i++){ //runs for how long the duration is, so a duration of 3 will fill the slot it starts at and the 2 after
					occupied[classstart+i][dayofclass] = true; //filling out the spaces in the timetable that have been filled
				}
			}
		}
		Random row = new Random();
		Random col = new Random();
		int r;
		int c;
		String day = "";
		while (!done){
			r = row.nextInt(9); //the amount of hours we have to choose from
			c = col.nextInt(5); //the amount of days we have to choose from
			r++; //cannot be 0th row
			c++; //cannot be 0th column
			if (!occupied[r][c]) { //if the slot we want to put it in is empty
				if (r+duration < occupied.length) { //if the duration doesnt run past acceptable hours
					if (!occupied[r+duration][c]) { //if the end time isnt intersecting a class
						if ((duration == 3 && !occupied[r + 1][c]) || duration < 3) { //block runs if duration < 3 or duration is 3 and middle slot is free
							day = numslots.get(c);
							schedule.get(courseCode.toUpperCase()).setLectureStart((r + 7) * 100); //this will turn a roll of 1 into 800, etc
							schedule.get(courseCode.toUpperCase()).setLectureDay(day);
							schedule.get(courseCode.toUpperCase()).setLectureDuration(duration);
							done = true;
						}
					}
				}
			}
		}
		if (day.equals("")){ //if we could not automatically schedule
			throw new LectureTimeCollisionException("No applicable time slots could be found");
		}
	}

	public void clearSchedule(String courseCode){
		schedule.get(courseCode).removeScheduling();
	}

	public void printSchedule(){
		createTable(); //create a new table each time print is called
		int dayofclass;
		int classstart;
		for (String keys : schedule.keySet()){ //filling the table with data
			for (int h = 0; h < schedule.get(keys).getCourseAmount(); h++){
				dayofclass = dayslots.get(schedule.get(keys).getLectureDay(h).toUpperCase());
				classstart = (schedule.get(keys).getLectureStart(h)/100)-7; //the first column 1 is 800, so 800/100 -  = 1, and so on for the other start times
				for (int i = 0; i < schedule.get(keys).getLectureDuration(h); i++){ //runs for how long the duration is, so a duration of 3 will fill the slot it starts at and the 2 after
					table[classstart+i][dayofclass] = schedule.get(keys).getCode();
				}
			}
		}
		for (int i = 0; i < table.length; i++){
			for (int j = 0; j < table[0].length; j++){ //printing the table
				if (table[i][j] == null){
					System.out.printf("%-8s", ""); //formatting is left aligned, 8 spaces
				}
				else {
					System.out.printf("%-8s", table[i][j]); //prints the value
				}
			}
			System.out.println();
		}
	}

	public void createTable(){
		table = new String[10][6]; //6 slots for monday-friday + column titles, 10 slots for 8-4 + row titles
		int hour = 800;
		for (int i = 1; i < 10; i ++){ //creating the hours name column
			if (hour  < 1000){
				table[i][0] = "0" + hour; //add a 0 to 800 and 900
			}
			else {
				table[i][0] = "" + hour;
			}
			hour += 100;
		}
		table[0][1] = " Mon"; table[0][2] = " Tue"; table[0][3] = " Wed"; table[0][4] = " Thu"; table[0][5] = " Fri"; //creating the days name row
	}

}
