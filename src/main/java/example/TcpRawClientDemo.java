package example;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.core.net.NetClient;
import io.vertx.core.net.NetClientOptions;
import io.vertx.core.net.NetSocket;

class TcpSocket {
	
	private final NetSocket socket;
	
	TcpSocket(NetSocket socket) {
		this.socket = socket;
		socket.handler(buf -> {
			
		});
		socket.closeHandler(none -> {
			
		});
		socket.exceptionHandler(err -> {
			
		});
	}
	
	
	
}

class TcpClient {
	
	static final Vertx vertx;
	static {
		vertx = Vertx.vertx();
		Runtime.getRuntime().addShutdownHook(
				new Thread(vertx::close));
	}
	
	
	private final AtomicLong sequenceHolder = new AtomicLong(0);
	private final Logger logger;
	private NetClient client;
	private NetSocket socket;
	
	TcpClient(String ip, int port) throws Exception {
		logger = LoggerFactory.getLogger(String.format("VertxTcpClient-[%s:%d]", ip, port));
		try {
			buildConn(ip, port);
		} catch (Throwable e) {
			if (client != null)
				client.close();
			if (e instanceof Exception)
				throw (Exception) e;
			throw new Exception(e);
		}
		
		long timeoutId = vertx.setPeriodic(0, res -> {});
		vertx.cancelTimer(timeoutId);
		
		sequenceHolder.incrementAndGet();
		
		socket.handler( buffer -> {
			socket.write("Hello World");
			logger.info("Data transfer.");
		});
		
		socket.closeHandler( __ -> {
			logger.info("Socket is closing.");
		});
		
	}
	
	private void buildConn(String ip, int port) throws Throwable {
		
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
		
		latch.await();
		
		if (!error.isEmpty()) {
			logger.error("", error.get(0));
			throw error.get(0);
		}
		
	}
	
	public void close() {
		if (client != null) {
			client.close();
		}
	}
}

public class TcpRawClientDemo {
	
	
	public static void main(String[] args) throws Exception {
		TcpClient client = new TcpClient("localhost", 2333);
	}
}


