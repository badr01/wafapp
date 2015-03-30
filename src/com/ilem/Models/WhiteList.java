package com.ilem.Models;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by laassiri on 23/03/15.
 */
public class WhiteList {
    public static final Map<Integer,String> WL_PROPS;
    static
    {
        WL_PROPS = new HashMap<Integer, String>();
        WL_PROPS.put(1000,"sql keywords");
        WL_PROPS.put(1001,"double quote");
        WL_PROPS.put(1002,"0x, possible hex encoding");
        WL_PROPS.put(1003,"mysql comment (/*)");
        WL_PROPS.put(1004,"mysql comment (*/)");
        WL_PROPS.put(1005,"mysql keyword (|)");
        WL_PROPS.put(1006,"mysql keyword (&&)");
        WL_PROPS.put(1007,"mysql comment (--)");
        WL_PROPS.put(1008,"semicolon");
        WL_PROPS.put(1009,"equal in var, probable sql/xss");
        WL_PROPS.put(1010,"parenthesis, probable sql/xss");
        WL_PROPS.put(1011,"parenthesis, probable sql/xss");
        WL_PROPS.put(1013,"simple quote");
        WL_PROPS.put(1015,"comma");
        WL_PROPS.put(1016,"mysql comment (#)");
        WL_PROPS.put(1100,"http:// in var");
        WL_PROPS.put(1101,"https:// in var");
        WL_PROPS.put(1102,"ftp:// in var");
        WL_PROPS.put(1103,"php:// in var");
        WL_PROPS.put(1104,"sftp:// in var");
        WL_PROPS.put(1105,"zlib:// in var");
        WL_PROPS.put(1106,"data:// in var");
        WL_PROPS.put(1107,"glob:// in var");
        WL_PROPS.put(1108,"phar:// in var");
        WL_PROPS.put(1109,"file:// in var");
        WL_PROPS.put(1200,"double dot");
        WL_PROPS.put(1202,"obvious probe");
        WL_PROPS.put(1203,"obvious windows path");
        WL_PROPS.put(1204,"obvious probe");
        WL_PROPS.put(1205,"backslash");
        WL_PROPS.put(1302,"html open tag");
        WL_PROPS.put(1303,"html close tag");
        WL_PROPS.put(1310,"[, possible js");
        WL_PROPS.put(1311,"], possible js");
        WL_PROPS.put(1312,"~ character");
        WL_PROPS.put(1314,"grave accent !");
        WL_PROPS.put(1315,"double encoding !");
        WL_PROPS.put(1400,"utf7/8 encoding");
        WL_PROPS.put(1401,"M$ encoding");
        WL_PROPS.put(1402,"Content is neither mulipart/x-www-form..");
        WL_PROPS.put(1500,"asp/php file upload!");
    }

}
