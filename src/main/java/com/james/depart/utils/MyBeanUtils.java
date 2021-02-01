package com.james.depart.utils;

import com.james.depart.domain.Department;
import com.james.depart.domain.VO.DepartmentVO;
import org.springframework.beans.BeanUtils;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

/**
 * @author james
 * @description
 * @date 2021-01-05
 */
public class MyBeanUtils extends BeanUtils {

    public static void copyBean(Object source, Object tag) throws Exception {

        // 获取属性
        BeanInfo sourceBean = Introspector.getBeanInfo(source.getClass(), java.lang.Object.class);
        PropertyDescriptor[] sourceProperty = sourceBean.getPropertyDescriptors();

        BeanInfo destBean = Introspector.getBeanInfo(tag.getClass(), java.lang.Object.class);
        PropertyDescriptor[] destProperty = destBean.getPropertyDescriptors();

        try {
            for (PropertyDescriptor descriptor : sourceProperty) {
                for (PropertyDescriptor propertyDescriptor : destProperty) {
                    if (descriptor.getName().equals(propertyDescriptor.getName())) {
                        // 调用source的getter方法和dest的setter方法
                        propertyDescriptor.getWriteMethod().invoke(tag, descriptor.getReadMethod().invoke(source));
                        break;
                    }
                }
            }
        } catch (Exception e) {
            throw new Exception("属性复制失败:" + e.getMessage());
        }
    }

    public static void copyBean1(Optional<Department> department, DepartmentVO departmentVO) {
        String s = department.toString();
        Long id = department.get().getId();
        departmentVO.setId(id);

    }

    /**
     * 对象数据的拷贝
     *
     * @param source   数据源
     * @param target   目标类::new(eg: UserVO::new)
     * @param callBack 回调
     * @param <S>      源数据类型
     * @param <T>      目标数据类型
     * @return 目标数据
     */
    public static <S, T> T copyProperties(S source, Supplier<T> target, BeanCopyUtilCallBack<S, T> callBack) {
        T t = target.get();
        copyProperties(source, t);
        if (callBack != null) {
            // 回调
            callBack.callBack(source, t);
        }
        return t;
    }

    /**
     * 对象数据的拷贝
     *
     * @param source 数据源
     * @param target 目标类::new(eg: UserVO::new)
     * @param <S>    源数据类型
     * @param <T>    目标数据类型
     * @return 目标数据
     */
    public static <S, T> T copyProperties(S source, Supplier<T> target) {
        return copyProperties(source, target, (BeanCopyUtilCallBack<S, T>) null);
    }

    /**
     * 集合数据的拷贝
     *
     * @param sources: 数据源
     * @param target:  目标类::new(eg: UserVO::new)
     * @param <S>      源数据类型
     * @param <T>      目标数据类型
     * @return List<T>
     */
    public static <S, T> List<T> copyListProperties(List<S> sources, Supplier<T> target) {
        return copyListProperties(sources, target, null);
    }


    /**
     * 带回调函数的集合数据的拷贝（可自定义字段拷贝规则）
     *
     * @param sources:  数据源
     * @param target:   目标类::new(eg: UserVO::new)
     * @param callBack: 回调函数
     * @param <S>       源数据类型
     * @param <T>       目标数据类型
     * @return List<T>
     */
    public static <
            S, T> List<T> copyListProperties(List<S> sources, Supplier<T> target, BeanCopyUtilCallBack<S, T> callBack) {
        List<T> list = new ArrayList<>(sources.size());
        for (S source : sources) {
            T t = target.get();
            copyProperties(source, t);
            if (callBack != null) {
                // 回调
                callBack.callBack(source, t);
            }
            list.add(t);
        }
        return list;
    }

}

