package com.james.depart.utils;

/**
 * 返回结果时只能返回MSG开头的值
 * @author james
 */
public interface IResult {

    /**
     * 已经是第一个
     */
    int FIRST = 1;

    /**
     * 已经是最后一个
     */
    int LAST = 0;

    /**
     * 默认成功
     */
    int MSG_SUCCESS = 0;

    /**
     * 返回修改失败
     */
    int MSG_MODIFY_ZERO = 0;

    /**
     * 返回修改成功
     */
    int MSG_MODIFY_ONE = 1;

    /**
     * 失败
     */
    int MSG_FAIL = -1;



    /**
     * 返回已经是第一个
     */
    int MSG_ALREADY_FIRST = 8001;

    /**
     * 返回已经是最后一个
     */
    int MSG_ALREADY_LAST = 8002;

    String MSG_MODIFY_SUCCESS = "修改成功";

    String MSG_MODIFY_FAIL = "修改失败";

    String MSG_FIRST = "已经是第一个";

    String MSG_LAST = "已经是最后一个";

    String MSG_DELETE_SUCCESS = "删除成功";

    String MSG_DELETE_FAIL = "删除失败";
}

