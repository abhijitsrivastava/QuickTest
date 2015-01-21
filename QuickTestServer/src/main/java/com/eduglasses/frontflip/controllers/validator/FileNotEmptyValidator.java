package com.eduglasses.frontflip.controllers.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.web.multipart.commons.CommonsMultipartFile;

public class FileNotEmptyValidator implements
		ConstraintValidator<FileNotEmpty, CommonsMultipartFile> {

	@Override
	public void initialize(FileNotEmpty constraintAnnotation) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isValid(CommonsMultipartFile commonsMultipartFile,
			ConstraintValidatorContext context) {

		String fileName = commonsMultipartFile.getOriginalFilename();

		return !fileName.equals("");

	}
}
