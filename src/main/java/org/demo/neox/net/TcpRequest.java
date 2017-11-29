package org.demo.neox.net;

import org.demo.neox.rpc.Message;

import java.net.SocketTimeoutException;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;


public class TcpRequest {

	public enum State {
		WAITING, SUCCESS, REDICRECT, FAIL, TIMEOUT
	}

	private final long timer;
	private final Class<? extends Message> clazz;
	private final Object[] lock;
	private final AtomicReference<State> state;


	TcpRequest(Class<? extends Message> clazz, Function<TcpRequest, Long> timer) {
		this.clazz = clazz;
		this.lock = new Object[1];
		this.state = new AtomicReference<>();
		this.timer = timer.apply(this);
	}

	final void await(long timeoutMills, Runnable asyncRpc) {

		if (state.compareAndSet(null, State.WAITING))
			throw new IllegalStateException("Request is corrupted.");

		synchronized (lock) {
			try {
				asyncRpc.run();
				lock.wait(timeoutMills);
			} catch (Exception e) {
				fail(e);
			}
		}

	}
	
	final boolean success(Object message, boolean expected) {
		if (state.compareAndSet(State.WAITING,
				expected ? State.SUCCESS: State.REDICRECT)) {
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

	@SuppressWarnings("unchecked")
	final <T> T result() { return (T) lock[0]; }

	final long timer() { return timer; }

	final Class<? extends Message> clazz() { return clazz; }

	final State state() { return state.get(); }
}
