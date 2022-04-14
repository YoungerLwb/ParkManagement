package org.park.programmer.myserver;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;

import org.park.programmer.dao.DuojiDao;
import org.park.programmer.entity.DuoJi;

public class Myserver {
    //监听端口
	
	
	
    private static final int PORT = 60020;

    public void goRun() throws IOException {
       
    	ServerSocket serverSocket = null;
        Socket socket = null;
        try {
            //建立服务器的Socket，并设定一个监听的端口PORT
            serverSocket = new ServerSocket(PORT);
            //由于需要进行循环监听，因此获取消息的操作应放在一个while大循环中
            while(true){
                 try {
                    //建立跟客户端的连接
                    socket = serverSocket.accept();
                 } catch (Exception e) {
                     System.out.println("建立与客户端的连接出现异常");
                     e.printStackTrace();
                 }
                 ServerThread thread = new ServerThread(socket);
                 thread.start();
            }
        } catch (Exception e) {
            System.out.println("端口被占用");
            e.printStackTrace();
        }        
        finally {
            serverSocket.close();
        }
    }
}

//服务端线程类
//继承Thread类的话，必须重写run方法，在run方法中定义需要执行的任务。
class ServerThread extends Thread {
    private Socket socket ;
    InputStream inputStream;
    OutputStream outputStream;
    
    public  ServerThread(Socket socket){
        this.socket=socket;
    }
    
    
    public int totalnum(){
    	String url = "jdbc:mysql://localhost:3306/db_park_manager_web?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=UTC";
		String user = "root";
		String password = "123456";
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		} // 加载具体的驱动类
			// b.于数据库建立连接
		Connection connection = null;
		try {
			connection = DriverManager.getConnection(url, user, password);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		}
		// c.发送，执行（增删改，查）
		Statement stmt = null;
		try {
			stmt = connection.createStatement();
			
			
			String sql = "select count(*) from s_park where status='空闲'";
			ResultSet rs = null;
			rs = stmt.executeQuery(sql);
			int count =-1;
			while(rs.next()) {
				count = rs.getInt(1);
			}
			System.out.println(count);
			return count;
			
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return -1;
		}
		
    }
    
    public int total(){
    	String url = "jdbc:mysql://localhost:3306/db_park_manager_web?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=UTC";
		String user = "root";
		String password = "123456";
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		} // 加载具体的驱动类
			// b.于数据库建立连接
		Connection connection = null;
		try {
			connection = DriverManager.getConnection(url, user, password);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		}
		// c.发送，执行（增删改，查）
		Statement stmt = null;
		try {
			stmt = connection.createStatement();
			
			
			String sql = "select count(*) from s_park";
			ResultSet rs = null;
			rs = stmt.executeQuery(sql);
			int count =-1;
			while(rs.next()) {
				count = rs.getInt(1);
			}
			System.out.println(count);
			return count;
			
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return -1;
		}
		
    }
    
    /**
     * 查找舵机状态
     */
    public String Duoji(){
    	String url = "jdbc:mysql://localhost:3306/db_park_manager_web?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=UTC";
		String user = "root";
		String password = "123456";
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		} // 加载具体的驱动类
			// b.于数据库建立连接
		Connection connection = null;
		try {
			connection = DriverManager.getConnection(url, user, password);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		}
		// c.发送，执行（增删改，查）
		Statement stmt = null;
		try {
			stmt = connection.createStatement();
			
			
			String sql = "select sduoji from s_duoji where sid=1";
			ResultSet rs = null;
			rs = stmt.executeQuery(sql);
			String duoji = null;
			while(rs.next()) {
				duoji = rs.getString("sduoji");
			}
			System.out.println(duoji);
			return duoji;
			
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return null;
		}
		
    }
    
    public void run(){
        try {
            //while (true){
                //接收客户端的消息并打印
                System.out.println(socket);
                inputStream=socket.getInputStream();
                byte[] bytes = new byte[1024];
                inputStream.read(bytes);
                String string = new String(bytes);
                System.out.println(string);    
                
                //向客户端发送消息
                outputStream = socket.getOutputStream();
                int b = totalnum();
                String a;
                if(b<10) {
                	  a = Integer.toString(b)+" ";
                }else {
                	 a = Integer.toString(b);
                }
                int c = total();
                String d = Integer.toString(c);
                SimpleDateFormat tempDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
                String datetime = tempDate.format(new java.util.Date());
                
                String dstatus = Duoji();
                
                 String aa = d+a+datetime+dstatus;
                 System.out.println(aa);
                outputStream.write(aa.getBytes());
                System.out.println("OK");
               
                DuojiDao duojidao = new DuojiDao();
                DuoJi duoji = new DuoJi();
                duoji.setSduoji("0");
                duojidao.editDuoji(duoji);
           // }
        } catch (Exception e) {
            System.out.println("客户端主动断开连接了");
            //e.printStackTrace();
        }
        //操作结束，关闭socket
        try{
            socket.close(); 
        }catch(IOException e){
            System.out.println("关闭连接出现异常");    
            e.printStackTrace();
        }
    }
}