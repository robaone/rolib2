package com.robaone;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FieldValidator {
	
	public static boolean isEmail(String str) {
		boolean retval = false;
		if (str != null) {
			String[] t = str.split(" ");
			if (t.length == 1) {
				t = str.split("[@]");
				if (t.length == 2) {
					t = t[1].split("[.]");
					if (t.length > 1) {
						retval = true;
					}
				}
			}
		}
		return retval;
	}

	public static boolean isZipCode(String string) {
		boolean retval = false;
		if (string != null) {
			String[] t = string.split("[-]");
			if (t.length <= 2) {
				String fivedigit = t[0];
				if (t.length > 1) {
					String fourdigit = t[1];
					if (fivedigit.trim().length() == 5 && fourdigit.trim().length() == 4) {
						try {
							Integer.parseInt(fivedigit);
							Integer.parseInt(fourdigit);
							retval = true;
						} catch (Exception e) {
						}
					}
				} else {
					if (fivedigit.trim().length() == 5) {
						try {
							Integer.parseInt(fivedigit);
							retval = true;
						} catch (Exception e) {
						}
					}
				}
			}
		}
		return retval;
	}

	public static boolean exists(String username) {
		if (username == null || username.trim().length() == 0)
			return false;
		else
			return true;
	}

	public static boolean isNumber(String limit) {
		if (limit == null) {
			return false;
		} else {
			limit = limit.trim();
			Pattern p = Pattern.compile("^[0-9]+([.][0-9]+)?$");
			Matcher m = p.matcher(limit);
			return m.matches();
		}
	}

	public static java.util.Date parseDate(String str) throws Exception {
		SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss.S");
		SimpleDateFormat df2 = new SimpleDateFormat("MM/dd/yyyy");
		try {
			Date d = df.parse(str);
			return d;
		} catch (Exception e) {
			try {
				Date d = df2.parse(str);
				return d;
			} catch (Exception e2) {
				throw e2;
			}
		}
	}

	public static boolean isDate(String workdate) {
		SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss.S");
		SimpleDateFormat df2 = new SimpleDateFormat("MM/dd/yyyy");
		SimpleDateFormat df3 = new SimpleDateFormat("yyyy-MM-dd");
		try {
			df.parse(workdate);
			return true;
		} catch (Exception e) {
			try {
				df2.parse(workdate);
				return true;
			} catch (Exception e2) {
				try {
					df3.parse(workdate);
					return true;
				} catch (Exception e3) {
				}
			}
		}
		return false;
	}

	public static java.util.Date getDate(String date) throws Exception {
		SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss.S");
		SimpleDateFormat df2 = new SimpleDateFormat("MM/dd/yyyy");
		try {
			Date d = df.parse(date);
			return d;
		} catch (Exception e) {
			try {
				Date d = df2.parse(date);
				return d;
			} catch (Exception e2) {
				throw e2;
			}
		}
	}

	public static boolean matches(String str, String pattern) {
		try {
			Pattern p = Pattern.compile(pattern);
			Matcher m = p.matcher(str);
			return m.matches();
		} catch (Exception e) {
			return false;
		}
	}

	public static boolean isUrl(String filter) {
		try {
			String regex = "^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";
			Pattern p = Pattern.compile(regex);
			Matcher m = p.matcher(filter);
			return m.matches();
		} catch (Exception e) {
			return false;
		}
	}
}
