package org.demo.neox.net;

import java.net.SocketTimeoutException;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.function.Supplier;


public class TcpRequest {

	public enum State {
		WAITING, SUCCESS, FAIL, TIMEOUT
	}

	private final Class<?> clazz;
	private final Object[] lock;
	private final AtomicReference<State> state;

	private final long sequence, timer;

	TcpRequest(Class<?> clazz,
			   Function<TcpRequest, Long> sequence,
			   Function<TcpRequest, Long> timer) {

		this.clazz = clazz;
		this.lock = new Object[1];
		this.state = new AtomicReference<>();
		this.sequence = sequence.apply(this);
		this.timer = timer.apply(this);
	}

	final void await(long timeoutMills) {

		if (state.compareAndSet(null, State.WAITING))
			throw new IllegalStateException("Request is corrupted.");

		synchronized (lock) {
			try {
				lock.wait(timeoutMills);
			} catch (InterruptedException e) {
				fail(e);
			}
		}

	}
	
	final boolean success(Object message) {
		if (state.compareAndSet(State.WAITING, State.SUCCESS)) {
			lock[0] = message;
			lock.notify();
			return true;
		}
		return false;
	}

	final boolean fail(Throwable error) {
		State state = this.state.getAndSet(State.FAIL);
		lock[0] = error;
		if (state == State.WAITING) {
			lock.notify();
			return true;
		}
		return false;
	}

	final boolean timeout(long timeout) {
		if (state.compareAndSet(State.WAITING, State.TIMEOUT)) {
			lock[0] = new SocketTimeoutException(
					String.format("Request timeout in %d ms", timeout));
			lock.notify();
			return true;
		}
		return false;
	}

	final long sequence() { return sequence;}

	final long timer() { return timer; }

	final Class<?> clazz() { return clazz; }

	final Throwable error() { return message(); }

	@SuppressWarnings("unchecked")
	final <T> T message() { return (T) lock[0]; }

}
