package top.peachyao.enums;

/**
 * @Description: 评论开放状态枚举类
 * @author: PeachYao
 * @date: 2026-03-29
 */
public enum CommentOpenStateEnum {
	/**
	 * 博客不存在，或博客未公开
	 */
	NOT_FOUND,
	/**
	 * 评论正常开放
	 */
	OPEN,
	/**
	 * 评论已关闭
	 */
	CLOSE,
	/**
	 * 评论所在页面需要密码
	 */
	PASSWORD,
}
