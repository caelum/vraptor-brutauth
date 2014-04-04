package br.com.caelum.brutauth.verifier;

import static org.hamcrest.Matchers.arrayWithSize;
import static org.hamcrest.Matchers.emptyArray;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import br.com.caelum.brutauth.reflection.Argument;
import br.com.caelum.vraptor.core.MethodInfo;
import br.com.caelum.vraptor.http.ValuedParameter;

@RunWith(MockitoJUnitRunner.class)
public class MethodArgumentsTest {

	@Mock private MethodInfo info;
	private MethodArguments methodArguments;
	
	@Before
	public void setup() {
		methodArguments = new MethodArguments(info);
	}
	
	@Test
	public void should_ignore_parameters_with_null_value() {
		ValuedParameter[] valuedParameters = new ValuedParameter[] {param("question", null)};
		when(info.getValuedParameters()).thenReturn(valuedParameters);
		
		Argument[] valuedArguments = methodArguments.getValuedArguments();
		
		assertThat(valuedArguments, emptyArray());
	}
	
	
	@Test
	public void should_ignore_null_parameters() {
		ValuedParameter[] valuedParameters = new ValuedParameter[] {null};
		when(info.getValuedParameters()).thenReturn(valuedParameters);
		
		Argument[] valuedArguments = methodArguments.getValuedArguments();
		
		assertThat(valuedArguments, emptyArray());
	}
	
	@Test
	public void should_make_new_argument_if_value_of_parameter_is_not_null() {
		ValuedParameter param = param("question", "anyValue");
		ValuedParameter[] valuedParameters = new ValuedParameter[] {param};
		when(info.getValuedParameters()).thenReturn(valuedParameters);
		
		Argument[] valuedArguments = methodArguments.getValuedArguments();
		
		assertThat(valuedArguments, arrayWithSize(1));
		assertThat(valuedArguments[0].getValue(), equalTo(param.getValue()));
	}

	private ValuedParameter param(String name, Object value) {
		ValuedParameter parameter = mock(ValuedParameter.class);
		when(parameter.getValue()).thenReturn(value);
		when(parameter.getName()).thenReturn(name);
		return parameter;
	}

}
