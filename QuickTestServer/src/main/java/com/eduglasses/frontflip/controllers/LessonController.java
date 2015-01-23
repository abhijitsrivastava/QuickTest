package com.eduglasses.frontflip.controllers;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.eduglasses.frontflip.controllers.form.AddLessonForm;
import com.eduglasses.frontflip.controllers.form.AddQuestionForm;
import com.eduglasses.frontflip.controllers.form.AddSectionForm;
import com.eduglasses.frontflip.controllers.form.LessonDetailsForm;
import com.eduglasses.frontflip.controllers.form.SecurityLiveStreamForm;
import com.eduglasses.frontflip.domain.Lesson;
import com.eduglasses.frontflip.domain.Question;
import com.eduglasses.frontflip.domain.Section;
import com.eduglasses.frontflip.domain.SectionTypeEnum;
import com.eduglasses.frontflip.service.LessonService;
import com.eduglasses.frontflip.service.QuestionService;
import com.eduglasses.frontflip.service.SectionService;
import com.eduglasses.frontflip.service.UserService;
import com.eduglasses.frontflip.util.PropertiesFileReaderUtil;

@Controller
public class LessonController {

	@Autowired
	private UserService userService;

	@Autowired
	private LessonService lessonService;

	@Autowired
	private SectionService sectionService;

	@Autowired
	private QuestionService questionService;

	@RequestMapping(value = "/home", method = RequestMethod.GET)
	public String prepareHome(ModelMap map, HttpServletRequest request) {

		HttpSession session = request.getSession();

		if (null == (String) session.getAttribute("email")) {
			return "redirect:/registration.htm";
		}

		SecurityLiveStreamForm liveStreamForm = new SecurityLiveStreamForm();
		map.addAttribute("securityLiveStreamForm", liveStreamForm);

		List<Lesson> list = lessonService.getAllLessons(userService
				.getUserByEmail(((String) session.getAttribute("email"))));
		session.setAttribute("lessonList", list);

		return "home";
	}

	@RequestMapping(value = "/showAddLessonForm", method = RequestMethod.GET)
	public ModelAndView showAddLessonForm(ModelMap map,
			HttpServletRequest request) {
		ModelAndView modelAndView = new ModelAndView("addLesson");

		AddLessonForm form = new AddLessonForm();
		modelAndView.addObject("addLessonForm", form);

		return modelAndView;
	}

	@RequestMapping(value = "/showAddQuestionForm", method = RequestMethod.GET)
	public ModelAndView showAddQuestionForm(ModelMap map,
			HttpServletRequest request) {
		ModelAndView modelAndView = new ModelAndView("addQuestion");

		AddQuestionForm form = new AddQuestionForm();
		modelAndView.addObject("addQuestionForm", form);

		return modelAndView;
	}

	@RequestMapping(value = "/addQuestion", method = RequestMethod.POST)
	public String addQuestion(
			final RedirectAttributes redirectAttributes,
			@Valid @ModelAttribute("addQuestionForm") AddQuestionForm addQuestionForm,
			BindingResult result, ModelMap map, HttpServletRequest request) {
		HttpSession session = request.getSession();

		if (result.hasErrors()) {
			return "addQuestion";
		}
		addQuestionForm.setSectionId((Long) session.getAttribute("sectionId"));
		questionService.saveQuestion(addQuestionForm);

		return "redirect:/questions.htm?sectionId="
				+ addQuestionForm.getSectionId();

	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public LessonService getLessonService() {
		return lessonService;
	}

	public void setLessonService(LessonService lessonService) {
		this.lessonService = lessonService;
	}

	@RequestMapping(value = "/addLesson", method = RequestMethod.POST)
	public String addLesson(
			final RedirectAttributes redirectAttributes,
			@Valid @ModelAttribute("addLessonForm") AddLessonForm addLessonForm,
			BindingResult result, ModelMap map, HttpServletRequest request) {
		HttpSession session = request.getSession();

		if (result.hasErrors()) {
			return "addLesson";
		}

		lessonService.saveLesson(addLessonForm,
				((String) session.getAttribute("email")));

		return "redirect:/home.htm";

	}

	@RequestMapping(value = "/showAddSectionForm", method = RequestMethod.GET)
	public ModelAndView showAddSectionForm(ModelMap map,
			HttpServletRequest request) {
		ModelAndView modelAndView = new ModelAndView("addSection");
		HttpSession session = request.getSession();

		AddSectionForm form = new AddSectionForm();
		form.setLessonId((Long) session.getAttribute("lessonId"));
		modelAndView.addObject("addSectionForm", form);

		return modelAndView;
	}

	@RequestMapping(value = "/questions", method = RequestMethod.GET)
	public ModelAndView questions(@RequestParam Long sectionId,
			HttpServletRequest request) {
		HttpSession session = request.getSession();

		ModelAndView modelAndView = new ModelAndView("questions");

		List<Question> questions = questionService
				.getQuestionsForSection(sectionId);

		session.setAttribute("questionList", questions);
		session.setAttribute("sectionId", sectionId);

		return modelAndView;
	}

	@RequestMapping(value = "/lessonDetails", method = RequestMethod.GET)
	public ModelAndView lessonDetails(@RequestParam Long lessonId,
			HttpServletRequest request) {
		HttpSession session = request.getSession();

		if (null == lessonId) {
			lessonId = (Long) session.getAttribute("lessonId");
		}
		ModelAndView modelAndView = new ModelAndView("lessonDetails");

		Lesson lesson = lessonService.getLesson(lessonId);

		LessonDetailsForm lessonDetailsForm = new LessonDetailsForm();
		lessonDetailsForm.setId(lessonId);
		lessonDetailsForm.setLessonDescription(lesson.getLessonDescription());
		lessonDetailsForm.setLessonName(lesson.getLessonName());

		modelAndView.addObject("lessonDetailsForm", lessonDetailsForm);

		List<Section> sections = sectionService.getAllSectionsForLesson(lesson);

		session.setAttribute("sectionList", sections);
		session.setAttribute("lessonId", lessonId);

		return modelAndView;
	}

	@RequestMapping(value = "/deleteLesson", method = RequestMethod.GET)
	public String deteleLesson(@RequestParam Long lessonId,
			HttpServletRequest request) {
		Lesson lesson = lessonService.getLesson(lessonId);

		lesson.setDeleted(true);
		lessonService.saveLesson(lesson);

		return "redirect:/home.htm";
	}

	@RequestMapping(value = "/deleteSection", method = RequestMethod.GET)
	public String deleteSection(@RequestParam Long sectionId,
			HttpServletRequest request) {
		Section section = sectionService.getSection(sectionId);
		HttpSession session = request.getSession();

		section.setDeleted(true);
		sectionService.saveSection(section);

		return "redirect:/lessonDetails.htm?lessonId="
				+ (Long) session.getAttribute("lessonId");
	}

	@RequestMapping(value = "/addSection", method = RequestMethod.POST)
	public String addSection(
			final RedirectAttributes redirectAttributes,
			@Valid @ModelAttribute("addSectionForm") AddSectionForm addSectionForm,
			BindingResult result, ModelMap map, HttpServletRequest request) {
		HttpSession session = request.getSession();

		if (result.hasErrors()) {
			return "addSection";
		}

		if (addSectionForm.getSectionType() == -1) {
			result.rejectValue("sectionType", "", "Please select a Section.");
			return "addSection";
		} else if (SectionTypeEnum.get(addSectionForm.getSectionType()).equals(
				SectionTypeEnum.POWERPOINT)) {
			if (null == addSectionForm.getSectionDetails()
					.getOriginalFilename()) {
				result.rejectValue("sectionDetails", "",
						"Please provide pptx/ppt file for upload.");
				return "addSection";
			} else if ("".equalsIgnoreCase(addSectionForm.getSectionDetails()
					.getOriginalFilename())) {
				result.rejectValue("sectionDetails", "",
						"Please provide pptx/ppt file for upload.");
				return "addSection";
			} else if (!addSectionForm.getSectionDetails()
					.getOriginalFilename().contains(".pptx")
					&& !addSectionForm.getSectionDetails()
							.getOriginalFilename().contains(".ppt")) {
				result.rejectValue("sectionDetails", "",
						"Please provide pptx/ppt file for upload.");
				return "addSection";
			}

		} else if (SectionTypeEnum.get(addSectionForm.getSectionType()).equals(
				SectionTypeEnum.VIDEO)) {
			if (null == addSectionForm.getSectionDetails()
					.getOriginalFilename()) {
				result.rejectValue("sectionDetails", "",
						"Please provide Video for upload.");
				return "addSection";
			} else if ("".equalsIgnoreCase(addSectionForm.getSectionDetails()
					.getOriginalFilename())) {
				result.rejectValue("sectionDetails", "",
						"Please provide Video for upload.");
				return "addSection";
			} else if (!addSectionForm.getSectionDetails()
					.getOriginalFilename().contains(".mp4")) {
				result.rejectValue("sectionDetails", "",
						"Please provide a mp4 Video for upload.");
				return "addSection";
			}

		}/*
		 * else if (SectionTypeEnum.get(addSectionForm.getSectionType()).equals(
		 * SectionTypeEnum.EDUGRADE) && (null == addSectionForm.getTeacherId()
		 * || "" .equalsIgnoreCase(addSectionForm.getTeacherId()))) {
		 * result.rejectValue("teacherId", "",
		 * "Please provide TeacherId for EduGrade."); return "addSection"; }
		 */

		addSectionForm.setLessonId((Long) session.getAttribute("lessonId"));
		sectionService.saveSection(addSectionForm);

		return "redirect:/lessonDetails.htm?lessonId="
				+ addSectionForm.getLessonId();

	}

	@RequestMapping(value = "/viewSlideShow", method = RequestMethod.GET)
	public String viewSlideShow(@RequestParam boolean isTeacher,@RequestParam Long lessonId,
			ModelMap map,
			HttpServletRequest request) {
		HttpSession session = request.getSession();

		map.addAttribute("brokerUrl",
				PropertiesFileReaderUtil.getApplicationProperty("broker-url"));

		Lesson lesson = lessonService.getLesson(lessonId);

		List<Section> sections = sectionService.getAllSectionsForLesson(lesson);

		long teacherId = lesson.getUser().getEduGradeTeacherId();

		if (sections.size() == 0) {
			return "redirect:/lessonDetails.htm?lessonId="
					+ (Long) session.getAttribute("lessonId");

		}
		long sectionId = 0;
		long numberOfQuestions = 0;
		for (Section section : sections) {
			if (section.getSectionType().equals(SectionTypeEnum.QUIZ)) {
				sectionId = section.getId();
				numberOfQuestions = section.getNumberOfSlides();
			}
		}

		map.addAttribute("topic",
				PropertiesFileReaderUtil.getApplicationProperty("base.topic")
						+ lessonId);
		map.addAttribute("teacherId", "" + teacherId);
		map.addAttribute("lessonId", "" + lessonId);
		map.addAttribute("sectionId", "" + sectionId);
		map.addAttribute("numberOfQuestions", "" + numberOfQuestions);
		map.addAttribute("isTeacher", "" + isTeacher);
		
		map.addAttribute("serverUrl",
				PropertiesFileReaderUtil.getPropertyValue("server.url")
						+ "/lessons/" + lessonId + "/" + sectionId);

		return "slideShow";

	}

	public SectionService getSectionService() {
		return sectionService;
	}

	public void setSectionService(SectionService sectionService) {
		this.sectionService = sectionService;
	}

}
