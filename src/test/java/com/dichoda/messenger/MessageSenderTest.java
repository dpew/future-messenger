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

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import com.dichoda.messenger.MockMessage.IntegerMockMessage;
import com.dichoda.messenger.MockMessage.StringMockMessage;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicReference;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.DisableOnDebug;
import org.junit.rules.TestRule;
import org.junit.rules.Timeout;

public class MessageSenderTest {

  @SuppressWarnings("deprecation")
  @Rule
  public TestRule timeout = new DisableOnDebug(new Timeout(200));

  @Test
  public void itSendsAndReceivesMessages() throws InterruptedException, ExecutionException {
    MockMessageSender<MockMessage> sender = new MockMessageSender<>();

    sender.setMockReceiver(m -> sender.sendMockMessage(MockMessage.of(m, "Output")));

    StringMockMessage output =
        (StringMockMessage) sender.sendAndAwait(MockMessage.of("Input")).get();
    assertThat(output.value, equalTo("Output"));
  }

  int value;

  private void setInt(int value) {
    this.value = value;
  }

  @Test
  public void itListensForMesages() {
    AtomicReference<IntegerMockMessage> intMsg = new AtomicReference<>();
    AtomicReference<StringMockMessage> strMsg = new AtomicReference<>();

    MockMessageSender<MockMessage> sender = new MockMessageSender<>();
    sender.listenFor(
        IntegerMockMessage.class::isInstance, m -> ((IntegerMockMessage) m).value, this::setInt);
    sender.listenFor(
        IntegerMockMessage.class::isInstance, IntegerMockMessage.class::cast, intMsg::set);

    sender.listenFor(
        StringMockMessage.class::isInstance, StringMockMessage.class::cast, strMsg::set);
    sender.sendMockMessage(MockMessage.of("String"));
    sender.sendMockMessage(MockMessage.of(844));

    assertThat(value, equalTo(844));
    assertThat(intMsg.get().value, equalTo(844));
    assertThat(strMsg.get().value, equalTo("String"));
  }
}
