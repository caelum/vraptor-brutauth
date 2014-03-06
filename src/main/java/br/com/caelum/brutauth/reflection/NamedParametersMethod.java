package br.com.caelum.brutauth.reflection;

import java.lang.reflect.Method;

import com.thoughtworks.paranamer.AdaptiveParanamer;
import com.thoughtworks.paranamer.CachingParanamer;
import com.thoughtworks.paranamer.Paranamer;

public class NamedParametersMethod {

	private Method method;

	public NamedParametersMethod(Method method) {
		this.method = method;
	}

	public Parameter[] getParameters() {
        Paranamer paranamer = new CachingParanamer(new AdaptiveParanamer());
        String[] lookupParameterNames = paranamer.lookupParameterNames(method, true);
        Class<?>[] parameterTypes = method.getParameterTypes();
        return namedParametersFor(lookupParameterNames, parameterTypes);
        
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
