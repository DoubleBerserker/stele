package io.github.DoubleBerserker.stele.services.impl;

import io.github.DoubleBerserker.stele.services.MarkdownService;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.commonmark.renderer.text.TextContentRenderer;
import org.mapstruct.Named;
import org.owasp.html.PolicyFactory;
import org.owasp.html.Sanitizers;
import org.springframework.stereotype.Service;

@Service
public class MarkdownServiceImpl implements MarkdownService {

    private static final Integer MAX_POST_SUMMARY_LENGTH = 200;

    private final Parser markdownParser = Parser.builder().build();
    private final TextContentRenderer textContentRenderer = TextContentRenderer.builder().build();
    private final HtmlRenderer renderer = HtmlRenderer.builder().build();

    private final PolicyFactory HTML_SANITIZER = Sanitizers.FORMATTING
            .and(Sanitizers.BLOCKS)
            .and(Sanitizers.IMAGES)
            .and(Sanitizers.LINKS)
            .and(Sanitizers.STYLES);

    @Override
    @Named(value = "toHtml")
    public String convertMarkdownToHtml(String markdownText) {

        if (markdownText == null)
            return "";

        Node node = markdownParser.parse(markdownText);
        return HTML_SANITIZER.sanitize(renderer.render(node));
    }

    @Override
    @Named(value = "toPlaintext")
    public String convertMarkdownToPlaintext(String markdownText) {

        if (markdownText == null) {
            return "";
        }

        Node node = markdownParser.parse(markdownText);
        return textContentRenderer.render(node);
    }

    @Override
    @Named(value = "toSummarizedPlaintext")
    public String convertMarkdownToSummarizedPlaintext(String markdownText) {

        if (markdownText == null) {
            return "";
        }

        Node node = markdownParser.parse(markdownText);
        String plaintext = textContentRenderer.render(node);

        if (plaintext.length() <= MAX_POST_SUMMARY_LENGTH) {
            return plaintext;
        }

        return plaintext.substring(0, MAX_POST_SUMMARY_LENGTH) + "...";
    }

}
