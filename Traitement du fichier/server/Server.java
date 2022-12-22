package server;

import file.FileToSend;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

public class Server implements ActionListener {
     static ArrayList<FileToSend> myFiles = new ArrayList<>();

     public static String getFileExtension(String filName) {
          int i = filName.lastIndexOf('.');

          if (i > 0) return filName.substring(i+1);
          else return "No extension found";
     }

     public static JFrame createFrame(String fileName,byte[] fileData,String fileExtension) {
          JFrame jFrame = new JFrame("Wittcode's file downloader");
          jFrame.setSize(400,400);

          JPanel jPanel = new JPanel();
          jPanel.setLayout(new BoxLayout(jPanel,BoxLayout.Y_AXIS));

          JLabel title = new JLabel("Wittcode's file downloader");
          title.setAlignmentX(Component.CENTER_ALIGNMENT);
          title.setFont(new Font("Arial",Font.BOLD,25));
          title.setBorder(new EmptyBorder(20,0,10,0));

          JLabel jPrompt = new JLabel("Are you sure you want to download");
          jPrompt.setFont(new Font("Arial",Font.BOLD,20));
          jPrompt.setBorder(new EmptyBorder(20,0,10,0));
          jPrompt.setAlignmentX(Component.CENTER_ALIGNMENT);

          JButton jbYes = new JButton("YES");
          jbYes.setPreferredSize(new Dimension(150,75));
          jbYes.setFont(new Font("Arial",Font.BOLD,20));

          JButton jbNo = new JButton("No");
          jbNo.setPreferredSize(new Dimension(150,75));
          jbNo.setFont(new Font("Arial",Font.BOLD,20));

          JLabel jlFileContent = new JLabel();
          jlFileContent.setAlignmentX(Component.CENTER_ALIGNMENT);

          JPanel jpButtons = new JPanel();
          jpButtons.setBorder(new EmptyBorder(20,0,10,0));
          jpButtons.add(jbYes);
          jpButtons.add(jbNo);

          jlFileContent.setIcon(new ImageIcon(fileData));

          jbYes.addActionListener(new ActionListener() {
               @Override
               public void actionPerformed(ActionEvent e) {
                    File fileToDownload = new File(fileName);
                    
                    try (FileOutputStream fileOutputStream = new FileOutputStream(fileToDownload)) {
                         fileOutputStream.close();

                         jFrame.dispose();
                    } catch (IOException e1) {
                         // TODO Auto-generated catch block
                         e1.printStackTrace();
                    }
                    
               }
          });

          jbNo.addActionListener(new ActionListener() {
               @Override
               public void actionPerformed(ActionEvent e) {
                    jFrame.dispose();
               }
          });

          jPanel.add(title);
          jPanel.add(jPrompt);
          jPanel.add(jlFileContent);
          jPanel.add(jpButtons);

          jFrame.add(jPanel);

          return jFrame;
     }
     public static MouseListener getMyMouseListener() {
          return new MouseListener() {

               @Override
               public void mouseClicked(MouseEvent e) {
                    // TODO Auto-generated method stub
                    JPanel jPanel = (JPanel) e.getSource();

                    int field = Integer.parseInt(jPanel.getName());
                    for(FileToSend file : myFiles) {
                         JFrame jfPreview = createFrame(file.getName(),file.getData(),file.getFileExtension());
                         jfPreview.setVisible(true);

                    }
                    
               }

               @Override
               public void mousePressed(MouseEvent e) {
                    // TODO Auto-generated method stub
                    
               }

               @Override
               public void mouseReleased(MouseEvent e) {
                    // TODO Auto-generated method stub
                    
               }

               @Override
               public void mouseEntered(MouseEvent e) {
                    // TODO Auto-generated method stub
                    
               }

               @Override
               public void mouseExited(MouseEvent e) {
                    // TODO Auto-generated method stub
                    
               }
          };
     }

     public static void main(String[] args) throws IOException {
          int fileId = 0;

          JFrame jFrame = new JFrame("Wittcode's Server");
          jFrame.setSize(400,400);
          jFrame.setLayout(new BoxLayout(jFrame.getContentPane(),BoxLayout.Y_AXIS));
          jFrame.setDefaultCloseOperation(jFrame.EXIT_ON_CLOSE);

          JPanel jPanel = new JPanel();
          jPanel.setLayout(new BoxLayout(jPanel,BoxLayout.Y_AXIS));

          JScrollPane jScrollPane = new JScrollPane(jPanel);
          jScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

          JLabel title = new JLabel("Wittcode 's file Receiver");
          title.setFont(new Font("Arial",Font.BOLD,25));
          title.setBorder(new EmptyBorder(20,0,10,0));
          title.setAlignmentX(Component.CENTER_ALIGNMENT);

          jFrame.add(title);
          jFrame.add(jScrollPane);
          jFrame.setVisible(true);

               ServerSocket serverSocket = new ServerSocket(20057);
               while (true) {
                    try {
                         Socket socket = serverSocket.accept();

                         DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());

                         int fileNameLength = dataInputStream.readInt();

                         if (fileNameLength > 0) {
                              byte[] fileNameBytes = new byte[fileNameLength];

                              dataInputStream.readFully(fileNameBytes,0,fileNameBytes.length);
                              
                              String fileName = new String(fileNameBytes);
                              System.out.println(fileName);
                              
                              int fileContentLength = dataInputStream.readInt();
                              if (fileContentLength > 0) {
                                   
                                   byte[] fileContentBytes = new byte[fileContentLength]; 
                                   dataInputStream.readFully(fileContentBytes,0,fileContentLength);

                                   JPanel jpFileRow = new JPanel();
                                   jpFileRow.setLayout(new BoxLayout(jpFileRow,BoxLayout.Y_AXIS));
                                   
                                   JLabel jlFileName = new JLabel(fileName);
                                   jlFileName.setFont(new Font("Arial",Font.BOLD,20));
                                   jlFileName.setBorder(new EmptyBorder(10,0,10,0));

                                   
                                   jpFileRow.setName(String.valueOf(fileId));
                                   jpFileRow.addMouseListener(getMyMouseListener());
                                   jpFileRow.add(jlFileName);
                                   jPanel.add(jpFileRow);
                                   jFrame.validate();
                                   
                                   myFiles.add(new FileToSend(fileId, fileName, fileContentBytes, getFileExtension(fileName)));
                                   fileId++;
                              }
                         }
                    } catch (Exception e) {
                         e.printStackTrace();
                    }
               }
     }

     @Override
     public void actionPerformed(ActionEvent e) {
          // TODO Auto-generated method stub
          
     }
}
