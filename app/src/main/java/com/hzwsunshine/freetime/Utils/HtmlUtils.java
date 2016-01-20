package com.hzwsunshine.freetime.Utils;

import com.hzwsunshine.freetime.Bean.CSDNBean;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 何志伟 on 2015/12/12.
 */
public class HtmlUtils {

    public static List<CSDNBean> html2list(String htmlStr) {
        List<CSDNBean> list = new ArrayList<>();
        Document doc = Jsoup.parse(htmlStr);
        Elements units = doc.getElementsByClass("unit");
        for (int i = 0; i < units.size(); i++) {
            CSDNBean newsItem = new CSDNBean();

            Element unit_ele = units.get(i);

            Element h1_ele = unit_ele.getElementsByTag("h1").get(0);
            Element h1_a_ele = h1_ele.child(0);
            String title = ToDBC(h1_a_ele.text());
            String href = h1_a_ele.attr("href");

            newsItem.setLink(href);
            newsItem.setTitle(title);

            Element h4_ele = unit_ele.getElementsByTag("h4").get(0);
            Element ago_ele = h4_ele.getElementsByClass("ago").get(0);
            String date = ago_ele.text();

            newsItem.setDate(date);

            Element dl_ele = unit_ele.getElementsByTag("dl").get(0);
            Element dt_ele = dl_ele.child(0);
            try {
                Element img_ele = dt_ele.child(0);
                String imgLink = img_ele.child(0).attr("src");
                newsItem.setImgLink(imgLink);
            } catch (IndexOutOfBoundsException e) {
                e.printStackTrace();
            }
            Element content_ele = dl_ele.child(1);
            String content = ToDBC(content_ele.text());
            newsItem.setContent(content);
            list.add(newsItem);
        }
        return list;
    }

    public static String ToDBC(String input) {
        char[] c = input.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == '　') {
                c[i] = ' ';
            } else if ((c[i] > 65280) && (c[i] < 65375))
                c[i] = ((char) (c[i] - 65248));
        }
        return new String(c);
    }
}
