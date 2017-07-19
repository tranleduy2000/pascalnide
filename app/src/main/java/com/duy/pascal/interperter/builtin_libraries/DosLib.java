/*
 *  Copyright 2017 Tran Le Duy
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

package com.duy.pascal.interperter.builtin_libraries;

import android.os.Build;

import com.duy.pascal.interperter.ast.expressioncontext.ExpressionContextMixin;
import com.duy.pascal.interperter.ast.runtime_value.references.PascalReference;
import com.duy.pascal.interperter.ast.runtime_value.value.RecordValue;
import com.duy.pascal.interperter.builtin_libraries.annotations.PascalMethod;
import com.duy.pascal.interperter.declaration.lang.types.BasicType;
import com.duy.pascal.interperter.declaration.lang.types.RecordType;
import com.duy.pascal.interperter.declaration.lang.value.VariableDeclaration;
import com.duy.pascal.interperter.runtime_exception.RuntimePascalException;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Map;

/**
 * Created by Duy on 01-Mar-17.
 */

@SuppressWarnings("DefaultFileTemplate")
public class DosLib implements PascalLibrary {

    public static final String NAME = "dos";

    public DosLib() {

    }

    /**
     * return system time
     */
    @PascalMethod(description = "Dos library")
    public static void getTime(PascalReference<Object> hour,
                               PascalReference<Object> minute,
                               PascalReference<Object> second,
                               PascalReference<Object> sec100) {
        Calendar calendar = Calendar.getInstance();
        hour.set(calendar.get(Calendar.HOUR));
        minute.set(calendar.get(Calendar.MINUTE));
        second.set(calendar.get(Calendar.SECOND));
        sec100.set(calendar.get(Calendar.MILLISECOND) / 10);
    }

    /**
     * return system date
     */
    @PascalMethod(description = "Dos library")
    public static void getDate(PascalReference<Integer> year,
                               PascalReference<Integer> month,
                               PascalReference<Integer> mday,
                               PascalReference<Integer> wday) {
        Calendar calendar = Calendar.getInstance();
        year.set(calendar.get(Calendar.YEAR));
        month.set(calendar.get(Calendar.MONTH) + 1);
        mday.set(calendar.get(Calendar.DAY_OF_MONTH));
        wday.set(calendar.get(Calendar.DAY_OF_WEEK));
    }

    @Override
    public boolean instantiate(Map<String, Object> pluginargs) {
        return true;
    }

    @Override
    @PascalMethod(description = "stop")

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
        //build datetime type
        ArrayList<VariableDeclaration> listVar = new ArrayList<>();
        listVar.add(new VariableDeclaration("year", BasicType.Integer));
        listVar.add(new VariableDeclaration("month", BasicType.Integer));
        listVar.add(new VariableDeclaration("day", BasicType.Integer));
        listVar.add(new VariableDeclaration("hour", BasicType.Integer));
        listVar.add(new VariableDeclaration("min", BasicType.Integer));
        listVar.add(new VariableDeclaration("sec", BasicType.Integer));
        RecordType recordType = new RecordType(listVar);
        parentContext.declareTypedef("DateTime", recordType);
    }

    @Override
    public void declareVariables(ExpressionContextMixin parentContext) {

    }

    @Override
    public void declareFunctions(ExpressionContextMixin parentContext) {

    }

    @PascalMethod(description = "Dos library")
    public int dosVersion() {
        return Build.VERSION.SDK_INT;
    }

    @PascalMethod(description = "Return the day of the week")
    public int weekday(int y, int m, int d) {
        Calendar calendar = new GregorianCalendar(y, m, d);
        return calendar.get(Calendar.DAY_OF_WEEK);
    }

    @PascalMethod(description = "Unpack packed file time to a DateTime value")
    public void unpackTime(Long time, PascalReference<RecordValue> pack) throws RuntimePascalException {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(time));
        RecordValue v = pack.get();
        v.setVar("year", calendar.get(Calendar.YEAR));
        v.setVar("month", calendar.get(Calendar.MONTH));
        v.setVar("day", calendar.get(Calendar.DAY_OF_MONTH));
        v.setVar("hour", calendar.get(Calendar.HOUR));
        v.setVar("min", calendar.get(Calendar.MINUTE));
        v.setVar("sec", calendar.get(Calendar.SECOND));
        pack.set(v);
    }

    @PascalMethod(description = "Pack DateTime value to a packed-time format.")
    public void packTime(PascalReference<RecordValue> datetime, PascalReference<Long> p) throws RuntimePascalException {
        RecordValue v = datetime.get();
        int year = (int) v.getVar("year");
        int month = (int) v.getVar("month");
        int day = (int) v.getVar("day");
        int hour = (int) v.getVar("hour");
        int minus = (int) v.getVar("min");
        int sec = (int) v.getVar("sec");
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day, hour, minus, sec);
        long time = calendar.getTime().getTime();
        p.set(time);
    }
}
