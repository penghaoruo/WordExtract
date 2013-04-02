import java.io.*;

import jxl.*;
import jxl.write.*;
import jxl.format.*;
import java.util.*;
import java.awt.Color;

public class MyExtract{
    
	// Number of input queries
	static int n;
	
	// Number of time slots 
    static int d;
    
    static Vector<String> query=new Vector<String>();
    
    static Vector<String> timeslot=new Vector<String>();
    
    static int[][] res;
    
    public static void initial() throws IOException {
    	File f=new File("input.txt");
		FileReader reader = new FileReader(f);
		BufferedReader buf= new BufferedReader(reader);
		String line=buf.readLine();
		while (line!=null) {
			query.addElement(line);
			line=buf.readLine();
		}
		buf.close();
		reader.close();
		
    	n=query.size();
    	
    	f=new File("TimeSlots.txt");
		reader = new FileReader(f);
		buf= new BufferedReader(reader);
		line=buf.readLine();
		while (line!=null) {
			timeslot.addElement(line);
			line=buf.readLine();
		}
		buf.close();
		reader.close();
		
		d=timeslot.size();
		
		res=new int[n][d];
		for (int i=0;i<n;i++)
			for (int j=0;j<d;j++)
				res[i][j]=0;
    }
	
    public static void parse() throws IOException {
    	File directory = new File("data/");
        File[] files = directory.listFiles();
        for (int k=0;k<files.length;k++) {
        	File curf=files[k];
    		FileReader reader = new FileReader(curf);
    		BufferedReader buf= new BufferedReader(reader);
    		
    		int num=0;
    		String line=buf.readLine();
    		while (line!=null) {
    			if (line.contains("<nowrap>")) {
    				String time=line.substring(line.indexOf('|')+2);
    				//System.out.println(time);
    				int index=timeslot.indexOf(time);
    				while (!line.contains("<td><font size=\"5\"><b>"))
    					line=buf.readLine();
    				for (int i=0;i<n;i++) {
    					while (true) {
    						int p=line.indexOf(query.elementAt(i));
    						if (p==-1) break;
    						line=line.substring(p+query.elementAt(i).length());
    						res[i][index]++;
    					}
    				}
    				while (!line.contains("<table bgcolor=\"#FFFFFF\" width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"5\">"))
    					line=buf.readLine();
    				for (int i=0;i<3;i++) line=buf.readLine();
    				/*
    				if (!line.contains("<p>")) {
    					System.out.println(curf.getName());
    					System.out.println(time);
    					System.out.println("error");
    				}*/
    				for (int i=0;i<n;i++) {
    					while (true) {
    						int p=line.indexOf(query.elementAt(i));
    						if (p==-1) break;
    						line=line.substring(p+query.elementAt(i).length());
    						res[i][index]++;
    					}
    				}
    				num++;
    			}
    			line=buf.readLine();
    		}
    		//System.out.println(num);
    		buf.close();
    		reader.close();
        }
	}
    
    public static void writeExcel() throws Exception {
    	File f=new File("output.xls");
    	f.createNewFile();
    	jxl.write.WritableWorkbook wwb = Workbook.createWorkbook(new FileOutputStream(f));
	    jxl.write.WritableSheet ws = wwb.createSheet("Sheet1", 0);
	    for (int i=0;i<n;i++) {
	    	jxl.write.Label labelC = new jxl.write.Label(i+1, 0, query.elementAt(i));
		    ws.addCell(labelC);
	    }
	    for (int i=0;i<d;i++) {
	    	jxl.write.Label labelC = new jxl.write.Label(0, i+1, timeslot.elementAt(i));
		    ws.addCell(labelC);
	    }
	    for (int i=0;i<n;i++)
	    	for (int j=0;j<d;j++) {
	    		jxl.write.Label labelC = new jxl.write.Label(i+1, j+1, new Integer(res[i][j]).toString());
			    ws.addCell(labelC);
	    	}
	    wwb.write();
	    wwb.close();
    }
    
	public static void main(String[] args)throws Exception{
		initial();
		parse();
		writeExcel();
    }
}