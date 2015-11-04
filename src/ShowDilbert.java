import java.net.*;
import java.io.*;

public class ShowDilbert {

    static{
        //System.getProperties().put( "proxySet", "true" );
        //System.getProperties().put( "proxyHost", "10.1.51.10" );
        //System.getProperties().put( "proxyPort", "80" );
    }
	public static void main(String [] args){
		try
		{
			System.out.println("Getting www.dilbert.com");
			URL url=new URL("http://www.dilbert.com");
			URLConnection conn=url.openConnection();
			InputStream is=conn.getInputStream();
			LineNumberReader r=new LineNumberReader(new InputStreamReader(is));
			while(true){
				String line=r.readLine();
				if(line==null) break;
				if (parseLine(line)) break;
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}

	public static boolean parseLine(String line) throws Exception{
        if(line.indexOf("assets.amuniversal.com") == -1) return false;
        System.out.println("Parsing line:"+line);
                
		int start=line.indexOf("src=");
		if(start == -1) return false;
		//start=line.indexOf("",start);
        
        start=line.indexOf('"',start);
        start++;
		int end=line.indexOf('"',start);
		//end=end+"strip.gif".length();
        System.out.println("Range:"+start+"-"+end);
		String url=line.substring(start,end);
		System.out.println("Image url:"+url);
		showImage(url);
		return true;
	}

	public static void showImage(String inurl) throws Exception{
		ImView x = new ImView(inurl,"Dilbert");
		//x.setLocation(1700,300);

	}




}
