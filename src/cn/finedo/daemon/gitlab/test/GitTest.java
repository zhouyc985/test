package cn.finedo.daemon.gitlab.test;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.ListBranchCommand;
import org.eclipse.jgit.api.RemoteListCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectLoader;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.notes.Note;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.transport.RemoteConfig;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.eclipse.jgit.treewalk.TreeWalk;

public class GitTest {
	
	public static void main(String[] args) {
		String gitroot="D:/java/repository/.git";
		String branch="master";
		String filename="src/cn/finedo/daemon/gitlab/Address.java";
		try {
			
			
			getHistoryInfo();
			
			String a=getContentWithFile(gitroot,branch,filename);
			System.out.println(a);
			
			List<String> b=getLocalBranchNames(gitroot);
			 for (String string : b) {
				System.out.println(string);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	 public static List<String> getLocalBranchNames(String localPath) throws IOException{
	        List<String> result=new LinkedList<String>();
	        Git git = Git.open(new File(localPath));
	        Map<String, Ref> map=git.getRepository().getAllRefs();
	        Set<String> keys=map.keySet();
	        for (String str : keys) {
	            if(str.indexOf("refs/heads")>-1){
	                String el=str.substring(str.lastIndexOf("/")+1, str.length());
	                result.add(el);
	            }
	        }
	        return result;
	    }
	
	
	  public static List<String> getRemotes(String localPath) throws IOException, GitAPIException{
	        Git git = Git.open(new File(localPath));
	        RemoteListCommand remoteListCommand=git.remoteList();
	        List<RemoteConfig> list=remoteListCommand.call();
	        List<String> result=new LinkedList<String>();
	        for (RemoteConfig remoteConfig : list) {
	            result.add(remoteConfig.getName());
	        }
	        return result;
	    }
	
	  //git@223.105.1.68:/home/git/repository/repository.git
	  static Git git;  
	    //历史记录  
	    public static void getHistoryInfo() {  
	        File gitDir = new File("D:/java/repository/.git");  
	     /*   CloneCommand cloneCommand = Git.cloneRepository();
	        cloneCommand.setURI("git@223.105.1.68:/home/git/repository/repository.git" );
	        cloneCommand.setCredentialsProvider( new UsernamePasswordCredentialsProvider( "zhouyc", "123456" ) );
	        Repository repository=cloneCommand.getRepository();*/
	            try {  
	            	CloneCommand cloneCommand = Git.cloneRepository();
	                if (git == null) {  

	                     git= cloneCommand.setURI("git@223.105.1.68:/home/git/repository/repository.git") //设置远程URI
	                             .setBranch("master") //设置clone下来的分支
	                             .setDirectory(new File("C:/Users/11095/Desktop/每日/test")) //设置下载存放路径
	                             .setCredentialsProvider( new UsernamePasswordCredentialsProvider( "zhouyc", "123456" ) ) //设置权限验证
	                             .call();
	                }
	                
	    			
	    			
	 			  try (Repository repository = git.getRepository()) {
	 		            try (Git git = new Git(repository)) {
	 		                List<Note> call = git.notesList().call();
	 		                System.out.println("Listing " + call.size() + " notes");
	 		                for (Note note : call) {
	 		                    System.out.println("Note: " + note + " " + note.getName() + " " + note.getData().getName() + "\nContent: ");

	 		                    // displaying the contents of the note is done via a simple blob-read
	 		                    ObjectLoader loader = repository.open(note.getData());
	 		                    loader.copyTo(System.out);
	 		                }
	 		            }
	 		        }

	                
	                
	                List<Ref> a=git.branchList().call();
	                Iterable<RevCommit> gitlog= git.log().call();  
	                for (RevCommit revCommit : gitlog) {  
	                    String version=revCommit.getName();//版本号  
	                    revCommit.getShortMessage();
	                    revCommit.getFullMessage();
	                    System.out.println(revCommit.getShortMessage()+"---"+revCommit.getFullMessage());
	                    revCommit.getAuthorIdent().getName();  
	                    revCommit.getAuthorIdent().getEmailAddress();  
	                    revCommit.getAuthorIdent().getWhen();//时间  
	                    System.out.println(version+"---"+revCommit.getAuthorIdent().getName()+"---"+ revCommit.getAuthorIdent().getEmailAddress()+"---"+ revCommit.getAuthorIdent().getName());  
	                }  
	            } catch (GitAPIException e) {  
	                e.printStackTrace();  
	            } catch (IOException e) {  
	                e.printStackTrace();  
	            }  
	    }  
	
	
	
	
	
	
	private static final String REF_REMOTES = null;

	/** 
	 * 获取指定分支、指定文件的内容 
	 * @param gitRoot git仓库目录 
	 * @param branchName 分支名称 
	 * @param fileName 文件名称 
	 * @return 
	 * @throws Exception 
	 */  
	public static String getContentWithFile(String gitRoot, final String branchName, String fileName)  
			throws Exception {  
		final Git git = Git.open(new File(gitRoot));  
		Repository repository = git.getRepository();  
		
		repository = git.getRepository();  
		RevWalk walk = new RevWalk(repository);

		Ref ref = repository.getRef(branchName);  
		if (ref == null) {  
			//获取远程分支  
			ref = repository.getRef(REF_REMOTES + branchName);  
		}  
		//异步pull  
		ExecutorService executor = Executors.newCachedThreadPool();  
		FutureTask<Boolean> task = new FutureTask<Boolean>(new Callable<Boolean>() {  
			@Override  
			public Boolean call() throws Exception {  
				/*//创建分支 
                CreateBranchCommand createBranchCmd = git.branchCreate(); 
                createBranchCmd.setStartPoint(REF_REMOTES + branchName).setName(branchName).call();*/  
				return git.pull().call().isSuccessful();  
			}  
		});  
		executor.execute(task);  

		ObjectId objId = ref.getObjectId();  
		RevCommit revCommit = walk.parseCommit(objId);  
		RevTree revTree = revCommit.getTree(); 
		TreeWalk treeWalk = TreeWalk.forPath(repository, fileName, revTree);  
		//文件名错误  
		if (treeWalk == null)  
			return null;  

		ObjectId blobId = treeWalk.getObjectId(0);  
		ObjectLoader loader = repository.open(blobId);  
		byte[] bytes = loader.getBytes();  
		if (bytes != null)  
			return new String(bytes);  
		return null;  
	}  
}
