package cn.jas.fastdfs.fastdfs_client.web;

import cn.jas.fastdfs.fastdfs_client.common.MyException;
import cn.jas.fastdfs.fastdfs_client.common.NameValuePair;
import cn.jas.fastdfs.fastdfs_client.framework.*;
import cn.jas.fastdfs.fastdfs_client.utils.FastDFSFile;
import cn.jas.fastdfs.fastdfs_client.utils.InitConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;

@Component
public class FileManager {
    static Logger logger = LoggerFactory.getLogger(FileManager.class);
    private static TrackerClient trackerClient;
    private static TrackerServer trackerServer;
    private static StorageServer storageServer;
    private static StorageClient storageClient;
    private static String DEFAULT_GROUP_NAME;//单点时默认groupname
    private static Object  obj=new Object();

    private  static InitConfig   config;
    @Autowired
    public  void  init(InitConfig config){
        FileManager.config=config;
    }
    public  static  void  ClientGlobalInit() throws MyException, IOException {
        ClientGlobal.init(config);
        trackerClient = new TrackerClient();
        trackerServer = trackerClient.getConnection();
        DEFAULT_GROUP_NAME = ClientGlobal.getDEFAULT_GROUP_NAME();
        storageClient = new StorageClient(trackerServer, storageServer);
    }
    public   static  void  init() throws IOException, MyException {
            if(trackerClient==null){
                synchronized (obj){
                    if(trackerClient==null){
                        ClientGlobalInit();
                    }
                }
            }
    }
    public  static  String   upload_file(FastDFSFile  file){
        long startTime = System.currentTimeMillis();
        String[] uploadResults = null;
        NameValuePair[] meta_list = new NameValuePair[3];
        meta_list[0] = new NameValuePair("width", file.getWidth());
        meta_list[1] = new NameValuePair("heigth", file.getWidth());
        meta_list[2] = new NameValuePair("author", file.getAuthor());
        try {
            init();
            uploadResults = storageClient.upload_file(file.getContent(), file.getExt(), meta_list);
        }catch (IOException e){
            logger.error("IO Exception when uploadind the file: "+file.getFileName(),e);
        }catch (Exception e){
            logger.error("Non IO Exception when uploadind the file: "+file.getFileName(), e);
            e.printStackTrace();
        }
        logger.info("upload_file time used: "+ (System.currentTimeMillis() - startTime) + " ms");
        if (uploadResults == null) {
            logger.error("upload file fail, error code: "+ storageClient.getErrorCode());
        }
        String groupName = uploadResults[0];
        String remoteFileName = uploadResults[1];
        String fileAbsolutePath = InitConfig.PROTOCOL
                + trackerServer.getInetSocketAddress().getHostName() + ":"
                + config.getHttp_tracker_http_port() + File.separator + groupName + File.separator
                + remoteFileName;
        logger.info("upload file successfully!!!  " + "group_name: "
                + groupName + ", remoteFileName:" + " " + remoteFileName);
        return fileAbsolutePath;
    }

    public  static  String  upload_file(byte[] content, String ext_name, NameValuePair [] pairs){
        long startTime = System.currentTimeMillis();
        String[] uploadResults = null;
        try {
            init();
            uploadResults = storageClient.upload_file(content, ext_name, pairs);
        }catch (IOException e){
            logger.error("IO Exception when uploadind the file: ",e);
        }catch (Exception e){
            logger.error("Non IO Exception when uploadind the file: ", e);
            e.printStackTrace();
        }
        logger.info("upload_file time used: "+ (System.currentTimeMillis() - startTime) + " ms");
        if (uploadResults == null) {
            logger.error("upload file fail, error code: "+ storageClient.getErrorCode());
        }
        String groupName = uploadResults[0];
        String remoteFileName = uploadResults[1];
        String fileAbsolutePath = InitConfig.PROTOCOL
                + trackerServer.getInetSocketAddress().getHostName() + ":"
                + config.getHttp_tracker_http_port() + File.separator + groupName + File.separator
                + remoteFileName;
        logger.info("upload file successfully!!!  " + "group_name: "
                + groupName + ", remoteFileName:" + " " + remoteFileName);
        return fileAbsolutePath;
    }

    /**
     *  获取文件详情
     * @param   groupName 组名
     * @param   remoteFileName  文件名
     * */
    public static FileInfo getFile(String groupName, String remoteFileName) {
        try {
            init();
            return storageClient.get_file_info(groupName, remoteFileName);
        } catch (IOException e) {
            logger.error("IO Exception: Get File from Fast DFS failed", e);
        } catch (Exception e) {
            logger.error("Non IO Exception: Get File from Fast DFS failed", e);
        }
        return null;
    }

    /**
     * 简单版
     * @param  remoteFileName 文件名称
     * **/
    public  static  FileInfo  getFile(String remoteFileName){
        try {
            init();
            return  storageClient.get_file_info(config.getDefault_group_name(),remoteFileName);
        } catch (IOException e) {
            logger.error("IO Exception: Get File from Fast DFS failed", e);
        } catch (Exception e) {
            logger.error("Non IO Exception: Get File from Fast DFS failed", e);
        }
        return null;
    }

    /**
     *  删除文件
     * @param   groupName 组名
     * @param   remoteFileName  文件名
     * @return   0  is  sccuess  non zero is fail
     * */
    public  static  int  deleteFile(String groupName,String remoteFileName) throws IOException, MyException {
        init();
        return storageClient.delete_file(groupName,remoteFileName);
    }

    /**
     * 删除文件简单版
     * */
    public  static  int deleteFile(String  remoteFileName) throws IOException, MyException {
        init();
        return  storageClient.delete_file(config.getDefault_group_name(),remoteFileName);
    }
    public static StorageServer[] getStoreStorages(String groupName) throws IOException, MyException {
        init();
        return trackerClient.getStoreStorages(trackerServer, groupName);
    }
    public static ServerInfo[] getFetchStorages(String groupName,String remoteFileName) throws IOException, MyException {
        init();
        return trackerClient.getFetchStorages(trackerServer, groupName,remoteFileName);
    }



}
