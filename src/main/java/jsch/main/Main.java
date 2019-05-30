package jsch.main;

import java.io.File;

import jsch.client.Client;
import jsch.server.SftpServer;

public class Main {

	public static void main(String[] args) {
		SftpServer server= new SftpServer();
		server.start();
		Client client = new Client();
		
		client.upload(new File("C:\\Users\\Dell\\Desktop\\video.mp4"), true);
	}

}
