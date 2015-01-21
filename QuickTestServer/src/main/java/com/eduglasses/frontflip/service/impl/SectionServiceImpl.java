package com.eduglasses.frontflip.service.impl;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.apache.poi.hslf.model.Notes;
import org.apache.poi.hslf.model.Shape;
import org.apache.poi.hslf.model.Slide;
import org.apache.poi.hslf.model.TextShape;
import org.apache.poi.hslf.usermodel.SlideShow;
import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFNotes;
import org.apache.poi.xslf.usermodel.XSLFShape;
import org.apache.poi.xslf.usermodel.XSLFSlide;
import org.apache.poi.xslf.usermodel.XSLFTextParagraph;
import org.apache.poi.xslf.usermodel.XSLFTextShape;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.eduglasses.frontflip.controllers.form.AddSectionForm;
import com.eduglasses.frontflip.dao.LessonDao;
import com.eduglasses.frontflip.dao.SectionDao;
import com.eduglasses.frontflip.dao.UserDao;
import com.eduglasses.frontflip.domain.Lesson;
import com.eduglasses.frontflip.domain.Section;
import com.eduglasses.frontflip.domain.SectionTypeEnum;
import com.eduglasses.frontflip.domain.SlideNotes;
import com.eduglasses.frontflip.domain.User;
import com.eduglasses.frontflip.service.SectionService;
import com.eduglasses.frontflip.util.FrontFlipUtil;
import com.eduglasses.frontflip.util.PropertiesFileReaderUtil;

@Service("sectionService")
public class SectionServiceImpl implements SectionService {

	@Autowired
	private SectionDao sectionDao;

	@Autowired
	private LessonDao lessonDao;

	@Autowired
	private UserDao userDao;

	public LessonDao getLessonDao() {
		return lessonDao;
	}

	public void setLessonDao(LessonDao lessonDao) {
		this.lessonDao = lessonDao;
	}

	public SectionDao getSectionDao() {
		return sectionDao;
	}

	public void setSectionDao(SectionDao sectionDao) {
		this.sectionDao = sectionDao;
	}

	public List<Section> getAllSectionsForLesson(Lesson lesson) {
		return sectionDao.getAllSectionsForLesson(lesson);
	}

	public Section saveSection(Section section) {
		return sectionDao.save(section);
	}

	public Section getSection(Long id) {
		return sectionDao.get(id);
	}

	public void saveSection(AddSectionForm addSectionForm) {
		String filePath = null;
		CommonsMultipartFile commonsMultipartFile = (CommonsMultipartFile) addSectionForm
				.getSectionDetails();

		try {
			if (SectionTypeEnum.get(addSectionForm.getSectionType()).equals(
					SectionTypeEnum.POWERPOINT)) {
				filePath = FrontFlipUtil.uploadFileOnServer(
						commonsMultipartFile,
						SectionTypeEnum.get(addSectionForm.getSectionType()),
						addSectionForm.getLessonId(), 0);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Lesson lesson = lessonDao.get(addSectionForm.getLessonId());
		Section section = new Section();
		section.setSectionDescription(addSectionForm.getSectionDescription());
		section.setSectionName(addSectionForm.getSectionName());
		section.setSectionType(SectionTypeEnum.get(addSectionForm
				.getSectionType()));
		section.setSectionDetails(filePath);
		if (SectionTypeEnum.get(addSectionForm.getSectionType()).equals(
				SectionTypeEnum.QUIZ)) {

			if (lesson.getUser().getEduGradeTeacherId() == 0) {
				String requestUrl = PropertiesFileReaderUtil
						.getApplicationProperty("edugrade.service.url");

				JSONObject json = new JSONObject();
				try {
					json.put("emailId", lesson.getUser().getEmail());
					JSONObject responseJsonObject = FrontFlipUtil
							.postHttpUrlConnection(json.toString(), requestUrl);
					String status = responseJsonObject.getString("statusCode");
					if ("EDU_101".equals(status)) {
						long teacherId = responseJsonObject
								.getLong("teacherId");
						addSectionForm.setTeacherId(teacherId + "");
						User user = lesson.getUser();
						user.setEduGradeTeacherId(teacherId);
						userDao.save(user);
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				addSectionForm.setTeacherId(lesson.getUser()
						.getEduGradeTeacherId() + "");

			}

			section.setSectionDetails(addSectionForm.getTeacherId());

		}

		section.setCreated(new Date());
		section.setUpdated(new Date());
		section.setLesson(lesson);
		section = sectionDao.save(section);
		if (SectionTypeEnum.get(addSectionForm.getSectionType()).equals(
				SectionTypeEnum.POWERPOINT)) {
			filePath = PropertiesFileReaderUtil
					.getApplicationProperty("lesson.section.storage.path.ppt")
					+ "/" + filePath;
			String lessonPath = PropertiesFileReaderUtil
					.getApplicationProperty("lesson.section.storage.path.lessons");

			File lessonIdfolder = new File(lessonPath + "/"
					+ addSectionForm.getLessonId());
			if (!lessonIdfolder.exists()) {
				lessonIdfolder.mkdirs();
			}
			String storagePath = lessonPath + "/"
					+ addSectionForm.getLessonId() + "/" + section.getId();
			File sectionIdfolder = new File(storagePath);
			if (!sectionIdfolder.exists()) {
				sectionIdfolder.mkdirs();
			}

			try {
				int numberOfSlides = convertPowerPointToImages(filePath,
						storagePath, section);
				if (numberOfSlides > 0) {
					section.setNumberOfSlides(numberOfSlides);
					sectionDao.save(section);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} else if (SectionTypeEnum.get(addSectionForm.getSectionType()).equals(
				SectionTypeEnum.VIDEO)) {
			try {
				filePath = FrontFlipUtil.uploadFileOnServer(
						commonsMultipartFile,
						SectionTypeEnum.get(addSectionForm.getSectionType()),
						addSectionForm.getLessonId(), section.getId());

				section.setSectionDetails(filePath);
				sectionDao.save(section);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	public int convertPowerPointToImages(String filePath, String storagePath,
			Section section) throws IOException {

		int numberOfSlides = 0;

		FileInputStream is = new FileInputStream(filePath);

		if (filePath.contains(".pptx")) {
			XMLSlideShow ppt = new XMLSlideShow(is);

			is.close();

			double zoom = 1.4; // magnify it by 2
			AffineTransform at = new AffineTransform();
			at.setToScale(zoom, zoom);

			Dimension pgsize = ppt.getPageSize();

			XSLFSlide[] slide = ppt.getSlides();
			int pageWidth = (int) ((int) pgsize.width * zoom);
			int pageHeight = (int) ((int) pgsize.height * zoom);
			numberOfSlides = slide.length;
			for (int i = 0; i < slide.length; i++) {

				BufferedImage img = new BufferedImage(pageWidth, pageHeight,
						BufferedImage.TYPE_INT_RGB);
				Graphics2D graphics = img.createGraphics();
				// clear the drawing area
				graphics.setPaint(Color.white);
				graphics.fill(new Rectangle2D.Float(0, 0, pageWidth, pageHeight));
				graphics.setTransform(at); // render
				slide[i].draw(graphics);

				// save the output
				FileOutputStream out = new FileOutputStream(storagePath + "/"
						+ (i + 1) + ".png");
				javax.imageio.ImageIO.write(img, "png", out);
				out.close();

				XSLFNotes mynotes = slide[i].getNotes();
				StringBuilder notes = new StringBuilder();
				SlideNotes slideNotes = new SlideNotes();
				slideNotes.setSection(section);
				slideNotes.setCreated(new Date());
				slideNotes.setUpdated(new Date());
				if (null != mynotes) {
					for (XSLFShape shape : mynotes) {
						if (shape instanceof XSLFTextShape) {
							XSLFTextShape txShape = (XSLFTextShape) shape;
							for (XSLFTextParagraph xslfParagraph : txShape
									.getTextParagraphs()) {
								if (!"".equals(xslfParagraph.getText())) {
									notes.append(xslfParagraph.getText() + "/n");
									/*
									 * System.out.println("slide number " + i +
									 * " " + xslfParagraph.getText());
									 */
								}
							}
						}
					}
				}
				slideNotes.setNotes(notes.toString());
				sectionDao.saveSlideNotes(slideNotes);
			}
		} else {
			SlideShow ppt = new SlideShow(is);

			Slide[] slide = ppt.getSlides();

			is.close();

			double zoom = 1.4; // magnify it by 2
			AffineTransform at = new AffineTransform();
			at.setToScale(zoom, zoom);

			Dimension pgsize = ppt.getPageSize();

			int pageWidth = (int) ((int) pgsize.width * zoom);
			int pageHeight = (int) ((int) pgsize.height * zoom);
			numberOfSlides = slide.length;
			for (int i = 0; i < slide.length; i++) {

				BufferedImage img = new BufferedImage(pageWidth, pageHeight,
						BufferedImage.TYPE_INT_RGB);
				Graphics2D graphics = img.createGraphics();
				// clear the drawing area
				graphics.setPaint(Color.white);
				graphics.fill(new Rectangle2D.Float(0, 0, pageWidth, pageHeight));
				graphics.setTransform(at); // render
				slide[i].draw(graphics);

				// save the output
				FileOutputStream out = new FileOutputStream(storagePath + "/"
						+ (i + 1) + ".png");
				javax.imageio.ImageIO.write(img, "png", out);
				out.close();

				Notes mynotes = slide[i].getNotesSheet();
				StringBuilder notes = new StringBuilder();
				SlideNotes slideNotes = new SlideNotes();
				slideNotes.setSection(section);
				slideNotes.setCreated(new Date());
				slideNotes.setUpdated(new Date());
				if (null != mynotes) {

					for (Shape shape : mynotes.getShapes()) {
						if (shape instanceof TextShape) {
							TextShape txShape = (TextShape) shape;
							if (!"".equals(txShape.getText())) {
								notes.append(txShape.getText() + "/n");
								/*
								 * System.out.println("slide number " + i + " "
								 * + xslfParagraph.getText());
								 */
							}

						}
					}
				}
				slideNotes.setNotes(notes.toString());
				sectionDao.saveSlideNotes(slideNotes);
			}

		}

		return numberOfSlides;
	}

	public List<SlideNotes> getSlideNotesForSection(long section_id) {
		return sectionDao.getSlideNotesForSection(section_id);
	}
}
