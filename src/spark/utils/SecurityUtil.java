package spark.utils;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 功能描述：MD5及SHA加密
 * 
 * @author freedom.xie
 * @version 版本：1.0
 */
final public class SecurityUtil {

	// 使用MD5加密
	public synchronized static final String toMd5(String data) {
		try {
			MessageDigest digest = MessageDigest.getInstance("MD5");
			digest.update(data.getBytes());
			return encodeHex(digest.digest());
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		}
	}

	// 使用SHA加密
	public synchronized static final String toSHA(String data) {
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-1");
			digest.update(data.getBytes());
			return encodeHex(digest.digest());
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		}
	}

	// 补位操作
	public static final String encodeHex(byte[] bytes) {
		StringBuffer buf = new StringBuffer(bytes.length * 2);
		int i;
		for (i = 0; i < bytes.length; i++) {
			if (((int) bytes[i] & 0xff) < 0x10) {
				buf.append("0");
			}
			buf.append(Long.toString((int) bytes[i] & 0xff, 16));
		}
		return buf.toString().toUpperCase();
	}

	public static final void createLience(String msg) {
		DataOutputStream out = null;
		System.out.println(toSHA(msg));
		//char []info = "2012-12-34hello".toCharArray();
		try {
			out=new DataOutputStream(new FileOutputStream("/home/xd/temp/lience.bin"));
			out.writeInt(123);
//			for (char c : info)
//			    out.writeChar(c);
//			 out.writeChar('\000');
			 out.writeInt(123);
			 out.writeByte(1);
			 out.writeByte(2);
			 out.writeByte(3);
			 out.writeByte(4);
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			try {
				if (out != null)
					out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	public static final void readLience() {
		DataInputStream in = null;
		try {
			in = new DataInputStream(new FileInputStream("/home/xd/temp/lience.bin")); 
			Character c=  null;
			StringBuilder sb = new StringBuilder();
		    while((c = in.readChar()) != '\000') {
		    	sb.append(c);
		    }
		    System.out.println(sb.toString());
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			try {
				if (in != null)
					in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	public static void main(String ...args) {
		createLience("2012-12-34hello");
		System.out.println("begin read.....");
		readLience();
	}
}
