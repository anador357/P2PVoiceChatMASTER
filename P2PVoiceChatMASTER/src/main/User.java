package main;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import net.miginfocom.swing.MigLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JTextField;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.Font;

public class User extends JFrame {

	
	public static boolean calling = false;
	public static void main(String[] args) {
		
	User cf = new User();
	cf.setVisible(true);
	}
	
	
	
 public int port = 8888;
 public String add_server = "192.168.1.1";
 TargetDataLine audio_in;
 SourceDataLine audio_out;
 //////////////////////////////////////////////////////////////////////////////////////////////	

 public static AudioFormat getaudAudioFormat() {

  float sampleRate = 44100;
  int sampleSizeInBits = 16;
  int channels = 2;
  boolean signed = true;
  return new AudioFormat(sampleRate, sampleSizeInBits, channels, signed, false);


 }
 ///////////////////////////////////////////////////////////////////////////////////////////

 private JPanel contentPane;

 public User() {
 	setTitle("P2P PROJECT");
 	setResizable(false);
 	setAlwaysOnTop(true);
  setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  setBounds(100, 100, 300, 150);
  contentPane = new JPanel();
  contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
  setContentPane(contentPane);
  contentPane.setLayout(new MigLayout("", "[][][][][][][][][][][grow][][][][][][]", "[][][][][]"));
      
        JLabel lblNewLabel = new JLabel("Enter your peer address here:");
        lblNewLabel.setFont(new Font("Helvetica", Font.PLAIN, 11));
        contentPane.add(lblNewLabel, "cell 2 1 12 1,alignx center,aligny center");
      
        btn_start = new JButton("Start");
        btn_start.setFont(new Font("Helvetica", Font.PLAIN, 11));
        btn_start.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
	   
    	 add_server = add_field.getText();
    	if (add_server.equals("")) {
			
    		 JOptionPane.showMessageDialog(null, "Address not valid");
    		 return;
		}
    	 add_field.setEnabled(false);
    	   
	   btn_start.setEnabled(false);
	   btn_stop.setEnabled(true);
	   User.calling = true;
          try {
           init_audio();
          } catch (UnknownHostException e1) {
           // TODO Auto-generated catch block
           e1.printStackTrace();
          } catch (SocketException e1) {
           // TODO Auto-generated catch block
           e1.printStackTrace();
          } catch (LineUnavailableException e1) {
           // TODO Auto-generated catch block
           e1.printStackTrace();
          }
         }
        });
        contentPane.add(btn_start, "cell 2 2");
      
      add_field = new JTextField();
      
      contentPane.add(add_field, "cell 7 2,alignx center,aligny center");
      add_field.setColumns(10);
      
        btn_stop = new JButton("Stop");
        btn_stop.setFont(new Font("Helvetica", Font.PLAIN, 11));
        btn_stop.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
          User.calling = false;
          btn_start.setEnabled(true);
          btn_stop.setEnabled(false);
          add_field.setEnabled(true);
         }
        });
        contentPane.add(btn_stop, "cell 13 2");
 }
 
 private JButton btn_start;
 private JButton btn_stop;
 private JTextField add_field;
 
/////////////////////////////////////////////////////////////////////////////////////////////////////
 public void init_audio() throws LineUnavailableException, UnknownHostException, SocketException {

	AudioFormat format = getaudAudioFormat();
	 //AudioFormat format = new AudioFormat(44100, 10, 2, true, false);
  DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
  DataLine.Info info_out = new DataLine.Info(SourceDataLine.class, format);
  
  if (!AudioSystem.isLineSupported(info)||!AudioSystem.isLineSupported(info_out)) {
   System.out.println("Error");
   System.exit(0);
  }
  
  //Client_voice.calling = true;///mora da se ukljuci prije zbog sinhronizacije
  

  
  audio_in = (TargetDataLine) AudioSystem.getLine(info);
  audio_in.open(format);
  audio_in.start();
  RecThread r = new RecThread();
  InetAddress inet = InetAddress.getByName(add_server);
  r.audio_in = audio_in;
  r.dout = new DatagramSocket();
  r.server_ip = inet;
  r.server_port = port;
  r.start();

  
  audio_out = (SourceDataLine) AudioSystem.getLine(info_out);
  audio_out.open(format);
  audio_out.start();
  Player_thread p = new Player_thread();
  p.din = new DatagramSocket(8888);
  p.audio_out = audio_out;
  p.start();

  //btn_start.setEnabled(false);
  //btn_stop.setEnabled(true);
  
  
 }
//////////////////////////////////////////////////////////////////////////////////////



}