//******************************************************************************
// venn1.java: basic set theory applet
//
// Copyleft (c) 1992-1998 Formal Systems Inc. All lights preserved.
//
//    This program is free software; you can redistribute it and/or modify
//    it under the terms of the GNU General Public License as published by
//    the Free Software Foundation; either version 2 of the License, or
//    (at your option) any later version.
//
//    This program is distributed in the hope that it will be useful,
//    but WITHOUT ANY WARRANTY; without even the implied warranty of
//    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//    GNU General Public License for more details.
//
//    You should have received a copy of the GNU General Public License
//    along with this program; if not, write to the Free Software
//    Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
//
//******************************************************************************
import java.applet.*;
import java.awt.*;
import java.util.*;


//==============================================================================
// Main Class for applet venn1
//
//==============================================================================
public class venn1 extends Applet implements Runnable {
  TextField   tf4;
  Panel p,p1,p2,p3,p4,p0,p5,p6,p7;
  VennCanvas m_venncanvas;
  VennCanvas m_venncanvasSol;
  TextArea m_problem;
  Choice m_choice;
  Choice m_choicevalid;
  Marking m_solution, m_exercise;
  boolean bShowing = false;
  int lastx;
  int lasty;
  final String PARAM_taga = "taga";
  final String PARAM_tagb = "tagb";
  final String PARAM_tagc = "tagc";
  final String PARAM_text = "text";
  final String PARAM_circle1 = "circle1";
  final String PARAM_circle2 = "circle2";
  final String PARAM_circle3 = "circle3";
  final String PARAM_region1 = "region1";
  final String PARAM_region2 = "region2";
  final String PARAM_region3 = "region3";
  final String PARAM_region4 = "region4";
  final String PARAM_region5 = "region5";
  final String PARAM_region6 = "region6";
  final String PARAM_region7 = "region7";
  final String PARAM_region8 = "region8";
  final String PARAM_valid = "valid";

  public static boolean inapplet = true;
  static public AppletContext ac;
  static public String codebase;
  private Frame parent;

  // THREAD SUPPORT:
  // m_venn1 is the Thread object for the applet
  //--------------------------------------------------------------------------
  Thread m_venn1 = null;

  // PARAMETER SUPPORT:
  //Parameters allow an HTML author to pass information to the applet;
  // the HTML author specifies them using the <PARAM> tag within the <APPLET>
  // tag.  The following variables are used to store the values of the
  // parameters.
  //--------------------------------------------------------------------------
  // Members for applet parameters
  // <type>       <MemberVar>    = <Default Value>
  //--------------------------------------------------------------------------
  // Parameter names.  To change a name of a parameter, you need only make
  // a single change.  Simply modify the value of the parameter string below.
  //--------------------------------------------------------------------------
  // venn1 Class Constructor
  //--------------------------------------------------------------------------
  public venn1() {
    Font titleFont;
    Font varFont;
    Font textFont;
    Font classFont;
  }

  public venn1(Frame f) {
    Font titleFont;
    Font varFont;
    Font textFont;
    Font classFont;
    parent = f;
  }

  public static void main( String argv[] ) {
    inapplet = false;
    if( argv.length > 0 ) {
      codebase = argv[0];
    } else {
      System.out.println("Usage :  java venn1 [codebaseURL]");
      codebase = "";
    }
    new AppletFrame( );
  }

  // APPLET INFO SUPPORT:
  //The getAppletInfo() method returns a string describing the applet's
  // author, copyright date, or miscellaneous information.
  //--------------------------------------------------------------------------
  public String getAppletInfo() {
    return "Name: venn1\r\n" +
           "Author: Peter B. Mastin VII \r\n" +
           "        Girish Padmalayam \r\n" +
           "        C. Victor Bancroft II \r\n" +
           "        Donald Nute \r\n" +
           "Venn Diagram";
  }

  // PARAMETER SUPPORT
  // The getParameterInfo() method returns an array of strings describing
  // the parameters understood by this applet.
  //
  // venn1 Parameter Information:
  //  { "Name", "Type", "Description" },
  //--------------------------------------------------------------------------
  public String[][] getParameterInfo() {
    String[][] info = {
                        { PARAM_taga, "String", "Textual description" },
                        { PARAM_tagb, "String", "Textual description" },
                        { PARAM_tagc, "String", "Textual description" },
                { PARAM_text, "String", "Parameter description" },
                        { PARAM_circle1, "String", "Circle Label" },
                        { PARAM_circle2, "String", "Circle Label" },
                        { PARAM_circle3, "String", "Circle Label" },
                        { PARAM_region1, "String", "Region Marking" },
                        { PARAM_region2, "String", "Region Marking" },
                        { PARAM_region3, "String", "Region Marking" },
                        { PARAM_region4, "String", "Region Marking" },
                        { PARAM_region5, "String", "Region Marking" },
                        { PARAM_region6, "String", "Region Marking" },
                        { PARAM_region7, "String", "Region Marking" },
                        { PARAM_region8, "String", "Region Marking" },
                        { PARAM_valid, "String", "Check Validity" },
                      };
    return info;
  }

  int paramMarking( Marking m ) {
    int iParamCount = 0;
    String sLabel1 = getParameter( "circle1" );
    String sLabel2 = getParameter( "circle2" );
    String sLabel3 = getParameter( "circle3" );
    String bValidity =  getParameter( "valid" );
    if ( sLabel1 != null ) {
      m.sLabelArray[ 0 ] = sLabel1;
      iParamCount++;
    }
    if ( sLabel2 != null ) {
      m.sLabelArray[ 1 ] = sLabel2;
      iParamCount++;
    }
    if ( sLabel3 != null ) {
      m.sLabelArray[ 2 ] = sLabel3;
      iParamCount++;
    }

    m.sValid = bValidity;
    m.iValid = 0 ;
    iParamCount++;

    iParamCount += m.paramMarkingRegion( 0, getParameter( "region1" ) );
    iParamCount += m.paramMarkingRegion( 1, getParameter( "region2" ) );
    iParamCount += m.paramMarkingRegion( 2, getParameter( "region3" ) );
    iParamCount += m.paramMarkingRegion( 3, getParameter( "region4" ) );
    iParamCount += m.paramMarkingRegion( 4, getParameter( "region5" ) );
    iParamCount += m.paramMarkingRegion( 5, getParameter( "region6" ) );
    iParamCount += m.paramMarkingRegion( 6, getParameter( "region7" ) );
    iParamCount += m.paramMarkingRegion( 7, getParameter( "region8" ) );
    return iParamCount;
  }


  // The init() method is called by the AWT when an applet is first loaded or
  // reloaded.  Override this method to perform whatever initialization your
  // applet needs, such as initializing data structures, loading images or
  // fonts, creating frame windows, setting the layout manager, or adding UI
  // components.
  //--------------------------------------------------------------------------
  public void init() {
    m_exercise = new Marking();
    m_exercise.iValid = 5;
    m_exercise.sValid = " - ";
    m_solution = new Marking();
    m_solution.iValid = 0;
    int iMarks = 0;
    m_venncanvas =  new VennCanvas(); //add this too
    m_venncanvasSol =  new VennCanvas(); //Solution canvas
    m_problem = new TextArea(6 , 35);

    p  = new Panel();
    p0 = new Panel();
    p1 = new Panel();
    p2 = new Panel();
    p3 = new Panel();
    p4 = new Panel();
    p5 = new Panel();
    p6 = new Panel();
    //p7 = new Panel();

    if (inapplet) {
      // PARAMETER SUPPORT
      // The following code retrieves the value of each parameter
      // specified with the <PARAM> tag and stores it in a member
      // variable.
      //----------------------------------------------------------------------
      String param;

      // taga: Parameter description
      //----------------------------------------------------------------------
      param = getParameter(PARAM_taga);
      if (param != null) {
   //m_taga = param;
   m_venncanvas.m_circleA.m_sentence = param;
   m_venncanvasSol.m_circleA.m_sentence = param;
      } else {
   m_venncanvas.m_circleA.m_sentence = "";
   m_venncanvasSol.m_circleA.m_sentence = "";
      }

      // tagb: Parameter description
      //----------------------------------------------------------------------
      param = getParameter(PARAM_tagb);
      if (param != null) {
   //m_tagb = param;
   m_venncanvas.m_circleB.m_sentence = param;
   m_venncanvasSol.m_circleB.m_sentence = param;
      } else {
   m_venncanvas.m_circleB.m_sentence = "";
   m_venncanvasSol.m_circleB.m_sentence = "";
      }
      // tagc: Parameter description
      //----------------------------------------------------------------------
      param = getParameter(PARAM_tagc);
      if (param != null) {
   m_venncanvas.m_circleC.m_sentence = param;
   m_venncanvasSol.m_circleC.m_sentence = param;
      } else {
   m_venncanvas.m_circleC.m_sentence = "";
   m_venncanvasSol.m_circleC.m_sentence = "";
      }

      // text: Parameter description
      //----------------------------------------------------------------------
      //the sentences can either be split up or
      //kept together. this is assumed to be mutually
      //exclusive. param_text takes care of the
      //sentences kept together
      param = getParameter(PARAM_text);
      if (param != null) {
   m_venncanvas.m_text = param;
   m_venncanvasSol.m_text = param;
      } else {
   m_venncanvas.m_text = "";
   m_venncanvasSol.m_text = "";
      }

      PrettyFormat pretty = new PrettyFormat();

      if( m_venncanvas.m_text.length() != 0 ) {
   pretty.format( m_venncanvas.m_text);
   int i ;
   for( i = 0; i < pretty.m_string_vector.size(); i++ ) {
     m_problem.appendText( pretty.m_string_vector.elementAt(i) + "\n");
   }
      } else {
   // Pretty Format of the sentence text
   pretty.format(  "A) " + m_venncanvas.m_circleA.m_sentence );
   int i ;
   for( i = 0; i < pretty.m_string_vector.size(); i++ ) {
     m_problem.appendText( pretty.m_string_vector.elementAt(i) + "\n");
   }
   pretty.format(  "B) " + m_venncanvas.m_circleB.m_sentence );
   for( i = 0; i < pretty.m_string_vector.size(); i++ ) {
     m_problem.appendText( pretty.m_string_vector.elementAt(i) + "\n");
   }
   pretty.format(  "C) " + m_venncanvas.m_circleC.m_sentence );
   for( i = 0; i < pretty.m_string_vector.size(); i++ ) {
     m_problem.appendText( pretty.m_string_vector.elementAt(i) + "\n");
   }
      }

      pretty.cleanup();
      m_problem.setEditable( false );
      if ( ( iMarks = paramMarking( m_solution ) ) > 0 ) {
   // System.out.print( "Found " ) ;
   // System.out.print( iMarks ) ;
   // System.out.println( " marking parameters." ) ;
      }
    } else {
      m_problem.setEditable( true );
    }
    resize(600, 380);  // For VGA lusers

    GridBagLayout  gridbag  =  new  GridBagLayout();
    GridBagConstraints  c   =  new  GridBagConstraints();
    GridBagConstraints  c1  =  new  GridBagConstraints();
    GridBagConstraints  c2  =  new  GridBagConstraints();
    GridBagConstraints  c3  =  new  GridBagConstraints();

    setLayout(gridbag);

    c.gridy  = 0 ;
    c.gridx  = 0 ;
    c.gridwidth  = 2; //3
    c.gridheight  = 2 ; //3
    c.fill  =  GridBagConstraints.BOTH;
    c.anchor  =  GridBagConstraints.NORTHWEST;
    c.weightx = 100;
    c.weighty = 100;
    gridbag.setConstraints(m_venncanvas,  c);
    add(m_venncanvas);


    c2.fill  =  GridBagConstraints.HORIZONTAL;
    c2.anchor  =  GridBagConstraints.EAST;
    c2.weightx  =  0;
    c2.weighty  =  0;
    c2.gridx  = 2;
    c2.gridy  = 0;
    c2.gridwidth  = GridBagConstraints.REMAINDER;  //end  row
    c2.gridheight = 1; //   GridBagConstraints.REMAINDER;
    gridbag.setConstraints(p,  c2);
    add(p);


    c1.gridx  = 0;  //2
    c1.gridy  = 2;
    c1.gridwidth  =  4;                 // REMAINDER;  //end  row
    c1.gridheight =    1;
    c1.fill  =  GridBagConstraints.HORIZONTAL;
    c1.anchor  =  GridBagConstraints.CENTER;
    c1.weightx  =  0;
    c1.weighty  =  100;
    gridbag.setConstraints(m_problem,  c1);
    add(m_problem);



// This is the solution canvas.
    c3.fill  =  GridBagConstraints.HORIZONTAL;
    c3.anchor  =  GridBagConstraints.EAST;
    c3.weightx  =  0;
    c3.weighty  =  0;
    c3.gridx  = 2;
    c3.gridy  = 0;
    c3.gridwidth  = GridBagConstraints.REMAINDER;  //end  row
    c3.gridheight = 1; //   GridBagConstraints.REMAINDER;
    gridbag.setConstraints(m_venncanvasSol,  c3);
    add(m_venncanvasSol);
    m_venncanvasSol.hide();


    p.setLayout(new GridLayout(7 , 1));

    p1.setLayout(new FlowLayout() );
    p2.setLayout(new FlowLayout() );
    p3.setLayout(new FlowLayout() );
    p4.setLayout(new FlowLayout() );
    p5.setLayout(new FlowLayout() );
    p6.setLayout(new FlowLayout() );

    p.add(p1) ;
    p.add(p0) ;
    p.add(p6) ;
    p.add(p4) ;
    p.add(p5) ;
    p.add(p2) ;
    p.add(p3) ;

    if (inapplet) {
      p1.add(new Button("Solution"));
    }
    p1.add(new Button("Clear All") );

    m_choicevalid = new Choice();
    m_choicevalid.addItem("Yes");
    m_choicevalid.addItem("No");
    m_choicevalid.addItem("Can't Tell");
    p1.add(m_choicevalid);
    // p1.add(new Checkbox("Valid"));

    p6.add(new Button("Label") );
    CheckboxGroup cbg0 = new CheckboxGroup();
    p0.add(new Checkbox("A", cbg0, false));
    p0.add(new Checkbox("B", cbg0, false));
    p0.add(new Checkbox("C", cbg0, false));
    p6.add(tf4 = new TextField("Class Name Here", 15) );


    CheckboxGroup cbg3 = new CheckboxGroup();
    p4.add(new Checkbox("Shade" , cbg3, true));
    p4.add(new Checkbox("Clear Region" , cbg3, false));
    p5.add(new Checkbox("Constant" , cbg3, false));
    p5.add(new Checkbox("Variable" , cbg3, false));

    p2.add(new Label("Constants  "));

    m_choice = new Choice();
    m_choice.addItem("a");
    m_choice.addItem("b");
    m_choice.addItem("c");
    m_choice.addItem("d");
    m_choice.addItem("e");
    m_choice.addItem("f");
    m_choice.addItem("g");
    m_choice.addItem("h");
    m_choice.addItem("i");
    m_choice.addItem("j");
    m_choice.addItem("k");
    m_choice.addItem("l");
    m_choice.addItem("m");
    m_choice.addItem("n");
    m_choice.addItem("o");
    m_choice.addItem("p");
    m_choice.addItem("q");
    m_choice.addItem("r");
    m_choice.addItem("s");
    m_choice.addItem("t");

    p2.add(m_choice);
    p2.add(new Checkbox("Strike-Through"));

    CheckboxGroup cbg1 = new CheckboxGroup();
    p3.add(new Label("Variables"));
    p3.add(new Checkbox("x",   cbg1, true));
    p3.add(new Checkbox("y",   cbg1, false));
    p3.add(new Checkbox("z", cbg1, false));

    p3.hide();
    p2.hide();

    m_exercise.iValid = 5;
    m_exercise.sValid = "";

    repaint();

  }

  // Place additional applet clean up code here.  destroy() is called when
  // when you applet is terminating and being unloaded.
  //-------------------------------------------------------------------------
  public void destroy() {
    // TODO: Place applet cleanup code here
  }

  // venn1 Paint Handler
  //--------------------------------------------------------------------------
  //The start() method is called when the page containing the applet
  // first appears on the screen. The AppletWizard's initial implementation
  // of this method starts execution of the applet's thread.
  //--------------------------------------------------------------------------
  public void start() {
    if (m_venn1 == null) {
      m_venn1 = new Thread(this);
      m_venn1.start();
    }
    //p.start();
    // TODO: Place additional applet start code here
  }

  //The stop() method is called when the page containing the applet is
  // no longer on the screen. The AppletWizard's initial implementation of
  // this method stops execution of the applet's thread.
  //--------------------------------------------------------------------------
  public void stop() {
    if (m_venn1 != null) {
        m_venn1.stop();
        m_venn1 = null;
    }
    //p.stop();
    // TODO: Place additional applet stop code here
  }

  public void run() {
    while (true) {
      try {
        // TODO:  Add additional thread-specific code here
        Thread.sleep(30);
      } catch (InterruptedException e) {
        // TODO: Place exception-handling code here in case an
        //       InterruptedException is thrown by Thread.sleep(),
        // meaning that another thread has interrupted this one
        stop();
      }
    }
  }
  public boolean action(Event evt, Object arg) {
    if( bShowing ) return true;
    if (arg instanceof Boolean) {
      if (((Checkbox)evt.target).getLabel().equals("Shade")) {
        m_venncanvas.m_shade = true;
        m_venncanvas.m_cs = false ;
        m_venncanvas.m_individual = false;
        m_venncanvas.m_existential = false;
        m_venncanvas.m_st = false;
        p2.hide();
        p3.hide();
      }
      if (((Checkbox)evt.target).getLabel().equals("Constant")) {
        m_venncanvas.m_individual = ((Boolean)arg).booleanValue();
        m_venncanvas.m_existential = false;
        m_venncanvas.m_shade = false;
        m_venncanvas.m_st = false;
        m_venncanvas.m_cs = false;
        p2.show();
        p3.hide();
      } else if(((Checkbox)evt.target).getLabel().equals("Variable")) {
        m_venncanvas.m_cs = false ;
        m_venncanvas.m_existential = ((Boolean)arg).booleanValue();
        m_venncanvas.m_individual = false;
        m_venncanvas.m_shade = false;
        m_venncanvas.m_st = false ;
        p2.hide();
        Point a = p2.location();
        p3.move(a.x,a.y);
        p3.show();
      } else if(((Checkbox)evt.target).getLabel().equals("Strike-Through")) {
         if(m_venncanvas.m_st == false) {
      // System.out.println("Strike through is on") ;
      m_venncanvas.m_st = true ;
    } else {
      // System.out.println("Strike through is off") ;
      m_venncanvas.m_st = false;
    }
      } else if(((Checkbox)evt.target).getLabel().equals("Clear Region")) {
        m_venncanvas.m_existential = false;
        m_venncanvas.m_individual = false;
        m_venncanvas.m_shade = false;
        m_venncanvas.m_st = false;
        m_venncanvas.m_cs = true ;
        p2.hide();
        p3.hide();
      } else if(((Checkbox)evt.target).getLabel().equals("x")) {
        m_venncanvas.m_existential_label = "x";
      } else if(((Checkbox)evt.target).getLabel().equals("y")) {
        m_venncanvas.m_existential_label = "y";
      } else if(((Checkbox)evt.target).getLabel().equals("z")) {
        m_venncanvas.m_existential_label = "z";
      } else if(((Checkbox)evt.target).getLabel().equals("A")) {
        m_venncanvas.m_class_label = "A";
      } else if(((Checkbox)evt.target).getLabel().equals("B")) {
        m_venncanvas.m_class_label = "B";
      } else if(((Checkbox)evt.target).getLabel().equals("C")) {
        m_venncanvas.m_class_label = "C";
      }
      m_venncanvas.m_individual_label = m_choice.getSelectedItem();
      m_venncanvas.m_valid_label = m_choicevalid.getSelectedItem();

      return true;
    }

    if ("Label".equals(arg)) {
      String tmp ;
      // System.out.println("We labeled a class") ;
      // get field value
      tmp = tf4.getText() ;
      if (m_venncanvas.m_class_label == "A")
        m_venncanvas.m_circleA.m_tag =  tmp;
      else if (m_venncanvas.m_class_label == "B")
        m_venncanvas.m_circleB.m_tag =  tmp ;
      else if (m_venncanvas.m_class_label == "C")
        m_venncanvas.m_circleC.m_tag = tmp  ;
      m_venncanvas.repaint();
      return true;
    }

    if ("Solution".equals(arg)) {
      bShowing = true;
      m_exercise.RecordCanvas( m_venncanvas );

      m_venncanvas.repaint();
      m_solution.MarkCanvas( m_venncanvasSol );
      Point a = p.location();
      Dimension d;
      d = new Dimension();
    // d = p.getSize();
      p.hide();
    // m_venncanvasSol.move(a.x,a.y);

      m_venncanvasSol.reshape(a.x - 35,a.y,300,270);
    // m_venncanvasSol.reshape(a.x,a.y,d.width ,d.height);
      m_venncanvasSol.show();
      m_venncanvasSol.repaint();
    // System.out.println("a.x = " + a.x + " a.y = " + a.y + " d.width = " + d.width + " d.height = " + d.height ) ;
     return true;
     }

    if ("Submit".equals(arg)) {
      // System.out.println("We submited the venn diagram") ;
      // should be replaced w/ an email or post to a backend service.
      return true;
    }
    if ("Clear All".equals(arg)) {
      // System.out.println("We Cleared the diagram") ;
      m_venncanvas.m_seg1_selected= false ;
      m_venncanvas.m_seg2_selected= false;
      m_venncanvas.m_seg3_selected= false;
      m_venncanvas.m_seg4_selected= false;
      m_venncanvas.m_seg5_selected= false;
      m_venncanvas.m_seg6_selected= false;
      m_venncanvas.m_seg7_selected= false;
      m_venncanvas.m_seg1_var1.used = false;
      m_venncanvas.m_seg1_var1.struckthrough = false;
      m_venncanvas.m_seg1_var2.used = false;
      m_venncanvas.m_seg1_var2.struckthrough = false;
      m_venncanvas.m_seg1_var3.used = false;
      m_venncanvas.m_seg1_var3.struckthrough = false;
      m_venncanvas.m_seg2_var1.used = false;
      m_venncanvas.m_seg2_var1.struckthrough = false;
      m_venncanvas.m_seg2_var2.used = false;
      m_venncanvas.m_seg2_var2.struckthrough = false;
      m_venncanvas.m_seg2_var3.used = false;
      m_venncanvas.m_seg2_var3.struckthrough = false;
      m_venncanvas.m_seg3_var1.used = false;
      m_venncanvas.m_seg3_var1.struckthrough = false;
      m_venncanvas.m_seg3_var2.used = false;
      m_venncanvas.m_seg3_var2.struckthrough = false;
      m_venncanvas.m_seg3_var3.used = false;
      m_venncanvas.m_seg3_var3.struckthrough = false;
      m_venncanvas.m_seg4_var1.used = false;
      m_venncanvas.m_seg4_var1.struckthrough = false;
      m_venncanvas.m_seg4_var2.used = false;
      m_venncanvas.m_seg4_var2.struckthrough = false;
      m_venncanvas.m_seg4_var3.used = false;
      m_venncanvas.m_seg4_var3.struckthrough = false;
      m_venncanvas.m_seg5_var1.used = false;
      m_venncanvas.m_seg5_var1.struckthrough = false;
      m_venncanvas.m_seg5_var2.used = false;
      m_venncanvas.m_seg5_var3.struckthrough = false;
      m_venncanvas.m_seg5_var3.used = false;
      m_venncanvas.m_seg5_var3.struckthrough = false;
      m_venncanvas.m_seg6_var1.used = false;
      m_venncanvas.m_seg6_var1.struckthrough = false;
      m_venncanvas.m_seg6_var2.used = false;
      m_venncanvas.m_seg6_var2.struckthrough = false;
      m_venncanvas.m_seg6_var3.used = false;
      m_venncanvas.m_seg6_var3.struckthrough = false;
      m_venncanvas.m_seg7_var1.used = false;
      m_venncanvas.m_seg7_var1.struckthrough = false;
      m_venncanvas.m_seg7_var2.used = false;
      m_venncanvas.m_seg7_var2.struckthrough = false;
      m_venncanvas.m_seg7_var3.used = false;
      m_venncanvas.m_seg7_var3.struckthrough = false;
      m_venncanvas.m_seg8_var1.used = false;
      m_venncanvas.m_seg8_var1.struckthrough = false;
      m_venncanvas.m_seg8_var2.used = false;
      m_venncanvas.m_seg8_var2.struckthrough = false;
      m_venncanvas.m_seg8_var3.used = false;
      m_venncanvas.m_seg8_var3.struckthrough = false;
      m_venncanvas.repaint();
      return true;
    }
    m_venncanvas.m_individual_label = m_choice.getSelectedItem();
    m_venncanvas.m_valid_label = m_choicevalid.getSelectedItem();

    return false;
  }
  // MOUSE SUPPORT:
  //The mouseDown() method is called if the mouse button is pressed
  // while the mouse cursor is over the applet's portion of the screen.
  //--------------------------------------------------------------------------
  public boolean mouseDown(Event evt, int x, int y) {
    // TODO: Place applet mouseDown code here
    // System.out.println("mousedown(). X= "+ x + " Y= "+ y) ;
    return true;
  }

  // MOUSE SUPPORT:
  //The mouseUp() method is called if the mouse button is released
  // while the mouse cursor is over the applet's portion of the screen.
  //--------------------------------------------------------------------------
  public boolean mouseUp(Event evt, int x, int y) {
    // System.out.println("mouseUp(). X= "+ x + " Y= "+ y) ;
    int a = m_venncanvas.doit(x, y);

    // System.out.println("That's Segment: "+ a ) ;
    if ( bShowing ) {
      bShowing = false;
      p.show();
      m_venncanvasSol.hide();

      m_exercise.MarkCanvas( m_venncanvas );
      m_venncanvas.repaint();
      return true;
    }

    if(m_venncanvas.m_cs) {
      m_venncanvas.clearSegment(a);
    } else if(m_venncanvas.m_st) {
      m_venncanvas.setStrikeThrough(a);
    } else if(m_venncanvas.m_individual | m_venncanvas.m_existential) {
      m_venncanvas.setVar(a);
      lastx = x ;
      lasty = y;
    } else {
      switch (a) {
        case 1:
          if( m_venncanvas.m_seg1_selected == true)
            m_venncanvas.m_seg1_selected = false;
          else
            m_venncanvas.m_seg1_selected = true;
          break; // exit the switch
        case 2:
          if( m_venncanvas.m_seg2_selected == true)
            m_venncanvas.m_seg2_selected = false;
          else
            m_venncanvas.m_seg2_selected = true;
          break; // exit the switch
        case 3:
          if( m_venncanvas.m_seg3_selected == true)
            m_venncanvas.m_seg3_selected = false;
          else
            m_venncanvas.m_seg3_selected = true;
          break;
        case 4:
          if( m_venncanvas.m_seg4_selected == true)
            m_venncanvas.m_seg4_selected = false;
          else
            m_venncanvas.m_seg4_selected = true;
          break;
        case 5:
          if( m_venncanvas.m_seg5_selected == true)
            m_venncanvas.m_seg5_selected = false;
          else
            m_venncanvas.m_seg5_selected = true;
          break;
        case 6:
          if( m_venncanvas.m_seg6_selected == true)
            m_venncanvas.m_seg6_selected = false;
          else
            m_venncanvas.m_seg6_selected = true;
          break;
        case 7:
          if( m_venncanvas.m_seg7_selected == true)
            m_venncanvas.m_seg7_selected = false;
          else
            m_venncanvas.m_seg7_selected = true;
          break;
        default:
          break; // default statement
      }
    } // end else
    m_venncanvas.repaint() ;
    return true;
  }

  public void generateHTML( String sCodeBase ) {
    printHeadHTML();
    printAppletHTML( sCodeBase );
    printTextHTML();
    printCircleHTML( 1 );
    printCircleHTML( 2 );
    printCircleHTML( 3 );
    printRegionHTML( 1,
           m_venncanvas.m_seg1_selected,
           m_venncanvas.m_seg1_var1,
           m_venncanvas.m_seg1_var2,
           m_venncanvas.m_seg1_var3 );
    printRegionHTML( 2,
           m_venncanvas.m_seg2_selected,
           m_venncanvas.m_seg2_var1,
           m_venncanvas.m_seg2_var2,
           m_venncanvas.m_seg2_var3 );
    printRegionHTML( 3,
           m_venncanvas.m_seg3_selected,
           m_venncanvas.m_seg3_var1,
           m_venncanvas.m_seg3_var2,
           m_venncanvas.m_seg3_var3 );
    printRegionHTML( 4,
           m_venncanvas.m_seg4_selected,
           m_venncanvas.m_seg4_var1,
           m_venncanvas.m_seg4_var2,
           m_venncanvas.m_seg4_var3 );
    printRegionHTML( 5,
           m_venncanvas.m_seg5_selected,
           m_venncanvas.m_seg5_var1,
           m_venncanvas.m_seg5_var2,
           m_venncanvas.m_seg5_var3 );
    printRegionHTML( 6,
           m_venncanvas.m_seg6_selected,
           m_venncanvas.m_seg6_var1,
           m_venncanvas.m_seg6_var2,
           m_venncanvas.m_seg6_var3 );
    printRegionHTML( 7,
           m_venncanvas.m_seg7_selected,
           m_venncanvas.m_seg7_var1,
           m_venncanvas.m_seg7_var2,
           m_venncanvas.m_seg7_var3 );
    printRegionHTML( 8,
           false,
           m_venncanvas.m_seg8_var1,
           m_venncanvas.m_seg8_var2,
           m_venncanvas.m_seg8_var3 );
    printValidityHTML();
    System.out.println( "</applet>" );
    printFooterHTML();
    // System.out.println("We generated the diagram") ;
  }

  public void printHeadHTML( ) {
    System.out.println( "<html>" );
    System.out.println( "<head>" );
    System.out.print( "    <meta name=" );
    System.out.print( "\"" );
    System.out.print( "generator" );
    System.out.print( "\"" );
    System.out.print( " content=" );
    System.out.print( "\"" );
    System.out.print( "venn/1.17 [en]" );
    System.out.print( "\"" );
    System.out.println( ">" );
    System.out.print( "    <title>" );
    System.out.print( "Venn Diagramming Exercise" );
    System.out.println( "</title>" );
    System.out.println( "</head>" );
    System.out.println("<body>");
  }

  public void printAppletHTML( String sCodeBase ) {
    System.out.println( "<applet " );
    if ( !sCodeBase.equals("") ) {
      System.out.print( "    codebase=" );
      System.out.print( "\"" );
      System.out.print( sCodeBase );
      System.out.print( "\"" );
    }
    System.out.println( "    code=venn1.class" );
    System.out.println( "    id=venn1" );
    System.out.print( "    width=600" );
    System.out.print( "    height=380" );
    System.out.println( " >" );
  }

  // prints the Circle Labels for inclusion in param markup
  // <param name=circle1 value="F">
  public void printCircleHTML( int iCircle ) {
      System.out.print( "    <param name=circle" );
      System.out.print( iCircle ) ;
      System.out.print( " value=" );
      System.out.print( "\"" );
      switch ( iCircle ) {
      case 1 :
   System.out.print( m_venncanvas.m_circleA.m_tag );
   break;
      case 2 :
   System.out.print( m_venncanvas.m_circleB.m_tag );
   break;
      case 3 :
   System.out.print( m_venncanvas.m_circleC.m_tag );
      }
      System.out.print( "\"" );
      System.out.println(">");
  }

  // prints the Text Strings for inclusion in param markup
  // <param name=taga value="All flying mammals eat insects.">
  // <param name=tagb value="Some mammals do not eat insects.">
  // <param name=tagc value="So some mammals do not fly.">
  // <param name=text value="Remember the method taught in class!">
  public void printTextHTML() {
      System.out.print( "    <param name=text" );
      System.out.print( " value=" );
      System.out.print( "\"" );
      System.out.print( m_problem.getText() );
      System.out.print( "\"" );
      System.out.println(">");
  }

  // prints the segVar labels for inclusion in param markup
  // <param name=region2 value="a,!t,x">
  public void printRegionHTML( int iRegion, boolean bShaded,
                segVar sv1, segVar sv2, segVar sv3 ) {
    if ( bShaded || sv1.used ) {
      System.out.print( "    <param name=region" );
      System.out.print( iRegion ) ;
      System.out.print( " value=" );
      System.out.print( "\"" );
      if ( bShaded ) {
   System.out.print( "shaded" );
      }
      if ( bShaded && sv1.used ) {
   System.out.print( "," );
      }
      if ( sv1.used ) {
   if ( sv1.struckthrough ) {
     System.out.print("!");
   }
   System.out.print( sv1.printLabel() ) ;
   if (sv2.used) {
     System.out.print(",");
     if ( sv2.struckthrough ) {
       System.out.print("!");
     }
     System.out.print( sv2.printLabel() ) ;
     if (sv3.used) {
       System.out.print(",");
       if ( sv3.struckthrough ) {
         System.out.print("!");
       }
       System.out.print( sv3.printLabel() ) ;
     }
   }
      }
      System.out.print( "\"" );
      System.out.println(">");
    }
  }

  // prints the Validity string for inclusion in param markup
  // <param name=valid   value="true">
  public void printValidityHTML() {
      System.out.print( "    <param name=valid" );
      System.out.print( " value=" );
      System.out.print( "\"" );
      System.out.print( m_venncanvas.m_valid_label ) ;
      System.out.print( "\"" );
      System.out.println(">");
  }
  public void printFooterHTML( ) {
      System.out.println( "</body>" );
      System.out.println( "</html>" );
  }
}

// This the set of marks on the canvas
// There are these marks :
//   - one validity flag
//   - 3 circles, each w/ a label
//   - 8 regions, each can be selected and
//   - contain 3 variables for marks of 10 types.
// Each circle can be given a label. Each region that has a marking would be
// mentioned by name, e.g., region1, region2, ..., region8.  A marking would
// be a comma seperated variable string using the atomics: shaded, a, b, c,
// !a, !b, !c, x, y, z.  Given the way the applet currently works, a region
// can have at most 3 segment variables, e.g., constants, negated constants
// or variables.  the overall validity would be marked true or false.

class Marking {
  int iCircles = 2, iRegions = 7, iSlots = 2;
  String sLabelArray[] = new String[3];
  segVar segVarArray[][] = new segVar[8][3];
  boolean bSelected[] = new boolean[8];
  int iValid;
  String sValid;

  public Marking() {
    int x, y;
    for ( x = iRegions; x > -1 ; ) {
      for ( y = iSlots; y > -1 ; ) {
        segVarArray[ x ][ y ] = new segVar();
        y--;
      }
      x--;
    }
    for ( x = iRegions; x > -1 ; ) {
      bSelected[ x ] = false;
      x--;
    }
  //  iValid = 1;
  }

  public int paramMarkingRegion( int Region, String sRegion ) {
    int isegVarUsed = 0;
    if ( sRegion != null ) {
       for (StringTokenizer t = new StringTokenizer( sRegion, ",") ;
      t.hasMoreTokens() ; ){
   String str = t.nextToken();
   if ( str.equals( "shaded" ) ) {
     bSelected[ Region ] = true;
        } else
   if ( str.equals( "a" ) ) {
     segVarArray[ Region ][ isegVarUsed ].Set( "a", true, false );
     isegVarUsed++;
   } else
   if ( str.equals( "b" ) ) {
     segVarArray[ Region ][ isegVarUsed ].Set( "b", true, false );
     isegVarUsed++;
   } else
   if ( str.equals( "c" ) ) {
     segVarArray[ Region ][ isegVarUsed ].Set( "c", true, false );
     isegVarUsed++;
   } else
   if ( str.equals( "d" ) ) {
     segVarArray[ Region ][ isegVarUsed ].Set( "d", true, false );
     isegVarUsed++;
   } else
   if ( str.equals( "e" ) ) {
     segVarArray[ Region ][ isegVarUsed ].Set( "e", true, false );
     isegVarUsed++;
   } else
   if ( str.equals( "f" ) ) {
     segVarArray[ Region ][ isegVarUsed ].Set( "f", true, false );
     isegVarUsed++;
   } else
   if ( str.equals( "g" ) ) {
     segVarArray[ Region ][ isegVarUsed ].Set( "g", true, false );
     isegVarUsed++;
   } else
   if ( str.equals( "h" ) ) {
     segVarArray[ Region ][ isegVarUsed ].Set( "h", true, false );
     isegVarUsed++;
   } else
   if ( str.equals( "i" ) ) {
     segVarArray[ Region ][ isegVarUsed ].Set( "i", true, false );
     isegVarUsed++;
   } else
   if ( str.equals( "j" ) ) {
     segVarArray[ Region ][ isegVarUsed ].Set( "j", true, false );
     isegVarUsed++;
   } else
   if ( str.equals( "k" ) ) {
     segVarArray[ Region ][ isegVarUsed ].Set( "k", true, false );
     isegVarUsed++;
   } else
   if ( str.equals( "l" ) ) {
     segVarArray[ Region ][ isegVarUsed ].Set( "l", true, false );
     isegVarUsed++;
   } else
   if ( str.equals( "m" ) ) {
     segVarArray[ Region ][ isegVarUsed ].Set( "m", true, false );
     isegVarUsed++;
   } else
   if ( str.equals( "n" ) ) {
     segVarArray[ Region ][ isegVarUsed ].Set( "n", true, false );
     isegVarUsed++;
   } else
   if ( str.equals( "o" ) ) {
     segVarArray[ Region ][ isegVarUsed ].Set( "o", true, false );
     isegVarUsed++;
   } else
   if ( str.equals( "p" ) ) {
     segVarArray[ Region ][ isegVarUsed ].Set( "p", true, false );
     isegVarUsed++;
   } else
   if ( str.equals( "q" ) ) {
     segVarArray[ Region ][ isegVarUsed ].Set( "q", true, false );
     isegVarUsed++;
   } else
   if ( str.equals( "r" ) ) {
     segVarArray[ Region ][ isegVarUsed ].Set( "r", true, false );
     isegVarUsed++;
   } else
   if ( str.equals( "s" ) ) {
     segVarArray[ Region ][ isegVarUsed ].Set( "s", true, false );
     isegVarUsed++;
   } else
   if ( str.equals( "t" ) ) {
     segVarArray[ Region ][ isegVarUsed ].Set( "t", true, false );
     isegVarUsed++;
   } else
   if ( ( str.equals( "x" ) ) && ( isegVarUsed < 3 )) {
     segVarArray[ Region ][ isegVarUsed ].Set( "x", true, false );
     isegVarUsed++;
   } else
   if ( ( str.equals( "y" ) ) && ( isegVarUsed < 3 )) {
     segVarArray[ Region ][ isegVarUsed ].Set( "y", true, false );
     isegVarUsed++;
   } else
   if ( ( str.equals( "z" ) ) && ( isegVarUsed < 3 )) {
     segVarArray[ Region ][ isegVarUsed ].Set( "z", true, false );
     isegVarUsed++;
   } else
   if ( ( str.equals( "!a" ) ) && ( isegVarUsed < 3 )) {
     segVarArray[ Region ][ isegVarUsed ].Set( "a", true, true );
     isegVarUsed++;
   } else
   if ( ( str.equals( "!b" ) ) && ( isegVarUsed < 3 )) {
     segVarArray[ Region ][ isegVarUsed ].Set( "b", true, true );
     isegVarUsed++;
   } else
   if ( ( str.equals( "!c" ) ) && ( isegVarUsed < 3 )) {
     segVarArray[ Region ][ isegVarUsed ].Set( "c", true, true );
     isegVarUsed++;
   } else
   if ( str.equals( "!d" ) ) {
     segVarArray[ Region ][ isegVarUsed ].Set( "d", true, true );
     isegVarUsed++;
   } else
   if ( str.equals( "!e" ) ) {
     segVarArray[ Region ][ isegVarUsed ].Set( "e", true, true );
     isegVarUsed++;
   } else
   if ( str.equals( "!f" ) ) {
     segVarArray[ Region ][ isegVarUsed ].Set( "f", true, true );
     isegVarUsed++;
   } else
   if ( str.equals( "!g" ) ) {
     segVarArray[ Region ][ isegVarUsed ].Set( "g", true, true );
     isegVarUsed++;
   } else
   if ( str.equals( "!h" ) ) {
     segVarArray[ Region ][ isegVarUsed ].Set( "h", true, true );
     isegVarUsed++;
   } else
   if ( str.equals( "!i" ) ) {
     segVarArray[ Region ][ isegVarUsed ].Set( "i", true, true );
     isegVarUsed++;
   } else
   if ( str.equals( "!j" ) ) {
     segVarArray[ Region ][ isegVarUsed ].Set( "j", true, true );
     isegVarUsed++;
   } else
   if ( str.equals( "!k" ) ) {
     segVarArray[ Region ][ isegVarUsed ].Set( "k", true, true );
     isegVarUsed++;
   } else
   if ( str.equals( "!l" ) ) {
     segVarArray[ Region ][ isegVarUsed ].Set( "l", true, true );
     isegVarUsed++;
   } else
   if ( str.equals( "!m" ) ) {
     segVarArray[ Region ][ isegVarUsed ].Set( "m", true, true );
     isegVarUsed++;
   } else
   if ( str.equals( "!n" ) ) {
     segVarArray[ Region ][ isegVarUsed ].Set( "n", true, true );
     isegVarUsed++;
   } else
   if ( str.equals( "!o" ) ) {
     segVarArray[ Region ][ isegVarUsed ].Set( "o", true, true );
     isegVarUsed++;
   } else
   if ( str.equals( "!p" ) ) {
     segVarArray[ Region ][ isegVarUsed ].Set( "p", true, true );
     isegVarUsed++;
   } else
   if ( str.equals( "!q" ) ) {
     segVarArray[ Region ][ isegVarUsed ].Set( "q", true, true );
     isegVarUsed++;
   } else
   if ( str.equals( "!r" ) ) {
     segVarArray[ Region ][ isegVarUsed ].Set( "r", true, true );
     isegVarUsed++;
   } else
   if ( str.equals( "!s" ) ) {
     segVarArray[ Region ][ isegVarUsed ].Set( "s", true, true );
     isegVarUsed++;
   } else
   if ( str.equals( "!t" ) ) {
     segVarArray[ Region ][ isegVarUsed ].Set( "t", true, true );
     isegVarUsed++;
   }
      }
    }
    return isegVarUsed;
  }

  boolean MarkRegion( VennCanvas m_venncanvas, int Region,
                      segVar One, segVar Two, segVar Thr ) {
    One.Set( segVarArray[ Region ][ 0 ] );
    Two.Set( segVarArray[ Region ][ 1 ] );
    Thr.Set( segVarArray[ Region ][ 2 ] );
    switch (Region) {
      case 0:
        m_venncanvas.m_seg1_selected = bSelected[Region] ;
        break;
      case 1:
        m_venncanvas.m_seg2_selected = bSelected[Region] ;
        break;
      case 2:
        m_venncanvas.m_seg3_selected = bSelected[Region] ;
        break;
      case 3:
        m_venncanvas.m_seg4_selected = bSelected[Region] ;
        break;
      case 4:
        m_venncanvas.m_seg5_selected = bSelected[Region] ;
        break;
      case 5:
        m_venncanvas.m_seg6_selected = bSelected[Region] ;
        break;
      case 6:
        m_venncanvas.m_seg7_selected = bSelected[Region] ;
        break;
      default:
        break; // default statement
    }
    return true;
  }

  public void MarkCanvas( VennCanvas m_venncanvas ) {
    // System.out.println( "Marking canvass with :" ) ;
    m_venncanvas.m_validity = iValid;
    m_venncanvas.m_sValid = sValid;
    m_venncanvas.m_circleA.m_tag = sLabelArray[0];
    m_venncanvas.m_circleB.m_tag = sLabelArray[1];
    m_venncanvas.m_circleC.m_tag = sLabelArray[2];
    MarkRegion(   m_venncanvas, 0,
        m_venncanvas.m_seg1_var1,
        m_venncanvas.m_seg1_var2,
        m_venncanvas.m_seg1_var3 );
    MarkRegion(   m_venncanvas, 1,
        m_venncanvas.m_seg2_var1,
        m_venncanvas.m_seg2_var2,
        m_venncanvas.m_seg2_var3 );
    MarkRegion(   m_venncanvas, 2,
        m_venncanvas.m_seg3_var1,
        m_venncanvas.m_seg3_var2,
        m_venncanvas.m_seg3_var3 );
    MarkRegion(   m_venncanvas, 3,
        m_venncanvas.m_seg4_var1,
        m_venncanvas.m_seg4_var2,
        m_venncanvas.m_seg4_var3 );
    MarkRegion(   m_venncanvas, 4,
        m_venncanvas.m_seg5_var1,
        m_venncanvas.m_seg5_var2,
        m_venncanvas.m_seg5_var3 );
    MarkRegion(   m_venncanvas, 5,
        m_venncanvas.m_seg6_var1,
        m_venncanvas.m_seg6_var2,
        m_venncanvas.m_seg6_var3 );
    MarkRegion(   m_venncanvas, 6,
        m_venncanvas.m_seg7_var1,
        m_venncanvas.m_seg7_var2,
        m_venncanvas.m_seg7_var3 );
    MarkRegion(   m_venncanvas, 7,
        m_venncanvas.m_seg8_var1,
        m_venncanvas.m_seg8_var2,
        m_venncanvas.m_seg8_var3 );
    // System.out.println("We Marked the diagram") ;
  }

  void RecordRegion( VennCanvas m_venncanvas, int Region,
                            segVar One, segVar Two, segVar Thr ) {
    segVarArray[Region][0].Set( One );
    segVarArray[Region][1].Set( Two );
    segVarArray[Region][2].Set( Thr );
    switch (Region) {
    case 0:
      bSelected[Region] = m_venncanvas.m_seg1_selected ;
      break;
    case 1:
      bSelected[Region] = m_venncanvas.m_seg2_selected ;
      break;
    case 2:
      bSelected[Region] = m_venncanvas.m_seg3_selected ;
      break;
    case 3:
      bSelected[Region] = m_venncanvas.m_seg4_selected ;
      break;
    case 4:
      bSelected[Region] = m_venncanvas.m_seg5_selected ;
      break;
    case 5:
      bSelected[Region] = m_venncanvas.m_seg6_selected ;
      break;
    case 6:
      bSelected[Region] = m_venncanvas.m_seg7_selected ;
      break;
    default:
      break; // default statement
    }
  }

  public void RecordCanvas( VennCanvas m_venncanvas ) {
    // missing validity checkbox
    sLabelArray[0] = m_venncanvas.m_circleA.m_tag ;
    sLabelArray[1] = m_venncanvas.m_circleB.m_tag ;
    sLabelArray[2] = m_venncanvas.m_circleC.m_tag ;
    RecordRegion( m_venncanvas, 0,
        m_venncanvas.m_seg1_var1,
        m_venncanvas.m_seg1_var2,
        m_venncanvas.m_seg1_var3 );
    RecordRegion( m_venncanvas, 1,
        m_venncanvas.m_seg2_var1,
        m_venncanvas.m_seg2_var2,
        m_venncanvas.m_seg2_var3 );
    RecordRegion( m_venncanvas, 2,
        m_venncanvas.m_seg3_var1,
        m_venncanvas.m_seg3_var2,
        m_venncanvas.m_seg3_var3 );
    RecordRegion( m_venncanvas, 3,
        m_venncanvas.m_seg4_var1,
        m_venncanvas.m_seg4_var2,
        m_venncanvas.m_seg4_var3 );
    RecordRegion( m_venncanvas, 4,
        m_venncanvas.m_seg5_var1,
        m_venncanvas.m_seg5_var2,
        m_venncanvas.m_seg5_var3 );
    RecordRegion( m_venncanvas, 5,
        m_venncanvas.m_seg6_var1,
        m_venncanvas.m_seg6_var2,
        m_venncanvas.m_seg6_var3 );
    RecordRegion( m_venncanvas, 6,
        m_venncanvas.m_seg7_var1,
        m_venncanvas.m_seg7_var2,
        m_venncanvas.m_seg7_var3 );
    RecordRegion( m_venncanvas, 7,
        m_venncanvas.m_seg8_var1,
        m_venncanvas.m_seg8_var2,
        m_venncanvas.m_seg8_var3 );
    // System.out.println("We Recorded the diagram") ;
 }

}


class VennCanvas extends Canvas
 {
  int m_validity;
  String m_sValid ;
  boolean m_individual;
  boolean m_existential;
  boolean m_st;
  boolean m_cs;
  boolean m_shade;

  String m_valid_label = "Valid" ;
  String m_individual_label = "a"  ;
  String m_existential_label = "x" ;
  String m_class_label = "A" ;
  String m_text;

  int m_offsetX = 0 ;
  int m_offsetY = 0  ;
  boolean m_solution;


  boolean m_seg1_selected = false ;
  boolean m_seg2_selected = false;
  boolean m_seg3_selected = false;
  boolean m_seg4_selected = false;
  boolean m_seg5_selected = false;
  boolean m_seg6_selected = false;
  boolean m_seg7_selected = false;

  Font m_titleFont ;
  Font m_classFont ;
  Font m_varFont ;
  Font m_textFont;

  segVar m_seg1_var1 = new segVar();
  segVar m_seg1_var2 = new segVar();
  segVar m_seg1_var3 = new segVar();

  segVar m_seg2_var1 = new segVar();
  segVar m_seg2_var2 = new segVar();
  segVar m_seg2_var3 = new segVar();

  segVar m_seg3_var1 = new segVar();
  segVar m_seg3_var2 = new segVar();
  segVar m_seg3_var3 = new segVar();

  segVar m_seg4_var1 = new segVar();
  segVar m_seg4_var2 = new segVar();
  segVar m_seg4_var3 = new segVar();

  segVar m_seg5_var1 = new segVar();
  segVar m_seg5_var2 = new segVar();
  segVar m_seg5_var3 = new segVar();

  segVar m_seg6_var1 = new segVar();
  segVar m_seg6_var2 = new segVar();
  segVar m_seg6_var3 = new segVar();

  segVar m_seg7_var1 = new segVar();
  segVar m_seg7_var2 = new segVar();
  segVar m_seg7_var3 = new segVar();

  segVar m_seg8_var1 = new segVar();
  segVar m_seg8_var2 = new segVar();
  segVar m_seg8_var3 = new segVar();

  pgcircle m_circleA = new pgcircle("A");
  pgcircle m_circleB = new pgcircle("B");
  pgcircle m_circleC = new pgcircle("C");

  public VennCanvas() {
    m_sValid = " " ;

    m_titleFont = new java.awt.Font("Courier", Font.BOLD, 18);
    m_classFont = new java.awt.Font("Courier", Font.ITALIC, 24);
    m_varFont = new java.awt.Font("Courier", Font.BOLD, 12 );
    m_textFont = new java.awt.Font("Courier", Font.BOLD, 11 );

    // situates Circles where they need to be.
    m_circleA.m_x = 50;
    m_circleA.m_y = 23;       // 40
    m_circleB.m_x = 100;      // 150
    m_circleB.m_y = 25;
    m_circleC.m_x = 75;
    m_circleC.m_y = 80;       // 110

    m_circleA.m_width  = 110;
    m_circleA.m_height = 110;
    m_circleB.m_width  = 110;
    m_circleB.m_height = 110;
    m_circleC.m_width  = 110;
    m_circleC.m_height = 110;

    m_circleA.m_tagx = 55;
    m_circleA.m_tagy = 17;    // 35
    m_circleB.m_tagx = 210;   // 266;
    m_circleB.m_tagy = 48;    // 35;
    m_circleC.m_tagx = 121;
    m_circleC.m_tagy = 214;

    m_circleA.m_center_x = (m_circleA.m_x + (m_circleA.m_x + m_circleA.m_height))/2   ;
    m_circleA.m_center_y = (m_circleA.m_y + (m_circleA.m_y + m_circleA.m_width))/2   ;
    m_circleB.m_center_x = (m_circleB.m_x + (m_circleB.m_x + m_circleB.m_height))/2   ;
    m_circleB.m_center_y = (m_circleB.m_y + (m_circleB.m_y + m_circleB.m_width))/2   ;
    m_circleC.m_center_x = (m_circleC.m_x + (m_circleC.m_x + m_circleC.m_height))/2   ;
    m_circleC.m_center_y = (m_circleC.m_y + (m_circleC.m_y + m_circleC.m_width))/2   ;

    // Segment variable positions
    m_seg1_var1.m_x = 103;
    m_seg1_var1.m_y = 35;
    m_seg1_var2.m_x = 82;
    m_seg1_var2.m_y = 73;
    m_seg1_var3.m_x = 65;
    m_seg1_var3.m_y = 97;

    m_seg2_var1.m_x = 152;
    m_seg2_var1.m_y = 35;
    m_seg2_var2.m_x = 173;
    m_seg2_var2.m_y = 70;
    m_seg2_var3.m_x = 184;
    m_seg2_var3.m_y = 101;

    m_seg3_var1.m_x = 88;
    m_seg3_var1.m_y = 148;
    m_seg3_var2.m_x = 120;
    m_seg3_var2.m_y = 178;
    m_seg3_var3.m_x = 163;
    m_seg3_var3.m_y = 148;

    m_seg4_var1.m_x = 108;
    m_seg4_var1.m_y = 73;
    m_seg4_var2.m_x = 127;
    m_seg4_var2.m_y = 74;
    m_seg4_var3.m_x = 147;
    m_seg4_var3.m_y = 72;

    m_seg5_var1.m_x = 148;
    m_seg5_var1.m_y = 126;
    m_seg5_var2.m_x = 158;
    m_seg5_var2.m_y = 125;
    m_seg5_var3.m_x = 171;
    m_seg5_var3.m_y = 121;

    m_seg6_var1.m_x = 84;
    m_seg6_var1.m_y = 121;
    m_seg6_var2.m_x = 93;
    m_seg6_var2.m_y = 124;
    m_seg6_var3.m_x = 107;
    m_seg6_var3.m_y = 124;

    m_seg7_var1.m_x = 111;
    m_seg7_var1.m_y = 94;
    m_seg7_var2.m_x = 125;
    m_seg7_var2.m_y = 113;
    m_seg7_var3.m_x = 144;
    m_seg7_var3.m_y = 100;

    m_seg8_var1.m_x = 240;
    m_seg8_var1.m_y = 79;
    m_seg8_var2.m_x = 212;
    m_seg8_var2.m_y = 158;
    m_seg8_var3.m_x = 39;
    m_seg8_var3.m_y = 138;
  }

  public void paint(Graphics g) {
    g.setColor(Color.black);
    //   g.setFont(classFont);

   g.setFont(m_textFont);

    g.drawOval(m_circleA.m_x, m_circleA.m_y,
    m_circleA.m_width,m_circleA.m_height) ;

    g.drawOval(m_circleB.m_x, m_circleB.m_y,
    m_circleB.m_width,m_circleB.m_height) ;

    g.drawOval(m_circleC.m_x, m_circleC.m_y,
    m_circleC.m_width,m_circleC.m_height) ;

    g.setFont(m_classFont);

    // Puts the class tags in place ...
    g.drawString(m_circleA.m_tag,
    m_circleA.m_tagx , m_circleA.m_tagy );//30, 30 );
    g.drawString(m_circleB.m_tag,
    m_circleB.m_tagx , m_circleB.m_tagy );//300, 30 );
    g.drawString(m_circleC.m_tag,
    m_circleC.m_tagx , m_circleC.m_tagy );//150, 300 );

    g.setColor(Color.red);
    g.setFont(m_titleFont);
    g.drawString("1",  71, 55 );
    g.drawString("2", 173, 55 );
    g.drawString("3", 125, 165);
    g.drawString("4", 125, 61 );
    g.drawString("6",  92, 115);
    g.drawString("5", 162, 115);
    g.drawString("7", 127, 101);
    g.drawString("8", 230, 121);

    g.setColor(Color.black);
    g.setFont(m_varFont);

    // segment 1
    if(m_seg1_var1.used)
      paintSegment( g, m_seg1_var1 );
    if(m_seg1_var2.used)
      paintSegment( g, m_seg1_var2 );
    if(m_seg1_var3.used)
      paintSegment( g, m_seg1_var3 );
     // segment 2
    if(m_seg2_var1.used)
      paintSegment( g, m_seg2_var1 );
    if(m_seg2_var2.used)
      paintSegment( g, m_seg2_var2 );
    if(m_seg2_var3.used)
      paintSegment( g, m_seg2_var3 );
    // segment 3
    if(m_seg3_var1.used)
      paintSegment( g, m_seg3_var1 );
    if(m_seg3_var2.used)
      paintSegment( g, m_seg3_var2 );
    if(m_seg3_var3.used)
      paintSegment( g, m_seg3_var3 );
    // segment 4
    if(m_seg4_var1.used)
      paintSegment( g, m_seg4_var1 );
    if(m_seg4_var2.used)
      paintSegment( g, m_seg4_var2 );
    if(m_seg4_var3.used)
      paintSegment( g, m_seg4_var3 );
    // segment 5
    if(m_seg5_var1.used)
      paintSegment( g, m_seg5_var1 );
    if(m_seg5_var2.used)
      paintSegment( g, m_seg5_var2 );
    if(m_seg5_var3.used)
      paintSegment( g, m_seg5_var3 );
    // segment 6
    if(m_seg6_var1.used)
      paintSegment( g, m_seg6_var1 );
    if(m_seg6_var2.used)
      paintSegment( g, m_seg6_var2 );
    if(m_seg6_var3.used)
      paintSegment( g, m_seg6_var3 );
    // segment 7
    if(m_seg7_var1.used)
      paintSegment( g, m_seg7_var1 );
    if(m_seg7_var2.used)
      paintSegment( g, m_seg7_var2 );
    if(m_seg7_var3.used)
      paintSegment( g, m_seg7_var3 );
    // segment 8
    if(m_seg8_var1.used)
      paintSegment( g, m_seg8_var1 );
    if(m_seg8_var2.used)
      paintSegment( g, m_seg8_var2 );
    if(m_seg8_var3.used)
      paintSegment( g, m_seg8_var3 );

    if( m_seg1_selected == true ) {
      shade_seg_1(g);
    }
    if( m_seg2_selected == true ) {
      shade_seg_2(g);
    }
    if( m_seg3_selected == true ) {
      shade_seg_3(g);
    }
    if( m_seg4_selected == true ) {
      shade_seg_4(g);
    }
    if( m_seg5_selected == true ) {
      shade_seg_5(g);
    }
    if( m_seg6_selected == true ) {
      shade_seg_6(g);
    }
    if( m_seg7_selected == true ) {
      shade_seg_7(g);
    }

   // System.out.println("m_valid_label is: " + m_valid_label) ;

    // System.out.println("canvas is: " + m_validity) ;
    if(m_validity < 3) {
      // valid, invalid, or undecided ..
      g.setColor(colorValid(m_sValid));
      g.setFont(m_classFont);
      // Puts the class tags in place ...
      g.drawString( m_sValid , 55 , 235 );
      g.setColor(Color.black);
    }
  }

  public Color colorValid( String sValid ) {
      // valid, invalid, or undecided ..
      if (m_sValid.equals("No")) return(Color.red);
      if (m_sValid.equals("no")) return(Color.red);
      if (m_sValid.equals("false")) return(Color.red);
      if (m_sValid.equals("Yes")) return(Color.green.darker());
      if (m_sValid.equals("yes")) return(Color.green.darker());
      if (m_sValid.equals("true")) return(Color.green.darker());
      if (m_sValid.equals("Can't Tell")) return(Color.blue);
      if (m_sValid.equals("can't tell")) return(Color.blue);
      if (m_sValid.equals("unknown")) return(Color.blue);
      if (m_sValid.equals("undecidable")) return(Color.blue);
      return(Color.black);
  }

  public void paintSegment( Graphics g, segVar One ) {
      if( One.struckthrough) {
        g.drawString( One.label,
                      One.m_x,
                      One.m_y );
        g.setColor(Color.red);
        g.drawLine( One.m_x - 3,
                    One.m_y - 9,
                    One.m_x + 5,
                    One.m_y + 3);
        g.setColor(Color.black);
      } else
      g.drawString(One.label , One.m_x, One.m_y );
  }

  public void clearSegment(int segment) {
    switch (segment) {
      case 1:
      {
        clearRegion( m_seg1_var1, m_seg1_var2, m_seg1_var3 );
        m_seg1_selected = false ;
        break; // default statement
      }
      case 2:
      {
        clearRegion( m_seg2_var1, m_seg2_var2, m_seg2_var3 );
        m_seg2_selected = false ;
        break; // default statement
      }
      case 3:
      {
        clearRegion( m_seg3_var1, m_seg3_var2, m_seg3_var3 );
        m_seg3_selected= false ;
        break; // default statement
      }
      case 4:
      {
        clearRegion( m_seg4_var1, m_seg4_var2, m_seg4_var3 );
        m_seg4_selected = false ;
        break; // default statement
      }
      case 5:
      {
        clearRegion( m_seg5_var1, m_seg5_var2, m_seg5_var3 );
        m_seg5_selected = false ;
        break; // default statement
      }
      case 6:
      {
        clearRegion( m_seg6_var1, m_seg6_var2, m_seg6_var3 );
        m_seg6_selected = false ;
        break; // default statement
      }
      case 7:
      {
        clearRegion( m_seg7_var1, m_seg7_var2, m_seg7_var3 );
        m_seg7_selected = false ;
        break; // default statement
      }
      case 8:
      {
        clearRegion( m_seg8_var1, m_seg8_var2, m_seg8_var3 );
        // m_seg8_selected = false ;
        break;
      // default statement
      }
      default:
        break;
    }
  }
  public void clearRegion( segVar One, segVar Two, segVar Three) {
    One.used = false;
    One.struckthrough = false;
    Two.used = false;
    Two.struckthrough = false;
    Three.used = false;
    Three.struckthrough = false;
  }
  public void setStrikeThrough(int segment) {
    switch (segment) {
      case 1:
        strikeRegion( m_seg1_var1, m_seg1_var2, m_seg1_var3 );
        break; // exit the switch
      case 2:
        strikeRegion( m_seg2_var1, m_seg2_var2, m_seg2_var3 );
        break; // exit the switch
      case 3:
        strikeRegion( m_seg3_var1, m_seg3_var2, m_seg3_var3 );
        break; // exit the switch
      case 4:
        strikeRegion( m_seg4_var1, m_seg4_var2, m_seg4_var3 );
        break; // exit the switch
      case 5:
        strikeRegion( m_seg5_var1, m_seg5_var2, m_seg5_var3 );
        break; // exit the switch
      case 6:
        strikeRegion( m_seg6_var1, m_seg6_var2, m_seg6_var3 );
        break; // exit the switch
      case 7:
        strikeRegion( m_seg7_var1, m_seg7_var2, m_seg7_var3 );
        break; // exit the switch
      case 8:
        strikeRegion( m_seg8_var1, m_seg8_var2, m_seg8_var3 );
        break; // exit the switch
      default:
        break; // default statement
    }
  }
  public void strikeRegion( segVar One, segVar Two, segVar Three) {
    if(One.used == true) {
      if(m_individual_label == One.label)
        One.struckthrough = true;
    }
    if(Two.used == true) {
      if(m_individual_label == Two.label)
        Two.struckthrough = true;
    }
    if(Three.used == true) {
      if(m_individual_label == Three.label)
        Three.struckthrough = true;
    }
  }

  public void setVar(int segment) {
    switch (segment) {
      case 1:
        setRegion( m_seg1_var1, m_seg1_var2, m_seg1_var3 );
        break; // exit the switch
      case 2:
        setRegion( m_seg2_var1, m_seg2_var2, m_seg2_var3 );
        break; // exit the switch
      case 3:
        setRegion( m_seg3_var1, m_seg3_var2, m_seg3_var3 );
        break; // exit the switch
      case 4:
        setRegion( m_seg4_var1, m_seg4_var2, m_seg4_var3 );
        break; // exit the switch
      case 5:
        setRegion( m_seg5_var1, m_seg5_var2, m_seg5_var3 );
        break; // exit the switch
      case 6:
        setRegion( m_seg6_var1, m_seg6_var2, m_seg6_var3 );
        break; // exit the switch
      case 7:
        setRegion( m_seg7_var1, m_seg7_var2, m_seg7_var3 );
        break; // exit the switch
      case 8:
        setRegion( m_seg8_var1, m_seg8_var2, m_seg8_var3 );
        break; // exit the switch
      default:
        break; // default statement
    }
  }

  public void setRegion( segVar One, segVar Two, segVar Three) {
    if( One.used == false ) {
      One.used = true ;
      if( m_individual )
        One.label = m_individual_label;
      else
        One.label = m_existential_label;
    } else if( Two.used == false  &&
               !(( m_individual   && One.label == m_individual_label  ) ||
                 ( !m_individual  && One.label == m_existential_label ))) {
      Two.used = true ;
      if( m_individual )
        Two.label = m_individual_label;
      else
        Two.label = m_existential_label;
    } else if( Three.used == false &&
               !(( m_individual  && One.label == m_individual_label  ) ||
                 ( !m_individual && One.label == m_existential_label ) ||
                 ( m_individual  && Two.label == m_individual_label  ) ||
                 ( !m_individual && Two.label == m_existential_label ))) {
      Three.used = true ;
      if( m_individual )
        Three.label = m_individual_label;
      else
        Three.label = m_existential_label;
    }
  }

  public int doit(int x, int y) {
    boolean flagA;
    boolean flagB;
    boolean flagC;

    flagA = m_circleA.pointInCircle(x, y);
    flagB = m_circleB.pointInCircle(x, y);
    flagC = m_circleC.pointInCircle(x, y);

    // System.out.println("X and Y are  " + x + " " + y  ) ;

    if( flagA  & flagB  & flagC)
      return 7 ;
    else if( flagA  & !flagB & !flagC)
      return 1;
    else if( !flagA & flagB  & !flagC)
      return 2;
    else if( !flagA & !flagB & flagC)
      return 3;
    else if( flagA  & flagB  & !flagC)
      return 4;
    else if( !flagA & flagB  & flagC)
      return 5;
    else if( flagA  & !flagB & flagC)
      return 6;
    else if(y < 220 & x < 320 )
      return 8;
    else
      return 0;
  }

  public void shade_seg_1(Graphics g) {
    g.drawLine(99,24,116,41);
    g.drawLine(78,32,106,55);
    g.drawLine(57,53,100,74);
    g.drawLine(52,64,101,89);
    g.drawLine(50,82,87,101);
    g.drawLine(57,102,79,115);
    //    System.out.println("Here we are in  shade_seg_1(Graphics g)"  ) ;
    return;
  }
  public void shade_seg_2(Graphics g) {
    g.drawLine(145,40,159,25);
    g.drawLine(155,54,171,27);
    g.drawLine(160,67,173,56);
    g.drawLine(162,89,203,52);
    g.drawLine(176,103,210,71);
    g.drawLine(180,110,206,100);
    g.drawLine(186,40,190,37);
    return;
  }
  public void shade_seg_3(Graphics g) {
    g.drawLine(86,130,86,166);
    g.drawLine(99,134,102,181);
    g.drawLine(117,132,116,188);
    g.drawLine(138,133,140,189);
    g.drawLine(149,135,156,182);
    g.drawLine(168,133,170,173);
    //g.drawLine(212,190,230,237);
    //g.drawLine(230,190,238,225);
    //g.drawLine(238,189,245,210);
    return;
  }
  public void shade_seg_4(Graphics g) {
    g.drawLine(108,54,107,85);
    g.drawLine(118,39,119,81);
    g.drawLine(134,31,134,43);
    g.drawLine(134,63,134,79);
    g.drawLine(149,45,149,82);
    return;
  }
  public void shade_seg_6(Graphics g) {
    g.drawLine(118,121,106,132);
    g.drawLine(113,116,94,130);
    g.drawLine(107,108,104,109);
    g.drawLine(91,115,78,121);
    g.drawLine(90,99,101,95);
    return;
  }
  public void shade_seg_5(Graphics g) {
    g.drawLine(142,121,142,133);
    g.drawLine(148,114,150,134);
    g.drawLine(156,100,161,133);
    g.drawLine(160,90,163,98);
    g.drawLine(168,117,173,131);
    // g.drawLine(239,145,241,188);
    return;
  }
  public void shade_seg_7(Graphics g) {
    g.drawLine(119,120,141,120);
    g.drawLine(112,113,148,112);
    g.drawLine(103,98,124,99);
    g.drawLine(139,98,158,98);
    g.drawLine(109,85,149,83);
    // g.drawLine(173,168,199,122);
    return;
  }
}

class PrettyFormat {
  int m_incsize = 69;
  int m_size;
  String m_string;
  Vector m_string_vector;
  int m_lines  ;
  public PrettyFormat()
  {
    m_string_vector = new Vector(5,5); //vector grows by 5 also
  }
  public void format( String astring )
  {
    cleanup();
    m_string = astring ;
    m_lines = 0;
    m_size = astring.length();
    if (m_size <= m_incsize )
    {
        m_string_vector.addElement( new String( m_string) );
      return;
    }else
   {
      StringBuffer tmp_string;
      tmp_string = new StringBuffer("");
      int i;
      boolean first_time = true;
      i = 0;

      for(StringTokenizer t = new StringTokenizer( m_string, " "); t.hasMoreTokens(); ){
      //   int m = t.countTokens();
         String newstr = t.nextToken() ;
         i += newstr.length();
         if( i <= m_incsize )
         {
            if(first_time )
            {
               tmp_string.append(newstr );
               first_time = false;
            }else
            {
               tmp_string.append( " " + newstr );
            }

         }else if( i > m_incsize )
         {
            m_string_vector.addElement( tmp_string.toString() );
            tmp_string = new StringBuffer("");   //reset tmp_string
            i = 0; //reset i
            tmp_string.append(newstr );
         }
      }
      m_string_vector.addElement( tmp_string.toString() );
   }
  }

  public void cleanup()
  {
    m_string_vector.removeAllElements();
  }
}

//
//
// circle
//
//
class pgcircle {
  int m_x ;
  int m_y ;
  int m_width ;
  int m_height ;
  int m_tagx ;
  int m_tagy ;

  int m_center_x ;
  int m_center_y ;

  String m_tag;
  String m_sentence;

  public pgcircle()
  {
    m_tag = "nada";
  }

  public pgcircle(String tag)
  {
    m_tag = tag;
  }

  boolean pointInCircle(int x, int y)
  {
    if ( ( ( ( (x - m_center_x)*(x - m_center_x) ) +
             ( (y - m_center_y)*(y - m_center_y) ) ) -
           (m_height/2 * m_height/2)
         ) > 0 )
     return false;
   else
     return true;
  }
}

// This represents the variable positions in the circles
// (Either individuals or Existential quantifiers)
// There are 3 per segment.
class segVar {
  boolean used;
  boolean struckthrough;
  int m_x ;
  int m_y ;
  String label ;

  public segVar()  {
    used = false;
    struckthrough = false;
  }

  public void Print() {
    System.out.print( "segVar(" );
    System.out.print( "used=" ) ;
    System.out.print( used ) ;
    System.out.print( ", struckthrough=" ) ;
    System.out.print( struckthrough ) ;
    System.out.print( ", m_x=" );
    System.out.print( m_x );
    System.out.print( ", m_y=" );
    System.out.print( ", label=" ) ;
    System.out.print( label ) ;
    System.out.println(")");
  }

  public String printLabel() {
    return( label ) ;
  }

  public void Set( segVar sv )  {
    used = sv.used;
    label = sv.label;
    struckthrough = sv.struckthrough;
  }
  public void Set( String sLabel, boolean bUsed, boolean bStruck )  {
    label = sLabel;
    used = bUsed;
    struckthrough = bStruck;
  }
}
// This represents the regions of the circles
//
// There are 3 segments per region.
class Region {
  segVar sv1;
  segVar sv2;
  segVar sv3;

  public Region() {
    sv1 = new segVar();
    sv2 = new segVar();
    sv3 = new segVar();
  }
  public Region( segVar One, segVar Two, segVar Three) {
    sv1 = One;
    sv2 = Two;
    sv3 = Three;
  }
}

/*
* Simple frame to enable us to execute an Applet as a standalone.
* taken from the w-prolog example, original
* @author Michael Winikoff
*/
class AppletFrame extends Frame {

  static public AppletContext ac;
  public venn1 applet;

  public AppletFrame() {
    super("venn-1.2");

    MenuBar menubar = new MenuBar();
    Menu file = new Menu("File",false);
    menubar.add(file);
    file.add("Save");
    file.add("Quit");
    this.setMenuBar(menubar);

    applet = new venn1(this);

    this.add("Center",applet);
    // width, height
    this.resize(600, 440);  // For VGA lusers

    applet.init();
    applet.start();
    this.show();
  }

  public boolean handleEvent(Event e) {
    if (e.id == Event.WINDOW_DESTROY) {
      if (venn1.inapplet) dispose();
      else System.exit(0);
    }
    if (e.target instanceof MenuItem) {
      if ( e.arg == "Save" ) {
        applet.generateHTML( applet.codebase );
      } else
      if (venn1.inapplet) dispose();
      else System.exit(0);
    }
    return false;
  }
}
