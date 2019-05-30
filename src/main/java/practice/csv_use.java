package practice;

import org.apache.commons.csv.CSVRecord;

import org.apache.commons.csv.CSVParser;
import java.io.FileReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.csv.CSVFormat;

public class csv_use {
	private String dataPath, resultPath, coursecode = null;
	private int analysis, startyear, endyear;
	boolean help;
	private HashMap<String,Student> students;
	
	public static void main(String[] argv) throws NotEnoughArgumentException{
		csv_use csvFile = new csv_use();
		
		csvFile.processFile( "/Users/jeong-yeeun/git/dataFile/hw5data.csv" );
	    }
	
	public void processFile(String file) throws NotEnoughArgumentException{
		ArrayList<Course> css = new ArrayList<Course>();
        try {
        	Reader in = new FileReader(file);

            CSVParser parser = CSVFormat.EXCEL.parse(in);

            // Loop on all the records of the file
            for (CSVRecord record : parser) {
            	ArrayList<String> s = new ArrayList<String>();
            	if((record.getRecordNumber() == 1)) {
                	for(int i = 0; i <9; i++)
                		s.add(record.get(i).trim());
                	continue;
                }
        	// Process the headers in the first line
        	for(int i = 0; i <9; i++)
        		s.add(record.get(i).trim());
        	
        	Course cs = new Course(s);
        	css.add(cs);
        	System.out.println(cs.getteryearTaken());
        	}
        } catch( Exception e ){
            e.printStackTrace();
        }
        
        ArrayList<Course> lines = Utils.getLines(dataPath, true);
		lines = removeCheck(lines);
		students = loadStudentCourseRecords(lines, analysis);
		Map<String, Student> sortedStudents = new TreeMap<String,Student>(students);
        ArrayList<String> linesToBeSaved = countNumberOfStudentsInEachSemester(sortedStudents);
		Utils.writeAFile(linesToBeSaved, resultPath);
		System.out.println("finish..!!");
    }
	
	private ArrayList<Course> removeCheck(ArrayList<Course> lines){
		for(int i = 0; i < lines.size(); i++) {
			if((lines.get(i).getteryearTaken() <= startyear) || (lines.get(i).getteryearTaken() >= endyear)) {
				lines.remove(i);
			}
		}
		
		return lines;
	}
	/**
	 * This method create HashMap<String,Student> from the data csv file. Key is a student id and the corresponding object is an instance of Student.
	 * The Student instance have all the Course instances taken by the student.
	 * @param lines
	 * @return
	 */
	private HashMap<String,Student> loadStudentCourseRecords(ArrayList<Course> css, int a) {
		String pre_Id = null;
		Student st = null;
		HashMap<String, Student> student = new HashMap<String, Student>();
		
		for(Course cs: css) {
			if(pre_Id == null) {
				st = new Student(cs.getterStudentId());
			}
			else if(pre_Id.equals(cs.getterStudentId())) {
				st.addCourse(cs);
			}
			else {
				System.out.println();
				if(a == 1)
					student.put(cs.getterStudentId(), st);
				else if(a == 2)
					student.put(cs.getterYearandSemester(), st);
				st = new Student(cs.getterStudentId());
				st.addCourse(cs);
			}
			pre_Id = cs.getterStudentId();
		}
		return student; // do not forget to return a proper variable.
	}
	

	/**
	 * This method generate the number of courses taken by a student in each semester. The result file look like this:
	 * StudentID, TotalNumberOfSemestersRegistered, Semester, NumCoursesTakenInTheSemester
	 * 0001,14,1,9
     * 0001,14,2,8
	 * ....
	 * 
	 * 0001,14,1,9 => this means, 0001 student registered 14 semeters in total. In the first semeter (1), the student took 9 courses.
	 * 
	 * 
	 * @param sortedStudents
	 * @return
	 */
	
	private ArrayList<String> countNumberOfCoursesTakenInEachSemester(Map<String, Student> sortedStudents) {
		ArrayList<String> all_lines = new ArrayList<String>();
		String first_Data,second_Data;
		all_lines.add("StudentID, TotalNumberOfSemestersRegistered, Semester, NumCoursesTakenInTheSemester");
		for(Student st: sortedStudents.values()) {
			first_Data = st.getterStudentId();
			
			first_Data += "," + Integer.toString(st.getSemestersByYearAndSemester().size());
			for(int i = 1; i <= st.getSemestersByYearAndSemester().size(); i++) {
				second_Data = ",";
				second_Data += Integer.toString(i);
				second_Data += ",";
				second_Data += Integer.toString(st.getNumCourseInNthSementer(i));
				all_lines.add(first_Data+second_Data);
			}
		}
		return all_lines;
	}
	
	private ArrayList<String> countNumberOfStudentsInEachSemester(Map<String, Student> sortedStudents) {
		ArrayList<String> all_lines = new ArrayList<String>();
		String line, pre_year = null, coursename = null;
		int all_count = 0;
		int count = 0;
		all_lines.add("Year,Semester,CouseCode,CourseName,TotalStudents,StudentsTaken,Rate");
		for(String year: sortedStudents.keySet()) {
			if(coursename == null)
				coursename = sortedStudents.get(year).findCourseName(coursecode);
			if(pre_year == null) {
				all_count++;
				count += sortedStudents.get(year).check_course(year, coursecode);
			}
			else if(pre_year == year) {
				all_count++;
				count += sortedStudents.get(year).check_course(year, coursecode);
			}
			else {
				float rate = all_count/count;
				line = year.split("-")[0] + "," + year.split("-")[1] + "," + coursecode + coursename + "," + Integer.toString(all_count) + "," + Integer.toString(count) + "," + Float.toString(rate)+"%";
				all_lines.add(line);
				all_count = 1;
				count = 0;
				count += sortedStudents.get(year).check_course(year, coursecode);
			}
		}
		return all_lines; // do not forget to return a proper variable.
	}
	
	
}
