/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package testproject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author rajatpawar
 */
public class RobotParser {
    
    private HashMap<String,Integer> myAMap;
    private HashMap<String,Integer> myNAMap;
    private HashMap<String,Float> myDelayValue;
    private ArrayList<String> agentsProcessed;
            
    
    
    public RobotParser(HashMap<String,Integer> AMap, HashMap<String,Integer> NAMap, HashMap<String,Float> delayValue, InputStream filepath) throws FileNotFoundException{
        
        myAMap = AMap;
        myNAMap = NAMap;
        myDelayValue= delayValue;
        agentsProcessed = new ArrayList<String>();
        String currentAgentName="";
                  String newAgentName="";
        
        
        Scanner myScanner = new Scanner(filepath);
        
        
        
        boolean firstAgent=true,tracking=false, badBehavior=false;
        
        while(myScanner.hasNextLine()) {
            
            // if next line corresponds to a user agent, update some variables where things start afresh
            String nextLine = myScanner.nextLine();
             if(!nextLine.equals("")){
                    Pattern pattern = 
            Pattern.compile("User-agent:");

                                Matcher matcher = 
            pattern.matcher(nextLine);
                                    
                                if(matcher.find())
                                {
                                   
                                    if (firstAgent) {
                                        newAgentName = ((nextLine.substring(11, nextLine.length())).trim()).toLowerCase();       
                                        currentAgentName = newAgentName;
                                        firstAgent=false;
                                        tracking=true;
                                        badBehavior=false;
                                    } else {
                                        
                                        // finalize the behavior of the old agent
                                       
                                        writeBadBehavior(newAgentName,badBehavior);
                                        agentsProcessed.add(newAgentName);
                                        
                                        newAgentName = ((nextLine.substring(11, nextLine.length())).trim()).toLowerCase();       
                                    currentAgentName = newAgentName;
                          //          System.out.println("Now tracking " + newAgentName);
                                    
                                        
                                        // check if new agent name exists in the list : if yes, increment the value
                                        tracking=true;
                                        badBehavior=false;
                                        
                                        // if no, create a new extry with number one 
                                        
                                    }
                                    
                                } else {
                                    if(tracking){
                                     Pattern poor_behavior = Pattern.compile("Disallow: /");
                                     Matcher behavior_matcher = poor_behavior.matcher(nextLine);
                                     
                                     if(behavior_matcher.find()){
                        //                 System.out.println(currentAgentName + " is found to be bad.");
                                         tracking=false;
                                         badBehavior=true;
                                     }
                                    }
                                }
                    
            }
        }
        
        
        updateRest();
        
       // System.out.println("At the end, A is " + myAMap.toString());
        
      //  System.out.println("At the end, NA is " + myNAMap.toString());
        
        
    }

    private void writeBadBehavior(String newAgentName,boolean bbhvr) {
        
        boolean doesMapAlreadyContain = myNAMap.containsKey(newAgentName);
    //    System.out.println("Updating "+ newAgentName +". ");
        
        if (doesMapAlreadyContain) {
    //    System.out.println("Map already contains " + newAgentName);
            int mapValue = myNAMap.get(newAgentName);
            
            int newValue;
            
            if(bbhvr)
             newValue = mapValue+1;
             else
                newValue=mapValue;
             
            myNAMap.put(newAgentName, newValue);
            
        } else {
            // this is a new agent
         //   System.out.println("Map does not contains " + newAgentName);
            
            if(bbhvr)
            {  myNAMap.put(newAgentName, 1);
            myAMap.put(newAgentName, 0);
            } else {
                myNAMap.put(newAgentName, 0);
            myAMap.put(newAgentName, 1);
            }
            
            myDelayValue.put(newAgentName, 0.0f);
        
        }
        
    }

    private void updateRest() {
        
        Iterator AllowedMapIterator = myAMap.keySet().iterator();
        
        while(AllowedMapIterator.hasNext()){
            
            String nextAgent = (String)AllowedMapIterator.next();
            
            if (agentsProcessed.contains(nextAgent)){
                
            } else {
                int currentvalue = myAMap.get(nextAgent);
                int newValue = currentvalue +1;
                myAMap.put(nextAgent, newValue);
            }
            
        }
        
    }
    
    
    
}
