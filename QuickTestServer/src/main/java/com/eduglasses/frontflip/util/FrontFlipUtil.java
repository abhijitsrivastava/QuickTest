package com.eduglasses.frontflip.util;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.Properties;
import java.util.Random;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import net.glxn.qrgen.QRCode;
import net.glxn.qrgen.image.ImageType;

import org.apache.log4j.Logger;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.eduglasses.frontflip.domain.SectionTypeEnum;

public class FrontFlipUtil {

	private static final Logger logger = Logger.getLogger(FrontFlipUtil.class);

	/*
	 * Method returns the stack trace of exception in string format. Used for
	 * logging of exception.
	 */
	public static String getExceptionDescriptionString(Exception e) {
		StringWriter stringWriter = new StringWriter();
		PrintWriter printWriter = new PrintWriter(stringWriter);
		e.printStackTrace(printWriter);
		return stringWriter.toString();
	}

	public static boolean generateQRCode(String QRCodeStr, long random) {

		boolean isError = false;

		int width = new Integer(
				PropertiesFileReaderUtil
						.getPropertyValue("qr.code.image.width"));
		int height = new Integer(
				PropertiesFileReaderUtil
						.getPropertyValue("qr.code.image.height"));

		String QRCodeFileLoc = PropertiesFileReaderUtil
				.getApplicationProperty("qr.code.storage.path");

		ByteArrayOutputStream out = QRCode.from(QRCodeStr).to(ImageType.PNG)
				.withSize(width, height).stream();
		FileOutputStream fout = null;

		try {

			File glassResourceFolder = new File(QRCodeFileLoc);

			if (!glassResourceFolder.exists()) {
				glassResourceFolder.mkdirs();
			}

			String QRCodeFileFullLoc = QRCodeFileLoc + "/" + random + ".png";

			File QRCodeFile = new File(QRCodeFileFullLoc);
			if (QRCodeFile.exists()) {
				QRCodeFile.delete();
			}

			fout = new FileOutputStream(QRCodeFile);

			fout.write(out.toByteArray());

			fout.flush();

		} catch (FileNotFoundException ex) {
			isError = true;
			logger.fatal(FrontFlipUtil.getExceptionDescriptionString(ex));
		} catch (IOException ex) {
			isError = true;
			logger.fatal(FrontFlipUtil.getExceptionDescriptionString(ex));
		} finally {
			if (fout != null) {
				try {
					fout.close();
				} catch (IOException e1) {
					isError = true;
					logger.fatal(FrontFlipUtil
							.getExceptionDescriptionString(e1));
				}
			}
		}

		return isError;
	}

	public static int getRandomNumber() {
		Random random = new Random();
		return random.nextInt(10000000);
	}

	public static String getQRCodeImageUrl(long randomId) {
		String serverURL = PropertiesFileReaderUtil
				.getApplicationProperty("server.url");
		String QRCodeURL = PropertiesFileReaderUtil
				.getApplicationProperty("qr.code.url");
		String QRCodeImageURL = serverURL + "/" + QRCodeURL + "/" + randomId
				+ ".png";
		return QRCodeImageURL;
	}

	public static void deleteQRCodeFile(int randomId) {

		String QRCodeFileFullLoc = PropertiesFileReaderUtil
				.getApplicationProperty("qr.code.storage.path")
				+ "/"
				+ randomId + ".png";

		File QRCodeFile = new File(QRCodeFileFullLoc);

		if (QRCodeFile.exists()) {
			QRCodeFile.delete();
		}
	}

	private static double rad2deg(double rad) {
		return (rad * 180 / Math.PI);
	}

	private static double deg2rad(double deg) {
		return (deg * Math.PI / 180.0);
	}

	public static double distance(double lat1, double lon1, double lat2,
			double lon2) {
		double theta = lon1 - lon2;
		double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2))
				+ Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2))
				* Math.cos(deg2rad(theta));
		dist = Math.acos(dist);
		dist = rad2deg(dist);
		dist = dist * 60 * 1.1515;
		dist = dist * 1.609344;
		return (dist);
	}

	public static String uploadFileOnServer(CommonsMultipartFile file,
			SectionTypeEnum sectionTypeEnum, long lessonId, long sectionId)
			throws IOException, FileNotFoundException {

		String path = null;

		if (SectionTypeEnum.POWERPOINT.equals(sectionTypeEnum)
				|| SectionTypeEnum.QUIZ.equals(sectionTypeEnum)) {

			path = PropertiesFileReaderUtil
					.getApplicationProperty("lesson.section.storage.path.ppt");
		} else if (SectionTypeEnum.VIDEO.equals(sectionTypeEnum)) {

			String lessonPath = PropertiesFileReaderUtil
					.getApplicationProperty("lesson.section.storage.path.lessons");

			File lessonIdfolder = new File(lessonPath + "/" + lessonId);
			if (!lessonIdfolder.exists()) {
				lessonIdfolder.mkdirs();
			}
			path = lessonPath + "/" + lessonId + "/" + sectionId;
			File sectionIdfolder = new File(path);
			if (!sectionIdfolder.exists()) {
				sectionIdfolder.mkdirs();
			}
		}
		System.out.println("The fileLocation is " + path);

		FileOutputStream fOut = null;
		InputStream fIn = null;
		Date date = new Date();

		/*
		 * String convertedImageName = date.toString().replaceAll(":", "") +
		 * imageName.getOriginalFilename();
		 */

		String convertedImageName = (new Long(date.getTime())).toString()
				+ file.getOriginalFilename().replace(" ", "_");

		String storagePath = path + "/" + convertedImageName;

		fIn = file.getInputStream();
		fOut = new FileOutputStream(storagePath);

		byte[] buf = new byte[1024];
		int len;

		while ((len = fIn.read(buf)) > 0) {
			fOut.write(buf, 0, len);
		}

		fIn.close();
		fOut.close();

		return convertedImageName;
	}

	/**
	 * Method takes the data for post call and the request url.
	 * 
	 * @param postInput
	 * @param requestUrl
	 * @return JSONObject
	 */
	public static JSONObject postHttpUrlConnection(String postInput,
			String requestUrl) {

		JSONObject responseObject = null;
		InputStream in = null;
		HttpURLConnection conn = null;
		String jsonString = null;
		try {
			URL url = new URL(requestUrl);

			conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/json");

			String input = postInput;

			OutputStream os = conn.getOutputStream();
			os.write(input.getBytes());
			os.flush();

			if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
				System.out.println("Inside Error" + conn.getResponseCode());

				System.out.println("Inside Error " + conn.getResponseMessage());
				throw new RuntimeException("Failed : HTTP error code : "
						+ conn.getResponseCode());
			}

			in = new BufferedInputStream(conn.getInputStream());
			jsonString = getStringFromInputStream(in);
			System.out.println("JSON Response : " + jsonString);
			responseObject = new JSONObject(jsonString);

			conn.disconnect();

		} catch (MalformedURLException e) {

			e.printStackTrace();

		} catch (IOException e) {

			e.printStackTrace();

		} catch (RuntimeException e) {
			jsonString = "{\"message\":\"Internal server error\",\"statusCode\":\"err001\"}";
			try {
				responseObject = new JSONObject(jsonString);
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return responseObject;

	}

	/**
	 * Method returns String from input stream.
	 * 
	 * @param is
	 * @return jsonString
	 */
	public static String getStringFromInputStream(InputStream is) {

		BufferedReader br = null;
		StringBuilder sb = new StringBuilder();

		String line;
		try {

			br = new BufferedReader(new InputStreamReader(is));
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return sb.toString();

	}

	/**
	 * Sends email
	 * @param receipent
	 * @param content
	 * @throws MessagingException
	 */
	public static void sendEmailToUser(String receipent, String content)
			throws MessagingException {

		String senderEmailID = PropertiesFileReaderUtil
				.getApplicationProperty("email.username");

		Properties props = new Properties();

		props.put("mail.smtp.user", senderEmailID);
		props.put("mail.smtp.host", PropertiesFileReaderUtil
				.getApplicationProperty("email.smtp.server"));
		props.put("mail.smtp.port", PropertiesFileReaderUtil
				.getApplicationProperty("email.smtp.port"));
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.ssl.enable", "true");
		props.put("mail.smtp.starttls.enable", "true");

		Authenticator auth = new SMTPAuthenticator();
		javax.mail.Session session = javax.mail.Session
				.getInstance(props, auth);

		MimeMessage message = new MimeMessage(session);
		message.setSubject("Your FrontFlip Password!");
		message.setFrom(new InternetAddress(senderEmailID));
		message.addRecipient(Message.RecipientType.TO, new InternetAddress(
				receipent));
		message.setSentDate(new Date());

		message.setText("Username: " + receipent + "\nPassword: " + content
				+ "\n\nRegards,\nEduGlasses");

		Transport.send(message);
	}

	private static class SMTPAuthenticator extends javax.mail.Authenticator {
		public PasswordAuthentication getPasswordAuthentication() {
			return new PasswordAuthentication(
					PropertiesFileReaderUtil
							.getApplicationProperty("email.username"),
					PropertiesFileReaderUtil
							.getApplicationProperty("email.password"));
		}
	}

}
