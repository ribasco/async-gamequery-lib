/*
 * Copyright 2022 Asynchronous Game Query Library
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ibasco.agql.core.transport.pool;

import io.netty.channel.Channel;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.*;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

//TODO: Add a release listener, using CompletableFuture/Future can only be used once.
abstract public class PooledChannel implements Channel {

    private final ReleaseFuture releaseFuture = new ReleaseFuture(this);

    private final Set<BiConsumer<PooledChannel, Throwable>> listeners = new HashSet<>();

    /**
     * Add release listener
     *
     * @param listener
     *         The callback to notify when this channel has been released
     */
    public void addListener(BiConsumer<PooledChannel, Throwable> listener) {
        listeners.add(listener);
    }

    /**
     * Remove release listener
     *
     * @param listener
     *         The callback to remove
     */
    public void removeListener(BiConsumer<PooledChannel, Throwable> listener) {
        listeners.remove(listener);
    }

    void notifyRelease() {
        notifyRelease(null);
    }

    void notifyRelease(Throwable error) {
        listeners.forEach(c -> c.accept(this, error));
    }

    abstract public NettyChannelPool getChannelPool();

    abstract public CompletableFuture<Void> release();

    public CompletableFuture<Channel> releaseFuture() {
        return releaseFuture;
    }

    static class ReleaseFuture extends CompletableFuture<Channel> {

        private final Channel channel;

        private CompletableFuture<Channel> delegate;

        ReleaseFuture(Channel channel) {
            this.delegate = new CompletableFuture<>();
            this.channel = channel;
        }

        void success() {
            delegate.complete(channel);
        }

        void fail(Throwable error) {
            delegate.completeExceptionally(error);
        }

        void reset() {
            this.delegate = new CompletableFuture<>();
        }

        @Override
        public boolean isDone() {
            return delegate.isDone();
        }

        @Override
        public Channel get() throws InterruptedException, ExecutionException {
            return delegate.get();
        }

        @Override
        public Channel get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
            return delegate.get(timeout, unit);
        }

        @Override
        public Channel join() {
            return delegate.join();
        }

        @Override
        public Channel getNow(Channel valueIfAbsent) {
            return delegate.getNow(valueIfAbsent);
        }

        @Override
        public boolean complete(Channel value) {
            throw new IllegalStateException("You are not allowed to complete a release future");
        }

        @Override
        public boolean completeExceptionally(Throwable ex) {
            throw new IllegalStateException("You are not allowed to complete a release future");
        }

        @Override
        public <U> CompletableFuture<U> thenApply(Function<? super Channel, ? extends U> fn) {
            return delegate.thenApply(fn);
        }

        @Override
        public <U> CompletableFuture<U> thenApplyAsync(Function<? super Channel, ? extends U> fn) {
            return delegate.thenApplyAsync(fn);
        }

        @Override
        public <U> CompletableFuture<U> thenApplyAsync(Function<? super Channel, ? extends U> fn, Executor executor) {
            return delegate.thenApplyAsync(fn, executor);
        }

        @Override
        public CompletableFuture<Void> thenAccept(Consumer<? super Channel> action) {
            return delegate.thenAccept(action);
        }

        @Override
        public CompletableFuture<Void> thenAcceptAsync(Consumer<? super Channel> action) {
            return delegate.thenAcceptAsync(action);
        }

        @Override
        public CompletableFuture<Void> thenAcceptAsync(Consumer<? super Channel> action, Executor executor) {
            return delegate.thenAcceptAsync(action, executor);
        }

        @Override
        public CompletableFuture<Void> thenRun(Runnable action) {
            return delegate.thenRun(action);
        }

        @Override
        public CompletableFuture<Void> thenRunAsync(Runnable action) {
            return delegate.thenRunAsync(action);
        }

        @Override
        public CompletableFuture<Void> thenRunAsync(Runnable action, Executor executor) {
            return delegate.thenRunAsync(action, executor);
        }

        @Override
        public <U, V> CompletableFuture<V> thenCombine(CompletionStage<? extends U> other, BiFunction<? super Channel, ? super U, ? extends V> fn) {
            return delegate.thenCombine(other, fn);
        }

        @Override
        public <U, V> CompletableFuture<V> thenCombineAsync(CompletionStage<? extends U> other, BiFunction<? super Channel, ? super U, ? extends V> fn) {
            return delegate.thenCombineAsync(other, fn);
        }

        @Override
        public <U, V> CompletableFuture<V> thenCombineAsync(CompletionStage<? extends U> other, BiFunction<? super Channel, ? super U, ? extends V> fn, Executor executor) {
            return delegate.thenCombineAsync(other, fn, executor);
        }

        @Override
        public <U> CompletableFuture<Void> thenAcceptBoth(CompletionStage<? extends U> other, BiConsumer<? super Channel, ? super U> action) {
            return delegate.thenAcceptBoth(other, action);
        }

        @Override
        public <U> CompletableFuture<Void> thenAcceptBothAsync(CompletionStage<? extends U> other, BiConsumer<? super Channel, ? super U> action) {
            return delegate.thenAcceptBothAsync(other, action);
        }

        @Override
        public <U> CompletableFuture<Void> thenAcceptBothAsync(CompletionStage<? extends U> other, BiConsumer<? super Channel, ? super U> action, Executor executor) {
            return delegate.thenAcceptBothAsync(other, action, executor);
        }

        @Override
        public CompletableFuture<Void> runAfterBoth(CompletionStage<?> other, Runnable action) {
            return delegate.runAfterBoth(other, action);
        }

        @Override
        public CompletableFuture<Void> runAfterBothAsync(CompletionStage<?> other, Runnable action) {
            return delegate.runAfterBothAsync(other, action);
        }

        @Override
        public CompletableFuture<Void> runAfterBothAsync(CompletionStage<?> other, Runnable action, Executor executor) {
            return delegate.runAfterBothAsync(other, action, executor);
        }

        @Override
        public <U> CompletableFuture<U> applyToEither(CompletionStage<? extends Channel> other, Function<? super Channel, U> fn) {
            return delegate.applyToEither(other, fn);
        }

        @Override
        public <U> CompletableFuture<U> applyToEitherAsync(CompletionStage<? extends Channel> other, Function<? super Channel, U> fn) {
            return delegate.applyToEitherAsync(other, fn);
        }

        @Override
        public <U> CompletableFuture<U> applyToEitherAsync(CompletionStage<? extends Channel> other, Function<? super Channel, U> fn, Executor executor) {
            return delegate.applyToEitherAsync(other, fn, executor);
        }

        @Override
        public CompletableFuture<Void> acceptEither(CompletionStage<? extends Channel> other, Consumer<? super Channel> action) {
            return delegate.acceptEither(other, action);
        }

        @Override
        public CompletableFuture<Void> acceptEitherAsync(CompletionStage<? extends Channel> other, Consumer<? super Channel> action) {
            return delegate.acceptEitherAsync(other, action);
        }

        @Override
        public CompletableFuture<Void> acceptEitherAsync(CompletionStage<? extends Channel> other, Consumer<? super Channel> action, Executor executor) {
            return delegate.acceptEitherAsync(other, action, executor);
        }

        @Override
        public CompletableFuture<Void> runAfterEither(CompletionStage<?> other, Runnable action) {
            return delegate.runAfterEither(other, action);
        }

        @Override
        public CompletableFuture<Void> runAfterEitherAsync(CompletionStage<?> other, Runnable action) {
            return delegate.runAfterEitherAsync(other, action);
        }

        @Override
        public CompletableFuture<Void> runAfterEitherAsync(CompletionStage<?> other, Runnable action, Executor executor) {
            return delegate.runAfterEitherAsync(other, action, executor);
        }

        @Override
        public <U> CompletableFuture<U> thenCompose(Function<? super Channel, ? extends CompletionStage<U>> fn) {
            return delegate.thenCompose(fn);
        }

        @Override
        public <U> CompletableFuture<U> thenComposeAsync(Function<? super Channel, ? extends CompletionStage<U>> fn) {
            return delegate.thenComposeAsync(fn);
        }

        @Override
        public <U> CompletableFuture<U> thenComposeAsync(Function<? super Channel, ? extends CompletionStage<U>> fn, Executor executor) {
            return delegate.thenComposeAsync(fn, executor);
        }

        @Override
        public CompletableFuture<Channel> whenComplete(BiConsumer<? super Channel, ? super Throwable> action) {
            return delegate.whenComplete(action);
        }

        @Override
        public CompletableFuture<Channel> whenCompleteAsync(BiConsumer<? super Channel, ? super Throwable> action) {
            return delegate.whenCompleteAsync(action);
        }

        @Override
        public CompletableFuture<Channel> whenCompleteAsync(BiConsumer<? super Channel, ? super Throwable> action, Executor executor) {
            return delegate.whenCompleteAsync(action, executor);
        }

        @Override
        public <U> CompletableFuture<U> handle(BiFunction<? super Channel, Throwable, ? extends U> fn) {
            return delegate.handle(fn);
        }

        @Override
        public <U> CompletableFuture<U> handleAsync(BiFunction<? super Channel, Throwable, ? extends U> fn) {
            return delegate.handleAsync(fn);
        }

        @Override
        public <U> CompletableFuture<U> handleAsync(BiFunction<? super Channel, Throwable, ? extends U> fn, Executor executor) {
            return delegate.handleAsync(fn, executor);
        }

        @Override
        public CompletableFuture<Channel> toCompletableFuture() {
            return delegate.toCompletableFuture();
        }

        @Override
        public CompletableFuture<Channel> exceptionally(Function<Throwable, ? extends Channel> fn) {
            return delegate.exceptionally(fn);
        }

        @Override
        public boolean cancel(boolean mayInterruptIfRunning) {
            throw new IllegalStateException("You are not allowed to cancel a release future");
        }

        @Override
        public boolean isCancelled() {
            return delegate.isCancelled();
        }

        @Override
        public boolean isCompletedExceptionally() {
            return delegate.isCompletedExceptionally();
        }

        @Override
        public void obtrudeValue(Channel value) {
            delegate.obtrudeValue(value);
        }

        @Override
        public void obtrudeException(Throwable ex) {
            delegate.obtrudeException(ex);
        }

        @Override
        public int getNumberOfDependents() {
            return delegate.getNumberOfDependents();
        }

        @Override
        public String toString() {
            return delegate.toString();
        }
    }
}
