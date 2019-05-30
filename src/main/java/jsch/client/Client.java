package jsch.client;

import java.io.File;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpProgressMonitor;

public class Client {

	final static String USER="";
	final static String HOST="0.0.0.0";
	final static int PORT=22;
	final static String PASSWORD="password";
	public void upload(File file, boolean retry) {
	    try 
	    {
	        System.out.println("Uplodaing file " + file.getName());

	        JSch jsch = new JSch();
	        Session session =  jsch.getSession(USER, HOST, PORT);
	        session.setPassword(PASSWORD);

	        java.util.Properties config = new java.util.Properties();
	        config.put("StrictHostKeyChecking", "no");
	        session.setConfig(config);

	        session.connect();

	        Channel channel = session.openChannel("sftp");
	        channel.connect();
	        ChannelSftp sftpChannel = (ChannelSftp) channel;
	        SftpProgressMonitor progressMonitor= new SftpProgressMonitor() {
				
				@Override
				public void init(int op, String src, String dest, long max) {
					System.out.println("File transfer begin..");
				}
				
				@Override
				public void end() {
					System.out.println("File transfer end od file" + file.getName());
					
				}
				
				@Override
				public boolean count(long count) {
					return true;
				}
			};
	        if (!retry)
	            sftpChannel.put(file.getAbsolutePath(), file.getName(),progressMonitor, ChannelSftp.OVERWRITE);
	        else
	            sftpChannel.put(file.getAbsolutePath(), file.getName(),progressMonitor ,ChannelSftp.RESUME);

	        channel.disconnect();
	        session.disconnect();
	    } 
	    catch (Exception e) 
	    {
	        e.printStackTrace();
	        upload(file, true);
	    }

	}
}
