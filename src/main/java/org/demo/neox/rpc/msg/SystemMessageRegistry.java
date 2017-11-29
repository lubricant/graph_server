package org.demo.neox.rpc.msg;

import org.demo.neox.rpc.Message;

public enum SystemMessageRegistry {

    SERVICE_NOT_FOUND(1, null);









    static final SystemMessageRegistry[] fastLocator = new
            SystemMessageRegistry[128];

    final int marker;
    final Class<? extends Message> clazz;

    public static Class<? extends Message> search(int marker) {
        if (marker > 0 && marker < fastLocator.length) {
            SystemMessageRegistry reg = fastLocator[marker];
            return reg == null? null: reg.clazz;
        }
        return null;
    }

    synchronized static void register(SystemMessageRegistry reg, int marker) {
        if (marker <= 0 || marker >= fastLocator.length) {
            throw new IllegalArgumentException("Illegal registry marker.");
        }
        if (fastLocator[marker] != null) {
            throw new IllegalArgumentException("Duplicate registry marker.");
        }
        fastLocator[marker] = reg;
    }

    SystemMessageRegistry(int marker, Class<? extends Message> clazz) {
        if (clazz == null)
            throw new NullPointerException();
        register(this, marker);
        this.marker = marker;
        this.clazz = clazz;
    }

}