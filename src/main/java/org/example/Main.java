package org.example;

import java.sql.Connection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class Main {
    static int lastId = 0;
    public static void main(String[] args)  {
        List<Article> list = new ArrayList<>();
        while (true) {
            System.out.print("명령어) ");
            Scanner sc = new Scanner(System.in);
            String a = sc.nextLine();
            if(a.equals("exit")) {
                break;
            }
            if (a.equals("article write")) {
                int id = lastId + 1;
                System.out.print("제목 : ");
                String title = sc.nextLine();
                System.out.print("내용 : ");
                String body = sc.nextLine();
                Article articles = new Article(id, title, body);
                list.add(articles);
                System.out.printf("%d번 글이 생성되었습니다\n", id);
                Connection conn = null;
                PreparedStatement pstmt = null;
                try {
                    Class.forName("org.mariadb.jdbc.Driver");
                    String url = "jdbc:mariadb://127.0.0.1:3306/AM_JDBC_2024_07?useUnicode=true&characterEncoding=utf8&autoReconnect=true&serverTimezone=Asia/Seoul";
                    conn = DriverManager.getConnection(url, "root", "");
                    System.out.println("연결성공");

                    String sql = "INSERT INTO article ";
                    sql += "SET regDate = NOW(),";
                    sql += "updateDate = NOW(),";
                    sql += "title = '"+title+"',";
                    sql += "`body` = '"+body+"';";

                    System.out.println(sql);

                    pstmt = conn.prepareStatement(sql);

                    int affectedRows = pstmt.executeUpdate();

                    System.out.println("affected rows: " + affectedRows);

                } catch (ClassNotFoundException e) {
                    System.out.println("드라이버 로딩 실패" + e);
                } catch (SQLException e) {
                    System.out.println("에러 : " + e);
                } finally {
                    try {
                        if (conn != null && !conn.isClosed()) {
                            conn.close();
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    try {
                        if (pstmt != null && !pstmt.isClosed()) {
                            pstmt.close();
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
                lastId++;
                continue;
            }
            if (a.equals("article list")) {
                if (list.isEmpty()) {
                    System.out.println("내용이 없습니다");
                    continue;
                }
                System.out.println("   번호   /   제목   /   내용   ");
                for (int i = list.size() - 1; i >= 0; i--) {
                    Article article = list.get(i);
                    System.out.printf("   %d   /   %s   /   %s\n", article.getId(), article.getTitle(), article.getBody());
                }
                continue;
            }
            else{
                System.out.println("명령어를 다시 써주세요");
            }
        }
    }
}
class Article{
    int id;
    String title;
    String body;
    public Article(int id, String title, String body) {
        this.id = id;
        this.title = title;
        this.body = body;
    }

    public int getId() {
        return id;
    }
    public String getTitle() {
        return title;
    }
    public String getBody() {
        return body;
    }
}
