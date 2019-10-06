package com.example.demo.config;

import org.commonmark.Extension;
import org.commonmark.ext.gfm.tables.TablesExtension;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.springframework.web.servlet.view.AbstractTemplateView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class MarkdownView extends AbstractTemplateView {

    private Parser parser = null;
    private HtmlRenderer renderer = null;

    public MarkdownView() {
        List<Extension> extensions = Arrays.asList(TablesExtension.create());
        parser = Parser.builder().extensions(extensions).build();
        renderer = HtmlRenderer.builder().extensions(extensions).build();
    }

    @Override
    protected void renderMergedTemplateModel(Map<String, Object> model,
                                             HttpServletRequest request,
                                             HttpServletResponse response) throws Exception {

        PrintWriter writer = response.getWriter();
        writer.append("<html><body>");
        writer.append(getHtmlFromMarkdown());
        writer.append("</body></html>");
    }

    private String getHtmlFromMarkdown() throws URISyntaxException, IOException {
        String fileName = getUrl();
        URL mdUrl = MarkdownView.class.getClassLoader().getResource("templates/markdown/" + fileName);
        String mdContent = new String(Files.readAllBytes(Paths.get(mdUrl.toURI())));
        Node document = parser.parse(mdContent);
        String html = renderer.render(document);
        return html;
    }
}