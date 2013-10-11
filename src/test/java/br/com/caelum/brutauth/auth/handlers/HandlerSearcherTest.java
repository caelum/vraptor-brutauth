package br.com.caelum.brutauth.auth.handlers;

import static br.com.caelum.brutauth.util.TestUtils.method;

import java.lang.reflect.Method;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.mockito.runners.MockitoJUnitRunner;

import br.com.caelum.brutauth.auth.annotations.HandledBy;
import br.com.caelum.brutauth.auth.rules.BrutauthRule;
import br.com.caelum.vraptor.controller.ControllerMethod;
import br.com.caelum.vraptor.ioc.Container;

@RunWith(MockitoJUnitRunner.class)
public class HandlerSearcherTest {
	private @Mock Container container;
	private @Mock ControllerMethod controllerMethod;
	private AccessNotAllowedHandler defaultHandler;
	private RuleSpecificHandler ruleSpecificHandler;
	private ResourceMethodSpecificHandler resourceMethodSpecificHandler;
	private ResourceClassSpecificHandler resourceClassSpecificHandler;
	private ControllerMethod handledMethod;
	private ControllerMethod notHandledMethod;
	private ControllerMethod handledController;
	private ControllerMethod handledControllerWithoutHandledMethod;

	@Before
	public void setUp() throws NoSuchMethodException, SecurityException{
		container = mock(Container.class);
		
		defaultHandler = new AccessNotAllowedHandler(null);
		when(container.instanceFor(AccessNotAllowedHandler.class)).thenReturn(defaultHandler);
		
		ruleSpecificHandler = new RuleSpecificHandler();
		when(container.instanceFor(RuleSpecificHandler.class)).thenReturn(ruleSpecificHandler);
		
		resourceMethodSpecificHandler = new ResourceMethodSpecificHandler();
		when(container.instanceFor(ResourceMethodSpecificHandler.class)).thenReturn(resourceMethodSpecificHandler);
		
		resourceClassSpecificHandler = new ResourceClassSpecificHandler();
		when(container.instanceFor(ResourceClassSpecificHandler.class)).thenReturn(resourceClassSpecificHandler);
		
		
		handledMethod = method(NotHandledController.class, "handled");
		notHandledMethod = method(NotHandledController.class, "notHandled");
		handledController = method(HandledController.class, "handled");
		handledControllerWithoutHandledMethod = method(HandledController.class, "notHandled");
	}
	
	@Test
	public void should_get_default_handler_if_class_doesnt_contains_annotation() throws Exception {
		RuleWithoutSpecificHandlers rule = new RuleWithoutSpecificHandlers();
		HandlerSearcher handlerSearcher = new HandlerSearcher(container, notHandledMethod);
		assertEquals(defaultHandler, handlerSearcher.getHandler(rule));
	}

	@Test
	public void should_get_specific_handler_of_rule() {
		RuleWithSpecificHandlers rule = new RuleWithSpecificHandlers();
		HandlerSearcher handlerSearcher = new HandlerSearcher(container, notHandledMethod);
		RuleHandler handler = handlerSearcher.getHandler(rule);
		assertEquals(ruleSpecificHandler, handler);
	}

	@Test
	public void should_ignore_rule_handler_if_resource_method_has_handledby_annotation() throws Exception {
		Method methodHandled = HandledController.class.getMethod("handled");
		HandlerSearcher handlerSearcher = new HandlerSearcher(container, handledMethod);
		when(controllerMethod.getMethod()).thenReturn(methodHandled);
		RuleWithSpecificHandlers rule = new RuleWithSpecificHandlers();
		assertEquals(resourceMethodSpecificHandler, handlerSearcher.getHandler(rule));
	}
	
	@Test
	public void should_ignore_rule_handler_if_resource_class_has_handledby_annotation() {
		RuleWithSpecificHandlers rule = new RuleWithSpecificHandlers();
		HandlerSearcher handlerSearcher = new HandlerSearcher(container, handledControllerWithoutHandledMethod);
		assertEquals(resourceClassSpecificHandler, handlerSearcher.getHandler(rule));
	}
	
	@Test
	public void should_ignore_resource_class_handler_if_resource_method_has_handledby_annotation() {
		RuleWithSpecificHandlers rule = new RuleWithSpecificHandlers();
		HandlerSearcher handlerSearcher = new HandlerSearcher(container, handledController);
		assertEquals(resourceMethodSpecificHandler, handlerSearcher.getHandler(rule));
	}
	
	private class RuleWithoutSpecificHandlers implements BrutauthRule{}

	@HandledBy(RuleSpecificHandler.class)
	private class RuleWithSpecificHandlers implements BrutauthRule{}


	private class RuleSpecificHandler implements RuleHandler{
		@Override public void handle() {}
	}

	private class ResourceMethodSpecificHandler implements RuleHandler{
		@Override public void handle() {}
	}

	private class ResourceClassSpecificHandler implements RuleHandler{
		@Override
		public void handle() {
		}
	}
	
	private class NotHandledController{
		@HandledBy(ResourceMethodSpecificHandler.class)
		public void handled() {}

		public void notHandled() {}
	}
	
	@HandledBy(ResourceClassSpecificHandler.class)
	private class HandledController{
		public void notHandled() {
		}
		@HandledBy(ResourceMethodSpecificHandler.class)
		public void handled() {
		}
	}
}

