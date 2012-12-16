/*
 * Copyright 2012 Krzysztof Otrebski (krzysztof.otrebski@gmail.com)
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package pl.otros.vfs.browser.actions;

import pl.otros.vfs.browser.VfsBrowser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public abstract class BaseNavigateAction extends AbstractAction {

  private static final int SWITCH_TO_LOADING_TIME = 120;

  private static final Logger LOGGER = LoggerFactory.getLogger(BaseNavigateAction.class);

  public VfsBrowser browser;
  private static Executor executor = Executors.newSingleThreadExecutor();
  private volatile SwingWorker<Void, Void> showLoadingAfterDelayWorker;
  private Component focusOwner;

  public BaseNavigateAction(VfsBrowser browser) {
    super();
    this.browser = browser;
  }

  public BaseNavigateAction(VfsBrowser browser, String name) {
    this(browser);
    putValue(NAME, name);
  }

  public BaseNavigateAction(VfsBrowser browser, String name, Icon icon) {
    this(browser, name);
    putValue(SMALL_ICON, icon);
  }

  protected abstract void performLongOperation();

  @Override
  public final void actionPerformed(ActionEvent e) {
    doInUiThreadBefore();

    SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {

      @Override
      protected void done() {
        doInUiThreadAfter();
      }

      @Override
      protected Void doInBackground() throws Exception {
        try {
          performLongOperation();
        } catch (Exception e) {
          e.printStackTrace();
        }
        return null;
      }
    };
    executor.execute(worker);

  }

  protected final void doInUiThreadAfter() {
    LOGGER.debug("ShowLoadingStringWorker is " + showLoadingAfterDelayWorker);
    if (showLoadingAfterDelayWorker != null) {
      LOGGER.debug("Canceling showLoadingAfterDelayWorker");
      showLoadingAfterDelayWorker.cancel(false);
    }
    updateGuiAfter();
    LOGGER.debug("Updating UI after base action");
    browser.showTable();
    focusOwner.requestFocus();
  }

  protected void updateGuiAfter() {

  }

  protected final void doInUiThreadBefore() {
    focusOwner = KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner();
    showLoadingAfterDelayWorker = new SwingWorker<Void, Void>() {

      @Override
      protected void done() {
        boolean cancelled = isCancelled();
        LOGGER.debug("showLoadingAfterDelayWorker is cancelled={}", cancelled);
        if (!cancelled) {
          browser.showLoading();
        }
      }


      @Override
      protected Void doInBackground() throws Exception {
        Thread.sleep(SWITCH_TO_LOADING_TIME);
        return null;
      }
    };
    executor.execute(showLoadingAfterDelayWorker);

  }

  protected void updateGuiBefore() {

  }


}
