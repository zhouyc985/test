package cn.finedo.daemon.gitlab.test;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Iterator;

import org.aspectj.apache.bcel.classfile.Constant;
import org.tmatesoft.svn.core.SVNCommitInfo;
import org.tmatesoft.svn.core.SVNDepth;
import org.tmatesoft.svn.core.SVNDirEntry;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNNodeKind;
import org.tmatesoft.svn.core.SVNRevisionProperty;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.auth.ISVNAuthenticationManager;
import org.tmatesoft.svn.core.internal.io.dav.DAVRepositoryFactory;
import org.tmatesoft.svn.core.internal.io.svn.SVNRepositoryFactoryImpl;
import org.tmatesoft.svn.core.internal.wc.DefaultSVNOptions;
import org.tmatesoft.svn.core.io.SVNRepository;
import org.tmatesoft.svn.core.io.SVNRepositoryFactory;
import org.tmatesoft.svn.core.wc.ISVNOptions;
import org.tmatesoft.svn.core.wc.SVNClientManager;
import org.tmatesoft.svn.core.wc.SVNStatus;
import org.tmatesoft.svn.core.wc.SVNStatusType;
import org.tmatesoft.svn.core.wc.SVNWCUtil;

import cn.finedo.common.date.DateUtil;

import com.alibaba.json.bvt.serializer.SimpleDataFormatSerializerTest;

public class Svntest {

	
	/*
	 * 此类用来显示版本库的树状结构。
	 * 此类用底层API（Low Level API）直接访问版本库。
	 * 此程序框架于1-5的示例（High Level API）稍有不同。
	 * */

	public static void main(String[] args) {


		//	    	初始化支持svn://协议的库。 必须先执行此操作。
		SVNRepositoryFactoryImpl.setup();

		/*
		 * 相关变量赋值
		 */
		String url = "svn://223.105.1.68/fapd/test";//https://jy-PC/svn/Myjy/
		String name = "osyunwei";
		String password = "123456";
		//定义svn版本库的URL。
		SVNURL repositoryURL = null;
		//定义版本库。
		SVNRepository repository = null;
		/*
		 * 实例化版本库类
		 * */
		try {
			//获取SVN的URL。
			repositoryURL=SVNURL.parseURIEncoded(url);
			//根据URL实例化SVN版本库。
			repository = SVNRepositoryFactory.create(repositoryURL);
		} catch (SVNException svne) {
			/*
			 * 打印版本库实例创建失败的异常。
			 */
			System.err
			.println("创建版本库实例时失败，版本库的URL是 ‘"
					+ url + "‘: " + svne.getMessage());
			System.exit(1);
		}

		/*
		 * 对版本库设置认证信息。
		 */
		ISVNAuthenticationManager authManager = SVNWCUtil.createDefaultAuthenticationManager(name, password);
		repository.setAuthenticationManager(authManager);

		/*
		 * 获得版本库的最新版本树
		 */
		long latestRevision = -1;
		try {
			latestRevision = repository.getLatestRevision();
			
		} catch (SVNException svne) {
			System.err
			.println("获取最新版本号时出错: "
					+ svne.getMessage());
			System.exit(1);
		}
		
		/*
		 * 上面的代码基本上是固定的操作。
		 * 下面的部门根据任务不同，执行不同的操作。
		 * */
		try {

			//打印版本库的根
			System.out.println("Repository Root: " + repository.getRepositoryRoot(true));
			//打印出版本库的UUID
			System.out.println("Repository UUID: " + repository.getRepositoryUUID(true));
			System.out.println("");
			//打印版本库的目录树结构
			listEntries(repository, "lib");
		} catch (SVNException svne) {
			System.err.println("打印版本树时发生错误: "
					+ svne.getMessage());
			System.exit(1);
		}
		
		System.exit(0);
	}


	
	
	
	/*
	 * 此函数递归的获取版本库中某一目录下的所有条目。
	 */
	public static void listEntries(SVNRepository repository, String path)
			throws SVNException {
		//获取版本库的path目录下的所有条目。参数－1表示是最新版本。
		Collection entries = repository.getDir(path, -1, null,(Collection) null);
		Iterator iterator = entries.iterator();
		while (iterator.hasNext()) {
			SVNDirEntry entry = (SVNDirEntry) iterator.next();
			System.out.println("entry"+entry);
			SimpleDateFormat sdf=new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
			String a=sdf.format(entry.getDate());
			String message=repository.getRevisionPropertyValue(entry.getRevision(), SVNRevisionProperty.LOG).toString();
			String msg=message.substring(message.indexOf("Message:")+8,message.length()-1).trim();
			System.out.println("message:"+message);
			System.out.println("msg"+msg);
			System.out.println(sdf.format(entry.getDate()));
			System.out.println(entry.getKind()+"--"+entry.getAuthor()+"--"+entry.getRevision()+"--"+entry.getName()+"--"+entry.getDate());
			System.out.println(entry.getCommitMessage()+"--"+entry.getPath()+"--"+entry.getRelativePath()+"--"+entry.getLock()+"--"+entry.getURL());
			System.out.println("/" + (path.equals("") ? "" : path + "/") + entry.getName());
			System.out.println(entry.getKind()+"-----"+SVNNodeKind.DIR);
			/*if (entry.getKind() == SVNNodeKind.DIR) {
				listEntries(repository, (path.equals("")) ? entry.getName(): path + "/" + entry.getName());
			}*/
		}
	}
}

