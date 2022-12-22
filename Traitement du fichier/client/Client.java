package client;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.Socket;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

/**
 * Fenetre
 */
public class Client implements ActionListener{

     public static void main(String[] args) {
          final File[] fileToSend = new File[1];
          JFrame jFrame = new JFrame("file tranfert");
          jFrame.setSize(450, 450);
          jFrame.setLayout(new BoxLayout(jFrame.getContentPane(),BoxLayout.Y_AXIS));
          jFrame.setDefaultCloseOperation(jFrame.EXIT_ON_CLOSE);
          
          JLabel title = new JLabel("file sender");
          title.setFont(new Font("Arial",Font.BOLD,25));
          title.setBorder(new EmptyBorder(20,0,10,0));
          title.setAlignmentX(Component.CENTER_ALIGNMENT);

          JLabel fileName = new JLabel("Choose a file to send");
          fileName.setFont(new Font("Arial",Font.BOLD,20));
          fileName.setBorder(new EmptyBorder(50,0,0,0));
          fileName.setAlignmentX(Component.CENTER_ALIGNMENT);

          JPanel jp = new JPanel();
          jp.setBorder(new EmptyBorder(75,0, 10, 0));

          JButton jbSendFile = new JButton("send file");
          jbSendFile.setPreferredSize(new Dimension(150,75));
          jbSendFile.setFont(new Font("Arial",Font.BOLD,20));

          JButton jbChooseFile = new JButton("Choose File");
          jbChooseFile.setPreferredSize(new Dimension(150,75));
          jbChooseFile.setFont(new Font("Arial",Font.BOLD,20));

          jp.add(jbSendFile);
          jp.add(jbChooseFile);

          jbChooseFile.addActionListener(new ActionListener() {
               @Override
               public void actionPerformed(ActionEvent e) {
                    JFileChooser jFileChooser = new JFileChooser();
                    jFileChooser.setDialogTitle("Choose a title to send");

                    if (jFileChooser.showOpenDialog(null) == jFileChooser.APPROVE_OPTION) {
                         fileToSend[0] = jFileChooser.getSelectedFile();
                         fileName.setText("The file you want to send is "+fileToSend[0].getName() );
                    }
               }
          });

          jbSendFile.addActionListener(new ActionListener() {
               @Override
               public void actionPerformed(ActionEvent e) {
                    if (fileToSend[0] == null) {
                         fileName.setText("Please choose a file ");
                    }
                    else {
                         try {
                              FileInputStream fileInputStream = new FileInputStream(fileToSend[0].getAbsolutePath());
                              Socket socket = new Socket("localhost",20057);
                              DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());

                              String fileName = fileToSend[0].getName();
                              byte[] fileNameBytes = fileName.getBytes();

                              byte[] filContentBytes = new byte[(int)fileToSend[0].length()];
                              fileInputStream.read(filContentBytes);

                              dataOutputStream.writeInt(fileNameBytes.length);
                              dataOutputStream.write(fileNameBytes);

                              dataOutputStream.writeInt(filContentBytes.length);
                              dataOutputStream.write(filContentBytes);
                         } catch (IOException e1) {
                              // TODO Auto-generated catch block
                              e1.printStackTrace();
                         }
                    }
               }
          });
          jFrame.add(title);
          jFrame.add(fileName);
          jFrame.add(jp);
          jFrame.setVisible(true);

     }

     @Override
     public void actionPerformed(ActionEvent e) {
          // TODO Auto-generated method stub
          
     }
}
