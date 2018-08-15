package cn.finedo.daemon.gitlab.test.download;
import java.io.File;

import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.auth.ISVNAuthenticationManager;
import org.tmatesoft.svn.core.internal.io.svn.SVNRepositoryFactoryImpl;
import org.tmatesoft.svn.core.internal.wc.DefaultSVNOptions;
import org.tmatesoft.svn.core.io.SVNRepository;
import org.tmatesoft.svn.core.io.SVNRepositoryFactory;
import org.tmatesoft.svn.core.wc.ISVNOptions;
import org.tmatesoft.svn.core.wc.SVNClientManager;
import org.tmatesoft.svn.core.wc.SVNRevision;
import org.tmatesoft.svn.core.wc.SVNUpdateClient;
import org.tmatesoft.svn.core.wc.SVNWCUtil;



/**
 * 下载svn项目
 * @author yangqingxin
 *
 */
public class DownloadTheSVNProject {
	
	/**
	 * 下载文件
	 * @param svn 
	 * @param workPath
	 * @return file类型
	 */
	public static File DownloadSVNFile(SvnVo svn, String workPath){
		File file = null;
		//检查是否有文件
		File workfile = new File(workPath);
		if(!workfile.exists()){
			workfile.mkdir();
		}
		String workspace = workfile.getAbsolutePath();
		//创建svn链接
		SVNRepositoryFactoryImpl.setup();

		ISVNOptions options = SVNWCUtil.createDefaultOptions(true);
		// 实例化客户端管理类
		SVNClientManager ourClientManager = SVNClientManager.newInstance((DefaultSVNOptions) options, svn.getUserName(), svn.getPassWd());
		SVNURL repositoryURL;
		SVNRepository repository;
		try {
			repositoryURL = SVNURL.parseURIEncoded(svn.getCheckPath());
			//根据URL实例化SVN版本库。
			repository = SVNRepositoryFactory.create(repositoryURL);
			//工作区检出文件
			String curDir = workspace + File.separator + svn.getProjectName();
			file = new File(curDir);
			if(!file.exists()){
				file.mkdirs();
			}else{
				System.out.println("svn-checkout:存在原文件正在删除....");
				boolean flg = StringX.delFolder(file.getAbsolutePath());
				System.out.println("svn-checkout:删除源文件成功!!");
				if(flg){
					file.mkdirs();
				}
				
			}
			/*
			 * 对版本库设置认证信息。
			 */
			ISVNAuthenticationManager authManager = SVNWCUtil.createDefaultAuthenticationManager(svn.getUserName(), svn.getPassWd());
			repository.setAuthenticationManager(authManager);

			long latestRevision = repository.getLatestRevision();
			
			
			//根据创建参数文件 ，在ant打包时 使用这些参数生成文件
			Long ver = Long.parseLong(svn.getSvnVersion());
			SVNRevision version = SVNRevision.create(ver);
			System.out.println("开始检出svn-checkout:" + repositoryURL);
			System.out.println("svn-version:" + version);
			System.out.println("svn-file:" + file.getAbsolutePath());
			/*ReleaseVO rv = new ReleaseVO();
			rv.setReleaseVersion(String.valueOf(version));*/
			System.out.println("svn-checkout:正在检出...");
			//long s = SVNUtil.checkout(clientManager, repositoryURL, version, file, SVNDepth.INFINITY);
			 SVNUpdateClient updateClient = ourClientManager.getUpdateClient(); 
			 updateClient.setIgnoreExternals(false);
			// long s = updateClient.doCheckout(repositoryURL, file, version, version,SVNDepth.INFINITY, false);
			 long s = updateClient.doCheckout(repositoryURL, file, version,version, true,true);
			if(s!=0){
				System.out.println("svn-checkout:检出完成:"+s);
			}else{
				System.out.println("svn-checkout:检出未完成，请检查svn连接是否有误:"+s);
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return file;
	}
	
	 
	 public static void main(String[] args) {
		
		SvnVo svn = new SvnVo();
		//项目检出地址D:\360驱动大师目录\驱动备份目录
		String  workPath= "C:/Users/11095/Desktop";
		//svn上项目路径
		svn.setCheckPath("svn://223.105.1.68/fapd/test");
		//检出项目名称
		svn.setProjectName("test");
		//svn帐号密码
		svn.setUserName("zhouyc");
		svn.setPassWd("123456");
		//设置检出项目版本
		svn.setSvnVersion("10");
		//获取检出文件
		File file = DownloadSVNFile(svn, workPath);
		 
		
		
		
	 }
	
}
