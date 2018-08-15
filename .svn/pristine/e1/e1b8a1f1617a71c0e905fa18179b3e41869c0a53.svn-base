package cn.finedo.daemon.gitlab.count;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import cn.finedo.daemon.gitlab.Address;
import cn.finedo.daemon.gitlab.Basecase;
import cn.finedo.daemon.gitlab.BaselineExtends;
import cn.finedo.daemon.gitlab.ProjectCodeStat;
import cn.finedo.daemon.gitlab.ProjectCodeStatService;
import cn.finedo.daemon.gitlab.detail.GitlabDetailService;
import cn.finedo.daemon.gitlab.domain.GitlabLog;
import cn.finedo.daemon.gitlab.domain.GitlabLogDetail;
import cn.finedo.daemon.gitlab.util.GitlabCountUtil;
import cn.finedo.daemon.gitlab.util.GitlabUtil;

public class GitlabCountDaemon {
	private static Logger logger = LogManager.getLogger();
	private ApplicationContext springcontext;

	public GitlabCountDaemon() {
		springcontext = new ClassPathXmlApplicationContext("spring-context.xml");
	}

	@SuppressWarnings("unused")
	public static void main(String[] args) {
		GitlabCountDaemon statDeamon = new GitlabCountDaemon();
		try {
			statDeamon.gitlab();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("统计出错",e);
		}
	}

	private void gitlab() throws Exception {
		ProjectCodeStatService codeStatService = springcontext
				.getBean(ProjectCodeStatService.class);
		List<BaselineExtends> bes = codeStatService.getStatList();
		Properties prop = new Properties();

		// properties中格式用||区分2个地址如
		// url=http://10.130.15.11||http://10.130.15.11

		InputStream in = GitlabCountDaemon.class.getClassLoader().getResourceAsStream("gitlab2.properties");
		prop.load(in);
		List<Address> list = new ArrayList<Address>();
		String url = prop.getProperty("url").trim();
		String username = prop.getProperty("username").trim();
		String password = prop.getProperty("password").trim();
		String str1 = prop.getProperty("str1").trim();
		String str2 = prop.getProperty("str2").trim();
		String str3 = prop.getProperty("str3").trim();
		if (url.indexOf("||") > 0) {
			String[] urls = url.split("\\|\\|");
			String[] usernames = username.split("\\|\\|");
			String[] passwords = password.split("\\|\\|");
			for (int i = 0; i < urls.length; i++) {
				Address address = new Address();
				address.setUrl(urls[i]);
				address.setUsername(usernames[i]);
				address.setPassword(passwords[i]);
				address.setStr1(str1);
				address.setStr2(str2);
				address.setStr3(str3);
				list.add(address);
			}
		} else {
			Address address = new Address();
			address.setUrl(url);
			address.setUsername(username);
			address.setPassword(password);
			address.setStr1(str1);
			address.setStr2(str2);
			address.setStr3(str3);
			list.add(address);
		}

		for(BaselineExtends be : bes){
			try {
				if(be.getPath().indexOf(list.get(0).getStr1())>-1||be.getPath().indexOf(list.get(0).getStr2())>-1){
					if(be.getPath().indexOf(list.get(0).getStr2())>-1){
						be.setPath(be.getPath().replace(list.get(0).getStr2(), list.get(0).getStr1()));
					}
					GitlabCountUtil gitlab = new GitlabCountUtil(bes.get(0).getStatmonth(),list.get(0).getUrl(),list.get(0).getUsername(), list.get(0).getPassword());
					List<ProjectCodeStat> stats= gitlab.gitlabcount(be);
					codeStatService.add(stats);
				}
				if(be.getPath().indexOf(list.get(0).getStr3())>-1){
					GitlabCountUtil gitlab = new GitlabCountUtil(bes.get(0).getStatmonth(),list.get(1).getUrl(),list.get(1).getUsername(), list.get(1).getPassword());
					List<ProjectCodeStat> stats= gitlab.gitlabcount(be);
					codeStatService.add(stats);
				}

			} catch (Exception e) {
				logger.error("统计失败",e);
			}
		}
		System.exit(0);
	}
}
