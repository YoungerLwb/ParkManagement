package org.park.programmer.servlet;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadBase;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.park.programmer.dao.CarInfoDao;
import org.park.programmer.dao.CarUserDao;
import org.park.programmer.entity.CarInfo;
import org.park.programmer.entity.CarUser;
import org.park.programmer.entity.Page;

import net.sf.json.JSONObject;
/**
 * 
 * @author Administrator
 * 车辆信息管理servlet
 */
public class CarInfoServlet extends HttpServlet {
/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	String method = request.getParameter("method");
	if ("toCarInfoListView".equals(method)) {
		CarInfoList(request, response);
	} else if ("AddCarInfo".equals(method)) {
		addCarInfo(request, response);
	}else if ("getCarInfoList".equals(method)) {
		getCarInfoList(request, response);
	}else if("EditCarInfo".equals(method)) {
		editCarInfoList(request, response);
	}else if("DeleteCarInfo".equals(method)) {
		delCarInfoList(request,response);
	}
		}
	private void delCarInfoList(HttpServletRequest request, HttpServletResponse response) {
	// TODO Auto-generated method stub
		String[] cids = request.getParameterValues("cids[]");
		String idStr = "";
		for(String cid:cids) {
			idStr += cid+",";
		}
		idStr = idStr.substring(0, idStr.length()-1);
		CarInfoDao carInfoDao = new CarInfoDao();
		if(carInfoDao.delCarInfo(idStr)) {
			try {
				response.getWriter().write("success");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally {
				carInfoDao.closeCon();
			}
		}
}
	private void editCarInfoList(HttpServletRequest request, HttpServletResponse response) {
	// TODO Auto-generated method stub
		Integer cid =Integer.parseInt(request.getParameter("ecid"));
		String cplatenum = request.getParameter("ecplatenum");
		String cname = request.getParameter("ecname");
		String cbrand = request.getParameter("ecbrand");
		
	
		CarInfo carInfo = new CarInfo();
		carInfo.setCid(cid);
		carInfo.setCplatenum(cplatenum);
		carInfo.setCname(cname);
		carInfo.setCbrand(cbrand);
		CarInfoDao carInfoDao = new CarInfoDao();
		if(carInfoDao.editCarInfo(carInfo)) {
			try {
				response.getWriter().write("success");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally {
				carInfoDao.closeCon();
			}
		}
}
	private void getCarInfoList(HttpServletRequest request, HttpServletResponse response) {
	// TODO Auto-generated method stub
		String cplatenum = request.getParameter("carPlatenum");
		String carcname = request.getParameter("carcname");
		Integer currentPage = request.getParameter("page") == null?1 : Integer.parseInt(request.getParameter("page"));
		Integer pageSize = request.getParameter("rows") == null?999 : Integer.parseInt(request.getParameter("rows"));
		CarInfo carInfo = new CarInfo();
		carInfo.setCname(carcname);
		carInfo.setCplatenum(cplatenum);
		CarInfoDao carInfoDao = new CarInfoDao();
		List<CarInfo> carUserList = carInfoDao.getCarInfoList(carInfo, new Page(currentPage,pageSize));
		int total = carInfoDao.getCarInfoListTotal(carInfo);
		carInfoDao.closeCon();
		response.setCharacterEncoding("UTF-8");
		//JsonConfig jsonConfig = new JsonConfig();
		//String ParkListString = JSONArray.fromObject(parkList, jsonConfig).toString();
		Map<String,Object> ret = new HashMap<String,Object>();
		ret.put("total",total);
		ret.put("rows",carUserList);
		
		try {
			response.getWriter().write(JSONObject.fromObject(ret).toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
}
	private void addCarInfo(HttpServletRequest request, HttpServletResponse response) {
	// TODO Auto-generated method stub
		// TODO Auto-generated method stub
				try {
					request.setCharacterEncoding("utf-8");
				} catch (UnsupportedEncodingException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				response.setCharacterEncoding("utf-8");
				response.setContentType("text/html; charset=UTF-8");
				//上传
				boolean isMultipart = ServletFileUpload.isMultipartContent(request);
				if(isMultipart) {//判断前台form是否有multipart属性
					//FileItemFactory factory = new DiskFileItemFactory();//FileItemFactory的实现类，FileItemFactory是一个接口
					DiskFileItemFactory factory = new DiskFileItemFactory();
					ServletFileUpload upload = new ServletFileUpload(factory);
					CarInfoDao carInfoDao = new CarInfoDao();
					try {		
						//控制上传单个文件的大小 20KB ServletFileUpload						
						//supload.setSizeMax(40960);//单位是字节B
						//通过parseRequest(解析form中的所有请求字段，并保存到items集合中，（即前台传递的sno sname spiture此时就保存在了item中）
						List<FileItem> items = upload.parseRequest(request);
						//遍历items中的数据（sno，sname，spicture）
						Iterator<FileItem> iter= items.iterator();//迭代器
						String cplatenum = null;
						String cname = null;
						String cbrand = null;
						InputStream file = null;
						while(iter.hasNext()) {
							FileItem item = iter.next();
							String itemName = item.getFieldName();
							//判断前端字段是普通form表单字段(sno,sname)还是文件字段
							
							if(item.isFormField()) {
								if(itemName.equals("cplatenum")) {//根据name属性判断item是sno sname 还是spiture
									cplatenum = item.getString("utf-8");
								}else if(itemName.equals("cname")) {
									cname= item.getString("utf-8");
								}else if(itemName.equals("cbrand")){
									cbrand = item.getString("utf-8");
								}else{
									System.out.println("文件字段");
								}
							}else {//spitre   文件上传处理
								 file = item.getInputStream();//将文件转为输入流
								}						
					}
						
						CarInfo carInfo = new CarInfo();
						carInfo.setCplatenum(cplatenum);;
						carInfo.setCname(cname);
						carInfo.setCphoto(file);
						carInfo.setCbrand(cbrand);
						
					   if(carInfoDao.addCarInfo(carInfo)) {
							try {
								response.getWriter().write("success");
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					  
					}catch(FileUploadBase.FileSizeLimitExceededException e){
						System.out.println("文件超出大小");
					}catch (FileUploadException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}finally {
						 carInfoDao.closeCon();
					}
				}	
				
}
	private void CarInfoList(HttpServletRequest request, HttpServletResponse response) {
	// TODO Auto-generated method stub
		try {
			request.getRequestDispatcher("view/CarInfoList.jsp").forward(request, response);
		} catch (ServletException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
			
			doGet(request, response);
	}

}
