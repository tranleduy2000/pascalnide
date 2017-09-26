/*
 *  Copyright (c) 2017 Tran Le Duy
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.duy.pascal.interperter.builtin_libraries.android.view;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.AndroidRuntimeException;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.View;

import com.duy.pascal.BasePascalApplication;
import com.duy.pascal.interperter.ast.expressioncontext.ExpressionContextMixin;
import com.duy.pascal.interperter.builtin_libraries.PascalLibrary;
import com.duy.pascal.interperter.builtin_libraries.android.AndroidLibraryManager;
import com.duy.pascal.interperter.builtin_libraries.android.activity.PascalActivityTaskExecutor;
import com.duy.pascal.interperter.builtin_libraries.android.view.dialog.AlertDialogTask;
import com.duy.pascal.interperter.builtin_libraries.android.view.dialog.DatePickerDialogTask;
import com.duy.pascal.interperter.builtin_libraries.android.view.dialog.DialogTask;
import com.duy.pascal.interperter.builtin_libraries.android.view.dialog.ProgressDialogTask;
import com.duy.pascal.interperter.builtin_libraries.android.view.dialog.SeekBarDialogTask;
import com.duy.pascal.interperter.builtin_libraries.android.view.dialog.TimePickerDialogTask;
import com.duy.pascal.interperter.builtin_libraries.annotations.PascalMethod;
import com.duy.pascal.interperter.builtin_libraries.annotations.PascalParameter;
import com.duy.pascal.interperter.exceptions.runtime.RuntimePascalException;
import com.duy.pascal.frontend.R;
import com.googlecode.sl4a.facade.AndroidEvent;
import com.googlecode.sl4a.interpreter.html.HtmlActivityTask;
import com.googlecode.sl4a.rpc.RpcDefault;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * User Interface Facade. <br>
 * <br>
 * <b>Usage Notes</b><br>
 * <br>
 * The UI facade provides access to a selection of dialog boxes for general user interaction, and
 * also hosts the {@link #showWebView} call which allows interactive use of html pages.<br>
 * The general use of the dialog FUNCTIONS is as follows:<br>
 * <ol>
 * <li>Create a dialog using one of the following calls:
 * <ul>
 * <li>{@link #createDialogInput}
 * <li>{@link #createAlertDialog}
 * <li>{@link #createDatePicker}
 * <li>{@link #createDialogHorizontalProgress}
 * <li>{@link #createDialogPassword}
 * <li>{@link #createDialogSeek}
 * <li>{@link #createDialogProcess}
 * </ul>
 * <li>Set additional features to your dialog
 * <ul>
 * <li>{@link #dialogSetItems} Set a list of items. Used like a menu.
 * <li>{@link #dialogSetMultiChoiceItems} Set a multichoice list of items.
 * <li>{@link #dialogSetSingleChoiceItems} Set a single choice list of items.
 * <li>{@link #dialogSetPositiveButtonText}
 * <li>{@link #dialogSetNeutralButtonText}
 * <li>{@link #dialogSetNegativeButtonText}
 * <li>{@link #dialogSetMaxProgress} Set max progress for your progress bar.
 * </ul>
 * <li>Display the dialog using {@link #showDialog}
 * <li>Update dialog information if needed
 * <ul>
 * <li>{@link #dialogSetCurrentProgress}
 * </ul>
 * <li>Get the results
 * <ul>
 * <li>Using {@link #dialogGetResponse}, which will wait until the user performs an action to close
 * the dialog box, or
 * <li>Use eventPoll to wait for a "dialog" event.
 * <li>You can find out which list items were selected using {@link #dialogGetSelectedItems}, which
 * returns an array of numeric indices to your list. For a single choice list, there will only ever
 * be one of these.
 * </ul>
 * <li>Once done, use {@link #dismissDialog} to remove the dialog.
 * </ol>
 * <br>
 * You can also manipulate menu options. The menu options are available for both {@link #showDialog}
 * <ul>
 * <li>{@link #clearOptionsMenu}
 * <li>{@link #addOptionsMenuItem}
 * </ul>
 * <br>
 * <b>Some notes:</b><br>
 * Not every dialogSet function is relevant to every dialog type, ie, dialogSetMaxProgress obviously
 * only applies to dialogs created with a progress bar. Also, an Alert Dialog may have a message or
 * items, not both. If you set both, items will take priority.<br>
 * In addition to the above FUNCTIONS, {@link #dialogGetInput} and {@link #dialogGetPassword} are
 * convenience FUNCTIONS that create, display and return the relevant dialogs in one call.<br>
 * There is only ever one instance of a dialog. Any dialogCreate call will cause the existing dialog
 * to be destroyed.
 *
 * @author MeanEYE.rcf (meaneye.rcf@gmail.com)
 */
public class AndroidDialogLib extends PascalLibrary {
    public static final String NAME = "aDialog".toLowerCase();
    // This value should not be used for menu groups outside this class.
    private static final int MENU_GROUP_ID = Integer.MAX_VALUE;

    private Context mContext;
    private PascalActivityTaskExecutor mTaskQueue;
    private List<MenuItem> mContextMenuItems;
    private List<MenuItem> mOptionsMenuItems;
    private AtomicBoolean mMenuUpdated;
    private AndroidEvent mEventFacade;
    private AndroidLibraryManager mManager;
    private DialogTask mDialogTask;

    public AndroidDialogLib(AndroidLibraryManager manager) {
        mContext = manager.getContext();
        mManager = manager;
        if (mContext != null) {
            mTaskQueue = ((BasePascalApplication) mContext).getTaskExecutor();
        }
        mContextMenuItems = new CopyOnWriteArrayList<>();
        mOptionsMenuItems = new CopyOnWriteArrayList<>();
        mEventFacade = manager.getReceiver(AndroidEvent.class);
        mMenuUpdated = new AtomicBoolean(false);
    }


    @PascalMethod(description = "Create a text input dialog.")
    public void createDialogInput(
            @PascalParameter(name = "title", description = "title of the input box")
            final StringBuilder title,
            @PascalParameter(name = "message", description = "message to display above the input box")
            final StringBuilder hint,
            @PascalParameter(name = "defaultText", description = "text to insert into the input box")
            final StringBuilder text,
            @PascalParameter(name = "inputType", description = "type of input data, ie number or text")
            final StringBuilder inputType) throws InterruptedException {
        dismissDialog();
        mDialogTask = new AlertDialogTask(title.toString(), "");
        ((AlertDialogTask) mDialogTask).setTextInput(text.toString());
        ((AlertDialogTask) mDialogTask).setHint(hint.toString());
        if (inputType != null) {
            ((AlertDialogTask) mDialogTask).setEditInputType(inputType.toString());
        }
    }

    @PascalMethod(description = "Create a password input dialog.")
    public void createDialogPassword(
            @PascalParameter(name = "title", description = "title of the input box")
            @RpcDefault("Password") final StringBuilder title,
            @PascalParameter(name = "message", description = "message to display above the input box")
            @RpcDefault("Please enter password:") final StringBuilder message) {
        dismissDialog();
        mDialogTask = new AlertDialogTask(title, message);
        ((AlertDialogTask) mDialogTask).setPasswordInput();
    }


    @SuppressWarnings("unchecked")
    @PascalMethod(description = "Queries the user for a text input.")
    public StringBuilder dialogGetInput(
            @PascalParameter(name = "title", description = "title of the input box") final StringBuilder title,
            @PascalParameter(name = "hint", description = "message to display above the input box") final StringBuilder hint,
            @PascalParameter(name = "default", description = "Default text") StringBuilder def)
            throws InterruptedException {
        createDialogInput(title, hint, def, new StringBuilder("text"));
        dialogSetNegativeButtonText(new StringBuilder(mContext.getString(R.string.cancel)));
        dialogSetPositiveButtonText(new StringBuilder(mContext.getString(R.string.ok)));
        showDialog();
        Map<String, Object> response = (Map<String, Object>) dialogGetResponse();
        if ("positive".equals(response.get("which"))) {
            return new StringBuilder(response.get("value").toString());
        } else {
            return new StringBuilder("");
        }
    }

    @PascalMethod(description = "Create time picker dialog.")
    public JSONObject dialogGetTime(
            @PascalParameter(name = "hour") @RpcDefault("0") int hour,
            @PascalParameter(name = "minute") @RpcDefault("0") int minute,
            @PascalParameter(name = "is24hour", description = "Use 24 hour clock") boolean is24hour)
            throws InterruptedException {

        createTimePicker(hour, minute, is24hour);
        showDialog();
        return (JSONObject) dialogGetResponse();
    }


    @PascalMethod(description = "Create date picker dialog.")
    public JSONObject dialogGetDate(@PascalParameter(name = "year") int year,
                                    @PascalParameter(name = "month") int month,
                                    @PascalParameter(name = "day") int day) throws InterruptedException {
        createDatePicker(year, month, day);
        showDialog();
        return (JSONObject) dialogGetResponse();
    }


    @SuppressWarnings({"unchecked", "unused"})
    @PascalMethod(description = "Queries the user for a password.")
    public StringBuilder dialogGetPassword(
            @PascalParameter(name = "title", description = "title of the password box") @RpcDefault("Password")
            final StringBuilder title,
            @PascalParameter(name = "message", description = "message to display above the input box")
            @RpcDefault("Please enter password:") final StringBuilder message)
            throws InterruptedException {
        createDialogPassword(title, message);
        dialogSetNegativeButtonText(new StringBuilder(mContext.getString(R.string.cancel)));
        dialogSetPositiveButtonText(new StringBuilder(mContext.getString(R.string.ok)));
        showDialog();
        Map<String, Object> response = (Map<String, Object>) dialogGetResponse();
        if ("positive".equals(response.get("which"))) {
            return new StringBuilder(response.get("value").toString());
        } else {
            return new StringBuilder("");
        }
    }


    @PascalMethod(description = "Create a spinner progress dialog.")
    public void createDialogProcess(@PascalParameter(name = "title") StringBuilder title,
                                    @PascalParameter(name = "message") StringBuilder message,
                                    @PascalParameter(name = "maximum progress") @RpcDefault("100") int max) {
        dismissDialog(); // Dismiss any existing dialog.
        mDialogTask = new ProgressDialogTask(ProgressDialog.STYLE_SPINNER, max, title, message, true);
    }


    @PascalMethod(description = "Create a horizontal progress dialog.")
    public void createDialogHorizontalProgress(
            @PascalParameter(name = "title") StringBuilder title,
            @PascalParameter(name = "message") StringBuilder message,
            @PascalParameter(name = "maximum progress") @RpcDefault("100") int max) {
        dismissDialog(); // Dismiss any existing dialog.
        mDialogTask =
                new ProgressDialogTask(ProgressDialog.STYLE_HORIZONTAL, max, title, message, true);
    }

    @PascalMethod(description = "Create alert dialog.")
    public void createAlertDialog(@PascalParameter(name = "title") StringBuilder title,
                                  @PascalParameter(name = "message") StringBuilder message) {
        dismissDialog(); // Dismiss any existing dialog.
        mDialogTask = new AlertDialogTask(title, message);
    }

    @PascalMethod(description = "Create alert dialog.")
    public void dialogAlert(@PascalParameter(name = "title") StringBuilder title,
                            @PascalParameter(name = "message") StringBuilder message,
                            @PascalParameter(name = "lock") boolean lock) throws InterruptedException {
        createAlertDialog(title, message);
        showDialog();
    }


    @PascalMethod(description = "Create seek bar dialog.")
    public void createDialogSeek(
            @PascalParameter(name = "starting value") @RpcDefault("50") int progress,
            @PascalParameter(name = "maximum value") @RpcDefault("100") int max,
            @PascalParameter(name = "title") StringBuilder title,
            @PascalParameter(name = "message") StringBuilder message) {
        dismissDialog(); // Dismiss any existing dialog.
        mDialogTask = new SeekBarDialogTask(progress, max, title, message);
    }


    @PascalMethod(description = "Create time picker dialog.")
    public void createTimePicker(
            @PascalParameter(name = "hour") @RpcDefault("0") int hour,
            @PascalParameter(name = "minute") @RpcDefault("0") int minute,
            @PascalParameter(name = "is24hour", description = "Use 24 hour clock") @RpcDefault("false") Boolean is24hour) {
        dismissDialog(); // Dismiss any existing dialog.
        mDialogTask = new TimePickerDialogTask(hour, minute, is24hour);
    }


    @PascalMethod(description = "Create date picker dialog.")
    public void createDatePicker(@PascalParameter(name = "year") @RpcDefault("1970") int year,
                                 @PascalParameter(name = "month") @RpcDefault("1") int month,
                                 @PascalParameter(name = "day") @RpcDefault("1") int day) {
        dismissDialog(); // Dismiss any existing dialog.
        mDialogTask = new DatePickerDialogTask(year, month, day);
    }

    @PascalMethod(description = "Dismiss dialog.")
    public void dismissDialog() {
        if (mDialogTask != null) {
            mDialogTask.dismissDialog();
            mDialogTask = null;
        }
    }

    @PascalMethod(description = "Show dialog.")
    public void showDialog() throws InterruptedException {
        if (mDialogTask != null && mDialogTask.getDialog() == null) {
            mDialogTask.setEventFacade(mEventFacade);
            mTaskQueue.execute(mDialogTask);
            mDialogTask.getShowLatch().await();
        } else {
            throw new RuntimeException("No dialog to show.");
        }
    }


    @PascalMethod(description = "Set progress dialog current value.")
    public void dialogSetCurrentProgress(@PascalParameter(name = "current") int current) {
        if (mDialogTask != null && mDialogTask instanceof ProgressDialogTask) {
            ((ProgressDialog) mDialogTask.getDialog()).setProgress(current);
        } else {
            throw new RuntimeException("No valid dialog to assign value to.");
        }
    }


    @PascalMethod(description = "Set progress dialog maximum value.")
    public void dialogSetMaxProgress(@PascalParameter(name = "max") int max) {
        if (mDialogTask != null && mDialogTask instanceof ProgressDialogTask) {
            ((ProgressDialog) mDialogTask.getDialog()).setMax(max);
        } else {
            throw new RuntimeException("No valid dialog to set maximum value of.");
        }
    }

    @PascalMethod(description = "Set alert dialog positive button text.")
    public void dialogSetPositiveButtonText(@PascalParameter(name = "text") StringBuilder text) {
        if (mDialogTask != null && mDialogTask instanceof AlertDialogTask) {
            ((AlertDialogTask) mDialogTask).setPositiveButtonText(text);
        } else if (mDialogTask != null && mDialogTask instanceof SeekBarDialogTask) {
            ((SeekBarDialogTask) mDialogTask).setPositiveButtonText(text);
        } else {
            throw new AndroidRuntimeException("No dialog to add button to.");
        }
    }

    @PascalMethod(description = "Set alert dialog button text.")
    public void dialogSetNegativeButtonText(@PascalParameter(name = "text") StringBuilder text) {
        if (mDialogTask != null && mDialogTask instanceof AlertDialogTask) {
            ((AlertDialogTask) mDialogTask).setNegativeButtonText(text);
        } else if (mDialogTask != null && mDialogTask instanceof SeekBarDialogTask) {
            ((SeekBarDialogTask) mDialogTask).setNegativeButtonText(text);
        } else {
            throw new AndroidRuntimeException("No dialog to add button to.");
        }
    }


    @PascalMethod(description = "Set alert dialog button text.")
    public void dialogSetNeutralButtonText(@PascalParameter(name = "text") StringBuilder text) {
        if (mDialogTask != null && mDialogTask instanceof AlertDialogTask) {
            ((AlertDialogTask) mDialogTask).setNeutralButtonText(text);
        } else {
            throw new AndroidRuntimeException("No dialog to add button to.");
        }
    }

    // TODO(damonkohler): Make RPC layer translate between JSONArray and List<Object>.

    /**
     * This effectively creates list of options. Clicking on an item will immediately return an "item"
     * element, which is the index of the selected item.
     */

    @PascalMethod(description = "Set alert dialog list items.")
    public void dialogSetItems(@PascalParameter(name = "items") JSONArray items) {
        if (mDialogTask != null && mDialogTask instanceof AlertDialogTask) {
            ((AlertDialogTask) mDialogTask).setItems(items);
        } else {
            throw new AndroidRuntimeException("No dialog to add list to.");
        }
    }

    /**
     * This creates a list of radio buttons. You can select one item out of the list. A response will
     * not be returned until the dialog is closed, either with the Cancel key or a button
     * (positive/negative/neutral). Use {@link #dialogGetSelectedItems()} to find out what was
     * selected.
     */

    @PascalMethod(description = "Set dialog single choice items and selected item.")
    public void dialogSetSingleChoiceItems(
            @PascalParameter(name = "items") JSONArray items,
            @PascalParameter(name = "selected", description = "selected item index") @RpcDefault("0") int selected) {
        if (mDialogTask != null && mDialogTask instanceof AlertDialogTask) {
            ((AlertDialogTask) mDialogTask).setSingleChoiceItems(items, selected);
        } else {
            throw new AndroidRuntimeException("No dialog to add list to.");
        }
    }

    /**
     * This creates a list of check boxes. You can select multiple items out of the list. A response
     * will not be returned until the dialog is closed, either with the Cancel key or a button
     * (positive/negative/neutral). Use {@link #dialogGetSelectedItems()} to find out what was
     * selected.
     */


    @PascalMethod(description = "Set dialog multiple choice items and selection.")
    public void dialogSetMultiChoiceItems(
            @PascalParameter(name = "items") JSONArray items,
            @PascalParameter(name = "selected", description = "list of selected items") JSONArray selected)
            throws JSONException {
        if (mDialogTask != null && mDialogTask instanceof AlertDialogTask) {
            ((AlertDialogTask) mDialogTask).setMultiChoiceItems(items, selected);
        } else {
            throw new AndroidRuntimeException("No dialog to add list to.");
        }
    }

    public Object dialogGetResponse() {
        try {
            return mDialogTask.getResult();
        } catch (Exception e) {
            throw new AndroidRuntimeException(e);
        }
    }


    @PascalMethod(description = "This method provides list of items user selected.", returns = "Selected items")
    public int[] dialogGetSelectedItems() throws RuntimePascalException {
        if (mDialogTask != null && mDialogTask instanceof AlertDialogTask) {
            Set<Integer> selectedItems = ((AlertDialogTask) mDialogTask).getSelectedItems();
            Iterator<Integer> iterator = selectedItems.iterator();
            int[] result = new int[selectedItems.size()];
            for (int i = 0; i < selectedItems.size(); i++) {
                result[i] = iterator.next();
            }
            return result;
        } else {
            throw new RuntimePascalException("No dialog to add list to.");
        }
    }

    @PascalMethod(description = "Display a WebView with the given URL.")
    public void showWebView(
            @PascalParameter(name = "url") StringBuilder url,
            @PascalParameter(name = "wait", description = "block until the user exits the WebView") boolean wait)
            throws IOException {
        HtmlActivityTask task = new HtmlActivityTask(mManager, url.toString(), false);
        mTaskQueue.execute(task);
        if (wait) {
            try {
                task.getResult();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Context menus are used primarily with {@link #showWebView}
     */
    @PascalMethod(description = "Adds a new item to context menu.")
    public void addContextMenuItem(
            @PascalParameter(name = "label", description = "label for this menu item") StringBuilder label,
            @PascalParameter(name = "event", description = "event that will be generated on menu item click") StringBuilder event,
            @PascalParameter(name = "eventData") Object data) {
        mContextMenuItems.add(new MenuItem(label, event, data, null));
    }

    @PascalMethod(description = "Adds a new item to options menu.")
    public void addOptionsMenuItem(
            @PascalParameter(name = "label", description = "label for this menu item") StringBuilder label,
            @PascalParameter(name = "event", description = "event that will be generated on menu item click") StringBuilder event,
            @PascalParameter(name = "eventData") Object data,
            @PascalParameter(name = "iconName", description = "Android system menu icon, see http://developer.android.com/reference/android/R.drawable.html") String iconName) {
        mOptionsMenuItems.add(new MenuItem(label, event, data, iconName));
        mMenuUpdated.set(true);
    }


    @PascalMethod(description = "Removes all items previously added to context menu.")
    public void clearContextMenu() {
        mContextMenuItems.clear();
    }


    @PascalMethod(description = "Removes all items previously added to options menu.")
    public void clearOptionsMenu() {
        mOptionsMenuItems.clear();
        mMenuUpdated.set(true);
    }

    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
        for (MenuItem item : mContextMenuItems) {
            android.view.MenuItem menuItem = menu.add(item.mmTitle);
            menuItem.setOnMenuItemClickListener(item.mmListener);
        }
    }

    public boolean onPrepareOptionsMenu(Menu menu) {
        if (mMenuUpdated.getAndSet(false)) {
            menu.removeGroup(MENU_GROUP_ID);
            for (MenuItem item : mOptionsMenuItems) {
                android.view.MenuItem menuItem = menu.add(MENU_GROUP_ID, Menu.NONE, Menu.NONE, item.mmTitle);
                if (item.mmIcon != null) {
                    menuItem.setIcon(mContext.getResources()
                            .getIdentifier(item.mmIcon, "drawable", "android"));
                }
                menuItem.setOnMenuItemClickListener(item.mmListener);
            }
            return true;
        }
        return true;
    }


    @Override
    public boolean instantiate(Map<String, Object> pluginargs) {
        return false;
    }

    @Override
    public void shutdown() {

    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public void declareConstants(ExpressionContextMixin parentContext) {

    }

    @Override
    public void declareTypes(ExpressionContextMixin parentContext) {

    }

    @Override
    public void declareVariables(ExpressionContextMixin parentContext) {

    }

    @Override
    public void declareFunctions(ExpressionContextMixin parentContext) {

    }

    private class MenuItem {

        private final CharSequence mmTitle;
        private final CharSequence mmEvent;
        private final Object mmEventData;
        private final String mmIcon;
        private final android.view.MenuItem.OnMenuItemClickListener mmListener;

        public MenuItem(CharSequence title, CharSequence event, Object data, String icon) {
            mmTitle = title;
            mmEvent = event;
            mmEventData = data;
            mmIcon = icon;
            mmListener = new android.view.MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(android.view.MenuItem item) {
                    // TODO(damonkohler): Does mmEventData need to be cloned somehow?
                    mEventFacade.postEvent(mmEvent.toString(), mmEventData);
                    return true;
                }
            };
        }
    }
}