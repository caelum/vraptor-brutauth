package br.com.caelum.brutauth.auth.handlers;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

import br.com.caelum.brutauth.auth.annotations.HandledBy;
import br.com.caelum.brutauth.auth.handlers.HandlerSearcherTest.ResourceClassSpecificHandler;
import br.com.caelum.brutauth.auth.rules.BrutauthRule;
import br.com.caelum.vraptor.core.DefaultMethodInfo;
import br.com.caelum.vraptor.core.MethodInfo;
import br.com.caelum.vraptor.ioc.Container;
import br.com.caelum.vraptor.resource.DefaultResourceClass;
import br.com.caelum.vraptor.resource.DefaultResourceMethod;

public class HandlerSearcherTest {

	private AccessNotAllowedHandler defaultHandler;
	private RuleSpecificHandler ruleSpecificHandler;
	private Container container;
	private ResourceMethodSpecificHandler resourceMethodSpecificHandler;
	private MethodInfo handledMethod;
	private MethodInfo notHandledMethod;
	private MethodInfo handledWithoutHandledMethodController;
	private MethodInfo handledController;
	private ResourceClassSpecificHandler resourceClassSpecificHandler;

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
		
		handledMethod = methodInfo("handled", NotHandledController.class);
		notHandledMethod = methodInfo("notHandled", NotHandledController.class);
		handledController = methodInfo("handled", HandledController.class);
		handledWithoutHandledMethodController = methodInfo("notHandled", HandledController.class);
		
	}
	
	private MethodInfo methodInfo(String methodName, Class<?> controller) throws NoSuchMethodException, SecurityException {
		DefaultMethodInfo defaultMethodInfo = new DefaultMethodInfo();
		defaultMethodInfo.setResourceMethod(new DefaultResourceMethod(new DefaultResourceClass(controller), controller.getMethod(methodName)));
		return defaultMethodInfo;
	}

	@Test
	public void should_get_default_handler_if_class_doesnt_contains_annotation() {
		HandlerSearcher handlerSearcher = new HandlerSearcher(container, notHandledMethod);
		RuleWithoutSpecificHandlers rule = new RuleWithoutSpecificHandlers();
		assertEquals(defaultHandler, handlerSearcher.getHandler(rule));
	}
	
	@Test
	public void should_get_specific_handler_of_rule() {
		HandlerSearcher handlerSearcher = new HandlerSearcher(container, notHandledMethod);
		RuleWithSpecificHandlers rule = new RuleWithSpecificHandlers();
		RuleHandler handler = handlerSearcher.getHandler(rule);
		assertEquals(ruleSpecificHandler, handler);
	}
	
	@Test
	public void should_ignore_rule_handler_if_resource_method_has_handledby_annotation() {
		HandlerSearcher handlerSearcher = new HandlerSearcher(container, handledMethod);
		RuleWithSpecificHandlers rule = new RuleWithSpecificHandlers();
		assertEquals(resourceMethodSpecificHandler, handlerSearcher.getHandler(rule));
	}
	
	@Test
	public void should_ignore_rule_handler_if_resource_class_has_handledby_annotation() {
		HandlerSearcher handlerSearcher = new HandlerSearcher(container, handledWithoutHandledMethodController);
		RuleWithSpecificHandlers rule = new RuleWithSpecificHandlers();
		assertEquals(resourceClassSpecificHandler, handlerSearcher.getHandler(rule));
	}
	
	@Test
	public void should_ignore_resource_class_handler_if_resource_method_has_handledby_annotation() {
		HandlerSearcher handlerSearcher = new HandlerSearcher(container, handledController);
		RuleWithSpecificHandlers rule = new RuleWithSpecificHandlers();
		assertEquals(resourceMethodSpecificHandler, handlerSearcher.getHandler(rule));
	}

	public class RuleWithoutSpecificHandlers implements BrutauthRule{
	}
	
	@HandledBy(RuleSpecificHandler.class)
	public class RuleWithSpecificHandlers implements BrutauthRule{
	}
	
	public class RuleSpecificHandler implements RuleHandler{
		@Override
		public void handle() {
		}
	}

	public class ResourceMethodSpecificHandler implements RuleHandler{
		@Override
		public void handle() {
		}
	}
	
	public class ResourceClassSpecificHandler implements RuleHandler{
		@Override
		public void handle() {
		}
	}
	
	public class NotHandledController{
		@HandledBy(ResourceMethodSpecificHandler.class)
		public void handled() {
		}

		public void notHandled() {
		}
	}
	
	@HandledBy(ResourceClassSpecificHandler.class)
	public class HandledController{
		public void notHandled() {
		}
		@HandledBy(ResourceMethodSpecificHandler.class)
		public void handled() {
		}
	}
}

