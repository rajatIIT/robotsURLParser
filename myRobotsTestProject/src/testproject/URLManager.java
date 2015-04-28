/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package testproject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author rajatpawar
 */
public class URLManager {
    
        private ArrayList<String> URLList;
        private ArrayList<String> nonReachableList;
        private ArrayList<String> ProcessedURLS;
                
        private HashMap<String,Integer> URLAMap;
        private HashMap<String,Integer> URLNAMap;
        private HashMap<String,Float> URLDelayValue;
        private int siteWithoutRobots=0,sitesWithRobots=0;
        private int totalProcessedURLs=0;
        //private ArrayList<String> 
    
    public URLManager(String[] args) throws FileNotFoundException, MalformedURLException {
        
        
        
        // get the path of the URLs, the path of the out file.
        
        // manage the URLs 
        
        // for each URL pass the URL to the robotParser file. 
        
        String myURLFilePath = args[0];
        String outputDirectoryPath = args[1];
        String tempFilePath = args[2];
        
        URLAMap = new HashMap<String, Integer>();
        URLNAMap = new HashMap<String, Integer>();
        URLDelayValue = new HashMap<String, Float>();
        URLList = new ArrayList<String>();
        ProcessedURLS = new ArrayList<String>();
       
       
        
        
        readURLS(myURLFilePath);
        
        
        
        Iterator URLIt = URLList.iterator();
        
        while(URLIt.hasNext()) {
        	
        	totalProcessedURLs++;
        	
        	if(totalProcessedURLs%1000==0){
        		// write the results
        		// now write the results
                try {
					writeMisbehaved(URLNAMap, outputDirectoryPath);
					writeBehaved(URLAMap, outputDirectoryPath);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
					System.out.println("Exception while writing files.");
				}
                
        		
        		
        	}
            
            String nextURL = (String)URLIt.next();
            nextURL = "http://" + nextURL;
           // System.out.println(nextURL);
            
       
            
            URL tempURL = new URL(nextURL);
            
            
            // here
            
            if (!ProcessedURLS.contains(tempURL.getHost()))
            {
            
            try {
                
                ProcessedURLS.add(tempURL.getHost());
                
                URL myURL = new URL(tempURL.getProtocol() + "://" + tempURL.getHost() + "/robots.txt");
                
                URLConnection myURLConnection = myURL.openConnection();
                
              //  myURLConnection.setConnectTimeout(500);
              //  myURLConnection.setReadTimeout(500);
                
                InputStream URLInputStream = myURLConnection.getInputStream();
                
//                FileOutputStream myFOS = new FileOutputStream(new File(tempFilePath));
//                int temp = URLInputStream.read();
//                
//                while(temp !=-1){
//                    myFOS.write(temp);
//                    temp = URLInputStream.read();
//            }
//                
               
              //  myFOS.close();
                
                
               // RobotParser myRobotParser = new RobotParser(URLAMap, URLNAMap, URLDelayValue,tempFilePath);
               RobotParser myRobotParser = new RobotParser(URLAMap, URLNAMap, URLDelayValue,URLInputStream);
                
                URLInputStream.close();
                
                System.out.println(tempURL.toString() + " exists");
                sitesWithRobots++;
                     System.out.println();
       
            
            } catch (FileNotFoundException fnex){
                
                System.out.println(tempURL.toString() + " does not exist");
                siteWithoutRobots++;
                     System.out.println();
       
               // System.out.println(fnex.toString());
                
            } catch(SocketTimeoutException stox) {
            	System.out.println( tempURL.toString() + ": Unable to connect to URL.");
            } catch (IOException ioex) {
            	System.out.println( tempURL.toString() + ": Unable to connect to URL.");
            }
            
        }
            
            
            // here
            
        }
        
        int total = siteWithoutRobots + sitesWithRobots;
        System.out.println("Total hosts examined: " +total + ". " );
        System.out.println("Robots.txt does not exist for " + siteWithoutRobots + " URLs. ");
        
            try {
                // now write the results
                writeMisbehaved(URLNAMap, outputDirectoryPath);
                writeBehaved(URLAMap, outputDirectoryPath);
            } catch (IOException ex) {
                Logger.getLogger(URLManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        
        
        
    }
    
    
    private void readURLS(String URLFilePath) throws FileNotFoundException {
        Scanner URLFileScanner = new Scanner(new File(URLFilePath));
        ArrayList<String> nonFunctionalSites = new ArrayList<String>();
        while(URLFileScanner.hasNextLine()) {
        URLList.add(URLFileScanner.nextLine());
        }
        URLFileScanner.close();
       
    }
    
    
    private void writeMisbehaved(HashMap misbehavedCrawlers,String resultDirectoryPath) throws IOException {
        
        File crawlerStats = new File(resultDirectoryPath + File.separator + "BadCrawlerStats_"+ totalProcessedURLs);
        crawlerStats.createNewFile();
        
        
       // System.out.println("Misbehaved Crawlers:" + misbehavedCrawlers.toString());
       // Iterator sortedIterator = getSortesdIterator(misbehavedCrawlers);
        
        
        Iterator mbcIterator = misbehavedCrawlers.keySet().iterator();
        
        
       // Iterator mbcIterator = sortedIterator;
        PrintWriter misheavedCrawlerStatsWriter = new PrintWriter(crawlerStats);
        
        String next,nextString;
        while(mbcIterator.hasNext()) {
           next = (String)mbcIterator.next();
           nextString = next + "\n" +   misbehavedCrawlers.get(next);
        //   System.out.println(nextString);
            misheavedCrawlerStatsWriter.println(nextString);
        }
        misheavedCrawlerStatsWriter.close();
        
    }

    
    private void writeBehaved(HashMap misbehavedCrawlers,String resultDirectoryPath) throws IOException {
        
        File crawlerStats = new File(resultDirectoryPath + File.separator + "GoodCrawlerStats_" + totalProcessedURLs);
        crawlerStats.createNewFile();
        
        
       // System.out.println("Misbehaved Crawlers:" + misbehavedCrawlers.toString());
       // Iterator sortedIterator = getSortesdIterator(misbehavedCrawlers);
        
        
        Iterator mbcIterator = misbehavedCrawlers.keySet().iterator();
        
        
       // Iterator mbcIterator = sortedIterator;
        PrintWriter misheavedCrawlerStatsWriter = new PrintWriter(crawlerStats);
        
        String next,nextString;
        while(mbcIterator.hasNext()) {
           next = (String)mbcIterator.next();
           nextString = next + "\n" +   misbehavedCrawlers.get(next);
        //   System.out.println(nextString);
            misheavedCrawlerStatsWriter.println(nextString);
        }
        misheavedCrawlerStatsWriter.close();
        
    }
    
    
    
    
}
