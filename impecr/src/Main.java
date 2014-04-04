import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;

import javax.imageio.ImageIO;
import javax.swing.JApplet;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

public class Main extends JApplet {
	
	private static final long serialVersionUID = 1L;
	private static final String String = null;
	public Frame fenetre;
	
	@Override
	public void start() {		
		super.start();
		fenetre = new Frame(this);
        fenetre.setVisible(true);
	}
	
	public void takescreen(int x,int y,int w,int h) {
		try {
	        Robot robot = new Robot();
	        Rectangle captureSize = new Rectangle(x,y,w,h);
	        fenetre.dispose();
	        BufferedImage bufferedImage = robot.createScreenCapture(captureSize);
	        sendBufferedImage(bufferedImage);
	    }
	    catch(Exception e) {
	        System.err.println("ERROR");
	    }
	}

	private void sendBufferedImage(BufferedImage bufferedImage) {
		System.out.println("Sending Buffered image");
		/*
		 * TODO:
		 * (Convert Buffered Image to ByteArray?)
		 * Send (POST) to vpictu.re
		 */
		//create needed strings
	    String address = "http://api.vpictu.re/?action=sendimages&source=applet";


	    //Create HTTPClient and post
	    HttpClient client = HttpClientBuilder.create().build();
	    HttpPost post = new HttpPost(address);
	    

	    try {
	        ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
	        ImageIO.write(bufferedImage, "png", byteArray);
	        byte[] byteImage = byteArray.toByteArray();

	        MultipartEntityBuilder form = MultipartEntityBuilder.create();
	        form.setBoundary("vPicture2_Applet");
	        
	        //ContentBody cd = new InputStreamBody(new ByteArrayInputStream(byteImage), "screenshot.png");
	        
	        ContentType typeMime = ContentType.create("image/png");

	        form.addBinaryBody("img[]", byteImage, typeMime, "screenshot.png");
	        post.setEntity(form.build());
	        HttpResponse response = client.execute(post);

	        String responseStr = new String(EntityUtils.toString(response.getEntity()));
	        
	        copyLink(responseStr);
	        
	    }
	    catch (Exception e){
	    	e.printStackTrace();
	    }
	}
	
	public void copyLink(String link){
		System.out.println(link);
		/*
         * TODO: 
         * OR --> Copy link to clipboard and show the message 
         * OR --> Show a line edit with the link in it
         */
		
	}

}
