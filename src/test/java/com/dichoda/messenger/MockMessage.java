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

import java.util.UUID;

public interface MockMessage {

  default String getCorrelationId() {
    return null;
  }

  public static class BaseTestMessage implements MockMessage {
    protected String correlationId;

    public BaseTestMessage() {
      this(null);
    }

    public BaseTestMessage(String cid) {
      this.correlationId = cid == null ? UUID.randomUUID().toString() : cid;
    }

    @Override
    public String getCorrelationId() {
      return correlationId;
    }
  }

  public static class StringMockMessage extends BaseTestMessage implements MockMessage {
    public String value;

    public StringMockMessage(String text) {
      this(null, text);
    }

    public StringMockMessage(String cid, String text) {
      super(cid);
      this.value = text;
    }
  }

  public static class IntegerMockMessage extends BaseTestMessage implements MockMessage {
    public Integer value;

    public IntegerMockMessage(Integer value) {
      this(null, value);
    }

    public IntegerMockMessage(String cid, Integer value) {
      super(cid);
      this.value = value;
    }
  }

  public static StringMockMessage of(String value) {
    return new StringMockMessage(value);
  }

  public static StringMockMessage of(MockMessage m, String value) {
    return new StringMockMessage(m.getCorrelationId(), value);
  }

  public static IntegerMockMessage of(Integer value) {
    return new IntegerMockMessage(value);
  }

  public static IntegerMockMessage of(MockMessage m, Integer value) {
    return new IntegerMockMessage(m.getCorrelationId(), value);
  }
}
