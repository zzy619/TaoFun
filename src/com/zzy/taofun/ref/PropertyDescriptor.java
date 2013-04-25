
package com.zzy.taofun.ref;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import android.util.Log;

public class PropertyDescriptor {

    private Class mClassName;
    private Field mField;
    private static final String WRITE_METHOD_HEAD = "set";
    private static final String READ_METHOD_HEAD = "get";

    public PropertyDescriptor(Class className, Field field) {
        this.mClassName = className;
        this.mField = field;
    }

    public Method getWriteMethod() {
        Method method = null;
        try {
            method = mClassName.getMethod(getMethodNameWithType(WRITE_METHOD_HEAD), new Class[] {
                    mField.getType()
            });
        } catch (NoSuchMethodException e) {
            return null;
        }

        return method;
    }

    public String getFiledName() {
        return mField.getName();
    }

    private String getMethodNameWithType(String methodType) {
        String str = methodType;
        String filedName = mField.getName();
        str += filedName.substring(0, 1).toUpperCase() + filedName.substring(1);
        Log.e("ql", str);
        return str;
    }
}
