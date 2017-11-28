package example;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetClient;
import io.vertx.core.net.NetSocket;

public class TcpClientDemo {
	
	
	public static void main(String[] args) {
		
		TcpClientVerticle client = new TcpClientVerticle();
		
		Vertx container = Vertx.vertx();
		container.deployVerticle(client, new DeploymentOptions().setWorker(true));
		
		client.sayHello();
		// container.eventBus().send(address, message)
		
//		NetClient client = container.createNetClient();
//		container.createNetClient(new NetClientOptions());
//		
//		client.connect(2333, "localhost", res -> {
//		  if (res.succeeded()) {
//		    System.out.println("Connected!");
//		    
//		    NetSocket socket = res.result();
//		    socket.handler(buffer -> {
//		    	System.out.println(buffer.getString(0, buffer.length()));
//		    	socket.notify();
//		    });
//		    
//		    for (int i=0; i<1000; i++) {
//		    	socket.write("Yeah");
//		    	synchronized (socket) {
//		    		try {
//						socket.wait();
//					} catch (InterruptedException e) {
//						e.printStackTrace();
//					}
//		    	}
//		    }
//		    
//		  } else {
//		    System.out.println("Failed to connect: " + res.cause().getMessage());
//		  }
//		});
	}
}


class TcpClientVerticle extends AbstractVerticle {
	
	boolean isReady = false;
	NetClient client = null;
	
    public void start() {
		client = vertx.createNetClient().connect(2333, "localhost", res -> {
			
		  if (res.succeeded()) {
		    System.out.println("Connected!");
		    
		    NetSocket socket = res.result();
		    socket.handler(buffer -> {
		    	if (buffer.length() < "Hello World".length()) {
		    		System.out.println("Ignore Imcomplete Message with size: "+ buffer.length());
		    	} else {
		    		System.out.println("We got the message: " + buffer.toString());
		    	}
		    });
		    
		  } else {
		    System.out.println("Failed to connect: " + res.cause().getMessage());
		  }
		});
		
		isReady = true;
    }
    
    public void sayHello() {
    	
    }
}

