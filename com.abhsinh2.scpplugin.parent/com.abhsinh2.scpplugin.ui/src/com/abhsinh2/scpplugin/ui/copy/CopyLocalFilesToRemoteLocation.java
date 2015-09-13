package com.abhsinh2.scpplugin.ui.copy;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.abhsinh2.scpplugin.ui.Logger;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

/**
 * Copies local files to remote machine on remote location.
 * 
 * @author abhsinh2
 *
 */
public class CopyLocalFilesToRemoteLocation {

	private String localFile;
	private String remoteMachine;
	private String remoteLocation;
	private String username;
	private String password;

	public CopyLocalFilesToRemoteLocation(String localFile, String remoteMachine,
			String remoteLocation, String username, String password) {
		this.localFile = localFile;
		this.remoteMachine = remoteMachine;
		this.remoteLocation = remoteLocation;
		this.username = username;
		this.password = password;
	}

	public void copy() {		
		FileInputStream fis = null;
		try {
			JSch jsch = new JSch();
			
			String userHomeDir = System.getProperty("user.home");
			String SSH_DIR = ".ssh";
			String KNOWN_HOSTS = "known_hosts";
			String ID_RSA = "id_rsa";
			
			File knowHostsFile = new File(userHomeDir + java.io.File.separator + SSH_DIR + java.io.File.separator + KNOWN_HOSTS);
			File idRSAFile = new File(userHomeDir + java.io.File.separator + SSH_DIR + java.io.File.separator + ID_RSA);			
						
			jsch.setKnownHosts(knowHostsFile.getPath());			
			jsch.addIdentity(idRSAFile.getPath());
			
			java.util.Properties config = new java.util.Properties(); 
			config.put("StrictHostKeyChecking", "no");			
			
			Session session = jsch.getSession(username, remoteMachine, 22);
			session.setConfig(config);
			
			// username and password will be given via UserInfo interface.
			RemoteUserInfo ui = new RemoteUserInfo(this.password);
			session.setUserInfo(ui);
			session.connect();
			session.setPassword(this.password);

			boolean ptimestamp = true;

			// exec 'scp -t rfile' remotely
			String command = "scp -i " + (ptimestamp ? "-p" : "") + " -t "
					+ this.remoteLocation;
			Channel channel = session.openChannel("exec");
			((ChannelExec) channel).setCommand(command);

			// get I/O streams for remote scp
			OutputStream out = channel.getOutputStream();
			InputStream in = channel.getInputStream();

			channel.connect();

			if (checkAck(in) != 0) {
				System.exit(0);
			}

			File _lfile = new File(this.localFile);

			if (ptimestamp) {
				command = "T " + (_lfile.lastModified() / 1000) + " 0";
				// The access time should be sent here,
				// but it is not accessible with JavaAPI ;-<
				command += (" " + (_lfile.lastModified() / 1000) + " 0\n");
				out.write(command.getBytes());
				out.flush();
				if (checkAck(in) != 0) {
					System.exit(0);
				}
			}

			// send "C0644 filesize filename", where filename should not include
			// '/'
			long filesize = _lfile.length();
			command = "C0644 " + filesize + " ";
			if (this.localFile.lastIndexOf('/') > 0) {
				command += this.localFile.substring(this.localFile
						.lastIndexOf('/') + 1);
			} else {
				command += this.localFile;
			}
			command += "\n";
			out.write(command.getBytes());
			out.flush();
			if (checkAck(in) != 0) {
				System.exit(0);
			}

			// send a content of lfile
			fis = new FileInputStream(this.localFile);
			byte[] buf = new byte[1024];
			while (true) {
				int len = fis.read(buf, 0, buf.length);
				if (len <= 0)
					break;
				out.write(buf, 0, len); // out.flush();
			}
			fis.close();
			fis = null;
			// send '\0'
			buf[0] = 0;
			out.write(buf, 0, 1);
			out.flush();
			if (checkAck(in) != 0) {
				System.exit(0);
			}
			out.close();

			channel.disconnect();
			session.disconnect();
		} catch (Exception e) {
			Logger.logError(e);
			try {
				if (fis != null)
					fis.close();
			} catch (Exception ee) {
			}
		}
	}

	private int checkAck(InputStream in) throws IOException {
		int b = in.read();
		// b may be 0 for success,
		// 1 for error,
		// 2 for fatal error,
		// -1
		if (b == 0)
			return b;
		if (b == -1)
			return b;

		if (b == 1 || b == 2) {
			StringBuffer sb = new StringBuffer();
			int c;
			do {
				c = in.read();
				sb.append((char) c);
			} while (c != '\n');
			if (b == 1) { // error
				System.out.print(sb.toString());
			}
			if (b == 2) { // fatal error
				System.out.print(sb.toString());
			}
		}
		return b;
	}

	@Override
	public String toString() {
		return "CopyLocalFilesToRemoteLocation [localFile=" + localFile
				+ ", remoteMachine=" + remoteMachine + ", remoteLocation="
				+ remoteLocation + ", username=" + username + ", password="
				+ password + "]";
	}
}
