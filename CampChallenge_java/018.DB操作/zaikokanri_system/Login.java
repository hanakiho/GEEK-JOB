package zaikokanri_system;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class Login extends HttpServlet {
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {

            Connection db_con = null;
            PreparedStatement db_st=null;
            ResultSet db_data=null;

            try{
                Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
    //            文字コード指定して文字化け防ぐ
                db_con=DriverManager.getConnection("jdbc:mysql://localhost:8889/zaikokanri_system?characterEncoding=UTF-8&serverTimezone=JST","root","root");
                db_st=db_con.prepareStatement("select * from user_zyoho_kanri where userID=? and passward=?");
                request.setCharacterEncoding("UTF-8");
//                getParameterで受け取る
                String id = (String)request.getParameter("userID");
                String pw= (String)request.getParameter("passward");
                db_st.setString(1,id);
                db_st.setString(2,pw);
                db_data=db_st.executeQuery();
                String dataName = null;
                while(db_data.next()){
                  dataName = db_data.getString("userName");
                }
//              hsセッション作成
                HttpSession hs = request.getSession();
//              アクセスチェックのためにセッションとリクエストスコープにランダムな数値を格納
                int check = (int)(Math.random() * 1000);
//              一つのセッションにいくつも変数入れることが可能
//              dataName＝db_data.getString("userName");←使うための変数
                hs.setAttribute("data",dataName);
                hs.setAttribute("ac", check);
//              リクエストスコープにもセット
                request.setAttribute("check", check);
//		ページ遷移
		RequestDispatcher dispatch = request.getRequestDispatcher("/zaikokanri_system/Login2.jsp");
		dispatch.forward(request, response);

            }catch(Exception e){
                out.println("接続時にエラーが発生しました:"+e.toString());
            }finally{
                try{
                    db_con.close();
                    db_st.close();
                    db_data.close();
                }catch(Exception e){
                out.println(e.getMessage());    
                }
            }

        }
        }
          
 // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}

