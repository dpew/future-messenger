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

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

public interface MessageSender<M> {

  /**
   * A non-blocking method to send a message. The future contains the status of the attempt to send.
   * When complete normally it is sent (the concept of 'sent' is up to the implementer). If unable
   * to send an future completes exceptionally. This method does not guarantee delivery of the
   * message.
   *
   * @param message The message to send.
   * @return A completable future indicating message sent.
   */
  CompletableFuture<Void> send(M message);

  /**
   * Wait for the receipt of a message based on a condition.
   *
   * @param condition The condition of receipt in which to return the given message.
   * @return A future that is completed when a message matching the condition arrives.
   */
  CompletableFuture<M> await(Predicate<M> condition);

  /**
   * Send a message and await a response.
   *
   * @param message The message to send.
   * @return a future capturing both the delivery and receipt of the message.
   */
  CompletableFuture<M> sendAndAwait(M message);

  /**
   * Send a message and await a response based on the condition.
   *
   * @param message The message to send.
   * @return a future capturing both the delivery and receipt of the message.
   */
  CompletableFuture<M> sendAndAwait(M message, Predicate<M> condition);

  /**
   * Register a listener based on a condition.
   *
   * @param condition
   * @param listener
   */
  <R> void listenFor(Predicate<M> condition, Function<M, R> function, Consumer<R> listener);
}
