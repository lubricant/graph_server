package org.demo.neox.dto;

import java.io.IOException;

import org.demo.neox.base.Serialization;

import io.netty.buffer.ByteBuf;
import io.protostuff.Tag;
import io.vertx.core.buffer.Buffer;

public class User {

    @Tag(1)
    private Long id;

    @Tag(2)
    private String name;

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }

    public static void main(String[] args) throws Exception {
        User user = new User();
        user.setId(123L);
        user.setName("Alice");

//        RuntimeSchema<User> schema = RuntimeSchema.createFrom(User.class);
//        
//        LinkedBuffer buf = LinkedBuffer.allocate();
//        byte[] data = ProtostuffIOUtil.toByteArray(user, schema, buf);
//        buf.clear();
//
//        User u = schema.newMessage();
//        ProtostuffIOUtil.mergeFrom(data, u, schema);
        
    }
}
