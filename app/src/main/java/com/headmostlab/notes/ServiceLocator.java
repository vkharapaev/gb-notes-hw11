package com.headmostlab.notes;

import android.content.Context;

import java.util.HashMap;
import java.util.Map;

public class ServiceLocator {
    private final Map<Class<?>, Object> locatedMap = new HashMap<>();

    public <T, I extends T> void register(Class<T> clazz, I impl) {
        locatedMap.put(clazz, impl);
    }

    public <T> T locate(Class<T> clazz) {
        T item = (T) locatedMap.get(clazz);
        if (item != null) {
            return item;
        }
        throw new IllegalStateException("Cannot find locatable for " + clazz);
    }

    public static ServiceLocator from(Context context) {
        if (context instanceof LocatorHost) {
            return ((LocatorHost) context).getLocator();
        } else if (context.getApplicationContext() instanceof LocatorHost) {
            return ((LocatorHost) context.getApplicationContext()).getLocator();
        } else {
            throw new IllegalArgumentException();
        }
    }
}
