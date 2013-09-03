package br.com.caelum.brutauth.parameters;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import br.com.caelum.brutauth.reflection.methodsearchers.Argument;

public class RequestArgumentsParser {

	@Inject private HttpServletRequest request;
	
	public Argument[] parseArguments() {
		List<Argument> parameterList = new ArrayList<>();

		Enumeration<?> enumeration = request.getParameterNames();
		while (enumeration.hasMoreElements()) {
			String argumentName = (String) enumeration.nextElement();
			String[] argumentValues = request.getParameterValues(argumentName);
			for (String value : argumentValues) {
				Argument newParameter = new Argument(argumentName, value);
				parameterList.add(newParameter);
			}
		}
		return parameterList.toArray(new Argument[parameterList.size()]);
	}
}
