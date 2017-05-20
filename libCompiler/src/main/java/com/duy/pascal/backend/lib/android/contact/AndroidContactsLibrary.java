/*
 * Copyright (C) 2010 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.duy.pascal.backend.lib.android.contact;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.Contacts.People;
import android.provider.Contacts.PhonesColumns;

import com.duy.pascal.backend.lib.PascalLibrary;
import com.duy.pascal.backend.lib.android.AndroidLibraryManager;
import com.duy.pascal.backend.lib.annotations.PascalMethod;
import com.duy.pascal.backend.lib.annotations.PascalParameter;
import com.googlecode.sl4a.facade.CommonIntentsFacade;
import com.googlecode.sl4a.rpc.RpcOptional;
import com.js.interpreter.expressioncontext.ExpressionContextMixin;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Provides access to contacts related functionality.
 *
 * @author MeanEYE.rcf (meaneye.rcf@gmail.com
 */
public class AndroidContactsLibrary implements PascalLibrary {
    private static final Uri CONTACTS_URI = Uri.parse("content://contacts/people");
    private final ContentResolver mContentResolver;
    private final Context mContext;
    private final CommonIntentsFacade mCommonIntentsFacade;
    public Uri mPhoneContent = null;
    public String mContactId;
    public String mPrimary;
    public String mPhoneNumber;
    @SuppressWarnings("unused")
    public String mHasPhoneNumber;

    public AndroidContactsLibrary(AndroidLibraryManager manager) {
        mContext = manager.getContext();
        mContentResolver = mContext.getContentResolver();
        mCommonIntentsFacade = manager.getReceiver(CommonIntentsFacade.class);
        try {
            // Backward compatibility... get contract stuff using reflection
            Class<?> phone = Class.forName("android.provider.ContactsContract$CommonDataKinds$Phone");
            mPhoneContent = (Uri) phone.getField("CONTENT_URI").get(null);
            mContactId = (String) phone.getField("CONTACT_ID").get(null);
            mPrimary = (String) phone.getField("IS_PRIMARY").get(null);
            mPhoneNumber = (String) phone.getField("NUMBER").get(null);
            mHasPhoneNumber = (String) phone.getField("HAS_PHONE_NUMBER").get(null);
        } catch (Exception ignored) {
        }
    }

    private Uri buildUri(int id) {
        return ContentUris.withAppendedId(People.CONTENT_URI, id);
    }

    @SuppressWarnings("unused")
    @PascalMethod(description = "Displays a list of contacts to pick from.", returns = "A map of result values.")
    public Intent pickContact() throws JSONException {
        return mCommonIntentsFacade.pick("content://contacts/people");
    }

    @SuppressWarnings("unused")
    @PascalMethod(description = "Displays a list of phone numbers to pick from.", returns = "The selected phone number.")
    public String pickPhone() throws JSONException {
        String result = null;
        Intent data = mCommonIntentsFacade.pick("content://contacts/phones");
        if (data != null) {
            Uri phoneData = data.getData();
            Cursor cursor = mContext.getContentResolver().query(phoneData, null, null, null, null);
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndexOrThrow(PhonesColumns.NUMBER));
                }
                cursor.close();
            }
        }
        return result;
    }

    @SuppressWarnings("unused")
    @PascalMethod(description = "Returns a List of all possible attributes for contacts.")
    public List<String> contactsGetAttributes() {
        List<String> result = new ArrayList<>();
        Cursor cursor = mContentResolver.query(CONTACTS_URI, null, null, null, null);
        if (cursor != null) {
            String[] columns = cursor.getColumnNames();
            Collections.addAll(result, columns);
            cursor.close();
        }
        return result;
    }

    // TODO(MeanEYE.rcf): Add ability to narrow selection by providing named pairs of attributes.
    @SuppressWarnings("unused")
    @PascalMethod(description = "Returns a List of all contact IDs.")
    public List<Integer> contactsGetIds() {
        List<Integer> result = new ArrayList<>();
        String[] columns = {"_id"};
        Cursor cursor = mContentResolver.query(CONTACTS_URI, columns, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                result.add(cursor.getInt(0));
            }
            cursor.close();
        }
        return result;
    }

    @SuppressWarnings("unused")
    @PascalMethod(description = "Returns a List of all contacts.", returns = "a List of contacts as Maps")
    public List<JSONObject> contactsGet(
            @PascalParameter(name = "attributes") @RpcOptional JSONArray attributes) throws JSONException {
        List<JSONObject> result = new ArrayList<>();
        String[] columns;
        if (attributes == null || attributes.length() == 0) {
            // In case no attributes are specified we set the default ones.
            columns = new String[]{"_id", "name", "primary_phone", "primary_email", "operator"};
        } else {
            // Convert selected attributes list into usable string list.
            columns = new String[attributes.length()];
            for (int i = 0; i < attributes.length(); i++) {
                columns[i] = attributes.getString(i);
            }
        }
        List<String> queryList = new ArrayList<>();
        Collections.addAll(queryList, columns);
        if (!queryList.contains("_id")) {
            queryList.add("_id");
        }

        String[] query = queryList.toArray(new String[queryList.size()]);
        Cursor cursor = mContentResolver.query(CONTACTS_URI, query, null, null, null);
        if (cursor != null) {
            int idIndex = cursor.getColumnIndex("_id");
            while (cursor.moveToNext()) {
                String id = cursor.getString(idIndex);
                JSONObject message = new JSONObject();
                for (String key : columns) {
                    String value = cursor.getString(cursor.getColumnIndex(key));
                    if (mPhoneNumber != null) {
                        if (key.equals("primary_phone")) {
                            value = findPhone(id);
                        }
                    }
                    message.put(key, value);
                }
                result.add(message);
            }
            cursor.close();
        }
        return result;
    }

    private String findPhone(String id) {
        String result = null;
        if (id == null || id.equals("")) {
            return null;
        }
        try {
            if (Integer.parseInt(id) > 0) {
                Cursor pCur =
                        mContentResolver.query(mPhoneContent, new String[]{mPhoneNumber}, mContactId
                                + " = ? and " + mPrimary + "=1", new String[]{id}, null);
                if (pCur != null) {
                    pCur.getColumnNames();
                    while (pCur.moveToNext()) {
                        result = pCur.getString(0);
                        break;
                    }
                }
                if (pCur != null) {
                    pCur.close();
                }
            }
        } catch (Exception e) {
            return null;
        }
        return result;
    }

    @SuppressWarnings("unused")
    @PascalMethod(description = "Returns contacts by ID.")
    public JSONObject contactsGetById(@PascalParameter(name = "id") int id,
                                      @PascalParameter(name = "attributes") @RpcOptional JSONArray attributes) throws JSONException {
        JSONObject result = null;
        Uri uri = buildUri(id);
        String[] columns;
        if (attributes == null || attributes.length() == 0) {
            // In case no attributes are specified we set the default ones.
            columns = new String[]{"_id", "name", "primary_phone", "primary_email", "operator"};
        } else {
            // Convert selected attributes list into usable string list.
            columns = new String[attributes.length()];
            for (int i = 0; i < attributes.length(); i++) {
                columns[i] = attributes.getString(i);
            }
        }
        Cursor cursor = mContentResolver.query(uri, columns, null, null, null);
        if (cursor != null) {
            result = new JSONObject();
            cursor.moveToFirst();
            for (int i = 0; i < columns.length; i++) {
                result.put(columns[i], cursor.getString(i));
            }
            cursor.close();
        }
        return result;
    }

    // TODO(MeanEYE.rcf): Add ability to narrow selection by providing named pairs of attributes.
    @SuppressWarnings("unused")
    @PascalMethod(description = "Returns the number of contacts.")
    public int contactsGetCount() {
        int result = 0;
        Cursor cursor = mContentResolver.query(CONTACTS_URI, null, null, null, null);
        if (cursor != null) {
            result = cursor.getCount();
            cursor.close();
        }
        return result;
    }

    private String[] jsonToArray(JSONArray array) throws JSONException {
        String[] result = null;
        if (array != null && array.length() > 0) {
            result = new String[array.length()];
            for (int i = 0; i < array.length(); i++) {
                result[i] = array.getString(i);
            }
        }
        return result;
    }

    /**
     * Exactly as per <a href=
     * "http://developer.android.com/reference/android/content/ContentResolver.html#query%28android.net.Uri,%20java.lang.String[],%20java.lang.String,%20java.lang.String[],%20java.lang.String%29"
     * >ContentResolver.query</a>
     */
    @SuppressWarnings("unused")
    @PascalMethod(description = "Content Resolver Query", returns = "result of query as Maps")
    public List<JSONObject> queryContent(
            @PascalParameter(name = "uri", description = "The URI, using the content:// scheme, for the content to retrieve.") String uri,
            @PascalParameter(name = "attributes", description = "A list of which columns to return. Passing null will return all columns") @RpcOptional JSONArray attributes,
            @PascalParameter(name = "selection", description = "A filter declaring which rows to return") @RpcOptional String selection,
            @PascalParameter(name = "selectionArgs", description = "You may include ?s in selection, which will be replaced by the values from selectionArgs") @RpcOptional JSONArray selectionArgs,
            @PascalParameter(name = "order", description = "How to order the rows") @RpcOptional String order)
            throws JSONException {
        List<JSONObject> result = new ArrayList<>();
        String[] columns = jsonToArray(attributes);
        String[] args = jsonToArray(selectionArgs);
        Cursor cursor = mContentResolver.query(Uri.parse(uri), columns, selection, args, order);
        if (cursor != null) {
            String[] names = cursor.getColumnNames();
            while (cursor.moveToNext()) {
                JSONObject message = new JSONObject();
                for (int i = 0; i < cursor.getColumnCount(); i++) {
                    String key = names[i];
                    String value = cursor.getString(i);
                    message.put(key, value);
                }
                result.add(message);
            }
            cursor.close();
        }
        return result;
    }

    @SuppressWarnings("unused")
    @PascalMethod(description = "Content Resolver Query Attributes", returns = "a list of available columns for a given content uri")
    public JSONArray queryAttributes(
            @PascalParameter(name = "uri", description = "The URI, using the content:// scheme, for the content to retrieve.") String uri)
            throws JSONException {
        JSONArray result = new JSONArray();
        Cursor cursor = mContentResolver.query(Uri.parse(uri), null, "1=0", null, null);
        if (cursor != null) {
            String[] names = cursor.getColumnNames();
            for (String name : names) {
                result.put(name);
            }
            cursor.close();
        }
        return result;
    }

    @Override
    public void shutdown() {
    }

    @Override
    public void declareConstants(ExpressionContextMixin context) {

    }

    @Override
    public void declareTypes(ExpressionContextMixin context) {

    }

    @Override
    public void declareVariables(ExpressionContextMixin context) {

    }

    @Override
    public void declareFunctions(ExpressionContextMixin context) {

    }

    @Override
    public boolean instantiate(Map<String, Object> pluginargs) {
        return false;
    }
}
