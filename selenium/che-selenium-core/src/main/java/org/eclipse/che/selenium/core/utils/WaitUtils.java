/*
 * Copyright (c) 2012-2018 Red Hat, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Red Hat, Inc. - initial API and implementation
 */
package org.eclipse.che.selenium.core.utils;

import static org.eclipse.che.selenium.core.constant.TestTimeoutsConstants.LOAD_PAGE_TIMEOUT_SEC;

import java.util.concurrent.TimeUnit;
import org.openqa.selenium.TimeoutException;

/** @author Mykola Morhun */
public class WaitUtils {

  /**
   * Waits given time. When thread catch interrupt signal, than it immediately ends.
   *
   * @param seconds time to wait in seconds
   */
  public static void sleepQuietly(int seconds) {
    sleepQuietly(seconds, TimeUnit.SECONDS);
  }

  /**
   * Waits given time. When thread catch interrupt signal, than it immediately ends.
   *
   * @param timeout time to wait
   * @param timeUnit time unit of the timeout parameter
   */
  public static void sleepQuietly(int timeout, TimeUnit timeUnit) {
    long millisecondToWait = timeUnit.toMillis(timeout);
    try {
      Thread.sleep(millisecondToWait);
    } catch (InterruptedException e) {
      // Taking into account, that tests newer interrupts each other,
      // we can say, that this interrupt signal is external
      // and it wants to immediately stop execution of this thread.
      // According to the above, method, which calls this method
      // has no mechanisms for handling interrupts (so, invoke of
      // interrupt() method has no effect).
      // Considering the above, we must stop this thread here immediately.
      throw new RuntimeException(e);
    }
  }

  public static void waitSuccessCondition(BooleanCondition condition, int timeout)
      throws InterruptedException {
    for (int i = 1; i <= timeout; i++) {
      if (condition.execution()) {
        break;
      }

      if (i == timeout) {
        throw new TimeoutException("The condition has not being in \"true\" state during timeout");
      }

      Thread.sleep(1000);
    }
  }

  public static void waitSuccessCondition(BooleanCondition condition) throws InterruptedException {
    final int defaultTimeout = LOAD_PAGE_TIMEOUT_SEC;
    waitSuccessCondition(condition, defaultTimeout);
  }

  @FunctionalInterface
  public interface BooleanCondition {
    boolean execution();
  }
}
