package example;


import io.netty.util.concurrent.FastThreadLocal;
import io.protostuff.LinkedBuffer;

import java.util.HashMap;
import java.util.Map;

public class Protocol {

    private final static FastThreadLocal<Map<Class<?>, LinkedBuffer>>
    bufferHolder = new FastThreadLocal<Map<Class<?>, LinkedBuffer>>() {
        protected Map<Class<?>, LinkedBuffer> initialValue() throws Exception {
            return new HashMap<>();
        }
    };

    private LinkedBuffer acquireBuffer(Class<?> clazz) {
        Map<Class<?>, LinkedBuffer> bufferMap = bufferHolder.get();
        LinkedBuffer buffer = bufferMap.get(clazz);
        if (buffer == null) {
            bufferMap.put(clazz, buffer = LinkedBuffer.allocate());
        }
        return buffer;
    }

    public <T> void appendByteBuf(T message) {

    }

}
