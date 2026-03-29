package top.peachyao.util.comment.channel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import top.peachyao.config.properties.BlogProperties;
import top.peachyao.enums.CommentPageEnum;
import top.peachyao.model.dto.CommentDto;
import top.peachyao.util.MailUtils;
import top.peachyao.util.comment.CommentUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description: 邮件提醒方式
 * @author: PeachYao
 * @date: 2026-03-29
 */
@Lazy
@Component
public class MailChannel implements CommentNotifyChannel {
	@Autowired
	private BlogProperties blogProperties;
	@Autowired
	private MailProperties mailProperties;
	@Autowired
	private MailUtils mailUtils;

	/**
	 * 发送邮件提醒我自己
	 *
	 * @param comment 当前收到的评论
	 */
	@Override
	public void notifyMyself(CommentDto comment) {
		CommentPageEnum commentPageEnum = CommentUtils.getCommentPageEnum(comment);
		Map<String, Object> map = new HashMap<>(16);
		map.put("title", commentPageEnum.getTitle());
		map.put("time", comment.getCreateTime());
		map.put("nickname", comment.getNickname());
		map.put("content", comment.getContent());
		map.put("ip", comment.getIp());
		map.put("email", comment.getEmail());
		map.put("status", comment.getPublished() ? "公开" : "待审核");
		map.put("url", blogProperties.getView() + commentPageEnum.getPath());
		map.put("manageUrl", blogProperties.getCms() + "/comments");
		String toAccount = mailProperties.getUsername();
		String subject = blogProperties.getName() + " 收到新评论";
		mailUtils.sendHtmlTemplateMail(map, toAccount, subject, "owner.html");
	}
}
