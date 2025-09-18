package top.lrshuai.demo.word;

import cn.hutool.core.io.file.FileReader;
import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.config.Configure;
import com.deepoove.poi.data.DocumentRenderData;
import com.deepoove.poi.data.Documents;
import com.deepoove.poi.data.Paragraphs;
import com.deepoove.poi.plugin.markdown.MarkdownRenderData;
import com.deepoove.poi.plugin.markdown.MarkdownRenderPolicy;
import com.deepoove.poi.plugin.markdown.MarkdownStyle;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MarkDownWordDemo {
    public static void main(String[] args) throws IOException {
        MarkdownRenderData code = new MarkdownRenderData();
        FileReader fileReader = new FileReader("D:\\md\\demo.md");
        code.setMarkdown(fileReader.readString());
        code.setStyle(MarkdownStyle.newStyle());

        Map<String, Object> data = new HashMap<>();
        data.put("md", code);
        Configure config = Configure.builder().bind("md", new MarkdownRenderPolicy()).build();
        XWPFTemplate.compile("D:\\word\\markdown_template.docx", config)
                .render(data)
                .writeToFile(String.format("D:\\word\\out_markdown_%s.docx", System.currentTimeMillis()));
    }

    /**
     * 创建模版
     */
    public static void createTemplateWord() throws IOException {
        String text = """
                {{md}}
                """;
        DocumentRenderData templateDocData = Documents.of().addParagraph(Paragraphs.of(text).create()).create();
        XWPFTemplate template = XWPFTemplate.create(templateDocData);
        File tempFile = new File("D:\\word\\template.docx");
        template.writeAndClose(new FileOutputStream(tempFile));
    }
}
