package cn.jas.fastdfs.fastdfs_client.web;

import cn.jas.fastdfs.fastdfs_client.utils.FastDFSFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import java.io.IOException;
import java.util.Map;

@Controller
public class TestController {
    private  static  final Logger log=LoggerFactory.getLogger(TestController.class);

    @RequestMapping("/")
    public  String  index(){
        return "index";
    }
    @RequestMapping("/api/isOK")
    @ResponseBody
    public  String   isOk(){
        return "isOk";
    }

    @RequestMapping("/api/upload")
    @ResponseBody
    public  String   upload_file(HttpServletRequest  request) throws IOException, ServletException {
        MultipartHttpServletRequest  request1=(MultipartHttpServletRequest) request;
        MultiValueMap<String, MultipartFile>  map=request1.getMultiFileMap();
        for(Map.Entry<String,MultipartFile> entry:map.toSingleValueMap().entrySet()){
            if("file".equals(entry.getKey())){
                MultipartFile  file=entry.getValue();
                String fileName=file.getOriginalFilename();
                String suffix=fileName.substring(fileName.indexOf(".")+1);
                try {
                    FastDFSFile  fastDFSFile=new FastDFSFile(file.getOriginalFilename(),file.getBytes(),suffix) ;
                    String  absolutePath=FileManager.upload_file(fastDFSFile);
                    return  absolutePath;
                }catch (Exception e){
                    log.error(e.getMessage());
                    e.printStackTrace();
                }
            }
        }
        return "upload is error,wait try it please.";
    }


/*    public static void main(String[] args) {
        String str ="aaa:bbb:ss";
        String [] ss=str.split(":",2);
        System.out.println(ss.length);
        for(String st:ss){
            System.out.println(st);
        }
    }*/
}
