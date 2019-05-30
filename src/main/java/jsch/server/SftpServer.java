package jsch.server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.sshd.common.NamedFactory;
import org.apache.sshd.server.Command;
import org.apache.sshd.server.SshServer;
import org.apache.sshd.server.auth.UserAuth;
import org.apache.sshd.server.auth.password.PasswordAuthenticator;
import org.apache.sshd.server.auth.password.PasswordChangeRequiredException;
import org.apache.sshd.server.auth.password.UserAuthPasswordFactory;
import org.apache.sshd.server.keyprovider.SimpleGeneratorHostKeyProvider;
import org.apache.sshd.server.scp.ScpCommandFactory;
import org.apache.sshd.server.session.ServerSession;
import org.apache.sshd.server.shell.ProcessShellFactory;
import org.apache.sshd.server.subsystem.sftp.SftpSubsystemFactory;

public class SftpServer {
	public void start() {

		List<NamedFactory<UserAuth>> userAuthFactories = new ArrayList<NamedFactory<UserAuth>>();
		userAuthFactories.add(new UserAuthPasswordFactory());

		List<NamedFactory<Command>> sftpCommandFactory = new ArrayList<NamedFactory<Command>>();
		sftpCommandFactory.add(new SftpSubsystemFactory());

		SshServer sshd = SshServer.setUpDefaultServer();
		sshd.setPort(22);
		sshd.setKeyPairProvider(new SimpleGeneratorHostKeyProvider());
		sshd.setUserAuthFactories(userAuthFactories);
		sshd.setPasswordAuthenticator(new PasswordAuthenticator() {

			@Override
			public boolean authenticate(String username, String password, ServerSession session)
					throws PasswordChangeRequiredException {
				
				return true;
			}
		});
		sshd.setCommandFactory(new ScpCommandFactory());
		sshd.setShellFactory(new ProcessShellFactory(new String[] { "/bin/sh", "-i", "-l" }));
		sshd.setSubsystemFactories(sftpCommandFactory);

		try {
			sshd.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
