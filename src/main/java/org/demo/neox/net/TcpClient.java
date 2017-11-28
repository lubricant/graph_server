package org.demo.neox.net;

import java.io.IOException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicLong;

import io.netty.util.internal.PlatformDependent;
import io.vertx.core.Vertx;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.core.net.NetClient;
import io.vertx.core.net.NetClientOptions;
import io.vertx.core.net.NetSocket;
import org.apache.commons.lang3.Validate;
import org.demo.neox.base.Protocol;

public class TcpClient {
	
	private final AtomicLong sequenceHolder = new AtomicLong(0);
	private final ConcurrentMap<Long, TcpRequest> requestHolder = PlatformDependent.newConcurrentHashMap();
	
	private final Logger logger;
	private final Vertx vertx;
	private NetClient client;
	private NetSocket socket;

	TcpClient(String ip, int port, Vertx vertx) throws Exception {
		this.vertx = vertx;
		this.logger = LoggerFactory.getLogger(String.format("TcpClient-[%s:%d]", ip, port));
		Validate.isTrue( connect(ip, port), "Fail to initialize TcpClient-[%s:%d].", ip, port);
		this.init();
	}

	private boolean connect(String ip, int port){

		final List<Throwable> error = new ArrayList<>(1);
		final CountDownLatch latch = new CountDownLatch(1);

		client = vertx.createNetClient(new NetClientOptions().setTcpKeepAlive(true).setConnectTimeout(10)).connect(port, ip, res -> {
			if (res.succeeded()) {
				socket = res.result();
			} else {
				error.add(res.cause());
			}
			latch.countDown();
		});

		try {
			latch.await();
		} catch (InterruptedException ex) {
			logger.error("Thread interrupted while establishing connection.", ex);
			return false;
		}

		if (!error.isEmpty()) {
			logger.error("Exception raised while establishing connection", error.get(0));
			return false;
		}

		return true;
	}

	private void init() {
		this.socket.handler(buffer -> {
			long sequence = Protocol.readSequence(buffer);
			TcpRequest request = requestHolder.remove(sequence);

			boolean waiting = false;
			if (request != null) try {
				waiting = request.success(Protocol.readMessage(buffer, request.clazz()));
			} catch (Exception e) {
				logger.error("Error occurred while parsing buffer.", e);
				waiting = request.fail(e);
			}

			if (waiting) vertx.cancelTimer(request.timer());

		});
		this.socket.exceptionHandler(error -> {
			logger.error(error);
		});
		this.socket.closeHandler(ignored -> {
			final Throwable error = new SocketException("Socket closed.");
			this.requestHolder.forEach((seq, req)->req.fail(error));
			this.requestHolder.clear();
		});
	}

	private TcpRequest prepare(Class<?> clazz, long timeout) {
		long sequence = sequenceHolder.incrementAndGet();
		TcpRequest request = new TcpRequest(clazz,
				(tcpRequest -> sequence),
				(tcpRequest -> vertx.setTimer(timeout, id->{
					if (tcpRequest.timeout(timeout)) {
						requestHolder.remove(sequence);
					}
				})));
		requestHolder.put(sequence, request);
		return request;
	}

	public <Request, Response> void call(
			Request reqMsg, Class<Request> reqClazz, Class<Response> respClazz) {
		TcpRequest request = prepare(respClazz, 100);
		socket.write(Protocol.writeBuffer(request.sequence(), reqMsg, reqClazz));
	}


}
