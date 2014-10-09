package br.com.caelum.brutauth.reflection.methodsearchers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import br.com.caelum.brutauth.reflection.ParameterMock;
import br.com.caelum.vraptor.http.Parameter;
import org.junit.Test;

import br.com.caelum.brutauth.reflection.Argument;

public class MatchedParametersTest {
	private final MatchedParameters matchedParameters = new MatchedParameters();

	@Test
	public void should_put_a_full_match() {
		Parameter param = new ParameterMock("dog", Dog.class);
		Argument arg = new Argument("dog", new Dog());
		
		matchedParameters.tryToPut(param, arg);
		
		assertEquals(arg, matchedParameters.get(param).getArgument());
	}
	
	@Test
	public void should_put_a_half_match() {
		Parameter param = new ParameterMock("tobby", Dog.class);
		Argument billy = new Argument("billy", new Dog());
		
		matchedParameters.tryToPut(param, billy);
		
		assertEquals(billy, matchedParameters.get(param).getArgument());
	}
		
	@Test
	public void should_not_replace_a_full_match() {
		Parameter param = new ParameterMock("tobby", Dog.class);
		Argument tobby = new Argument("tobby", new Dog());
		
		matchedParameters.tryToPut(param, tobby);

		Argument billy = new Argument("billy", new Dog());
		matchedParameters.tryToPut(param, billy);
		
		assertEquals(tobby, matchedParameters.get(param).getArgument());
	}
	
	@Test
	public void should_ignore_zero_match() {
		Parameter param = new ParameterMock("tobby", Dog.class);
		Argument billy = new Argument("billy", new Cat());
		
		matchedParameters.tryToPut(param, billy);
		
		assertNull(matchedParameters.get(param));
	}

	@Test
	public void should_replace_a_half_match_with_full_match() {
		Parameter param = new ParameterMock("tom", Dog.class);
		Argument billy = new Argument("billy", new Dog());
		
		matchedParameters.tryToPut(param, billy);

		Argument tom = new Argument("tom", new Dog());
		matchedParameters.tryToPut(param, tom);
		
		assertEquals(tom, matchedParameters.get(param).getArgument());
	}


}
