package com.james.depart.utils;

import org.checkerframework.checker.regex.RegexUtil;
import org.junit.jupiter.api.Test;

/**
 * @author james
 * @description
 * @date 2021-01-04
 */
public class RegexTest {
    @Test
    public void a() {
        String a = "12z";
        RegexUtil.asRegex(a);

    }
}
