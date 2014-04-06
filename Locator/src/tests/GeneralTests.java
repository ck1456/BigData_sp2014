package tests;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import main.Locator;

import org.junit.Test;

public class GeneralTests {

	@Test
	public void testQueries() throws IOException {
			// load data
			Locator l = new Locator("./txt/zip_code_database.csv", "Ny");
			System.out.println("Found " + l.getCountyCount() + " unique " + l.getState().toUpperCase()
								+ " counties" + ", read " + l.getLineCount() + " lines.");
			
			// user interface
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	        String s = null;
			do{
				System.out.print("Enter Search String (q to quit): " );
		        s = br.readLine();
		        if (!s.equals("q")){
		        		String result = l.getCountyByZipcode(s);
		        		if (result != null){
		        			System.out.println(result);
		        		}
		        		else{
		        			ArrayList<String> resultList = l.getZipcodesByCounty(s);
		        			if (resultList != null){
		        				System.out.println(resultList.toString());
			        		}
			        		else{
			        			System.out.println("Not found!");
			        		}
		        		}
		        }
		        System.out.println();
			}while (!s.equals("q"));
	}
	
	@Test
	public void testRegEx(){
		String text = "\"10282\",\"STANDARD\",\"New York\",,\"Manhattan, Nyc\",\"NY\",\"New York County\",\"America/New_York\",\"212,646\",\"40.71\",\"-73.99\",\"NA\",\"US\",\"0\",\"3575\",";
	    Pattern pattern = Pattern.compile("\"([^\"]+)\"");
	    Matcher matcher = pattern.matcher(text);
	    while(matcher.find()){
	        System.out.println(matcher.group(1));
	    }
	}

}
