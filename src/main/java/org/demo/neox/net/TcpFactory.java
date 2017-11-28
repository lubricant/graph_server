package org.demo.neox.net;

import io.vertx.core.Vertx;

public class TcpFactory {

	static final Vertx vertx;
	static {
		vertx = Vertx.vertx();
		Runtime.getRuntime().addShutdownHook(
				new Thread(vertx::close));
	}

	public static TcpClient newClient(String ip, int port) {
		try {
			return new TcpClient(ip, port, vertx);
		} catch (Exception ex) {}
		return null;
	}

//	static <T> TcpRequest<T> newRequest(
//			long sequence, int timeout, Class<T> clazz) {
//		
//		long timer = vertx.setTimer(timeout, id->{});
//		TcpRequest<T> request = new TcpRequest<>(sequence, timer, clazz);
//		return null;
//	}
//	
}
