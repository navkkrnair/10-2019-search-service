package com.ams.search.exception;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class SearchError
{
	private final String errorMessage;
	private List<String> errors = new ArrayList<String>();

	public void addSearchValidationError(String error)
	{
		this.errors.add(error);
	}

}
