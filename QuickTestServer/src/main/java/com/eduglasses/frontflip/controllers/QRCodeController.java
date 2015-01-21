package com.eduglasses.frontflip.controllers;

import java.io.File;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.eduglasses.frontflip.service.LessonService;
import com.eduglasses.frontflip.service.SectionService;
import com.eduglasses.frontflip.service.UserService;
import com.eduglasses.frontflip.util.FrontFlipUtil;
import com.eduglasses.frontflip.util.PropertiesFileReaderUtil;

@Controller
public class QRCodeController {

	@Autowired
	private UserService userService;

	@Autowired
	private LessonService lessonService;

	public LessonService getLessonService() {
		return lessonService;
	}

	public void setLessonService(LessonService lessonService) {
		this.lessonService = lessonService;
	}

	@Autowired
	private SectionService sectionService;

	public SectionService getSectionService() {
		return sectionService;
	}

	public void setSectionService(SectionService sectionService) {
		this.sectionService = sectionService;
	}

	@RequestMapping(value = "/generateQRCode", method = RequestMethod.GET)
	public ModelAndView showQRCode(HttpServletRequest request,
			@ModelAttribute("lessonId") Long lessonId) {

		/*
		 * In case user press F5 then below logic will check if glassName flash
		 * Attribute have some value or not. If it has empty value then F5 has
		 * been pressed. In this case display the same QR Code.
		 */
		boolean isQRCodeGenerated = false;
		String QRCodeFileLoc = PropertiesFileReaderUtil
				.getApplicationProperty("qr.code.storage.path");
		String QRCodeFileFullLoc = QRCodeFileLoc + "/" + lessonId + ".png";

		File qrCode = new File(QRCodeFileFullLoc);

		if (!qrCode.exists()) {

			String qrCodeStr = "";
		/*	List<Section> sections = sectionService
					.getAllSectionsForLesson(lessonService.getLesson(lessonId));

			Long sectionId = sections.get(0).getId();*/

			StringBuilder qrCodeStrBuilder = new StringBuilder("");
			qrCodeStrBuilder.append(PropertiesFileReaderUtil
					.getPropertyValue("server.url")
					+ "/lessons/"
					+ lessonId);
			/*qrCodeStrBuilder.append(",");
			qrCodeStrBuilder.append(sections.get(0).getNumberOfSlides());*/

			qrCodeStr = qrCodeStrBuilder.toString();

			isQRCodeGenerated = FrontFlipUtil.generateQRCode(qrCodeStr,
					lessonId);
		}
		ModelAndView modelAndView = new ModelAndView("QRCode");

		if (!isQRCodeGenerated) {
			modelAndView.addObject("QRCodeImageURL",
					FrontFlipUtil.getQRCodeImageUrl(lessonId));
			modelAndView.addObject("lessonId",lessonId);
		}

		return modelAndView;
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

}
