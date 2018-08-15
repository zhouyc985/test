package cn.finedo.daemon.gitlab.test;

import java.io.File;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

public class Conle {
	public static void main(String[] args) {
		Conle gitUtil = new Conle();
	        //git远程url地址
	        String url = "git@223.105.1.68:/home/git/repository/repository.git";
	        String localPath = "C:/Users/11095/Desktop/test";
		String a=gitUtil.cloneRepository(url,localPath);
				System.out.println(a);
	}
	
	//克隆仓库
    public String cloneRepository(String url, String localPath) {
        try {
            System.out.println("开始下载......");

            Git git = Git.cloneRepository()
                    .setURI(url)
                    .setDirectory(new File(localPath))
                    .setCredentialsProvider(new UsernamePasswordCredentialsProvider("zhouyc","123456"))
                    .setCloneAllBranches(true)
                    .call();

            System.out.println("下载完成......");

            return "success";
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
    }
}
