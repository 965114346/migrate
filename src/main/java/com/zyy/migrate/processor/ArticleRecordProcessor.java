package com.zyy.migrate.processor;

import com.alibaba.fastjson.JSONObject;
import com.vdurmont.emoji.EmojiParser;
import com.zyy.migrate.model.Article;
import lombok.extern.slf4j.Slf4j;
import org.easybatch.core.processor.RecordProcessor;
import org.easybatch.core.record.Record;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;

/**
 * @author yangyang.zhang
 * @date 2019年11月04日22:48:34
 */
@Slf4j
@Component
public class ArticleRecordProcessor implements RecordProcessor {

    @Override
    public Record processRecord(Record record) throws Exception {
        Article article = (Article) record.getPayload();
        article.setSortId(30);
        article.setLanguageType("zh");
        article.setThumbnail("");
        article.setPassed(1);
        article.setReadType(4);

        //String content = EmojiParser.removeAllEmojis(article.getContent());
        //article.setContent(content);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("pic", article.getArticleExtend());
        article.setArticleExtend(jsonObject.toJSONString());

        //log.info("Article: {}", article.getId());
        return record;
    }
}
