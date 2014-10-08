package br.com.caelum.brutauth.reflection;

import java.lang.reflect.Method;

public class NamedParametersMethod {

    private Method method;

	public NamedParametersMethod(Method method) {
		this.method = method;
	}

    public Parameter[] getParameters() {
        /*Paranamer paranamer = new CachingParanamer(new AdaptiveParanamer());
        String[] lookupParameterNames = paranamer.lookupParameterNames(method, true);
        Class<?>[] parameterTypes = method.getParameterTypes();
        return namedParametersFor(lookupParameterNames, parameterTypes);*/

        java.lang.reflect.Parameter[] params = method.getParameters();
        Class<?>[] parametersType = method.getParameterTypes();
        String[] parametersName = new String[params.length];
        for (int i = 0; i < params.length; i++) {
           parametersName[i] =  params[i].getName();
        }
        return namedParametersFor(parametersName,parametersType);
    }

    private Parameter[] namedParametersFor(String[] parameterNames,
                                           Class<?>[] parameterTypes) {
        Parameter[] namedParameters = new Parameter[parameterTypes.length];
        for (int i = 0; i < parameterTypes.length; i++) {
            namedParameters[i] = new Parameter(parameterNames[i], parameterTypes[i]);
        }
        return namedParameters;
    }

    public Method getMethod() {
        return method;
    }
}
