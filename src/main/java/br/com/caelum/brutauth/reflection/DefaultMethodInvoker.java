package br.com.caelum.brutauth.reflection;

import br.com.caelum.brutauth.auth.rules.CustomBrutauthRule;
import br.com.caelum.brutauth.reflection.methodsearchers.Argument;
import br.com.caelum.brutauth.reflection.methodsearchers.MethodSearchers;
import br.com.caelum.vraptor.ioc.Component;

@Component
public class DefaultMethodInvoker implements MethodInvoker {

		private final MethodSearchers searcher;

		public DefaultMethodInvoker(MethodSearchers searcher) {
			this.searcher = searcher;
		}
	
		@Override
		public boolean invoke(CustomBrutauthRule toInvoke, Argument[] args) {
			return searcher.search(toInvoke, args).invoke();
		}

}
