package io.github.nick11roberts.llamaspawningbuttonthing;



/**
@author Kristian Lundkvist <kristian.lundkvist@gmail.com>

 Credit goes to author. ^^^
 Some revisions by Nick Roberts.
*/

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
A class for downloading and parsing a 1024 bytes of random data from http://150.203.48.55/RawHex.php
*/
public class AnuRandom{

	/**
	DataInputStream using for reading from the website.
	*/
	private DataInputStream in;

	/**
	Variable temporary storing the webpage.
	*/
	private String page;

	/**
	Stores the bytes as a String variable.
	*/
	private String bytes;

	/**
	The number of bytes to download.
	*/
	private int numberOfBytes;

	/**
	Standard constructor.
	*/
	public AnuRandom(){
		this.in = null;
		this.page = "";
		this.bytes = "";
		this.numberOfBytes = 0;
	}

	/**
	Constructor.
	@param numberOfBytes The number of bytes to download
	*/
	public AnuRandom(int numberOfBytes){
		this.numberOfBytes = numberOfBytes*2; //Slightly altered to account for Hex bytes.
		this.in = null;
		this.page = "";
		this.bytes = "";
	}

    public double getQRandomMultiplier(){

        //Gets bytes from server, will exit if server inaccessible

        Integer randHexNum = 0;
        double randMultiplier = 0;
        AnuRandom random = new AnuRandom(1);
        String temp = new String(random.getBytes());
        System.out.println(temp);
        //Gets bytes from server, throws catchable exception if server inaccessible
        try {
            temp = new String(random.getBytesSafe());
        } catch (IOException e) {
            //Handle inaccessible server
        }


        //Convert from base 16 string to int
        randHexNum = Integer.parseInt(temp, 16);
        randMultiplier = (double)((randHexNum/255.0)); // divide by the max hex value to return a value between 0-1.

        return randMultiplier;
    }

	/**
	Set the number of bytes.
	@param numberOfBytes The new number of bytes
	*/
	public void setNumberOfBytes(int numberOfBytes){
		this.numberOfBytes = numberOfBytes;
	}

	/**
	Returns the number of bytes.
	@return The number of bytes
	*/
	public int getNumberOfBytes(){
		return this.numberOfBytes;
	}

	/**
	Runs to methods to download and parse the webpage.
	Returns the bytes from the webpage.
	Handles exceptions internally by exiting application.
	@return The downloaded bytes
	*/
	public byte[] getBytes(){
		try {
			return getBytesSafe();
		} catch(MalformedURLException e){
			e.printStackTrace();
			System.exit(1);
		}
		catch(IOException e){
			e.printStackTrace();
			System.exit(1);
		}
		return null;
	}
	
	/**
	Runs to methods to download and parse the webpage.
	Returns the bytes from the webpage.
	Passes exceptions to be handled externally
	@return The downloaded bytes
	@throws IOException 
	@throws MalformedURLException 
	 */
	public byte[] getBytesSafe() throws MalformedURLException, IOException {
		byte[] temp = new byte[this.numberOfBytes];
		String store = "";

		while(store.length() < this.numberOfBytes){
			this.getPage();
			this.parsePage();
			store += this.bytes;
		}
		byte[] storeTemp = store.getBytes();
		for(int i = 0; i < this.numberOfBytes; i++){
			temp[i] = storeTemp[i];
		}

		return temp;
	}

	/**
	Downloads the webpage and stores it in memory.
	@throws MalformedURLException 
	@throws IOException 
	*/
	@SuppressWarnings("deprecation")
	public void getPage() throws MalformedURLException, IOException {
		try{
			URL u = new URL("http://150.203.48.55/RawHex.php"); //Changed from RawChar.php to RawHex.php
			this.in = new DataInputStream(new BufferedInputStream(u.openStream()));
			String temp = "";
			while ((temp = this.in.readLine()) != null){
				this.page += temp;
			}
		} finally{
			try{
				if(this.in != null) {
					this.in.close();
				}
			}
			catch(IOException e){
				e.printStackTrace();
			}
		}
	}

	/**
	Parses the random bytes from the webpage.
	*/
	public void parsePage(){

		int start;
		int end;

		start = this.page.indexOf("<table class=\"rng\" cellpadding=\"10\"> <tr><td>");
		end = this.page.indexOf("</td></tr></table><br />", start);
		start += 45;

		this.bytes = this.page.substring(start, end);

	}




}
