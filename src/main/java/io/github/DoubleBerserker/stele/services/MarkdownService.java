package io.github.DoubleBerserker.stele.services;

import org.mapstruct.Named;

public interface MarkdownService {

    @Named(value = "toHtml")
    String convertMarkdownToHtml(String markdownText);

    @Named(value = "toPlaintext")
    String convertMarkdownToPlaintext(String markdownText);

    @Named(value = "toSummarizedPlaintext")
    String convertMarkdownToSummarizedPlaintext(String markdownText);

}
