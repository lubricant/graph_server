package test;

import com.google.common.collect.Lists;

import java.lang.reflect.*;
import java.util.Arrays;

public class Test {

    public static void main(String[] args) {
        Class<?> clazz = SomeService.class;
        checkClass(clazz);
    }

    static void checkClass(Class<?> clazz) {

        if (! clazz.isInterface()) {
            throw new IllegalArgumentException(
                    String.format("[%s] is not an interface.", clazz.getName()));
        }

        if (clazz.getTypeParameters().length > 0) {
            throw new IllegalArgumentException(
                    String.format("Interface [%s] has type parameters.", clazz.getName()));
        }

        Method[] methods = clazz.getDeclaredMethods();
        for (Method method: methods) {
            if (method.isDefault()) {
                throw new IllegalArgumentException(
                        String.format("[%s] is default method.", method));
            }
            if (method.getTypeParameters().length > 0) {
                throw new IllegalArgumentException(
                        String.format("Method [%s] has type parameters.", method));
            }

            Type returnType = method.getGenericReturnType();
            Class<?> returnClazz = method.getReturnType();
            if (! checkType(returnClazz, returnType)) {
                throw new IllegalArgumentException(
                        String.format("Return type [%s] is illegal which belong to method [%s].",
                                returnType.getTypeName(), method));
            }

            Parameter[] parameters = method.getParameters();
            for (Parameter parameter: parameters) {
                Type paramType = parameter.getParameterizedType();
                Class<?> paramClazz = parameter.getType();
                if (! checkType(paramClazz, paramType)) {
                    throw new IllegalArgumentException(
                            String.format("Parameter type [%s] is illegal which belong to method [%s].",
                                    parameter, method));
                }
            }

        }
    }

    static boolean checkType(Class<?> clazz, Type type) {

        if ("void".equals(clazz.toString())) return true;
        if (clazz.isPrimitive()) return false;
        if (! (type instanceof ParameterizedType)) return true;

        Type[] genericTypes = ((ParameterizedType)type).getActualTypeArguments();

        if (clazz == java.util.List.class || clazz == java.util.Set.class) {
            Type elemType = genericTypes[0];
            return checkGenericType(elemType);
        }

        if (clazz == java.util.Map.class) {
            Type keyType = genericTypes[0], valType = genericTypes[1];
            return checkGenericType(keyType) && checkGenericType(valType);
        }

        return false;
    }

    static boolean checkGenericType(Type type) {
        if (type instanceof WildcardType)
            return false;
        if (type instanceof ParameterizedType)
            return false;

        try {
            Class<?> clazz = Class.forName(type.getTypeName());
            Class<?> superClazz = clazz.getSuperclass();
            return superClazz == Object.class || /* POJO or String*/
                   superClazz == Number.class; /* Number */
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    static Field searchInvalidFields(Class<?> pojoClazz) {

        Field[] fields = pojoClazz.getDeclaredFields();

        if (fields == null)
            return null;

        for (Field field: fields) {
            Class<?> fieldClazz = field.getClass();
            if (! checkType(fieldClazz, field.getGenericType())) {
                return field;
            }

            Field invalidField = searchInvalidFields(fieldClazz);
        }

        return null;
    }
}
