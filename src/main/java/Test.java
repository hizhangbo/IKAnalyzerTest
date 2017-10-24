package main.java;

import org.wltea.analyzer.core.IKSegmenter;
import org.wltea.analyzer.core.Lexeme;

import java.io.*;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by zhangbo on 2017/8/14.
 */
public class Test {
    public static void main(String[] args) throws IOException, SQLException {

        String sql = "select id,price,isbn,PUBLISHEDCLASSIFICATION,PUBLICATIONDATE,introduction,bookname from zq_cn.goods where INTRODUCTION not in ('暂无','略','重印','内容摘要','无','好','内容提要','1','——','W2180','/','W2203','-','.','。','11','无内容提要','_') GROUP BY BOOKNAME,ISBN,PRICE";
        int count = 126995;
        MysqlHelper db1 = new MysqlHelper(sql);
        ResultSet ret = db1.pst.executeQuery();
        while (ret.next()) {
            int bookid = ret.getInt(1);
            String price = ret.getString(2);
            String isbn = ret.getString(3);
            String press = ret.getString(4);
            String publish_date = ret.getString(5);
            String introduction = ret.getString(6);
            String name = ret.getString(7);

            StringReader sr = new StringReader(introduction);
            //true代表不是细颗粒的分词
            IKSegmenter ik = new IKSegmenter(sr, true);
            Lexeme lex = null;
            while ((lex = ik.next()) != null && !lex.getLexemeText().isEmpty() && lex.getLexemeText().length() > 1) {
                String sql2 = String.format("insert into publish_data.bookwords_ik(bookid,price,isbn,press,word,publishdate) values(%s,'%s','%s','%s','%s',%s)"
                        , bookid, price, isbn, press, lex.getLexemeText(), publish_date == null ? null : String.format("'%s'",publish_date.toString()));
                MysqlHelper db2 = new MysqlHelper(sql2);
                db2.pst.execute();
                db2.close();
            }
            System.out.println(name + '-' + isbn + '-' + press + count--);
        }
        ret.close();
        db1.close();//关闭连接
    }
}
