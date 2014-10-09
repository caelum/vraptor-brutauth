package br.com.caelum.brutauth.reflection;

import br.com.caelum.vraptor.http.Parameter;
import br.com.caelum.vraptor.http.ParameterNameProvider;

import java.lang.reflect.Method;

public class NamedParametersMethod {

    private ParameterNameProvider parameterNameProvider;

    private Method method;

	public NamedParametersMethod(Method method, ParameterNameProvider parameterNameProvider) {
		this.method = method;
        this.parameterNameProvider = parameterNameProvider;
	}

    public Parameter[] getParameters() {

        return parameterNameProvider.parametersFor(method);
    }

    public Method getMethod() {
        return method;
    }
}
