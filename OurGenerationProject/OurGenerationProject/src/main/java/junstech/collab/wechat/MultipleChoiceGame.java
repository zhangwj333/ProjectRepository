package junstech.collab.wechat;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.google.gson.Gson;

import junstech.util.ENVConfig;
import junstech.util.FileUtil;
import junstech.util.RedisUtil;
import junstech.collab.BaseController;
import junstech.exception.BusinessException;
import junstech.model.Option;
import junstech.model.Question;
import junstech.model.TableProperty;

@Controller
public class MultipleChoiceGame extends BaseController {

	String resourceUrl = "multipleChoiceGame";

	@RequestMapping(value = "/RedirectToTest")
	public ModelAndView RedirectToTest(@RequestParam("type") String type, HttpServletRequest request,
			HttpSession session) throws Exception {
		ModelAndView mv = new ModelAndView();
		Matcher m = testTypePattern.matcher(type);
		if(m.find()){
			type = m.group(1);
		}
		System.out.println(type);
		mv.addObject("type", type);
		mv.setViewName("index");
		return mv;
	}

	@RequestMapping(value = "/GetResult")
	public ModelAndView MultipleChoiceGameResult(@RequestParam("type") String type,
			@RequestParam("answer") String answer, HttpServletRequest request, HttpSession session) throws Exception {
		ModelAndView mv = new ModelAndView();
		ArrayList<TableProperty> result = new ArrayList<TableProperty>();
		;
		Matcher m = resultPattern
				.matcher(FileUtil.getFileAsStringFromJunstech(ENVConfig.multipleChoiceGame, "calMethod.xml"));
		if (!m.find()) {
			throw new BusinessException();
		}
		String data = m.group(1);
		String[] calMethods = data.split("\r\n");
		String[] factors = null;
		String testType = "";
		for (String calMethod : calMethods) {
			String[] info = calMethod.split("~");
			if (info[0].equals(type)) {
				factors = info[2].split(",");
				testType = info[1];
			}
		}

		String[] answers = answer.split(",");
		if (factors.length > 0) {
			if ("single".equals(testType)) {
				for (String factor : factors) {
					int point = 0;
					for (int i = 0; i < answers.length; i++) {
						if (factor.equals(answers[i])) {
							point++;
						}
					}
					TableProperty tableProperty = new TableProperty();
					tableProperty.setKey(factor);
					tableProperty.setValue(point);
					result.add(tableProperty);
				}
			}
		}
		mv.addObject("result", result);
		Collections.sort(result);
		String finalResult = result.get(0).getKey() + result.get(1).getKey() + result.get(2).getKey(); 
		
		MappingJackson2JsonView json = new MappingJackson2JsonView();
		Gson gson = new Gson();
		String output = gson.toJson(mv.getModel());
		mv.addObject("dataSet", output);
		mv.setViewName("result");
		return mv;
	}

	@RequestMapping(value = "/MultipleChoiceGame")
	public ModelAndView MultipleChoiceGameRequest(@RequestParam("type") String type, HttpServletRequest request,
			HttpSession session) throws Exception {
		ModelAndView mv = new ModelAndView();

		List<Question> questions = new ArrayList<Question>();
		String testType = "test.xml";
		if (!type.isEmpty()) {
			testType = type;
		}
		String data = FileUtil.getFileAsStringFromJunstech(ENVConfig.multipleChoiceGame, testType);
		
		Matcher questionsMatcher = questionsPattern.matcher(data);
		if(questionsMatcher.find()){
			data = questionsMatcher.group(1);
		}	
		
		Matcher questionPerPageMatcher = questionPerPagePattern.matcher(data);
		if(questionPerPageMatcher.find()){
			mv.addObject("questionPerPage", questionPerPageMatcher.group(1));
		}	
		
		Matcher questionMatcher = null;
		boolean flag = true;
		int i = 1;
		while (flag) {
			questionPattern = Pattern.compile("<Question" + i + ">(.*)</Question" + i + ">", Pattern.DOTALL);
			questionMatcher = questionPattern.matcher(data);
			if (questionMatcher.find()) {
				String temp = questionMatcher.group(1);
				questions.add(parseQuestion(temp));
			} else {
				flag = false;
			}
			i++;
		}

		mv.addObject("questions", questions);
		MappingJackson2JsonView json = new MappingJackson2JsonView();
		Gson gson = new Gson();
		String output = gson.toJson(mv.getModel());
		mv.addObject("dataSet", output);
		mv.addObject("type", type);
		mv.setViewName("multipleChoiceGame");
		return mv;
	}

	public Question parseQuestion(String data) {
		Question question = new Question();
		Matcher titlenMatcher = titlePattern.matcher(data);
		Matcher optionMatcher = null;
		if (titlenMatcher.find()) {
			question.setTitle(titlenMatcher.group(1));
		}

		boolean flag = true;
		int i = 1;
		while (flag) {
			optionPattern = Pattern.compile("<Option" + i + ">(.*)</Option" + i + ">", Pattern.DOTALL);
			optionMatcher = optionPattern.matcher(data);
			if (optionMatcher.find()) {
				String temp = optionMatcher.group(1);
				question.addOption(parseOption(temp));
			} else {
				flag = false;
			}
			i++;
		}
		return question;
	}

	public Option parseOption(String data) {
		Option option = new Option();
		Matcher descriptionMatcher = descriptionPattern.matcher(data);
		Matcher valueMatcher = valuePattern.matcher(data);
		if (descriptionMatcher.find() && valueMatcher.find()) {
			option.setDescription(descriptionMatcher.group(1));
			option.setValue(valueMatcher.group(1));
		}
		return option;
	}

	private Pattern testTypePattern = Pattern.compile("(.*\\.xml*)",
			Pattern.DOTALL);
	private Pattern resultPattern = Pattern.compile("<MultipleChoiceResult>(.*)</MultipleChoiceResult>",
			Pattern.DOTALL);
	private Pattern questionsPattern = Pattern.compile("<MultipleChoice>(.*)</MultipleChoice>",
			Pattern.DOTALL);
	
	private Pattern questionPattern = null;
	private Pattern titlePattern = Pattern.compile("<Title>(.*)</Title>", Pattern.DOTALL);
	private Pattern optionPattern = null;
	private Pattern descriptionPattern = Pattern.compile("<Description>(.*)</Description>", Pattern.DOTALL);
	private Pattern valuePattern = Pattern.compile("<Value>(.*)</Value>", Pattern.DOTALL);
	private Pattern questionPerPagePattern = Pattern.compile("<QuestionPerPage>(.*)</QuestionPerPage>", Pattern.DOTALL);
}
