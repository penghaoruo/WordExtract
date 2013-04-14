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
    	String encoding = "UTF-8"; 
    	BufferedReader buf = new BufferedReader( 
    	  new InputStreamReader( 
    	    new FileInputStream(f), 
    	    encoding 
    	  ) 
    	);
    	//FileReader reader = new FileReader(f);
		//BufferedReader buf= new BufferedReader(reader);
		String line=buf.readLine();
		while (line!=null) {
			query.addElement(line);
			line=buf.readLine();
		}
		buf.close();
		query.setElementAt(query.elementAt(0).substring(1), 0);
		System.out.println(query.elementAt(0));
		//reader.close();
		
    	n=query.size();
    	
    	f=new File("TimeSlots.txt");
    	FileReader reader = new FileReader(f);
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
        	String encoding = "UTF-8"; 
        	BufferedReader buf = new BufferedReader( 
        	  new InputStreamReader( 
        	    new FileInputStream(curf), 
        	    encoding 
        	  ) 
        	);
    		//FileReader reader = new FileReader(curf);
    		//BufferedReader buf= new BufferedReader(reader);
    		//System.out.println(curf.getName());
    		int num=0;
    		String line=buf.readLine();
    		while (line!=null) {
    			if (line.contains("<nowrap>")) {
    				String article="";
    				String time=line.substring(line.indexOf('|')+2);
    				//System.out.println(time);
    				int index=timeslot.indexOf(time);
    				while (!line.contains("<td><font size=\"5\"><b>"))
    					line=buf.readLine();
    				article=line;
    				while (!line.contains("<table bgcolor=\"#FFFFFF\" width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"5\">"))
    					line=buf.readLine();
    				for (int i=0;i<3;i++) line=buf.readLine();
    				article=article+line;
    				//System.out.println("yes");
    				//System.out.println(article);
    				while (article.contains("<")) {
    					
    					int p1=article.indexOf("<");
    					int p2=article.indexOf(">")+1;
    					String tmp1=article.substring(0,p1);
    					String tmp2=article.substring(p2,article.length());
    					article=tmp1+tmp2;
    				}
    				//System.out.println(article);
    				for (int i=0;i<n;i++) {
    					String tmp_article=article;
    					String target=query.elementAt(i);
    					if (!target.contains("&")) {
	    					while (true) {
	    						int p=tmp_article.indexOf(target);
	    						if (p==-1) break;
	    						tmp_article=tmp_article.substring(p+target.length());
	    						res[i][index]++;
	    					}
    					}
    					else {
    						String[] strs=target.split("&");
    						int tmp_n=strs.length;
    						int tmp_count[]=new int[tmp_n];
    						for (int j=0;j<tmp_n;j++) {
    							tmp_article=article;
    							String ss=strs[j];
    							tmp_count[j]=0;
    							while (true) {
    	    						int p=tmp_article.indexOf(ss);
    	    						if (p==-1) break;
    	    						tmp_article=tmp_article.substring(p+ss.length());
    	    						tmp_count[j]++;
    	    					}
    						}
    						int tmp_res=tmp_count[0];
    						for (int j=1;j<tmp_n;j++)
    							tmp_res=Math.min(tmp_res, tmp_count[j]);
    						res[i][index]=res[i][index]+tmp_res;
    						//if (i==3&&index==1) System.out.println(tmp_count[1]);
    					}
    				}
    				num++;
    				//System.out.println(num);
    			}
    			line=buf.readLine();
    		}
    		//System.out.println(num);
    		buf.close();
    		//reader.close();
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