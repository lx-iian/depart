package com.james.depart.utils;

/**
 * @author james
 * @date 2021-01-05
 */
@FunctionalInterface
public interface BeanCopyUtilCallBack <S, T> {

    /**
     * 定义默认回调方法
     * @param source 源数据
     * @param target 目标数据
     */
    void callBack(S source, T target);
}
