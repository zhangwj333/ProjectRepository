package junstech.collab.wechat;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import junstech.util.FileUtil;
import junstech.collab.BaseController;
import junstech.model.Option;
import junstech.model.Question;



@Controller
public class MultipleChoiceGame extends BaseController{

	String resourceUrl = "multipleChoiceGame";
	
	@RequestMapping(value = "/MultipleChoiceGame")
	public ModelAndView androidUserLogin(@ModelAttribute String type,HttpServletRequest request, HttpSession session) throws Exception {
		ModelAndView mv = new ModelAndView();
		
		ArrayList<Question> questions = new ArrayList<Question>();
		
		String data = FileUtil.getFileAsString(resourceUrl + "/" + "test.xml");		
		Matcher questionMatcher = questionPattern.matcher(data);
		boolean flag = true;
		int i=1;
		while(flag){
			String temp = questionMatcher.group(i);
			if(temp != null && !temp.isEmpty()){
				questions.add(parseQuestion(temp));
				
			}else{
				flag = false;
			}
			
		}
		mv.addObject("questions", questions);
		mv.setViewName("multipleChoiceGame");
		return mv;
	}
	
	public Question parseQuestion(String data){
		Question question = new Question();
		Matcher titlenMatcher = titlePattern.matcher(data);
		question.setTitle(titlenMatcher.group(1));
		Matcher optionMatcher = optionPattern.matcher(data);
		boolean flag = true;
		int i=1;
		while(flag){
			String temp = optionMatcher.group(i);
			if(temp != null && !temp.isEmpty()){
				question.addOption(parseOption(temp));
			}else{
				flag = false;
			}
		}
		
		return question;
	}
	
	public Option parseOption(String data){
		Option option = new Option();
		Matcher descriptionMatcher = descriptionPattern.matcher(data);
		option.setDescription(descriptionMatcher.group(1));
		Matcher valueMatcher = valuePattern.matcher(data);
		option.setValue(valueMatcher.group(1));	
		return option;
	}
	
	private Pattern questionPattern = Pattern.compile("<Question>(.*)</Question>"); 
	private Pattern titlePattern = Pattern.compile("<Title>(.*)</Title>"); 
	private Pattern optionPattern = Pattern.compile("<Option>(.*)</Option>"); 
	private Pattern descriptionPattern = Pattern.compile("<Description>(.*)</Description>"); 
	private Pattern valuePattern = Pattern.compile("<Value>(.*)</Value>"); 
	
}
