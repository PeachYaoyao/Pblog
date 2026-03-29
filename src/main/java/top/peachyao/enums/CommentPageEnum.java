package top.peachyao.enums;

/**
 * @Description: 评论页面枚举类
 * @author: PeachYao
 * @date: 2026-03-29
 */
public enum CommentPageEnum {
	UNKNOWN("UNKNOWN", "UNKNOWN"),

	BLOG("", ""),
	ABOUT("关于我", "/about"),
	FRIEND("友人帐", "/friends"),
	;

	private String title;
	private String path;

	CommentPageEnum(String title, String path) {
		this.title = title;
		this.path = path;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
}
