package org.demo.neox.net;

import java.lang.reflect.Array;


public class TcpRequest {
	
	final long requestSequence;
	final long timeoutTimerId;
	final Object[] responseMessage;

	TcpRequest(long sequence, long timeoutTimer) {
		this.timeoutTimerId = timeoutTimer;
		this.requestSequence = sequence;
		this.responseMessage = new Object[1];
	}
	
	public void wait(int timeoutMills) {
		synchronized (responseMessage) {
			try {
				responseMessage.wait(timeoutMills);
			} catch (InterruptedException e) {}
		}
	}
	
	public void response(Object message) {
		responseMessage[0] = message;
		responseMessage.notify();
	}
}
