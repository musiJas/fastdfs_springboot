package cn.jas.fastdfs.fastdfs_client.utils;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "fastdfs")
public class InitConfig {
        private  int  connect_timeout;
        private  int  network_timeout;
        private  String  charset;
        private  int  http_tracker_http_port;
        private  boolean  http_anti_steal_token;
        private  String  http_secret_key;
        private  String  tracker_server;
        private  String  default_group_name;
        protected  static  final  String  FILE_DEFAULT_HEIGHT="150";
        protected  static  final  String  FILE_DEFAULT_WIDTH="150";
        protected  static  final  String  FILE_DEFAULT_AUTHOR="fastdfs.";
        public static final String PROTOCOL = "http://";


    public InitConfig() {
    }

    public InitConfig(int connect_timeout, int network_timeout, String charset, int http_tracker_http_port, boolean http_anti_steal_token, String http_secret_key, String tracker_server, String default_group_name) {
        this.connect_timeout = connect_timeout;
        this.network_timeout = network_timeout;
        this.charset = charset;
        this.http_tracker_http_port = http_tracker_http_port;
        this.http_anti_steal_token = http_anti_steal_token;
        this.http_secret_key = http_secret_key;
        this.tracker_server = tracker_server;
        this.default_group_name = default_group_name;
    }

    public int getConnect_timeout() {
        return connect_timeout;
    }

    public void setConnect_timeout(int connect_timeout) {
        this.connect_timeout = connect_timeout;
    }

    public int getNetwork_timeout() {
        return network_timeout;
    }

    public void setNetwork_timeout(int network_timeout) {
        this.network_timeout = network_timeout;
    }

    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    public int getHttp_tracker_http_port() {
        return http_tracker_http_port;
    }

    public void setHttp_tracker_http_port(int http_tracker_http_port) {
        this.http_tracker_http_port = http_tracker_http_port;
    }

    public boolean isHttp_anti_steal_token() {
        return http_anti_steal_token;
    }

    public void setHttp_anti_steal_token(boolean http_anti_steal_token) {
        this.http_anti_steal_token = http_anti_steal_token;
    }

    public String getHttp_secret_key() {
        return http_secret_key;
    }

    public void setHttp_secret_key(String http_secret_key) {
        this.http_secret_key = http_secret_key;
    }

    public String getTracker_server() {
        return tracker_server;
    }

    public void setTracker_server(String tracker_server) {
        this.tracker_server = tracker_server;
    }

    public String getDefault_group_name() {
        return default_group_name;
    }

    public void setDefault_group_name(String default_group_name) {
        this.default_group_name = default_group_name;
    }
}
