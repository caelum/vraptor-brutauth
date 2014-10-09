package br.com.caelum.brutauth.reflection;

import br.com.caelum.vraptor.http.Parameter;

import java.lang.reflect.AccessibleObject;

/**
 * Created by felipeweb on 09/10/14.
 */
public class ParameterMock extends Parameter {

    private final Class clazz;

    public ParameterMock(String name, Class clazz) {
        super(0, name, getFakeMethod());
        this.clazz = clazz;
    }

    @Override
    public Class<?> getType() {
        return clazz;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getName() == null) ? 0 : getName().hashCode());
        result = prime * result + ((getType() == null) ? 0 : getType().hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Parameter other = (Parameter) obj;
        if (getName() == null) {
            if (other.getName() != null)
                return false;
        } else if (!getName().equals(other.getName()))
            return false;
        if (getType() == null) {
            if (other.getType() != null)
                return false;
        } else if (!getType().equals(other.getType()))
            return false;
        return true;
    }

    private static AccessibleObject getFakeMethod() {
        try {
            return ParameterMock.class.getDeclaredMethod("fakeMethod", Integer.class);
        } catch (NoSuchMethodException e) {
            throw new IllegalStateException(e);
        }
    }

    public void fakeMethod(Integer parameter) {

    }
}
