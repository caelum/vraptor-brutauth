package br.com.caelum.brutauth.reflection.paranamer;

import java.lang.reflect.Method;

import com.thoughtworks.paranamer.AdaptiveParanamer;
import com.thoughtworks.paranamer.CachingParanamer;
import com.thoughtworks.paranamer.Paranamer;

public class ParanamerParser {
	    
    @SuppressWarnings("rawtypes")
	public static String[] paramsFor(Method m) {
        Paranamer paranamer = new CachingParanamer(new AdaptiveParanamer());
        return paranamer.lookupParameterNames(m, true);
    }

}
