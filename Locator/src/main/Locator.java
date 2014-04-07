package main;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The <code> Locator </code> class is used to map zipcodes to US counties, and vice versa. The functionality depends on
 * an accompanying csv file, attainable from http://www.unitedstateszipcodes.org/zip-code-database/
 *
 * @author Khen Price
 *
 */
public class Locator {
	/**
	 * The hash map that holds zipcode, counties as key,value pairs respectively.
	 */
	HashMap<String, String> zipcodeMap = new HashMap<>();
	
	/**
	 * The hash map that holds counties, zipcodes as key,value pairs respectively.
	 */
	HashMap<String, ArrayList<String>> countyMap = new HashMap<>();
	
	/**
	 * The file to read the data from when instantiating.
	 */
	String fileName;
	
	/**
	 * The American state in for which data is to be retrieved from the csv file.
	 */
	String state;
	
	/**
	 * Holds the number of unique counties for which zipcode numbers have been found
	 */
	int countyCount;
	
	/**
	 * Number of lines read from the text file
	 */
	int lineCount;
	
	 /**
     * Initializes a newly created {@code Locator} object.
     *
     * @param  fileName
     *         A {@code String} representing the path to the csv file containing the zipcode data.
	 *
	 * @param  state
     *         A case insensitive {@code String} representing the American state in question. Example: "NY"
     */
	public Locator(String fileName, String state) throws IOException{
		this.fileName = fileName;
		this.state = state.toLowerCase();
		
		// loads zipcode mapping to counties from textfile, into 'zipCodeData'
		loadData();
	}
	
	/**
     * @param  zipcode
     *         A {@code String} comprised of 5 digits.
     *         
     * @throws IOException
     *         If the zipcode contains non-numeric characters.
     *         
     * @return The county referred to by the entered zipcode if found, null otherwise.        
	 *
     */
	public String getCountyByZipcode(String zipcode){
		try{
			Integer.parseInt(zipcode);
		}
		catch(NumberFormatException nfe)  
		{
		}
		
		String county = zipcodeMap.get(zipcode);
		if (county != null){
			return county;
		}
	
		return null;
	}
	
	/**
     * @param  county
     *         A {@code String} comprised of alphabetical characters
     *         
     * @throws IOException
     *         If the zipcode contains non-numeric characters.
     *         
     * @return The list of zipcodes of the given countyif found, null otherwise.        
	 *
     */
	public ArrayList<String> getZipcodesByCounty(String county) throws IOException{
		try{
			if (isAllLetters(county)){	
				ArrayList<String> zipcodeList = countyMap.get(county);
				if (zipcodeList != null){
					return zipcodeList;
				}
			}
			else{ 
				throw new IOException();
			}
		}
		catch(IOException e)
		{	
		}
		return null;
	}
	
	/**
     * Prints zipcode, county pairs. 
     */
	public void printByZipcode(){
		for (String key : zipcodeMap.keySet()) {
		    System.out.println(key + ";" + zipcodeMap.get(key));
		}
	}
	
	/**
     * Prints the list of zipcodes for each county.  
     */
	public void printByCounty(){
		Object[] keys = countyMap.keySet().toArray();
		Arrays.sort(keys);
		int idx = 1;
		for (Object key : keys){
			System.out.printf("%2d: [%s] %s\n", idx, key.toString(), countyMap.get(key));
		    idx++;
		}
	}
	
	/**
	 * Getters, Setters
	 * 
	 */
	public int getCountyCount() {
		return countyCount;
	}

	public void setCountyCount(int countyCount) {
		this.countyCount = countyCount;
	}

	public int getLineCount() {
		return lineCount;
	}

	public void setLineCount(int lineCount) {
		this.lineCount = lineCount;
	}
	
	public String getState() {
		return state;
	}
	

	/**
	 * @param county The String object to check
	 * 		  
	 * @return True if contains only letters, false otherwise (space and '.' allowed).
	 */
	private boolean isAllLetters(String county) {
		char[] countyAsCharArray = county.replaceAll(" ","").replaceAll("\\.","").toCharArray();
		for (char c : countyAsCharArray){
			if (!Character.isLetter(c))
				return false;
		}
		return true;
	}

	/**
     * Loads data from csv file into the Locator object;
     * 
     * @throws IOException
     * 		   If file is not found. 
     *
     */
	private void loadData() throws IOException{
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(fileName));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		// read from file line by line, get zipcode and county data by state
		String line;
		while ((line = br.readLine()) != null) {
			   lineCount++;
			   
			   ArrayList<String> splitLine = zipParser(line);
			   String currState = preProcessTxt(splitLine.get(LocatorEnum.STATE.getValue()));
			   
			   if (currState.equals(state)){ 
				   // state found, get zipcode and county from current line
				   String zip = preProcessTxt(splitLine.get(LocatorEnum.ZIP.getValue()));
				   String county = preProcessTxt(splitLine.get(LocatorEnum.COUNTY.getValue()));
				   
				   // add county to zipcode map (for lookup by county).
				   zipcodeMap.put(zip, county);
				   
				   // add zipcode to county map
				   ArrayList<String> currList = countyMap.get(county);
				   if (currList == null){
					   countyCount++;
					   ArrayList<String> newList = new ArrayList<>(1);
					   newList.add(zip);
					   countyMap.put(county, newList);
				   }
				   else{
					   if (!currList.contains(zip))
						   currList.add(zip);
				   }
			   }
		}
		br.close();
	}
	
	/**
	 * @param line The {@code String} to be parsed.
	 * @return An {@code ArrayList} containing the fields that were parsed from the line. 
	 */
	private ArrayList<String> zipParser(String line) {
		Pattern pattern = Pattern.compile("\"([^\"]+)\"");
	    Matcher matcher = pattern.matcher(line);
	    ArrayList<String> parsed = new ArrayList<>(1);
	    while(matcher.find()){
	    	parsed.add(matcher.group(1));
	    }
		return parsed;
	}

	/**
     * Preprocess txt, when loading data from csv file into object.
     * This method is used to normalize text fields. 
     * It normalizes with respect to the state (American state) in question.
     * 
     * @param str
     * 		  The text field that is processed.
     * 
     * @param outStr
     * 		  The processed text. 
     *
     */
	private String preProcessTxt(String str){
		String outStr = str.toLowerCase().replaceAll("\"", "").trim();
		if (outStr.equals("ny")){
			outStr = outStr.replaceAll("\\.", "").replaceAll("nyc", "new york county");
		}
		return outStr;
	}	
}
