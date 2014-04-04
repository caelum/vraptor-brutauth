package br.com.caelum.brutauth.verifier;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

import br.com.caelum.brutauth.reflection.Argument;
import br.com.caelum.vraptor.http.ValuedParameter;

public class EqualArgumentMatcher extends BaseMatcher<Argument>{

	private ValuedParameter param;

	public EqualArgumentMatcher(ValuedParameter param) {
		this.param = param;
	}

	@Override
	public void describeTo(Description description) {
	}

	@Override
	public boolean matches(Object item) {
		if(!(item instanceof Argument)) return false;
		
		Argument argument = (Argument) item;
		
		return argument.getValue().equals(param.getValue());
	}

	@Override
	public void describeMismatch(Object item, Description mismatchDescription) {
	}

}
