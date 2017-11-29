package org.demo.neox.net;

import io.netty.util.internal.PlatformDependent;
import io.vertx.core.Vertx;

import java.util.concurrent.ConcurrentMap;

public class TcpFactory {

	static final Vertx vertx;
	static {
		vertx = Vertx.vertx();
		Runtime.getRuntime().addShutdownHook(
				new Thread(vertx::close));
	}

	static final ConcurrentMap<TcpAddress, TcpClient>
		clientHolder = PlatformDependent.newConcurrentHashMap();

	synchronized static TcpClient newClient(TcpAddress address) throws Exception {
		TcpClient client = clientHolder.get(address);
		if (client != null) {
			return client;
		}

		client = new TcpClient(address.ip, address.port, vertx, tcpClient -> clientHolder.remove(address) );
		clientHolder.put(address, client);
		return client;
	}

	public static TcpClient getClient(String ip, int port) throws Exception {
		TcpAddress tpcAddress = new TcpAddress(ip, port);
		TcpClient tcpClient = clientHolder.get(tpcAddress);
		if (tcpClient == null) {
			tcpClient = newClient(tpcAddress);
		}
		return tcpClient;
	}

}
