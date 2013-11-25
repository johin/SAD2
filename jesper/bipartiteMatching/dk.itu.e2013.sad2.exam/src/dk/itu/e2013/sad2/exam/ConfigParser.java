package dk.itu.e2013.sad2.exam;

import java.io.*;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Scanner;

public class ConfigParser {

	private File confFile;
	private Dictionary<String, String> Config;
	
	public Dictionary<String,String> ParseConfig() throws IOException{
		this.Config = new Hashtable<String,String>();
		this.confFile = new File("config.properties");
		
		if(this.confFile.exists()){
            Scanner inFile = new Scanner(this.confFile);
                while(inFile.hasNextLine()){
                	String tmpLine = inFile.nextLine();
                	if(!tmpLine.startsWith("#")){
                		this.Config.put(tmpLine.split(";")[0], tmpLine.split(";")[1]);
        			}
                }
                inFile.close();
        }
        return this.Config;
	}
}
