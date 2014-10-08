package br.com.caelum.brutauth.reflection;

import br.com.caelum.vraptor.http.ParameterNameProvider;

import javax.inject.Inject;
import java.lang.reflect.Method;

public class NamedParametersMethod {

    @Inject
    private ParameterNameProvider parameterNameProvider;
    private Method method;

	public NamedParametersMethod(Method method) {
		this.method = method;
	}

	public Parameter[] getParameters() {
        br.com.caelum.vraptor.http.Parameter[] parametersVRaptor = parameterNameProvider.parametersFor(method);
        if (parametersVRaptor != null) {
            Parameter[] parameters = new Parameter[parametersVRaptor.length];
            for (int i=0; i<parametersVRaptor.length; i++) {
                parameters[i] = new Parameter(parametersVRaptor[i].getName(),parametersVRaptor[i].getType());
            }
            return parameters;
        }
        return null;
    }

	public Method getMethod() {
		return method;
	}
	
	

}
