package com.shawn.server.core.http;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

public class ResponseHandler {

	public static void outputJson(HttpServletResponse response, String content) {
		try {
			response.setContentType(ContentType.JSON);
			response.getWriter().write(content);
			response.getWriter().close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void output(HttpServletResponse response, String content) {
		try {
			response.setContentType(ContentType.JSON);
			response.getWriter().write(content);
			response.getWriter().close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
