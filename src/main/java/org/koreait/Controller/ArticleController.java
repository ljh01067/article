package org.koreait.Controller;

import org.koreait.Article;
import org.koreait.service.ArticleService;
import org.koreait.util.DBUtil;
import org.koreait.util.SecSql;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class ArticleController {
    private String cmd;
    Connection conn;
    Scanner sc;

    private ArticleService articleService;

    public ArticleController(Connection conn, Scanner sc) {
        this.conn = conn;
        this.sc = sc;
        this.articleService = new ArticleService(conn);
    }

    public void doAction(String cmd, String actionMethodName) throws SQLException {
        this.cmd = cmd;
        switch (actionMethodName) {
            case "write":
                doWrite();
                break;
            case "list":
                showList();
                break;
            case "detail":
                showDetail();
                break;
            case "modify":
                doModify();
                break;
            case "delete":
                doDelete();
                break;
            default:
                System.out.println("명령어 확인 (actionMethodName) 오류");
                break;
        }
    }
    private void doWrite() throws SQLException {
        System.out.println("==글쓰기==");
        System.out.print("제목 : ");
        String title = sc.nextLine();
        System.out.print("내용 : ");
        String body = sc.nextLine();

        SecSql sql = new SecSql();
        sql.append("INSERT INTO article");
        sql.append("SET regDate = NOW(),");
        sql.append("updateDate = NOW(),");
        sql.append("title = ?,", title);
        sql.append("`body`= ?;", body);

        int id = DBUtil.insert(conn, sql);

        System.out.println(id + "번 글이 생성되었습니다");
    }
    private void showList() throws SQLException {
        System.out.println("==목록==");
        List<Article> articles = new ArrayList<>();
        SecSql sql = new SecSql();
        sql.append("SELECT *");
        sql.append("FROM article");
        sql.append("ORDER BY id DESC");
        List<Map<String, Object>> articleListMap = DBUtil.selectRows(conn, sql);
        for (Map<String, Object> articleMap : articleListMap) {
            articles.add(new Article(articleMap));
        }
        if (articles.size() == 0) {
            System.out.println("게시글이 없습니다");
            return;
        }
        System.out.println("  번호  /   제목  ");
        for (Article article : articles) {
            System.out.printf("  %d     /   %s   \n", article.getId(), article.getTitle());
        }
    }
    private void showDetail() throws SQLException {
        int id = 0;
        try {
            id = Integer.parseInt(cmd.split(" ")[2]);
        } catch (Exception e) {
            System.out.println("번호는 정수로 입력해");
            return;
        }
        System.out.println("==상세보기==");
        SecSql sql = new SecSql();
        sql.append("SELECT *");
        sql.append("FROM article");
        sql.append("WHERE id = ?", id);
        Map<String, Object> articleMap = DBUtil.selectRow(conn, sql);
        if (articleMap.isEmpty()) {
            System.out.println(id + "번 글은 없어");
            return;
        }
        Article article = new Article(articleMap);
        System.out.println("번호 : " + article.getId());
        System.out.println("작성날짜 : " + article.getRegDate());
        System.out.println("수정날짜 : " + article.getUpdateDate());
        System.out.println("제목 : " + article.getTitle());
        System.out.println("내용 : " + article.getBody());
    }
    private void doModify() throws SQLException {
        int id = 0;
        try {
            id = Integer.parseInt(cmd.split(" ")[2]);
        } catch (Exception e) {
            System.out.println("번호는 정수로 입력해");
            return;
        }
        SecSql sql = new SecSql();
        sql.append("SELECT *");
        sql.append("FROM article");
        sql.append("WHERE id = ?", id);
        Map<String, Object> articleMap = DBUtil.selectRow(conn, sql);
        if (articleMap.isEmpty()) {
            System.out.println(id + "번 글은 없어");
            return;
        }
        System.out.println("==수정==");
        System.out.print("새 제목 : ");
        String title = sc.nextLine().trim();
        System.out.print("새 내용 : ");
        String body = sc.nextLine().trim();
        sql = new SecSql();
        sql.append("UPDATE article");
        sql.append("SET updateDate = NOW()");
        if (title.length() > 0) {
            sql.append(",title = ?", title);
        }
        if (body.length() > 0) {
            sql.append(",`body` = ?", body);
        }
        sql.append("WHERE id = ?", id);
        DBUtil.update(conn, sql);
        System.out.println(id + "번 글이 수정되었습니다.");
    }
    private void doDelete() throws SQLException {
        int id = 0;
        try {
            id = Integer.parseInt(cmd.split(" ")[2]);
        } catch (Exception e) {
            System.out.println("번호는 정수로 입력해");
            return;
        }
        SecSql sql = new SecSql();
        sql.append("SELECT *");
        sql.append("FROM article");
        sql.append("WHERE id = ?", id);
        Map<String, Object> articleMap = DBUtil.selectRow(conn, sql);
        if (articleMap.isEmpty()) {
            System.out.println(id + "번 글은 없어");
            return;
        }
        System.out.println("==삭제==");
        sql = new SecSql();
        sql.append("DELETE FROM article");
        sql.append("WHERE id = ?", id);
        DBUtil.delete(conn, sql);
        System.out.println(id + "번 글이 삭제되었습니다.");
    }
}
