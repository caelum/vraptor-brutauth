package br.com.caelum.brutauth.reflection.methodsearchers;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import br.com.caelum.brutauth.reflection.Argument;
import br.com.caelum.brutauth.reflection.ParameterMock;
import br.com.caelum.vraptor.http.Parameter;

public class ArgumentParameterMatcherTest {
	private final ArgumentParameterMatcher argumentParameterMatcher = new ArgumentParameterMatcher(new MatchedParameters());

	@Test
	public void the_half_matches_should_be_replaced_until_the_last_one() throws NoSuchMethodException {
		Parameter tobbyParam = new ParameterMock("tobby", Dog.class);
		Parameter bobParam = new ParameterMock("bob", Dog.class);
		
		Parameter[] params = new Parameter[] {tobbyParam, bobParam};

		Argument tobbyArg = new Argument("tobby", new Dog());
		Argument bobArg = new Argument("bob", new Cat());
		Argument billyArg = new Argument("billy", new Dog());

		Argument[] arguments= new Argument[] {
				tobbyArg,
				bobArg,
				billyArg
		};
		
		Argument[] values = argumentParameterMatcher.getValuesMatchingParameters(params, arguments);

		assertEquals(new Argument[] {tobbyArg, billyArg}, values);
	}
	
	@Test
	public void should_not_return_two_values_from_the_same_argument() throws NoSuchMethodException {
		Parameter tobbyParam = new ParameterMock("tobby", Dog.class);
		Parameter bobParam = new ParameterMock("bob", Dog.class);
		Parameter[] params = new Parameter[] {tobbyParam, bobParam};
		
		Argument tobbyArg = new Argument("tobby", new Dog());
		Argument[] arguments= new Argument[] {tobbyArg};
		
		Argument[] valuesMatchingParameters = argumentParameterMatcher.getValuesMatchingParameters(params, arguments);
		
		assertEquals(new Argument[] {tobbyArg, null}, valuesMatchingParameters);
	}
	
}
