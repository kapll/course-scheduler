//Besar Kapllani - 500943601


import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

public class StudentRegistrySimulator {
	public static void main(String[] args){
		Registry registry = null;
		try {
			registry = new Registry();
		}
		catch (FileNotFoundException e){
			System.out.println("students.txt File not Found");
			System.exit(-1);
		}
		catch (IOException a){
			System.out.println("Bad File Format students.txt");
			System.exit(-1);
		}

		//scheduler object
		Scheduler schedule = new Scheduler(registry.getCourses());

		Scanner scanner = new Scanner(System.in);
		System.out.print(">");
		while (scanner.hasNextLine()){
			String inputLine = scanner.nextLine();
			if (inputLine == null || inputLine.equals("")){
				continue;
			}
			Scanner commandLine = new Scanner(inputLine);
			String command = commandLine.next();
			if (command == null || command.equals("")) {
				continue;
			}

			//LIST
			else if (command.equalsIgnoreCase("L") || command.equalsIgnoreCase("LIST")){
				registry.printAllStudents();
			}

			//QUIT
			else if (command.equalsIgnoreCase("Q") || command.equalsIgnoreCase("QUIT")){
				return;
			}

			//REGISTER
			else if (command.equalsIgnoreCase("REG")){ //this command registers a student into the university
				try { //to make sure we dont have errors if there arent enough commands passed in when expected
					String regname = commandLine.next();
					String regid = commandLine.next();
					if (isStringOnlyAlphabet(regname)){ //check if the name is only letters
						if (isNumeric(regid) && regid.length() == 5){ //check if the id is 5 digits and only numbers
							registry.addNewStudent(regname, regid); //run the method in registry to add a new student
						}
						else {
							System.out.println("Invalid characters in ID " + regid);
						}
					}
					else {
						System.out.println("Invalid characters in name " + regname);
					}
				}
				catch (Exception inREG){
					System.out.println("Please enter a valid input, a name followed by a 5 digit ID");
				}
			}

			//DELETE
			else if (command.equalsIgnoreCase("DEL")){ //removes a student from the university
				try {
					String remid = commandLine.next();
					if (isNumeric(remid) && remid.length() == 5){ //if the id is good
						registry.removeStudent(remid);
					}
				}
				catch (Exception inDel) {
					System.out.println("Invalid input, please enter a valid 5 digit ID");
				}
			}

			//ADD COURSE
			else if (command.equalsIgnoreCase("ADDC")){
				try {
					String addid = commandLine.next();
					String addcourse = commandLine.next();
					registry.addCourse(addid, addcourse.toUpperCase());
				}
				catch (Exception inADDC){
					System.out.println("Please enter the student's ID and the course you wish to add");
				}
			}

			//DROP COURSE
			else if (command.equalsIgnoreCase("DROPC")){ //drop course
				try {
					String dropid = commandLine.next();
					String dropcourse = commandLine.next();
					registry.dropCourse(dropid, dropcourse.toUpperCase());
				}
				catch (Exception inDROPC){
					System.out.println("Please enter the student's ID and the course you wish to drop");
				}
			}

			//PRINT ALL COURSES
			else if (command.equalsIgnoreCase("PAC")){
				registry.printActiveCourses();
			}

			//PRINT CLASS LIST
			else if (command.equalsIgnoreCase("PCL")){
				try {
					String pclid = commandLine.next();
					registry.printClassList(pclid.toUpperCase());
				}
				catch (Exception inPCL){
					System.out.println("Please enter the course code for the class you wish to examine");
				}
			}

			//PRINT GRADE REPORT
			else if (command.equalsIgnoreCase("PGR")){
				try {
					String pgrid = commandLine.next();
					registry.printGrades(pgrid.toUpperCase());
				}
				catch (Exception inPGR){
					System.out.println("Please enter the course code for the class you wish to examine");
				}
			}

			//PRINT STUDENT COURSES
			else if (command.equalsIgnoreCase("PSC")){
				try {
					String pscid = commandLine.next();
					registry.printStudentCourses(pscid);
				}
				catch (Exception inPSC){
					System.out.println("PLease enter the ID of the student who's schedule you wish to view");
				}
			}

			//PRINT STUDENT TRANSCRIPT
			else if (command.equalsIgnoreCase("PST")){
				// get student id string
				// print student transcript
				try {
					String pstid = commandLine.next();
					registry.printStudentTranscript(pstid);
				}
				catch (Exception inPST){
					System.out.println("Please enter the ID of the student who's transcript you wish to view");
				}
			}

			//SET FINAL GRADE
			else if (command.equalsIgnoreCase("SFG")){
				try {
					String sfgcode = commandLine.next();
					String sfgid = commandLine.next();
					double sfggrade = commandLine.nextDouble();
					registry.setFinalGrade(sfgcode.toUpperCase(), sfgid, sfggrade);
				}
				catch (Exception inSFG){
					System.out.println("Please enter the course code, student ID, and grade to be set");
				}
			}

			//SORT CLASS BY NAME
			else if (command.equalsIgnoreCase("SCN")){
				try {
					String scnid = commandLine.next();
					registry.sortCourseByName(scnid.toUpperCase());
				}
				catch (Exception inSCN){
					System.out.println("Please enter the code for the course you wish to sort");
				}
			}

			//SORT CLASS BY ID
			else if (command.equalsIgnoreCase("SCI")){
				try {
					String sciid = commandLine.next();
					registry.sortCourseById(sciid.toUpperCase());
				}
				catch (Exception inSCI){
					System.out.println("Please enter the code of the course you wish to sort");
				}
			}

			//SCHEDULE
			else if (command.equalsIgnoreCase("SCH")){
				try {
					String schcode = commandLine.next();
					if (commandLine.hasNextInt()){
						int schdur = commandLine.nextInt();
						schedule.setDayAndTime(schcode.toUpperCase(), schdur);
					}
					else {
						String schday = commandLine.next();
						int schstart = commandLine.nextInt();
						int schdur = commandLine.nextInt();
						schedule.setDayAndTime(schcode.toUpperCase(), schday.toUpperCase(), schstart, schdur);
					}
				}
				catch (UnknownCourseException | InvalidDayException | InvalidTimeException | InvalidDurationException | LectureTimeCollisionException inSCH){
					System.out.println(inSCH.getMessage());
				}
				catch (Exception e){
					System.out.println("Please enter a valid command; course code, day, start time, duration");
				}
			}

			//CLEAR SCHEDULE
			else if (command.equalsIgnoreCase("CSCH")){
				try {
					String cschcode = commandLine.next();
					schedule.clearSchedule(cschcode.toUpperCase());
				}
				catch (Exception inCSCH){
					System.out.println("Please enter a valid course code");
				}
			}

			//PRINT SCHEDULE
			else if (command.equalsIgnoreCase("PSCH")){
				schedule.printSchedule();
			}

			else {
				System.out.println("Unrecognised command");
			}

			//newline to prompt the user to continue to interact with the program
			System.out.print("\n>");
		}
	}

	//Method to check if there are only letters in a given string
	private static boolean isStringOnlyAlphabet(String str){
		for (int i = 0; i < str.length(); i++){
			if (!Character.isLetter(str.charAt(i))){
				return false;
			}
		}
		return true;
	}

	//Method to check if there are only numbers in a given string
	public static boolean isNumeric(String str){
		for (int i = 0; i < str.length(); i++){
			if (!Character.isDigit(str.charAt(i))){
				return false;
			}
		}
		return true;
	}

}