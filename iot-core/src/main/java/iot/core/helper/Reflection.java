package iot.core.helper;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Enumeration;
import java.util.logging.Logger;

/**
 * Classe que abstrai o uso da API Reflection.
 *
 * @author Rafael Guterres
 */
public final class Reflection implements Serializable {

    private static final long serialVersionUID = -7417663509020364988L;

    private static final String RAWTYPES = "rawtypes";

    private Reflection() {
    }

    /**
     * Retorna a classe informada no argumento do generic Type.
     *
     * @param currentClass
     * @param genericSuperClass
     * @param argumentIndex
     * @return
     */
    @SuppressWarnings(RAWTYPES)
    public static Class getGenericArgumentType(Class currentClass,
            Class genericSuperClass, int argumentIndex) {
        Type superType = currentClass.getGenericSuperclass();
        if (superType == null) {
            throw new IllegalArgumentException(
                    "Classe '"
                    + currentClass
                    + "' a super classe informada não possui o valor do argumento genérico válido");
        }
        if (!(superType instanceof ParameterizedType)
                || genericSuperClass != null
                && ((ParameterizedType) superType).getRawType() != genericSuperClass) {
            return getGenericArgumentType(currentClass.getSuperclass(),
                    genericSuperClass, argumentIndex);
        }
        Object[] args = ((ParameterizedType) superType).getActualTypeArguments();
        if (argumentIndex >= args.length) {
            throw new IllegalArgumentException(
                    "Posição do argumento inválido: " + argumentIndex);
        }
        return cast(Class.class, args[argumentIndex]);
    }

    /**
     * Variação do método
     * {@link Reflection#getGenericArgumentType(Class, Class, int)}
     *
     * @param currentClass
     * @param genericSuperClass
     * @return
     */
    @SuppressWarnings(RAWTYPES)
    public static Class getGenericArgumentType(Class currentClass, Class genericSuperClass) {
        return getGenericArgumentType(currentClass, genericSuperClass, 0);
    }

    /**
     * Variação do método
     * {@link Reflection#getGenericArgumentType(Class, Class, int)}
     *
     * @param currentClass
     * @param argumentIndex
     * @return
     */
    @SuppressWarnings(RAWTYPES)
    public static Class getGenericArgumentType(Class currentClass, int argumentIndex) {
        return getGenericArgumentType(currentClass, null, argumentIndex);
    }

    /**
     * Variação do método
     * {@link Reflection#getGenericArgumentType(Class, Class, int)}
     *
     * @param currentClass
     * @return
     */
    @SuppressWarnings(RAWTYPES)
    public static Class getGenericArgumentType(Class currentClass) {
        return getGenericArgumentType(currentClass, null, 0);
    }

    /**
     * Cria uma instância da classe informada no parâmetro.
     *
     * @param <T>
     * @param classe
     * @return
     */
    public static <T> T instantiateClass(Class<T> classe) {
        try {
            return classe.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new IllegalArgumentException(
                    "Não foi possível instanciar a classe: " + classe.getName(), e);
        }
    }

    /**
     * Realiza o cast do objeto conforme a classe informada.
     *
     * @param <T>
     * @param classe
     * @param objeto
     * @return
     */
    public static <T> T cast(Class<T> classe, Object objeto) {
        if (classe.isAssignableFrom(objeto.getClass())) {
            return classe.cast(objeto);
        }
        throw new ClassCastException(
                "Não foi possível realizar o cast da classe "
                + objeto.getClass().getName()
                + ", cujo o carregador de classe "
                + objeto.getClass().getClassLoader() + ", da " + classe
                + ", pois o carregador de classe "
                + classe.getClassLoader());
    }

    /**
     * Carrega uma classe conforme o nome da classe informada.
     *
     * @param className
     * @return
     */
    @SuppressWarnings(RAWTYPES)
    public static Class forName(String className) {
        try {
            return Class.forName(className, false, Thread.currentThread().getContextClassLoader());
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException(e);
        }
    }

    /**
     * Retorna os streams de todos os recursos encontrados no class loader
     * padrão com o nome informado.
     *
     * @param name the of resource
     * @param useCache define if open stream with cache
     * @return list of streams
     */
    public static Collection<InputStream> getResources(String name, boolean useCache) {
        Collection<InputStream> inputsStreams = new ArrayList<>();
        ClassLoader cl = getDefaultClassLoader();
        try {
            Enumeration<URL> urls = cl.getResources(name);
            while (urls.hasMoreElements()) {
                URL url = (URL) urls.nextElement();
                URLConnection urlConnection = url.openConnection();
                urlConnection.setUseCaches(useCache);
                inputsStreams.add(urlConnection.getInputStream());
            }
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
        return inputsStreams;
    }

    public static ClassLoader getDefaultClassLoader() {
        return getDefaultClassLoader(null);
    }

    /**
     * Retorna o class loader da classe cls ou o class loader padrão.
     *
     * @param cls
     * @return class loader
     */
    public static ClassLoader getDefaultClassLoader(Class<?> cls) {
        ClassLoader classLoader = null;
        if (cls != null) {
            return cls.getClassLoader();
        }
        try {
            classLoader = Thread.currentThread().getContextClassLoader();
        } catch (Exception e) {
            Logger.getAnonymousLogger().warning(e.getMessage());
        }
        if (classLoader == null) {
            // No thread context class loader -> use class loader of this class.
            classLoader = Reflection.class.getClassLoader();
        }
        return classLoader;
    }

    /**
     * Retorna todos campos de uma classe;
     *
     * @param fields
     * @param clazz
     * @return
     */
    public static Collection<Field> getAllFields(Collection<Field> fields, Class<?> clazz) {
        if (clazz.getSuperclass() != null) {
            fields = getAllFields(fields, clazz.getSuperclass());
        }
        fields.addAll(Arrays.asList(clazz.getDeclaredFields()));
        return fields;
    }

}
