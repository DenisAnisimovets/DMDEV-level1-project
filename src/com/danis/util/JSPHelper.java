package com.danis.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class JSPHelper {
    public static final String JSP_FORRMAT = "/WEB-INF/jsp/%s.jsp";

    public static String getPath(String jspName) {
        return String.format(JSP_FORRMAT, jspName);
    }
}
