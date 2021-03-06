package cn.finedo.daemon.gitlab.util;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.gitlab.api.GitlabAPI;
import org.gitlab.api.models.GitlabSession;

import cn.finedo.common.non.NonUtil;
import cn.finedo.daemon.gitlab.BaselineExtends;
import cn.finedo.daemon.gitlab.Change;
import cn.finedo.daemon.gitlab.Compare;
import cn.finedo.daemon.gitlab.Diff;
import cn.finedo.daemon.gitlab.GitProjectInfo;
import cn.finedo.daemon.gitlab.Groups;
import cn.finedo.daemon.gitlab.Member;
import cn.finedo.daemon.gitlab.OkHttpUtil;
import cn.finedo.daemon.gitlab.domain.Branch;
import cn.finedo.daemon.gitlab.domain.GitlabLog;
import cn.finedo.daemon.gitlab.domain.GitlabLogDetail;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

public class GitlabUtil {
	private String url;
	private String password;
	private String username;
	private GitlabSession ga;
	private List<GitProjectInfo> allProjects;
	private Map<String,String> nameEmailMap = new HashMap<String,String>();
	private static SimpleDateFormat sdf = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");
	
	
	
	
	
	
	
	
	private String minTime;
	private String maxTime;
	private static Map<String, String> defaultfilters = new HashMap<String, String>();
	static {
		defaultfilters.put(".java", "java");
		defaultfilters.put(".c", "c");
		defaultfilters.put(".cs", ".net");
		defaultfilters.put(".cpp", "c++");
		defaultfilters.put(".py", "Python");
		defaultfilters.put(".php", "php");
		defaultfilters.put(".js", "javascript");
		defaultfilters.put(".aspx", ".net");
		defaultfilters.put(".css", "css");
		defaultfilters.put(".html", "html");
	}

	private static Logger logger = LogManager.getLogger();

	public GitlabUtil() {
	}


	public GitlabUtil(String month, String url, String username, String password)
			throws ParseException {
		super();
		this.url = url;
		this.password = password;
		this.username = username;
		List<Date> minMaxTime = getMinMaxTime(month);
		minTime = sdf.format(minMaxTime.get(0));
		maxTime = sdf.format(minMaxTime.get(1));
		try {
			ga = GitlabAPI.connect(url, username, password);
			nameEmailMap = getUserNameEmailMap();
		} catch (IOException e) {
			e.printStackTrace();
			logger.error("查询失败", e);
		}
	}

	// 获取明细，日志信息
	public Object[] achievelog(BaselineExtends basecase) throws Exception {
		List<GitlabLog> logs = new ArrayList<GitlabLog>();
		List<GitlabLogDetail> logDetails = new ArrayList<GitlabLogDetail>();
		//	for (GitProjectInfo gitProject : allProjects) {
		String pathid=basecase.getPath().substring(basecase.getPath().indexOf("/",8)+1);
		String groupid="";
		if(pathid.indexOf("/")>-1){
			groupid=pathid.substring(0,pathid.indexOf("/"));
		}else{
			groupid=pathid;
		}
		logger.debug("组id{}",groupid);
		List<Groups> groups=Groups(groupid);
		for (Groups group : groups) {
			String weburl = group.getWeb_url();
			String projectid = group.getId();
			try {
				if (!weburl.startsWith(basecase.getPath())) {
					continue;
				}
				List<Branch> branchs=branch(projectid);
				for (Branch branch : branchs) {
					List<GitProjectInfo> projectcommits= getBranchCommits(projectid,
							minTime, maxTime,branch.getName());
					logger.debug("集合大小{}",projectcommits.size());
					if (projectcommits.size() < 1) {
						continue;
					}
					for (int j = 0; j < projectcommits.size() ; j++) {
						GitlabLog log = new GitlabLog();
						String logId = UUID.randomUUID().toString();
						log.setStatid(logId);
						log.setProjectid(basecase.getProjectid());
						log.setFromversion(projectcommits.get(j).getId());
						log.setToversion(projectcommits.get(j).getId());
						log.setOpttime(dealDateFormat(projectcommits.get(j).getCreated_at()));
						log.setAddcount(0);// TODO
						log.setRemovecount(0);// TODO
						log.setAddress(basecase.getPath());
						log.setRealaddress(weburl);
						log.setTool("gitlab");
						log.setUsername(nameEmailMap.get(projectcommits.get(j).getCommitter_email())==null ? projectcommits.get(j).getAuthor_name():nameEmailMap.get(projectcommits.get(j).getCommitter_email()));
						log.setAuthor_email(projectcommits.get(j).getAuthor_email());
						log.setAuthor_name(projectcommits.get(j).getAuthor_name());
						log.setCommitid(projectcommits.get(j).getId());
						log.setShortid(projectcommits.get(j).getShort_id());
						log.setBranch(branch.getName());
						log.setMessage(projectcommits.get(j).getMessage());
						logs.add(log);
						//TODO
						//Compare compare = compare(projectid, projectcommits.get(j-1).getId(), projectcommits.get(j).getId());
						List<Diff> diff = compare(projectid, projectcommits.get(j).getId());
						if (NonUtil.isNon(diff)||diff.size() == 0) {
							continue;
						}
						for (Diff d : diff) {
							// {"add":1,"codetype":"ts","diffname":"webapp/src/app/shared/validators/tel.validator.ts","modify":0,"remove":1,"sub":0}
							Change change = this.readFile(d);
							log.setAddcount(log.getAddcount() + change.getAdd());
							log.setRemovecount(log.getRemovecount()
									+ change.getRemove());

							GitlabLogDetail gld = new GitlabLogDetail();
							gld.setDetailid(UUID.randomUUID().toString());
							gld.setStatid(logId);
							gld.setFilepath(change.getDiffname());
							gld.setFiletype(change.getCodetype());
							gld.setCodetype(defaultfilters.get("."
									+ change.getCodetype()) == null ? "其他"
											: defaultfilters.get("." + change.getCodetype()));
							gld.setAddcount(change.getAdd());
							gld.setRemovecount(change.getRemove());
							logDetails.add(gld);
						}
					}
				}
			} catch (Exception e) {
				logger.error("统计错误", e);
			}
		}
		Object[] objs = new Object[] { logs, logDetails };

		return objs;
	}

	/**
	 * month:'2018-02'
	 * 
	 * @param month
	 * @return
	 * @throws ParseException
	 */
	public List<Date> getMinMaxTime(String month) throws ParseException {
		List<Date> list = new ArrayList<Date>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String min = month + "-01 00:00:00";
		Date minTime = sdf.parse(min);
		list.add(minTime);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(minTime);
		calendar.add(Calendar.MONTH, 1);
		list.add(calendar.getTime());
		return list;
	}


	// 读文件
	private Change readFile(Diff diff) throws IOException {
		String d = diff.getNew_path();
		String codetype = d.substring(d.lastIndexOf(".") + 1);
		Change change = new Change();
		String str = diff.getDiff();
		int add = 0;
		int remove = 0;
		int modify=0;
		int index = str.indexOf("\n@@");
		if (index > 0) {
			int bodyIndex = str.indexOf("@@", index + 3) + 3;
			String body = str.substring(bodyIndex);
			String[] lines = body.split("\n");
			for (int i = 0; i < lines.length; i++) {
				if (lines[i].startsWith("+")) {
					add++;
				} else if (lines[i].startsWith("-")) {
					remove++;
				}else if(lines[i].startsWith("-")&&i+1<lines.length&&lines[i+1].startsWith("+")){
					modify++;
				}
			}
		}
		change.setDiffname(d);
		change.setModify(modify);
		change.setAdd(add);
		change.setRemove(remove);
		change.setCodetype(codetype);
		return change;

	}

	// 查询所有项目
	private List<GitProjectInfo> getAllProjects() {
		List<GitProjectInfo> allProjs = new ArrayList<GitProjectInfo>();
		List<GitProjectInfo> temp = new ArrayList<GitProjectInfo>();
		int page = 100000;
		for (int k = 0; k <= page + 1; k++) {
			temp = OkHttpUtil.httpGetJson(url
					+ "//api/v3/projects/all?per_page=100&page=" + k
					+ "&private_token=" + ga.getPrivateToken(),
					new TypeReference<List<GitProjectInfo>>() {
			});
			if (temp.isEmpty()) {
				break;
			}
			allProjs.addAll(temp);
		}
		return allProjs;
	}

	//查询所有用户信息
	private Map<String,String> getUserNameEmailMap() {
		Map<String,String> nameEmailMap = new HashMap<String,String>();

		List<GitProjectInfo> allUsers = new ArrayList<GitProjectInfo>();
		List<GitProjectInfo> temp = new ArrayList<GitProjectInfo>();
		int page = 100000;
		for (int k = 0; k <= page + 1; k++) {
			temp = OkHttpUtil.httpGetJson(url
					+ "//api/v3/users?per_page=100&page=" + k
					+ "&private_token=" + ga.getPrivateToken(),
					new TypeReference<List<GitProjectInfo>>() {
			});
			if (temp.isEmpty()) {
				break;
			}
			allUsers.addAll(temp);
		}

		for(GitProjectInfo project : allUsers){
			nameEmailMap.put(project.getEmail(), project.getUsername());
		}
		return nameEmailMap;
	}
	// 查询组中的人
	private List<Member> members(String groupsid) {
		List<Member> memberlist = OkHttpUtil.httpGetJson(url
				+ "//api/v3/groups/" + groupsid + "/members?private_token="
				+ ga.getPrivateToken(), new TypeReference<List<Member>>() {
		});
		return memberlist;
	}

	// 查询所有提交
	private List<GitProjectInfo> getAllCommits(String projectid, String min,
			String max) {
		String minTime = min.replace(" ", "T");
		String maxTime = max.replace(" ", "T");
		List<GitProjectInfo> list=new ArrayList<GitProjectInfo>();
		int page=10000;
		for (int k = 0; k <= page + 1; k++) {
			List<GitProjectInfo> projectcommits = OkHttpUtil.httpGetJson(url
					+ "//api/v3/projects/" + projectid
					+ "/repository/commits?since=" + minTime + "&until=" + maxTime
					+ "&per_page=100&page="+k+"&private_token=" + ga.getPrivateToken(),
					new TypeReference<List<GitProjectInfo>>() {
			});
			if (projectcommits.isEmpty()) {
				break;
			}
			list.addAll(projectcommits);
		}
		return list;
	}

	// 比对2个版本
	private Compare compare(String projectid, String fromVersion, String toVersion) {
		Compare compare = OkHttpUtil.httpGetJson(url + "//api/v3/projects/"
				+ projectid + "/repository/compare?from=" + fromVersion + "&to="
				+ toVersion + "&private_token=" + ga.getPrivateToken(),
				new TypeReference<Compare>() {
		});
		return compare;
	}
	//比较1个版本
	private List<Diff> compare(String projectid, String commitId) {
		List<Diff> diffs = OkHttpUtil.httpGetJson(url + "//api/v3/projects/"
				+ projectid + "/repository/commits/"+commitId+"/diff?private_token=" + ga.getPrivateToken(),
				new TypeReference<List<Diff>>() {
		});
		return diffs;
	}

	public static String dealDateFormat(String oldDateStr) throws ParseException{
		//此格式只有  jdk 1.7才支持  yyyy-MM-dd'T'HH:mm:ss.SSSXXX          
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");  //yyyy-MM-dd'T'HH:mm:ss.SSSZ
		Date  date = df.parse(oldDateStr);
		SimpleDateFormat df1 = new SimpleDateFormat ("EEE MMM dd HH:mm:ss Z yyyy", Locale.UK);
		Date date1 =  df1.parse(date.toString());
		DateFormat df2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		//  Date date3 =  df2.parse(date1.toString());
		return df2.format(date1);
	}

	//组下的项目
	private List<Groups> Groups(String groupid) {
		List<Groups> groups = OkHttpUtil.httpGetJson(url + "//api/v3/groups/"
				+ groupid + "/projects?private_token=" + ga.getPrivateToken(),
				new TypeReference<List<Groups>>() {
		});
		return groups;
	}


	//指定分支的提交
	private List<GitProjectInfo> getBranchCommits(String projectid, String min,
			String max,String branchid) {
		String minTime = min.replace(" ", "T");
		String maxTime = max.replace(" ", "T");
		List<GitProjectInfo> list=new ArrayList<GitProjectInfo>();
		int page=10000;
		for (int k = 0; k <= page + 1; k++) {
			List<GitProjectInfo> projectcommits = OkHttpUtil.httpGetJson(url
					+ "//api/v3/projects/" + projectid
					+ "/repository/commits?ref_name="+branchid+"&since=" + minTime + "&until=" + maxTime
					+ "&per_page=100&page="+k+"&private_token=" + ga.getPrivateToken(),
					new TypeReference<List<GitProjectInfo>>() {
			});
			if (projectcommits.isEmpty()) {
				break;
			}
			list.addAll(projectcommits);
		}
		return list;
	}


	//获取所有分支
	private List<Branch> branch(String projectid) {
		List<Branch> branch = OkHttpUtil.httpGetJson(url + "//api/v3/projects/"
				+ projectid + "/repository/branches?private_token=" + ga.getPrivateToken(),
				new TypeReference<List<Branch>>() {
		});
		return branch;
	}


}
