package org.wangfeng.panda.app.util;


public class StringUtils extends org.apache.commons.lang3.StringUtils {

    /**
     * 把字符串的第一个字符大写
     * @param str
     * @return
     */
    public static String toFirstCaseUpperCase(String str){

        if(str!=null && str!=""){
            str  = str.substring(0,1).toUpperCase()+str.substring(1);
        }
        return str;
    }


    /**
     * 去除字符串首尾的指定字符
     * @param args
     * @param beTrim
     * @return
     */
    public static String trim(String args,char beTrim) {
        if(args == null){
            return null;
        }
        int st = 0;
        int len = args.length();
        char[] val = args.toCharArray();
        char sbeTrim = beTrim;

        while ((st < len) && (val[st] <= sbeTrim)) {
            st++;
        }

        while ((st < len) && (val[len - 1] <= sbeTrim)) {
            len--;
        }

        return ((st > 0) || (len < args.length())) ? args.substring(st, len) : args;
    }



    /***
     * 下划线命名转为驼峰命名
     *
     * @param para
     *        下划线命名的字符串
     */

    public static String underlineToHump(String para){
        StringBuilder result=new StringBuilder();
        String a[]=para.split("_");
        for(String s:a){
            if(result.length()==0){
                result.append(s.toLowerCase());
            }else{
                result.append(s.substring(0, 1).toUpperCase());
                result.append(s.substring(1).toLowerCase());
            }
        }
        return result.toString();
    }



    /***
     * 驼峰命名转为下划线命名
     *
     * @param para
     *        驼峰命名的字符串
     */

    public static String humpToUnderline(String para){
        StringBuilder sb=new StringBuilder(para);
        int temp=0;//定位
        for(int i=0;i<para.length();i++){
            if(Character.isUpperCase(para.charAt(i))){
                sb.insert(i+temp, "_");
                temp+=1;
            }
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        System.out.println(underlineToHump("dqw_123asd_qwe123"));
        System.out.println(humpToUnderline("dqw123asdQwe123"));

    }

}
