package net.frcdb.tag;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import net.frcdb.util.UserUtil;
import org.apache.commons.lang.StringEscapeUtils;

/**
 * Source: http://vivin.net/2009/12/04/jstl-instanceof-and-hasproperty/
 * 
 * If only JSTL supported varags...
 * @author tim
 */
public class ELUtils {

	public static boolean hasProperty(Object o, String propertyName) {
		if (o == null || propertyName == null) {
			return false;
		}

		BeanInfo beanInfo;
		try {
			beanInfo = java.beans.Introspector.getBeanInfo(o.getClass());
		} catch (IntrospectionException e) {
			return false;
		}

		for (final PropertyDescriptor pd : beanInfo.getPropertyDescriptors()) {
			if (propertyName.equals(pd.getName())) {
				return true;
			}
		}
		return false;
	}
	
	public static String format(String s, Object a) {
		return String.format(s, a);
	}
	
	public static String format(String s, Object a, Object b) {
		return String.format(s, a, b);
	}
	
	public static String format(String s, Object a, Object b, Object c) {
		return String.format(s, a, b, c);
	}
	
	public static String format(String s, Object a, Object b, Object c, Object d) {
		return String.format(s, a, b, c, d);
	}
	
	public static String escape(String s) {
		return StringEscapeUtils.escapeHtml(s);
	}
	
	public static String date(Date date, String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(date);
	}
	
	public static boolean isEmpty(List list) {
		return list.isEmpty();
	}
	
	public static int size(List list) {
		return list.size();
	}
	
	public static boolean isUserLoggedIn() {
		return UserUtil.isUserLoggedIn();
	}
	
	public static boolean isUserAdmin() {
		return UserUtil.isUserAdmin();
	}
	
}
