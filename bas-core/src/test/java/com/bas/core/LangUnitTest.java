package com.bas.core;

import com.bas.core.lang.StringUtils;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by Lucio on 2021/11/14.
 */
public class LangUnitTest {

    @Test
    public void testCharSequenceContent(){
        CharSequence csnull = null;
        CharSequence cs1 = "Hello World";
        CharSequence cs12 = "Hello World";
        String s2 = "Hello World";
        String s21 = "Hello World";

        CharSequence cs3 = "Hello 中文World";
        String s3 = "Hello 中文World";

        StringBuilder sb = new StringBuilder("Hello World");
        Assert.assertTrue(StringUtils.areContentsSame(cs1, cs12));
        Assert.assertTrue(StringUtils.areContentsSame(s2, s21));
        Assert.assertTrue(StringUtils.areContentsSame(cs1, s2));
        Assert.assertTrue(StringUtils.areContentsSame(cs3, s3));
        Assert.assertTrue(StringUtils.areContentsSame(cs1, sb));
        Assert.assertTrue(StringUtils.areContentsSame(s2, sb));

        Assert.assertFalse(StringUtils.areContentsChanged(cs1, cs12));
        Assert.assertFalse(StringUtils.areContentsChanged(s2, s21));
        Assert.assertFalse(StringUtils.areContentsChanged(cs1, s2));
        Assert.assertFalse(StringUtils.areContentsChanged(cs3, s3));

        Assert.assertFalse(StringUtils.areContentsSame(csnull, cs1));
        Assert.assertFalse(StringUtils.areContentsSame(csnull, s2));
        Assert.assertFalse(StringUtils.areContentsChanged(csnull, null));

    }
}
