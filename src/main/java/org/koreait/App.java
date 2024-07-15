package org.koreait;

import org.koreait.Controller.ArticleController;
import org.koreait.Controller.MemberController;
import java.sql.*;
import java.util.Scanner;

public class App {
    static int rogi;
    public void run() {
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.print("명령어 > ");
            String cmd = sc.nextLine().trim();

            Connection conn = null;

            try {
                Class.forName("org.mariadb.jdbc.Driver");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

            String url = "jdbc:mariadb://127.0.0.1:3306/AM_JDBC_2024_07?useUnicode=true&characterEncoding=utf8&autoReconnect=true&serverTimezone=Asia/Seoul";

            try {
                conn = DriverManager.getConnection(url, "root", "");

                int actionResult = action(conn, sc, cmd);

                if (actionResult == -1) {
                    System.out.println("==프로그램 종료==");
                    sc.close();
                    break;
                }
                if (actionResult == 0){
                    continue;
                }

            } catch (SQLException e) {
                System.out.println("에러 1 : " + e);
            } finally {
                try {
                    if (conn != null && !conn.isClosed()) {
                        conn.close();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private int action(Connection conn, Scanner sc, String cmd) throws SQLException {

        if (cmd.equals("exit")) {
            return -1;
        }
        MemberController memberController = new MemberController(sc, conn);
        ArticleController articleController = new ArticleController(conn, sc);
        String[] cmdBits = cmd.split(" ");
        if (cmdBits.length == 1) {
            System.out.println("명령어 확인해");
            return 0;
        }
        String action = cmd.split(" ")[0];
        String actionMethodName = cmd.split(" ")[1];
        if (action.equals("member")) {
            memberController.doAction(cmd,actionMethodName);
        } else if (action.equals("article")) {
            articleController.doAction(cmd,actionMethodName);
        } else if(action.equals("member")||action.equals("article") == false){
            System.out.println("명령어 오류");
        }
        return 0;
    }
    public static void setlogi(){
        rogi++;
    }
    public static int getlogi(){
        return rogi;
    }
}
