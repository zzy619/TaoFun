
package com.zzy.taofun.ref;

import java.lang.reflect.Field;
import java.util.WeakHashMap;

import android.util.Log;

import com.taobao.api.TaobaoResponse;

public class Introspector {

    public static final Class STOP_CLASS = TaobaoResponse.class;
    private static WeakHashMap<Class, BeanInfo> CACHE = new WeakHashMap<Class, BeanInfo>();

    private Introspector() {

    }

    public static BeanInfo getBeanInfo(Class clazz) {
        return getBeanInfo(clazz, STOP_CLASS);
    }

    public static BeanInfo getBeanInfo(Class clazz, Class stopClass) {
        BeanInfo beanInfo = null;
        beanInfo = CACHE.get(clazz);
        if (beanInfo != null) {
            return beanInfo;
        } else {
            beanInfo = new BeanInfo(clazz);
            Class parentClass = clazz;
            if (stopClass != null) {
                while (stopClass.isAssignableFrom(parentClass)) {
                    parentClass = traversalFields(beanInfo, parentClass);
                }
            } else {
                while (parentClass != null) {
                    parentClass = traversalFields(beanInfo, parentClass);
                }
            }
            CACHE.put(clazz, beanInfo);
        }

        return beanInfo;
    }

    private static Class traversalFields(BeanInfo beanInfo, Class parentClass) {
        Field[] fields = parentClass.getFields();
        for (Field field : fields) {
            Log.e("ql", field.getName());
            beanInfo.createPropertyDescriptorByField(field);
        }
        parentClass = parentClass.getSuperclass();
        return parentClass;
    }

}
