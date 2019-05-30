package practice;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;


public class excel_sort {
	public static void main(String[] args) throws FileNotFoundException {
		excel_sort es = new excel_sort();
		ArrayList<String> lines = new ArrayList<String>();
		lines = es.input_file();
		lines = es.make_output(lines);
		writeAFile(lines);
	}
	
	
	public ArrayList<String> input_file() throws FileNotFoundException{
		String file = "/Users/jeong-yeeun/Desktop/test.csv";
		Scanner inputStream = null;
		ArrayList<String> lines = new ArrayList<String>();
		String line;
		
	    File input_file = new File (file);
	    inputStream = new Scanner (input_file);
	    while (inputStream.hasNextLine ()) {
			line = inputStream.nextLine ();
			lines.add(line);
		}
		inputStream.close ();
		
		return lines;
	}
	
	
	public ArrayList<String> make_output(ArrayList<String> lines) {
		String sep = null;
		ArrayList<String> output = new ArrayList<String>();
		for(String line: lines) {
			sep = line.trim().split("<br>")[0];
			while((sep.charAt(0) == '\'')||(sep.charAt(0) == '"'))
				sep = sep.substring(1).trim();
			sep = sep.replace(' ', '+');
			while((sep.charAt(sep.length()-1) == '+')||(sep.charAt(sep.length()-1) == '?')||(sep.charAt(sep.length()-1) == '!'||(sep.charAt(sep.length()-1) == '.')))
				sep = sep.substring(0, sep.length()- 1);
			sep = "http://creation.webpot.kr/search?keyword=" + sep;
			System.out.println(sep);
			output.add(sep);
		}
		System.out.println(output.size());
		
		return output;
	}
	
	public static void writeAFile(ArrayList<String> lines) {
		PrintWriter outputStream = null;
		String targetFileName = "/Users/jeong-yeeun/Desktop/test2.txt";
		
		try {
			File file = new File(targetFileName);
			if (!file.exists()) {
				file.getParentFile().mkdirs();
			}
			outputStream = new PrintWriter(targetFileName);
		} catch(FileNotFoundException e) {
			System.out.println(e.getMessage());
			System.exit(0);
		}
		
		for (String line: lines) {
            outputStream.println (line);
		}
		
		outputStream.close();
	}
}
