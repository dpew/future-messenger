/*
    MIT License

    Copyright (c) 2018 Dichoda Software Solutions

    Permission is hereby granted, free of charge, to any person obtaining a copy
    of this software and associated documentation files (the "Software"), to deal
    in the Software without restriction, including without limitation the rights
    to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
    copies of the Software, and to permit persons to whom the Software is
    furnished to do so, subject to the following conditions:

    The above copyright notice and this permission notice shall be included in all
    copies or substantial portions of the Software.

    THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
    IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
    FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
    AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
    LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
    OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
    SOFTWARE.
*/
package com.dichoda.messenger;

import java.util.List;
import java.util.Objects;
import java.util.Queue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class MockMessageSender<M extends MockMessage> implements MessageSender<M> {

  public Queue<ConditionalFuture<M>> futures = new ConcurrentLinkedQueue<>();
  public List<ConditionalListener<M, ?>> listeners = new CopyOnWriteArrayList<>();
  private Consumer<M> receiver = (m) -> {};

  public void setMockReceiver(Consumer<M> receiver) {
    this.receiver = receiver;
  }

  public void sendMockMessage(M message) {
    listeners.stream().forEach(l -> l.apply(message));
    futures.removeAll(
        futures
            .stream()
            .peek(f -> f.complete(message))
            .filter(ConditionalFuture::isDone)
            .collect(Collectors.toList()));
  }

  @Override
  public CompletableFuture<Void> send(M message) {
    return CompletableFuture.runAsync(() -> receiver.accept(message));
  }

  @Override
  public CompletableFuture<M> await(Predicate<M> condition) {
    ConditionalFuture<M> f = new ConditionalFuture<>(condition);
    futures.add(f);
    return f.future;
  }

  @Override
  public CompletableFuture<M> sendAndAwait(M message) {
    return sendAndAwait(
        message, m -> Objects.equals(m.getCorrelationId(), message.getCorrelationId()));
  }

  @Override
  public CompletableFuture<M> sendAndAwait(M message, Predicate<M> condition) {
    CompletableFuture<M> f = await(condition);
    return send(message).thenCombine(f, (s, r) -> r);
  }

  @Override
  public <R> void listenFor(Predicate<M> condition, Function<M, R> function, Consumer<R> listener) {
    listeners.add(new ConditionalListener<M, R>(condition, function, listener));
  }

  protected static class ConditionalFuture<M> {
    CompletableFuture<M> future = new CompletableFuture<>();
    Predicate<M> condition;

    public ConditionalFuture(Predicate<M> condition) {
      super();
      this.condition = condition;
    }

    public void complete(M m) {
      if (condition.test(m)) future.complete(m);
    }

    public boolean isDone() {
      return future.isDone();
    }
  }

  protected static class ConditionalListener<M, R> {
    Predicate<M> condition;
    Function<M, R> function;
    Consumer<R> listener;

    public ConditionalListener(
        Predicate<M> condition, Function<M, R> function, Consumer<R> listener) {
      super();
      this.condition = condition;
      this.function = function;
      this.listener = listener;
    }

    public void apply(M m) {
      if (condition.test(m)) listener.accept(function.apply(m));
    }
  }
}
