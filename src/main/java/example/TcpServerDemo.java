package example;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.core.net.NetServer;

public class TcpServerDemo {
	public static void main(String[] args) {
		Vertx container = Vertx.vertx();
		container.deployVerticle(new TcpVerticle());
		Runtime.getRuntime().addShutdownHook(
				new Thread(container::close));
	}
}


class TcpVerticle extends AbstractVerticle {

	Logger logger = LoggerFactory.getLogger(getClass());
	
	NetServer server = null;
	
	public void start(Future<Void> startFuture) {
		
		server = vertx.createNetServer().connectHandler( socket ->{
			
			logger.info("Socket is connected.");
			
			socket.handler( buffer -> {
				socket.write("Hello World");
				logger.info("Data transfer.");
			});
			
			socket.closeHandler( __ -> {
				logger.info("Socket is closing.");
			});
			
		}).listen(2333);
		
		logger.info("Server running on port {0}", server.actualPort());
		startFuture.complete();
	}
	
	public void stop(Future<Void> stopFuture) {
		server.close();
		stopFuture.complete();
	}
}