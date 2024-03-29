import java.net.*;
import java.io.*;

public class ShowComic {

    static final String DILBERT="dilbert";
    static final String XKCD="xkcd";
    static final String [] SUPPORTED={DILBERT,XKCD};

    String origurl;
    String linematch;
    String searchkey1;
    String searchkey2;
    String endkey;
    String name;

    
	public static void main(String [] args){
	    String thename=DILBERT;
	    if (args.length!=0) thename=args[0];
	    ShowComic comic = new ShowComic(thename);
	    try{
		comic.show();
	    }
	    catch(IOException e){
		e.printStackTrace();
	    }	
	}

    public ShowComic(String name) {
	this.name=name;
	if(name.equals(DILBERT)){
	    origurl="https://dilbert.com/";
	    linematch="twitter:image";
	    searchkey1="content=";
	    searchkey2="\"";
	    endkey="\"";
	}
	if(name.equals(XKCD)){
	    origurl="https://xkcd.com";
	    linematch="hotlinking/embedding";
	    searchkey1="a href= \"";
	    searchkey2=">";
	    endkey="</a>";
	}
	if(origurl==null){
	    System.out.println("Comic "+name+" not supported");
	    System.out.println("Supported comics:");
	    for (int x=0;x<SUPPORTED.length;x++){
		System.out.println(" - "+SUPPORTED[x]);
	    }
	    System.exit(1);
	}
    }

    public void show() throws IOException {
	String image=getImageUrl();
	if(image!=null) {
	    if(image.startsWith("//")) image="http:"+image;
	    System.out.println("Image:"+image);
	    ImView x = new ImView(image,name);
       	}else{
	    System.out.println("No Image found");
	}
    }


    String getImageUrl() throws IOException{		    
	System.out.println("Getting "+origurl);
	URL url=new URL(origurl);
	URLConnection conn=url.openConnection();
	InputStream is=conn.getInputStream();
	
	System.out.println("Reading....");
	LineNumberReader r=new LineNumberReader(new InputStreamReader(is));
	while(true){
	    String line=r.readLine();
	    if(line==null) break;
	    //System.out.println(line);
	    String image = getImageFromLine(line);
	    if(image!=null) return image;
	}
	return null;
    }


    public String getImageFromLine(String line){
	if(line==null) return null;
        if(line.indexOf(linematch) == -1) return null;
        System.out.println("Parsing line:"+line);                
	int start=line.indexOf(searchkey1);
	if(start == -1) return null;
        start=line.indexOf(searchkey2,start);
        start++;
	while (line.charAt(start)==' ') start++;
	int end=line.indexOf(endkey,start);
        System.out.println("Range:"+start+"-"+end);
	if(end==-1){
	    return line.substring(start);
	}else{
	    return line.substring(start,end);
	}
    }




}
