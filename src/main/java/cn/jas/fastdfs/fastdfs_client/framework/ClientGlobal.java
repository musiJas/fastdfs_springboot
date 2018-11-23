/**
* Copyright (C) 2008 Happy Fish / YuQing
*
* FastDFS Java Client may be copied only under the terms of the GNU Lesser
* General Public License (LGPL).
* Please visit the FastDFS Home Page http://www.csource.org/ for more detail.
**/

package cn.jas.fastdfs.fastdfs_client.framework;



import cn.jas.fastdfs.fastdfs_client.common.IniFileReader;
import cn.jas.fastdfs.fastdfs_client.common.MyException;
import cn.jas.fastdfs.fastdfs_client.utils.InitConfig;
import org.springframework.util.StringUtils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
* Global variables
* @author Happy Fish / YuQing
* @version Version 1.11
*/
public class ClientGlobal
{
	public static int g_connect_timeout; //millisecond
	public static int g_network_timeout; //millisecond
	public static String g_charset;
	public static int g_tracker_http_port;
	public static boolean g_anti_steal_token;  //if anti-steal token
	public static String g_secret_key;   //generage token secret key
	public static TrackerGroup g_tracker_group;
	
	public static final int DEFAULT_CONNECT_TIMEOUT = 5;  //second
	public static final int DEFAULT_NETWORK_TIMEOUT = 30; //second
	
	public static String DEFAULT_GROUP_NAME;//if groupname is null ,return deault groupname
	
	private ClientGlobal()
	{
	}

	/**
	 *  load  initconfig  varibles
	 * @param  config config
	 * */
	public  static  void  init(InitConfig config) throws MyException {
        String[] szTrackerServers;
        String[] parts;

        g_connect_timeout=config.getConnect_timeout();
        if (config.getConnect_timeout()<0) {
            g_connect_timeout = DEFAULT_CONNECT_TIMEOUT;
        }
        g_connect_timeout*=1000;
        g_network_timeout=config.getNetwork_timeout();
        if (config.getNetwork_timeout()<0) {
            g_network_timeout=DEFAULT_NETWORK_TIMEOUT;
        }
        g_network_timeout *= 1000;
        g_charset=config.getCharset();
        if (g_charset == null || g_charset.length() == 0) {
            g_charset = "ISO8859-1";
        }
        String args=config.getTracker_server();
        szTrackerServers=new String[args.split(",").length];
        szTrackerServers[0]=config.getTracker_server();
        if(args.indexOf(",")!=-1){
            szTrackerServers=args.split(",");
        }
        if (szTrackerServers == null)  {
             throw new MyException("item \"tracker_server\" in " + config.getTracker_server() + " not found");
        }
        InetSocketAddress[] tracker_servers = new InetSocketAddress[szTrackerServers.length];
        for (int i=0; i<szTrackerServers.length; i++) {
            parts = szTrackerServers[i].split("\\:", 2);
            if (parts.length != 2)
            {
                throw new MyException("the value of item \"tracker_server\" is invalid, the correct format is host:port");
            }
            tracker_servers[i] = new InetSocketAddress(parts[0].trim(), Integer.parseInt(parts[1].trim()));
        }
        g_tracker_group = new TrackerGroup(tracker_servers);
        g_tracker_http_port = config.getHttp_tracker_http_port();
        if(g_tracker_http_port<0){
            g_tracker_http_port=80;
        }
        g_anti_steal_token = config.isHttp_anti_steal_token();
        if (g_anti_steal_token){
            g_secret_key = config.getHttp_secret_key();
        }
        DEFAULT_GROUP_NAME = config.getDefault_group_name();
	}



	/**
	* load global variables
	* @param conf_filename config filename
	*/
	public static void init(String conf_filename) throws FileNotFoundException, IOException, MyException
	{
  		IniFileReader iniReader;
  		String[] szTrackerServers;
			String[] parts;
			
  		iniReader = new IniFileReader(conf_filename);

			g_connect_timeout = iniReader.getIntValue("connect_timeout", DEFAULT_CONNECT_TIMEOUT);
  		if (g_connect_timeout < 0)
  		{
  			g_connect_timeout = DEFAULT_CONNECT_TIMEOUT;
  		}
  		g_connect_timeout *= 1000; //millisecond
  		
  		g_network_timeout = iniReader.getIntValue("network_timeout", DEFAULT_NETWORK_TIMEOUT);
  		if (g_network_timeout < 0)
  		{
  			g_network_timeout = DEFAULT_NETWORK_TIMEOUT;
  		}
  		g_network_timeout *= 1000; //millisecond

  		g_charset = iniReader.getStrValue("charset");
  		if (g_charset == null || g_charset.length() == 0)
  		{
  			g_charset = "ISO8859-1";
  		}
  		
  		szTrackerServers = iniReader.getValues("tracker_server");
  		if (szTrackerServers == null)
  		{
  			throw new MyException("item \"tracker_server\" in " + conf_filename + " not found");
  		}
  		
  		InetSocketAddress[] tracker_servers = new InetSocketAddress[szTrackerServers.length];
  		for (int i=0; i<szTrackerServers.length; i++)
  		{
  			parts = szTrackerServers[i].split("\\:", 2);
  			if (parts.length != 2)
  			{
  				throw new MyException("the value of item \"tracker_server\" is invalid, the correct format is host:port");
  			}
  			
  			tracker_servers[i] = new InetSocketAddress(parts[0].trim(), Integer.parseInt(parts[1].trim()));
  		}
  		g_tracker_group = new TrackerGroup(tracker_servers);
  		
  		g_tracker_http_port = iniReader.getIntValue("http.tracker_http_port", 80);
  		g_anti_steal_token = iniReader.getBoolValue("http.anti_steal_token", false);
  		if (g_anti_steal_token)
  		{
  			g_secret_key = iniReader.getStrValue("http.secret_key");
  		}
  		DEFAULT_GROUP_NAME = iniReader.getStrValue("default_group_name");
	}
	
/**
* construct Socket object
* @param ip_addr ip address or hostname
* @param port port number
* @return connected Socket object
*/
	public static Socket getSocket(String ip_addr, int port) throws IOException
	{
		Socket sock = new Socket();
		sock.setSoTimeout(ClientGlobal.g_network_timeout);
		sock.connect(new InetSocketAddress(ip_addr, port), ClientGlobal.g_connect_timeout);
		return sock;
	}
	
/**
* construct Socket object
* @param addr InetSocketAddress object, including ip address and port
* @return connected Socket object
*/
	public static Socket getSocket(InetSocketAddress addr) throws IOException
	{
		Socket sock = new Socket();
		sock.setSoTimeout(ClientGlobal.g_network_timeout);
		sock.connect(addr, ClientGlobal.g_connect_timeout);
		return sock;
	}
	
	public static int getG_connect_timeout()
	{
		return g_connect_timeout;
	}
	
	public static void setG_connect_timeout(int connect_timeout)
	{
		ClientGlobal.g_connect_timeout = connect_timeout;
	}
	
	public static int getG_network_timeout()
	{
		return g_network_timeout;
	}
	
	public static void setG_network_timeout(int network_timeout)
	{
		ClientGlobal.g_network_timeout = network_timeout;
	}
	
	public static String getG_charset()
	{
		return g_charset;
	}
	
	public static void setG_charset(String charset)
	{
		ClientGlobal.g_charset = charset;
	}
	
	public static int getG_tracker_http_port()
	{
		return g_tracker_http_port;
	}
	
	public static void setG_tracker_http_port(int tracker_http_port)
	{
		ClientGlobal.g_tracker_http_port = tracker_http_port;
	}
	
	public static boolean getG_anti_steal_token()
	{
		return g_anti_steal_token;
	}
	
	public static boolean isG_anti_steal_token()
	{
		return g_anti_steal_token;
	}
	
	public static void setG_anti_steal_token(boolean anti_steal_token)
	{
		ClientGlobal.g_anti_steal_token = anti_steal_token;
	}
	
	public static String getG_secret_key()
	{
		return g_secret_key;
	}
	
	public static void setG_secret_key(String secret_key)
	{
		ClientGlobal.g_secret_key = secret_key;
	}
	
	public static TrackerGroup getG_tracker_group()
	{
		return g_tracker_group;
	}
	
	public static void setG_tracker_group(TrackerGroup tracker_group)
	{
		ClientGlobal.g_tracker_group = tracker_group;
	}

	public static String getDEFAULT_GROUP_NAME() {
		return DEFAULT_GROUP_NAME;
	}

	public static void setDEFAULT_GROUP_NAME(String dEFAULT_GROUP_NAME) {
		DEFAULT_GROUP_NAME = dEFAULT_GROUP_NAME;
	}
	
	
}
