package br.com.caelum.brutauth.reflection.methodsearchers;

import java.util.Arrays;

import br.com.caelum.brutauth.reflection.Arguments;
import br.com.caelum.vraptor.ioc.Component;

@Component
public class ArgumentMatcher {
	
	public Arguments getArgumentsThatMatchToParameters(Parameter[] parameters, Argument[] args) throws NoSuchMethodException {
		Argument[] argsToUse = new Argument[parameters.length];
		for (int i = 0; i < parameters.length; i++) {
			for (Argument arg : args) {
				if (arg != null && arg.getArgument().getClass().isAssignableFrom(parameters[i].getType())){
					if(argsToUse[i] != null)
						argsToUse[i] = matchNames(arg, argsToUse[i], parameters[i]);
					else
						argsToUse[i] = arg;
				}
			}
			if(argsToUse[i] == null) throw new IllegalArgumentException("Your resource method should recieve all the parameters that your rule needs: "+ Arrays.toString(parameters));
		}
		return new Arguments(argsToUse);
	}

	private Argument matchNames(Argument arg1, Argument arg2, Parameter parameter) {
		if(arg1.getName().equals(parameter.getName())) return arg1;
		if(arg2.getName().equals(parameter.getName())) return arg2;
		return null;
	}
	

}
