package com.james.depart.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * @author james
 * @date 2021-01-04
 */
public class UUIDUtil {

    private static long tmpID = 0;

    private static boolean tmpIDLocked = false;

    public static long getId()
    {
        long ltime = 0;
        while (true)
        {
            if(!tmpIDLocked)
            {
                tmpIDLocked = true;
                //当前：（年、月、日、时、分、秒、毫秒）*10000
                ltime = Long.parseLong(new SimpleDateFormat("yyMMddhhmmssSSS").format(new Date())) * 10000;
                if(tmpID < ltime)
                {
                    tmpID = ltime;
                }
                else
                {
                    tmpID = tmpID + 1;
                    ltime = tmpID;
                }
                tmpIDLocked = false;
                return ltime;
            }
        }
    }

    public static long getMathUUID() {
        return Math.abs(UUID.randomUUID().getLeastSignificantBits());
    }
}
