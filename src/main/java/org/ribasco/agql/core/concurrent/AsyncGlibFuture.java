package org.ribasco.agql.core.concurrent;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

//TODO: For re-work
public class AsyncGlibFuture<T> extends CompletableFuture<T> {

    private CompletableFuture<T> genericFuture;

    public AsyncGlibFuture() {
        super();
    }

    public AsyncGlibFuture(CompletableFuture<T> future) {
        this.genericFuture = future;
    }

    public T getUnchecked() {
        try {
            return super.get();
        } catch (InterruptedException | ExecutionException ignored) {
        }
        return null;
    }

    public T getUnchecked(long timeout, TimeUnit unit) throws TimeoutException {
        try {
            return super.get(timeout, unit);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }
}
