package suncere.androidappcf.tools;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

public class IniUtil {

	private String filename;
    protected HashMap<String, Properties> sections = new HashMap<String, Properties>();
    private transient String section;
    private transient Properties properties;
     
    public IniUtil(String filename) throws IOException{
    	
    	File file=new File(filename);
    	File path=new File(file.getParent());
    	if(!path.exists())
    	{
    		path.mkdir();
    	}
    	if(!file.exists())
    	{
    		file.createNewFile();
    	}
        BufferedReader reader = new BufferedReader(new FileReader(filename));
        read(reader);
        reader.close();
        this.filename = filename;
    }
     
    protected void read(BufferedReader reader) throws IOException {
        String line;
        while ((line = reader.readLine()) != null) {
            parseLine(line);
        }
    }
 
    private void parseLine(String line) {
        //剪尾
        line = line.trim();
        if(line.startsWith(";")){
            //跳过注释
            return;
        }
         
        if (line.matches("\\[.*\\]") == true) {
            //Section段标志
            section = line.replaceFirst("\\[(.*)\\]", "$1");
            properties = new Properties();
            sections.put(section, properties);
        } else if (line.matches(".*=.*") == true) {
            //普通值
            if (properties != null) {
                int i = line.indexOf('=');
                String name = line.substring(0, i);
                String value = line.substring(i + 1);
                properties.setProperty(name, value);
            }
        }
    }
 
    public String getValue(String section, String name) {
        Properties p = (Properties) sections.get(section);
 
        if (p == null) {
            return null;
        }
 
        String value = p.getProperty(name);
        return value;
    }
     
    public boolean putValue(String section, String name, String value){
        Properties p = (Properties) sections.get(section);
        if (p == null){
            //TODO:add a new section
            p = new Properties();
            sections.put(section, p);
        }
        String val = p.getProperty(name);
        if(val == null){
            //TODO:add a new name
        }
        p.setProperty(name, value);
        return true;
    }
     
    public boolean flush() throws IOException{
    	
    	File file=new File(filename);
    	File path=new File(file.getParent());
    	if(!path.exists())
    	{
    		path.mkdir();
    	}
    	if(!file.exists())
    	{
    		file.createNewFile();
    	}
    	
        FileWriter fw = null;
        BufferedWriter bw = null;
        fw = new FileWriter(filename);
        bw = new BufferedWriter(fw);
        if(sections == null || sections.isEmpty()){
            //TODO: just empty the file 
            bw.flush();
            bw.close();
            return true;
        }
         
        Set<Entry<String, Properties>> entryset = sections.entrySet();
        for(Entry<String, Properties> entry: entryset){
            //Write sections
            String strSection = (String) entry.getKey();
            Properties p = (Properties) sections.get(strSection);
            bw.write("[" +strSection +"]");
            bw.newLine();
             
            if(p == null || p.isEmpty()){
                //TODO: just empty the file         
                continue;
            }
            //Write Properties
            for(Object obj: p.keySet()){
                String key = (String)obj;
                bw.write(key +"=");
                String value = p.getProperty(key);
                bw.write(value);
                bw.newLine();
            }
        }
        bw.flush();
        bw.close();
        return true;
    }
}
