package com.bas.converter;

import androidx.annotation.NonNull;

import com.bas.core.converter.gson.LocalJsonField;

import java.util.Objects;

/**
 * Created by Lucio on 2021/7/21.
 */
class Person {

    public String name;

    @LocalJsonField(serialize = false)
    public int age;

    private String address = "北京市朝阳区28号";

    private Object Like = true;

    protected String protectname ="protectname";
     String internalName ="internalName";

    public Person() {
        this.name = "Unknown";
        this.age = 0;
    }

    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getSex(){
        return "未知";
    }
    /**
     * Returns a string representation of the object. In general, the
     * {@code toString} method returns a string that
     * "textually represents" this object. The result should
     * be a concise but informative representation that is easy for a
     * person to read.
     * It is recommended that all subclasses override this method.
     * <p>
     * The {@code toString} method for class {@code Object}
     * returns a string consisting of the name of the class of which the
     * object is an instance, the at-sign character `{@code @}', and
     * the unsigned hexadecimal representation of the hash code of the
     * object. In other words, this method returns a string equal to the
     * value of:
     * <blockquote>
     * <pre>
     * getClass().getName() + '@' + Integer.toHexString(hashCode())
     * </pre></blockquote>
     *
     * @return a string representation of the object.
     */
    @NonNull
    @Override
    public String toString() {
        return "(name:"+this.name + " age:"+this.age +" address:"+this.address + " like:"+this.Like +")";
    }
}
