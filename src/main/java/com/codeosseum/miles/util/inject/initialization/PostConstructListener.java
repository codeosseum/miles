package com.codeosseum.miles.util.inject.initialization;

import com.codeosseum.miles.util.inject.initialization.PostConstruct;
import com.google.inject.TypeLiteral;
import com.google.inject.spi.InjectionListener;
import com.google.inject.spi.TypeEncounter;
import com.google.inject.spi.TypeListener;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public enum PostConstructListener implements TypeListener {
    INSTANCE;

    private final Set<Object> postConstructed = new HashSet<>();

    @Override
    public <I> void hear(final TypeLiteral<I> typeLiteral, final TypeEncounter<I> typeEncounter) {
        final List<Method> callableMethods = findCallableMethods(typeLiteral.getRawType());

        System.out.println(callableMethods.size());

        typeEncounter.register((InjectionListener<I>) injectee -> {
            if (postConstructed.contains(injectee)) {
                return;
            }

            try {
                for (final Method method : callableMethods) {
                    method.invoke(injectee);
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            } finally {
                postConstructed.add(injectee);
            }
        });
    }

    private List<Method> findCallableMethods(final Class<?> clazz) {
        return Arrays.stream(clazz.getDeclaredMethods())
                .filter(this::hasPostConstructAnnotation)
                .filter(this::returnsVoid)
                .filter(this::hasZeroArguments)
                .collect(Collectors.toList());
    }

    private boolean hasPostConstructAnnotation(final Method method) {
        return Arrays.stream(method.getDeclaredAnnotations())
                .anyMatch(annotation -> annotation.annotationType().equals(PostConstruct.class));
    }

    private boolean returnsVoid(final Method method) {
        return method.getReturnType().equals(Void.TYPE);
    }

    private boolean hasZeroArguments(final Method method) {
        return method.getParameterCount() == 0;
    }
}
