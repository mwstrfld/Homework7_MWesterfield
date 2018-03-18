/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mwesterfield;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Matthew
 */
public class Homework7 extends HttpServlet {

    private BookingDay m_bookingDay;
    private Rates.HIKE m_hike;

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Initialize the booking day
        if( m_bookingDay == null )
            m_bookingDay = new BookingDay( 0, 0, 0 );
        
        // Grab inputs and write output html
        response.setContentType("text/html;charset=UTF-8");
        try( PrintWriter out = response.getWriter() ) {
            // OK check
            Boolean ok = true;
            
            // Local string variables from page
            String hikeStr = request.getParameter( "Hike" );
            if( hikeStr == null )
                ok = false;
            String startDateStr = request.getParameter( "Date" );
            if( startDateStr == null )
                ok = false;
            String durationStr = request.getParameterValues( "Duration" )[ 0 ];
            if( durationStr == null )
                ok = false;
            
            /* TODO output your page here. You may use following sample code. */
            out.println( "<!DOCTYPE html>" );
            out.println( "<html>" );
            out.println( "<head>" );
            out.println( "<link href=\"bhc.css\" rel=\"stylesheet\" type=\"text/css\" />" );
            out.println( "<title>BHC Hike Calculator</title>" );
            out.println( "</head>" );
            out.println( "<body>" );
            out.println( "<h1>BHC Hike Calculator</h1>" );
            if( ok ) {
                ok = false;
                int duration = Integer.parseInt( durationStr );
                
                // Parse the form String results
                if( hikeStr.equals( "gl" ) ) {
                    m_hike = Rates.HIKE.GARDINER;
                    
                    // Check duration
                    if( duration == 3 || duration == 5 )
                        ok = true;
                }
                else if( hikeStr.equals( "hp" ) ) {
                    m_hike = Rates.HIKE.HELLROARING;
                    
                    // Check duration
                    if( duration > 1 && duration < 5 )
                        ok = true;
                }
                else { // hikeStr == "tbp"
                    m_hike = Rates.HIKE.BEATEN;
                    
                    // Check duration
                    if( duration == 5  || duration == 7 )
                        ok = true;
                }
                
                // OK check
                if( ok ) {
                    // Convert the date string
                    int[] dateArray = new int[ 3 ];
        
                    String dateStrArray[] = startDateStr.split( "/" );
                    try {
                        dateArray[ 0 ] = Integer.parseInt( dateStrArray[ 0 ] );
                        dateArray[ 1 ] = Integer.parseInt( dateStrArray[ 1 ] );
                        dateArray[ 2 ] = Integer.parseInt( dateStrArray[ 2 ] );
                        
                        m_bookingDay.setDayOfMonth( dateArray[ 0 ] );
                        m_bookingDay.setMonth( dateArray[ 1 ] );
                        m_bookingDay.setYear( dateArray[ 2 ] );

                        if( !m_bookingDay.isValidDate() )
                            out.println( "<h2>Please enter date in correct format.</h2>" );
                        else {
                            // Do calculation
                            Rates rates = new Rates( m_hike );
                            rates.setBeginDate( m_bookingDay );
                            rates.setDuration( duration );
                            
                            if( !rates.isValidDates() )
                                out.println( "<h2>" + rates.getDetails() + "</h2>" );
                            else
                                out.println( "<h2>The cost is $" + String.valueOf( rates.getCost() ) + "0.</h2>");
                        }
                    }
                    catch ( NumberFormatException e ) {
                        out.println( "<h2>Please enter date in correct format.</h2>" );
                    }

                }
                else {
                    // Output error
                    out.println( "<h2>Hike duration must be:</h2>");
                    out.println( "<ul>");
                    out.println( "<li>3/5 for Gardiner Lake</li>");
                    out.println( "<li>2-4 for Hellroaring Plateau</li>");
                    out.println( "<li>5/7 for The Beaten Path</li>");
                    out.println( "</ul>");
                }
            }
            else
            {
                // Output error
                out.println( "<h2>Error reading one of the form inputs.</h2>");
            }
            out.println( "</html>" );
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
