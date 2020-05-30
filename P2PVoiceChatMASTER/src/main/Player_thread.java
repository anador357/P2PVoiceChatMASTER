package main;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

import javax.sound.sampled.SourceDataLine;

public class Player_thread extends Thread{

	
	public DatagramSocket din;
	public SourceDataLine audio_out;
	byte[] byBuff = new byte[1024];
	
	@Override
	public void run() {
		

		
		int i=0;
		DatagramPacket incoming = new DatagramPacket(byBuff, byBuff.length);
		while (User.calling) {
			/*
			if (din.getInetAddress() == null) {
				System.out.println("no incoming transmission");
				break;
			}
			*/
			try {
				din.receive(incoming);
				byBuff = incoming.getData();
				audio_out.write(byBuff, 0, byBuff.length);
				//System.out.println("recieved#"+i++);///terminal recieved
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		
		audio_out.close();
		audio_out.drain();
		System.out.println("Terminated{Thread_Player}");
		din.close();
		
	
	
	}
}
