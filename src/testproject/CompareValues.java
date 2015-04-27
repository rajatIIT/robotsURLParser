/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package testproject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;
import org.archive.modules.net.RobotsDirectives;
import org.archive.modules.net.Robotstxt;

/**
 *
 * @author rajatpawar
 */
public class CompareValues {
    
    
    public CompareValues () throws IOException {
        
        
        String url = "http://www.wikipedia.com";
        
        URL myURL = new URL(url + "/robots.txt");
        
        
        
        BufferedReader myBufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream( new File("/Users/rajatpawar/Documents/robotinput"))));
    
        Robotstxt myRobotstxt = new Robotstxt(myBufferedReader);
        
       
             System.out.println(myRobotstxt.getNamedUserAgents());
        
        
        
        
        List<String> myList = myRobotstxt.getNamedUserAgents();
        
        Iterator myIt = myList.iterator();
        
        while(myIt.hasNext())
            System.out.println(myIt.next());
        
       System.out.println(myRobotstxt.allowsAll());
        
        RobotsDirectives myRobotsDirectives = myRobotstxt.getDirectivesFor("israbot");
        
        //System.out.println(myRobotsDirectives.allows("/Documents"));
        System.out.println(myRobotsDirectives.allows("/"));
        System.out.println(myRobotsDirectives.allows("/Documents"));
        
        
    }
    
}
