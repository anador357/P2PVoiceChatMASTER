package main;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import javax.sound.sampled.TargetDataLine;

public class RecThread extends Thread{
	
	public TargetDataLine audio_in = null;
	public DatagramSocket dout;
	byte byBuff[] = new byte[1024];
	public InetAddress server_ip;
	public int server_port;
	
	
	@Override
	public void run() {
		int i=0;
		while (User.calling) {
			audio_in.read(byBuff, 0, byBuff.length);
			DatagramPacket data = new DatagramPacket(byBuff, byBuff.length, server_ip, server_port);
			//System.out.println("sent #" +i++);///print
			try {
				dout.send(data);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		audio_in.close();
		audio_in.drain();
		System.out.println("Con terminated{Thread_Rec}");
		dout.close();
	}
	
	
}
