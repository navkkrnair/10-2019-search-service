package com.ams.search.exception;

import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SearchErrorBuilder
{
	private static final SearchErrorBuilder builder = new SearchErrorBuilder();
	private String                          errorMessage;

	public static SearchErrorBuilder create()
	{
		return builder;
	}

	public SearchErrorBuilder withError(String errorMessage)
	{
		this.errorMessage = errorMessage;
		return this;
	}

	public SearchError build()
	{
		return new SearchError(this.errorMessage);
	}

	public static SearchError fromBindingErrors(Errors errors)
	{
		SearchError error = new SearchError("Validation failed on "
				+ StringUtils.capitalize(errors.getObjectName()) + ". "
				+ errors.getErrorCount()
				+ " error(s)");
		errors.getFieldErrors()
			.forEach(fe -> error
				.addSearchValidationError(fe.getField() + " " + fe.getDefaultMessage()));
		return error;
	}
}
