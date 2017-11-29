package org.demo.neox.net;

public class TcpAddress {

    final String ip;
    final int port;

    TcpAddress(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    @Override
    public boolean equals(Object o) {
        TcpAddress that = (TcpAddress) o;
        if (port != that.port) return false;
        return ip != null ? ip.equals(that.ip) : that.ip == null;
    }

    @Override
    public int hashCode() {
        return port;
    }
}
