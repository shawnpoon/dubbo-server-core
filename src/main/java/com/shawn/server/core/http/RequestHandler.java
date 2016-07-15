package com.shawn.server.core.http;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import com.shawn.server.core.util.StringUtil;

/**
 * http request 处理
 * 
 * @author Shawnpoon
 *
 */
public class RequestHandler {

	/**
	 * 取得HTTP Payload 内容
	 * 
	 * @param request
	 * @return
	 * @throws IOException
	 */
	public static String getString(HttpServletRequest request) throws IOException {
		BufferedInputStream in = null;
		try {
			request.setCharacterEncoding("utf-8");
			in = new BufferedInputStream(request.getInputStream());
			byte[] buffer = new byte[1024];
			StringBuffer sb = new StringBuffer();
			int bytesRead = 0;
			while ((bytesRead = in.read(buffer)) != -1) {
				String chunk = new String(buffer, 0, bytesRead, "utf-8");
				sb.append(chunk);
			}
			return sb.toString();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static String getIpAddr(HttpServletRequest request) throws UnknownHostException {
		String ipAddress = null;
		// ipAddress = this.getRequest().getRemoteAddr();
		ipAddress = request.getHeader("x-forwarded-for");
		if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
			ipAddress = request.getHeader("Proxy-Client-IP");
		}
		if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
			ipAddress = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
			ipAddress = request.getRemoteAddr();
			if (ipAddress.equals("127.0.0.1")) {
				// 根据网卡取本机配置的IP
				InetAddress inet = null;
				try {
					inet = InetAddress.getLocalHost();
				} catch (UnknownHostException e) {
					e.printStackTrace();
					throw new UnknownHostException("当前访问者为未知网络地址");
				}
				ipAddress = inet.getHostAddress();
			}

		}

		// 对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
		if (ipAddress != null && ipAddress.length() > 15) { // "***.***.***.***".length()
															// = 15
			if (ipAddress.indexOf(",") > 0) {
				ipAddress = ipAddress.substring(0, ipAddress.indexOf(","));
			}
		}
		return ipAddress;
	}

	/**
	 * 取得USER-AGENT
	 * 
	 * @param request
	 * @return
	 */
	public static String getUserAgent(HttpServletRequest request) {
		String userAgent = request.getHeader("USER-AGENT").toLowerCase();
		if (userAgent == null) {
			userAgent = "";
		}
		return userAgent;
	}

	static String phoneReg = "\\b(ip(hone|od)|android|opera m(ob|in)i" + "|windows (phone|ce)|blackberry"
			+ "|s(ymbian|eries60|amsung)|p(laybook|alm|rofile/midp" + "|laystation portable)|nokia|fennec|htc[-_]"
			+ "|mobile|up.browser|[1-4][0-9]{2}x[1-4][0-9]{2})\\b";
	static String tableReg = "\\b(ipad|tablet|(Nexus 7)|up.browser" + "|[1-4][0-9]{2}x[1-4][0-9]{2})\\b";

	// 移动设备正则匹配：手机端、平板
	static Pattern phonePat = Pattern.compile(phoneReg, Pattern.CASE_INSENSITIVE);
	static Pattern tablePat = Pattern.compile(tableReg, Pattern.CASE_INSENSITIVE);

	/**
	 * 检测是否是移动设备访问
	 * 
	 * @param userAgent
	 *            浏览器标识
	 * @return true:移动设备接入，false:pc端接入
	 */
	public static boolean isFromMobile(String userAgent) {
		if (null == userAgent) {
			userAgent = "";
		}
		Matcher matcherPhone = phonePat.matcher(userAgent);
		Matcher matcherTable = tablePat.matcher(userAgent);
		if (matcherPhone.find() || matcherTable.find()) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 检测是否是移动设备访问
	 * 
	 * @param userAgent
	 *            浏览器标识
	 * @return true:移动设备接入，false:pc端接入
	 */
	public static boolean isFromMobile(HttpServletRequest request) {
		String userAgent = getUserAgent(request);
		if (null == userAgent) {
			userAgent = "";
		}
		Matcher matcherPhone = phonePat.matcher(userAgent);
		Matcher matcherTable = tablePat.matcher(userAgent);
		if (matcherPhone.find() || matcherTable.find()) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * html Form 2 Object
	 * 
	 * @param request
	 * @param t
	 * @return
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public static <T extends Object> T formatForm(HttpServletRequest request, T t)
			throws IllegalArgumentException, IllegalAccessException {
		Class<? extends Object> clazz = t.getClass();
		Field[] fields = clazz.getDeclaredFields();
		for (int i = 0; i < fields.length; i++) {
			Field field = fields[i];
			field.setAccessible(true);
			String name = field.getName();
			String data = request.getParameter(name);
			if (!StringUtil.isBlank(data)) {
				setData(field, data, t);
			}
		}
		return t;
	}

	private static <T extends Object> void setData(Field field, String data, T t)
			throws IllegalArgumentException, IllegalAccessException {
		String type = field.getType().toString();
		if (type.equals("class java.lang.String")) {
			setStringData(field, data, t);
		} else if (type.equals("class java.lang.Integer")) {
			setIntegerData(field, data, t);
		} else if (type.equals("class java.lang.Long")) {
			setLongData(field, data, t);
		}
	}

	private static <T extends Object> void setStringData(Field field, String data, T t)
			throws IllegalArgumentException, IllegalAccessException {
		field.setAccessible(true);
		field.set(t, data);
	}

	private static <T extends Object> void setIntegerData(Field field, String data, T t)
			throws IllegalArgumentException, IllegalAccessException {
		Integer mData = Integer.parseInt(data);
		field.setAccessible(true);
		field.set(t, mData);
	}

	private static <T extends Object> void setLongData(Field field, String data, T t)
			throws IllegalArgumentException, IllegalAccessException {
		Long mData = Long.parseLong(data);
		field.setAccessible(true);
		field.set(t, mData);
	}
}
