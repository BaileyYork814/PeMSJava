import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.BufferedReader;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.CookieStore;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.ArrayList;

class ConfigSettings {
	public String browser;
	public int startMonth;
	public int startDay;
	public int startYear;
	public int endMonth;
	public int endDay;
	public int endYear;
	public String secondParam;
	public String granularity;
	public String sessionID;
	public String directoryName;
	
	public ConfigSettings(String browser, int startMonth, int startDay, int startYear, int endMonth, int endDay, int endYear,
							String secondParam, String granularity, String sessionID, String directoryName)
	{
		this.browser = browser;
		this.startMonth = startMonth;
		this.startDay = startDay;
		this.startYear = startYear;
		this.endMonth = endMonth;
		this.endDay = endDay;
		this.endYear = endYear;
		this.secondParam = secondParam;
		this.granularity = granularity;
		this.sessionID = sessionID;
		this.directoryName = directoryName;
	}
}

public class PeMSDataDump {

	//Delimiter used in CSV file
	private static final String COMMA_DELIMITER = ",";
	
	//PEMs attributes index
	private static final int STATION_COL = 0;
	
	//PEMs configuration index
	private static final int BROWSER_COL = 0;
	private static final int STARTM_COL = 1;
	private static final int STARTD_COL = 2;
	private static final int STARTY_COL = 3;
	private static final int ENDM_COL = 4;
	private static final int ENDD_COL = 5;
	private static final int ENDY_COL = 6;
	private static final int VAR_COL = 7;
	private static final int GRAN_COL = 8;
	private static final int PHP_COL = 9;
	private static final int DIR_COL = 10;
	
	/**
	 * @param args
	 */
	public static void main(String[] args)
	{	
		BufferedReader fileReader = null;
		String stationFile = "PeMS_Stations.csv";
		String configFile = "PeMS_config.csv";
		ConfigSettings settings = null;
		
		List<Long> stations = new ArrayList<Long>();
		
        try {
        	
        	//Create a new list of Stations to be filled by CSV file data 
        	
            String line = "";
            
            //Create the file reader
            fileReader = new BufferedReader(new FileReader(stationFile));
            
            //Read the CSV file header to skip it
            fileReader.readLine();
            
            //Read the file line by line starting from the second line
            while ((line = fileReader.readLine()) != null) {
                //Get all tokens available in line
                String[] tokens = line.split(COMMA_DELIMITER);
                if (tokens.length > 0) {
                	//Create a new student object and fill his  data
					Long station = Long.parseLong(tokens[STATION_COL]);
					stations.add(station);
				}
            }
            
            //Print the new station list
            for (Long station : stations) {
				System.out.println(station.toString());
			}
        } 
        catch (Exception e) {
        	System.out.println("Error in CsvFileReader !!!");
            e.printStackTrace();
        } finally {
            try {
                fileReader.close();
            } catch (IOException e) {
            	System.out.println("Error while closing fileReader !!!");
                e.printStackTrace();
            }
        }
		
		try {
        	
            String line = "";
            
            //Create the file reader
            fileReader = new BufferedReader(new FileReader(configFile));
            
            //Read the CSV file header to skip it
            fileReader.readLine();
            
            //Read the file line by line starting from the second line
            while ((line = fileReader.readLine()) != null) {
                //Get all tokens available in line
                String[] tokens = line.split(COMMA_DELIMITER);
                if (tokens.length > 0) {
                	//Create a new student object and fill his  data
					settings = new ConfigSettings (
						tokens[BROWSER_COL], 
						Integer.parseInt(tokens[STARTM_COL]),
						Integer.parseInt(tokens[STARTD_COL]),
						Integer.parseInt(tokens[STARTY_COL]),
						Integer.parseInt(tokens[ENDM_COL]),
						Integer.parseInt(tokens[ENDD_COL]),
						Integer.parseInt(tokens[ENDY_COL]),
						tokens[VAR_COL],
						tokens[GRAN_COL], 
						"PHPSESSID=" + tokens[PHP_COL],
						tokens[DIR_COL]);
				}
            }
            
			if(settings == null)
			{
				System.out.println("Unable to read settings file successfully");
				return;
			}
			
            //Print the new station list
			System.out.println(settings.browser.toString());
			System.out.println(settings.startMonth);
			System.out.println(settings.startDay);
			System.out.println(settings.startYear);
			System.out.println(settings.endMonth);
			System.out.println(settings.endDay);
			System.out.println(settings.endYear);
			System.out.println(settings.secondParam.toString());
			System.out.println(settings.granularity.toString());
			System.out.println(settings.sessionID.toString());
			System.out.println(settings.directoryName.toString());
        } 
        catch (Exception e) {
        	System.out.println("Error in CsvFileReader !!!");
            e.printStackTrace();
        } finally {
            try {
                fileReader.close();
            } catch (IOException e) {
            	System.out.println("Error while closing fileReader !!!");
                e.printStackTrace();
            }
        }
		
		String webURL = "https://udot.iteris-pems.com";

		SimpleDateFormat dateFmt = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
		
		String[] monthNames = new String[12];
		monthNames[0] = "January";
		monthNames[1] = "February";
		monthNames[2] = "March";
		monthNames[3] = "April";
		monthNames[4] = "May";
		monthNames[5] = "June";
		monthNames[6] = "July";
		monthNames[7] = "August";
		monthNames[8] = "September";
		monthNames[9] = "October";
		monthNames[10] = "November";
		monthNames[11] = "December";

		String USER_AGENT = "Chrome/" + settings.browser;

		// Change month number

		String parseDate;
		String parseMonth;

		for (Long station : stations)
		{
			for (int y = settings.startYear; y <= settings.endYear; y++)
			{
				for (int m = settings.startMonth; m <= settings.endMonth; m++)
				{
					String monthString;

					if (m < 10)
						parseMonth = "0" + m;
					else
						parseMonth = "" + m;
					
					int lastDay = 31;

					if (y == settings.endYear && m == settings.endMonth)
						lastDay = settings.endDay;
					else
					{
						if(m == 4 || m == 6 || m == 9 || m == 11)
							lastDay = 30;
						else if(m == 2)
							lastDay = 28;
						// else lastDay has already been set to 31
					}

					for (int d = settings.startDay; d <= lastDay; d++)
					{
						if (d < 10)
							parseDate = parseMonth + "/0" + d;
						else
							parseDate = parseMonth + "/" + d;

						parseDate = parseDate + "/" + y;

						long startSec = 0;
						long endSec = 0;

						try
						{
							startSec = dateFmt.parse(parseDate + " 00:00:00").getTime() / 1000;
							endSec = dateFmt.parse(parseDate + " 23:59:59").getTime() / 1000;
						}
						catch (Exception e)
						{
							System.err.println("There was an exception");
							  e.printStackTrace(System.err);
							System.exit(1);
						}

						String urlParameters = "report_form=1";
						urlParameters = urlParameters + "&dnode=VDS";
						urlParameters = urlParameters + "&content=detector_health";
						urlParameters = urlParameters + "&tab=dh_raw";
						urlParameters = urlParameters + "&export=xls";
						urlParameters = urlParameters + "&station_id=" + station;
						
						urlParameters = urlParameters + "&s_time_id=" + startSec;
						urlParameters = urlParameters + "&s_mm=" + m;
						urlParameters = urlParameters + "&s_dd=" + d;
						urlParameters = urlParameters + "&s_yy=" + y;
						urlParameters = urlParameters + "&s_hh=0";
						urlParameters = urlParameters + "&s_mi=0";

						urlParameters = urlParameters + "&e_time_id=" + endSec;
						urlParameters = urlParameters + "&e_mm=" + m;
						urlParameters = urlParameters + "&e_dd=" + d;
						urlParameters = urlParameters + "&e_yy=" + y;
						urlParameters = urlParameters + "&e_hh=23";
						urlParameters = urlParameters + "&e_mi=55";
							
						urlParameters = urlParameters + "&lanes=" + station + "-0";
						urlParameters = urlParameters + "&q=flow";
						urlParameters = urlParameters + "&q2=" + settings.secondParam;
						urlParameters = urlParameters + "&gn=" + settings.granularity;
						System.out.println(urlParameters);
		
						try
						{
							// Change filename
							String newFilename = "" + settings.directoryName + "/";
							newFilename = newFilename + monthNames[m-1] + " " + d;
							newFilename = newFilename + " " + station + ".xls";

							URL obj = new URL(webURL+"/?"+urlParameters);
							HttpURLConnection con = (HttpURLConnection) obj.openConnection();

							//add request header
							con.setRequestMethod("GET");
							con.setRequestProperty("User-Agent", USER_AGENT);
							con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

							/*
							Map<String, List<String>> headerFields =
								con.getHeaderFields();
	
							Set<String> headerFieldsSet = headerFields.keySet();
							Iterator<String> headerFieldsIter =
								headerFieldsSet.iterator();
	
							while(headerFieldsIter.hasNext())
							{
								String headerFieldKey = headerFieldsIter.next();

								if("Set-Cookie".equalsIgnoreCase(headerFieldKey))
								{
									List<String> headerFieldValue =
										headerFields.get(headerFieldKey);

									for(String headerValue : headerFieldValue)
									{
										System.out.println("Cookie found...");

										String[] fields = headerValue.split(";\\s*");

										String cookieValue = fields[0];
										String expires = null;
										String path = null;
										String domain = null;
										boolean secure = false;

										// Parse each field
										for(int j = 1; j < fields.length; j++)
										{
											if("secure".equalsIgnoreCase(fields[j]))
												secure = true;
											else if(fields[j].indexOf('=') > 0)
											{
												String[] f = fields[j].split("=");
												if("expires".equalsIgnoreCase(f[0]))
													expires = f[1];
												else if("domain".equalsIgnoreCase(f[0]))
													domain = f[1];
												else if("path".equalsIgnoreCase(f[0]))
													path = f[1];
											}
										}

										System.out.println("cookieValue: " + cookieValue);
										System.out.println("expires: " + expires);
										System.out.println("path: " + path);
										System.out.println("domain: " + domain);
										System.out.println("secure: " + secure);
										System.out.println("***************************");

									}
								}
							}
							*/
							//String sessionID = "PHPSESSID=76d49a9f4d92a5ed07bbd20829e42dee";
							con.setRequestProperty("Cookie", settings.sessionID);

							con.setDoOutput(true);

							int responseCode = con.getResponseCode();
							System.out.println("\nSending 'GET' request to URL : " + webURL);
							System.out.println("Response Code : " + responseCode);

							InputStream response = con.getInputStream();
							FileOutputStream out = new FileOutputStream(new File(newFilename));

							byte[] buf = new byte[1024 * 100];
							int r;

							do
							{
								r = response.read(buf);

								if(r != -1)
								{
									out.write(buf, 0, r);
								}

							} while(r != -1);

							out.flush();
							out.close();
						}	
						catch (Exception e)
						{
							System.err.println("There was an exception");
							e.printStackTrace(System.err);
							System.exit(1);
						}
					}
				}
			}
		}
	}
}
