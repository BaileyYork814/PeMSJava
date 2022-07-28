package pems_datadump;

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




public class PeMS_DataDump {

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		//Delimiter used in CSV file
		private static final String COMMA_DELIMITER = ",";
	
		//PEMs attributes index
		private static final int STATION_COL = 0;
		
		BufferedReader fileReader = null;
		String fileName = "PeMS_Stations.csv"
		
        try {
        	
        	//Create a new list of Stations to be filled by CSV file data 
        	List<Long> stations = new ArrayList<Long>();
        	
            String line = "";
            
            //Create the file reader
            fileReader = new BufferedReader(new FileReader(fileName));
            
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
		
		return;
		
		Scanner scanIn= new Scanner(System.in);
		String inputString;

		String webURL = "http://udot.bt-systems.com";

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

		System.out.println("Enter the browser version number: ");
		String browserNumber = scanIn.nextLine();
		String USER_AGENT = "Chrome/" + browserNumber;

		// Change CVS File
		System.out.println("Enter CSV file name (including extension): ");
		int filename = scanIn.nextInt();
		// int siteID = scanIn.nextInt();

		// Change month number
		System.out.println("Enter the number of the starting month: ");
		int startMonth = scanIn.nextInt();

		System.out.println("Enter the number of the starting day: ");
		int startDay = scanIn.nextInt();

		System.out.println("Enter the starting year: ");
		int startYear = scanIn.nextInt();

		System.out.println("Enter the number of the ending month: ");
		int endMonth = scanIn.nextInt();

		System.out.println("Enter the number of the ending day: ");
		int endDay = scanIn.nextInt();

		System.out.println("Enter the ending year: ");
		int endYear = scanIn.nextInt();
		scanIn.nextLine();

		System.out.println("What is the second quantity variable?");
		String quantity2 = scanIn.nextLine();

		System.out.println("What is the granularity variable?");
		String granularity = scanIn.nextLine();

		// System.out.println("What is the name of the folder to store the output in?");
		// String directoryName = scanIn.nextLine();

		// System.out.println("What is the name of the rest of the file?");
		// System.out.println("(ex. NB I-15 1200 N Lehi)");
		// String fileSuffix = scanIn.nextLine();

		System.out.println("What is the PHP Session ID? ");
		String sessionID = scanIn.nextLine();
		sessionID = "PHPSESSID=" + sessionID;

		System.out.println(quantity2);
		System.out.println(granularity);
		System.out.println(directoryName);
		System.out.println(fileSuffix);

		String parseDate;
		String parseMonth;
		
		try {
        	
        	//Create a new list of Stations to be filled by CSV file data 
        	List<Stations> stations = new ArrayList<Station>();
        	
            String line = "";
            
            //Create the file reader
            fileReader = new BufferedReader(new FileReader(fileName));
            
            //Read the CSV file header to skip it
            fileReader.readLine();
            
            //Read the file line by line starting from the second line
            while ((line = fileReader.readLine()) != null) {
                //Get all tokens available in line
                String[] tokens = line.split(COMMA_DELIMITER);
                if (tokens.length > 0) {
                	//Create a new student object and fill his  data
					Station station = new Station(Long.parseLong(tokens[PEM_ID_SNUM]), tokens[PEM_DIR], tokens[PEM_NAME]));
					stations.add(station);
				}
            }
            
            //Print the new student list
            for (Station station : stations) {
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


		for (int y = startYear; y <= endYear; y++)
		{
			for (int m = startMonth; m <= endMonth; m++)
			{
				String monthString;

				if (m < 10)
					parseMonth = "0" + m;
				else
					parseMonth = "" + m;
				
				int lastDay = 31;

				if (y == endYear && m == endMonth)
					lastDay = endDay;
				else
				{
					if(m == 4 || m == 6 || m == 9 || m == 11)
						lastDay = 30;
					else if(m == 2)
						lastDay = 28;
					// else lastDay has already been set to 31
				}

				for (int d = startDay; d <= lastDay; d++)
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
					urlParameters = urlParameters + "&station_id=" + siteID;
					
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
					for
					{
						int siteID = scanIn.nextInt();
						String directoryName = scanIn.nextLine();
						String fileSuffix = scanIn.nextLine();
						
						urlParameters = urlParameters + "&lanes=" + siteID + "-0";
						urlParameters = urlParameters + "&q=flow";
						urlParameters = urlParameters + "&q2=" + quantity2;
						urlParameters = urlParameters + "&gn=" + granularity;
						System.out.println(urlParameters);
	
						try
						{
							// Change filename
							String newFilename = "" + directoryName + "/";
							newFilename = newFilename + monthNames[m-1] + " " + d;
							newFilename = newFilename + " " + fileSuffix + ".xls";

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
							con.setRequestProperty("Cookie", sessionID);

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
