package br.com.caelum.brutauth.auth.handlers;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.lang.reflect.Method;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import br.com.caelum.brutauth.auth.annotations.HandledBy;
import br.com.caelum.brutauth.auth.rules.BrutauthRule;
import br.com.caelum.vraptor4.controller.ControllerMethod;
import br.com.caelum.vraptor4.ioc.Container;

@RunWith(MockitoJUnitRunner.class)
public class HandlerSearcherTest {

	private AccessNotPermitedHandler defaultHandler;
	private RuleSpecificHandler ruleSpecificHandler;
	private ResourceMethodSpecificHandler resourceSpecificHandler;
	private HandlerSearcher handlerSearcher;

	private @Mock Container container;
	private @Mock ControllerMethod controllerMethod;


	@Before
	public void setUp() throws NoSuchMethodException, SecurityException{

		defaultHandler = new AccessNotPermitedHandler();
		when(container.instanceFor(AccessNotPermitedHandler.class)).thenReturn(defaultHandler);

		Method methodNotHandled = TestController.class.getMethod("notHandled");
		when(controllerMethod.getMethod()).thenReturn(methodNotHandled);

		ruleSpecificHandler = new RuleSpecificHandler();
		when(container.instanceFor(RuleSpecificHandler.class)).thenReturn(ruleSpecificHandler);

		resourceSpecificHandler = new ResourceMethodSpecificHandler();
		when(container.instanceFor(ResourceMethodSpecificHandler.class)).thenReturn(resourceSpecificHandler);
		handlerSearcher = new HandlerSearcher(container, controllerMethod);
	}

	@Test
	public void should_get_default_handler_if_class_doesnt_contains_annotation() throws Exception {
		RuleWithoutSpecificHandlers rule = new RuleWithoutSpecificHandlers();
		assertEquals(defaultHandler, handlerSearcher.getHandler(rule));
	}

	@Test
	public void should_get_specific_handler_of_rule() {
		RuleWithSpecificHandlers rule = new RuleWithSpecificHandlers();
		RuleHandler handler = handlerSearcher.getHandler(rule);
		assertEquals(ruleSpecificHandler, handler);
	}

	@Test
	public void should_ignore_rule_handler_if_resource_method_has_handledby_annotation() throws Exception {
		Method methodHandled = TestController.class.getMethod("handled");
		when(controllerMethod.getMethod()).thenReturn(methodHandled);
		RuleWithSpecificHandlers rule = new RuleWithSpecificHandlers();
		assertEquals(resourceSpecificHandler, handlerSearcher.getHandler(rule));
	}

	public class RuleWithoutSpecificHandlers implements BrutauthRule{}

	@HandledBy(RuleSpecificHandler.class)
	public class RuleWithSpecificHandlers implements BrutauthRule{}


	public class RuleSpecificHandler implements RuleHandler{
		@Override public void handle() {}
	}

	public class ResourceMethodSpecificHandler implements RuleHandler{
		@Override public void handle() {}
	}

	public class TestController {

		@HandledBy(ResourceMethodSpecificHandler.class)
		public void handled() {}

		public void notHandled() {}
	}
}

