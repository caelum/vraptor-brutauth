package br.com.caelum.brutauth.reflection.methodsearchers;

import br.com.caelum.brutauth.auth.rules.CustomBrutauthRule;
import br.com.caelum.brutauth.reflection.BrutauthValidation;
import br.com.caelum.brutauth.reflection.NamedParametersMethod;
import br.com.caelum.vraptor.http.ParameterNameProvider;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import java.lang.reflect.Method;

@Dependent
public class DefaultMethodSearcher {

    private final ParameterNameProvider parameterNameProvider;

    /**
     * @deprecated CDI eyes only
     */
    protected DefaultMethodSearcher() {
        this(null);
    }

    @Inject
    public DefaultMethodSearcher(ParameterNameProvider parameterNameProvider) {
        this.parameterNameProvider = parameterNameProvider;
    }

    public NamedParametersMethod getMethod(CustomBrutauthRule toInvoke) throws NoSuchMethodException {
        Method[] methods = toInvoke.getClass().getMethods();
		for (Method method : methods) {
            if (isDefaultMethod(method)) {
                return new NamedParametersMethod(method, parameterNameProvider);
            }
        }
		throw new NoSuchMethodException("Your rule should have a method with name 'isAllowed' or annotated with '@" + BrutauthValidation.class.getSimpleName() + "' and return boolean.  Rule: "+ toInvoke.getClass());
	}

	private boolean isDefaultMethod(Method method) {
		boolean returnsBoolean = method.getReturnType().isAssignableFrom(boolean.class);
		return returnsBoolean && (method.getName().equals("isAllowed") || method.isAnnotationPresent(BrutauthValidation.class));
	}


}
