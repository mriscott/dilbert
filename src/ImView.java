import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.util.*;

public class ImView extends Frame {
	private Image image;
	static String TEMP_DIR;
	static
	{
		if(File.separatorChar=='/') TEMP_DIR="/tmp";
		else TEMP_DIR="c:\\temp";
	}
	public ImView(){
		addWindowListener(new WindowAdapter() {
      		public void windowClosing(WindowEvent e) {
        		System.exit(0);
      		}
		});

	}

	public ImView(String file,String title){
		this();
		try
		{
			setImage(file,title);
		}
		catch(MalformedURLException e){
			System.out.println("Malformed URL:"+file);
		}
		catch(IOException e){
			System.out.println("Error reading "+file);
		}

	}


	public void setImage(String fileName,String title) throws IOException, MalformedURLException{
		if(fileName.startsWith("http") || fileName.startsWith("ftp")){
			setImageURL(fileName,title);
			return;
		}
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		image = toolkit.getImage(fileName);
		MediaTracker mediaTracker = new MediaTracker(this);
		mediaTracker.addImage(image, 0);
		try
		{
			mediaTracker.waitForID(0);
		}
		catch (InterruptedException ie)
		{
			System.err.println(ie);
			System.exit(1);
		}
		setSize(image.getWidth(null), image.getHeight(null)+20);
		setTitle(title);
		show();
		repaint();
	}

	public void setImageURL(String inurl, String title) throws IOException, MalformedURLException{
		URL url=new URL(inurl);
		URLConnection conn=url.openConnection();
		InputStream is=conn.getInputStream();
		String tempfile=TEMP_DIR+File.separator+"image.png";
		System.out.println("Downloading "+inurl+" to "+tempfile);
		FileOutputStream fos=new FileOutputStream(tempfile);
		byte [] buf=new byte[1000];
		while(true){
			int read=is.read(buf);
			//System.out.println("Read "+read+" bytes");
			if(read==0 || read==-1) break;
			fos.write(buf,0,read);
		}
		fos.close();
		setImage(tempfile,title);
	}

	public void paint(Graphics graphics) {
		graphics.drawImage(image, 0, 20, null);
	}

    public Hashtable getopts(String [] args)
    {
        Hashtable opts = new Hashtable();
        for (int x=0;x<args.length;x++)
        {
            if(args[x].startsWith("-"))
            {
                opts.put(args[x],args[x]);
                args[x]=null;
            }
        }

        if(opts.get("-rand")!=null)
        {
            System.out.println("Randomizing array");
            Arrays.sort(args,new Comparator(){
                public int compare(Object o1, Object o2){
                    return (int)(Math.random()*100-50);
                }
                public boolean equals(Object o){
                    return o!=null && o.equals(this);
                }
            });
        }
        return opts;
    }

	public static void main(String[] args) {
		ImView me=new ImView();
        Hashtable opts = me.getopts(args);
		for(int x=0;x<args.length;x++)
		{
            if(args[x]==null) continue;
			System.out.println("Showing image "+args[x]);
			try
			{
				me.setImage(args[x],args[x]);
				if(x!=(args.length-1))
				{
					try
					{
						Thread.sleep(2000);
					}
					catch(InterruptedException e){
						// never mind
					}
				}
			}
			catch(IOException e){
				System.out.println("Could not show image "+args[x]);
			}
		}
	}
}
