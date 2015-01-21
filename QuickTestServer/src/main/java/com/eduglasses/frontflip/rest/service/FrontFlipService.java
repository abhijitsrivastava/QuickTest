package com.eduglasses.frontflip.rest.service;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.eduglasses.frontflip.domain.Section;
import com.eduglasses.frontflip.domain.SectionTypeEnum;
import com.eduglasses.frontflip.domain.SlideNotes;
import com.eduglasses.frontflip.dto.LessonDTO;
import com.eduglasses.frontflip.dto.SectionDTO;
import com.eduglasses.frontflip.service.LessonService;
import com.eduglasses.frontflip.service.SectionService;

@Path("/frontflip-service")
public class FrontFlipService {

	private static final Logger logger = Logger
			.getLogger(FrontFlipService.class);

	ApplicationContext appContext = new ClassPathXmlApplicationContext(
			"../applicationContext.xml");

	@Path("/text")
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String text() {
		return "Hello Jersey";
	}

	@GET
	@Path("/get-lesson-details")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getLessonDetails(@QueryParam("lessonId") long lessonId) {
		SectionService sectionService = (SectionService) appContext
				.getBean("sectionService");
		LessonService lessonService = (LessonService) appContext
				.getBean("lessonService");
		LessonDTO lessonDTO = new LessonDTO();
		List<Section> sections = sectionService
				.getAllSectionsForLesson(lessonService.getLesson(lessonId));

		for (Section section : sections) {
			SectionDTO sectionDTO = new SectionDTO();
			lessonDTO.getSections().add(sectionDTO);
			sectionDTO.setId(section.getId());
			sectionDTO.setSectionDetails(section.getSectionDetails());
			sectionDTO.setSectionName(section.getSectionName());
			sectionDTO.setSectionType(section.getSectionType());
			if (section.getSectionType().equals(SectionTypeEnum.POWERPOINT)) {
				List<SlideNotes> slideNotesList = sectionService.getSlideNotesForSection(section.getId());
				for (SlideNotes slideNotes : slideNotesList) {
					if (sectionDTO.getSlideNotes() == null) {
						sectionDTO.setSlideNotes(new ArrayList<String>());
					}
					sectionDTO.getSlideNotes().add(slideNotes.getNotes());
 				}
			}
			
			sectionDTO.setNumberOfSlides(section.getNumberOfSlides());
		}
		return Response.status(200).entity(lessonDTO).build();
	}
}
