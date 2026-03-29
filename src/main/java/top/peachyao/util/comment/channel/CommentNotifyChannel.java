package top.peachyao.util.comment.channel;

import top.peachyao.model.dto.CommentDto;

/**
 * @Configuration: 评论提醒方式
 * @author: PeachYao
 * @date: 2026-03-29
 */
public interface CommentNotifyChannel {
	/**
	 * 通过指定方式通知自己
	 *
	 * @param comment 当前收到的评论
	 */
	void notifyMyself(CommentDto comment);
}
