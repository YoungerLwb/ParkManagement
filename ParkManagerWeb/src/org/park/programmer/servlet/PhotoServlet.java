package org.park.programmer.servlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.park.programmer.dao.CarInfoDao;
import org.park.programmer.dao.CarUserDao;
import org.park.programmer.entity.CarInfo;
import org.park.programmer.entity.CarUser;

import com.lizhou.exception.FileFormatException;
import com.lizhou.exception.NullFileException;
import com.lizhou.exception.ProtocolException;
import com.lizhou.exception.SizeException;
import com.lizhou.fileload.FileUpload;
/**
	 * 
	 */

/**
 * 
 * @author Administrator
 *	图片处理类servlet
 */
public class PhotoServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	protected void doGet(HttpServletRequest request, HttpServletResponse response) {
		String method = request.getParameter("method");
		if("getPhoto".equals(method)) {
			getPhoto(request,response);
		}else if("SetPhoto".equals(method)) {
			uploadPhoto(request,response);
		}else if("AddPhoto".equals(method)) {
			addPhoto(request,response);
		}
	}
	private void addPhoto(HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		FileUpload fileUpload = new FileUpload(request);
		fileUpload.setFileFormat("jpg");
		fileUpload.setFileFormat("png");
		fileUpload.setFileFormat("jpeg");
		fileUpload.setFileFormat("gif");
		fileUpload.setFileSize(2048);
		response.setCharacterEncoding("utf-8");
		try {
			InputStream uploadInputStream = fileUpload.getUploadInputStream();
			CarUser carUser = new CarUser();
			carUser.setUphoto(uploadInputStream);
		} catch (ProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NullFileException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SizeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileUploadException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private void uploadPhoto(HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		int uid = request.getParameter("uid") == null ? 0:Integer.parseInt(request.getParameter("uid"));
		int cid = request.getParameter("cid") == null ? 0:Integer.parseInt(request.getParameter("cid"));

		FileUpload fileUpload = new FileUpload(request);
		fileUpload.setFileFormat("jpg");
		fileUpload.setFileFormat("png");
		fileUpload.setFileFormat("jpeg");
		fileUpload.setFileFormat("gif");
		fileUpload.setFileSize(2048);

		response.setCharacterEncoding("utf-8");
			try {
				InputStream uploadInputStream = fileUpload.getUploadInputStream();
				
				if(uid!=0) {
					CarUser carUser = new CarUser();
					carUser.setUid(uid);
					carUser.setUphoto(uploadInputStream);
					CarUserDao carUserDao = new CarUserDao();
					if(carUserDao.setCarUserPhoto(carUser)) {
						response.getWriter().write("<div id='message'>上传成功</div>");
					}else {
						response.getWriter().write("<div id='message'>上传失败</div>");
					}
				}
				if(cid!=0) {
					CarInfo carInfo = new CarInfo();
					carInfo.setCid(cid);
					carInfo.setCphoto(uploadInputStream);
					CarInfoDao carInfoDao = new CarInfoDao();
					if(carInfoDao.setCarInfoPhoto(carInfo)) {
						response.getWriter().write("<div id='message'>上传成功</div>");
					}else {
						response.getWriter().write("<div id='message'>上传失败</div>");
					}
				}
			} catch (ProtocolException e) {
				// TODO Auto-generated catch block
				try {
					response.getWriter().write("<div id='message'>上传协议错误</div>");
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				e.printStackTrace();
			} catch (NullFileException e) {
				// TODO Auto-generated catch block
				try {
					response.getWriter().write("<div id='message'>上传文件为空</div>");
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				e.printStackTrace();
			} catch (SizeException e) {
				// TODO Auto-generated catch block
				try {
					response.getWriter().write("<div id='message'>上传文件大小不能超过"+fileUpload.getFileSize()+"</div>");
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				e.printStackTrace();
			} catch (FileFormatException e) {
				// TODO Auto-generated catch block
				try {
					response.getWriter().write("<div id='message'>上传文件格式错误，请上传"+fileUpload.getFileFormat()+"格式的文件</div>");
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				try {
					response.getWriter().write("<div id='message'>读取文件出错</div>");
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				e.printStackTrace();
			} catch (FileUploadException e) {
				// TODO Auto-generated catch block
				try {
					response.getWriter().write("<div id='message'>上传文件失败</div>");
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				e.printStackTrace();
			}
		
	}
	private void getPhoto(HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		int uid = request.getParameter("uid") == null ? 0:Integer.parseInt(request.getParameter("uid"));
		int cid = request.getParameter("cid") == null ? 0:Integer.parseInt(request.getParameter("cid"));

		if(uid != 0) {
				//车主		
				CarUserDao carUserDao = new CarUserDao();
				CarUser carUser = carUserDao.getCarUser(uid);  
				carUserDao.closeCon();
				if(carUser != null) {
					InputStream uphoto = carUser.getUphoto();
					if(uphoto!=null) {
						
						try {
							byte[] b = new byte[uphoto.available()];
							uphoto.read(b);
							outputStream(b, response);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						return;
					
				}
			}
		}else if(cid!=0) {
			CarInfoDao carInfoDao = new CarInfoDao();
			CarInfo carInfo = carInfoDao.getCarInfoPhoto(cid);  
			carInfoDao.closeCon();
			if(carInfo != null) {
				InputStream cphoto = carInfo.getCphoto();
				if(cphoto!=null) {
					
					try {
						byte[] b = new byte[cphoto.available()];
						cphoto.read(b);
						outputStream(b, response);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					return;
				
			}
		}
		}
		String path = request.getSession().getServletContext().getRealPath("");//获取当前的工作空间
		File file = new File(path+"\\file\\teacher.png");
		try {
			FileInputStream files = new FileInputStream(file);
			
			byte[] b = new byte[files.available()];
			files.read(b);
			outputStream(b, response);
			 
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response){
			
			doGet(request, response);
	}
	private void outputStream(byte[] b,HttpServletResponse response) {
		try {
			response.getOutputStream().write(b,0,b.length);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	
	}
