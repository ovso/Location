package kr.blogspot.ovsoce.location.common;

public class Log {
	public final static boolean DEBUG = !false;
	public static void d(String msg)
	{
		if(DEBUG && msg!=null) {
			String fullClassName = Thread.currentThread().getStackTrace()[3].getClassName();
			String className = fullClassName.substring(fullClassName.lastIndexOf(".") + 1);
			String methodName = Thread.currentThread().getStackTrace()[3].getMethodName();
			int lineNumber = Thread.currentThread().getStackTrace()[3].getLineNumber();

			android.util.Log.d("OJH",className + "." + methodName + "():" + lineNumber+" -> "+msg);
		}
	}
	public static void d(String tag, String msg) {
		d(msg);
	}
	public static void e(String msg)
	{
		if(DEBUG && msg!=null) {
			String fullClassName = Thread.currentThread().getStackTrace()[3].getClassName();
			String className = fullClassName.substring(fullClassName.lastIndexOf(".") + 1);
			String methodName = Thread.currentThread().getStackTrace()[3].getMethodName();
			int lineNumber = Thread.currentThread().getStackTrace()[3].getLineNumber();

			android.util.Log.e("OJH",className + "." + methodName + "():" + lineNumber+" -> "+msg);
		}
	}
	public static void e(String tag, String msg) {
		e(msg);
	}
	public static void w(String msg)
	{
		if(DEBUG && msg!=null) {
			String fullClassName = Thread.currentThread().getStackTrace()[3].getClassName();
			String className = fullClassName.substring(fullClassName.lastIndexOf(".") + 1);
			String methodName = Thread.currentThread().getStackTrace()[3].getMethodName();
			int lineNumber = Thread.currentThread().getStackTrace()[3].getLineNumber();

			android.util.Log.w("OJH",className + "." + methodName + "():" + lineNumber+" -> "+msg);
		}
	}
	public static void w(String tag, String msg) {
		w(msg);
	}
}
