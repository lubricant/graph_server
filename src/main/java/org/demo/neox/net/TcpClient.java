package org.demo.neox.net;

import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

import io.netty.util.internal.PlatformDependent;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.core.net.NetClient;
import io.vertx.core.net.NetSocket;

public class TcpClient {
	
	private final AtomicLong sequenceHolder = new AtomicLong(0);
	private final ConcurrentMap<Long, TcpRequest> requestHolder = PlatformDependent.newConcurrentHashMap();
	
	private final Logger logger;
	private NetClient client;
	private NetSocket socket;

	TcpClient(String ip, int port) throws Exception {
		logger = LoggerFactory.getLogger(String.format("VertxTcpClient-[%s:%d]", ip, port));
		
	}
	
}
