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

public class AbstractMessageSender<M> implements MessageSender<M> {

  @Override
  public CompletableFuture<Void> send(M message) {
    return null;
  }

  @Override
  public CompletableFuture<M> await(Predicate<M> condition) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public CompletableFuture<M> sendAndAwait(M message) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public CompletableFuture<M> sendAndAwait(M message, Predicate<M> condition) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public <R> void listenFor(Predicate<M> condition, Function<M, R> function, Consumer<R> listener) {
    // TODO Auto-generated method stub

  }
}
