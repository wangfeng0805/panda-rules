package org.wangfeng.panda.app.util;

import org.apache.commons.lang3.text.StrTokenizer;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.regex.Pattern;

public class IpUtils {

    public static final String _255 = "(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)";
    public static final Pattern pattern = Pattern.compile("^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$");

    public IpUtils() {
    }

    public static String longToIpV4(long longIp) {
        int octet3 = (int)((longIp >> 24) % 256L);
        int octet2 = (int)((longIp >> 16) % 256L);
        int octet1 = (int)((longIp >> 8) % 256L);
        int octet0 = (int)(longIp % 256L);
        return octet3 + "." + octet2 + "." + octet1 + "." + octet0;
    }

    public static long ipV4ToLong(String ip) {
        String[] octets = ip.split("\\.");
        return (Long.parseLong(octets[0]) << 24) + (long)(Integer.parseInt(octets[1]) << 16) + (long)(Integer.parseInt(octets[2]) << 8) + (long)Integer.parseInt(octets[3]);
    }

    public static boolean isIPv4Private(String ip) {
        long longIp = ipV4ToLong(ip);
        return longIp >= ipV4ToLong("10.0.0.0") && longIp <= ipV4ToLong("10.255.255.255") || longIp >= ipV4ToLong("172.16.0.0") && longIp <= ipV4ToLong("172.31.255.255") || longIp >= ipV4ToLong("192.168.0.0") && longIp <= ipV4ToLong("192.168.255.255");
    }

    public static boolean isIPv4Valid(String ip) {
        return pattern.matcher(ip).matches();
    }

    public static String getIpFromRequest(HttpServletRequest request) {
        boolean found = false;
        String ip;
        if ((ip = request.getHeader("x-forwarded-for")) != null) {
            StrTokenizer tokenizer = new StrTokenizer(ip, ",");

            while(tokenizer.hasNext()) {
                ip = tokenizer.nextToken().trim();
                if (isIPv4Valid(ip) && !isIPv4Private(ip)) {
                    found = true;
                    break;
                }
            }
        }

        if (!found) {
            ip = request.getRemoteAddr();
        }

        return ip;
    }

    public static String getIpAddr(HttpServletRequest request) {
        String ipAddress = null;

        try {
            ipAddress = request.getHeader("x-forwarded-for");
            if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getHeader("Proxy-Client-IP");
            }

            if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getHeader("WL-Proxy-Client-IP");
            }

            if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getRemoteAddr();
                if (("127.0.0.1").equals(ipAddress)) {
                    InetAddress inet = null;

                    try {
                        inet = InetAddress.getLocalHost();
                        ipAddress = inet.getHostAddress();
                    } catch (UnknownHostException var4) {
                        ipAddress = "";
                    }

                }
            }

            if (ipAddress != null && ipAddress.length() > 15 && ipAddress.indexOf(",") > 0) {
                ipAddress = ipAddress.substring(0, ipAddress.indexOf(","));
            }
        } catch (Exception var5) {
            ipAddress = "";
        }

        return ipAddress;
    }
}
