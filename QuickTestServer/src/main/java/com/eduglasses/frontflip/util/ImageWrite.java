package com.eduglasses.frontflip.util;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.eduglasses.frontflip.domain.Question;
import com.eduglasses.frontflip.domain.Section;

public class ImageWrite {
	public static void imageWrite(Section section, Question question,
			int questionNumber) {
		String questionNumberText = "                                                       Question "
				+ questionNumber;
		String questionDescription = question.getQuestionDescription();
		String optionA = null;
		String optionB = null;
		String optionC = null;
		String optionD = null;
		String optionE = null;
		if (!"".equals(question.getOptionA())) {
			optionA = "A. " + question.getOptionA();
		}

		if (!"".equals(question.getOptionB())) {
			optionB = "B. " + question.getOptionB();
		}

		if (!"".equals(question.getOptionC())) {
			optionC = "C. " + question.getOptionC();
		}

		if (!"".equals(question.getOptionD())) {
			optionD = "D. " + question.getOptionD();
		}

		if (!"".equals(question.getOptionE())) {
			optionE = "E. " + question.getOptionE();
		}
		/*
		 * Because font metrics is based on a graphics context, we need to
		 * create a small, temporary image so we can ascertain the width and
		 * height of the final image
		 */
		BufferedImage img = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = img.createGraphics();
		Font font = new Font("Arial", Font.PLAIN, 24);
		g2d.setFont(font);
		FontMetrics fm = g2d.getFontMetrics();
		int width = 1000;// fm.stringWidth(text);
		int height = fm.getHeight() * 25;
		g2d.dispose();

		img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

		g2d = img.createGraphics();
		g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION,
				RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING,
				RenderingHints.VALUE_COLOR_RENDER_QUALITY);
		g2d.setRenderingHint(RenderingHints.KEY_DITHERING,
				RenderingHints.VALUE_DITHER_ENABLE);
		g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS,
				RenderingHints.VALUE_FRACTIONALMETRICS_ON);
		g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
				RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g2d.setRenderingHint(RenderingHints.KEY_RENDERING,
				RenderingHints.VALUE_RENDER_QUALITY);
		g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL,
				RenderingHints.VALUE_STROKE_PURE);
		g2d.setFont(font);
		fm = g2d.getFontMetrics();
		g2d.setColor(Color.BLACK);
		g2d.fillRect(0, 0, width, height);
		g2d.setColor(Color.WHITE);

		int lines = drawString(g2d, questionNumberText, 3, fm.getAscent() * 2,
				width);

		lines = lines
				+ drawString(g2d, questionDescription, 3,
						fm.getAscent() * 3 + 10, width) + 1;
		if (null != optionA)
			lines = lines
					+ drawString(g2d, optionA, 3,
							fm.getAscent() + fm.getAscent() * lines + 50, width);

		if (null != optionB)
			lines = lines
					+ drawString(g2d, optionB, 3,
							fm.getAscent() + fm.getAscent() * lines + 60, width);
		if (null != optionC)
			lines = lines
					+ drawString(g2d, optionC, 3,
							fm.getAscent() + fm.getAscent() * lines + 70, width);

		if (null != optionD)
			lines = lines
					+ drawString(g2d, optionD, 3,
							fm.getAscent() + fm.getAscent() * lines + 80, width);

		if (null != optionE)
			lines = lines
					+ drawString(g2d, optionE, 3,
							fm.getAscent() + fm.getAscent() * lines + 90, width);
		// g2d.drawString(text, 0, fm.getAscent());
		// g2d.drawString(text1, 0, fm.getAscent()*2);

		g2d.dispose();
		try {

			String lessonPath = PropertiesFileReaderUtil
					.getApplicationProperty("lesson.section.storage.path.lessons");

			File lessonIdfolder = new File(lessonPath + "/"
					+ section.getLesson().getId());
			if (!lessonIdfolder.exists()) {
				lessonIdfolder.mkdirs();
			}
			String storagePath = lessonPath + "/" + section.getLesson().getId()
					+ "/" + section.getId();
			File sectionIdfolder = new File(storagePath);
			if (!sectionIdfolder.exists()) {
				sectionIdfolder.mkdirs();
			}

			ImageIO.write(img, "png", new File(sectionIdfolder + "/"
					+ questionNumber + ".png"));

		} catch (IOException ex) {
			ex.printStackTrace();
		}

	}

	private static int drawString(Graphics2D g, String s, int x, int y,
			int width) {
		// FontMetrics gives us information about the width,
		// height, etc. of the current Graphics object's Font.
		int lines = 1;
		FontMetrics fm = g.getFontMetrics();

		int lineHeight = fm.getHeight();

		int curX = x;
		int curY = y;

		String[] words = s.split(" ");

		for (String word : words) {
			// Find out thw width of the word.
			int wordWidth = fm.stringWidth(word + " ");

			// If text exceeds the width, then move to next line.
			if (curX + wordWidth >= x + width) {
				lines++;
				curY += lineHeight;
				curX = x;
			}

			g.drawString(word, curX, curY);

			// Move over to the right for next word.
			curX += wordWidth;
		}
		return lines;
	}
}