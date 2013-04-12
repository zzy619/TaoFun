
package com.zzy.taofun.ref;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class BeanInfo {

    private Class mClassName;
    private List<PropertyDescriptor> mPropertyDescriptor;

    public BeanInfo(Class className) {
        this.mClassName = className;
    }

    public PropertyDescriptor[] getPropertyDescriptors() {
        return (PropertyDescriptor[]) mPropertyDescriptor.toArray();
    }

    public void setPropertyDescriptor(PropertyDescriptor pro) {
        if (mPropertyDescriptor == null) {
            mPropertyDescriptor = new ArrayList<PropertyDescriptor>();
        }
        mPropertyDescriptor.add(pro);
    }

    public void createPropertyDescriptorByField(Field filed) {
        PropertyDescriptor pro = new PropertyDescriptor(mClassName, filed);
        setPropertyDescriptor(pro);
    }

}
