package com.eduglasses.frontflip.controllers.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.io.FilenameUtils;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

public class FileTypeMatchValidator implements
		ConstraintValidator<FileTypeMatch, CommonsMultipartFile> {

	@Override
	public void initialize(FileTypeMatch constraintAnnotation) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isValid(CommonsMultipartFile commonsMultipartFile,
			ConstraintValidatorContext context) {

		boolean typeOk = false;

		String fileName = commonsMultipartFile.getOriginalFilename();

		if (fileName.equals("")) {
			return true;
		}

		String fileExtension = FilenameUtils.getExtension(fileName);

		String fileTypes[] = { "ppt","pptx","mp4"};

		for (String string : fileTypes) {
			if (fileExtension.equals(string)) {
				typeOk = true;
			}
		}

		return typeOk;
	}

}
