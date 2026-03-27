package top.peachyao.util.markdown;

import org.commonmark.Extension;
import org.commonmark.ext.gfm.strikethrough.StrikethroughExtension;
import org.commonmark.ext.gfm.tables.TableBlock;
import org.commonmark.ext.gfm.tables.TablesExtension;
import org.commonmark.ext.heading.anchor.HeadingAnchorExtension;
import org.commonmark.ext.task.list.items.TaskListItemsExtension;
import org.commonmark.node.Image;
import org.commonmark.node.Link;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.AttributeProvider;
import org.commonmark.renderer.html.HtmlRenderer;
import top.peachyao.util.markdown.ext.cover.CoverExtension;
import top.peachyao.util.markdown.ext.heimu.HeimuExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Map;


/**
 * @Description: Markdown转换
 * @Author: PeachYao
 * @Date: 2026-03-26
 */
public class MarkdownUtils {
    // 扩展集合
    private static final List<Extension> extensions = Arrays.asList(
            TablesExtension.create(),              // 表格
            HeadingAnchorExtension.create(),       // 标题锚点（用于目录）
            TaskListItemsExtension.create(),       // 任务列表
            StrikethroughExtension.create(),        // 删除线
            CoverExtension.create(),                // 遮盖
            HeimuExtension.create()                 // 黑幕
            );

    // 解析器
    private static final Parser parser = Parser.builder()
            .extensions(extensions)
            .build();

    // 渲染器
    private static final HtmlRenderer renderer = HtmlRenderer.builder()
            .extensions(extensions)
            .attributeProviderFactory(context -> new CustomAttributeProvider())
            .build();

    // 自定义属性注入
    private static class CustomAttributeProvider implements AttributeProvider {
        @Override
        public void setAttributes(Node node, String tagName, Map<String, String> attributes) {
            // 图片懒加载
            if (node instanceof Image) {
                Image img = (Image) node;
                attributes.put("data-src", img.getDestination());
                attributes.remove("src");
            }
            // 外部链接新窗口打开
            if (node instanceof Link) {
                Link link = (Link) node;
                if (!link.getDestination().startsWith("#")) {
                    attributes.put("target", "_blank");
                    attributes.put("rel", "noopener noreferrer");
                }
            }
            // 表格样式
            if (node instanceof TableBlock) {
                attributes.put("class", "table");
            }
        }
    }

    // 基础转换（无扩展）
    public static String toHtml(String markdown) {
        Node document = Parser.builder().build().parse(markdown);
        return HtmlRenderer.builder().build().render(document);
    }

    // 完整转换（带扩展）
    public static String toHtmlWithExtensions(String markdown) {
        Node document = parser.parse(markdown);
        return renderer.render(document);
    }

}
