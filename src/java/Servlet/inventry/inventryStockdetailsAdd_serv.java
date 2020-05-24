/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servlet.inventry;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import mypojos.Fashionstock;
import mypojos.FashionstockHasUser;
import mypojos.User;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author Sandun
 */
@WebServlet(name = "inventryStockdetailsAdd_serv", urlPatterns = {"/inventryStockdetailsAdd_serv"})
public class inventryStockdetailsAdd_serv extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {

            String l = (String) request.getSession().getAttribute("user");
            if (l != null) {
                System.out.println("user kenek innawaa----------------");

                String supnic = request.getParameter("supnic");
                String itmnm = request.getParameter("itmnm");
                String suprate = request.getParameter("suprate");
                String qtyltr = request.getParameter("qtyltr");
                String ppl = request.getParameter("ppl");

                Session ses = Connection.NewHibernateUtil.getSessionFactory().openSession();

                Criteria cr = ses.createCriteria(User.class);
                cr.add(Restrictions.eq("nic", supnic));
                cr.add(Restrictions.eq("utypstat", 4));

                User u = (User) cr.uniqueResult();

                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                Date datec = new Date();
                formatter.format(datec);

                Transaction tr = ses.beginTransaction();

                if (cr.list().isEmpty()) {
                    response.getWriter().write("3");
                } else {

                    Fashionstock fs = new Fashionstock();
                        fs.setItemName(itmnm);
                        fs.setUnitprice(Double.parseDouble(ppl));
                        fs.setCurrentqty(Double.parseDouble(qtyltr));
                        fs.setStatus(1);
                        int fsid = (int) ses.save(fs);
                        Fashionstock fuelstock = (Fashionstock) ses.load(Fashionstock.class, fsid);

                        FashionstockHasUser fshu = new FashionstockHasUser();

                        fshu.setFashionstock(fuelstock);
                        fshu.setUser(u);
                        fshu.setDateintake(datec);
                        fshu.setQtyintake(Double.parseDouble(qtyltr));
                        fshu.setSuprate(Double.parseDouble(suprate));
                        fshu.setStatus(1);

                        ses.save(fshu);

                        tr.commit();
                        ses.flush();

                        response.getWriter().write("1");
                        ses.close();

                }

////
//                response.getWriter().write("1");
            } else {
                response.getWriter().write("4");
                
            }

//            System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>.");
        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().write("2");
        }

    }

}
