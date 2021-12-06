package com.bas.core.android

import com.bas.core.security.MD5Utils
import org.junit.Assert
import org.junit.Test

/**
 * Created by Lucio on 2021/11/10.
 */
class MD5UnitTest {

    @Test
    fun testMd5(){
        val expected = "b10a8db164e0754105b7a99be72e3fe5"
        val message = "Hello World"
        val md5l = MD5Utils.MD5(message)
        val md5l2 = MD5Utils.MD5Lowercase(message)
        Assert.assertEquals(expected,md5l)
        Assert.assertEquals(expected,md5l2)
        Assert.assertEquals(md5l,md5l2)
        val md5u = MD5Utils.MD5Uppercase(message)
        Assert.assertEquals(expected.toUpperCase(),md5u)
        Assert.assertEquals(md5l.toUpperCase(),md5u)
        val md5Sl = MD5Utils.MD5Lowercase(message,"22")
        val md5Sl2 = MD5Utils.MD5Uppercase(message,"22")
        Assert.assertEquals(md5Sl.toUpperCase(),md5Sl2)
    }
}