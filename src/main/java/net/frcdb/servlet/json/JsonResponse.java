package net.frcdb.servlet.json;

/**
 *
 * @author tim
 */
public class JsonResponse<T> {
	
	private ResponseType type;
	private String message;
	private T value;

	public JsonResponse() {
	}
	
	public JsonResponse(ResponseType type, String message, T value) {
		this.type = type;
		this.message = message;
		this.value = value;
	}

	public ResponseType getType() {
		return type;
	}

	public void setType(ResponseType type) {
		this.type = type;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public T getValue() {
		return value;
	}

	public void setValue(T value) {
		this.value = value;
	}
	
	public static enum ResponseType {
		success,
		error
	}
	
	public static <T> JsonResponse<T> success(T value, String message) {
		return new JsonResponse(ResponseType.success, message, value);
	}
	
	public static <T> JsonResponse<T> success(T value) {
		return success(value, null);
	}
	
	public static <T> JsonResponse<T> success(String message) {
		return success(null, message);
	}
	
	public static <T> JsonResponse<T> error(T value, String message) {
		return new JsonResponse(ResponseType.error, message, value);
	}
	
	public static <T> JsonResponse<T> error(T value) {
		return error(value, null);
	}
	
	public static <T> JsonResponse<T> error(String message) {
		return error(null, message);
	}
	
}
