/*    */ import java.awt.Frame;
/*    */ import java.awt.event.ActionEvent;
/*    */ import java.awt.event.ActionListener;
/*    */ import javax.swing.JButton;
/*    */ import javax.swing.JFrame;
/*    */ import javax.swing.JScrollPane;
/*    */ import javax.swing.JTextArea;
/*    */ import javax.swing.JTextField;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Main
/*    */   extends Frame
/*    */ {
/*    */   private static final int WIDTH = 400;
/*    */   private static final int HEIGHT = 300;
/* 17 */   static JFrame guiFrame = new JFrame();
/* 18 */   static JButton button = new JButton("Search");
/*    */   public static boolean running = false;
/*    */   public static boolean stop = true;
/* 21 */   public static JTextField textField = new JTextField(20);
/* 22 */   public static JTextArea textArea = new JTextArea(1, 40);
/* 23 */   public static JScrollPane scroll = new JScrollPane(textArea);
/*    */   
/*    */   public static void main(String[] args) {
/* 26 */     guiFrame.setDefaultCloseOperation(3);
/* 27 */     guiFrame.setTitle("Double hutter By Xcom totally not using L64s hut finder");
/* 28 */     guiFrame.setSize(600, 250);
/* 29 */     textField.setText("Enter Seed Here");
/* 30 */     textArea.setEditable(false);
/* 31 */     guiFrame.add(textField, "North");
/*    */     
/* 33 */     guiFrame.add(button, "South");
/*    */     
/* 35 */     scroll = new JScrollPane(textArea);
/* 36 */     scroll.setVerticalScrollBarPolicy(22);
/* 37 */     guiFrame.add(scroll, "Center");
/*    */     
/* 39 */     button.addActionListener(new ActionListener()
/*    */         {
/*    */           public void actionPerformed(ActionEvent event)
/*    */           {
/* 43 */             if (!Main.running) {
/* 44 */               Main.stop = false;
/* 45 */               Main.running = true;
/* 46 */               Main.button.setText("Stop");
/*    */               try {
/* 48 */                 Main.textArea.setText("");
/* 49 */                 String str = Main.textField.getText();
/* 50 */                 Long seed = Long.valueOf(Long.parseLong(str));
/* 51 */                 (new QuadHutFinder(seed)).start();
/* 52 */               } catch (Exception e) {
/* 53 */                 Main.button.setText("Search");
/* 54 */                 Main.running = false;
/* 55 */                 Main.textArea.setText("That is not a seed you mongrel.");
/*    */               } 
/*    */             } else {
/* 58 */               Main.stop = true;
/*    */             } 
/*    */           }
/*    */         });
/*    */     
/* 63 */     guiFrame.setVisible(true);
/*    */   }
/*    */ }


/* Location:              /Users/jfoster/Desktop/DoubleHutFinder_1_13_by_Vales.jar!/Main.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.0.7
 */