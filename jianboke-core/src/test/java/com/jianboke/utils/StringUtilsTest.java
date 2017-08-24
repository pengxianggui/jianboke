package com.jianboke.utils; 

import org.junit.Test; 
import org.junit.Before; 
import org.junit.After; 

/** 
* StringUtils Tester. 
* 
* @author pengxg
* @since <pre>���� 23, 2017</pre> 
* @version 1.0 
*/ 
public class StringUtilsTest { 

@Before
public void before() throws Exception { 
} 

@After
public void after() throws Exception { 
} 

/** 
* 
* Method: randomNumberStr(int length) 
* 
*/ 
@Test
public void testRandomNumberStr() throws Exception { 
//TODO: Test goes here... 
} 

/** 
* 
* Method: split(String source, String delimiter) 
* 
*/ 
@Test
public void testSplit() throws Exception { 
//TODO: Test goes here... 
} 

/** 
* 
* Method: regexUsername(String content) 
* 
*/ 
@Test
public void testRegexUsername() throws Exception {
    String content = "@inaaaaa sb";
    String result = StringUtils.regexUsername(content);
    System.out.println(result);
    System.out.println(content);
}


} 
